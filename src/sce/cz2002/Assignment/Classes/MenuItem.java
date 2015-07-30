package sce.cz2002.Assignment.Classes;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Represents a menu item in the menu of this restaurant<p>
 * Implements the Serializable interface to allow menu items
 * to be saved<br>
 * Implements the Comparable interface to allow menu items
 * to be sorted by item type
 * 
 * @author Chin Meng
 *
 */
public class MenuItem implements Serializable, Comparable<MenuItem>
{
	/**
	 * Generated serial version ID for serializable classes
	 */
	private static final long serialVersionUID = -6956226729093114621L;
	
	/**
	 * Enumeration type used to determine the item type of this menu item
	 * 
	 * @author Chin Meng
	 *
	 */
	public static enum ItemType
	{
		MainCourse("Main Course"),
		Drink("Drink"),
		Dessert("Dessert"),
		SetPackage("Set Package");
		
		private final String value;
		  
		private ItemType(String value) {
			this.value = value;
		}

		public String toStrValue() {
			return value;
		}
	}
	
	/**
	 * The item type of this menu item
	 */
	private ItemType _itemType;
	
	/**
	 * The name of this menu item
	 */
	private String _name;
	
	/**
	 * The description of this menu item
	 */
	private String _desc;
	
	/**
	 * The price of this menu item
	 */
	private double _price;
	
	/**
	 * Creates a menu item with the given item type, name, description
	 * and the price
	 * 
	 * @param itemType this menu item's item type
	 * @param name this menu item's name
	 * @param desc this menu item's description
	 * @param price this menu item's price
	 */
	public MenuItem(ItemType itemType, String name, String desc, double price)
	{
		_itemType = itemType;
		_name = name;
		_desc = desc;
		_price = price;
	}
	
	/**
	 * Gets the item type of this menu item
	 * 
	 * @return this menu item's item type
	 */
	public ItemType getItemType() {
		return _itemType;
	}
	
	/**
	 * Changes the item type of this menu item
	 * 
	 * @param newItemType this menu item's new item type
	 */
	public void setItemType(ItemType newItemType) {
		_itemType = newItemType;
	}
	
	/**
	 * Gets the name of this menu item
	 * 
	 * @return This menu item's name
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Changes the name of this menu item
	 * 
	 * @param newName This menu item's new name
	 */
	public void setName(String newName) {
		_name = newName;
	}
	
	/**
	 * Gets the description of this menu item
	 * 
	 * @return This menu item's description
	 */
	public String getDesc() {
		return _desc;
	}
	
	/**
	 * Changes the description of this menu item
	 * 
	 * @param newDesc This menu item's new description
	 */
	public void setDesc(String newDesc) {
		_desc = newDesc;
	}
	
	/**
	 * Gets the price of this menu item
	 * 
	 * @return This menu item's price
	 */
	public double getPrice() {
		return _price;
	}
	
	/**
	 * Changes the price of this menu item
	 * 
	 * @param newPrice This menu item's new price
	 */
	public void setPrice(double newPrice) {
		_price = newPrice;
	}
	
	/**
	 * Display the details of this item with proper formatting<br>
	 * Displayed information include menu item's name, price and description
	 */
	public void displayItemDetails() {
		System.out.printf("%-30s", getName());
		System.out.printf("%20s%n",
				new DecimalFormat("$###,##0.00").format(getPrice()));
		System.out.println("\"" + getDesc() + "\"");
	}
	
	/**
	 * Display item name, item type and the item price in a single line<br>
	 * Provides a summary of this menu item
	 */
	public void displayItemSummary() {
		System.out.printf("%-30s", getName());
		System.out.printf("%-20s", "[" + getItemType().toStrValue() + "]");
		System.out.printf("%12s%n",
				new DecimalFormat("$###,##0.00").format(getPrice()));
	}
	
	/**
	 * Function used to compare menu items based on their item type<br>
	 * Used when calling <b>Collections.sort</b>
	 * 
	 * @param o menuItem to be compared with
	 */
	@Override
	public int compareTo(MenuItem o)
	{
		// Compare based on ItemType
		return this.getItemType().compareTo(o.getItemType());
	}
}