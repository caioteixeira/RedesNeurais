import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;

public class DataSet {
	
	private List<String> dataSet;
	public int classAttributteIndex;
	
	private int readIndex = 0;
	
	// FIXME - Pre Carregamento TODO
	public static final int ATTRIB_COUNT = 64;
	
	// Classificar entre 10 classes
	public static final int CLASS_COUNT = 10;
	
	/** 
	 * Carrega DataSet de arquivos
	 * @param classAtributteIndex
	 * @param dataSetFiles
	 */
	public DataSet(int classAtributteIndex, String[] dataSetFiles)
	{
		dataSet = new LinkedList<String>();
		this.classAttributteIndex = classAtributteIndex;
		
		for(String fileName : dataSetFiles)
		{
			readDataSetFile(fileName);
		}
	}
	
	/**
	 * 
	 * @param classAtributteIndex
	 * @param dataSetFile
	 */
	public DataSet(int classAtributteIndex, String dataSetFile)
	{
		dataSet = new LinkedList<String>();
		this.classAttributteIndex = classAtributteIndex;
		readDataSetFile(dataSetFile);
	}
	
	/**
	 * Inicializa DataSet de uma List
	 * @param classAtributteIndex
	 * @param list
	 */
	public DataSet(int classAtributteIndex, List<String> list)
	{
		this.classAttributteIndex = classAtributteIndex;
		
		dataSet = new LinkedList<String>();
		for(String line : list)
		{
			Random r = new Random();
			int s = dataSet.size();
			
			//Garante que DataSet seja carregado em mem�ria em ordem aleat�ria
			int a = s > 1? r.nextInt(s): 0;
			
			dataSet.add(a, line);
		}
	}
	
	/**
	 * 
	 * @param fileName
	 */
	private void readDataSetFile(String fileName)
	{
		System.out.println("reading "+fileName);
		try
		{
			//System.out.println(fileName);
			File f = new File(fileName);
			Scanner sc = new Scanner(f);
			
			while(sc.hasNext())
			{
				String line = sc.nextLine();
				Random r = new Random();
				int s = dataSet.size();
				
				//Garante que DataSet seja carregado em mem�ria em ordem aleat�ria
				int a = s > 1? r.nextInt(s): 0;
				
				dataSet.add(a, line);
			}
			
			sc.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("N�O ACHOU ARQUIVO: "+fileName);
		}
	}
	
	/**
	 * Imprime distribuicao
	 * @param classAtributteIndex
	 */
	public void printClassDistribution(int classAtributteIndex)
	{
		HashMap<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		
		for(String dataLine : dataSet)
		{
			String[] data = dataLine.split(",");
			Integer classIn = Integer.parseInt(data[classAtributteIndex]);
			List<String> list = map.get(classIn);
			if(list == null)
			{
				list = new LinkedList<String>();
				map.put(classIn, list);
			}
			
			list.add(dataLine);
		}
		
		for(Integer i : map.keySet())
		{
			System.out.println(i + " : "+ map.get(i).size());
		}
	}
	
	
	/**
	 * Divide DataSet em conjuntos igualmente distribuidos
	 * Saida: Arranjo com os DataSets (Treino, Validacao e Saida)
	 * @return DataSet[]
	 */
	public DataSet[] divideDataSet()
	{
		//Inicializa HashMap para dividir por classe
		HashMap<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		for(String dataLine : dataSet)
		{
			String[] data = dataLine.split(",");
			Integer classIn = Integer.parseInt(data[this.classAttributteIndex]);
			List<String> list = map.get(classIn);
			if(list == null)
			{
				list = new LinkedList<String>();
				map.put(classIn, list);
			}
			list.add(dataLine);
		}
		
		DataSet[] out = new DataSet[3];
		
		//Divide conjuntos com distribui��o uniforme
		for(int cont = 0; cont < 3; cont++)
		{
			List<String> list = new LinkedList<>();
			int size = 0;
			switch(cont)
			{
				case 0:
					size = 60; //60 por cento Treinamento
					break;
				case 1:
					size = 50; //20 por cento Validacao
					break;
				case 2:
					size = 100; //20 por cento teste
					break;
			}
			
			for(Integer key : map.keySet())
			{
				List<String> mapList = map.get(key);
				
				//System.out.println(key + "Size: "+mapList.size()+ " Subset: "+size+" result: "+ ((size * mapList.size())/100));
				int cSize = (size * mapList.size())/100;
				int i = 0;
				while(i<cSize && !mapList.isEmpty())
				{
					list.add(mapList.remove(0));
					i++;
				}	
			}
			
			out[cont] = new DataSet(this.classAttributteIndex, list);
		}	
		return out;
	}
	
	public void save(String name)
	{
		
	}
	
	public boolean hasNext()
	{
		return readIndex < dataSet.size();
	}
	
	public String nextString()
	{
		String r = dataSet.get(readIndex);
		readIndex++;
		return r;
	}
	
	/**
	 * Função para retornar proximo vetor do arquivo convertido para double
	 * @return double[]
	 */
	public double[] next()
	{
		String line = nextString();
		String[] values = line.split(",");
		
		double[] response = new double[values.length];
		
		for(int i = 0; i < values.length; i++)
		{
			response[i] = Double.parseDouble(values[i]);
		}
		
		return response;
	}
	
	/**
	 * Volta para o inicio do dataset
	 */
	public void reset()
	{
		readIndex = 0;
	}
}
