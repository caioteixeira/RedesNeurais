# LVQ and MLP

## Index
- [Group Members](#group-members)
- [Class Diagram](#class-diagram)
- [1. How to compile and run](#1-how-to-compile-and-run-the-neural-networks)
- [2. MLP](#2-mlp)
	- [2.1 MLP Parameters](#21-mlp-parameters)
	- [2.2 MLP Examples](#22-mlp-examples)
- [3. LVQ](#3-lvq)
	- [3.1 LVQ Parameters](#31-lvq-parameters)
	- [3.2 LVQ Examples](#32-lvq-examples)

## Group Members:
* 8623910 - Adriano dos Santos Rodrigues da Silva
* 8516883 - Caio Vinicius Marques Teixeira
* 8598631 - Guilherme Hernandes do Nascimento
* 8623865 - João Pedro Nardari dos Santos

## Class Diagram
![project class diagram](https://raw.githubusercontent.com/caioteixeira/RedesNeurais/master/RedesNeuraisDiagram.png?token=AEPMf9o2NOz8XWvrElqwB7v3jK7tQDOfks5VgwXDwA%3D%3D)

#1. How to compile and run the neural networks
Open the RedesNeurais folder and run the following commands:

`javac -cp ".\libs\commons-cli-1.3.jar;" .\*.java`

`java -cp "libs\commons-cli-1.3.jar;" MLPDigits + Argumentos`

`java -cp "libs\commons-cli-1.3.jar;" LVQDigits + Argumentos`

*Obs: You should use the cp argument to include the commons-cli-1.3.jar library*

*Obs2: Look below about how to use parameters*

#2. MLP
##2.1 MLP parameters
	usage: MLPDigits [-bias] [-hc <arg>] [-ic <arg>] [-init <arg>] [-lc <arg>]
	       [-load <arg>] [-lr <arg>] [-oc <arg>] [-save <arg>] [-testlog
	       <arg>] [-tn <arg>] [-trainlog <arg>] [-tt <arg>] [-vl <arg>]
	 -bias             use bias
	 -hc <arg>         number of hidden nodes
	 -ic <arg>         number of input nodes
	 -init <arg>       neuron initialization methods (RANDOM or ZERO)
	 -lc <arg>         number of hidden layers
	 -load <arg>       path for file containing existing LVQ network
	 -lr <arg>         learning rate
	 -oc <arg>         number of output nodes
	 -save <arg>       save the neural network, insert the file path
	 -testlog <arg>    save a log file, insert the file path
	 -tn <arg>         training data set path
	 -trainlog <arg>   save training log file, insert the file path
	 -tt <arg>         test data set path
	 -vl <arg>         validation data set path


##2.2 MLP Examples
**2.2.1 Create a new MLP network, train (with validation), test, generate and save log file**
	
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
	
**2.2.2 Load a MLP network, test and save output data**
	
	java -cp "libs\commons-cli-1.3.jar;" MLPDigits
	-load "mlpNetwork.mlp"
	-tt "optdigits.norm.cortado.tes"
	-testlog "testLogMLPDigits.csv"

#3. LVQ
##3.1 LVQ Parameters
	usage: LVQDigits [-distance <arg>] [-init <arg>] [-load <arg>] [-lr <arg>]
	       [-nc <arg>] [-rr <arg>] [-save <arg>] [-testlog <arg>] [-tn <arg>]
	       [-trainlog <arg>] [-tt <arg>] [-vl <arg>]
	 -distance <arg>   length metric mode (EUCLIDEAN or
	                   MANHATTAN)
	 -init <arg>       neuron initialization method (RANDOM ,
	                   FIRST_VALUES ou ZERO)
	 -load <arg>       File path for LVQ network
	 -lr <arg>         learning rate
	 -nc <arg>         number of neurons
	 -rr <arg>         reduction rate
	 -save <arg>       save the neural network, insert the file path
	 -testlog <arg>    save a test log file, insert the file path
	 -tn <arg>         training data set path
	 -trainlog <arg>   save training log file, insert the file path
	 -tt <arg>         test data set path
	 -vl <arg>         validation data set path

##3.2 LVQ Examples
**3.2.1 Create a new LVQ network, train (with validation), test, generate and save log file**

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
			
**3.2.2 Load a LVQ network, test and save output data**

	java -cp "libs\commons-cli-1.3.jar;" LVQDigits
	-load "lvqNetwork.lvq"
	-tt "optdigits.norm.cortado.tes"
	-testlog "testLogLVQDigits.csv"
=====================================================================================
