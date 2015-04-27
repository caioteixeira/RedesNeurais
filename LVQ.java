
public class LVQ extends Classifier {

	
	int i = 65; //Class Index
	Vector[] inputNeurons;
	Vector[] outputNeurons;
	
	
	public LVQ(String config)
	{
		//Lê string de configuracao da LVQ
	}

	@Override
	public void train(DataSet trainSet) {
		// TODO Auto-generated method stub
		System.out.println("Training");
	}

	@Override
	public double validate(DataSet validateSet) {
		// TODO Auto-generated method stub
		System.out.println("Validating");
		return 0;
	}

	@Override
	public void test(DataSet testSet) {
		// TODO Auto-generated method stub
		System.out.println("Testing");
	}
}

//Representa vetor e suas operacoes
class Vector
{
	public double[] components;
	public Vector(double[] values)
	{
		this.components = values;
	}
	
	//Operacao de adicao
	public Vector add(Vector value)
	{
		if(!this.hasEqualDimensions(value))
		{
			//TODO: Lancar exception?
			System.out.println("Nao pode adicionar vetores de dimensoes diferentes!");
			return null;
		}
		
		//Vetor de resposta
		double[] rVector = new double[components.length];
		for(int i = 0; i < rVector.length; i++)
		{
			rVector[i] = this.components[i] + value.components[i];
		}
		
		return new Vector(rVector);
	}
	
	//Operacao de subtracao
	public Vector subtract(Vector value)
	{
		if(!this.hasEqualDimensions(value))
		{
			//TODO: Lancar exception?
			System.out.println("Nao pode adicionar vetores de dimensoes diferentes!");
			return null;
		}
		
		//Vetor de resposta
		double[] rVector = new double[components.length];
		for(int i = 0; i < rVector.length; i++)
		{
			rVector[i] = this.components[i] - value.components[i];
		}
		
		return new Vector(rVector);
	}
	
	//Distancia de manhatan
	public double distanceFrom(Vector value)
	{
		//TODO: Implementar distancia de manhatan
		//TODO: Outras tecnicas
		return 0.0;
	}
	
	private boolean hasEqualDimensions(Vector value)
	{
		return this.components.length == value.components.length;
	}
}

//Representa neuronio
class LVQNeuron
{
	public Vector vector;
	double _class;
	
	public LVQNeuron(double[] dataSetLine, int classAtributteIndex)
	{
		//TODO: Verificar se indice e valido
		this._class = dataSetLine[classAtributteIndex];
		
		//Cria novo arranjo removendo indice de classe
		double[] v = new double[dataSetLine.length - 1];
		for(int i = 0, j = 0; i < v.length && j < dataSetLine.length; i++)
		{
			if(j != classAtributteIndex)
			{
				v[i] = dataSetLine[j];
				j++;
			}
		}
		
		this.vector = new Vector(v);	
	}
	
	//Calcula distancia com outra unidade
	public double distanceFrom(LVQNeuron n)
	{
		return this.vector.distanceFrom(n.vector);
	}
	
	//TODO: metodo de aproximar
	//TODO: metodo de distanciar
}
