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
 * @version 1.0.2
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
}
