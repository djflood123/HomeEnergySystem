package Project;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.TickerBehaviour;
import java.util.*;

public class ApplicationAgent extends Agent {
	
	private int powerMinUsage;
	private int powerMaxUsage;
	private int powerUpdate;
	
	protected void setup () {
		// Only 1 argument is the local name of the home agent
		Object[] args = getArguments();
		String powerMinUsageString = args[1].toString();
		powerMinUsage = Integer.parseInt(powerMinUsageString);
		String powerMaxUsageString = args[2].toString();
		powerMaxUsage = Integer.parseInt(powerMaxUsageString);
		String powerUpdateString = args[3].toString();
		powerUpdate = Integer.parseInt(powerUpdateString);
		
		addBehaviour(new TickerBehaviour(this, powerUpdate) {
			protected void onTick() {
				Random rnd = new Random();
				 int powerUsage = rnd.nextInt(powerMaxUsage - powerMinUsage + 1) + powerMinUsage;
				
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(new AID(args[0].toString(), AID.ISLOCALNAME));
				msg.setContent(String.valueOf(powerUsage));
				msg.setConversationId("application-usage");
				send(msg);
				
				//System.out.println(getLocalName() + " sent power usage to " + args[0].toString());
			}
		});
	}
}
