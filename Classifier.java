
public abstract class Classifier {
	
	public abstract void train(DataSet trainSet);
	
	//Retorna o erro
	public abstract double validate(DataSet validateSet);
	public abstract void test(DataSet testSet);
	
}
