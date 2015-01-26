package name.tomflucke.virtualdesktop;

/**
 * The interface simply contains a set of constants which can be used anywhere
 * in the program.
 * 
 * Each constant provides more information than any of the previous ones.
 * Classes using this interface should use <code>printIfDebug</code> methods to
 * print debug information.
 * 
 * @author Thomas Flucke
 * @version 1.0.1
 */
public interface DebugConstants
{
	/**
	 * Print no information to console.
	 */
	public static final byte OFF = 0;
	
	/**
	 * Print to console only if a non-recoverable error occurs.
	 */
	public static final byte FATAL = 1;
	
	/**
	 * Print to console if any error occurs.
	 */
	public static final byte ERROR = 2;
	
	/**
	 * Print to console if anything note-worthy or of any concern occurs.
	 */
	public static final byte WARNING = 3;
	
	/**
	 * Print everything that occurs.
	 */
	public static final byte VERBOSE = 4;
	
	/**
	 * A method which can be used as a short way to compare to debug levels, and
	 * if the given is greater than or equal to the required, prints the message
	 * to the console.
	 * 
	 * @param debugMode The current debug mode
	 * @param requiredLevel The level required to print the message
	 * @param message A message to print
	 * @since 1.0.1
	 */
	public static void printIfDebug(byte debugMode, byte requiredLevel,
	        String message)
	{
		if (debugMode >= requiredLevel)
		{
			System.out.println(message);
		}
	}
}
