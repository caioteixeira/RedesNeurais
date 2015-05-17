import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;


public class Digits {
	protected static Options options = new Options();
	protected static HelpFormatter formatter = new HelpFormatter();

	protected static String trainFilePath;
	protected static String validateFilePath;
	protected static String testFilePath;
	
	static final String TRAINING_FILE_OPTION = "tn";
	static final String VALIDATE_FILE_OPTION = "vl";
	static final String TEST_FILE_OPTION = "tt";
	static final String SAVE_OPTION = "save";
	static final String LOG_OPTION = "log";
	
	static final String TRAINING_FILE_OPTION_TEXT = "nome do arquivo do conjunto de dados de treino";
	static final String VALIDATE_FILE_OPTION_TEXT = "nome do arquivo do conjunto de dados de validacao";
	static final String TEST_FILE_OPTION_TEXT = "nome do arquivo do conjunto de dados de teste";
	static final String SAVE_OPTION_TEXT = "opcao de salvar rede neural, passe com o caminho do arquivo que quer salvar a rede";
	static final String LOG_OPTION_TEXT = "opcao para gerar log .csv, passar o caminho";
	
}
