//---------------------------------------------------------------------
// Name:			Utils.java
// Author:			Bernard.Gorman@computing.dcu.ie
// Author:			Martin.Fredriksson@bth.se
//---------------------------------------------------------------------

package soc.qase.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

import soc.qase.tools.vecmath.Vector2f;
import soc.qase.tools.vecmath.Vector3f;

/*-------------------------------------------------------------------*/
/**	Utility class. Implements a number of common data manipulation
 *	functions; byte-array-to-type converters, angle-to-vector converters,
 *	environment variable parsers, etc. */
/*-------------------------------------------------------------------*/
public class Utils
{
	private static Random r = new Random(System.currentTimeMillis());

/*-------------------------------------------------------------------*/
/**	Convert byte array (8 bytes) to long value.
 *	@param data source data
 *	@param offset source offset
 *	@return long representation of source */
/*-------------------------------------------------------------------*/
	public static long longValue(byte[] data, int offset)
	{
		long result = 0;
		int multiply = 1;
		int value = 0;

		for(int i = 0; i < 8; i++) {
			value = data[i + offset];
			if(value < 0) value = value + 256;
			result = result + (value * multiply);
			multiply = multiply * 256;
		}

		return result;
	}

/*-------------------------------------------------------------------*/
/**	Convert byte array (4 bytes) to int value.
 *	@param data source data
 *	@param offset source offset
 *	@return int representation of source */
/*-------------------------------------------------------------------*/
	public static int intValue(byte[] data, int offset)
	{
		int result = 0;
		int multiply = 1;
		int value = 0;

		for(int i = 0; i < 4; i++) {
			value = data[i + offset];
			if(value < 0) value = value + 256;
			result = result + (value * multiply);
			multiply = multiply * 256;
		}

		return result;
	}

/*-------------------------------------------------------------------*/
/**	Convert byte array (2 bytes) to short value.
 *	@param data source data
 *	@param offset source offset
 *	@return short representation of source */
/*-------------------------------------------------------------------*/
	public static short shortValue(byte[] data, int offset)
	{
		int result = 0;
		int multiply = 1;
		int value = 0;

		for(int i = 0; i < 2; i++) {
			value = data[i + offset];
			if(value < 0) value = value + 256;
			result = result + (value * multiply);
			multiply = multiply * 256;
		}

		return (short)result;
	}

/*-------------------------------------------------------------------*/
/**	Convert byte array (4 bytes) to float value.
 *	@param data source data
 *	@param offset source offset
 *	@return short representation of source */
/*-------------------------------------------------------------------*/
	public static float floatValue(byte[] data, int offset)
	{
		return Float.intBitsToFloat(intValue(data, offset));
	}

/*-------------------------------------------------------------------*/
/**	Convert byte array (4 bytes) to unsigned int value.
 *	@param data source data
 *	@param offset source offset
 *	@return int representation of source */
/*-------------------------------------------------------------------*/
	public static long unsignedIntValue(byte[] data, int offset)
	{
		long result = 0;
		int multiply = 1;
		int value = 0;

		for(int i = 0; i < 4; i++) {
			value = data[i + offset];
			if(value < 0) value = value + 256;
			result = result + (value * multiply);
			multiply = multiply * 256;
		}

		return result;
	}

/*-------------------------------------------------------------------*/
/**	Convert byte array (2 bytes) to unsigned short value.
 *	@param data source data
 *	@param offset source offset
 *	@return short representation of source */
/*-------------------------------------------------------------------*/
	public static int unsignedShortValue(byte[] data, int offset)
	{
		int result = 0;
		int multiply = 1;
		int value = 0;

		for(int i = 0; i < 2; i++) {
			value = data[i + offset];
			if(value < 0) value = value + 256;
			result = result + (value * multiply);
			multiply = multiply * 256;
		}

		return result;
	}

	public static short unsignedByteValue(byte[] data, int offset)
	{
		return (short)((int)(data[offset]) & 0xFF);
	}

/*-------------------------------------------------------------------*/
/**	Convert long value (8 bytes) to byte array.
 *	@param data source data
 *	@param offset source offset
 *	@return long representation of source */
/*-------------------------------------------------------------------*/
	public static byte[] longToByteArray(long value, byte[] data, int offset)
	{
		if(data == null)
			data = new byte[8];

		long temp = Math.abs(value);

		for(int i = 0; i < 8; i++)
		{
			data[offset + i] = (byte)(temp % 256);
			temp = temp / 256;

			if(i == 0 && value < 0)
				data[offset] = (byte)(~data[offset] + 1);
			else if(value < 0)
				data[offset + i] = (byte)(~data[offset + i] + (data[offset + i - 1] == 0 ? 1 : 0));
		}

		return data;
	}

/*-------------------------------------------------------------------*/
/**	Convert integer value (4 bytes) to byte array.
 *	@param data source data
 *	@param offset source offset
 *	@return int representation of source */
/*-------------------------------------------------------------------*/
	public static byte[] intToByteArray(int value, byte[] data, int offset)
	{
		if(data == null)
			data = new byte[4];

		int temp = Math.abs(value);

		for(int i = 0; i < 4; i++)
		{
			data[offset + i] = (byte)(temp % 256);
			temp = temp / 256;

			if(i == 0 && value < 0)
				data[offset] = (byte)(~data[offset] + 1);
			else if(value < 0)
				data[offset + i] = (byte)(~data[offset + i] + (data[offset + i - 1] == 0 ? 1 : 0));
		}

		return data;
	}

/*-------------------------------------------------------------------*/
/**	Convert short value (2 bytes) to byte array.
 *	@param data source data
 *	@param offset source offset
 *	@return short representation of source */
/*-------------------------------------------------------------------*/
	public static byte[] shortToByteArray(short value, byte[] data, int offset)
	{
		if(data == null)
			data = new byte[2];

		short temp = (short)Math.abs(value);

		for(int i = 0; i < 2; i++)
		{
			data[offset + i] = (byte)(temp % 256);
			temp = (short)(temp / 256);

			if(i == 0 && value < 0)
				data[offset] = (byte)(~data[offset] + 1);
			else if(value < 0)
				data[offset + i] = (byte)(~data[offset + i] + (data[offset + i - 1] == 0 ? 1 : 0));
		}

		return data;
	}

/*-------------------------------------------------------------------*/
/**	Get string length of byte array.
 *	@param data source data
 *	@param offset source offset
 *	@return length of string. */
/*-------------------------------------------------------------------*/
	public static int stringLength(byte[] data, int offset)
	{
		int result = 0;

		while(data[offset + result] != '\0') result++;
		return result;
	}

/*-------------------------------------------------------------------*/
/**	Convert byte array to String value.
 *	@param data source data
 *	@param offset source offset
 *	@param length string length.
 *	@return String representation of source */
/*-------------------------------------------------------------------*/
	public static String stringValue(byte[] data, int offset, int length)
	{
		return new String(data, offset, length);
	}

/*-------------------------------------------------------------------*/
/**	Search for a given subsequence of bytes in a larger byte array.
 *	@param data source
 *	@param searchData byte search 'string'
 *	@return the index in the large array at which the smaller byte
 *	sequence begins */
/*-------------------------------------------------------------------*/
	public static int byteArraySearch(byte[] data, byte[] searchData)
	{
		for(int i = 0; i <= data.length - searchData.length; i++)
		{
			if(data[i] == searchData[0] && Arrays.equals(searchData, extractBytes(data, i, searchData.length)))
				return i;
		}

		return -1;
	}

/*-------------------------------------------------------------------*/
/**	Extract a subsequence of bytes from a larger array.
 *	@param data source data
 *	@param offset source offset
 *	@param length byte sequence length
 *	@return byte array duplicate of specified subsequence */
/*-------------------------------------------------------------------*/
	public static byte[] extractBytes(byte[] data, int offset, int length)
	{
		byte[] copiedBytes = new byte[length];

		for(int i = 0; i < copiedBytes.length; i++)
			copiedBytes[i] = data[offset + i];

		return copiedBytes;
	}

/*-------------------------------------------------------------------*/
/**	Extract a subsequence of bytes from a larger array from a given
 *	offset to the end.
 *	@param data source data
 *	@param offset source offset
 *	@return byte array duplicate of specified subsequence */
/*-------------------------------------------------------------------*/
	public static byte[] extractBytes(byte[] data, int offset)
	{
		return extractBytes(data, offset, data.length - offset);
	}

/*-------------------------------------------------------------------*/
/**	Concatenate two byte arrays.
 *	@param data1 first byte array
 *	@param data2 second byte array
 *	@return concatenated byte array */
/*-------------------------------------------------------------------*/
	public static byte[] concatBytes(byte[] data1, byte[] data2)
	{
		if(data1 == null)
			return data2;
		else if(data2 == null)
			return data1;

		byte[] result = new byte[data1.length + data2.length];

		for(int i = 0; i < result.length; i++)
			result[i] = (i < data1.length ? data1[i] : data2[i - data1.length]);

		return result;
	}

/*-------------------------------------------------------------------*/
/**	Remove the given sequence of bytes from a larger array.
 *	@param data source array
 *	@param offset source offset
 *	@param length byte sequence length
 *	@return the array with the byte subsequence removed */
/*-------------------------------------------------------------------*/
	public static byte[] removeBytes(byte[] data, int offset, int length)
	{
		if((offset + length) >= data.length)
			return null;

		int index = 0;
		byte[] result = new byte[data.length - length];

		for(int i = 0; i < data.length; i++)
		{
			if(!(i >= offset && i < offset + length))
				result[index++] = data[i];
		}

		return result;
	}

/*-------------------------------------------------------------------*/
/**	Remove the first x bytes of an array.
 *	@param data source array
 *	@param length number of bytes to be removed from start of array
 *	@return the array with the byte subsequence removed */
/*-------------------------------------------------------------------*/
	public static byte[] removeBytes(byte[] data, int length)
	{
		return removeBytes(data, 0, length);
	}

/*-------------------------------------------------------------------*/
/**	Convert an x and y directional vector into an angle.
 *	@param x the horizontal component of the vector
 *	@param y the vertical component of the vector
 *	@param returnAsDegrees if true, return answer in degrees, otherwise radians
 *	@param canBeNegative if true, -180 <= x <= 180, otherwise 0 <= x <= 360
 *	@return the final angular equivalent of the directional vector */
/*-------------------------------------------------------------------*/
	public static float quadTan(double x, double y, boolean returnAsDegrees, boolean canBeNegative)
	{
		if(x == 0 && y == 0)
			return Float.MIN_VALUE;
		else if(x == 0)
			return (y < 0 ? (float)(-Math.PI / 2.0) : (float)(Math.PI / 2.0));

		double basic = Math.abs(Math.atan(y / x));

		if(x > 0)
		{
			if(y < 0)
				basic *= -1;
		}
		else
		{
			if(y > 0)
				basic = Math.PI - basic;
			else
				basic -= Math.PI;
		}

		if(!canBeNegative && basic < 0)
			basic += (2.0 * Math.PI);

		return (returnAsDegrees ? (float)(basic * 180.0 / Math.PI) : (float)basic);
	}

/*-------------------------------------------------------------------*/
/**	Compute the yaw and pitch angles corresponding to a 3D directional vector.
 *	@param toDir the 3D directional vector to convert to angular representation
 *	@return an array of 3 floats indicating yaw, pitch and (redundantly) roll */
/*-------------------------------------------------------------------*/
	public static float[] calcAngles(Vector3f toDir)
	{
		float forward = 0;
		float fAngles[] = new float[3];

		if (toDir.y == 0 && toDir.x == 0)
		{
			fAngles[0] = 0;

			if (toDir.z > 0)
				fAngles[1] = 90;
			else
				fAngles[1] = 270;
		}
		else
		{
			fAngles[0] = Utils.quadTan(toDir.x, toDir.y, true, false);

			if (fAngles[0] < 0)
				fAngles[0] += 360;

			forward = (float)Math.sqrt(toDir.x * toDir.x + toDir.y * toDir.y);

			fAngles[1] = -quadTan(forward, toDir.z, true, true);
		}

		return fAngles;
	}

/*-------------------------------------------------------------------*/
/**	A utility function. Copies data from one integer array to another.
 *	@param fromArray the source array
 *	@param toArray the target array
 *	@param fromOffset the position in the source array to copy from
 *	@param toOffset the position in the destination array to copy to
 *	@param length the number of bytes to copy
 *	@return a reference to the destination array, for convenience */
/*-------------------------------------------------------------------*/
	public static int[] copyArray(int[] fromArray, int[] toArray, int fromOffset, int toOffset, int length)
	{
		for(int i = 0; i < length; i++)
			toArray[toOffset + i] = fromArray[fromOffset + i];

		return toArray;
	}

/*-------------------------------------------------------------------*/
/**	Convenience function. Copies all data from one integer array to another.
 *	@param fromArray the source array
 *	@param toArray the target array
 *	@return a reference to the destination array, for convenience */
/*-------------------------------------------------------------------*/
	public static int[] copyArray(int[] fromArray, int[] toArray)
	{
		return copyArray(fromArray, toArray, 0, 0, fromArray.length);
	}

/*-------------------------------------------------------------------*/
/**	A utility function. Copies data from one byte array to another.
 *	@param fromArray the source array
 *	@param toArray the target array
 *	@param fromOffset the position in the source array to copy from
 *	@param toOffset the position in the destination array to copy to
 *	@param length the number of bytes to copy
 *	@return a reference to the destination array, for convenience */
/*-------------------------------------------------------------------*/
	public static byte[] copyArray(byte[] fromArray, byte[] toArray, int fromOffset, int toOffset, int length)
	{
		for(int i = 0; i < length; i++)
			toArray[toOffset + i] = fromArray[fromOffset + i];

		return toArray;
	}

/*-------------------------------------------------------------------*/
/**	Convenience function. Copies all data from one byte array to another.
 *	@param fromArray the source array
 *	@param toArray the target array
 *	@return a reference to the destination array, for convenience */
/*-------------------------------------------------------------------*/
	public static byte[] copyArray(byte[] fromArray, byte[] toArray)
	{
		return copyArray(fromArray, toArray, 0, 0, fromArray.length);
	}

/*-------------------------------------------------------------------*/
/**	Convert a angle into a 2D directional vector.
 *	@param angle the angle to be converted
 *	@return the 2D directional vector */
/*-------------------------------------------------------------------*/
	public static Vector2f degreesToVector2f(float angle)
	{
		return new Vector2f((float)Math.cos(Math.toRadians(angle)), (float)Math.sin(Math.toRadians(angle)));
	}

	public static String parseEnvironmentVariables(String fKey)
	{
		Process p = null;
		Properties envVars = new Properties();
		Runtime r = Runtime.getRuntime();

		String OS = System.getProperty("os.name").toLowerCase();

		try
		{
			if (OS.indexOf("windows 9") != -1)
				p = r.exec("command.com /c set");
			else if ((OS.indexOf("nt") != -1) || (OS.indexOf("windows") != -1))
			    p = r.exec("cmd.exe /c set");
			else
				p = r.exec("env");

			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

			int idx = 0;
			String line = null, key = null, value = null;

			while((line = br.readLine()) != null)
			{
				idx = line.indexOf('=');
				key = line.substring(0, idx);
				value = line.substring(idx + 1);

				envVars.setProperty(key, value);
			}
		}
		catch(IOException ioe)
		{
			return null;
		}

		return envVars.getProperty(fKey);
	}

/*-------------------------------------------------------------------*/
/**	Generate and return a random number.
 *	@param canBeNegative if true, the random number will lie in the range
 *	[-1, 1]; otherwise [0, 1]
 *	@return the random number */
/*-------------------------------------------------------------------*/
	public static double getRandom(boolean canBeNegative)
	{
		if(canBeNegative)
			return r.nextDouble() - r.nextDouble();
		else
			return r.nextDouble();
	}
}
