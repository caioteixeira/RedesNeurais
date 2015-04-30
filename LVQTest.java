
public class LVQTest {
	public static void main(String[] args) {
		DataSet set = new DataSet(64, "optdigits.tra");
		LVQ lvq = new LVQ(0.9, 2);
		lvq.train(set);
	}
}
