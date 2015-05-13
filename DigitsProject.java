import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class DigitsProject {
	
	private static CommandLineParser parser = new DefaultParser();
	private static Options options = new Options();
	private static HelpFormatter formatter = new HelpFormatter();
	
	public static void main(String[] args) {
		initializeOptions();
		
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse( options, args);
			formatter.printHelp("DigitsProject", options, true);
		} catch (ParseException e) {
			//e.printStackTrace();
			
			System.out.println("Erro ao passar argumentos!");
			formatter.printHelp("DigitsProject", options, true);
		}
	}
	
	private static void initializeOptions() {
		options.addOption("tn", true, "nome do arquivo do conjunto de dados de treino");
		options.addOption("vl", true, "nome do arquivo do conjunto de dados de validacao");
		options.addOption("tt", true, "nome do arquivo do conjunto de dados de teste");
		
		options.addOption("lr", true, "taxa de aprendizado inicial");
		
		// TODO Select Alg. Type MLP or LVQ
		options.addOption("nhidden", true, "numero de neuronios na cada escondida (para a rede MLP)");
		options.addOption("nclass", true, "numero de neuronios para cada classe (para a rede LVQ)");
		
		options.addOption("init", true, "inicializacao de pesos (zero/aleatoria)"); // FIXME
	}

}
