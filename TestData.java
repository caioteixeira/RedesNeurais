
public class TestData {
	int[][] confusionMatrix;
	int hits; //Erros
	int errors; //Acertos
	
	//nClasses: numero de Classes do dataset
	public TestData(int nClasses)
	{
		confusionMatrix = new int[nClasses][nClasses];
	}
	
	public void test(double selectedClass, double answerClass)
	{
		if(selectedClass == answerClass)
		{
			hits++;
		}
		else
		{
			errors++;
		}
		
		int i = (int) selectedClass;
		int j = (int) answerClass;
		confusionMatrix[i][j]++;
	}
	
	public void printResults()
	{
		System.out.println("Numero de testes: "+ (hits+errors));
		System.out.println("Numero de acertos: " + hits);
		System.out.println("Numero de erros " + errors);
		System.out.println("Porcentagem de acertos: "+ ((double)hits/(double)(hits+errors)));
		
		System.out.println("\nMatrix de confusao\n");
		for(int i = -1; i < confusionMatrix.length; i++)
			System.out.print((i>-1?i:"") + "\t");
		System.out.println("");
		for(int i = 0; i < confusionMatrix.length; i++)
		{
			for(int j = -1; j < confusionMatrix.length; j++)
			{
				if(j == -1)
				{
					System.out.print(i + "\t");
				}
				else
				{
					System.out.print(confusionMatrix[i][j] + "\t");
				}
			}
			System.out.println("");
		}
		
	}
}
