package ia.lvq;
/**
 * Classe que representa vetor e suas operacoes
 */
public class VectorNeural
{
	/**
	 * Enum dos metodos de calculo de distancia (euclidiana e manhattan)
	 */
	public static enum DistanceMethod {
		MANHATTAN,
		EUCLIDEAN
	};
	
	// Componentes do vetor (pesos)
	public double[] components;
	
	/**
	 * Construtor do vetor neural recebendo valores dos pesos
	 * @param values
	 */
	public VectorNeural(double[] values)
	{
		this.components = values;
	}
	
	/**
	 * Construtor de vetor neural recebendo unico valor 
	 * que sera setado em um array de determinada dimensao
	 * @param value
	 * @param dimensions
	 */
	public VectorNeural(double value, int dimensions)
	{
		this.components = new double[dimensions];
		for(int i = 0; i < dimensions; i++)
		{
			this.components[i] = value;
		}
	}
	
	/**
	 * Operacao de adicao
	 * @param value
	 * @return
	 */
	public VectorNeural add(VectorNeural value)
	{
		if(!this.hasEqualDimensions(value))
		{
			System.out.println("Nao pode adicionar vetores de dimensoes diferentes!");
			return null;
		}
		
		// Vetor de resposta
		double[] rVectorNeural = new double[components.length];
		for(int i = 0; i < rVectorNeural.length; i++)
		{
			rVectorNeural[i] = this.components[i] + value.components[i];
		}
		
		return new VectorNeural(rVectorNeural);
	}
	
	/**
	 * Operacao de subtracao
	 * @param value
	 * @return vectorNeural
	 */
	public VectorNeural subtract(VectorNeural value)
	{
		if(!this.hasEqualDimensions(value))
		{
			System.out.println("Nao pode adicionar vetores de dimensoes diferentes!");
			return null;
		}
		
		// Vetor de resposta
		double[] rVectorNeural = new double[components.length];
		for(int i = 0; i < rVectorNeural.length; i++)
		{
			rVectorNeural[i] = this.components[i] - value.components[i];
		}
		
		return new VectorNeural(rVectorNeural);
	}
	
	/**
	 * Operacao de multiplicacao
	 * @param value
	 * @return vectorNeural
	 */
	public VectorNeural multiply(double value)
	{
		double[] rVectorNeural = new double[components.length];
		for(int i = 0; i < rVectorNeural.length; i++)
		{
			rVectorNeural[i] = this.components[i] * value;
		}
		
		return new VectorNeural(rVectorNeural);
	}
	
	/**
	 * Calcula distancia ate determinado vetor (euclidiana ou manhattan)
	 * @param VectorNeural
	 * @param distanceMethod
	 * @return distance
	 */
	public double distanceFrom(VectorNeural VectorNeural, DistanceMethod distanceMethod)
	{
		/* Baseado em
		 * 
		 *	http://en.wikipedia.org/wiki/Taxicab_geometry
		 *	http://stackoverflow.com/questions/8224470/calculating-manhattan-distance
		 *	distance = Math.abs(x1-x0) + Math.abs(y1-y0);
		 *	http://pt.stackoverflow.com/questions/12654/como-fa%C3%A7o-pra-calcular-dist%C3%A2ncia-euclidiana
		 *	Euclidiana -- distance = Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2));
		 */
		
		double distance = -1;
		
		// Tamanho dos vetores
		int VectorNeural1Length = this.components.length;
		int VectorNeural2Length = VectorNeural.components.length;
		
		// Valida tamanho dos vetores
		if (VectorNeural1Length == VectorNeural2Length) {
			distance = 0;
			
			// Seleciona metodo e calcula distancia
			switch (distanceMethod) {

			case MANHATTAN:
				for(int i = 0; i < VectorNeural1Length; i++) {
					distance += Math.abs(this.components[i] - VectorNeural.components[i]);
				}
				break;
				
			case EUCLIDEAN:
			default:
				for(int i = 0; i < VectorNeural1Length; i++) {
					distance += Math.pow(this.components[i] - VectorNeural.components[i], 2);
				}
			}
		}

		return distance;
	}
	
	private boolean hasEqualDimensions(VectorNeural value)
	{
		return this.components.length == value.components.length;
	}
}
