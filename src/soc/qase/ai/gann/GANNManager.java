
package soc.qase.ai.gann;

import soc.qase.com.Proxy;
import soc.qase.state.World;

import java.util.Observer;
import java.util.Observable;

import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*-------------------------------------------------------------------*/
/**	A class which provides the basic template of a 'bridge' between
 *	the GA and ANN classes, demonstrating the steps required to evolve
 *	the weights of a population of networks by treating each weight as
 *	a nucleotide in the GA's genome. The class provides two modes of
 *	operation. For offline experiments - that is, those performed outside
 *	a live Quake 2 match - the GANNManager can be run as a thread,
 *	continually assessing the fitness of each network according to a user-
 *	defined fitness function, recombining the associated genomes, and
 *	evolving towards an optimal solution for a specified duration of
 *	each generation and of overall simulation time. For online experiments,
 *	the class can be attached as an Observer of one or more Proxy objects,
 *	providing direct feedback from the Quake 2 game world. The class is
 *	abstract; it must be subclassed to provide the necessary fitness and
 *	observer functions, and to tailor its operation to the specific
 *	problem at hand. The class also allows the user to save a simulation
 *	to disk at an arbitrary point, and resume it from the same point later. */
/*-------------------------------------------------------------------*/
public abstract class GANNManager extends Thread implements Observer, Serializable
{
	protected int lifespan = -1;
	protected long elapsedTime = -1;
	protected int simulationTime = -1;

	protected NeuralNet[] nets = null;
	protected Genetic genePool = null;

/*-------------------------------------------------------------------*/
/**	Constructor. Takes a set of parameters and uses them to build a
 *	population of neural networks, each paired with a single chromosome
 *	in the GA's gene pool. Each nucleotide in each chromosome corresponds
 *	to a single weight in the associated neural network. As the network's
 *	performance is evaluated by the fitness function, these chromosomes
 *	will recombine such that the fittest genes from each generation are
 *	passed to the next.
 *	@param i number of neural network inputs
 *	@param hl number of hidden layers
 *	@param hn number of neurons per hidden layer
 *	@param o number of outputs
 *	@param trans the transfer function to use; should be one of the
 *	TRANS_LOGSIG or TRANS_TANSIG constants in the NeuralNet class
 *	@param resp the response value, usually set to 1.0. As response tends
 *	to 0, the neural network's activation function becomes increasingly
 *	steplike (ie binary output); as resp tends to infinity, it becomes
 *	increasingly elongated (ie continuous but small variation in outputs).
 *	@param pop the total gene pool polulation
 *	@param cRate in the range [0, 1], the probability that crossover
 *	will occur between two chromosomes
 *	@param mRate in the range [0, 1], the probability that mutation
 *	will spontaneously occur
 *	@param maxPerturb the maximum value (+\-) by which a nucleotide
 *	(single value in a chromosome) can change
 *	@param nElite the number of fittest chromosomes to regard as 'elite'
 *	at the end of each generation
 *	@param nEliteCopies the number of copies of each elite chromosome to
 *	pass on unchanged to the next generation
 *	@param updatesPerGen the number of times the performance of each network
 *	will be evaluated before each successive generation of the gene pool
 *	is created
 *	@param simTime the total amount of time, in seconds, that the simulation
 *	will run for */
/*-------------------------------------------------------------------*/
	public GANNManager(int i, int hl, int hn, int o, int trans, double resp, int pop, double cRate, double mRate, double maxPerturb, int nElite, int nEliteCopies, int updatesPerGen, int simTime)
	{
		lifespan = updatesPerGen;
		simulationTime = simTime * 1000;

		nets = new NeuralNet[pop];

		for(int j = 0; j < nets.length; j++)
			nets[j] = new NeuralNet(i, hl, hn, o, trans, resp);

		genePool = new Genetic(pop, nets[0].getNumWeights(), cRate, mRate, maxPerturb, nElite, nEliteCopies);

		for(int j = 0; j < nets.length; j++)
			nets[j].updateWeights(genePool.getChromo(j).sequence);
	}

/*-------------------------------------------------------------------*/
/**	The main thread loop for offline experiments, as described in the
 *	constructor comments. For the duration of the simulation time as
 *	defined by the user, the fitness function will be called to evaluate
 *	the performance of each network. After the specified number of
 *	evaluations, the next generation of the gene pool is created, and
 *	the new chromosomes (weights) are placed back into the neural network.
 *	The process then repeats, with the new set of network weights being
 *	evaluated.
 *	@see #fitnessFunction(NeuralNet) */
/*-------------------------------------------------------------------*/
	public void run()
	{
		long startTime = System.currentTimeMillis();

		while((elapsedTime = System.currentTimeMillis() - startTime) < simulationTime)
		{
			for(int i = 0; i < lifespan; i++)
			{
				for(int j = 0; j < nets.length; j++)
					genePool.getChromo(j).fitness += fitnessFunction(nets[j]);
			}

			genePool.generateTheNextGeneration();

			for(int i = 0; i < nets.length; i++)
				nets[i].updateWeights(genePool.getChromo(i).sequence);
		}
	}

/*-------------------------------------------------------------------*/
/**	The Observer update method, called whenever the observable object
 *	has new information to share with registered observers. Since this
 *	class is intended to be registered with Proxy objects, the method
 *	casts the arguments to the expected types and passes the call on
 *	to the overloaded version of the update method.
 *	@param ob the Observable object, in this case a Proxy
 *	@param arg the data passed by the Proxy, that is, a World gamestate
 *	object
 *	@see #update(Proxy, World) */
/*-------------------------------------------------------------------*/
	public void update(Observable ob, Object arg)
	{
		try
		{	update((Proxy)ob, (World)arg);	}
		catch(Exception e)
		{	}
	}

/*-------------------------------------------------------------------*/
/**	Overloaded version of the update method. For online experiments
 *	involving GANNs in the Quake 2 environment, this method must
 *	perform whatever evaluation or other functions are required for
 *	the specific task.
 *	@param proxy the observable Proxy object which initiated the method
 *	call
 *	@param world the World gamestate object passed by the Proxy */
/*-------------------------------------------------------------------*/
	protected abstract void update(Proxy proxy, World world);

/*-------------------------------------------------------------------*/
/**	The fitness function, used to evaluate the performance of each
 *	neural network. Called automatically by the main update thread when
 *	used in offline mode; its use in online mode is left to the discretion
 *	of the implementer.
 *	@param nn the neural network to be evaluated
 *	@return a double value indicating the 'fitness' of the neural net's
 *	performance */
/*-------------------------------------------------------------------*/
	protected abstract double fitnessFunction(NeuralNet nn);

/*-------------------------------------------------------------------*/
/**	Save the current simulation to file. Serializes the object and saves
 *	it to disk at the specified location; this allows it to be loaded
 *	later and the genetic algorithm process to resume from the same point.
 *	@param filename and file name under which to save the simulation
 *	@return true if the file was successfully saved, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean saveSimulation(String filename)
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
/**	Load and return a saved simulation stored at the spcified location.
 *	@param filename the path and filename of the saved simulation
 *	@return the GANNManager object if successfully deserialized, null otherwise */
/*-------------------------------------------------------------------*/
	public static GANNManager loadSimulation(String filename)
	{
		try
		{
			return (GANNManager)(new ObjectInputStream(new FileInputStream(filename))).readObject();
		}
		catch(Exception e)
		{
			return null;
		}
	}
}
