import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class DataSet {
	
	private List<String> dataSet;
	public int classAttributteIndex;
	public int class_count = 0;
	public int attrib_count = 0;
	
	private int readIndex = 0;
	
	
	
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
		calculateNumberOfClasses();
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
		calculateNumberOfClasses();
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
			//Define attrib_count
			String[] attributes = line.split(",");
			if(attrib_count == 0)
				attrib_count = attributes.length-1;
			if(attrib_count != attributes.length-1)
			{
				System.out.println("Numero de atributos incompativel!");
			}
			
			dataSet.add(line);
		}
		calculateNumberOfClasses();
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
				
				//Define attrib_count
				String[] attributes = line.split(",");
				if(attrib_count == 0)
					attrib_count = attributes.length-1;
				if(attrib_count != attributes.length-1)
				{
					System.out.println("Numero de atributos incompativel!");
				}
								
				dataSet.add(line);
			}
			
			sc.close();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("NAO ACHOU ARQUIVO: "+fileName);
		}
	}
	
	/**
	 * Imprime distribuicao
	 * @param classAtributteIndex
	 */
	public void printClassDistribution()
	{
		HashMap<Double, List<String>> map = new HashMap<Double, List<String>>();
		
		for(String dataLine : dataSet)
		{
			String[] data = dataLine.split(",");
			Double classIn = Double.parseDouble(data[this.classAttributteIndex]);
			List<String> list = map.get(classIn);
			if(list == null)
			{
				list = new LinkedList<String>();
				map.put(classIn, list);
			}
			
			list.add(dataLine);
		}
		
		for(Double i : map.keySet())
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
		HashMap<Double, List<String>> map = new HashMap<Double, List<String>>();
		for(String dataLine : dataSet)
		{
			String[] data = dataLine.split(",");
			Double classIn = Double.parseDouble(data[this.classAttributteIndex]);
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
			
			for(Double key : map.keySet())
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
	
	/**
	 * Salva DataSet
	 * @param name
	 */
	public void save(String name)
	{
		try {
			FileWriter file = new FileWriter(name);
			this.reset();
			while(this.hasNext())
			{
				file.write(this.nextString() + "\n");
			}
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Calcula Numero das classes
	 */
	private void calculateNumberOfClasses()
	{
		this.class_count = 0;
		List<Double> classes = new ArrayList<Double>();
		reset();
		while(hasNext())
		{
			double[] line = next();
			
			//Inicializacoes
			if(this.classAttributteIndex == -1)
			{
				this.classAttributteIndex = line.length-1;
			}
			
			double _class = line[this.classAttributteIndex];
			if(!classes.contains(_class))
				classes.add(_class);
			
		}
		
		this.class_count = classes.size();
		this.reset();
	}
	
	/**
	 * Verifica se o conjunto possui proximo retornando uma variavel booleana
	 * @return
	 */
	public boolean hasNext()
	{
		return readIndex < dataSet.size();
	}
	
	/**
	 * Retorna proxima String do DataSet
	 * @return String
	 */
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
	 * Função para setar vetor do arquivo convertido
	 */
	public void nextSet(double[] values, int attribClass)
	{
		String newLine = String.valueOf(values[0]);
		for (int i = 1; i < values.length; i++) {
			newLine += ("," + String.valueOf(values[i]));
		}
		
		newLine += "," + attribClass;
		
		dataSet.set(readIndex, newLine);
		readIndex++;
	}
	
	/**
	 * Volta para o inicio do dataset
	 */
	public void reset()
	{
		readIndex = 0;
	}
	
	/**
	 * Funcao que retorna tamanho do DataSet
	 * @return
	 */
	public int size() {
		return dataSet.size();
	}
	
	/**
	 * Função estatica para normalizacao dos dados 
	 * @param dataSet
	 * @param min
	 * @param max
	 */
	public void normalize() {
		DataSet dataSet = this;
		this.reset();
		// Baseado no slide http://homepages.dcc.ufmg.br/~glpappa/slides/Curso-Parte1.pdf
		int lines = dataSet.dataSet.size();
		//System.out.println("Tamanho" + lines);
		
		int min = 0;
		int max = 1;
		
		System.out.println("Merging...");
		double[][] attrib = new double[lines][attrib_count]; //Desconsidera classe como atributo
		
		int[] classes = new int[lines];
		int i = 0;
		while (dataSet.hasNext()) {
			double[] line = dataSet.next();
			for (int j = 0; j < line.length; j++) {
				if (j == line.length-1) {
					classes[i] = (int) line[j];
				} else {
					attrib[i][j] = line[j];
				}
				
			}
			i++;
		}
		
		System.out.println("Definindo min-max de cada atributo");
		double[][] minMaxAttribs = new double[attrib_count][2];
		int z = 0;
		// Pega min e max de cada atributo
		for (; z < attrib_count; z++) {
			minMaxAttribs[z][0] = attrib[z][0];
			minMaxAttribs[z][1] = attrib[z][0];
			for (int f = 0; f < lines; f++) {
				// Define min
				if (attrib[f][z] < minMaxAttribs[z][0]) {
					minMaxAttribs[z][0] = attrib[f][z];
				}
				
				// Define max
				if (attrib[f][z] > minMaxAttribs[z][1]) {
					minMaxAttribs[z][1] = attrib[f][z];
				}
			}
			//System.out.println("Min " + minMaxAttribs[z][0] + " Max " + minMaxAttribs[z][1]);
		}
		
		System.out.println("Calculando min-max item-a-item");
		int f;

		for (z = 0; z < attrib_count; z++) {
			for (f = 0; f < lines; f++) {
				double atributo = attrib[f][z];
				double minimoAtributo = minMaxAttribs[z][0];
				double maximoAtributo = minMaxAttribs[z][1];

				double newValue = (((atributo - minimoAtributo) / (maximoAtributo - minimoAtributo)) * (max - min))+min;
				
				if (Double.isNaN(newValue)) {
					newValue = min;
				}
				
				attrib[f][z] = newValue;
			}
		}
		
		System.out.println("Alterando dataSet a partir dos dados normalizados...");
		dataSet.reset();
		for (i = 0; i < attrib.length; i++) {
			dataSet.nextSet(attrib[i], classes[i]);
		}
		
		System.out.println("Salvando DataSet Normalizado...");
		// TODO Salvar novo dataSet
		dataSet.reset();
		dataSet.save("optdigits.norm");
	}
}
