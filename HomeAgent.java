package Project;

import jade.core.Agent;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.proto.SubscriptionInitiator;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Iterator;
import java.util.*;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.MessageTemplate;

/**
 * This home agent subscribe for service (selling energy) in DF agent
 * Based on week 6 tutorial
 * And http://jade.tilab.com/doc/tutorials/JADEProgramming-Tutorial-for-beginners.pdf
 **/

public class HomeAgent extends Agent {
	private Map<AID, String> retailerList = new HashMap<AID, String>();
	private int usage;
	
	protected void setup () {
		
		String serviceName = "energy-selling";
		
		// Build the description used as template for the subscription
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription templateSd = new ServiceDescription();
		templateSd.setType(serviceName);
		template.addServices(templateSd);
		
		SearchConstraints sc = new SearchConstraints();
		// We want to receive 3 results at most
		sc.setMaxResults(new Long(3));
		
		// Start subscription for any retailers provide "energy-selling" service
		addBehaviour(new SubscriptionInitiator(this, DFService.createSubscriptionMessage(this, getDefaultDF(), template, sc)) {
			protected void handleInform(ACLMessage inform) {
				System.out.println("Agent " + getLocalName() + ": Notification received from DF");
				try {
					DFAgentDescription[] results = DFService.decodeNotification(inform.getContent());
					if (results.length > 0) {
						for (int i = 0; i < results.length; ++i) {
							DFAgentDescription dfd = results[i];
							AID retailer = dfd.getName();
							
							// In this case, every retailer agent will only have 1 service (only one element in the iterator)
							// Just looking for "energy-selling" service
							Iterator it = dfd.getAllServices();
							while (it.hasNext()) {
								ServiceDescription sd = (ServiceDescription)it.next();
								if (sd.getType().equals(serviceName)) {
									System.out.println("Agent offering Energy-Selling Service found");
									System.out.println("Service \"" + sd.getName() + "\" is provided by agent " + retailer.getName());
									
									// Add this provider to list
									retailerList.put(retailer, sd.getName());
									
									// Send a notification to let that retailer knows we subscribed
									ACLMessage noti = new ACLMessage(ACLMessage.INFORM);
									noti.addReceiver(retailer);
									noti.setContent("subscribed");
									noti.setConversationId("customer-subscription");
									send(noti);
								}
							}
						}
					}
					System.out.println();
				}
				catch (FIPAException fe) {
					fe.printStackTrace();
				}
			}
		});
		
		// Open message receive cyclic behaviour to receive message match id "application-usage" from application agents
		// Or match id "retailer-terminated" from retailer agents
		addBehaviour(new CyclicBehaviour() {
			private int appCnt = 0;
			
			public void action() {
				MessageTemplate msgTmp = MessageTemplate.or(MessageTemplate.MatchConversationId("application-usage"),
										MessageTemplate.MatchConversationId("retailer-terminated"));
				ACLMessage msg = receive(msgTmp);
				if (msg != null) {
					if (msg.getConversationId().equals("application-usage")) {
						// We got 3 application agents, so we check if we get all the usage from 3 agents or not
						// If yes, then the next time usage will be reset
						if (appCnt == 3) {
							appCnt = 0;
							usage = 0;
						}
						System.out.println(msg.getSender().getLocalName() + " " + msg.getContent());
						usage += Integer.parseInt(msg.getContent());
						appCnt++;
					}
					else {
						// This case must be the termination message from retailer agents
						// Remove the sender retailer from list
						if (retailerList.containsKey(msg.getSender())) { // If that sender actually in our list
							retailerList.remove(msg.getSender());
							System.out.println(msg.getSender().getLocalName() + " " + msg.getContent());
						}
					}
				}
				else {
					block();
				}
			}
		});
		
		// Start an energy buying process every 15s
		addBehaviour(new TickerBehaviour(this, 15000) {
			protected void onTick() {
				if (!retailerList.isEmpty()) {
					myAgent.addBehaviour(new StartBuyingRequest());
				}
				else {
					System.out.println("Cannot find any retailers!");
				}
			}
		});
	}
	
	// This class handle the whole buying request process
	private class StartBuyingRequest extends Behaviour {
		private AID bestRetailer;
		private int bestPrice;
		private int responsesCnt = 0;
		private MessageTemplate mt;
		private int step = 0;
		private int buyingQty = usage;
		
		public void action() {
			switch (step) {
			case 0:
				System.out.println(getLocalName() + " sent requests to retailer agents");
				// Send the cfp message to all retailers
				ACLMessage cfpMsg = new ACLMessage(ACLMessage.CFP);
				for (AID retailer : retailerList.keySet()) {
					cfpMsg.addReceiver(retailer);
				}
				cfpMsg.setContent(String.valueOf(buyingQty));
				cfpMsg.setConversationId("energy-trade");
				cfpMsg.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value
				myAgent.send(cfpMsg);
				// Prepare the message template to get proposals
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("energy-trade"),
										MessageTemplate.MatchInReplyTo(cfpMsg.getReplyWith())); // detect different retailers' reply by unique generated value before with time stamp
				step = 1;
				break;
			case 1:
				// Receive all the proposals from retailer agents
				// We assume that all retailers are happy to propose their products (No point to refuse customers)
				ACLMessage response = myAgent.receive(mt);
				if (response != null) {
					// Response received
					if(response.getPerformative() == ACLMessage.PROPOSE) {
						// Extract this offer information
						int price = Integer.parseInt(response.getContent());
						if (bestRetailer == null || price < bestPrice) {
							// This is the best offer at the moment
							bestPrice = price;
							bestRetailer = response.getSender();
						}
						System.out.println(getLocalName() + " received offer from " + response.getSender().getLocalName() + " with price: " + response.getContent());
					}
					responsesCnt++;
					// Check if receive all responses
					if (responsesCnt == retailerList.size()) {
						// Start the next step
						step = 2;
					}
					
					/** Do something when the best price is not even acceptable
					*	Then set the step = 2 (the previous step = 2 is for basic implementation, further improvement might change it)
					*	Might need more than 4 step as initial setup
					*	The pricing strategies will start from here
					*/
				}
				else {
					block();
				}
				break;
			case 2:
				// Send the purchase order to the best retailer
				ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				order.addReceiver(bestRetailer);
				order.setContent(String.valueOf(buyingQty));
				order.setConversationId("energy-trade");
				order.setReplyWith("order" + System.currentTimeMillis());
				myAgent.send(order);
				// Set a new template to get the purchase order reply
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("energy-trade"), 
										MessageTemplate.MatchInReplyTo(order.getReplyWith()));
				step = 3;
				
				System.out.println(getLocalName() + " accept the offer of " + bestRetailer.getLocalName());

				break;
			case 3:
				// Receive the purchase confirmation from the retailer and finish the trade
				response = myAgent.receive(mt);
				if (response != null) {
					// The response message received
					if (response.getPerformative() == ACLMessage.CONFIRM) {
						// Purchased successfully
						System.out.println(buyingQty + " energy units have been successfully bought from " + response.getSender().getName());
						System.out.println("Paid " + response.getContent());
					}
					step = 4;
				}
				else {
					block();
				}
				break;
			}
		}
		
		@Override
		public boolean done() {
			return step == 4;
		}
	}
}
