import java.util.Random;

//Representa neuronio
public class LVQNeuron
{
	public VectorNeural vector;
	double _class;
	
	// Random weights constructor
	public LVQNeuron(double _class, int attrib_count) {
		Random random = new Random();
		double[] values = new double[attrib_count];
		for (int i = 0; i < values.length; i++) {
			// Tem que ver se assim ta um random legal
			values[i] = random.nextDouble();
		}
		
		// Set neuron class number
		this._class = _class;
		
		// Create vector with random values
		vector = new VectorNeural(values);
	}
	
	// Dataset constructor
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
		
		this.vector = new VectorNeural(v);	
	}
	
	//Calcula distancia com outra unidade
	public double distanceFrom(LVQNeuron n)
	{
		return this.vector.distanceFrom(n.vector, LVQ.DEFAULT_DISTANCE_METHOD);
	}
	
	//Aproxima
	public void aproach(LVQNeuron x, double learnRate)
	{
		VectorNeural xV = x.vector;
		
		//Pesos antigos + taxa * (x - pesos antigos)
		this.vector = this.vector.add(xV.subtract(this.vector).multiply(learnRate));
	}
	
	//Afasta
	public void diverge(LVQNeuron x, double learnRate)
	{
		VectorNeural xV = x.vector;
		
		//Pesos antigos - taxa * (x - pesos antigos)
		this.vector = this.vector.subtract(xV.subtract(this.vector).multiply(learnRate));
	}
}