
public class DataSetTest {

	public static void main(String[] args) {
	
		String[] files = {"optdigits.tes", "optdigits.tra"};
		DataSet set = new DataSet(64, files);
		set.printClassDistribution(64);
		
		//Divide dataSet em tres subconjuntos com 60,20 e 20 por cento dos dados, respectivamente
		DataSet[] sets = set.divideDataSet();
		
		//Conjuntos
		System.out.println("Training Set");
		sets[0].printClassDistribution(64);
		System.out.println("Validation Set");
		sets[1].printClassDistribution(64);
		System.out.println("Test Set");
		sets[2].printClassDistribution(64);
		
		//Imprime conjunto de treinamento
		/*System.out.println("Printing values of training set");
		while(sets[0].hasNext())
		{
			System.out.println(sets[0].nextString());
		}*/
	}

}
