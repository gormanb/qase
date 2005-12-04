//--------------------------------------------------
// Name:			NeuralLayer.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.ai.gann;

import java.util.Vector;
import java.io.Serializable;

/*-------------------------------------------------------------------*/
/**	Represents a single later in the neural network. */
/*-------------------------------------------------------------------*/
public class NeuralLayer implements Serializable
{
	int layernum;
	Neuron[] neurons;

/*-------------------------------------------------------------------*/
/**	Constructor. Builds the neural network layer, initialising all
 *	weights to random values in the range [-1, 1].
 *	@param lnum the layer number
 *	@param numNeurons the number of neurons in the current layer
 *	@param nInputs the number of inputs to this layer, ie the number of
 *	neurons in the preceding layer
 *	@param trans the transfer function to use; should be one of
 *	TRANS_LOGSIG or TRANS_TANSIG as detailed in NeuralNet
 *	@param resp the response value, as detailed in NeuralNet
 *	@see NeuralNet#NeuralNet(int, int, int, int, double)
 *	@see NeuralNet#TRANS_LOGSIG
 *	@see NeuralNet#TRANS_TANSIG */
/*-------------------------------------------------------------------*/
	NeuralLayer(int lnum, int numNeurons, int nInputs, int trans, double resp)
	{
		layernum = lnum;
		neurons = new Neuron[numNeurons];

		for(int i = 0; i < numNeurons; i++)
			neurons[i] = new Neuron(lnum, i, nInputs, trans, resp);
	}

/*-------------------------------------------------------------------*/
/**	Compute the output values of this layer (ie the list of the output
 *	of each individual node) given a set of inputs.
 *	@param inputs the inputs to this layer - either the inputs to the
 *	neural network if this is the first layer, or the outputs of the
 *	previous layer's neurons
 *	@return the list of outputs of this layer's neurons */
/*-------------------------------------------------------------------*/
	public double[] getOutputs(double[]inputs)
	{
		double[] outputs = new double[neurons.length];

		for(int i = 0; i < neurons.length; i++)
			outputs[i] = neurons[i].getOutput(inputs);

		return outputs;
	}

/*-------------------------------------------------------------------*/
/**	Get a list of all the weights in the layer, in left-to-right order.
 *	@param v a Vector to which the current layer's weights are appended */
/*-------------------------------------------------------------------*/
	public void getWeights(Vector v)
	{
		for(int i = 0; i < neurons.length; i++)
			neurons[i].getWeights(v);
	}

/*-------------------------------------------------------------------*/
/**	Get the number of weights in this layer, ie number of neurons *
 *	number of weights per neuron.
 *	@return the number of weights in the current layer */
/*-------------------------------------------------------------------*/
	public int getNumWeights()
	{
		int count = 0;

		for(int i = 0; i < neurons.length; i++)
			count += neurons[i].getNumWeights();

		return count;
	}

/*-------------------------------------------------------------------*/
/**	Get a string representation of the layer.
 *	@return a string representation of the layer */
/*-------------------------------------------------------------------*/
	public String toString()
	{
		String temp = "";

		for(int i = 0; i < neurons.length; i++)
			temp += neurons[i] + "\t";

		return temp; // or shorthand "Layer " + layernum + ", Neurons " + neurons.length;
	}
}
