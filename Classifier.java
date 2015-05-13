
public abstract class Classifier {
	
	public abstract void train(DataSet trainSet, DataSet validateSet);
	
	//Retorna o erro
	public abstract double validate(DataSet validateSet);
	public abstract void test(DataSet testSet);
	
}
