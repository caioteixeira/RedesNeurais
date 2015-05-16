import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class DigitsLVQ extends Digits {
	
	// LVQ Args
	double learnRate;
	double reductionRate;
	int neuronsCount;
	LVQ.LVQIniMethod iniMethod;
	
	// LVQ Args consts
	static final String LEARN_RATE_OPTION = "lr";
	static final String REDUCTION_RATE_OPTION = "rr";
	static final String NEURONS_COUNT_OPTION = "nc";
	static final String INI_METHOD_OPTION = "init";
	
	static final String LEARN_RATE_OPTION_TEXT = "taxa de aprendizado";
	static final String REDUCTION_RATE_OPTION_TEXT = "taxa de reducao";
	static final String NEURONS_COUNT_OPTION_TEXT = "numero de neuronios";
	static final String INI_METHOD_OPTION_TEXT = "metodo de inicializacao dos neuronios (RANDOM , FIRST_VALUES ou ZERO)";
	
	public static void main(String[] args) {
		initializeOptions();
		
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse( options, args);
			formatter.printHelp("DigitsLVQ", options, true);
			
			populateAndValidateArgs(cmd);
			
		} catch (ParseException e) {
			//e.printStackTrace();
			
			System.out.println("Erro ao passar argumentos!");
			formatter.printHelp("DigitsProject", options, true);
		}
	}
	
	private static void initializeOptions() {
		options.addOption(Digits.TRAINING_FILE_OPTION, true, Digits.TRAINING_FILE_OPTION_TEXT);
		options.addOption(Digits.VALIDATE_FILE_OPTION, true, Digits.VALIDATE_FILE_OPTION_TEXT);
		options.addOption(Digits.TEST_FILE_OPTION, true, Digits.TEST_FILE_OPTION_TEXT);
		
		options.addOption(LEARN_RATE_OPTION, true, LEARN_RATE_OPTION_TEXT);
		options.addOption(REDUCTION_RATE_OPTION, true, REDUCTION_RATE_OPTION_TEXT);
		options.addOption(NEURONS_COUNT_OPTION, true, NEURONS_COUNT_OPTION_TEXT);
		options.addOption(INI_METHOD_OPTION, true, INI_METHOD_OPTION_TEXT);
	}
	
	private static void populateAndValidateArgs(CommandLine cmd) {
		// Files
		cmd.getOptionValue(Digits.TRAINING_FILE_OPTION);
		cmd.getOptionValue(Digits.VALIDATE_FILE_OPTION);
		cmd.getOptionValue(Digits.TEST_FILE_OPTION);
		
		// LVQ
		cmd.getOptionValue(LEARN_RATE_OPTION);
		cmd.getOptionValue(REDUCTION_RATE_OPTION);
		cmd.getOptionValue(NEURONS_COUNT_OPTION);
		cmd.getOptionValue(INI_METHOD_OPTION);
	}

}
