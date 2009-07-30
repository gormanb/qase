//--------------------------------------------------
// Name:			Neuron.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.ai.gann;

import java.io.Serializable;
import java.util.Vector;

import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Represents a generic neuron in the neural network. */
/*-------------------------------------------------------------------*/
public class Neuron implements Serializable
{
	int layer, num;
	double[] weights;
	private double value, transFunc, response;

	private static final int BIAS = -1;

/*-------------------------------------------------------------------*/
/**	Constructor. Initialises the neuron's weights to random values in
 *	the range [-1, 1].
 *	@param l the layer number
 *	@param n the neuron number within the current layer
 *	@param nw the number of weights
 *	@param trans the transfer function to use; should be one of
 *	TRANS_LOGSIG or TRANS_TANSIG as detailed above
 *	@param resp the response value, as detailed in NeuralNet
 *	@see NeuralNet#NeuralNet(int, int, int, int, double)
 *	@see NeuralNet#TRANS_LOGSIG
 *	@see NeuralNet#TRANS_TANSIG */
/*-------------------------------------------------------------------*/
	Neuron(int l, int n, int nw, int trans, double resp)
	{
		num = n;
		layer = l;

		response = resp;
		transFunc = trans;
		weights = new double[nw + 1];

		for(int i = 0; i < weights.length; i++)
			weights[i] = Utils.getRandom(true);
	}

/*-------------------------------------------------------------------*/
/**	Comput the output of this neuron given a set of inputs.
 *	@param inputs inputs to this neuron
 *	@return the neuron's output */
/*-------------------------------------------------------------------*/
	public double getOutput(double[] inputs)
	{
		double output = 0;

		for(int i = 0; i < inputs.length; i++)
			output += inputs[i] * weights[i];

		return sigmoid(output + (weights[weights.length - 1] * BIAS));
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private double sigmoid(double a)
	{
		if(transFunc == NeuralNet.TRANS_LOGSIG)
			return 1.0 / (1.0 + Math.exp(-a / response));		// logsig(a)
		else
			return (2.0 / (1.0 + Math.exp(-a / response))) - 1;	// tansig(a)
	}

/*-------------------------------------------------------------------*/
/**	Get a list of the current neuron's weights.
 *	@param v a Vector to which the current neuron's weights should be
 *	appended */
/*-------------------------------------------------------------------*/
	public void getWeights(Vector v)
	{
		for(int i = 0; i < weights.length - 1; i++)
			v.add(new Double(weights[i]));
	}

/*-------------------------------------------------------------------*/
/**	Get the number of weights associated with the current neuron.
 *	@return the number of weights */
/*-------------------------------------------------------------------*/
	public int getNumWeights()
	{	return weights.length;	}

/*-------------------------------------------------------------------*/
/** Get a string representation of this neuron.
 *	@return a string representation of this neuron */
/*-------------------------------------------------------------------*/
	public String toString()
	{
		String temp = "";

		for(int i = 0; i < weights.length; i++)
			temp += weights[i] + "/";

		return temp;
	}
}
