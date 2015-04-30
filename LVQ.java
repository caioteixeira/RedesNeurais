import java.util.Random;

public class LVQ extends Classifier {
	int neuronsCount;

	int i = 65; //Class Index
	Vector[] inputNeurons;
	Vector[] outputNeurons;
	private double learnRate;
	private LVQNeuron[] neurons;
	
	static final Vector.DistanceMethod DEFAULT_DISTANCE_METHOD = Vector.DistanceMethod.EUCLIDEAN;
	
	public LVQ(double learnRate, int neuronsCount)
	{
		this.learnRate = learnRate;
		this.neuronsCount = neuronsCount;
		
		initializeNeurons();
	}
	
	private void initializeNeurons() {
		// Initialize Neurons
		neurons = new LVQNeuron[(DataSet.CLASS_COUNT)*neuronsCount];
		int countNeuronsFromClass = 0;
		int actualClassIndex = 0;
		for (int i = 0; i < neurons.length; i++) {
			// Change class index
			if (countNeuronsFromClass < DataSet.CLASS_COUNT) {
				countNeuronsFromClass++;
			} else {
				actualClassIndex++;
				countNeuronsFromClass = 0;
			}
			
			neurons[i] = new LVQNeuron(actualClassIndex);
		}
	}

	@Override
	public void train(DataSet trainSet) {
		System.out.println("Training");
		
		// PASSO 0 inicializar todos os pesos... K-means ou random
		/*
			x – vetor de treinamento (x1, ..., xi, ..., xn)
			T – classe correta para o vetor de treinamento
			wj – vetor peso da j-ésima unidade de saída (w1,j, ..., wi,j, ..., wn,j)
			Cj – classe representada pela j-ésima unidade de saída
			║ x – wj ║ - distância Euclidiana entre o vetor de entrada e (vetor de pesos para) o j-ésimo vetor de saída.
			J - uma unidade de saída
		*/
		//1- Enquanto condicao de parada eh falsa execute os passos 2-6
		//2- Para cada vetor de entrada de treinamento, executar os passos 3-4
		//3- Encontre a unidade de saida J tal que | x - Wj | seja minima
		/*4- Altere Wj como na regra abaixo
			Se T = CJ, então
				wJ(new) = wJ(old) + α[x – wJ(old)];
			Se T ≠ CJ, então
				wJ(new) = wJ(old) - α[x – wJ(old)]; 
		*/
		// 5 - Reduza a taxa de aprendizado (?) como?
		/* 6
			Teste a condição de parada
			A condição deve especificar um número fixo de iterações (i.e.,execução do Passo 1) 
			ou um valor mínimo para a taxa de aprendizado. 
		*/

		// WORK IN PROGRESS...
		double stopCondition = 0; // TODO: Assim? Zero?
		
		// 1- Enquanto condicao de parada eh falsa execute os passos 2-6
		while (learnRate > stopCondition) {
			//2- Para cada vetor de entrada de treinamento, executar os passos 3-4
			while (trainSet.hasNext()) {
				//3- Encontre a unidade de saida J tal que | x - Wj | seja minima
				LVQNeuron selectedNeuron = neurons[0];
				LVQNeuron neuronDataLine = new LVQNeuron(trainSet.next(), trainSet.classAttributteIndex);

				double minDistance = Double.MAX_VALUE;
				double temp;
				for (int i = 0; i < neurons.length; i++) {
					temp = neurons[i].distanceFrom(neuronDataLine);
					if (temp < minDistance) {
						minDistance = temp;
						selectedNeuron = neurons[i];
					}
				}
				
				double newWeight = learnRate * minDistance;
				
				/*4- Altere Wj como na regra abaixo
					Se T = CJ, então
						wJ(new) = wJ(old) + α[x – wJ(old)];
					Se T ≠ CJ, então
						wJ(new) = wJ(old) - α[x – wJ(old)]; 
				 */
				if (selectedNeuron._class == neuronDataLine._class) {
					selectedNeuron.vector.add(newWeight);
				} else {
					selectedNeuron.vector.subtract(newWeight);
				}
			}
			
			// 5 - Reduza a taxa de aprendizado (?) como?
			learnRate -= 0.1;
			
			/* 6
				Teste a condição de parada
				A condição deve especificar um número fixo de iterações (i.e.,execução do Passo 1) 
				ou um valor mínimo para a taxa de aprendizado. 
			 */
		}
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
	public static enum DistanceMethod {
		MANHATTAN,
		EUCLIDEAN
	};
	
	public double[] components;
	public Vector(double[] values)
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
	public Vector add(Vector value)
	{
		if(!this.hasEqualDimensions(value))
		{
			//TODO: Lancar exception?
			System.out.println("Nao pode adicionar vetores de dimensoes diferentes!");
			return null;
		}
		
		// Vetor de resposta
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
		
		// Vetor de resposta
		double[] rVector = new double[components.length];
		for(int i = 0; i < rVector.length; i++)
		{
			rVector[i] = this.components[i] - value.components[i];
		}
		
		return new Vector(rVector);
	}
	
	//Distancia de manhatan e euclidiana
	public double distanceFrom(Vector vector, DistanceMethod distanceMethod)
	{
		// http://en.wikipedia.org/wiki/Taxicab_geometry
		// http://stackoverflow.com/questions/8224470/calculating-manhattan-distance
		// distance = Math.abs(x1-x0) + Math.abs(y1-y0);
		// http://pt.stackoverflow.com/questions/12654/como-fa%C3%A7o-pra-calcular-dist%C3%A2ncia-euclidiana
		// Euclidiana -- distance = Math.sqrt(Math.pow((x1-x2),2) + Math.pow((y1-y2),2));
		
		double distance = -1;
		
		// Vector Lengths
		int vector1Length = this.components.length;
		int vector2Length = vector.components.length;
		
		// Check vector lengths
		if (vector1Length == vector2Length) {
			distance = 0;
			// Select method
			switch (distanceMethod) {

			case MANHATTAN:
				for(int i = 0; i < vector1Length; i++) {
					distance += Math.abs(this.components[i] - vector.components[i]);
				}
				break;
				
			case EUCLIDEAN:
			default:
				for(int i = 0; i < vector1Length; i++) {
					distance += Math.pow(this.components[i] - vector.components[i], 2);
				}
			}
		}

		return distance;
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
	
	// Random weights constructor
	public LVQNeuron(double _class) {
		Random random = new Random();
		double[] values = new double[DataSet.ATTRIB_COUNT];
		for (int i = 0; i < values.length; i++) {
			// Tem que ver se assim ta um random legal
			values[i] = random.nextDouble()*10;
		}
		
		// Set neuron class number
		this._class = _class;
		
		// Create vector with random values
		vector = new Vector(values);
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
		
		this.vector = new Vector(v);	
	}
	
	//Calcula distancia com outra unidade
	public double distanceFrom(LVQNeuron n)
	{
		return this.vector.distanceFrom(n.vector, LVQ.DEFAULT_DISTANCE_METHOD);
	}
	
	//TODO: metodo de aproximar
	//TODO: metodo de distanciar
}
