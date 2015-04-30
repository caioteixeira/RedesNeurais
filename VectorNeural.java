
//Representa vetor e suas operacoes
public class VectorNeural
{
	public static enum DistanceMethod {
		MANHATTAN,
		EUCLIDEAN
	};
	
	public double[] components;
	public VectorNeural(double[] values)
	{
		this.components = values;
	}
	
	//Operacao de alteracao dos pesos - add
	public void add(double newWeight)
	{
		for(int i = 0; i < this.components.length; i++)
		{
			this.components[i] += newWeight;
		}
	}
	
	//Operacao de alteracao dos pesos - subtract
	public void subtract(double newWeight)
	{
		for(int i = 0; i < this.components.length; i++)
		{
			this.components[i] -= newWeight;
		}
	}
	
	//Operacao de adicao
	public VectorNeural add(VectorNeural value)
	{
		if(!this.hasEqualDimensions(value))
		{
			//TODO: Lancar exception?
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
	
	//Operacao de subtracao
	public VectorNeural subtract(VectorNeural value)
	{
		if(!this.hasEqualDimensions(value))
		{
			//TODO: Lancar exception?
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
	
	//Distancia de manhatan e euclidiana
	public double distanceFrom(VectorNeural VectorNeural, DistanceMethod distanceMethod)
	{
		// http://en.wikipedia.org/wiki/Taxicab_geometry
		// http://stackoverflow.com/questions/8224470/calculating-manhattan-distance
		// distance = Math.abs(x1-x0) + Math.abs(y1-y0);
		// http://pt.stackoverflow.com/questions/12654/como-fa%C3%A7o-pra-calcular-dist%C3%A2ncia-euclidiana
		// Euclidiana -- distance = Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2));
		
		double distance = -1;
		
		// VectorNeural Lengths
		int VectorNeural1Length = this.components.length;
		int VectorNeural2Length = VectorNeural.components.length;
		
		// Check VectorNeural lengths
		if (VectorNeural1Length == VectorNeural2Length) {
			distance = 0;
			// Select method
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
