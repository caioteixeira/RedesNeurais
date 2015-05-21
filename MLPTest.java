import ia.data.DataSet;
import ia.mlp.MLP;

class MLPTest{
	
	public static void main(String[]args){
		DataSet trainSet = new DataSet(-1, "optdigits.norm.cortado.tra");
		trainSet.printClassDistribution();
		DataSet testSet = new DataSet(-1, "optdigits.norm.cortado.tes");
		testSet.printClassDistribution();
		DataSet validateSet = new DataSet(-1, "optdigits.norm.cortado.val");
		validateSet.printClassDistribution();
		//boolean print = false;
		MLP p = new MLP(1,61,4,40,true,0.5,true);
		p.train(trainSet, validateSet);
		//p.logError(i+1, erro);
		//trainSet.reset();
		//p.validate(validateSet);
		//p.saveTrainningLogFile("Erro-MLP.csv");
		//p.apagaErro();
		p.test(testSet);
		
	}


}