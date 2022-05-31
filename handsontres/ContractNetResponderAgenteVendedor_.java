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
 * **************************************************************
 */

package examples.clips;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.FailureException;
import net.sf.clipsrules.jni.*;
import java.io.File;
import java.io.IOException;
import java.util.List;


/**
   This example shows how to implement the responder role in 
   a FIPA-contract-net interaction protocol. In this case in particular 
   we use a <code>ContractNetResponder</code>  
   to participate into a negotiation where an initiator needs to assign
   a task to an agent among a set of candidates.
   @author Giovanni Caire - TILAB
 */


public class ContractNetResponderAgenteVendedor extends Agent {

	Environment clips;
	String facts = "M:\\2022a_SistemasBasadosEnConocimientoCAtedra_Mie-Vie-CLIPS-KBS\\ClipsScriptsFromGithub\\clips\\hechos_vende_.clp";
	String rules = "M:\\2022a_SistemasBasadosEnConocimientoCAtedra_Mie-Vie-CLIPS-KBS\\ClipsScriptsFromGithub\\clips\\reglas_vende_.clp";

	protected void setup() {

		//Create new enviroment clips
		try{
			clips = new Environment();
		}catch(Exception e) 
		{
			System.out.println("No se pudo crear ambiente CLIPS");
		}
		Object[] args = getArguments();



		cargaclps();


		System.out.println("AgenteVendedor "+getLocalName()+": esperando por CallingForProposal...");
		MessageTemplate template = MessageTemplate.and(
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
				MessageTemplate.MatchPerformative(ACLMessage.CFP) );

		addBehaviour(new ContractNetResponder(this, template) {
			@Override
			protected ACLMessage handleCfp(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
				System.out.println("AgenteVendedor "+getLocalName()+": CallingForProposal recibido de "+cfp.getSender().getName()+". Y esta buscando "+cfp.getContent());
				
				String[] propuesta = tell(cfp.getContent());
				if(propuesta[0]!="error")
				{
					System.out.println("AgenteVendedor "+getLocalName()+": esta proponiendo "+cfp.getContent()+" en "+propuesta[0]);
					ACLMessage proponer = cfp.createReply();
					proponer.setPerformative(ACLMessage.PROPOSE);
					String propuestas=propuesta[0]+"+"+propuesta[1]+"+"+propuesta[2];
					proponer.setContent(propuestas);
					return proponer;
				}
				else{
					System.out.println("AgenteVendedor "+getLocalName()+": Producto no encontrado");
					throw new RefuseException("evaluacion fallo");
				}
			}

			@Override
			protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose,ACLMessage accept) throws FailureException {
				System.out.println("AgenteVendedor "+getLocalName()+": Propuesta aceptada");
				String array_query[] = ask(cfp.getContent()).split("\\+");
				int cantidad = Integer.parseInt(array_query[4]); //4
				int id = Integer.parseInt(array_query[1]); //1
				String promocion = array_query[5]; //5
				if (cantidad>0) {
					System.out.println("AgenteVendedor "+getLocalName()+": Accion completada correctamente");
					ACLMessage inform = accept.createReply();
					inform.setPerformative(ACLMessage.INFORM);
					inform.setContent(promocion);
					return inform;
				}
				else {
					System.out.println("AgenteVendedor "+getLocalName()+": Producto sin saldo");
					throw new FailureException("error_ms");
				}	
			}

			protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
				System.out.println("AgenteVendedor "+getLocalName()+": Propuesta rechazada");
			}
		} );
	}

	private void cargaclps()
	{
		try{
			clips.clear();
			clips.load(facts);
			clips.load(rules);
			clips.reset();
			printlogo();
			System.out.println("*************************************************** Hechos en Memoria *******************************************");
			clips.eval("(facts)");
			System.out.println("*****************************************************************************************************************");

		}catch(Exception e) 
		{
			System.out.println("No se cargaron archivos .clp");
		}
		
	}





	private String[] tell(String art) {
		String[] propuesta;
		try
				{
					clips.run();
					System.out.println("trabajando en la busqueda del pedido...");

					FactAddressValue fv;
					fv = (FactAddressValue)((MultifieldValue) clips.eval("(find-fact ((?f producto)) (eq ?f:nombre "+art+"))")).get(0);
					System.out.println("Encontramos el producto!");
					int precio = ((NumberValue) fv.getSlotValue("precio")).intValue();
					int id = ((NumberValue) fv.getSlotValue("id")).intValue();
					String banco = "n/a";
					int msis = 1;
					try
					{
						FactAddressValue fv2 = (FactAddressValue)((MultifieldValue) clips.eval("(find-fact ((?f mesessi)) (eq ?f:id "+id+"))")).get(0);
						banco=((LexemeValue) fv2.getSlotValue("banco")).getValue();
						msis=((NumberValue) fv2.getSlotValue("msi")).intValue();
					}
					catch(Exception e){}
					propuesta = new String[]{String.valueOf(precio),String.valueOf(msis),banco};
					return propuesta;
				}
				catch(Exception e)
				{
					propuesta = new String[]{"error"};
					return propuesta;
				}
	}

	private String ask(String c) {
		try
				{
					clips.run();
					String qry_result,promocion="n/a";
					System.out.println("trabajando en la busqueda del producto para entrega...");
					FactAddressValue fv;
					fv = (FactAddressValue)((MultifieldValue) clips.eval("(find-fact ((?f producto)) (eq ?f:nombre "+c+"))")).get(0);
					String nombre = ((LexemeValue) fv.getSlotValue("nombre")).getValue();
					int id = ((NumberValue) fv.getSlotValue("id")).intValue();
					String marca = ((LexemeValue) fv.getSlotValue("marca")).getValue();
					int precio = ((NumberValue) fv.getSlotValue("precio")).intValue();
					int cantidad = ((NumberValue) fv.getSlotValue("existencia")).intValue();
					try
					{
						FactAddressValue fv2 = (FactAddressValue)((MultifieldValue) clips.eval("(find-fact ((?f promocion)) (eq ?f:id "+id+"))")).get(0);
						promocion=" Aplica esta promocion: "+((LexemeValue) fv2.getSlotValue("descripcion")).getValue();
					}
					catch(Exception e){}
					qry_result=nombre+"+"+id+"+"+marca+"+"+precio+"+"+cantidad+"+"+promocion;
					return qry_result;
				}
				catch(Exception e){return "error";}
	}
    private void printlogo() {
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

