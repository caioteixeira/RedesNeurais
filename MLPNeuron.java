import java.util.*;

class MLPNeuron {

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

	public MLPNeuron(int numSinapse, boolean bias, boolean aleatorio) {
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