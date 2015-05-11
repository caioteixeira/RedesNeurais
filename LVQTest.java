
public class LVQTest {
	public static void main(String[] args) {
		DataSet trainSet = new DataSet(-1, "optdigits.norm.cortado.tra");
		trainSet.printClassDistribution();
		DataSet testSet = new DataSet(-1, "optdigits.norm.cortado.tes");
		testSet.printClassDistribution();
		DataSet validateSet = new DataSet(-1, "optdigits.norm.cortado.val");
		validateSet.printClassDistribution();
		
		
		//Divide dataSet em tres subconjuntos com 60,20 e 20 por cento dos dados, respectivamente
		//DataSet[] sets = set.divideDataSet();
		
		//DataSet trainSet = new DataSet(64, "optdigits.norm.tra");
		//DataSet validateSet = new DataSet(64, "optdigits.norm.val");
		LVQ lvq = new LVQ(0.001, 2);
		lvq.validateSet = validateSet;
		lvq.train(trainSet);
		
		//DataSet testSet = new DataSet(64, "optdigits.norm.tes");
		//testSet.printClassDistribution(testSet.classAttributteIndex);
		lvq.test(testSet); 
	}
}
