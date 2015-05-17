import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class MLP extends Classifier {
	List<MLPLayer> layers;
	public double learnRate;
	public int layersCount;
	
	public int inputNodeCount;
	public int outputNodeCount;
	public int hiddenNodeCount;
	public boolean bias;
	public boolean random;
	
	int erros;
	int acertos;

	public MLP(int nHidden, int inputNodeCount, int outputNodeCount, int hiddenNodeCount,
			boolean bias, double learnRate, boolean random) {
		this.erros = 0;
		this.acertos = 0;
		this.layersCount = nHidden;
		this.learnRate = learnRate;
		this.inputNodeCount = inputNodeCount;
		this.outputNodeCount = outputNodeCount;
		this.hiddenNodeCount = hiddenNodeCount;
		this.bias = bias;
		this.random = random;
		
		layers = new ArrayList<MLPLayer>();
		layers.add(new MLPLayer(inputNodeCount, hiddenNodeCount, bias,random)); // cria camada de
															// entrada

		for (int i = 0; i < nHidden - 1; i++) {

			layers.add(new MLPLayer(hiddenNodeCount, hiddenNodeCount, bias,random)); // cria ate o
																// penultimo
																// nivel de
																// camadas
																// escondidas

		}

		layers.add(new MLPLayer(hiddenNodeCount, outputNodeCount, bias,random)); // cria ultima camada
															// escondida
		layers.add(new MLPLayer(outputNodeCount, 0, bias,random)); // cria camada de saida

	}
	
	/**
	 * Construtor da MLP a partir de um arquivo .mlp
	 * @param fileName
	 */
	public MLP(String fileName) {
		/* Ordem
		 *  learnRate (double)
		 *	layersCount (int)
		 *	inputNodeCount (int)
		 *	outputNodeCount (int)
		 *  hiddenNodeCount (int)
		 *  bias (boolean)
		 *	random (boolean)
		 *	Layers, Neurons (linha a linha) com os components
		 */
		try
		{
		System.out.println("Loading MLP Network...");
		File f = new File(fileName);
		Scanner sc = new Scanner(f);
		
		// Read and populate attribs
		String[] attribs = sc.nextLine().split(",");
		this.learnRate = Double.valueOf(attribs[0]);
		this.layersCount = Integer.valueOf(attribs[1]);
		this.inputNodeCount = Integer.valueOf(attribs[2]);
		this.outputNodeCount = Integer.valueOf(attribs[3]);
		this.hiddenNodeCount = Integer.valueOf(attribs[4]);
		this.bias = Boolean.valueOf(attribs[5]);
		this.random = Boolean.valueOf(attribs[6]);
		
		List<MLPLayer> layersList = new ArrayList<MLPLayer>();
		while(sc.hasNext())
		{
			// Read Line
			String neuronLine = sc.nextLine();
			//String[] neurons = neuronLine.split(";");
			
			/*layersList.add(new MLPLayer(nNos, numSinapse, bias, aleatorio))
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
			neuronsList.add(neuron);*/
		}
		
		// Set Layers in MLP Network
		this.layers = layersList;
		
		sc.close();
		System.out.println(fileName + " loaded!");
	}
	catch(FileNotFoundException e)
	{
		System.out.println("File not founded: "+fileName);
	}
	}

	public double[] respostaEsperada(double classe, int numNeuronios) {
		double[] resp = new double[numNeuronios];
		int trunca = (int) classe;

		switch (trunca) {
		case 1:
			resp[0] = 1;
			break;

		case 2:
			resp[1] = 1;
			break;

		case 3:
			resp[0] = 1;
			resp[1] = 1;
			break;

		case 4:
			resp[2] = 1;
			break;

		case 5:
			resp[0] = 1;
			resp[2] = 1;
			break;

		case 6:
			resp[1] = 1;
			resp[2] = 1;
			break;

		case 7:
			resp[0] = 1;
			resp[1] = 1;
			resp[2] = 1;
			break;

		case 8:
			resp[3] = 1;
			break;

		case 9:
			resp[0] = 1;
			resp[3] = 1;

		default:
			break;
		}
		// resp[0] = classe;

		// else resp[0] = 1;
		return resp;

	}

	// faz a diferenca entre a camada de saida e a resposta esperada
	public double calculaErro(double[] respostaEsperada) {
		MLPLayer saida = layers.get(layers.size() - 1);
		double erro = 0;
		List<MLPNeuron> neuronios = saida.neuronios;
		for (int i = 0; i < neuronios.size(); i++) {
			MLPNeuron neuronio = neuronios.get(i);
			// verificar com o guilherme se o neuronio.valor é a mesma coisa q
			// o somatório dos pesos.
			double erroDelta = (respostaEsperada[i] - neuronio.saida)
					* saida.derivada(neuronio.valor);
			neuronio.erroDelta = erroDelta;
			erro += Math.pow(respostaEsperada[i] - neuronio.saida, 2);
			// System.out.print("Resposta: "+neuronio.saida+" Esperado: "+respostaEsperada[i]);
			// System.out.println("");
			MLPLayer oculta = layers.get(layers.size() - 2);
			List<MLPNeuron> listNeuronioOculto = oculta.neuronios;
			for (int j = 0; j < listNeuronioOculto.size(); j++) {
				MLPNeuron neuronioOculto = listNeuronioOculto.get(j);
				neuronioOculto.erroPeso.add(learnRate * erroDelta
						* neuronioOculto.saida);
			}
			if (neuronio.temBias)
				neuronio.erroBias = learnRate * erroDelta;

		}
		// System.out.println("Erro: "+erro);
		return erro;
	}

	// PARA CADA CAMDA OCULTA SOMAR OS DELTAS DA CAMADA ACIMA
	public void calcularErroCamadaOculta(int i) {
		MLPLayer saida = layers.get(i + 1);
		MLPLayer oculta = layers.get(i);
		MLPLayer entrada = layers.get(i - 1);
		List<MLPNeuron> listNeuronioSaida = saida.neuronios;
		List<MLPNeuron> listNeuronioOculta = oculta.neuronios;
		List<MLPNeuron> listNeuronioEntrada = entrada.neuronios;

		for (int j = 0; j < listNeuronioOculta.size(); j++) {
			MLPNeuron neuronio = listNeuronioOculta.get(j);
			double soma = 0;
			for (int k = 0; k < listNeuronioSaida.size(); k++) {
				soma += listNeuronioSaida.get(k).erroDelta
						* neuronio.pesos.get(k);
			}
			double erroDelta = soma * oculta.derivada(neuronio.valor);
			neuronio.erroDelta = erroDelta;

			for (int l = 0; l < listNeuronioEntrada.size(); l++) {
				MLPNeuron neuronioEntrada = listNeuronioEntrada.get(l);
				neuronioEntrada.erroPeso.add(erroDelta * learnRate
						* neuronioEntrada.saida);

			}
			if (neuronio.temBias)
				neuronio.erroBias = learnRate * erroDelta;
		}
	}

	public void feedForward() {

		for (int i = 1; i < layers.size(); i++) { // inicia em 1 devido aos
													// neuronios da camada de
													// entrada nao terem
													// neuronios camadas abaixo

			MLPLayer entrada = layers.get(i - 1); // seleciona o nivel abaixo
			MLPLayer saida = layers.get(i); // seleciona o nivel atual

			Iterator<MLPNeuron> itSaida = saida.neuronios.iterator();
			int j = 0;
			while (itSaida.hasNext()) { // percorre todos os neuronios do nivel
										// atual
				MLPNeuron aux = itSaida.next();
				Iterator<MLPNeuron> itEntrada = entrada.neuronios.iterator();

				while (itEntrada.hasNext()) { // percorre todos os neuronios do
												// nivel abaixo e adiciona o
												// valor com o peso ao j-esimo
												// neuronio do nivel atual

					MLPNeuron calc = itEntrada.next();
					aux.valor += calc.saida * calc.pesos.get(j);
				}
				if (aux.temBias)
					aux.valor += aux.bias; // ADRIANO E ASSIM A SOMA DO
											// BIAS?????
				aux.saida = saida.ativ(aux.valor); // calcula o valor de saida
													// apos a funcao de ativacao
				j++;
			}
		}
	}

	public void update() {
		double novoPeso;
		for (int j = layers.size() - 1; j >= 0; j--) {
			MLPLayer camada = layers.get(j);
			List<MLPNeuron> neuronios = camada.neuronios;
			for (MLPNeuron neuronio : neuronios) {
				for (int k = 0; k < neuronio.pesos.size(); k++) {
					novoPeso = neuronio.pesos.get(k) + neuronio.erroPeso.get(k);
					neuronio.pesos.set(k, novoPeso);
					if (neuronio.temBias)
						neuronio.bias += neuronio.erroBias;
				}
			}
		}
		apagaErro();
	}

	public void apagaErro() {
		for (int i = 0; i < layers.size(); i++) {
			MLPLayer nivel = layers.get(i);
			List<MLPNeuron> neuronio = nivel.neuronios;
			for (MLPNeuron apaga : neuronio) {
				apaga.erroPeso.clear();
				apaga.valor = 0;
				apaga.saida = 0;

			}

		}

	}

	public void train(DataSet trainSet, DataSet validateSet) {
		// System.out.println("Treinamento");
		int epoca = 1;
		double erroValidacao = validate(validateSet);
		double erroAtual = erroValidacao;
		int cont = 0;
		while (cont < 3) {
			System.out.println("Epoca: "+epoca);
			double erroTreino = 0;
			while (trainSet.hasNext()) {
				double[] atributos = trainSet.next();
				MLPLayer entrada = this.layers.get(0);
				for (int i = 0; i < entrada.neuronios.size(); i++) {
					entrada.neuronios.get(i).valor = atributos[i];
					entrada.neuronios.get(i).saida = atributos[i];
					// System.out.println(entrada.neuronios.get(i).saida);

				}
				// System.out.println("FIm");
				feedForward();
				double classe = atributos[trainSet.classAttributteIndex];
				// System.out.println("Classe: "+classe);
				double[] resp = respostaEsperada(classe,
						layers.get(layers.size() - 1).neuronios.size());

				erroTreino += calculaErro(resp);
				for (int i = this.layers.size() - 2; i > 0; i--) {
					calcularErroCamadaOculta(i);

				}
				update();
			}
			trainSet.reset();
			System.out.println("Erro Treinamento: " +erroTreino);
			
			double erro = validate(validateSet);
			logError(epoca,erroTreino,erro);
			
			if(epoca % 10 == 0){
				erroValidacao = erroAtual;
				erroAtual = erro;
				if(erroAtual > erroValidacao) cont++;
				else cont = 0;
				System.out.println("Contador: "+cont);
				//System.out.println("Erro Validacao: "+erroAtual);
								
			}
			epoca++;
			
			//logError(epoca, erroTreino);
			//apagaErro();
			// return erro;
			// if(imprimePesos) printPeso();
		}
	}

	public double validate(DataSet dados) {
		// System.out.println("Validacao");
		double erro = 0;
		//int numDados = 0;
		while (dados.hasNext()) {
			//numDados++;
			double[] atributos = dados.next();
			MLPLayer entrada = this.layers.get(0);
			for (int i = 0; i < entrada.neuronios.size(); i++) {
				entrada.neuronios.get(i).valor = atributos[i];
				entrada.neuronios.get(i).saida = atributos[i];

			}
			feedForward();
			double classe = atributos[dados.classAttributteIndex];
			double[] resp = respostaEsperada(classe, layers.get(layers.size() - 1).neuronios.size());
			MLPLayer saida = layers.get(layers.size() - 1);
			for (int i = 0; i < saida.neuronios.size(); i++) {
				erro += Math.pow((resp[i] - saida.neuronios.get(i).saida), 2);

			}

			apagaErro();
		}
		dados.reset();
		System.out.println("Erro Validacao: "+erro);
		return erro;
	}

	public TestData test(DataSet dados) {
		System.out.println("Teste");
		TestData estatistica = new TestData(10);
		while (dados.hasNext()) {
			double[] atributos = dados.next();
			MLPLayer entrada = layers.get(0);
			for (int i = 0; i < entrada.neuronios.size(); i++) {
				entrada.neuronios.get(i).valor = atributos[i];
				entrada.neuronios.get(i).saida = atributos[i];

			}
			feedForward();
			double classe = atributos[dados.classAttributteIndex];
			//System.out.println(classe);
			double[] resp = respostaEsperada(classe,layers.get(layers.size() - 1).neuronios.size());
			double[]respostaRede = new double[layers.get(layers.size() - 1).neuronios.size()];
			MLPLayer saida = layers.get(layers.size() - 1);
			double erro = 0;
			for (int i = 0; i < saida.neuronios.size(); i++) {
				erro += Math.abs(resp[i] - saida.neuronios.get(i).saida);
				if(saida.neuronios.get(i).saida < 0.5) respostaRede[i] = 0;
				else respostaRede[i] = 1;
				//System.out.print("Resposta: " +saida.neuronios.get(i).saida + " " +"Esperado: " +resp[i] + " " +"Erro: " +erro);
				//System.out.println("");

			}
			
			estatistica.test(classe, converteParaClasse(respostaRede));

			if (erro <= 0.5)
				acertos++;
			else
				erros++;
			// System.out.println("----------------------------------------------------------------");
			apagaErro();
		}
		System.out.println("Acertos: " + acertos);
		System.out.println("Erros: " + erros);
		// if(imprimePeso) printPeso();
		// System.out.println(layers.get(layers.size()-1).neuronios.get(0).bias);
		estatistica.printResults();
		estatistica.saveResults("MLP-matriz");
		return estatistica;

	}
	
	public int converteParaClasse(double[] resp){
		int soma = 0;
		for(int i = 0; i < resp.length; i++){
			if(resp[i] == 1){
				soma += Math.pow(2, i);
			}
		}
		
		return soma;
	}

	public void printPeso() {
		for (int i = 0; i < layers.size(); i++) {
			MLPLayer imp = layers.get(i);
			System.out.println("Layer: " + i);
			for (int j = 0; j < imp.neuronios.size(); j++) {
				MLPNeuron n = imp.neuronios.get(j);
				System.out.println("Neuronio: " + j);
				for (int k = 0; k < n.pesos.size(); k++) {
					System.out.println("Pesos: ");
					System.out.println(n.pesos.get(k));
				}
			}
		}
	}
	
	/**
	 * Salva Rede MLP em um arquivo .mlp
	 * @param fileName
	 */
	public void save(String fileName) {
		try {
			System.out.println("Saving MLP " + fileName + "...");
			FileWriter file = new FileWriter(fileName);

			/* Ordem
			 *  learnRate (double)
			 *	layersCount (int)
			 *	inputNodeCount (int)
			 *	outputNodeCount (int)
			 *  hiddenNodeCount (int)
			 *  bias (boolean)
			 *	random (boolean)
			 *	Layers, Neurons (linha a linha) com os components
			 */
		
			StringBuilder builder = new StringBuilder();
			// Learn Rate
			builder.append(learnRate + ",");
			
			// Layers Count
			builder.append(layersCount + ",");
			
			// Input Node Count
			builder.append(inputNodeCount + ",");
			
			// Output Node Count
			builder.append(outputNodeCount + ",");
			
			// Hidden Node Count
			builder.append(hiddenNodeCount + ",");
									
			// Bias
			builder.append(bias + ",");
			
			// Ini Random
			builder.append(random + "\n");
			
			// Write Attributes
			file.write(builder.toString());
			
			// Write Layers, Neurons and Weights
			for (MLPLayer layer : layers) {
				for (MLPNeuron neuron : layer.neuronios) {
					StringBuilder builderNeuron = new StringBuilder();
					// If bias... Save bias.
					if (bias) {
						// Bias
						builderNeuron.append(neuron.bias + ",");
					}
					
					for (double weight : neuron.pesos) {
						builderNeuron.append(weight + ",");
					}
					
					// Delete last comma
					builderNeuron.deleteCharAt(builderNeuron.length()-1);
					
					// Write Neuron Line
					file.write(builderNeuron.toString() + ";");
				}
				
				// Write Layer Line
				file.write("\n");
			}			
			file.close();
			System.out.println(fileName + " saved!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
