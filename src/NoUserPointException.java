/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ahraza
 */
public class NoUserPointException extends Exception
{

	/**
	 * Creates a new instance of <code>NoUserPointException</code> without detail message.
	 */
	public NoUserPointException()
	{
	}

	/**
	 * Constructs an instance of <code>NoUserPointException</code> with the specified detail message.
	 * @param msg the detail message.
	 */
	public NoUserPointException(String msg)
	{
		super(msg);
	}
}
