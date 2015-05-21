package ia.lvq;
import java.util.Random;

/**
 * Classe que representa neuronio da rede LVQ
 */
public class LVQNeuron
{
	public VectorNeural vector;
	
	// Classe representada pelo neuronio
	double _class;
	
	/**
	 * Construtor de neuronio com pesos aleatorios
	 * @param _class
	 * @param attrib_count
	 */
	public LVQNeuron(double _class, int attrib_count) {
		Random random = new Random();
		double[] values = new double[attrib_count];
		for (int i = 0; i < values.length; i++) {
			values[i] = random.nextDouble();
		}
		
		// Seta numero da classe
		this._class = _class;
		
		// cria vetor com valores aleatorios
		vector = new VectorNeural(values);
	}
	
	/**
	 * Construtor baseado na linha do dataset
	 * @param dataSetLine
	 * @param classAtributteIndex
	 */
	public LVQNeuron(double[] dataSetLine, int classAtributteIndex)
	{
		// Seta classe
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
	
	/**
	 * Calcula distancia com outra unidade
	 * @param n
	 * @return distance
	 */
	public double distanceFrom(LVQNeuron n)
	{
		return this.vector.distanceFrom(n.vector, LVQ.DEFAULT_DISTANCE_METHOD);
	}
	
	/**
	 * Aproxima neuronio
	 * @param x
	 * @param learnRate
	 */
	public void aproach(LVQNeuron x, double learnRate)
	{
		VectorNeural xV = x.vector;
		
		//Pesos antigos + taxa * (x - pesos antigos)
		this.vector = this.vector.add(xV.subtract(this.vector).multiply(learnRate));
	}
	
	/**
	 * Afasta
	 * @param x
	 * @param learnRate
	 */
	public void diverge(LVQNeuron x, double learnRate)
	{
		VectorNeural xV = x.vector;
		
		//Pesos antigos - taxa * (x - pesos antigos)
		this.vector = this.vector.subtract(xV.subtract(this.vector).multiply(learnRate));
	}
}