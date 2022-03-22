package clips;
import net.sf.clipsrules.jni.Environment;

public class javaclips3{
	public static void main (String[] noseusa) throws Exception {
		Environment env = new Environment();
		env.eval("(reset)");
		env.eval("(clear)");
		env.load("M:\\2022a_SistemasBasadosEnConocimientoCAtedra_Mie-Vie-CLIPS-KBS\\ClipsScriptsFromGithub\\clips\\prodcust\\load-prod-cust.clp");
		env.load("M:\\2022a_SistemasBasadosEnConocimientoCAtedra_Mie-Vie-CLIPS-KBS\\ClipsScriptsFromGithub\\clips\\prodcust\\load-prodcust-rules.clp");
		env.eval("(reset)");
		env.eval("(run)");
	}
}