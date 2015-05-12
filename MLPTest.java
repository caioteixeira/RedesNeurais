class MLPTest{
	
	public static void main(String[]args){
		DataSet trainSet = new DataSet(-1, "xor.tra");
		trainSet.printClassDistribution();
		DataSet testSet = new DataSet(-1, "xor.tes");
		testSet.printClassDistribution();
		DataSet validateSet = new DataSet(-1, "xor.val");
		validateSet.printClassDistribution();
		
		Principal p = new Principal(1,2,2,1,false);
		for(int i = 0; i < 500;i++){
			p.train(trainSet);
		}
		p.test(testSet);
	}


}