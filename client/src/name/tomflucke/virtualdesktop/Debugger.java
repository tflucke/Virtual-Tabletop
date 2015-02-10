package name.tomflucke.virtualdesktop;

public abstract class Debugger {
	/**
	 * A method which can be used as a short way to compare to debug levels, and
	 * if the given is greater than or equal to the required, prints the message
	 * to the console.
	 * 
	 * @param debugMode The current debug mode
	 * @param requiredLevel The level required to print the message
	 * @param message A message to print
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
