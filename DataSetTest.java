
public class DataSetTest {

	public static void main(String[] args) {
	
		String[] files = {"optdigits.tes", "optdigits.tra"};
		DataSet set = new DataSet(1, files);
		set.checkClassDistribution(64);
		
		set = new DataSet(1, files[1]);
		set.checkClassDistribution(64);
		
		set = new DataSet(1, files[0]);
		set.checkClassDistribution(64);
	}

}
