package Project;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.TickerBehaviour;
import java.util.*;

public class PowerAgent extends Agent {
	

	private int powerGenerated;
	
	protected void setup () {
		// Only 1 argument is the local name of the home agent
		Object[] args = getArguments();
		
		addBehaviour(new TickerBehaviour(this, 10000) {
			protected void onTick() {
				Random rnd = new Random();
				powerGenerated = 4;
				
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(new AID(args[0].toString(), AID.ISLOCALNAME));
				msg.setContent(String.valueOf(powerGenerated));
				msg.setConversationId("application-generation");
				send(msg);
				
				System.out.println(getLocalName() + " sent power Generation to " + args[0].toString());
			}
		});
	}
}
