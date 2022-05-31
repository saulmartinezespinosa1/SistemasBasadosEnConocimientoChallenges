package clips;
import net.sf.clipsrules.jni.Environment;
import jade.core.Agent;
//import jade.core.behaviours.Behaviour;
import jade.core.behaviours.*;

public class OneShotAgenteProdcust extends Agent {
	Environment clips;

  protected void setup() {
    System.out.println("Agent "+getLocalName()+" started.");
    clips = new Environment();

    addBehaviour(new TellBehaviour());
    addBehaviour(new AskBehaviour());
  } 

  private class TellBehaviour extends OneShotBehaviour {

    public void action() {
		try{
        System.out.println("metodo TellBehaviour a ejecutarse");
        clips.load("M:\\2022a_SistemasBasadosEnConocimientoCAtedra_Mie-Vie-CLIPS-KBS\\ClipsScriptsFromGithub\\clips\\prodcust\\load-prod-cust.clp");
        clips.load("M:\\2022a_SistemasBasadosEnConocimientoCAtedra_Mie-Vie-CLIPS-KBS\\ClipsScriptsFromGithub\\clips\\prodcust\\load-prodcust-rules.clp");
		}catch(Exception e){}
    } 
    
    /*public int onEnd() {
      myAgent.doDelete();   
      return super.onEnd();
    } */
  }    // END of inner class ...Behaviour
  private class AskBehaviour extends OneShotBehaviour {

    public void action() {
		try{
        System.out.println("metodo AskBehaviour a ejecutarse");
		clips.eval("(reset)");
		clips.eval("(run)");
		}catch(Exception e){}

    } 
    
    public int onEnd() {
      myAgent.doDelete();   
      return super.onEnd();
    } 
  }    // END of inner class ...Behaviour
}
