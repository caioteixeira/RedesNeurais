class MLPTest{
	
	public static void main(String[]args){
		DataSet trainSet = new DataSet(-1, "optdigits.norm.cortado.tra");
		trainSet.printClassDistribution();
		DataSet testSet = new DataSet(-1, "optdigits.norm.cortado.tes");
		testSet.printClassDistribution();
		DataSet validateSet = new DataSet(-1, "optdigits.norm.cortado.val");
		validateSet.printClassDistribution();
		boolean print = false;
		Principal p = new Principal(1,61,4,40,true);
		for(int i = 0; i < 1000;i++){
			System.out.println("Epoca: "+i);
			//if(i == 9999) print = true;
			p.train(trainSet,print);
			trainSet.reset();
		}
		//p.apagaErro();
		p.test(testSet,false);
		
	}


}