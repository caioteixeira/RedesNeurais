public class LVQ extends Classifier {
	int neuronsCount;

	int i = 65; //Class Index
	VectorNeural[] inputNeurons;
	VectorNeural[] outputNeurons;
	private double learnRate;
	private LVQNeuron[] neurons;
	
	static final VectorNeural.DistanceMethod DEFAULT_DISTANCE_METHOD = VectorNeural.DistanceMethod.MANHATTAN;
	
	public LVQ(double learnRate, int neuronsCount)
	{
		this.learnRate = learnRate;
		this.neuronsCount = neuronsCount;
		
		// PASSO 0 inicializar todos os pesos... K-means ou random
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
		
		/*
			x – vetor de treinamento (x1, ..., xi, ..., xn)
			T – classe correta para o vetor de treinamento
			wj – vetor peso da j-ésima unidade de saída (w1,j, ..., wi,j, ..., wn,j)
			Cj – classe representada pela j-ésima unidade de saída
			║ x – wj ║ - distância Euclidiana entre o vetor de entrada e (vetor de pesos para) o j-ésimo vetor de saída.
			J - uma unidade de saída
		*/

		// WORK IN PROGRESS...
		int stopCondition = 20; // TODO: Assim? Zero?
		
		// 1- Enquanto condicao de parada eh falsa execute os passos 2-6
		
		int EpochsCounter = 0;
		while (EpochsCounter < stopCondition) {
			int line = 1;
			
			System.out.println("Epoca: " + EpochsCounter +" Taxa de aprendizado em: " + learnRate + " - parar em " + stopCondition);
			//2- Para cada vetor de entrada de treinamento, executar os passos 3-4
			while (trainSet.hasNext()) {
				//System.out.println("Treinando linha " + line + " do DataSet");
				
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
				
				/*4- Altere Wj como na regra abaixo
					Se T = CJ, então
						wJ(new) = wJ(old) + α[x – wJ(old)];
					Se T ≠ CJ, então
						wJ(new) = wJ(old) - α[x – wJ(old)]; 
				 */
				if (selectedNeuron._class == neuronDataLine._class) {
					selectedNeuron.aproach(neuronDataLine, learnRate);
					//double distance = selectedNeuron.distanceFrom(neuronDataLine);
					//System.out.println(distance);
				} else {
					selectedNeuron.diverge(neuronDataLine, learnRate);
					//double distance = selectedNeuron.distanceFrom(neuronDataLine);
					//System.out.println(distance);
				}
				
				line++;
			}
			
			// Reseta Train set para um possivel novo treinamento
			trainSet.reset();
			
			// 5 - Reduza a taxa de aprendizado (?) como?
			learnRate *= 0.5;
			
			EpochsCounter++;
			/* 6
				Teste a condição de parada
				A condição deve especificar um número fixo de iterações (i.e.,execução do Passo 1) 
				ou um valor mínimo para a taxa de aprendizado. 
			 */
		}
	}

	@Override
	public double validate(DataSet validateSet) {
		System.out.println("Validating");
		
		double totalError = 0;
		
		while(validateSet.hasNext())
		{
			LVQNeuron selectedNeuron = neurons[0];
			LVQNeuron neuronDataLine = new LVQNeuron(validateSet.next(), validateSet.classAttributteIndex);
			
			double minDistance = Double.MAX_VALUE;
			double temp;
			for (int i = 0; i < neurons.length; i++) {
				temp = neurons[i].distanceFrom(neuronDataLine);
				//System.out.println("Temp: " + temp);
				if (temp < minDistance && neuronDataLine._class == neurons[i]._class) {
					//System.out.println(minDistance);
					minDistance = temp;
					selectedNeuron = neurons[i];
				}
			}
			
			totalError += minDistance;
		}
		
		System.out.println(totalError);
		return totalError;
	}

	@Override
	public void test(DataSet testSet) {
		System.out.println("Testing");
		int acertos = 0;
		int erros = 0;
		//testSet.printClassDistribution(testSet.classAttributteIndex);
		while (testSet.hasNext()) {
			LVQNeuron selectedNeuron = neurons[0];
			LVQNeuron neuronDataLine = new LVQNeuron(testSet.next(), testSet.classAttributteIndex);

			double minDistance = Double.MAX_VALUE;
			double temp;
			for (int i = 0; i < neurons.length; i++) {
				temp = neurons[i].distanceFrom(neuronDataLine);
				if (temp < minDistance) {
					minDistance = temp;
					selectedNeuron = neurons[i];
				}
			}
			
			if (selectedNeuron._class == neuronDataLine._class) acertos++;
			else erros++;
		}
		
		System.out.println("Acertou: " + acertos);
		System.out.println("Errou: " + erros);
	}
}
