import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;

/*
	Criando uma nova Rede, treinando (com validacao), teste, gerando log e salvando

	MLPDigits 
	-init RANDOM 
	-lr 0.5 
	-bias
	-lc 1
	-ic 61 
	-oc 4 
	-hc 40
	-tn "optdigits.norm.cortado.tra" 
	-tt "optdigits.norm.cortado.tes" 
	-vl "optdigits.norm.cortado.val"
	-trainlog "trainningLogMLPDigits.csv"
	-testlog "testLogMLPDigits.csv"
	-save "mlpNetwork.mlp"
	
	Carregando Rede, testando e salvando dados gerados no teste
	
	MLPDigits
	-load "mlpNetwork.mlp"
	-tt "optdigits.norm.cortado.tes"
	-testlog "testLogMLPDigits.csv"

*/
public class MLPDigits extends Digits {
	
	enum MLPIniMethod {
		ZERO,
		RANDOM
	}
	
	// MLP Args
	static MLP mlp;
	static String mlpFilePath;
	static double learnRate;
	static boolean bias;
	static int layersCount;
	static int inputNodesCount;
	static int outputNodesCount;
	static int hiddenNodesCount;
	static MLPIniMethod iniMethod;
	
	// MLP Args consts
	static final String LEARN_RATE_OPTION = "lr";
	static final String BIAS_OPTION = "bias";
	static final String LAYERS_COUNT_OPTION = "lc";
	static final String INPUT_NODES_COUNT_OPTION = "ic";
	static final String OUTPUT_NODES_COUNT_OPTION = "oc";
	static final String HIDDEN_NODES_COUNT_OPTION = "hc";
	static final String INI_METHOD_OPTION = "init";
	
	static final String LEARN_RATE_OPTION_TEXT = "taxa de aprendizado";
	static final String BIAS_OPTION_TEXT = "opcao para utilizacao de bias";
	static final String LAYERS_COUNT_OPTION_TEXT = "numero de camadas hidden";
	static final String INPUT_NODES_COUNT_OPTION_TEXT = "numero de nos de entrada";
	static final String OUTPUT_NODES_COUNT_OPTION_TEXT = "numero de nos de saida";
	static final String HIDDEN_NODES_COUNT_OPTION_TEXT = "numero de nos hidden";
	static final String INI_METHOD_OPTION_TEXT = "metodo de inicializacao dos neuronios (RANDOM ou ZERO)";
	
	public static void main(String[] args) {
		initializeOptions();
		
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse( options, args);
			formatter.printHelp("MLPDigits", options, true);
			
			processArgs(cmd);
			
		} catch (ParseException e) {
			System.out.println("Erro ao passar argumentos!");
			formatter.printHelp("MLPDigits", options, true);
		}
	}
	
	private static void initializeOptions() {
		options.addOption(Digits.TRAINING_FILE_OPTION, true, Digits.TRAINING_FILE_OPTION_TEXT);
		options.addOption(Digits.VALIDATE_FILE_OPTION, true, Digits.VALIDATE_FILE_OPTION_TEXT);
		options.addOption(Digits.TEST_FILE_OPTION, true, Digits.TEST_FILE_OPTION_TEXT);
		options.addOption(Digits.LOAD_FILE_OPTION, true, LOAD_OPTION_TEXT);
		options.addOption(Digits.SAVE_OPTION, true, Digits.SAVE_OPTION_TEXT);
		options.addOption(Digits.TRAIN_LOG_OPTION, true, Digits.TRAIN_LOG_OPTION_TEXT);
		options.addOption(Digits.TEST_LOG_OPTION, true, Digits.TEST_LOG_OPTION_TEXT);
		
		options.addOption(LEARN_RATE_OPTION, true, LEARN_RATE_OPTION_TEXT);
		options.addOption(BIAS_OPTION, BIAS_OPTION_TEXT);
		options.addOption(LAYERS_COUNT_OPTION, true, LAYERS_COUNT_OPTION_TEXT);
		options.addOption(INPUT_NODES_COUNT_OPTION, true, INPUT_NODES_COUNT_OPTION_TEXT);
		options.addOption(OUTPUT_NODES_COUNT_OPTION, true, OUTPUT_NODES_COUNT_OPTION_TEXT);
		options.addOption(HIDDEN_NODES_COUNT_OPTION, true, HIDDEN_NODES_COUNT_OPTION_TEXT);
		options.addOption(INI_METHOD_OPTION, true, INI_METHOD_OPTION_TEXT);
	}
	
	private static void processArgs(CommandLine cmd) {
		
		mlpFilePath = cmd.getOptionValue(LOAD_FILE_OPTION);
		
		if (mlpFilePath != null) { 
			// Load MLP File
			// TODO
		} else {
			// NEW MLP
			String l,lc,in,o,h,i;
			l = cmd.getOptionValue(LEARN_RATE_OPTION);
			lc = cmd.getOptionValue(LAYERS_COUNT_OPTION);
			in = cmd.getOptionValue(INPUT_NODES_COUNT_OPTION);
			o = cmd.getOptionValue(OUTPUT_NODES_COUNT_OPTION);
			h = cmd.getOptionValue(HIDDEN_NODES_COUNT_OPTION);
			i = cmd.getOptionValue(INI_METHOD_OPTION);
			
			if (l != null && lc != null && in != null &&
					o != null && h != null  && i != null) { 
				
				learnRate = Double.parseDouble(l);
				bias = cmd.hasOption(BIAS_OPTION);
				layersCount = Integer.valueOf(lc);
				inputNodesCount = Integer.valueOf(in);
				outputNodesCount = Integer.valueOf(o);
				hiddenNodesCount = Integer.valueOf(h);
				
				boolean random = false;
				switch (MLPIniMethod.valueOf(i)) {
				case RANDOM:
					random = true;
					break;
				default:
					break;
				}
				
				mlp = new MLP(layersCount, inputNodesCount, outputNodesCount, hiddenNodesCount, bias, learnRate, random);
			} else {
				System.out.println("Faltou parametros para criacao/carregamento da rede LVQ");
				return;
			}
		}
		
		// XXX TODO REFACTOR - Codigo identico ao do LVQDigits!
		// Files
		trainFilePath    = cmd.getOptionValue(Digits.TRAINING_FILE_OPTION);
		validateFilePath = cmd.getOptionValue(Digits.VALIDATE_FILE_OPTION);
		testFilePath     = cmd.getOptionValue(Digits.TEST_FILE_OPTION);
		
		// DATA SETS
		DataSet trainSet, validateSet, testSet;
		if (trainFilePath != null && validateFilePath != null) {
			trainSet = new DataSet(-1, trainFilePath);
			validateSet = new DataSet(-1, validateFilePath);
			mlp.train(trainSet, validateSet);
			
			// If user pass trainLogPath... Save.
			String trainLogPath = cmd.getOptionValue(TRAIN_LOG_OPTION);
			if (trainLogPath != null) {
				mlp.saveTrainningLogFile(trainLogPath);
			}
		}
		
		if ((trainFilePath != null && validateFilePath == null) ||
			(trainFilePath == null && validateFilePath != null) ) {
			System.out.println("Para Treino deve-se passar o conjunto de teste E validacao!");
		}
		
		if (testFilePath != null) {
			testSet = new DataSet(-1, testFilePath);
			TestData testData = mlp.test(testSet);
			
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
			// TODO mlp.save(savePath);
		}
	}
}
