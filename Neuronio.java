import java.util.*;

class Neuronio {

	// ARMAZENA AQUELA PARTE DA ENTRADA QUE ELE MULTIPLICA PESO E PASSA PARA A
	// FUNCAO DE ATIVACAO
	double valor;
	List<Double> pesos;
	// ARMAZENA O RESULTADO DA FUNCAO DE ATIVACAO
	double saida;
	// ARMAZENA A DIFERENCA PARA FACILITAR CODIGO. novoPeso =
	// erroPeso.get(i)+pesos.get(i)
	List<Double> erroPeso;
	// E O MESMO ESQUEMA PARA OS PESOS
	double erroBias;
	// CADA NEURONIO ARMAZENA SEU erroDelta
	double erroDelta;

	double bias;

	boolean temBias;

	public Neuronio(int numSinapse, boolean bias, boolean aleatorio) {
		this.pesos = new ArrayList<Double>();
		this.erroPeso = new ArrayList<Double>();
		if(aleatorio){
			for (int i = 0; i < numSinapse; i++) {
				pesos.add(-1 + 2 * Math.random());

			}
		}
		else{

			for (int i = 0; i < numSinapse; i++) {
				pesos.add((double) 0);

			}

		}
		temBias = bias;
		this.bias = -0.5 + Math.random();

	}
}

class Layer {
	List<Neuronio> neuronios;

	// List<Double> bias;
	public Layer(int nNos, int numSinapse, boolean bias, boolean aleatorio) {
		neuronios = new ArrayList<Neuronio>();
		for (int i = 0; i < nNos; i++) {
			neuronios.add(new Neuronio(numSinapse, bias, aleatorio));
		}

	}

	public double ativ(double valor) {
		return sigmoid(valor);

	}

	public double sigmoid(double valor) {
		return 1 / (1 + Math.exp(-3 * valor));

	}

	public double derivada(double valor) {
		double fx = sigmoid(valor);
		return 3 * fx * (1 - fx);
	}

}

class Principal extends Classifier {
	List<Layer> layers;
	public double taxaDeAprendizagem;
	public int nHidden;
	int erros;
	int acertos;

	public Principal(int nHidden, int nosEntrada, int nosSaida, int nosHidden,
			boolean bias, double taxa, boolean aleatorio) {
		this.erros = 0;
		this.acertos = 0;
		this.nHidden = nHidden;
		this.taxaDeAprendizagem = taxa;
		layers = new ArrayList<Layer>();
		layers.add(new Layer(nosEntrada, nosHidden, bias,aleatorio)); // cria camada de
															// entrada

		for (int i = 0; i < nHidden - 1; i++) {

			layers.add(new Layer(nosHidden, nosHidden, bias,aleatorio)); // cria ate o
																// penultimo
																// nivel de
																// camadas
																// escondidas

		}

		layers.add(new Layer(nosHidden, nosSaida, bias,aleatorio)); // cria ultima camada
															// escondida
		layers.add(new Layer(nosSaida, 0, bias,aleatorio)); // cria camada de saida

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
		Layer saida = layers.get(layers.size() - 1);
		double erro = 0;
		List<Neuronio> neuronios = saida.neuronios;
		for (int i = 0; i < neuronios.size(); i++) {
			Neuronio neuronio = neuronios.get(i);
			// verificar com o guilherme se o neuronio.valor é a mesma coisa q
			// o somatório dos pesos.
			double erroDelta = (respostaEsperada[i] - neuronio.saida)
					* saida.derivada(neuronio.valor);
			neuronio.erroDelta = erroDelta;
			erro += Math.pow(respostaEsperada[i] - neuronio.saida, 2);
			// System.out.print("Resposta: "+neuronio.saida+" Esperado: "+respostaEsperada[i]);
			// System.out.println("");
			Layer oculta = layers.get(layers.size() - 2);
			List<Neuronio> listNeuronioOculto = oculta.neuronios;
			for (int j = 0; j < listNeuronioOculto.size(); j++) {
				Neuronio neuronioOculto = listNeuronioOculto.get(j);
				neuronioOculto.erroPeso.add(taxaDeAprendizagem * erroDelta
						* neuronioOculto.saida);
			}
			if (neuronio.temBias)
				neuronio.erroBias = taxaDeAprendizagem * erroDelta;

		}
		// System.out.println("Erro: "+erro);
		return erro;
	}

	// PARA CADA CAMDA OCULTA SOMAR OS DELTAS DA CAMADA ACIMA
	public void calcularErroCamadaOculta(int i) {
		Layer saida = layers.get(i + 1);
		Layer oculta = layers.get(i);
		Layer entrada = layers.get(i - 1);
		List<Neuronio> listNeuronioSaida = saida.neuronios;
		List<Neuronio> listNeuronioOculta = oculta.neuronios;
		List<Neuronio> listNeuronioEntrada = entrada.neuronios;

		for (int j = 0; j < listNeuronioOculta.size(); j++) {
			Neuronio neuronio = listNeuronioOculta.get(j);
			double soma = 0;
			for (int k = 0; k < listNeuronioSaida.size(); k++) {
				soma += listNeuronioSaida.get(k).erroDelta
						* neuronio.pesos.get(k);
			}
			double erroDelta = soma * oculta.derivada(neuronio.valor);
			neuronio.erroDelta = erroDelta;

			for (int l = 0; l < listNeuronioEntrada.size(); l++) {
				Neuronio neuronioEntrada = listNeuronioEntrada.get(l);
				neuronioEntrada.erroPeso.add(erroDelta * taxaDeAprendizagem
						* neuronioEntrada.saida);

			}
			if (neuronio.temBias)
				neuronio.erroBias = taxaDeAprendizagem * erroDelta;
		}
	}

	public void feedForward() {

		for (int i = 1; i < layers.size(); i++) { // inicia em 1 devido aos
													// neuronios da camada de
													// entrada nao terem
													// neuronios camadas abaixo

			Layer entrada = layers.get(i - 1); // seleciona o nivel abaixo
			Layer saida = layers.get(i); // seleciona o nivel atual

			Iterator<Neuronio> itSaida = saida.neuronios.iterator();
			int j = 0;
			while (itSaida.hasNext()) { // percorre todos os neuronios do nivel
										// atual
				Neuronio aux = itSaida.next();
				Iterator<Neuronio> itEntrada = entrada.neuronios.iterator();

				while (itEntrada.hasNext()) { // percorre todos os neuronios do
												// nivel abaixo e adiciona o
												// valor com o peso ao j-esimo
												// neuronio do nivel atual

					Neuronio calc = itEntrada.next();
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
			Layer camada = layers.get(j);
			List<Neuronio> neuronios = camada.neuronios;
			for (Neuronio neuronio : neuronios) {
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
			Layer nivel = layers.get(i);
			List<Neuronio> neuronio = nivel.neuronios;
			for (Neuronio apaga : neuronio) {
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
				Layer entrada = this.layers.get(0);
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
			Layer entrada = this.layers.get(0);
			for (int i = 0; i < entrada.neuronios.size(); i++) {
				entrada.neuronios.get(i).valor = atributos[i];
				entrada.neuronios.get(i).saida = atributos[i];

			}
			feedForward();
			double classe = atributos[dados.classAttributteIndex];
			double[] resp = respostaEsperada(classe, layers.get(layers.size() - 1).neuronios.size());
			Layer saida = layers.get(layers.size() - 1);
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
			Layer entrada = layers.get(0);
			for (int i = 0; i < entrada.neuronios.size(); i++) {
				entrada.neuronios.get(i).valor = atributos[i];
				entrada.neuronios.get(i).saida = atributos[i];

			}
			feedForward();
			double classe = atributos[dados.classAttributteIndex];
			//System.out.println(classe);
			double[] resp = respostaEsperada(classe,layers.get(layers.size() - 1).neuronios.size());
			double[]respostaRede = new double[layers.get(layers.size() - 1).neuronios.size()];
			Layer saida = layers.get(layers.size() - 1);
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
			Layer imp = layers.get(i);
			System.out.println("Layer: " + i);
			for (int j = 0; j < imp.neuronios.size(); j++) {
				Neuronio n = imp.neuronios.get(j);
				System.out.println("Neuronio: " + j);
				for (int k = 0; k < n.pesos.size(); k++) {
					System.out.println("Pesos: ");
					System.out.println(n.pesos.get(k));

				}

			}

		}

	}
}
