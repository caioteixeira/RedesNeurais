
public class LVQTest {
	public static void main(String[] args) {
		DataSet trainSet = new DataSet(64, "optdigits.tra");
		LVQ lvq = new LVQ(0.9, 2);
		lvq.train(trainSet);
		DataSet testSet = new DataSet(64, "optdigits.tes");
		lvq.test(testSet);
	}
}
