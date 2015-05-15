import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public abstract class Classifier {
	
	private Map<Integer, Double> errorMap;
	
	public abstract void train(DataSet trainSet, DataSet validateSet);
	//Retorna o erro
	public abstract double validate(DataSet validateSet);
	public abstract void test(DataSet testSet);
	
	
	//Guarda o erro de cada época em um map
	protected void logError(int numberOfEpochs, double error)
	{
		if(errorMap == null)
			errorMap = new HashMap<Integer, Double>();
		errorMap.put(numberOfEpochs, error);
	}
	
	//Salva arquivo CSV com o Log de erros por época do treinamento
	public void saveTrainningLogFile(String dir)
	{
		//Checando erros
		if(errorMap == null)
		{
			System.out.println("Log vazio! Não salvando");
			return;
		}
		if(errorMap.keySet().isEmpty())
		{
			System.out.println("Log vazio! Não salvando");
			return;
		}
		
		//Salvando log em formato CSV
		try {
			FileWriter fw = new FileWriter(dir);
			
			for(Integer key : errorMap.keySet())
			{
				fw.append(key + "," + errorMap.get(key)+"\n");
			}
			
			fw.close();
			
		} catch (IOException e) {
			System.out.println("Impossível salvar log! "+ dir );
			e.printStackTrace();
		}
	}
	
}
