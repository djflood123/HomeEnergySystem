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
					
					//System.out.println(getLocalName() + " received request message from " + msg.getSender().getName());
					
					// Create a propose reply message that content the price
					Random rnd = new Random();
					price = rnd.nextInt(maxPrice - minPrice + 1) + minPrice; // random number from 20 to 100
					
									
					
					//Send proposal back to home agent
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(String.valueOf(price));
					myAgent.send(reply);
					
					//System.out.println(getLocalName() + " sent offer price: " + price + " to " + msg.getSender().getName());
				}
				
				// CHECK IF receive a discount negotation 
				if (msg.getPerformative() == ACLMessage.REQUEST) {
					
					//System.out.println(getLocalName() + " received negotation request message for better price from " + msg.getSender().getName());
					
					//do some with it	
					//Send proposal back to home agent
					ACLMessage replyfornegotation = msg.createReply();
					replyfornegotation.setPerformative(ACLMessage.REFUSE);   //this one gives a discount, so it write agree, otherwise just write 'REFUSE'
					replyfornegotation.setContent(String.valueOf(price));
					myAgent.send(replyfornegotation);
					
					//System.out.println(getLocalName() + " sent price: " + price + " to " + msg.getSender().getName());
				}
				
				
				
				
				// Check if receiving a accept message
				// If yes, then reply with a confirmation
				if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
					//System.out.println(getLocalName() + " received accept offer from " + msg.getSender().getName());
					ACLMessage confirm = msg.createReply();
					confirm.setPerformative(ACLMessage.CONFIRM);
					confirm.setContent(String.valueOf(price * qty));
					myAgent.send(confirm);
					//System.out.println(getLocalName() + " sent purchase confirm message to " + msg.getSender().getName());
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
