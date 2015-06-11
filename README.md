Programa LVQ e MLP
Integrantes do grupo:
Adriano dos Santos Rodrigues da Silva	8623910
Caio Vinicius Marques Teixeira	        8516883
Guilherme Hernandes do Nascimento       8598631
João Pedro Nardari dos Santos		8623865

Diagrama de Classes do projeto
![project class diagram](https://raw.githubusercontent.com/caioteixeira/RedesNeurais/master/diagrama_classes_redesNeurais.jpg?token=AEPMf0wX-MBrIUIUM_4GsphAOWsgGBhTks5VgvsHwA%3D%3D)

=============================================================================
1 - Como compilar e executar as redes?

2 - MLP
	1.1 - Parametros e utilizacao MLP
	1.2 - Exemplos de utilizacao MLP
3 - LVQ
	2.1 - Parametros e utilizacao LVQ
	2.2 - Exemplos de utilizacao LVQ

=============================================================================
1 - Como compilar e executar as redes?
=============================================================================
Inicialize seu programa na linha de comando, acesse a pasta RedesNeurais
e execute o seguintes comandos:

javac -cp ".\libs\commons-cli-1.3.jar;" .\*.java

java -cp "libs\commons-cli-1.3.jar;" MLPDigits + Argumentos
java -cp "libs\commons-cli-1.3.jar;" LVQDigits + Argumentos

* deve-se utilizar o argumento cp para passar a biblioteca commons-cli-1.3.jar como parametro
** Leia abaixo utilizacao dos argumentos

=============================================================================
2 - MLP
=============================================================================
1.1 - Parametros e utilizacao MLP
=============================================================================
usage: MLPDigits [-bias] [-hc <arg>] [-ic <arg>] [-init <arg>] [-lc <arg>]
       [-load <arg>] [-lr <arg>] [-oc <arg>] [-save <arg>] [-testlog
       <arg>] [-tn <arg>] [-trainlog <arg>] [-tt <arg>] [-vl <arg>]
 -bias             opcao para utilizacao de bias
 -hc <arg>         numero de nos hidden
 -ic <arg>         numero de nos de entrada
 -init <arg>       metodo de inicializacao dos neuronios (RANDOM ou ZERO)
 -lc <arg>         numero de camadas hidden
 -load <arg>       Caminho para arquivo contendo Rede LVQ
 -lr <arg>         taxa de aprendizado
 -oc <arg>         numero de nos de saida
 -save <arg>       opcao de salvar rede neural, passe com o caminho do
                   arquivo que quer salvar a rede
 -testlog <arg>    opcao para gerar log de teste .csv, passar o caminho
 -tn <arg>         nome do arquivo do conjunto de dados de treino
 -trainlog <arg>   opcao para gerar log de treinamento .csv, passar o
                   caminho
 -tt <arg>         nome do arquivo do conjunto de dados de teste
 -vl <arg>         nome do arquivo do conjunto de dados de validacao

==================================================================================
2.2 - Exemplos de uso MLP
==================================================================================
2.2.1 - Criando uma nova Rede MLP, treinando (com validacao), teste, gerando log e salvando

	java -cp "libs\commons-cli-1.3.jar;" MLPDigits 
	-init RANDOM 
	-lr 0.5 
	-bias
	-lc 1
	-ic 61 
	-oc 4 
	-hc 40
	-tn "optdigits.norm.cortado.tra" 
	-tt "optdigits.norm.cortado.tes" 
	-vl "optdigits.norm.cortado.val"
	-trainlog "trainningLogMLPDigits.csv"
	-testlog "testLogMLPDigits.csv"
	-save "mlpNetwork.mlp"
	
2.2.2 - Carregando Rede, testando e salvando dados gerados no teste
	
	java -cp "libs\commons-cli-1.3.jar;" MLPDigits
	-load "mlpNetwork.mlp"
	-tt "optdigits.norm.cortado.tes"
	-testlog "testLogMLPDigits.csv"

====================================================================================

3 - LVQ
====================================================================================
3.1 - Parametros e utilizacao
====================================================================================
usage: LVQDigits [-distance <arg>] [-init <arg>] [-load <arg>] [-lr <arg>]
       [-nc <arg>] [-rr <arg>] [-save <arg>] [-testlog <arg>] [-tn <arg>]
       [-trainlog <arg>] [-tt <arg>] [-vl <arg>]
 -distance <arg>   escolha do calculo de distancia (EUCLIDEAN ou
                   MANHATTAN)
 -init <arg>       metodo de inicializacao dos neuronios (RANDOM ,
                   FIRST_VALUES ou ZERO)
 -load <arg>       Caminho para arquivo contendo Rede LVQ
 -lr <arg>         taxa de aprendizado
 -nc <arg>         numero de neuronios
 -rr <arg>         taxa de reducao
 -save <arg>       opcao de salvar rede neural, passe com o caminho do
                   arquivo que quer salvar a rede
 -testlog <arg>    opcao para gerar log de teste .csv, passar o caminho
 -tn <arg>         nome do arquivo do conjunto de dados de treino
 -trainlog <arg>   opcao para gerar log de treinamento .csv, passar o
                   caminho
 -tt <arg>         nome do arquivo do conjunto de dados de teste
 -vl <arg>         nome do arquivo do conjunto de dados de validacao

====================================================================================
3.2 - Exemplos de uso LVQ
====================================================================================
3.2.1 - Criando uma nova Rede, treinando (com validacao), 
	teste, gerando log e salvando

	java -cp "libs\commons-cli-1.3.jar;" LVQDigits 
	-init FIRST_VALUES
	-distance EUCLIDEAN 
	-lr 0.001 
	-rr 30 
	-nc 4 
	-tn "optdigits.norm.cortado.tra" 
	-tt "optdigits.norm.cortado.tes" 
	-vl "optdigits.norm.cortado.val"
	-trainlog "trainningLogLVQDigits.csv"
	-testlog "testLogLVQDigits.csv"
	-save "lvqNetwork.lvq"
			
3.2.2 - Carregando Rede, testando e salvando dados gerados no teste
		
	java -cp "libs\commons-cli-1.3.jar;" LVQDigits
	-load "lvqNetwork.lvq"
	-tt "optdigits.norm.cortado.tes"
	-testlog "testLogLVQDigits.csv"
=====================================================================================
