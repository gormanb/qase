//--------------------------------------------------
// Name:			Genetic.java
// Author:			Bernard.Gorman@computing.dcu.ie
//--------------------------------------------------

package soc.qase.ai.gann;

import java.util.Vector;
import java.util.Arrays;
import java.util.Random;
import soc.qase.tools.Utils;

import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*-------------------------------------------------------------------*/
/**	A genetic algorithm generator class. Maintains fitness stats, controls
 *	mutation and recombination, and generates each successive generation of
 *	the gene pool when prompted. The class also provides methods to save the
 *	Genetic object to file and reload it at a later time, thereby allowing
 *	the genetic algorithm process to be resumed rather than restarted. */
/*-------------------------------------------------------------------*/
public class Genetic implements Serializable
{
	private Vector genomePop;
	private int chromoLength, population;
	private double crossRate, mutateRate, maxMutation;

	private int numElite, numEliteCopies;

	private Genome bestGenome;
	private double worstFitness, avgFitness, bestFitness, totalFitness, totalPlusElite;

/*-------------------------------------------------------------------*/
/**	Constructor. Initialises the GA generator and creates the gene pool.
 *	@param pop the total gene pool polulation
 *	@param length the length of each chromosome
 *	@param cRate in the range [0, 1], the probability that crossover
 *	will occur between two chromosomes
 *	@param mRate in the range [0, 1], the probability that mutation
 *	will spontaneously occur
 *	@param maxPerturb the maximum value (+\-) by which a nucleotide
 *	(single value in a chromosome) can change
 *	@param nElite the number of fittest chromosomes to regard as 'elite'
 *	at the end of each generation
 *	@param nEliteCopies the number of copies of each elite chromosome to
 *	pass on unchanged to the next generation */
/*-------------------------------------------------------------------*/
	Genetic(int pop, int length, double cRate, double mRate, double maxPerturb, int nElite, int nEliteCopies)
	{
		population = pop;
		chromoLength = length;

		crossRate = cRate;
		mutateRate = mRate;
		maxMutation = maxPerturb;

		numElite = nElite;
		numEliteCopies = nEliteCopies;

		if((numElite * numEliteCopies) % 2 == 1)
			numElite++;

		genomePop = new Vector();

		for(int i = 0; i < population; i++)
			genomePop.add(new Genome(chromoLength));
	}

/*-------------------------------------------------------------------*/
/**	Find the max, min, average and total fitnesses among the population. */
/*-------------------------------------------------------------------*/
	private void updateFitnessStats()
	{
		resetStats();

		double max = 0.0;
		double min = Double.MAX_VALUE;
		double total = 0.0;
		double tempTotalForAvg = 0.0;

		double temp = 0.0;

		for(int i = 0; i < population; i++)
		{
			temp = ((Genome)genomePop.elementAt(i)).fitness;

			total += Math.abs(temp);
			tempTotalForAvg += temp;

			if(temp > max)
			{	max = temp;
				bestGenome = (Genome)genomePop.elementAt(i);
			}

			if(temp < min)
				min = temp;
		}

		totalFitness = total;
		bestFitness = max;
		worstFitness = min;
		avgFitness = tempTotalForAvg / ((double)population);
	}

/*-------------------------------------------------------------------*/
/**	Returns an array containing the worst, best, average and total fitness
 *	of the gene pool, in that order.
 *	@return the stats array */
/*-------------------------------------------------------------------*/
	public double[] getFitnessStats()
	{
		return new double[]{worstFitness, bestFitness, avgFitness, totalFitness};
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private void resetStats()
	{
		worstFitness = Double.MAX_VALUE;
		bestFitness = avgFitness = totalFitness = 0.0;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Genome pickChromo()
	{
		Genome currChromo;
		double temp = 0.0;
		double rand = Utils.getRandom(false) * totalFitness; // totalPlusElite reduces elitism

		for(int i = 0; i < genomePop.size(); i++)
		{
			currChromo = (Genome)genomePop.elementAt(i);
			temp += Math.abs(currChromo.fitness);

			if(temp >= rand)
				return (Genome)genomePop.elementAt(i);
		}

		return (Genome)genomePop.elementAt((int)(genomePop.size() * Utils.getRandom(false)));
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private Genome[] crossOver(Genome mum, Genome dad)
	{
		Genome[] babies = new Genome[2];

		if(Utils.getRandom(false) > crossRate || mum == dad)
		{	babies[0] = (Genome)mum.clone();
			babies[1] = (Genome)dad.clone();

			return babies;
		}

		// random crossover point between 1 and chromosome length
		int spindlePoint = (int)(Utils.getRandom(false) * (chromoLength - 2)) + 1;

		babies[0] = new Genome(chromoLength);
		babies[1] = new Genome(chromoLength);

		// create the offspring
		for (int i = 0; i < spindlePoint; i++)
		{
			babies[0].sequence[i] = mum.sequence[i];
			babies[1].sequence[i] = dad.sequence[i];
		}

		for (int i = spindlePoint; i < chromoLength; i++)
		{
			babies[0].sequence[i] = dad.sequence[i];
			babies[1].sequence[i] = mum.sequence[i];
		}

		return babies;
	}

/*-------------------------------------------------------------------*/
/**	Generate the next generation of the gene pool by probabilistically
 *	choosing chromosomes, mutating them, and recombining them by locus
 *	crossover. A number of the fittest genes pass to the next generation
 *	unchanged. */
/*-------------------------------------------------------------------*/
	public void generateTheNextGeneration()
	{
		Genome[] gtemp = new Genome[genomePop.size()];
		genomePop.toArray(gtemp);

		Arrays.sort(gtemp, 0, gtemp.length);

		genomePop.clear();

		for(int i = 0; i < gtemp.length; i++)
			genomePop.add(gtemp[i]);

		updateFitnessStats();

		addElite();

		Genome[] babies = null;
		Vector newPop = new Vector();

		while(newPop.size() < population)
		{
			babies = crossOver(pickChromo(), pickChromo());

			babies[0].mutate(mutateRate, maxMutation);
			babies[1].mutate(mutateRate, maxMutation);

			newPop.add(babies[0]);
			newPop.add(babies[1]);
		}

		genomePop = newPop;
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private void addElite()
	{
		double tempFit = 0;
		Genome[] tempChromos = new Genome[numElite];

		for(int i = 0; i < numElite; i++)
		{
			tempChromos[i] = (Genome)genomePop.elementAt(i);
			tempFit += Math.abs(tempChromos[i].fitness);
		}

		tempFit *= numEliteCopies;
		totalPlusElite = totalFitness + tempFit;

		for(int i = 0; i < numElite; i++)
		{
			for(int j = 0; j < numEliteCopies; j++)
			{
				genomePop.add(0, tempChromos[i].clone()); // or try genomePop.add((int)(getRandom(false) * genomePop.size()), tempChromos[i].clone());
			}
		}
	}

/*-------------------------------------------------------------------*/
/**	Get the specified chromosome.
 *	@param i the index of the chromosome to return
 *	@return the desired chromosome */
/*-------------------------------------------------------------------*/
	public Genome getChromo(int i)
	{	return (Genome)genomePop.elementAt(i);	}

/*-------------------------------------------------------------------*/
/**	Save the current Genetic object to file. Serializes the object
 *	and saves it to disk at the specified location; this allows it to
 *	be loaded later and the genetic algorithm process to resume from
 *	the same point.
 *	@param filename and file name under which to save the Genetic object
 *	@return true if the file was successfully saved, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean saveGenetic(String filename)
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
/**	Load and return a Genetic object stored at the spcified location.
 *	@param filename the path and filename of the saved Genetic object
 *	@return the Genetic object if successfully deserialized, null otherwise */
/*-------------------------------------------------------------------*/
	public static Genetic loadGenetic(String filename)
	{
		try
		{
			return (Genetic)(new ObjectInputStream(new FileInputStream(filename))).readObject();
		}
		catch(Exception e)
		{
			return null;
		}
	}
}
