import java.util.ArrayList;
import java.util.List;

/**
 * Representa camada da rede MLP
 */
public class MLPLayer {
	
	// Lista de neuronios da camada
	public List<MLPNeuron> neuronios;
	
	/**
	 * Construtor padrao da camada da MLP
	 * @param nNos
	 * @param numSinapse
	 * @param bias
	 * @param aleatorio
	 */
	public MLPLayer(int nNos, int numSinapse, boolean bias, boolean aleatorio) {
		neuronios = new ArrayList<MLPNeuron>();
		for (int i = 0; i < nNos; i++) {
			neuronios.add(new MLPNeuron(numSinapse, bias, aleatorio));
		}
	}
	
	/**
	 * Construtor de camada MLP passando uma lista de neuronios
	 * @param neurons
	 */
	public MLPLayer(List<MLPNeuron> neurons) {
		this.neuronios = neurons;
	}
	
	/**
	 * Calcula ativacao
	 * @param valor
	 * @return
	 */
	public double ativ(double valor) {
		return sigmoid(valor);
	}
	
	/**
	 * Calcula Sigmoid
	 * @param valor
	 * @return
	 */
	public double sigmoid(double valor) {
		return 1 / (1 + Math.exp(-3 * valor));

	}
	
	/**
	 * Calcula Derivada
	 * @param valor
	 * @return
	 */
	public double derivada(double valor) {
		double fx = sigmoid(valor);
		return 3 * fx * (1 - fx);
	}

}
