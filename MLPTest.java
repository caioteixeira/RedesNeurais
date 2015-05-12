class MLPTest{
	
	public static void main(String[]args){
		DataSet trainSet = new DataSet(-1, "optdigits.norm.cortado.tra");
		trainSet.printClassDistribution();
		DataSet testSet = new DataSet(-1, "optdigits.norm.cortado.tes");
		testSet.printClassDistribution();
		DataSet validateSet = new DataSet(-1, "optdigits.norm.cortado.val");
		validateSet.printClassDistribution();
		
		Principal p = new Principal(1,61,9,40,false);
		for(int i = 0; i < 500;i++){
			p.train(trainSet);
		}
		p.test(testSet);
	}


}