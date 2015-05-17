import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

/*
 		Criando uma nova Rede, treinando (com validacao), teste, gerando log e salvando

			DigitsLVQ 
			-init FIRST_VALUES 
			-lr 0.001 
			-rr 30 
			-nc 4 
			-tn "optdigits.norm.cortado.tra" 
			-tt "optdigits.norm.cortado.tes" 
			-vl "optdigits.norm.cortado.val"
			-trainlog "trainningLogLVQDigits.csv"
			-testlog "testLogLVQDigits.csv"
			-save "lvqNetwork.lvq"
			
		Carregando Rede, testando e salvando dados gerados no teste
		
			DigitsLVQ
			-load "lvqNetwork.lvq"
			-tt "optdigits.norm.cortado.tes"
			-testlog "testLogLVQDigits.csv"
 
 */
public class DigitsLVQ extends Digits {
	
	// LVQ Args
	static LVQ lvq;
	static String lvqFilePath;
	static double learnRate;
	static double reductionRate;
	static int neuronsCount;
	static LVQ.LVQIniMethod iniMethod;
	
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
			
			processArgs(cmd);
			
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
		options.addOption(Digits.SAVE_OPTION, true, Digits.SAVE_OPTION_TEXT);
		options.addOption(Digits.TRAIN_LOG_OPTION, true, Digits.TRAIN_LOG_OPTION_TEXT);
		options.addOption(Digits.TEST_LOG_OPTION, true, Digits.TEST_LOG_OPTION_TEXT);
		
		options.addOption(LOAD_FILE_OPTION, true, LOAD_OPTION_TEXT);
		options.addOption(LEARN_RATE_OPTION, true, LEARN_RATE_OPTION_TEXT);
		options.addOption(REDUCTION_RATE_OPTION, true, REDUCTION_RATE_OPTION_TEXT);
		options.addOption(NEURONS_COUNT_OPTION, true, NEURONS_COUNT_OPTION_TEXT);
		options.addOption(INI_METHOD_OPTION, true, INI_METHOD_OPTION_TEXT);
	}
	
	private static void processArgs(CommandLine cmd) {
		
		lvqFilePath = cmd.getOptionValue(LOAD_FILE_OPTION);
		
		if (lvqFilePath != null) { 
			// Load LVQ File
			lvq = new LVQ(lvqFilePath);
		} else {
			// NEW LVQ
			String l,r,n,i;
			l = cmd.getOptionValue(LEARN_RATE_OPTION);
			r = cmd.getOptionValue(REDUCTION_RATE_OPTION);
			n = cmd.getOptionValue(NEURONS_COUNT_OPTION);
			i = cmd.getOptionValue(INI_METHOD_OPTION);
			
			if (l != null && r != null && n != null && i != null) { 
				// Check Parameters
				learnRate = Double.valueOf(l);
				reductionRate = Double.valueOf(r);
				neuronsCount = Integer.parseInt(n);
				iniMethod = LVQ.LVQIniMethod.valueOf(i);
				lvq = new LVQ(learnRate, reductionRate, neuronsCount, iniMethod);
			} else {
				System.out.println("Faltou parametros para criacao/carregamento da rede LVQ");
				return;
			}
		}
		
		// Files
		trainFilePath    = cmd.getOptionValue(Digits.TRAINING_FILE_OPTION);
		validateFilePath = cmd.getOptionValue(Digits.VALIDATE_FILE_OPTION);
		testFilePath     = cmd.getOptionValue(Digits.TEST_FILE_OPTION);

		// DATA SETS
		DataSet trainSet, validateSet, testSet;
		if (trainFilePath != null && validateFilePath != null) {
			trainSet = new DataSet(-1, trainFilePath);
			validateSet = new DataSet(-1, validateFilePath);
			lvq.train(trainSet, validateSet);
			
			// If user pass trainLogPath... Save.
			String trainLogPath = cmd.getOptionValue(TRAIN_LOG_OPTION);
			if (trainLogPath != null) {
				lvq.saveTrainningLogFile(trainLogPath);
			}
		}
		
		if ((trainFilePath != null && validateFilePath == null) ||
			(trainFilePath == null && validateFilePath != null) ) {
			System.out.println("Para Treino deve-se passar o conjunto de teste E validacao!");
		}
		
		if (testFilePath != null) {
			testSet = new DataSet(-1, testFilePath);
			TestData testData = lvq.test(testSet);
			
			// If user pass testLogPath... Save.
			String testLogPath = cmd.getOptionValue(TEST_FILE_OPTION);
			if (testLogPath != null && testData != null) {
				testData.saveResults(testLogPath);
			}
		}
		
		// SAVE AND LOG OPTIONS
		String savePath = cmd.getOptionValue(SAVE_OPTION);
		if (savePath != null) {
			// Save LVQ
			lvq.save(savePath);
		}
	}
}
