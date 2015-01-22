package name.tomflucke.virtualdesktop.map;

import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

/**
 * Contains the information describing a tile on the map.
 * 
 * @author tom
 */
public class Tile
{
	/**
	 * A collection of all the tiles by name
	 */
	private final static Map<String, Tile> tiles;
	/**
	 * A collection of all the classes of tiles by name
	 */
	private final static Map<String, List<Tile>> groups;
	
	static
	{
		tiles = new HashMap<String, Tile>();
		groups = new HashMap<String, List<Tile>>();
		
		try
		{
			addTile("Grass", ImageIO.read(Tile.class
			        .getResource("/config/images/green.png")), new String[] {
			        "Test", "Grass" });
			addTile("Water", ImageIO.read(Tile.class
			        .getResource("/config/images/blue.png")), new String[] {
			        "Test", "Water" });
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a tile to the collection
	 * 
	 * @param name
	 *            The name of the tile
	 * @param img
	 *            The image representation of the tile
	 * @param groupNames
	 *            All of the groups to which the tile belongs
	 */
	public static void addTile(String name, Image img, String[] groupNames)
	{
		Tile tile;
		if (!tiles.containsKey(name))
		{
			tile = new Tile(name, img);
			tiles.put(name, tile);
		}
		else
		{
			tile = tiles.get(name);
		}
		for (String groupName : groupNames)
		{
			List<Tile> group;
			if (!groups.containsKey(groupName))
			{
				group = new ArrayList<Tile>(groupNames.length);
				groups.put(groupName, group);
			}
			else
			{
				group = groups.get(groupName);
			}
			if (!group.contains(tile))
			{
				group.add(tile);
			}
		}
	}
	
	/**
	 * Gets a tile with a given name
	 * 
	 * @param name
	 *            The name of the tile
	 * @return A tile which has been assigned the given name or null if none
	 *         exists
	 */
	public static Tile getTile(String name)
	{
		return tiles.get(name);
	}
	
	/**
	 * Gets all the tiles that belong to the given group
	 * 
	 * @param name
	 *            The name of the group
	 * @return All the tiles which were marked as being in the group
	 */
	public static Tile[] getGroup(String name)
	{
		return groups.containsKey(name) ? groups.get(name).toArray(new Tile[0])
		        : new Tile[0];
	}
	
	/**
	 * Gets all the tiles that belong to the given group or, if partialMatch is
	 * true, any group which contains the group name
	 * 
	 * @param name
	 *            The name of the group
	 * @param partialMatch
	 *            If true, will allow any group which contains the name to be
	 *            added to the results
	 * @return All tiles of any group who matches the expression
	 */
	public static Tile[] getGroup(String name, boolean partialMatch)
	{
		return partialMatch ? getGroup(name) : getGroupByPattern(".*"
		        + Pattern.quote(name) + ".*");
	}
	
	/**
	 * Gets all the tiles that belong to any group whose name matches the given
	 * regular expression
	 * 
	 * @param regex
	 *            The regular expression which group names will be tested
	 *            against
	 * @return All tiles of any group who matches the expression
	 */
	public static Tile[] getGroupByPattern(String regex)
	{
		Set<Tile> result = new HashSet<Tile>();
		for (Entry<String, List<Tile>> e : groups.entrySet())
		{
			if (e.getKey().matches(regex))
			{
				result.addAll(e.getValue());
			}
		}
		return result.toArray(new Tile[0]);
	}
	
	/**
	 * Gets all the group names to which any tile belongs.
	 * 
	 * @return The names of all the groups
	 */
	public static String[] getGroupNames()
	{
		return groups.keySet().toArray(new String[0]);
	}
	
	/**
	 * The name of the tile
	 */
	public final String name;
	/**
	 * The image which displays on the map
	 */
	public final transient Image image;
	
	/**
	 * Creates a tile with the given name and image
	 * 
	 * @param name
	 *            The name of the tile
	 * @param img
	 *            The image to be displayed on the map
	 */
	private Tile(String name, Image img)
	{
		this.name = name;
		this.image = img;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}