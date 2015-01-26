package name.tomflucke.virtualdesktop.ui.layerlist;

/**
 * A collection of constants which describe what information a column in the
 * layer list displays.
 * 
 * @author tom
 * @version 1.0.0
 */
public interface ColumnConstants
{
	/**
	 * Represents a toggle button to show/hide the layer.
	 * 
	 * Uses display specifications.
	 */
	public static final byte HIDE_BUTTON = 1;
	/**
	 * Represents a pane which will be drawn to match the layer.
	 */
	public static final byte PREVIEW = 2;
	/**
	 * Represents a pane with the name of the layer.
	 */
	public static final byte LAYER_NAME = 4;
	/**
	 * Represents a button which moves the layer towards the top in the z-order.
	 * 
	 * Uses display specifications.
	 */
	public static final byte UP_BUTTON = 8;
	/**
	 * Represents a button which moves the layer towards the bottom in the z-order.
	 * 
	 * Uses display specifications.
	 */
	public static final byte DOWN_BUTTON = 16;
}
