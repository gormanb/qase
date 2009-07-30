//--------------------------------------------------
// Name:			NeuralNet.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.ai.gann;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

/*-------------------------------------------------------------------*/
/**	Represents and controls a neural network, designed to be used in
 *	conjunction with the genetic algorithm classes via the GANNManager.
 *	Builds the network given design parameters, controls the retrieval
 *	and updating of the net's weights, facilitates output using graded
 *	logsig or tansig functions, and computes the network's output for
 *	given input.  */
/*-------------------------------------------------------------------*/
public class NeuralNet implements Serializable
{
	private int transFunc;
	private NeuralLayer[] layers;
	private int numInputs, numHiddenLayers, numHiddenNeurons, numOutputs;

/*-------------------------------------------------------------------*/
/**	Used to define the type of transfer function employed by the
 *	neural network. logsig(a) = 1/(1 + e^(-a/p)) produces output in
 *	the range [0, 1] ; tansig(a) = (2/(1+e^(-a/p)))-1. The variable p
 *	is used to 'shape' the function; as p >= 0 the activation function
 *	becomes increasingly steplike (ie binary output), as p => infinity
 *	the function becomes increasingly elongated (ie continuous but small
 *	variation in outputs). */
/*-------------------------------------------------------------------*/
	public static int TRANS_LOGSIG = 0, TRANS_TANSIG = 1;

/*-------------------------------------------------------------------*/
/**	Constructor. Builds the neural network according to specified
 *	parameters, initialising the weights to random values in the range
 *	[-1, 1].
 *	@param i number of inputs
 *	@param hl number of hidden layers
 *	@param hn number of neurons per hidden layer
 *	@param o number of outputs
 *	@param trans the transfer function to use; should be one of
 *	TRANS_LOGSIG or TRANS_TANSIG as detailed above
 *	@param resp the response value, usually set to 1.0. Equivalent to
 *	the variable 'p' in the logsig and tansig functions given above.
 *	As resp tends to 0, the activation function becomes increasingly
 *	steplike (ie binary output); as resp tends to infinity, it becomes
 *	increasingly elongated (ie continuous but small variation in outputs). */
/*-------------------------------------------------------------------*/
	NeuralNet(int i, int hl, int hn, int o, int trans, double resp)
	{
		numInputs = i;
		numOutputs = o;
		transFunc = trans;
		numHiddenLayers = hl;
		numHiddenNeurons = hn;

		layers = new NeuralLayer[numHiddenLayers + 1];

		if(numHiddenLayers > 0)
		{	layers[0] = new NeuralLayer(0, numHiddenNeurons, numInputs, trans, resp);	
		
			for(int z = 1; z < layers.length - 1; z++)
				layers[z] = new NeuralLayer(z, numHiddenNeurons, numHiddenNeurons, trans, resp);
	
			layers[layers.length - 1] = new NeuralLayer(layers.length - 1, numOutputs, numHiddenNeurons, trans, resp);
		}
		else
			layers[0] = new NeuralLayer(0, numOutputs, numInputs, trans, resp);
	}

/*-------------------------------------------------------------------*/
/**	Compute the output of the neural network given a set of inputs.
 *	@param inputs an array containing the input values
 *	@return an array containing the output values */
/*-------------------------------------------------------------------*/
	public double[] compute(double[] inputs)
	{
		if(inputs.length != numInputs)
			return null;

		double[] outputs = null;

		for(int i = 0; i < numHiddenLayers + 1; i++)
		{
			if(i > 0)
				inputs = outputs;

			outputs = layers[i].getOutputs(inputs);
		}

		return outputs;
	}

/*-------------------------------------------------------------------*/
/**	Get the specified layer of the neural network.
 *	@param layerNum the index of the desired layer
 *	@return the specified layer of the neural network */
/*-------------------------------------------------------------------*/
	public NeuralLayer getLayer(int layerNum)
	{
		return layers[layerNum];
	}

/*-------------------------------------------------------------------*/
/**	Get a list of all the weights in the neural network, in left-to-right
 *	order from the first layer. This list can be used as the genome in
 *	the genetic algorithm process.
 *	@return the list of all weights in the neural network */
/*-------------------------------------------------------------------*/
	public double[] getAllWeights()
	{
		Vector v = new Vector();

		for(int i = 0; i < layers.length; i++)
			layers[i].getWeights(v);

		double[] d = new double[v.size()];

		for(int i = 0; i < v.size(); i++)
			d[i] = ((Double)v.elementAt(i)).doubleValue();

		return d;
	}

/*-------------------------------------------------------------------*/
/**	Get the total number of weights across all neurons in the network.
 *	@return the total number of weights */
/*-------------------------------------------------------------------*/
	public int getNumWeights()
	{
		int count = 0;

		for(int i = 0; i < layers.length; i++)
			count += layers[i].getNumWeights();

		return count;
	}

/*-------------------------------------------------------------------*/
/**	Update the weights of the neural network. Generally used at the
 *	start of each generation, to repopulate the networks with the newly
 *	mutated and recombined genomes. Weights are added left-to-right from
 *	the first to last layer.
 *	@param weights the weights with which to populate the neural network */
/*-------------------------------------------------------------------*/
	public void updateWeights(double[] weights)
	{
		int arrayIndex = 0;

		for(int i = 0; i < layers.length; i++)
		{
			for(int j = 0; j < layers[i].neurons.length; j++)
			{
				for(int k = 0; k < layers[i].neurons[j].weights.length; k++)
					layers[i].neurons[j].weights[k] = weights[arrayIndex++];
			}
		}
	}

/*-------------------------------------------------------------------*/
/**	Save the current neural network to file. Serializes the object
 *	and saves it to disk at the specified location; this allows it to
 *	be loaded later.
 *	@param filename and file name under which to save the neural net
 *	@return true if the file was successfully saved, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean saveNeuralNet(String filename)
	{
		try
		{
			(new ObjectOutputStream(new FileOutputStream(filename))).writeObject(this);
		}
		catch(Exception e)
		{
			return false;
		}

		return true;
	}

/*-------------------------------------------------------------------*/
/**	Load and return a NeuralNet object stored at the spcified location.
 *	@param filename the path and filename of the saved NeuralNet object
 *	@return the NeuralNet object if successfully deserialized, null otherwise */
/*-------------------------------------------------------------------*/
	public static NeuralNet loadNeuralNet(String filename)
	{
		try
		{
			return (NeuralNet)(new ObjectInputStream(new FileInputStream(filename))).readObject();
		}
		catch(Exception e)
		{
			return null;
		}
	}

/*-------------------------------------------------------------------*/
/**	Get a string representation of the current network.
 *	@return string representation of this object */
/*-------------------------------------------------------------------*/
	public String toString()
	{
		String temp = "";

		for(int i = 0; i < layers.length; i++)
			temp += layers[i] + "\n";

		return temp;
	}
}
