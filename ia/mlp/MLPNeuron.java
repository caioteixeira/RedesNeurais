package ia.mlp;
import java.util.*;

/**
 * Representa neuronio da rede MLP
 */
class MLPNeuron {

	// Valor
	double valor;
	// Lista de pesos
	public List<Double> pesos;
	
	// Armazena resultado da funcao de ativacao
	double saida;
	
	/*
	 * Armazena diferenca para facilitar o codigo
	 * novoPeso = erroPeso.get(i)+pesos.get(i)
	 */
	List<Double> erroPeso;
	double erroBias;
	
	// Armazenamento do erro delta
	double erroDelta;
	
	// Bias
	public double bias;
	
	// Possui bias
	boolean hasBias;
	
	/**
	 * Construtor padrao do neuronio MLP, contendo o numero de sinapses
	 * se tem bias ou nao
	 * se vai ter pesos com inicializacao aleatoria ou nao
	 * @param numSinapse
	 * @param hasBias
	 * @param aleatorio
	 */
	public MLPNeuron(int numSinapse, boolean hasBias, boolean aleatorio) {
		this.pesos = new ArrayList<Double>();
		this.erroPeso = new ArrayList<Double>();
		
		if(aleatorio) {
			for (int i = 0; i < numSinapse; i++) {
				pesos.add(-1 + 2 * Math.random());
			}
		} else {
			for (int i = 0; i < numSinapse; i++) {
				pesos.add((double) 0);
			}
		}
		
		this.hasBias = hasBias;
		this.bias = -0.5 + Math.random();
	}
	
	/**
	 * Construtor de neuronio a partir de uma lista de pesos
	 * @param pesos
	 */
	public MLPNeuron(List<Double> pesos) {
		this.pesos = pesos;
		this.erroPeso = new ArrayList<Double>();
	}
	
	/**
	 * Construtor de neuronio a partir de uma lista de pesos e bias
	 * @param bias
	 * @param pesos
	 */
	public MLPNeuron(double bias, List<Double> pesos) {
		this(pesos);
		this.hasBias = true;
		this.bias = bias;
	}
}