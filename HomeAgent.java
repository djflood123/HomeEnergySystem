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
	private int fundings;
	private int tradeUpdate;
	
	protected void setup () {
		Object[] args = getArguments();
		String fundingsString = args[0].toString();
		fundings = Integer.parseInt(fundingsString);
		String tradeUpdateString = args[1].toString();
		tradeUpdate = Integer.parseInt(tradeUpdateString);
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
						// We  now got 4 application agents, so we check if we get all the usage from 4 agents or not
						// Which are 3 usage and 1 generation
						// If yes, then the next time usage will be reset
						/*if (appCnt == 3) {
							appCnt = 0;
							usage = 0;
						}*/
						System.out.println(msg.getSender().getLocalName() + " used " + msg.getContent() + " units of power");
						usage += Integer.parseInt(msg.getContent());
						//appCnt++;
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
		addBehaviour(new TickerBehaviour(this, tradeUpdate) {
			protected void onTick() {
				if (!retailerList.isEmpty()) {
					if(usage > 0){
						myAgent.addBehaviour(new StartBuyingRequest());
					}
				}
				else {
					System.out.println("Cannot find any retailers!");
				}
			}
		});
		
		// Earn the setting income every 1 minute
		addBehaviour(new TickerBehaviour(this, 60000) {
			protected void onTick() {
				fundings += Integer.parseInt(fundingsString); 
				System.out.println("Monthly income has arrived. Earned: " + fundings);
			}
		});
	}
	
	// This class handle the whole buying request process
	private class StartBuyingRequest extends Behaviour {
		private AID bestRetailer;
		private int bestPrice = 0;
		private int responsesCnt = 0;
		private MessageTemplate mt;
		private int step = 0;
		private int buyingQty = usage;
		
		public void action() {
			//now we have a funding check, if fundings higher than 1000, it will keep the regular buy, but if not
			//it will try to ask for a better price
			
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
                            if(bestRetailer == null && bestPrice==0){
                                bestPrice = price;
                                bestRetailer = response.getSender();
                            }
                            
							else if (price < bestPrice) {
								// This is the best offer at the moment
								bestPrice = price;
								bestRetailer = response.getSender();
							}
							
						}
						responsesCnt++;
						// Check if receive all responses
						if (responsesCnt == retailerList.size()) {
							if(fundings > usage *bestPrice) {
								step = 3;
								System.out.println(getLocalName() + " received offer from " + bestRetailer.getLocalName() + " with price: " + bestPrice);
								responsesCnt = 0;
							}
							else {
								step = 2;
								System.out.println("We don't have enough fundings( " + fundings + " ) to buy the power. Start a negotation.");
								responsesCnt = 0;
							}
						}

					}
					else {
						block();
					}
					break;
				case 2:
					// a genotation step for get a better price
					System.out.println(getLocalName() + " sent negotiations requests to retailer agents because our fundings are low");
					// Send the cfp message to all retailers
					ACLMessage cfpMsg1 = new ACLMessage(ACLMessage.REQUEST);
					for (AID retailer : retailerList.keySet()) {
						cfpMsg1.addReceiver(retailer);
					}
					cfpMsg1.setContent(String.valueOf(buyingQty));
					cfpMsg1.setConversationId("discount-energy-trade"); // we use new id ask for a better price
					cfpMsg1.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value
					myAgent.send(cfpMsg1);
					// Prepare the message template to get proposals
					mt = MessageTemplate.and(MessageTemplate.MatchConversationId("discount-energy-trade"),
											MessageTemplate.MatchInReplyTo(cfpMsg1.getReplyWith())); // detect different retailers' reply by unique generated value before with time stamp
					
					ACLMessage response1 = myAgent.receive(mt);
					if (response1 != null) {
						// Response received
						if(response1.getPerformative() == ACLMessage.AGREE) {
							// Extract this offer information
							int price = Integer.parseInt(response1.getContent());
							if (bestRetailer == null || price < bestPrice) {
								// This is the best offer at the moment
								bestPrice = price;
								bestRetailer = response1.getSender();
							}
							System.out.println("a agreement received:" + response1.getSender().getLocalName() + " with the price - " + price);
						}
						
						if(response1.getPerformative() == ACLMessage.REFUSE) {
							System.out.println("a DISagreement received:" + response1.getSender().getLocalName() );
						}
						
						responsesCnt++;
						// Check if receive all responses
						if (responsesCnt == retailerList.size()) {
							// Start the loop check
							if(fundings > usage *bestPrice) {
								step = 3;
								System.out.println(getLocalName() + " received offer from " + bestRetailer.getLocalName() + " with price: " + bestPrice);
								responsesCnt =0;
							}
							else {
								step = 5;
								System.out.println("We don't have enough funding in " + fundings +" The negotation is unsuccessful. ");
								responsesCnt =0;
							}
						}
						
					}else {
							
							System.out.println("the negotation has some error, try negotation again");
							step = 2;
							responsesCnt =0;
						}
						break;
				case 3:
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
					step = 4;
					
					System.out.println(getLocalName() + " accept the offer of " + bestRetailer.getLocalName());

					break;
				case 4:
					// Receive the purchase confirmation from the retailer and finish the trade
					response = myAgent.receive(mt);
					if (response != null) {
						// The response message received
						if (response.getPerformative() == ACLMessage.CONFIRM) {
							// Purchased successfully
							System.out.println(buyingQty + " energy units have been successfully bought from " + response.getSender().getName());
							System.out.println("Paid " + response.getContent());
							fundings -=  Integer.parseInt(response.getContent()) ;
							System.out.println("funding pool only: "+ fundings + " left.");
						}
						step = 5;
						usage = 0;
					}
					else {
						block();
					}
					break;
				}
		}
		
		@Override
		public boolean done() {
			return step == 5;
		}
	}
}
