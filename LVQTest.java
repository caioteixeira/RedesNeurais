
public class LVQTest {
	public static void main(String[] args) {
		//String[] files = {"optdigits.tes", "optdigits.tra"};
		/*DataSet set = new DataSet(64, "optdigits.norm");
		set.printClassDistribution(64);
		DataSet[] sets = set.divideDataSet();
		DataSet trainSet = sets[0];
		DataSet testSet = sets[2];
		DataSet validateSet = sets[1];
		testSet.save("optdigits.norm.tes");
		trainSet.save("optdigits.norm.tra");
		validateSet.save("optdigits.norm.val");
		//DataSet.normalize(set, 1, 2);*/
		
		//Divide dataSet em tres subconjuntos com 60,20 e 20 por cento dos dados, respectivamente
		//DataSet[] sets = set.divideDataSet();
		
		DataSet trainSet = new DataSet(64, "optdigits.norm.tra");
		LVQ lvq = new LVQ(0.001, 2);
		lvq.train(trainSet);
		
		DataSet validateSet = new DataSet(64, "optdigits.norm.val");
		lvq.validate(validateSet);
		
		DataSet testSet = new DataSet(64, "optdigits.norm.tes");
		//testSet.printClassDistribution(testSet.classAttributteIndex);
		lvq.test(testSet); 
	}
}
