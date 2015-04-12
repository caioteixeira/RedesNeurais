
public abstract class Classifier {
	
	public abstract void train(String trainSet, String validateSet);
	public abstract void test(String testSet);
	
}
