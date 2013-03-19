package jSIMD;

public class LengthException extends IllegalArgumentException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public  LengthException()
	{
		super("Arrays must be the same length");
	}
}
