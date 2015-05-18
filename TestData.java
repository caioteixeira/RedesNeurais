import java.io.FileWriter;
import java.io.IOException;


public class TestData {
	
	public String description = ""; //Deve ser usada para descrever a configuração da máquina testada
	
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
		if(i > confusionMatrix.length || j > confusionMatrix.length)
		{
			System.out.println("Indice invalido!");
			return;
		}
		
		confusionMatrix[i][j]++;
	}
	
	//Imprime resultados na tela
	public void printResults()
	{
		System.out.println("Numero de testes: "+ (hits+errors));
		System.out.println("Acurácia: " + hits);
		System.out.println("Erro: " + errors);
		System.out.println("Taxa de precisão: "+ precision());
		System.out.println("Taxa de falsas descobertas: " + fdr());
		
		
		//System.out.println("")
		
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
	
	//Imprime resultados em arquivo
	public void saveResults(String dir)
	{
		try {
			FileWriter fw = new FileWriter(dir);
			
			fw.write(description + "\n"); // Descrição da máquina
			fw.write("Numero de testes: "+ (hits+errors) + "\n");
			fw.write("Acuracia: " + hits + "\n");
			fw.write("Erro: " + errors + "\n");
			fw.write("Taxa de precisao: "+ precision() + "\n");
			fw.write("Taxa de falsas descobertas: " + fdr() + "\n");
			
			//fw.write("")
			
			fw.write("\nMatrix de confusao\n");
			for(int i = -1; i < confusionMatrix.length; i++)
				fw.write((i>-1?i:"") + "\t");
			fw.write("\n");
			for(int i = 0; i < confusionMatrix.length; i++)
			{
				for(int j = -1; j < confusionMatrix.length; j++)
				{
					if(j == -1)
					{
						fw.write(i + "\t");
					}
					else
					{
						fw.write(confusionMatrix[i][j] + "\t");
					}
				}
				fw.write("\n");
			}
			
			fw.close();
			
		} catch (IOException e) {
			System.out.println("Não conseguiu salvar arquivo de resultados! "+ dir);
			e.printStackTrace();
		}
	}
	
	//Taxa de precisão
	public double precision()
	{
		double truePositives = (double)truePositives();
		double falsePositives = (double)falsePositives();
		
		return truePositives/(truePositives+falsePositives);
	}
	
	//Taxa de falsas descobertas
	public double fdr()
	{
		double truePositives = (double)truePositives();
		double falsePositives = (double)falsePositives();
		
		return falsePositives/(truePositives +falsePositives);
	}
	
	
	//Número de verdadeiros positivos
	public int truePositives()
	{
		int vp = 0;
		for(int i = 0; i < confusionMatrix.length; i++)
		{
			vp += confusionMatrix[i][i];
		}
		
		return vp;
	}
	//Número de falsos positivos
	public int falsePositives()
	{
		int fp = 0;
		for(int i = 0; i < confusionMatrix.length; i++)
		{
			for(int j = 0; j < confusionMatrix.length; j++)
			{
				if(i != j)
				{
					fp += confusionMatrix[i][j];
				}
			}
		}
		return fp;
	}
}
