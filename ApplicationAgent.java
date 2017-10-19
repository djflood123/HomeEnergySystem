package Project;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.TickerBehaviour;
import java.util.*;

public class ApplicationAgent extends Agent {
	
	private int powerUsage;
	private int powerGenerated;
	
	protected void setup () {
		// Only 1 argument is the local name of the home agent
		Object[] args = getArguments();
		
		addBehaviour(new TickerBehaviour(this, 10000) {
			protected void onTick() {
				Random rnd = new Random();
				powerUsage = rnd.nextInt(5) + 2;
				powerGenerated = 0;
				
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(new AID(args[0].toString(), AID.ISLOCALNAME));
				msg.setContent(String.valueOf(powerUsage));
				msg.setConversationId("application-usage");
				send(msg);
				
				System.out.println(getLocalName() + " sent power usage to " + args[0].toString());
			}
		});
	}
}
