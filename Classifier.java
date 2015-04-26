
public abstract class Classifier {
	
	public abstract void train(DataSet trainSet, DataSet validateSet);
	public abstract void test(DataSet testSet);
	
}
