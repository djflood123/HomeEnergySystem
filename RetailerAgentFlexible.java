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

public class RetailerAgentFlexible extends Agent {
	private String serviceName = "";
	private List<AID> subscribers = new ArrayList<AID> ();
	
	protected void setup () {
		Object[] args = getArguments();
		serviceName = args[0].toString();

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
		
		//TODO get price entered from user
		private int price;
		private int qty;
		private double discount;
		
		public void action () {
			ACLMessage msg = receive();
			if (msg != null) {
								
				// Check if receiving a subscription message
				if (msg.getConversationId().equals("customer-subscription")) {
					subscribers.add(msg.getSender());
				}
				// Check if receiving a request message
				if (msg.getPerformative() == ACLMessage.CFP) {
					qty = Integer.parseInt(msg.getContent());
					
					System.out.println(getLocalName() + " received request message from " + msg.getSender().getName());
															
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(String.valueOf(price));
					myAgent.send(reply);
					
					System.out.println(getLocalName() + " sent offer price: " + price + " to " + msg.getSender().getName());
				}
				
				//Check if proposal is NOT accepted
				//If yes, send message back to home agent asking to enter new offer
				if (msg.getPerformative() != ACLMessage.ACCEPT_PROPOSAL) {
					System.out.println(getLocalName() + " rejected offer from " + msg.getSender().getName());			
					
					//receive new user entered price and give 10% discount
					discount = (rnd.nextInt(11) + 90) / 100;
					double newprice = price * discount;
					
						ACLMessage confirm = msg.createReply();
						confirm.setPerformative(ACLMessage.CONFIRM);
						confirm.setContent(String.valueOf(newprice * qty));
						myAgent.send(confirm);
						System.out.println(getLocalName() + " sent purchase confirm message to " + msg.getSender().getName());	
					
				}				
				
				// Check if receiving a accept message
				// If yes, then reply with a confirmation
				if (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
					System.out.println(getLocalName() + " received accept offer from " + msg.getSender().getName());
					ACLMessage confirm = msg.createReply();
					confirm.setPerformative(ACLMessage.CONFIRM);
					confirm.setContent(String.valueOf(price * qty));
					myAgent.send(confirm);
					System.out.println(getLocalName() + " sent purchase confirm message to " + msg.getSender().getName());
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
