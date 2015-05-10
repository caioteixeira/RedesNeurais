public class LVQ extends Classifier {
	int neuronsCount;

	int i = 65; //Class Index
	VectorNeural[] inputNeurons;
	VectorNeural[] outputNeurons;
	private double learnRate;
	private LVQNeuron[] neurons;
	
	public DataSet validateSet;
	
	
	static final VectorNeural.DistanceMethod DEFAULT_DISTANCE_METHOD = VectorNeural.DistanceMethod.MANHATTAN;
	
	public LVQ(double learnRate, int neuronsCount)
	{
		this.learnRate = learnRate;
		this.neuronsCount = neuronsCount;
	}
	
	private void initializeNeurons(DataSet trainSet) {
		// Initialize Neurons
		neurons = new LVQNeuron[(trainSet.class_count)*neuronsCount];
		int countNeuronsFromClass = 0;
		int actualClassIndex = 0;
		for (int i = 0; i < neurons.length; i++) {
			// Change class index
			if (countNeuronsFromClass < neuronsCount) {
				countNeuronsFromClass++;
			} else {
				actualClassIndex++;
				countNeuronsFromClass = 1;
			}
			
			neurons[i] = new LVQNeuron(actualClassIndex, trainSet.attrib_count);
		}
	}

	@Override
	public void train(DataSet trainSet) {
		System.out.println("Training");
		
		// PASSO 0 inicializar todos os pesos... K-means ou random
		initializeNeurons(trainSet);
		
		
		/*
			x – vetor de treinamento (x1, ..., xi, ..., xn)
			T – classe correta para o vetor de treinamento
			wj – vetor peso da j-ésima unidade de saída (w1,j, ..., wi,j, ..., wn,j)
			Cj – classe representada pela j-ésima unidade de saída
			║ x – wj ║ - distância Euclidiana entre o vetor de entrada e (vetor de pesos para) o j-ésimo vetor de saída.
			J - uma unidade de saída
		*/

		// WORK IN PROGRESS...
		int stopCondition = 50; // TODO: Assim? Zero?
		
		// 1- Enquanto condicao de parada eh falsa execute os passos 2-6
		int EpochsCounter = 0;
		double actualLearnRate = learnRate;
		while (EpochsCounter < stopCondition && actualLearnRate > 0.0) {

			System.out.println("Epoca: " + EpochsCounter +" Taxa de aprendizado em: " + actualLearnRate + " - parar em " + stopCondition);
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
					selectedNeuron.aproach(neuronDataLine, actualLearnRate);
					//double distance = selectedNeuron.distanceFrom(neuronDataLine);
					//System.out.println(distance);
				} else {
					selectedNeuron.diverge(neuronDataLine, actualLearnRate);
					//double distance = selectedNeuron.distanceFrom(neuronDataLine);
					//System.out.println(distance);
				}
			}
			
			// Reseta Train set para um possivel novo treinamento
			trainSet.reset();
			
			// 5 - Reduza a taxa de aprendizado (?) como?
			//Reduz linearmente
			actualLearnRate = learnRate * ((double)(stopCondition - EpochsCounter)/(double)stopCondition);
			
			EpochsCounter++;
			/* 6
				Teste a condição de parada
				A condição deve especificar um número fixo de iterações (i.e.,execução do Passo 1) 
				ou um valor mínimo para a taxa de aprendizado. 
			 */
			
			if(EpochsCounter % 10 == 0)
				System.out.println("validando: " + validate(validateSet));
		}
	}

	@Override
	public double validate(DataSet validateSet) {
		System.out.println("Validating");
		
		double totalError = 0;
		validateSet.reset();
		while(validateSet.hasNext())
		{
			boolean found = false;
			double minDistance = 0.0;
			LVQNeuron input = new LVQNeuron(validateSet.next(), validateSet.classAttributteIndex);
			for (int i = 0; i < neurons.length; i++) {
				if(input._class == neurons[i]._class)
				{
					double distance = input.distanceFrom(neurons[i]);
					if(!found)
						minDistance = distance;
					else if(minDistance > distance)
					{
						minDistance = distance;
					}
					
					found = true;
				}
			}
			
			if(found)
				totalError += minDistance;
			else
				System.out.println("Class not found!" + input._class);
		}
		return totalError/validateSet.size();
	}

	@Override
	public void test(DataSet testSet) {
		System.out.println("Testing");
		System.out.println("Quantidade de testes: " + testSet.size());
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
