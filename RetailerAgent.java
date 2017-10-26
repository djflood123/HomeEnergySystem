package Project;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.CyclicBehaviour;
import java.util.*;
/**
 * This retailer agent register for service (selling energy) in DF agent
 * Based on week 6 tutorial
 * And http://jade.tilab.com/doc/tutorials/JADEProgramming-Tutorial-for-beginners.pdf
 **/

public class RetailerAgent extends Agent {
	private String serviceName = "";
	private List<AID> subscribers = new ArrayList<AID> ();
	private int minPrice;
	private int maxPrice;
	protected void setup () {
		Object[] args = getArguments();
		serviceName = args[0].toString();
		String minPriceString = args[1].toString();
		minPrice = Integer.parseInt(minPriceString);
		String maxPriceString = args[2].toString();
		maxPrice = Integer.parseInt(maxPriceString);

		// Description of service to be registered
		ServiceDescription sd = new ServiceDescription();
		sd.setType("energy-selling");
		sd.setName(serviceName);
		register(sd);
		
		// Add behaviour that receives messages and reply
		addBehaviour(new RequestProcessingServer());
	}
	
	// Method to register the service
	void register(ServiceDescription sd) {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd); // An agent can register one or more service
		
		// Register the agent and its services
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}
	
	private class RequestProcessingServer extends CyclicBehaviour {
		private int price;
		private int qty;
		private int discount = 0;
		
		public void action () {
			ACLMessage msg = receive();
			if (msg != null) {
				// Check if receiving a subscription message
				//Home agent should send message "energy-trade"
				if (msg.getConversationId().equals("energy-trade")) {
					//We add the sender(Home Agent) as a subscriber
					subscribers.add(msg.getSender());
				}
				// Check if receiving a request message
				if (msg.getPerformative() == ACLMessage.CFP) {
					qty = Integer.parseInt(msg.getContent());
										
					// Create a propose reply message that content the price
					Random rnd = new Random();
					price = rnd.nextInt(maxPrice - minPrice + 1) + minPrice; // random number from 20 to 100				
					discount = price;
					
					//Send proposal back to home agent
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(String.valueOf(price));
					myAgent.send(reply);
					
				}
				
				// CHECK IF receive a discount request 
				if (msg.getPerformative() == ACLMessage.REQUEST) {
					
					//Send proposal back to home agent
					ACLMessage replyForDiscount = msg.createReply();
					
					// Discount only once for every cycle (20% off)
					if (discount == price) {
						discount = (int) (discount * 0.8);
						replyForDiscount.setPerformative(ACLMessage.AGREE); // Set performative to AGREE when discount
					} else {
						replyForDiscount.setPerformative(ACLMessage.REFUSE); // Set performative to REFUSE when not discount
					}
					
					replyForDiscount.setContent(String.valueOf(discount));
					myAgent.send(replyForDiscount);
				}
				
				// Check if receiving a accept message
				// If yes, then reply with a confirmation
				if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
					ACLMessage confirm = msg.createReply();
					confirm.setPerformative(ACLMessage.CONFIRM);
					confirm.setContent(String.valueOf(discount * qty)); // If discount is request, then use discount. Else discount will be equal to price
					myAgent.send(confirm);
				}
				
			}
			else {
				block();
			}
		}
	}
		
	// Method to send terminated notification to subscribers and de-register the service (on take down)
	protected void takeDown() {
		try {
			// Send notification
			ACLMessage noti = new ACLMessage(ACLMessage.INFORM);
			noti.setConversationId("retailer-terminated");
			noti.setContent("went bankrupt");
			for (AID receiver: subscribers) {
				noti.addReceiver(receiver);
			}
			send(noti);
			
			// De-register from DF agent
			DFService.deregister(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(getAID().getName() + " terminated");
	}
}
