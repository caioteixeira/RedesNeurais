import java.util.ArrayList;
import java.util.List;


public class MLPLayer {
	public List<MLPNeuron> neuronios;

	// List<Double> bias;
	public MLPLayer(int nNos, int numSinapse, boolean bias, boolean aleatorio) {
		neuronios = new ArrayList<MLPNeuron>();
		for (int i = 0; i < nNos; i++) {
			neuronios.add(new MLPNeuron(numSinapse, bias, aleatorio));
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
