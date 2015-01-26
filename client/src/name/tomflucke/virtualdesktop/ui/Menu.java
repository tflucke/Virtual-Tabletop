package name.tomflucke.virtualdesktop.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import name.tomflucke.virtualdesktop.DebugConstants;

/*
 * Unsure if this will be part of further designs.
 */

/**
 * A class which builds and displays menus.
 * This class views menus as an ordered collection of buttons. Every
 * button has a name and an action associated with it.
 * 
 * @author Thomas Flucke
 * @version 1.0.0
 */
public class Menu extends JPanel implements DebugConstants
{
	private static final long serialVersionUID = 6111973261024662427L;
	private static byte DEBUG_MODE = OFF;
	
	/**
	 * A collection of all the possible menus by name.
	 */
	protected static final Map<String, ButtonInfo[]> menus;
	
	static
	{
		DebugConstants.printIfDebug(DEBUG_MODE, VERBOSE, "Creating menu options.");
		menus = new HashMap<String, ButtonInfo[]>();
		
		ButtonInfo[] topMenu = new ButtonInfo[2];
		topMenu[0] = new ButtonInfo("Play");
		topMenu[1] = new ButtonInfo("Map");
		addMenu("", topMenu);
		
		ButtonInfo[] playMenu = new ButtonInfo[2];
		playMenu[0] = new ButtonInfo("LAN");
		playMenu[1] = new ButtonInfo("Online");
		addMenu("Play", playMenu);
		
		ButtonInfo[] mapMenu = new ButtonInfo[2];
		mapMenu[0] = new ButtonInfo("New");
		mapMenu[1] = new ButtonInfo("Edit");
		addMenu("Map", mapMenu);
	}
	
	/**
	 * Adds a new menu to the collection
	 * 
	 * @param name
	 *            Will identity the new menu.
	 * @param buttons
	 *            All the information about the buttons to be displayed.
	 */
	public static void addMenu(String name, ButtonInfo[] buttons)
	{
		if (DEBUG_MODE >= VERBOSE)
		{
			System.out.println("Creating new menu " + name + " with buttons:");
			for (ButtonInfo bi : buttons)
			{
				System.out.println("\t" + bi);
			}
		}
		menus.put(name, buttons);
	}
	
	/**
	 * Contains all the details necessary to create a new button.
	 * This mostly servers as a wrapper class for the name and action.
	 * 
	 * @author tom
	 */
	protected static class ButtonInfo
	{
		/**
		 * The text which will be displayed on the button.
		 */
		public final String name;
		/**
		 * Determines what will happen when the button is pressed.
		 */
		public final String action;
		
		/**
		 * Creates a new ButtonInfo with a given name and action.
		 * 
		 * @param name
		 *            The text to appear on the button
		 * @param action
		 *            The action taken when clicked
		 */
		public ButtonInfo(String name, String action)
		{
			this.name = name;
			this.action = action;
		}
		
		/**
		 * Creates a new ButtonInfo with a given name.
		 * In this case, the name is also the action to be taken.
		 * 
		 * @param name
		 *            The text to appear on the button and the action taken when
		 *            clicked
		 */
		public ButtonInfo(String name)
		{
			this.name = name;
			this.action = name;
		}
		
		@Override
		public String toString()
		{
			return name + " (" + action + ")";
		}
	}
	
	/**
	 * When a button is pressed determines the appropriate action and executes
	 * it.
	 */
	private final ActionListener buttonAction = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent ae)
		{
			goToMenu(ae.getActionCommand());
			revalidate();
			repaint();
		}
	};
	
	/**
	 * The menu that was displayed before the current menu.
	 * Used for a back button.
	 */
	private transient String lastMenu = new String();
	
	{
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		goToMenu("");
	}
	
	/**
	 * Creates a button from a ButtonInfo object.
	 * 
	 * @param buttonInfo
	 *            Describes the button to be created
	 * @return A button matching the given parameter.
	 */
	protected AbstractButton makeButton(ButtonInfo buttonInfo)
	{
		DebugConstants.printIfDebug(DEBUG_MODE, VERBOSE, "Creating a new button: "+buttonInfo);
		AbstractButton result = new JButton(buttonInfo.name);
		result.addActionListener(buttonAction);
		result.setActionCommand(buttonInfo.action);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.setHorizontalAlignment(SwingConstants.CENTER);
		return result;
	}
	
	/**
	 * Clears the current menu and repopulates it with a predescribed menu
	 * matching the given name.
	 * 
	 * @param menu
	 *            The name of the menu to which the method would switch
	 */
	public void goToMenu(String menu)
	{
		DebugConstants.printIfDebug(DEBUG_MODE, VERBOSE, "Clearing current menu.");
		removeAll();
		add(Box.createVerticalGlue());
		Box.Filler lastFiller = null;
		for (ButtonInfo buttonInfo : menus.get(menu))
		{
			DebugConstants.printIfDebug(DEBUG_MODE, VERBOSE, "Adding new button: "+buttonInfo);
			add(makeButton(buttonInfo));
			
			DebugConstants.printIfDebug(DEBUG_MODE, VERBOSE, "Adding filler.");
			lastFiller = new Box.Filler(new Dimension(0, 10), new Dimension(0,
			        50), new Dimension(0, 100));
			add(lastFiller);
		}
		if (menu.length() == 0)
		{
			DebugConstants.printIfDebug(DEBUG_MODE, VERBOSE, "Removing unused filler.");
			remove(lastFiller);
		}
		else
		{
			DebugConstants.printIfDebug(DEBUG_MODE, VERBOSE, "Adding back button.");
			add(makeButton(new ButtonInfo("Back", lastMenu)));
		}
		add(Box.createVerticalGlue());
		DebugConstants.printIfDebug(DEBUG_MODE, VERBOSE, "Saving current menu name: "+menu);
		lastMenu = menu;
	}
}
