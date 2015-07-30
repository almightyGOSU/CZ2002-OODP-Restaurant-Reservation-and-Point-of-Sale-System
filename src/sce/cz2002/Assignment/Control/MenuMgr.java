package sce.cz2002.Assignment.Control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import sce.cz2002.Assignment.Classes.MenuItem;
import sce.cz2002.Assignment.Classes.MenuItem.ItemType;
import sce.cz2002.Assignment.Classes.SetPackage;

/**
 * A singleton class that takes care of all menu-related functionality<p>
 * 
 * Functionalities include saving/loading menu information, creating
 * new menu items,<br> updating/removing existing menu items,
 * creating new set packages, updating/removing existing<br> set packages and
 * also viewing the menu
 * 
 * @author Jin Yao
 *
 */
public class MenuMgr
{
	/**
	 * An enumeration type used to determine the submenu state<br>
	 * Performs the appropriate function based on the submenu state selected
	 * 
	 * @author Jin Yao
	 *
	 */
	private enum MenuSubmenuState
	{
		ViewMenu,
		CreateMenuItem,
		UpdateMenuItem,
		RemoveMenuItem,
		CreateSetPackage,
		UpdateSetPackage,
		RemoveSetPackage
	}
	
	/**
	 * The file path for the file used to store the menu information
	 */
	private static final String MENU_FILE_PATH = "menu.dat";
	
	/**
	 * A static instance of this menu manager
	 */
	private static MenuMgr _menuMgr = null;
	
	/**
	 * A static list of all menu items contained in the menu
	 */
	private static List<MenuItem> _menu;
	
	/**
	 * Standard Java scanner using for processing inputs
	 */
	private static Scanner sc;
	
	/**
	 * Private constructor used to support the Singleton design pattern
	 * <br>
	 * Creates a new Java Scanner object, and creates a new
	 * arraylist of menu items
	 */
	private MenuMgr()
	{
		sc = new Scanner(System.in);
		
		_menu = new ArrayList<MenuItem>();
	}
	
	/**
	 * Public static function used to get hold of the menu manager
	 * 
	 * @return The static instance of the menu manager
	 */
	public static MenuMgr getMenuMgr()
	{
		if(_menuMgr == null)
		{
			_menuMgr = new MenuMgr();
		}
		
		return _menuMgr;
	}
	
	/**
	 * Loads the menu information from the menu file, if it exists
	 * <br>Should be called at the start of the application
	 */
	public void loadMenu()
	{
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try
		{
			fis = new FileInputStream(MENU_FILE_PATH);
			in = new ObjectInputStream(fis);
			
			Object obj = in.readObject();

			if (obj instanceof ArrayList<?>)
			{
				ArrayList<?> al = (ArrayList<?>) obj;

				if (al.size() > 0)
				{
					for (int objIndex = 0; objIndex < al.size(); objIndex++)
					{
						Object childObj = al.get(objIndex);
						
						// Ensure that we are really adding menu items
						// to the menu
						if (childObj instanceof MenuItem)
						{
							_menu.add(((MenuItem) childObj));
						}
					}
				}
			}

			in.close();
			
			if(!_menu.isEmpty())
				System.out.println("'Menu' data loaded successfully!");
			
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to load 'Menu' data!");
		} catch (IOException ex) {
			System.out.println("Unable to load 'Menu' data!");
		} catch (ClassNotFoundException ex) {
			System.out.println("Unable to load 'Menu' data!");
		} catch (Exception ex) {
			System.out.println("Unable to load 'Menu' data!");
		}
	}
	
	/**
	 * Saves the menu information to the menu file<br>
	 * Should be called before exiting the application
	 */
	public void saveMenu() {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;

		try {
			fos = new FileOutputStream(MENU_FILE_PATH);
			out = new ObjectOutputStream(fos);
			out.writeObject(_menu);
			out.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to save 'Menu' data!");
		} catch (IOException ex) {
			System.out.println("Unable to save 'Menu' data!");
		} catch (Exception ex) {
			System.out.println("Unable to save 'Menu' data!");
		}

		System.out.println("Saved 'Menu' data successfully!");
	}
	
	/**
	 * Displays the list of options for this menu submenu
	 */
	private void displayMenuOptions()
	{
		System.out.println("\n" + new String(new char[50]).replace("\0", "="));
		System.out.print("|" + new String(new char[18]).replace("\0", " "));
		System.out.print("Menu Submenu");
		System.out.println(new String(new char[18]).replace("\0", " ") + "|");
		System.out.println(new String(new char[50]).replace("\0", "="));
		
		System.out.println("0. Return to main menu");
		System.out.println("1. View menu");
		System.out.println("2. Create a new menu item");
		System.out.println("3. Update a existing menu item");
		System.out.println("4. Remove a existing menu item");
		System.out.println("5. Create a new set package");
		System.out.println("6. Update a existing set package");
		System.out.println("7. Remove a existing set package");
	}
	
	/**
	 * Prompts the user to choose a menu function<br>
	 * Handles exceptions appropriately and allows the user to retry
	 * 
	 * @return The integer representing the menu function that the user
	 * 		   has chosen
	 */
	public int getMenuChoice()
	{
		displayMenuOptions();
		
		int menuChoice = -1;
		do
		{
			try
			{
				System.out.print("\nPlease enter your choice (0-7): ");
				menuChoice = sc.nextInt();
				sc.nextLine();
			}
			catch(InputMismatchException ex1)
			{
				System.out.println("Invalid input! Please try again..");
				sc.nextLine(); // Clear the garbage input
				continue;
			}
			catch(Exception ex2)
			{
				System.out.println("Invalid input! Please try again..");
				sc.nextLine(); // Clear the garbage input
				continue;
			}
			
			if(menuChoice < 0 || menuChoice > 7)
				System.out.println("Invalid choice! Please try again..");
			
		} while (menuChoice < 0 || menuChoice > 7);
		
		if(menuChoice == 0)
			return menuChoice;	
		else
		{
			switch(MenuSubmenuState.values()[menuChoice - 1])
			{
			case ViewMenu:
				displayMenu();
				break;
			
			case CreateMenuItem:
				addMenuItem();
				break;
				
			case UpdateMenuItem:
				updateMenuItem();
				break;
				
			case RemoveMenuItem:
				removeMenuItem();
				break;
				
			case CreateSetPackage:
				addSetPackage();
				break;
				
			case UpdateSetPackage:
				updateSetPackage();
				break;
				
			case RemoveSetPackage:
				removeSetPackage();
				break;
			}
		}
		
		return menuChoice;
	}
	
	/**
	 * Displays the complete menu, with the information of each menu item
	 * <p>
	 * The menu is sorted by the item type
	 */
	private void displayMenu()
	{
		if(_menu.isEmpty())
		{
			System.out.print("\nWell, the Menu is empty right now!");
			System.out.println(" Try adding new menu items? :-)");
		}
		else
		{
			System.out.println();
			System.out.print(new String(new char[22]).replace("\0", "*"));
			System.out.print(" MENU ");
			System.out.println(new String(new char[22]).replace("\0", "*"));
			
			MenuItem.ItemType currItemType = null;
			
			int menuSize = _menu.size();
			
			// Display menu items
			for(int currItem = 0; currItem < menuSize; currItem++)
			{
				if(_menu.get(currItem).getItemType() != currItemType)
				{
					currItemType = _menu.get(currItem).getItemType();
					displayMenuItemType(currItemType);
				}
				
				_menu.get(currItem).displayItemDetails();
				System.out.println();
			}
			
			System.out.print(new String(new char[22]).replace("\0", "*"));
			System.out.print(" MENU ");
			System.out.println(new String(new char[22]).replace("\0", "*"));
			System.out.println();
		}
	}
	
	/**
	 * Displays the item type of a menu item
	 * 
	 * @param currItemType The item type to be displayed
	 */
	private void displayMenuItemType(MenuItem.ItemType currItemType)
	{
		String strCurrItemType = currItemType.toStrValue();
		int paddingLength = (50 - strCurrItemType.length())/2;
		
		System.out.println(new String(new char[50]).replace("\0", "="));
		System.out.print(new String(new char[paddingLength]).replace("\0", " "));
		System.out.print(strCurrItemType);
		System.out.println(new String(new char[paddingLength]).replace("\0", " "));
		System.out.println(new String(new char[50]).replace("\0", "=") + "\n");
	}
	
	/**
	 * Adds a new menu item to the menu<p>
	 * User must choose from a given list of item types,
	 * provide a item name<br> that does not exist in the menu,
	 * a item description and a valid price<p>
	 * The created menu item will be added to the menu, and the menu
	 * will be sorted according to the item type<p>
	 * Exceptions are handled and an error message is shown
	 * if the process failed
	 */
	private void addMenuItem()
	{	
		MenuItem newMenuItem = null;
		
		try
		{
			System.out.print("\nEnter the item type"
					+ " (1. Main Course 2. Drink 3. Dessert): ");
			int itemTypeValue = sc.nextInt();
			sc.nextLine();
			
			// Valid itemTypeValues from 1 to 3
			if(itemTypeValue < 1 || itemTypeValue > 3)
			{
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to create new menu item,"
						+ " please try again..");
				return;
			}
			
			MenuItem.ItemType itemType =
					MenuItem.ItemType.values()[itemTypeValue - 1];
			
			System.out.print("Enter the name of the item: ");
			String itemName = sc.nextLine();
			
			if(isDuplicateItem(itemName))
			{
				System.out.print("\nSorry, a menu item with the given"
						+ " name exists in the system. No duplicate item"
						+ " name allowed!");
				System.out.println(" Failed to create new menu item,"
						+ " please try again..");
				return;
			}
			
			System.out.print("Enter a description for the item: ");
			String itemDesc = sc.nextLine();
			
			System.out.print("Enter a price for the item: ");
			double itemPrice = sc.nextDouble();
			sc.nextLine();
			
			if(itemPrice < 0)
			{
				System.out.print("\nItem price must be an non-negative"
						+ " integer! ");
				System.out.println("Failed to create new menu item,"
						+ " please try again..");
				return;
			}
			
			newMenuItem = new MenuItem(itemType, itemName,
					itemDesc, itemPrice);
			_menu.add(newMenuItem);
			
			// Keep the menu sorted according to the item type
			Collections.sort(_menu);
		}
		catch(InputMismatchException ex)
		{
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to create new menu item,"
					+ " please try again..");
			
			sc.nextLine(); // Clear the garbage input
			return;
		}
		catch(Exception ex)
		{
			System.out.println("\nFailed to create new menu item,"
					+ " please try again..");
			return;
		}
		
		if(newMenuItem != null)
		{
			System.out.printf("%nSuccessfully added \"%s\" to the menu!%n",
					newMenuItem.getName());
		}
	}
	
	/**
	 * Updates an existing menu item<p>
	 * 
	 * The user can choose from a list of existing menu items,
	 * and update the name, description <br>and price of the menu item
	 * <p>
	 * If the menu item is included in a set package, the set package
	 * will be updated automatically
	 */
	private void updateMenuItem()
	{
		if(_menu.isEmpty())
		{
			System.out.print("\nWell, the Menu is empty right now!");
			System.out.println(" Seems like there is nothing to update? :-)");
		}
		else
		{
			// Index of item to be updated
			int itemIndex;
			
			try
			{
				System.out.printf("%n%5s%-30s", "", "Item Name");
				System.out.printf("%-17s", "Item Type");
				System.out.printf("%15s%n", "Item Price");
				
				int menuSize = _menu.size();
				
				// Actual menu size, excluding set packages
				int actualMenuSize = 0;
				
				// Display menu items
				for(int currItem = 0; currItem < menuSize; currItem++)
				{
					// Do not include set packages as valid option
					if(_menu.get(currItem).getItemType() ==
							MenuItem.ItemType.SetPackage)
						break;
					
					System.out.printf("%-5s", "(" + (currItem + 1) + ")");
					_menu.get(currItem).displayItemSummary();		
					actualMenuSize = currItem + 1;
				}
				
				System.out.printf("%nPlease select an item to update "
						+ "(0 to cancel): ");
			
				itemIndex = sc.nextInt();
				sc.nextLine();
				
				// User chooses not to update any menu item
				if(itemIndex == 0)
				{
					System.out.println("\nNothing to be updated!");
					return;
				}
				
				// Valid itemTypeValues from 1 to actualMenuSize
				if (itemIndex < 1 || itemIndex > actualMenuSize) {
					System.out.print("\nInvalid input! ");
					System.out.println("Failed to update menu item,"
							+ " please try again..");
					return;
				}
				
				MenuItem updatingItem = _menu.get(itemIndex - 1);
				updateItemInfo(updatingItem);
				
				System.out.printf("%nSuccessfully updated \"%s\"!%n",
						updatingItem.getName());
				
				// Update Set Packages (if any)
				for(int currItem = 0; currItem < menuSize; currItem++)
				{
					MenuItem menuItem = _menu.get(currItem);
					
					// If this item is a set package
					if(menuItem.getItemType() == MenuItem.ItemType.SetPackage)
					{
						// Explicit downcasting - menuItem is of SetPackage type
						// Makes the set package recalculate its price
						((SetPackage) menuItem).recalculatePackagePrice();
					}
				}	
			}
			catch(InputMismatchException ex)
			{
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to update menu item,"
						+ " please try again..");
				
				sc.nextLine(); // Clear the garbage input
				return;
			}
			catch(Exception ex)
			{
				System.out.println("\nFailed to update menu item,"
						+ " please try again..");
				return;
			}
		}
	}
	
	/**
	 * Removes an existing menu item<p>
	 * The user can choose to remove an menu item from the list of existing
	 * menu items<p>
	 * If the menu item is included in any set package, the set package will
	 * be updated<br>
	 * However, if the set package becomes <u>empty</u> after removing the
	 *  menu item,<br>the set package will be deleted
	 */
	private void removeMenuItem()
	{
		if(_menu.isEmpty())
		{
			System.out.print("\nWell, the Menu is empty right now!");
			System.out.println(" Seems like there is nothing to remove? :-)");
		}
		else
		{
			// Index of item to be removed
			int itemIndex;
			
			// Name of removed item, if any
			String removedItemName = "";
			
			try
			{
				System.out.printf("%n%5s%-30s", "", "Item Name");
				System.out.printf("%-17s", "Item Type");
				System.out.printf("%15s%n", "Item Price");
				
				int menuSize = _menu.size();
				
				// Actual menu size, excludes set packages
				int actualMenuSize = 0;
				
				// Display menu items
				for(int currItem = 0; currItem < menuSize; currItem++)
				{
					// Do not include set packages as valid option
					if(_menu.get(currItem).getItemType() ==
							MenuItem.ItemType.SetPackage)
						break;
					
					System.out.printf("%-5s", "(" + (currItem + 1) + ")");
					_menu.get(currItem).displayItemSummary();
					actualMenuSize = currItem + 1;
				}
				
				System.out.printf("%nPlease select an item to remove "
						+ "(0 to cancel): ");
			
				itemIndex = sc.nextInt();
				
				// User chooses not to remove any menu item
				if(itemIndex == 0)
				{
					System.out.println("\nNothing to be removed!");
					return;
				}
				
				// Valid itemTypeValues from 1 to actualMenuSize
				if (itemIndex < 1 || itemIndex > actualMenuSize) {
					System.out.print("\nInvalid input! ");
					System.out.println("Failed to remove menu item,"
							+ " please try again..");
					return;
				}
				
				// Menu item to be removed
				MenuItem removedItem = _menu.get(itemIndex - 1);
				
				Iterator<MenuItem> menuIter = _menu.iterator();
				MenuItem menuItem = null;
				
				// Update Set Packages (if any)
				while(menuIter.hasNext()) {
					
					menuItem = menuIter.next();
					
					// If this item is a set package
					if(menuItem.getItemType() == MenuItem.ItemType.SetPackage)
					{
						// Explicit downcasting - menuItem is of SetPackage type
						if(((SetPackage) menuItem).findInPackage(removedItem))
						{
							// Remove it from the set package
							((SetPackage) menuItem).removeItemFromPackage(
									removedItem);
							
							// Makes the set package recalculate its price
							((SetPackage) menuItem).recalculatePackagePrice();
							
							if(((SetPackage) menuItem).getPackageSize() == 0)
							{
								// Cannot have a set package with no items
								// Add to list of set packages pending removal
								menuIter.remove();
							}
						}
					}
				}
				
				// Remove it from the menu
				removedItemName = removedItem.getName();
				_menu.remove(removedItem);
				
				System.out.printf("%nSuccessfully removed \"%s\" from the menu!%n",
						removedItemName);
			}
			catch(InputMismatchException ex1)
			{
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to remove menu item,"
						+ " please try again..");
				
				sc.nextLine(); // Clear the garbage input
				return;
			}
			catch(Exception ex2)
			{
				System.out.println("\nFailed to remove menu item,"
						+ " please try again..");
				return;
			}
		}
	}
	
	/**
	 * Creates a new set package<p>
	 * 
	 * Allows the user to choose from a list of menu items to add to
	 * the set package<p>
	 * 
	 * The set package will not be created if there are no menu items in it
	 */
	private void addSetPackage()
	{
		if(_menu.isEmpty())
		{
			System.out.print("\nWell, the Menu is empty right now!");
			System.out.println(" Create new menu items to add them"
					+ " to a set package!");
		}
		else
		{
			SetPackage newSetPackage = null;
			
			try
			{
				System.out.print("Enter a name for the set package: ");
				String newPackageName = sc.nextLine();
				
				if(isDuplicateItem(newPackageName))
				{
					System.out.print("\nSorry, a set package with the given"
							+ " name exists in the system. No duplicate item"
							+ " name allowed!");
					System.out.println(" Failed to create set package,"
							+ " please try again..");
					return;
				}
				
				System.out.print("Enter a description for"
						+ " the set package: ");
				String newPackageDesc = sc.nextLine();
				
				newSetPackage = new SetPackage(MenuItem.ItemType.SetPackage,
						newPackageName, newPackageDesc, 0);
			}
			catch(InputMismatchException ex1)
			{
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to create set package,"
						+ " please try again..");
				
				sc.nextLine(); // Clear the garbage input
				return;
			}
			catch(Exception ex2)
			{
				System.out.println("\nFailed to create set package,"
						+ " please try again..");
				return;
			}
			
			int itemIndex = 0;
			
			do
			{
				try
				{
					System.out.printf("%n%5s%-30s", "", "Item Name");
					System.out.printf("%-17s", "Item Type");
					System.out.printf("%15s%n", "Item Price");
					
					int menuSize = _menu.size();
					
					// Actual menu size, excludes set packages
					int actualMenuSize = 0;
					
					// Display menu items
					for(int currItem = 0; currItem < menuSize; currItem++)
					{
						// Do not include set packages as valid option
						if(_menu.get(currItem).getItemType() ==
								MenuItem.ItemType.SetPackage)
							break;
						
						System.out.printf("%-5s", "(" + (currItem + 1) + ")");
						_menu.get(currItem).displayItemSummary();
						actualMenuSize = currItem + 1;
					}
					
					System.out.printf("%nSelect item to be added "
							+ "to the set package (0 to end): ");
				
					itemIndex = sc.nextInt();
					sc.nextLine();
					
					// User decides to stop adding items to set package
					if(itemIndex == 0)
						continue;
					
					// Valid itemTypeValues from 1 to actualMenuSize
					if (itemIndex < 1 || itemIndex > actualMenuSize) {
						System.out.print("\nInvalid input! ");
						System.out.println("Failed to add menu item,"
								+ " please try again..");
						continue;
					}
					
					MenuItem selectedItem = _menu.get(itemIndex - 1);
					newSetPackage.addItemToPackage(selectedItem);
					
					System.out.printf("\"%s\" has been added to the "
							+ "set package!%n", selectedItem.getName());
				}
				catch(InputMismatchException ex1)
				{
					System.out.print("\nInvalid input! ");
					System.out.println("Failed to add menu item,"
							+ " please try again..");
					
					sc.nextLine(); // Clear the garbage input
				}
				catch(Exception ex2)
				{
					System.out.println("\nFailed to add menu item,"
							+ " please try again..");
				}
				
			} while(itemIndex != 0);
			
			if(newSetPackage.getPackageSize() == 0)
			{
				System.out.println("\nHow can there be a set package"
						+ " with nothing inside? Failed to create set"
						+ " package, please try again..");
			}
			else
			{
				System.out.printf("\nSuccessfully created new \"%s\" "
						+ "set package!%n%n", newSetPackage.getName());
				newSetPackage.displayItemDetails();
				
				// Add the newly created set package to the menu
				_menu.add(newSetPackage);
				
				// Keep the menu sorted according to the item type
				Collections.sort(_menu);
			}
		}
	}
	
	/**
	 * Allows the user to update the set package<p>
	 * 
	 * The user can update the information for the set package, add more
	 * menu<br> items to the set package or remove existing menu items from
	 * the set package
	 */
	private void updateSetPackage()
	{
		if(_menu.isEmpty())
		{
			System.out.print("\nWell, the Menu is empty right now!");
			System.out.println(" No set package to update..");
		}
		else
		{
			int startingSetIndex = -1;
			int currSet = 0, totalSets = 0;
			
			int menuSize = _menu.size();
			
			// Display set packages (if any)
			for(int currItem = 0; currItem < menuSize; currItem++)
			{
				MenuItem menuItem = _menu.get(currItem);
				
				// Only include set packages as valid options
				if(menuItem.getItemType() == MenuItem.ItemType.SetPackage)
				{
					if(startingSetIndex == -1)
					{
						startingSetIndex = currItem;
						currSet = 0;
						totalSets = 1;
						
						System.out.printf("%n%5s%-30s", "",
								"Set Package Name");
						System.out.printf("%-30s",
								"Set Package Description");
						System.out.printf("%12s%n", "Item Price");
					}
					else
					{
						currSet++;
						totalSets++;
					}
					
					System.out.printf("%-5s", "(" + (currSet + 1) + ")");
					((SetPackage) menuItem).displayItemSummary();
				}
			}
			
			if(totalSets == 0)
			{
				System.out.println("\nWell, there are no set packages in the"
						+ " menu right now! Try creating one? :-)");
			}
			else
			{
				// Index of set package to be updated
				int itemIndex;
				
				// Name of updated set package, if any
				String updatedPackageName = "";
				
				try
				{
					System.out.printf("%nPlease select a set package to "
							+ "update (0 to cancel): ");
				
					itemIndex = sc.nextInt();
					sc.nextLine();
					
					// User chooses not to update any set package
					if(itemIndex == 0)
					{
						System.out.println("\nNothing to be updated!");
						return;
					}
					
					// Valid values from 1 to totalSets
					if (itemIndex < 1 || itemIndex > totalSets) {
						System.out.print("\nInvalid input! ");
						System.out.println("Failed to update set package,"
								+ " please try again..");
						return;
					}
					
					// Calculate actual index
					itemIndex = (startingSetIndex + itemIndex - 1);
					
					MenuItem updatingPackage = _menu.get(itemIndex);
					updatedPackageName = updatingPackage.getName();
					
					int updateOption = -1;
					do
					{
						System.out.println();
						System.out.println("(1) Update item information");
						System.out.println("(2) Add menu item to set package");
						System.out.println("(3) Remove menu item from"
								+ " set package");
						
						System.out.printf("%nPlease select the action to be"
								+ " performed (0 to cancel): ");
					
						updateOption = sc.nextInt();
						sc.nextLine();
						
						// User chooses not to update any set package
						if(updateOption == 0)
						{
							System.out.println("\nNothing to be updated!");
							return;
						}
						
						// Valid values from 1 to 3
						if (updateOption < 1 || updateOption > 3) {
							System.out.print("\nInvalid input! ");
							System.out.println("Failed to update set package,"
									+ " please try again..");
							return;
						}
						
						if(updateOption == 1)
						{
							updateItemInfo(updatingPackage);
							System.out.printf("%nSuccessfully updated \"%s\"!"
									+ "%n", updatedPackageName);
						}
						else if(updateOption == 2)
						{
							addItemToPackage(updatingPackage);
							System.out.printf("%nSuccessfully updated \"%s\"!"
									+ "%n", updatedPackageName);
						}
						else
						{
							removeItemFromPackage(updatingPackage);
							
							if(((SetPackage) updatingPackage).isEmpty())
							{
								System.out.print("\nEmpty set package!");
								System.out.printf("\"%s\" has been " +
										"deleted..%n", updatedPackageName);
								_menu.remove(updatingPackage);
								
								return;
							}
							else
							{
								System.out.printf("%nSuccessfully updated "
										+ "\"%s\"!%n", updatedPackageName);
							}
						}
						
					} while(updateOption != 0);
				}
				catch(InputMismatchException ex1)
				{
					System.out.print("\nInvalid input! ");
					System.out.println("Failed to update set package,"
							+ " please try again..");
					
					sc.nextLine(); // Clear the garbage input
					return;
				}
				catch(Exception ex2)
				{
					System.out.println("\nFailed to update set package,"
							+ " please try again..");
					return;
				}
			}
		}
	}
	
	/**
	 * Allows the user to remove the set package from the menu
	 */
	private void removeSetPackage()
	{
		if(_menu.isEmpty())
		{
			System.out.print("\nWell, the Menu is empty right now!");
			System.out.println(" No set package to remove..");
		}
		else
		{
			int startingSetIndex = -1;
			int currSet = 0, totalSets = 0;
			
			int menuSize = _menu.size();
			
			// Display set packages (if any)
			for(int currItem = 0; currItem < menuSize; currItem++)
			{
				MenuItem menuItem = _menu.get(currItem);
				
				// Only include set packages as valid options
				if(menuItem.getItemType() == MenuItem.ItemType.SetPackage)
				{
					if(startingSetIndex == -1)
					{
						startingSetIndex = currItem;
						currSet = 0;
						totalSets = 1;
						
						System.out.printf("%n%5s%-30s", "",
								"Set Package Name");
						System.out.printf("%-30s",
								"Set Package Description");
						System.out.printf("%12s%n", "Item Price");
					}
					else
					{
						currSet++;
						totalSets++;
					}
					
					System.out.printf("%-5s", "(" + (currSet + 1) + ")");
					((SetPackage) menuItem).displayItemSummary();
				}
			}
			
			if(totalSets == 0)
			{
				System.out.println("\nWell, there are no set packages in the"
						+ " menu right now! Try creating one? :-)");
			}
			else
			{
				// Index of set package to be removed
				int itemIndex;
				
				// Name of removed set package, if any
				String removedItemName = "";
				
				try
				{	
					System.out.printf("%nPlease select a set package to "
							+ "remove (0 to cancel): ");
				
					itemIndex = sc.nextInt();
					sc.nextLine();
					
					// User chooses not to remove any set package
					if(itemIndex == 0)
					{
						System.out.println("\nNothing to be removed!");
						return;
					}
					
					// Valid values from 1 to totalSets
					if (itemIndex < 1 || itemIndex > totalSets) {
						System.out.print("\nInvalid input! ");
						System.out.println("Failed to remove set package,"
								+ " please try again..");
						return;
					}
					
					// Calculate actual index
					itemIndex = (startingSetIndex + itemIndex - 1);
					
					// Remove it from the menu
					removedItemName = _menu.get(itemIndex).getName();
					_menu.remove(itemIndex);
					
					System.out.printf("%nSuccessfully removed \"%s\" from"
							+ " the menu!%n", removedItemName);
				}
				catch(InputMismatchException ex1)
				{
					System.out.print("\nInvalid input! ");
					System.out.println("Failed to remove set package,"
							+ " please try again..");
					
					sc.nextLine(); // Clear the garbage input
					return;
				}
				catch(Exception ex2)
				{
					System.out.println("\nFailed to remove set package,"
							+ " please try again..");
					return;
				}
			}
		}
	}
	
	/**
	 * Updates the information for a particular menu item
	 * 
	 * @param updatingItem The menu item to be updated
	 *  
	 * @throws InputMismatchException Exception thrown for input mismatch
	 * @throws Exception General exception thrown
	 */
	private void updateItemInfo(MenuItem updatingItem)
			throws InputMismatchException, Exception
	{
		System.out.printf("%-50s", "Current Name: \"" + 
				updatingItem.getName() + "\"");
		System.out.print("\tEnter a new name: ");
		String newItemName = sc.nextLine();
		
		System.out.printf("%-50s", "Current description: \"" + 
				updatingItem.getDesc() + "\"");
		System.out.print("\tEnter a new description: ");
		String newItemDesc = sc.nextLine();
		
		double newItemPrice = 0;
		if(! (updatingItem instanceof SetPackage))
		{
			System.out.printf("%-50s", "Current price: \"" + 
					new DecimalFormat("$###,##0.00").format(
							updatingItem.getPrice()) + "\"");
			System.out.print("\tEnter a new price: ");
			newItemPrice = sc.nextDouble();
			sc.nextLine();
		}
		
		updatingItem.setName(newItemName);
		updatingItem.setDesc(newItemDesc);
		
		// Do not allow user to set package price
		if(! (updatingItem instanceof SetPackage))
		{
			updatingItem.setPrice(newItemPrice);
		}
	}
	
	/**
	 * Adds a menu item to the set package
	 * 
	 * @param updatingSetPackage The set package that the menu item is added to
	 * 
	 * @throws InputMismatchException Exception thrown for input mismatch
	 * @throws Exception General exception thrown
	 */
	private void addItemToPackage(MenuItem updatingSetPackage)
			throws InputMismatchException, Exception
	{
		System.out.printf("%n%5s%-30s", "", "Item Name");
		System.out.printf("%-17s", "Item Type");
		System.out.printf("%15s%n", "Item Price");
		
		int menuSize = _menu.size();
		
		// Actual menu size, excludes set packages
		int actualMenuSize = 0;
		
		// Display menu items
		for(int currItem = 0; currItem < menuSize; currItem++)
		{
			// Do not include set packages as valid option
			if(_menu.get(currItem).getItemType() ==
					MenuItem.ItemType.SetPackage)
				break;
			
			System.out.printf("%-5s", "(" + (currItem + 1) + ")");
			_menu.get(currItem).displayItemSummary();
			actualMenuSize = currItem + 1;
		}
		
		System.out.printf("%nSelect item to be added "
				+ "to the set package (0 to cancel): ");
	
		int itemIndex = sc.nextInt();
		sc.nextLine();
		
		// User decides not to add any item to set package
		if(itemIndex == 0)
			return;
		
		// Valid itemTypeValues from 1 to actualMenuSize
		if (itemIndex < 1 || itemIndex > actualMenuSize) {
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to add menu item,"
					+ " please try again..");
			return;
		}
		
		MenuItem selectedItem = _menu.get(itemIndex - 1);
		((SetPackage) updatingSetPackage).addItemToPackage(selectedItem);
		
		System.out.printf("\"%s\" has been added to the "
				+ "set package!%n", selectedItem.getName());
	}
	
	/**
	 * Removes a menu item from the set package
	 * 
	 * @param updatingSetPackage The set package to remove the menu item from
	 * 
	 * @throws InputMismatchException Exception thrown for input mismatch
	 * @throws Exception General exception thrown
	 */
	private void removeItemFromPackage(MenuItem updatingSetPackage)
			throws InputMismatchException, Exception
	{
		System.out.printf("%n%5s%-30s", "", "Item Name");
		System.out.printf("%-17s", "Item Type");
		System.out.printf("%15s%n", "Item Price");
		
		List<MenuItem> packageItems =
				((SetPackage) updatingSetPackage).getPackageItems();
		
		int packageSize = packageItems.size();
		
		// Display package items
		for(int currItem = 0; currItem < packageSize; currItem++)
		{
			System.out.printf("%-5s", "(" + (currItem + 1) + ")");
			packageItems.get(currItem).displayItemSummary();
		}
		
		// Only one item in set package
		// Set package will be deleted if it is removed
		if(packageSize == 1)
		{
			System.out.println("\nNOTE: Set package will be deleted"
					+ " when there are no items left in it!");
		}
		
		System.out.printf("%nSelect item to be removed "
				+ "from the set package (0 to cancel): ");
	
		int itemIndex = sc.nextInt();
		sc.nextLine();
		
		// User decides not to remove any item from set package
		if(itemIndex == 0)
			return;
		
		// Valid itemTypeValues from 1 to packageSize
		if (itemIndex < 1 || itemIndex > packageSize) {
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to remove menu item,"
					+ " please try again..");
			return;
		}
		
		MenuItem selectedItem = packageItems.get(itemIndex - 1);
		((SetPackage) updatingSetPackage).removeItemFromPackage(selectedItem);
		
		System.out.printf("\"%s\" has been removed from the "
				+ "set package!%n", selectedItem.getName());
	}
	
	/**
	 * Checks whether a given menu item name already exists in the menu
	 * 
	 * @param menuItemName The name of the new menu item to be created
	 * 
	 * @return True if it is a duplicate menu item name
	 */
	private boolean isDuplicateItem(String menuItemName)
	{
		if(_menu.isEmpty())
			return false;
		
		for(MenuItem menuItem : _menu)
		{
			if(menuItem.getName().equalsIgnoreCase(menuItemName))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets a selected menu item based on a list of menu items<br>
	 * Displays a list of existing menu items to allow the user to choose
	 * from
	 * 
	 * @return The selected menu item
	 */
	public MenuItem selectMenuItem()
	{
		if(_menu.isEmpty())
		{
			System.out.print("\nWell, the Menu is empty right now!");
			System.out.println(" Try adding new menu items? :-)");
			
			return null;
		}
		else
		{
			int menuChoice = 0;
			int menuSize = _menu.size();
			
			do
			{
				int currItemIndex = 0;
				
				try
				{
					System.out.println();
					
					// Display menu items
					for(MenuItem menuItem : _menu)
					{
						if(menuItem.getItemType() == ItemType.SetPackage)
							System.out.println("\n[Set Package]");
						
						System.out.printf("%-4s ", "(" + (++currItemIndex) + ")");
						menuItem.displayItemSummary();
					}

					System.out.printf("%nPlease select a menu item"
							+ " (0 to cancel): ");

					menuChoice = sc.nextInt();
					sc.nextLine();
					
					// User is done with selecting menu items
					if(menuChoice == 0)
						return null;

					// Valid menuChoice from 1 to menuSize
					if (menuChoice < 1 || menuChoice > menuSize)
					{
						System.out.print("\nInvalid input! ");
						System.out.println("Failed to select menu item,"
								+ " please try again..");
						continue;
					}
				}
				catch(InputMismatchException ex)
				{
					System.out.print("\nInvalid input! ");
					System.out.println("Failed to select menu item,"
							+ " please try again..");

					sc.nextLine(); // Clear the garbage input
				}
				catch(Exception ex)
				{
					System.out.println("Failed to select menu item,"
							+ " please try again..");

					sc.nextLine(); // Clear the garbage input
				}

			} while(menuChoice < 1 || menuChoice > menuSize);

			return _menu.get(menuChoice - 1);
		}
	}
}