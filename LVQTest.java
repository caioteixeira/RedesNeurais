
public class LVQTest {
	public static void main(String[] args) {
		String[] files = {"optdigits.tes", "optdigits.tra"};
		DataSet set = new DataSet(64, files);
		set.printClassDistribution(64);
		
		DataSet.normalize(set, 0, 1);
		
		//Divide dataSet em tres subconjuntos com 60,20 e 20 por cento dos dados, respectivamente
		DataSet[] sets = set.divideDataSet();
		DataSet trainSet = sets[0];
		LVQ lvq = new LVQ(0.9, 2);
		lvq.train(trainSet);
		
		DataSet validateSet = sets[1];
		lvq.validate(validateSet);
		
		DataSet testSet = sets[2];
		//testSet.printClassDistribution(testSet.classAttributteIndex);
		lvq.test(testSet);
	}
}
