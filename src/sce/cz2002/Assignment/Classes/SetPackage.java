package sce.cz2002.Assignment.Classes;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Set Package in the menu of this restaurant<br>
 * A set package is also a {@link MenuItem}
 * 
 * @author Chin Meng
 *
 */
public class SetPackage extends MenuItem
{
	/**
	 * Generated serial version ID for serializable classes
	 */
	private static final long serialVersionUID = 4669978176050005898L;
	
	/**
	 * Discount applied to all menu items that are added into a set package
	 */
	private static final double ITEM_DISCOUNT = 0.8;
	
	/**
	 * A list of all menu items in this set package
	 */
	private List<MenuItem> _setPackageItems;
	
	
	/**
	 * Creates a new set package with the given information
	 * 
	 * @param itemType A set package is created with the enumerated item type
	 * 				   SetPackage
	 * @param name This set package's name
	 * @param desc This set package's description
	 * @param price This set package's price
	 */
	public SetPackage(ItemType itemType, String name, String desc, double price) {
		super(itemType, name, desc, price);
		
		_setPackageItems = new ArrayList<MenuItem>();
	}
	
	/**
	 * Creates a new set package with the given information<br>
	 * Used for loading an existing set package if the list of
	 * menu items is provided
	 * 
	 * @param itemType A set package is created with the enumerated item type
	 * 				   SetPackage
	 * @param name This set package's name
	 * @param desc This set package's description
	 * @param price This set package's price
	 * @param setPackageItems This set package's list of menu items (if any)
	 */
	public SetPackage(ItemType itemType, String name, String desc, double price,
			List<MenuItem> setPackageItems) {
		super(itemType, name, desc, price);
		
		if(setPackageItems != null)
			_setPackageItems = setPackageItems;
		else
			_setPackageItems = new ArrayList<MenuItem>();
	}
	
	/**
	 * Allows adding menu item to a set package<p>
	 * A <b>20% discount</b> is applied to all menu items that are added into
	 * the set package<br>
	 * The price of the set package is updated, and the menu items within
	 * the set<br> package are sorted based on their item type
	 * 
	 * @param newPackageItem The menu item to be added to the set package
	 */
	public void addItemToPackage(MenuItem newPackageItem)
	{
		_setPackageItems.add(newPackageItem);
		setPrice(getPrice() + (newPackageItem.getPrice() * ITEM_DISCOUNT));
		
		Collections.sort(_setPackageItems);
	}
	
	/**
	 * Allows removing menu item from a set package<p>
	 * 
	 * Nothing to be removed if there are no menu items in the set package<p>
	 * 
	 * The price of the set package is updated after removing menu item
	 * 
	 * @param packageItem The menu item to be removed from the set package
	 */
	public void removeItemFromPackage(MenuItem packageItem)
	{
		if(getPackageSize() == 0)
			return;
		
		setPrice(getPrice() - (packageItem.getPrice() * ITEM_DISCOUNT));
		_setPackageItems.remove(packageItem);
	}
	
	/**
	 * Checks if this set package contains the specified menu item
	 * 
	 * @param menuItem The menu item to look for in this set package
	 * @return True if this menu item can be found in the set package
	 */
	public boolean findInPackage(MenuItem menuItem)
	{
		if(getPackageSize() == 0)
			return false;
		
		return _setPackageItems.contains(menuItem);
	}
	
	/**
	 * Gets the number of menu items inside this set package
	 * 
	 * @return The number of menu items inside this set package
	 */
	public int getPackageSize() {
		return _setPackageItems.size();
	}
	
	/**
	 * Checks whether this set package is empty
	 * 
	 * @return True if this set package is empty
	 */
	public boolean isEmpty() {
		return _setPackageItems.isEmpty();
	}
	
	/**
	 * Gets the list of all menu items contained in this set package
	 * 
	 * @return The list of all menu items in this set package
	 */
	public List<MenuItem> getPackageItems()	{
		return _setPackageItems;
	}
	
	/**
	 * Recalculates the price of the set package based on each
	 * menu item that it contains<p>
	 * 
	 * This can be used to update the set package price when
	 * a menu item's price has been changed
	 */
	public void recalculatePackagePrice()
	{
		if(getPackageSize() == 0)
			return;
		
		double newPackagePrice = 0;
		for(MenuItem setPackageItem : _setPackageItems)
		{
			newPackagePrice += (setPackageItem.getPrice() * ITEM_DISCOUNT);
		}
		
		setPrice(newPackagePrice);
	}
	
	/**
	 * Display the set package details with proper formatting<p>
	 * Displayed information include the list of all menu items
	 * that this set package contains,<br> as well as the price of the
	 * set package
	 */
	@Override
	public void displayItemDetails()
	{
		String packageName = " \"" + getName() + "\" Set Package ";
		int paddingLength = (50 - packageName.length())/2;
		
		System.out.print(new String(new char[paddingLength]).replace("\0", "-"));
		System.out.print(packageName);
		System.out.println(new String(new char[paddingLength]).replace("\0", "-"));	
		System.out.printf("%-50s%n", "\"" + getDesc() + "\"");
		
		int setPackageSize = getPackageSize();
		
		MenuItem.ItemType currItemType = null;
		
		// Display set package items
		for (int currItem = 0; currItem < setPackageSize; currItem++) {
			if (_setPackageItems.get(currItem).getItemType() != currItemType)
			{
				currItemType = _setPackageItems.get(currItem).getItemType();
				String strCurrItemType = "[" + currItemType.toStrValue() + "]";
				
				paddingLength = (50 - strCurrItemType.length())/2;
				
				System.out.print("\n" + new String(new char[paddingLength]).
						replace("\0", " "));
				System.out.print(strCurrItemType);
				System.out.println(new String(new char[paddingLength]).
						replace("\0", " "));
			}

			_setPackageItems.get(currItem).displayItemDetails();
		}
		
		System.out.printf("%n%50s%n", "Discounted Price: " +
				new DecimalFormat("$###,##0.00").format(getPrice()));
		System.out.println(new String(new char[50]).replace("\0", "-"));
	}
	
	/**
	 * Displays the set package name, description and price<br>
	 * This provides a summary of the set package
	 */
	@Override
	public void displayItemSummary()
	{
		System.out.printf("%-30s", getName());
		System.out.printf("%-30s", "\"" + getDesc() + "\"");
		System.out.printf("%12s%n",
				new DecimalFormat("$###,##0.00").format(getPrice()));
	}
}