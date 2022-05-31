/**
 * ***************************************************************
 * JADE - Java Agent DEvelopment Framework is a framework to develop
 * multi-agent systems in compliance with the FIPA specifications.
 * Copyright (C) 2000 CSELT S.p.A.
 * 
 * GNU Lesser General Public License
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **
  */
package examples.clips;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.proto.ContractNetInitiator;
import jade.domain.FIPANames;

import java.util.Date;
import java.util.Vector;
import java.util.Enumeration;

/**
   This example shows how to implement the initiator role in 
   a FIPA-contract-net interaction protocol. In this case in particular 
   we use a <code>ContractNetInitiator</code>  
   to assign a dummy task to the agent that provides the best offer
   among a set of agents (whose local
   names must be specified as arguments).
   @author Giovanni Caire - TILAB
 */
 
public class ContractNetInitiatorAgenteComprador extends Agent {
	private int nResponders;
	
	protected void setup() { 
  	Object[] args = getArguments();
  	if (args != null && args.length > 0) {
  		nResponders = args.length-1;
		printlogo();
  		System.out.println("AgenteComprador "+getLocalName()+": Trying to find "+ args[0]+" to one out of "+nResponders+" responders.");
  		
  		// Fill the CFP message
  		ACLMessage msg = new ACLMessage(ACLMessage.CFP);
  		for (int i = 1; i < args.length; ++i) {
  			msg.addReceiver(new AID((String) args[i], AID.ISLOCALNAME));
  		}
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
			// We want to receive a reply in 10 secs
			msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
			msg.setContent(args[0].toString());
			
			addBehaviour(new ContractNetInitiator(this, msg) {
				
				protected void handlePropose(ACLMessage propose, Vector v) {
					System.out.println("AgenteComprador "+getLocalName()+": AgenteVendedor "+propose.getSender().getName()+" propuso "+propose.getContent());
				}
				
				protected void handleRefuse(ACLMessage refuse) {
					System.out.println("AgenteComprador "+getLocalName()+": AgenteVendedor "+refuse.getSender().getName()+" rechazoo");
				}
				
				protected void handleFailure(ACLMessage failure) {
					if (failure.getSender().equals(myAgent.getAMS())) {
						// FAILURE notification from the JADE runtime: the receiver
						// does not exist
						System.out.println("AgenteComprador "+getLocalName()+": Responder does not exist");
					}
					else {
						System.out.println("AgenteComprador "+getLocalName()+": Agent "+failure.getSender().getName()+" failed");
					}
					// Immediate failure --> we will not receive a response from this agent
					nResponders--;
				}
				
				protected void handleAllResponses(Vector responses, Vector acceptances) {
					if (responses.size() < nResponders) {
						// Some responder didn't reply within the specified timeout
						System.out.println("AgenteComprador "+getLocalName()+": Timeout expired: missing "+(nResponders - responses.size())+" responses");
					}
					int mejor_oferta = 55000;
					AID mejoroferente = null;
					ACLMessage aceptar = null;
					Enumeration e = responses.elements();
					while (e.hasMoreElements()) {
						ACLMessage msg = (ACLMessage) e.nextElement();
						if (msg.getPerformative() == ACLMessage.PROPOSE) {
							ACLMessage reply = msg.createReply();
							reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
							acceptances.addElement(reply);
							String[] arr_resultado = msg.getContent().split("\\+");
							int precio = Integer.parseInt(arr_resultado[0]);
							int msis = Integer.parseInt(arr_resultado[1]);
							String banco = arr_resultado[2];
							System.out.println("AgenteComprador "+getLocalName()+": Recibio de "+msg.getSender()+" el precio por $"+precio);
							if(msis>1)
								System.out.println("AgenteComprador "+getLocalName()+": Promocion vigente:"+msis+" meses sin intereses T.C. "+banco);
							int propuesta = precio/msis;
							if (propuesta < mejor_oferta) {
								mejor_oferta = propuesta;
								mejoroferente = msg.getSender();
								aceptar = reply;
							}
						}
					}
					if (aceptar != null) {
						System.out.println("AgenteComprador "+getLocalName()+": Accepting proposal "+mejor_oferta+" from responder "+mejoroferente.getName());
						aceptar.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
					}						
				}
				
				protected void handleInform(ACLMessage inform) {
					System.out.println("AgenteComprador "+getLocalName()+": AgenteVendedor "+inform.getSender().getName()+" completaron compra y venta "+args[0]);
					if(inform.getContent()!="na")
						System.out.println("Promocion vigente: "+inform.getContent());
				}
			} );
  	}
  	else {
  		System.out.println("AgenteComprador "+getLocalName()+": Necesita argumento 2 con nombre de agente vendedor");
  	}
  }
    private void printlogo() {
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("");
		System.out.println("                          ******************************************************");
		System.out.println("                          *                                                    *");
		System.out.println("                          *          JJ        AAA        DDDD     EEEEEE      *");
		System.out.println("                          *          JJ       AA AA       DD DDD   EE          *");
		System.out.println("                          *          JJ      AA   AA      DD  DDD  EE          *");
		System.out.println("                          *     JJ   JJ     AAAAAAAAA     DD  DDD  EEEEEE      *");
		System.out.println("                          *     JJ   JJ    AAAAAAAAAAA    DD  DD   EE          *");
		System.out.println("                          *      JJ  JJ   AA         AA   DD DD    EE          *");
		System.out.println("                          *       JJJ    AA           AA  DDDD     EEEEEE      *");
		System.out.println("                          *                                                    *");
		System.out.println("                          ******************************************************");
		System.out.println("");
    }   
}

