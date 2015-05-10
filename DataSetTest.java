
public class DataSetTest {

	public static void main(String[] args) {
	
		String[] files = {"datasetcortado"};
		DataSet set = new DataSet(-1, files);
		set.normalize();
		set.printClassDistribution();
		
		//Divide dataSet em tres subconjuntos com 60,20 e 20 por cento dos dados, respectivamente
		DataSet[] sets = set.divideDataSet();
		DataSet trainSet = sets[0];
		DataSet testSet = sets[2];
		DataSet validateSet = sets[1];
		
		testSet.save("optdigits.norm.cortado.tes");
		trainSet.save("optdigits.norm.cortado.tra");
		validateSet.save("optdigits.norm.cortado.val");
		
		//Conjuntos
		System.out.println("Training Set");
		sets[0].printClassDistribution();
		System.out.println("Validation Set");
		sets[1].printClassDistribution();
		System.out.println("Test Set");
		sets[2].printClassDistribution();
		
		//Imprime conjunto de treinamento
		/*System.out.println("Printing values of training set");
		while(sets[0].hasNext())
		{
			System.out.println(sets[0].nextString());
		}*/
	}

}
