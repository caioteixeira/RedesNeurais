package ia.lvq;
import ia.data.Classifier;
import ia.data.DataSet;
import ia.data.TestData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class LVQ extends Classifier {
	
	/**
	 * Enum para os metodos 
	 * de inicializacao de neuronios 
	 * da rede LVQ
	 */
	public enum LVQIniMethod
	{
		RANDOM,
		FIRST_VALUES,
		ZERO
	}
	
	// Quantidade de neuronios
	int neuronsCount;
	
	// Neuronios da rede LVQ
	private LVQNeuron[] neurons;
	
	/** Taxas Utilizadas na LVQ **/
	// Taxa de redução da taxa de aprendizado
	private double reductionRate; 
	// Taxa inicial de aprendizado
	private double learnRate; 
	// Limiar de parada
	private double stopLimiar = 0.00001; 
	
	// Metodo de inicializacao dos neuronios da LVQ
	private LVQIniMethod iniMethod = LVQIniMethod.FIRST_VALUES;
	
	// Metodo de calculo da distancia (euclidiana ou manhattan)
	static VectorNeural.DistanceMethod DEFAULT_DISTANCE_METHOD = VectorNeural.DistanceMethod.MANHATTAN;
	
	/**
	 * Construtor padrao da LVQ recebendo os seguintes parametos:
	 * @param learnRate
	 * @param reductionRate
	 * @param neuronsCount
	 * @param iniMethod
	 */
	public LVQ(double learnRate, double reductionRate, int neuronsCount, LVQIniMethod iniMethod)
	{
		this.learnRate = learnRate;
		this.neuronsCount = neuronsCount;
		this.iniMethod = iniMethod;
		this.reductionRate = reductionRate;
	}
	
	/**
	 * Construtor da LVQ que altera o calculo da distancia:
	 * @param learnRate
	 * @param reductionRate
	 * @param neuronsCount
	 * @param iniMethod
	 * @param distanceMethod
	 */
	public LVQ(double learnRate, double reductionRate, int neuronsCount, LVQIniMethod iniMethod, VectorNeural.DistanceMethod distanceMethod)
	{
		this(learnRate,reductionRate,neuronsCount,iniMethod);
		LVQ.DEFAULT_DISTANCE_METHOD = distanceMethod;
	}
	
	/**
	 * Construtor da LVQ a partir de um arquivo .lvq
	 * @param fileName
	 */
	public LVQ(String fileName) {
		/* Ler na seguinte ordem
		 *  learnRate (double
		 *	reductionRate (double)
		 *	stopLimiar (double)
		 *	neuronsCount (int)
		 *	LVQIniMethod (int)
		 *	Neurons (linha a linha) com os components
		 */
		try
		{
			System.out.println("Loading LVQ Network...");
			File f = new File(fileName);
			Scanner sc = new Scanner(f);
			
			// Read and populate attribs
			String[] attribs = sc.nextLine().split(",");
			this.learnRate = Double.valueOf(attribs[0]);
			this.reductionRate = Double.valueOf(attribs[1]);
			this.stopLimiar = Double.valueOf(attribs[2]);
			this.neuronsCount = Integer.valueOf(attribs[3]);
			this.iniMethod = LVQIniMethod.values()[Integer.valueOf(attribs[4])];
			
			// Neurons List
			List<LVQNeuron> neuronsList = new ArrayList<LVQNeuron>();
			while(sc.hasNext())
			{
				// Read Line
				String neuronLine = sc.nextLine();
				String[] components = neuronLine.split(",");
				
				// Create New Neuron
				LVQNeuron neuron = new LVQNeuron(Double.valueOf(components[0]), components.length-1);
				
				// Create Double values
				double[] values = new double[components.length-1];
				for (int i = 1; i < components.length;i++) {
					values[i-1] = Double.valueOf(components[i]);
				}
				
				// Set Vector in Neuron
				neuron.vector = new VectorNeural(values);
				
				// Add neuron in list
				neuronsList.add(neuron);
			}
			
			// Set neurons array in LVQ Network
			this.neurons = neuronsList.toArray(new LVQNeuron[neuronsList.size()]);
			
			sc.close();
			System.out.println(fileName + " loaded!");
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not founded: "+fileName);
		}
	}
	
	/**
	 * Metodo para inicializacao dos neuronios
	 * @param trainSet
	 */
	private void initializeNeurons(DataSet trainSet) {
		// Inicializa em posicoes randomicas ou zero
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
			//Inicializa pesos randomicamente
			if(iniMethod == LVQIniMethod.RANDOM)
				neurons[i] = new LVQNeuron(actualClassIndex, trainSet.attrib_count);
			//Inicializa pesos em zero
			else
			{
				double[] zeroVector = new double[trainSet.attrib_count+1];
				zeroVector[trainSet.classAttributteIndex] = actualClassIndex;
				neurons[i] = new LVQNeuron(zeroVector, trainSet.classAttributteIndex);
			}
		}
		
		//Se for First Values, procura os primeiros valores de cada classe
		if(iniMethod == LVQIniMethod.FIRST_VALUES)
		{
			trainSet.reset();
			for(int i = 0; i < neurons.length; i++)
			{
				LVQNeuron neuron = neurons[i];
				
				//Se for um neuronio de classe diferente do anterior, volta a contar do inicio
				if(i-1 > -1)
				{
					if(neuron._class != neurons[i-1]._class)
					{
						trainSet.reset();
					}
				}
				
				//Posiciona neuronio na posicao da primeira entrada encontrada
				while(trainSet.hasNext())
				{
					LVQNeuron in = new LVQNeuron(trainSet.next(), trainSet.classAttributteIndex);
					if(in._class == neuron._class)
					{
						neuron.vector = in.vector;
						break;
					}
				}
			}
		}
		
	}
	
	@Override
	public void train(DataSet trainSet, DataSet validateSet) {
		System.out.println("Training");
		
		// PASSO 0 inicializar todos os pesos
		initializeNeurons(trainSet);
		
		
		/*
			x – vetor de treinamento (x1, ..., xi, ..., xn)
			T – classe correta para o vetor de treinamento
			wj – vetor peso da j-ésima unidade de saída (w1,j, ..., wi,j, ..., wn,j)
			Cj – classe representada pela j-ésima unidade de saída
			║ x – wj ║ - distância Euclidiana entre o vetor de entrada e (vetor de pesos para) o j-ésimo vetor de saída.
			J - uma unidade de saída
		*/
		
		// 1- Enquanto condicao de parada eh falsa execute os passos 2-6
		int EpochsCounter = 0;
		double actualLearnRate = learnRate;
		double actualErrorVariation = Double.MAX_VALUE; //Variação atual do erro, se tiver menor que o limiar, treinamento para
		
		//Erros
		double actualError = validate(validateSet);
		double lastError = actualError;
		System.out.println("Erro inicial (pos-inicializacao): " + actualError);
		
		
		while (actualErrorVariation > stopLimiar && actualError < 100.0 /*&& EpochsCounter < 1000*/) {
			
			if(actualLearnRate <= 0.0)
			{
				System.out.print("TAXA MENOR QUE ZERO!");
				break;
			}
			
			//Erro do treino
			double trainError = 0.0;
			
			System.out.println("Epoca: " + EpochsCounter +" Taxa de aprendizado em: " + actualLearnRate);
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
					trainError += minDistance;
					selectedNeuron.aproach(neuronDataLine, actualLearnRate);
				} else {
					trainError += calculateError(neuronDataLine);
					selectedNeuron.diverge(neuronDataLine, actualLearnRate);
				}
			}
			
			// Reseta Train set para um possivel novo treinamento
			trainSet.reset();
			
			//Média do erro de treinamento
			trainError /= trainSet.size();
			
			// 5 - Reduza a taxa de aprendizado (?) como?
			//Reduz linearmente
			//actualLearnRate = learnRate * ((double)(stopCondition - EpochsCounter)/(double)stopCondition);
			actualLearnRate = learnRate * Math.pow(Math.E, -1 * (EpochsCounter/reductionRate));

			/* 6
				Teste a condição de parada
				A condição deve especificar um número fixo de iterações (i.e.,execução do Passo 1) 
				ou um valor mínimo para a taxa de aprendizado. 
			 */
			double validationError = validate(validateSet);
			logError(EpochsCounter, trainError ,validationError);
			EpochsCounter++;
			
			// Atualiza a checagem do limiar de variacao
			if(EpochsCounter % 5 == 0)
			{
				lastError = actualError;
				actualError = validationError;
				
				actualErrorVariation = Math.abs(lastError - actualError); //Pega o módulo de erro anterior - erro atual
				System.out.println("validando: " + actualError);
			}
		}
	}

	@Override
	public double validate(DataSet validateSet) {
		//System.out.println("Validating");
		
		double totalError = 0;
		validateSet.reset();
		while(validateSet.hasNext())
		{
			LVQNeuron input = new LVQNeuron(validateSet.next(), validateSet.classAttributteIndex);
			double minDistance = calculateError(input);
			totalError += minDistance;
			
		}
		
		//Retorna erro médio
		return totalError/validateSet.size();
	}
	
	@Override
	public TestData test(DataSet testSet) {
		System.out.println("Testing");
		TestData test = new TestData(testSet.class_count);
		//testSet.printClassDistribution(testSet.classAttributteIndex);
		while (testSet.hasNext()) {
			LVQNeuron selectedNeuron = neurons[0];
			LVQNeuron neuronDataLine = new LVQNeuron(testSet.next(), testSet.classAttributteIndex);

			//Procura neurônio de saída mais próximo
			double minDistance = Double.MAX_VALUE;
			double temp;
			for (int i = 0; i < neurons.length; i++) {
				temp = neurons[i].distanceFrom(neuronDataLine);
				if (temp < minDistance) {
					minDistance = temp;
					selectedNeuron = neurons[i];
				}
			}
			
			//Guarda resultado do teste
			test.test(selectedNeuron._class , neuronDataLine._class);
		}
		
		test.printResults();
		return test;
	}
	
	/**
	 * Calcula o erro para determinado neurônio de entrada
	 * @param input
	 * @return double
	 */
	private double calculateError(LVQNeuron input)
	{
		boolean found = false;
		double minDistance = 0.0;
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
		
		if(!found)
			System.out.println("Class not found!" + input._class);
		
		return minDistance;
	}

	/**
	 * Salva Rede LVQ em um arquivo .lvq
	 * @param fileName
	 */
	public void save(String fileName) {
		try {
			System.out.println("Saving LVQ " + fileName + "...");
			FileWriter file = new FileWriter(fileName);
			// Escrever
			/* Ordem
			 * learnRate (double
			 *	reductionRate (double)
			 *	stopLimiar (double)
			 *	neuronsCount (int)
			 *	LVQIniMethod (int)
			 *	Neurons (linha a linha) com os components
			 */
			
			StringBuilder builder = new StringBuilder();
			// Learn Rate
			builder.append(learnRate + ",");
			// Reduction Rate
			builder.append(reductionRate + ",");
			// Stop Limiar
			builder.append(stopLimiar + ",");
			// Neurons Count
			builder.append(neuronsCount + ",");
			// Init Method
			builder.append(iniMethod.ordinal() + "\n");
			
			// Write Attributes
			file.write(builder.toString());
			
			// Write Neurons and Weights
			for (LVQNeuron neuron : neurons) { 
				StringBuilder builderNeuron = new StringBuilder();
				// Neuron class
				builderNeuron.append(neuron._class + ",");
				
				// Iterate vector components
				for (double component : neuron.vector.components) {
					builderNeuron.append(component + ",");
				}
				
				// Delete last comma
				builderNeuron.deleteCharAt(builderNeuron.length()-1);
				// Write Neuron Line
				file.write(builderNeuron.toString() + "\n");
			}
			
			file.close();
			System.out.println(fileName + " saved!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
