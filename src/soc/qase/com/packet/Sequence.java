//---------------------------------------------------------------------
// Name:			Sequence.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.com.packet;

import soc.qase.tools.Utils;

/*-------------------------------------------------------------------*/
/**	Sequence number wrapper used when sending packets to host. */
/*-------------------------------------------------------------------*/
public class Sequence
{
	private int sequence = 0;
	private boolean reliable = false;

/*-------------------------------------------------------------------*/
/**	Constructor. Builds Sequence object from sequence number.
 *	@param sequence sequence number */
/*-------------------------------------------------------------------*/
	public Sequence(int sequence)
	{
		this.sequence = sequence;
	}

/*-------------------------------------------------------------------*/
/**	Constructor. Builds Sequence object from sequence number and reliability.
 *	@param sequence sequence number
 *	@param reliable true if sequence denotes a reliable packet,
 *	otherwise false */
/*-------------------------------------------------------------------*/
	public Sequence(int sequence, boolean reliable)
	{
		this.sequence = sequence;
		this.reliable = reliable;
	}

/*-------------------------------------------------------------------*/
/**	Constructor. Builds Sequence object from byte array.
 *	@param sequence byte array containing sequence info */
/*-------------------------------------------------------------------*/
	public Sequence(byte[] sequence)
	{
		readSequence(sequence, 0);
	}

/*-------------------------------------------------------------------*/
/**	Constructor. Builds Sequence object from byte array with offset.
 *	@param sequence byte array
 *	@param offset source offset */
/*-------------------------------------------------------------------*/
	public Sequence(byte[] sequence, int offset)
	{
		readSequence(sequence, offset);
	}

/*-------------------------------------------------------------------*/
/*-------------------------------------------------------------------*/
	private void readSequence(byte[] sequence, int offset)
	{
		int result = 0;
		int multiply = 1;
		int value = 0;

		for(int i = 0; i < 4; i++)
		{
			value = sequence[i + offset];

			if(value < 0)
				value = value + 256;

			result = result + (value * multiply);
			multiply = multiply * 256;
		}

		this.sequence = result;
		
		if(this.sequence < 0)
		{
			setReliable(true);
			this.sequence = this.sequence & 0x7fffffff;
		}
	}

/*-------------------------------------------------------------------*/
/**	Get sequence number.
 *	@return sequence number. */
/*-------------------------------------------------------------------*/
	public int intValue()
	{
		return sequence;
	}

/*-------------------------------------------------------------------*/
/**	Get next sequence number.
 *	@return next sequence number. */
/*-------------------------------------------------------------------*/
	public Sequence getNext()
	{
		Sequence result = null;
		
		result = new Sequence(sequence + 1);
		return result;
	}

/*-------------------------------------------------------------------*/
/**	Get previous sequence number.
 *	@return previous sequence number. */
/*-------------------------------------------------------------------*/
	public Sequence getPrevious()
	{
		Sequence result = null;
		
		result = new Sequence(sequence - 1);
		if(result.intValue() < 1) result = new Sequence(1);
		return result;
	}

/*-------------------------------------------------------------------*/
/**	Get sequence bytes.
 *	@return byte array. */
/*-------------------------------------------------------------------*/
	public byte[] getBytes(byte[] seqBytes, int off)
	{
		Utils.intToByteArray(sequence, seqBytes, off);

		if(isReliable())
			seqBytes[off + 3] = (byte)(seqBytes[off + 3] | 0x00000080);

		return seqBytes;
	}

/*-------------------------------------------------------------------*/
/**	Check if this sequence number denotes a reliable packet.
 *	@return true if this sequence number denotes a reliable packet,
 *	otherwise false. */
/*-------------------------------------------------------------------*/
	public boolean isReliable()
	{
		return reliable;
	}

/*-------------------------------------------------------------------*/
/**	Set reliable sequence number flag.
 *	@param flag reliable flag. */
/*-------------------------------------------------------------------*/
	public void setReliable(boolean flag)
	{
		reliable = flag;
	}

/*-------------------------------------------------------------------*/
/**	Get String representation of this object.
 *	@return String representation of this object. */
/*-------------------------------------------------------------------*/
	public String toString()
	{
		String result = null;

		result = new String("" + intValue());
		if(isReliable()) {
			result += "R";
		}
		return result;
	}

}
