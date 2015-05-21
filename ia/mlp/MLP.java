package ia.mlp;
import ia.data.Classifier;
import ia.data.DataSet;
import ia.data.TestData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Representa rede neural MLP
 */
public class MLP extends Classifier {
	
	// Lista de camadas da MLP
	List<MLPLayer> layers;
	
	// Taxa de aprendizado
	public double learnRate;
	
	// Total de camadas
	public int layersCount;
	
	// Numero de nos nas camadas de entrada, saida e escondida
	public int inputNodeCount;
	public int outputNodeCount;
	public int hiddenNodeCount;
	
	// Usar bias
	public boolean bias;
	
	// Usar random na inicializacao dos pesos - caso false usa zero
	public boolean random;
	
	// Contadores de erros e acertos
	int erros;
	int acertos;
	
	/**
	 * Construtor padrao da MLP que cria uma nova rede MLP conforme os atributos passados
	 * @param nHidden
	 * @param inputNodeCount
	 * @param outputNodeCount
	 * @param hiddenNodeCount
	 * @param bias
	 * @param learnRate
	 * @param random
	 */
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
		
		// Criando camada de entrada
		layers.add(new MLPLayer(inputNodeCount, hiddenNodeCount, bias,random)); 
		
		// Cria ate o penultimo nivel de camadas escondidas
		for (int i = 0; i < nHidden - 1; i++) {
			layers.add(new MLPLayer(hiddenNodeCount, hiddenNodeCount, bias,random));
		}
		
		// Cria ultima camada escondida
		layers.add(new MLPLayer(hiddenNodeCount, outputNodeCount, bias,random));
		// Cria camada de saida
		layers.add(new MLPLayer(outputNodeCount, 0, bias,random));
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
			
			// Le e popula atributos da rede mlp
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
				// Le linha
				String neuronLine = sc.nextLine();
				// Pega neuronios daquela linha
				String[] neurons = neuronLine.split(";");
				
				List<MLPNeuron> neuronsList = new ArrayList<MLPNeuron>();
				for (String neuron : neurons) {
					String[] components = neuron.split(",");
					
					// Componentes do neuronio
					MLPNeuron neuronComponent;
					List<Double> weightsList = new ArrayList<Double>();
					
					// Index para contar bias ou nao
					int i = 0;
					
					// Validacao para ver se tem bias e criar MLPNeuron
					if (bias) {
						double biasValue = Double.parseDouble(components[0]);
						i = 1;
						neuronComponent = new MLPNeuron(biasValue, weightsList);
					} else {
						neuronComponent = new MLPNeuron(weightsList);
					}
					
					// Seta Pesos do neuronio
					for (; i < components.length;i++) {
						weightsList.add(Double.parseDouble(components[i]));
					}
					
					// Adiciona neuronio
					neuronsList.add(neuronComponent);
				}
				
				// Adiciona camada passando a lista de neuronios
				layersList.add(new MLPLayer(neuronsList));
			}
			
			// Seta camadas da rede MLP
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

	/**
	 * Calcula a diferenca entre a camada de saida e a resposta esperada
	 * @param respostaEsperada
	 * @return
	 */
	public double calculaErro(double[] respostaEsperada) {
		MLPLayer saida = layers.get(layers.size() - 1);
		double erro = 0;
		List<MLPNeuron> neuronios = saida.neuronios;
		for (int i = 0; i < neuronios.size(); i++) {
			MLPNeuron neuronio = neuronios.get(i);
			
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
			if (neuronio.hasBias)
				neuronio.erroBias = learnRate * erroDelta;

		}
		// System.out.println("Erro: "+erro);
		return erro;
	}

	/**
	 * Calcula o erro da camada oculta
	 * @param i
	 */
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
			if (neuronio.hasBias)
				neuronio.erroBias = learnRate * erroDelta;
		}
	}
	
	/**
	 * Metodo de feedForward
	 */
	public void feedForward() {
		/*
		 * Inicia em 1 devido aos neuronios
		 * da camada de entrada nao terem
		 * neuronios camadas abaixo
		 */
		for (int i = 1; i < layers.size(); i++) {
			// Seleciona o nivel abaixo
			MLPLayer entrada = layers.get(i - 1);
			// Seleciona o nivel atual
			MLPLayer saida = layers.get(i);

			Iterator<MLPNeuron> itSaida = saida.neuronios.iterator();
			int j = 0;
			// Percorre todos os neuronios do nivel atual
			while (itSaida.hasNext()) {
				MLPNeuron aux = itSaida.next();
				Iterator<MLPNeuron> itEntrada = entrada.neuronios.iterator();
				
				/* Percorre todos os neuronios do nivel abaixo 
				 * e adiciona o valor com o peso
				 * ao j-esimo neuronio do nivel atual
				 */
				while (itEntrada.hasNext()) {
					MLPNeuron calc = itEntrada.next();
					aux.valor += calc.saida * calc.pesos.get(j);
				}
				
				if (aux.hasBias) {
					aux.valor += aux.bias;
				}
				
				// Calcula o valor de saida apos a funcao de ativacao
				aux.saida = saida.ativ(aux.valor);
				
				j++;
			}
		}
	}
	
	/**
	 * Atualiza pesos e bias
	 */
	public void update() {
		double novoPeso;
		for (int j = layers.size() - 1; j >= 0; j--) {
			MLPLayer camada = layers.get(j);
			List<MLPNeuron> neuronios = camada.neuronios;
			for (MLPNeuron neuronio : neuronios) {
				for (int k = 0; k < neuronio.pesos.size(); k++) {
					novoPeso = neuronio.pesos.get(k) + neuronio.erroPeso.get(k);
					neuronio.pesos.set(k, novoPeso);
					if (neuronio.hasBias)
						neuronio.bias += neuronio.erroBias;
				}
			}
		}
		apagaErro();
	}
	
	/**
	 * Apaga erro dos neuronios das camadas da MLP
	 */
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
	
	/**
	 * Recebe trainSet e validateSet para treinar rede MLP
	 * @param trainSet
	 * @param validateSet
	 */
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
			//System.out.println("Erro Treinamento: " +erroTreino);
			
			double erro = validate(validateSet);
			logError(epoca,erroTreino,erro);
			
			if(epoca % 10 == 0){
				erroValidacao = erroAtual;
				erroAtual = erro;
				if(erroAtual > erroValidacao) cont++;
				else cont = 0;
				//System.out.println("Contador: "+cont);
				//System.out.println("Erro Validacao: "+erroAtual);
								
			}
			epoca++;
			
			//logError(epoca, erroTreino);
			//apagaErro();
			// return erro;
			// if(imprimePesos) printPeso();
		}
	}
	
	/**
	 * Valida rede MLP
	 * @param dados
	 */
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
		//System.out.println("Erro Validacao: "+erro);
		return erro;
	}
	
	/**
	 * Testa rede MLP
	 * @param dados
	 */
	public TestData test(DataSet dados) {
		//System.out.println("Teste");
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
	
	
	/**
	 * Metodo para verificar os pesos/bias de todos os neuronios
	 */
	public void printPeso() {
		for (int i = 0; i < layers.size(); i++) {
			//Sytem.out.println("Layer: "+i);
			MLPLayer imp = layers.get(i);
			System.out.println("Layer: " + i);
			for (int j = 0; j < imp.neuronios.size(); j++) {
				MLPNeuron n = imp.neuronios.get(j);
				System.out.println("Neuronio: " + j);
				for (int k = 0; k < n.pesos.size(); k++) {
					System.out.println("Pesos: ");
					System.out.println(n.pesos.get(k));
				}
				System.out.println("Bias "+n.bias);
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
			
			// Taxa de aprendizado
			builder.append(learnRate + ",");
			
			// Numero de camadas
			builder.append(layersCount + ",");
			
			// Numero de nos da camada de entrada
			builder.append(inputNodeCount + ",");
			
			// Numero de nos da camada de saida
			builder.append(outputNodeCount + ",");
			
			// Numero de nos da camada escondida
			builder.append(hiddenNodeCount + ",");
									
			// Bias
			builder.append(bias + ",");
			
			// Inicializacao de pesos random
			builder.append(random + "\n");
			
			// Escreve atributos
			file.write(builder.toString());
			
			// Escreve Camadas, Neuronios e pesos
			for (MLPLayer layer : layers) {
				for (MLPNeuron neuron : layer.neuronios) {
					StringBuilder builderNeuron = new StringBuilder();
					// Se bias... Salva bias.
					if (bias) {
						// Bias
						builderNeuron.append(neuron.bias + ",");
					}
					
					for (double weight : neuron.pesos) {
						builderNeuron.append(weight + ",");
					}
					
					// Deleta ultima virgula
					builderNeuron.deleteCharAt(builderNeuron.length()-1);
					
					// Escreve linha do neuronio
					file.write(builderNeuron.toString() + ";");
				}
				
				// Escreve linha da camada
				file.write("\n");
			}			
			file.close();
			System.out.println(fileName + " saved!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
