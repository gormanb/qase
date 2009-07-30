//--------------------------------------------------
// Name:			Genome.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.ai.gann;

import java.io.Serializable;

import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Represents a single chromosome in the GA gene pool. */
/*-------------------------------------------------------------------*/
public class Genome implements Comparable, Cloneable, Serializable
{
	double fitness;
	double[] sequence;

/*-------------------------------------------------------------------*/
/**	Constructor. Initialises the chromosome sequence and sets each
 *	nucleotide to a random value in the range [-1, 1].
 *	@param chromoLength the length of the chromosome */
/*-------------------------------------------------------------------*/
	Genome(int chromoLength)
	{
		fitness = 0.0;	
		sequence = new double[chromoLength];

		for(int i = 0; i < chromoLength; i++)
			sequence[i] = Utils.getRandom(true);
	}

/*-------------------------------------------------------------------*/
/**	Constructor. Initialises the chromosome to the given initial
 *	fitness and sequence values.
 *	@param fit the initial fitness
 *	@param s the initial sequence values */
/*-------------------------------------------------------------------*/
	Genome(double fit, double[] s)
	{
		fitness = fit;
		sequence = new double[s.length];

		for(int i = 0; i < s.length; i++)
			sequence[i] = s[i];
	}

/*-------------------------------------------------------------------*/
/**	Mutate the individual nucleotides of the chromosome according to
 *	the specified probability and maximum mutation range.
 *	@param mRate the probability that each nucleotide will mutate
 *	@param maxMutate the maximum range of mutation */
/*-------------------------------------------------------------------*/
	public void mutate(double mRate, double maxMutate)
	{
		for(int i = 0; i < sequence.length; i++)
		{
			if(Utils.getRandom(false) < mRate)
				sequence[i] += Utils.getRandom(true) * maxMutate;
		}
	}

/*-------------------------------------------------------------------*/
/**	Compare this Genome against another on the basis of their fitnesses
 *	@param o the Genome against which to compare the current Genome
 *	@return 1 if this < o, 0 if this == o, -1 if this > o */
/*-------------------------------------------------------------------*/
	public int compareTo(Object o)
	{
		Genome g = (Genome)o;

		if(this.fitness < g.fitness)
			return 1;
		else if(this.fitness == g.fitness)
			return 0;
		else
			return -1;
	}

/*-------------------------------------------------------------------*/
/**	Clone the current Genome.
 *	@return an exact duplication of the current Genome */
/*-------------------------------------------------------------------*/
	protected Object clone()
	{	return new Genome(this.fitness, this.sequence);	}
}
