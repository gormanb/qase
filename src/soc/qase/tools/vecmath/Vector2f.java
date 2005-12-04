//---------------------------------------------------------------------
// Name:			Vector2f.java
// Author:			Bernard.Gorman@computing.dcu.ie
//---------------------------------------------------------------------

package soc.qase.tools.vecmath;

import java.io.Serializable;
import soc.qase.state.Origin;

/*-------------------------------------------------------------------*/
/**	A class, based closely on the Java3D equivalent, which is used to
 *	represent and manipulate 2-dimensional vectors in single precision.
 *	This class is also used to represent points in certain contexts. */
/*-------------------------------------------------------------------*/
public class Vector2f implements Serializable
{
	public float x, y;

/*-------------------------------------------------------------------*/
/**	Default constructor. Initialises location to (0, 0). */
/*-------------------------------------------------------------------*/
   public Vector2f()
	{
		x = y = 0.0f;
	}

/*-------------------------------------------------------------------*/
/**	Initialises location to (Origin.x, Origin.y). This is because Quake
 *	2 uses a co-ordinate system whereby the Z-axis is the vertical plane
 *	from the agent's point of view.
 *	@param o the Origin object from which the current vector's
 *	position is set */
/*-------------------------------------------------------------------*/
	public Vector2f(Origin o)
	{
		set(o.getX(), o.getY());
	}

/*-------------------------------------------------------------------*/
/**	Initialises location to (x, y).
 *	@param x the x co-ordinate
 *	@param y the y co-ordinate */
/*-------------------------------------------------------------------*/
	public Vector2f(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

/*-------------------------------------------------------------------*/
/**	Initialises location to (x, y).
 *	@param x the x co-ordinate
 *	@param y the y co-ordinate */
/*-------------------------------------------------------------------*/
	public Vector2f(double x, double y)
	{
		this.x = (float)x;
		this.y = (float)y;
	}

/*-------------------------------------------------------------------*/
/**	Initialises location to (xy[0], xy[1]).
 *	@param xy the x and y co-ordinates */
/*-------------------------------------------------------------------*/
	public Vector2f(float xy[])
	{
		x = xy[0];
		y = xy[1];
	}

/*-------------------------------------------------------------------*/
/**	Initialises location to (v.x, v.y) - that is, sets this vector to
 *	be the same as the argument vector.
 *	@param v the x and y co-ordinates */
/*-------------------------------------------------------------------*/
	public Vector2f(Vector2f v)
	{
		x = v.x;
		y = v.y;
	}

/*-------------------------------------------------------------------*/
/**	Set the specified dimension to the specified value. Useful for
 *	setting parameters in loops.
 *	@param dim the dimension to be set (0 = x, 1 = y)
 *	@param val the value to be set */
/*-------------------------------------------------------------------*/
	public final void set(int dim, float val)
	{
 		if(dim == 0)
			x = val;
		else if(dim == 1)
			y = val;
	}

/*-------------------------------------------------------------------*/
/**	Set the location to (x, y).
 *	@param x the x co-ordinate
 *	@param y the y co-ordinate set */
/*-------------------------------------------------------------------*/
	public final void set(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

/*-------------------------------------------------------------------*/
/**	Set the location to (xy[0], xy[1]).
 *	@param xy the x and y co-ordinates */
/*-------------------------------------------------------------------*/
	public final void set(float xy[])
	{
		x = xy[0];
		y = xy[1];
	}

/*-------------------------------------------------------------------*/
/**	Set the location to (v.x, v.y). That is, set this vector to match
 *	the argument vector.
 *	@param v the x and y co-ordinates */
/*-------------------------------------------------------------------*/
	public final void set(Vector2f v)
	{
		x = v.x;
		y = v.y;
	}

/*-------------------------------------------------------------------*/
/**	Set the location to (Origin.x, Origin.y). This is because Quake
 *	2 uses a co-ordinate system whereby the Z-axis is the vertical plane
 *	from the agent's point of view.
 *	@param o the Origin object from which the current vector's
 *	position is set */
/*-------------------------------------------------------------------*/
	public final void set(Origin o)
	{
		x = o.getX();
		y = o.getY();
	}

/*-------------------------------------------------------------------*/
/**	Get the value of the specified dimension. Useful for accessing
 *	vectors from loops.
 *	@param dim the dimension to be set (0 = x, 1 = y)
 *	@return the value of the specified dimension */
/*-------------------------------------------------------------------*/
	public final float get(int dim)
	{
		if(dim == 0)
			return x;
		else if(dim == 1)
			return y;
		else
			return Float.NaN;
	}

/*-------------------------------------------------------------------*/
/**	Return the x and y co-ordinates in the first and second positions
 *	of the passed array.
 *	@param xy the array to be populated */
/*-------------------------------------------------------------------*/
	public final void get(float xy[])
	{
		xy[0] = x;
		xy[1] = y;
	}

/*-------------------------------------------------------------------*/
/**	Add two vectors and store the result in this vector.
 *	@param v1 the first vector to be added
 *	@param v2 the second vector to be added*/
/*-------------------------------------------------------------------*/
	public final void add(Vector2f v1, Vector2f v2)
	{
		x = v1.x + v2.x;
		y = v1.y + v2.y;
	}

/*-------------------------------------------------------------------*/
/**	Add this vector to the argument and store the result locally.
 *	@param v the vector to be added to the current vector */
/*-------------------------------------------------------------------*/
	public final void add(Vector2f v)
	{
		x += v.x;
		y += v.y;
	}

/*-------------------------------------------------------------------*/
/**	Subtract the second vector from the first, and store the result in
 *	the current vector.
 *	@param v1 the vector to subtract from
 *	@param v2 the vector to subtract */
/*-------------------------------------------------------------------*/
	public final void sub(Vector2f v1, Vector2f v2)
	{
		x = v1.x - v2.x;
		y = v1.y - v2.y;
	}

/*-------------------------------------------------------------------*/
/**	Subtract the argument vector from the current vector, and store
 *	the result locally.
 *	@param v the vector to subtract */
/*-------------------------------------------------------------------*/
	public final void sub(Vector2f v)
	{
		x -= v.x;
		y -= v.y;
	}

/*-------------------------------------------------------------------*/
/**	Negate the argument vector and store the result locally.
 *	@param v the vector to negate */
/*-------------------------------------------------------------------*/
	public final void negate(Vector2f v)
	{
		x = -v.x;
		y = -v.y;
	}

/*-------------------------------------------------------------------*/
/**	Negate the current vector in place. */
/*-------------------------------------------------------------------*/
	public final void negate()
	{
		x = -x;
		y = -y;
	}

/*-------------------------------------------------------------------*/
/**	Scale the argument vector and store the result in the current vector.
 *	@param s the scaling factor
 *	@param v the vector to be scaled */
/*-------------------------------------------------------------------*/
	public final void scale(float s, Vector2f v)
	{
		x = s * v.x;
		y = s * v.y;
	}

/*-------------------------------------------------------------------*/
/**	Scale the current vector in place.
 *	@param s the scaling factor */
/*-------------------------------------------------------------------*/
	public final void scale(float s)
	{
		x *= s;
		y *= s;
	}

/*-------------------------------------------------------------------*/
/**	Scale the first argument vector, add it to the second, and store
 *	the result locally.
 *	@param s the scaling factor
 *	@param v1 the vector to be scaled 
 *	@param v2 the vector to add */
/*-------------------------------------------------------------------*/
	public final void scaleAdd(float s, Vector2f v1, Vector2f v2)
	{
		x = s * v1.x + v2.x;
		y = s * v1.y + v2.y;
	}

/*-------------------------------------------------------------------*/
/**	Scale the argument vector and add it to the current vector in place.
 *	@param s the scaling factor
 *	@param v the vector to be scaled */
/*-------------------------------------------------------------------*/
	public final void scaleAdd(float s, Vector2f v)
	{
		x = s * x + v.x;
		y = s * y + v.y;
	}

/*-------------------------------------------------------------------*/
/**	Compare the current vector against another vector.
 *	@param v the vector to test the current vector against
 *	@return true if the two vectors are equal, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean equals(Vector2f v)
	{
		return x == v.x && y == v.y;
	}

/*-------------------------------------------------------------------*/
/**	Compare the current vector against another object. Returns true
 *	only if the object is a Vector2f with the same co-ordinates.
 *	@param obj the object to test the current vector against
 *	@return true if equal, false otherwise */
/*-------------------------------------------------------------------*/
	public boolean equals(Object obj)
	{
		try
		{
			return equals((Vector2f)obj);
		}
		catch(NullPointerException npe)
		{
			return false;
		}
	}

/*-------------------------------------------------------------------*/
/**	Clamp the vector to specified minimum and maximum limits.
 *	@param min the minimum allowed value of the vector's co-ordinates
 *	@param max the maximum allowed value of the vector's co-ordinates */
/*-------------------------------------------------------------------*/
	public final void clamp(float min, float max)
	{
		if(x > max)
			x = max;
		else if(x < min)
			x = min;

		if(y > max)
			y = max;
		else if(y < min)
			y = min;
	}

/*-------------------------------------------------------------------*/
/**	Clamp the argument vector to specified minimum and maximum limits,
 *	and store the result locally.
 *	@param min the minimum allowed value of the vector's co-ordinates
 *	@param max the maximum allowed value of the vector's co-ordinates
 *	@param v the vector to be clamped */
/*-------------------------------------------------------------------*/
	public final void clamp(float min, float max, Vector2f v)
	{
		if(v.x > max)
			x = max;
		else if(v.x < min)
			x = min;
		else
			x = v.x;

		if(v.y > max)
			y = max;
		else if(v.y < min)
			y = min;
		else
			y = v.y;
	}

/*-------------------------------------------------------------------*/
/**	Set the current vector's co-ordinates to their absolute values. */
/*-------------------------------------------------------------------*/
	public final void absolute()
	{
		x = Math.abs(x);
		y = Math.abs(y);
	}

/*-------------------------------------------------------------------*/
/**	Set the argument vector's co-ordinates to their absolute values
 *	and store the result locally.
 *	@param v the vector whose absolute values are to be obtained */
/*-------------------------------------------------------------------*/
	public final void absolute(Vector2f v)
	{
		x = Math.abs(v.x);
		y = Math.abs(v.y);
	}

/*-------------------------------------------------------------------*/
/**	Linearly interpolates between this vector and vector v, and places
 *	the result into this vector: this = (1-alpha) * this + alpha * v. 
 *	@param v the vector to be used for interpolation
 *	@param alpha the alpha interpolation parameter */
/*-------------------------------------------------------------------*/
	public final void interpolate(Vector2f v, float alpha)
	{
		x = (1.0F - alpha) * x + alpha * v.x;
		y = (1.0F - alpha) * y + alpha * v.y;
	}

/*-------------------------------------------------------------------*/
/**	Linearly interpolates between the two argument vectors, and places
 *	the result into this vector: this = (1-alpha) * v1 + alpha * v2. 
 *	@param v1 the first vector to be used for interpolation
 *	@param v2 the second vector to be used for interpolation
 *	@param alpha the alpha interpolation parameter */
/*-------------------------------------------------------------------*/
	public final void interpolate(Vector2f v1, Vector2f v2, float alpha)
	{
		x = (1.0F - alpha) * v1.x + alpha * v2.x;
		y = (1.0F - alpha) * v1.y + alpha * v2.y;
	}

/*-------------------------------------------------------------------*/
/**	Construct and return an exact duplicate of the current vector. 
 *	@return a duplicate vector */
/*-------------------------------------------------------------------*/
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch(CloneNotSupportedException cnse)
		{
			throw new InternalError();
		}
	}

/*-------------------------------------------------------------------*/
/**	Computes the dot product of this vector and vector v. 
 *	@param v the vector with which to compute the dot product
 *	@return the dot product */
/*-------------------------------------------------------------------*/
	public final float dot(Vector2f v)
	{
		return x * v.x + y * v.y;
	}

/*-------------------------------------------------------------------*/
/**	Compute the length of this vector. 
 *	@return the vector's length */
/*-------------------------------------------------------------------*/
	public final float length()
	{
		return (float)Math.sqrt(x * x + y * y);
	}

/*-------------------------------------------------------------------*/
/**	Compute and return the distance between this vector and the argument
 *	vector, treating the vectors as points.
 *	@param v the vector to which to find the distance */
/*-------------------------------------------------------------------*/
	public final float distance(Vector2f v)
	{
		return (float)Math.sqrt((x - v.x) * (x - v.x) + (y - v.y) * (y - v.y));
	}

/*-------------------------------------------------------------------*/
/**	Normalise the argument vector and store the result locally.
 *	@param v the vector to normalise */
/*-------------------------------------------------------------------*/
	public final void normalize(Vector2f v)
	{
		set(v);
		normalize();
	}

/*-------------------------------------------------------------------*/
/**	Normalise the current vector in place.*/
/*-------------------------------------------------------------------*/
	public final void normalize()
	{
		scale(1.0f / length());
	}

/*-------------------------------------------------------------------*/
/**	Returns the angle between two vectors in degrees, in the range
 *	[0, 180].
 *	@param v the vector with which to compute the angle
 *	@return the angle in degrees */
/*-------------------------------------------------------------------*/
	public final float angle(Vector2f v)
	{
		double d = dot(v) / (length() * v.length());

		if(d < -1D)
			d = -1D;

		if(d > 1.0D)
			d = 1.0D;

		return (float)Math.toDegrees(Math.acos(d));
	}

/*-------------------------------------------------------------------*/
/**	Convert the vector to array form and return.
 *	@return the vector in array form */
/*-------------------------------------------------------------------*/
	public final float[] toArray()
	{
		return new float[]{ x, y };
	}

/*-------------------------------------------------------------------*/
/**	Return a string representation of the vector.
 *	@return a string representation of the vector */
/*-------------------------------------------------------------------*/
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
}
