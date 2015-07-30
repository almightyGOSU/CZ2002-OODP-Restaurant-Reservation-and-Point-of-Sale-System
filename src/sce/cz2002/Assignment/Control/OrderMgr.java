package sce.cz2002.Assignment.Control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import sce.cz2002.Assignment.Classes.Customer;
import sce.cz2002.Assignment.Classes.MenuItem;
import sce.cz2002.Assignment.Classes.Order;
import sce.cz2002.Assignment.Classes.OrderItem;
import sce.cz2002.Assignment.Classes.Staff;
import sce.cz2002.Assignment.Classes.Table;

/**
 * A singleton class that takes care of all order-related functionality<p>
 * 
 * Functionalities include saving/loading past transaction information, creating
 * new orders,<br> adding/removing menu items from the order and
 * making payment for the order<p>
 * 
 * The order manager also takes care of displaying past sale revenue reports, which can
 * <br>be broken down by day/month
 * 
 * @author Jin Yao
 *
 */
public class OrderMgr
{
	/**
	 * An enumeration type used to determine the submenu state<br>
	 * Performs the appropriate function based on the submenu state selected
	 * 
	 * @author Jin Yao
	 *
	 */
	private enum OrderSubmenuState
	{
		ViewCurrentOrders,
		CreateOrder,
		AddItemToOrder,
		RemoveItemFromOrder,
		MakePaymentForOrder
	}
	
	/**
	 * The file path indicating where the file used to store order information
	 * is saved
	 */
	private static final String ORDER_FILE_PATH = "order.dat";
	
	/**
	 * A static instance of this order manager
	 */
	private static OrderMgr _orderMgr = null;
	
	/**
	 * A static list of ongoing orders (Not yet paid for)
	 */
	private static List<Order> _currentOrders;
	
	/**
	 * A static list of completed transactions (Orders that have been paid for)
	 */
	private static List<Order> _completedOrders;
	
	/**
	 * Standard Java Scanner used for processing user inputs
	 */
	private static Scanner sc;
	
	/**
	 * Private constructor used to support the Singleton design pattern
	 * <br>
	 * Creates a new Java Scanner object, and creates a new
	 * arraylist of current orders as well a <br>new arraylist
	 * of completed orders
	 */
	private OrderMgr()
	{
		sc = new Scanner(System.in);
		
		_currentOrders = new ArrayList<Order>();
		_completedOrders = new ArrayList<Order>();
	}
	
	/**
	 * Public static function used to get hold of the Order manager
	 * 
	 * @return The static instance of the Order manager
	 */
	public static OrderMgr getOrderMgr()
	{
		if(_orderMgr == null)
		{
			_orderMgr = new OrderMgr();
		}
		
		return _orderMgr;
	}
	
	/**
	 * Loads the order-related information from the order file, if it exists
	 * <br>Should be called at the start of the application
	 */
	public void loadOrders()
	{
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try
		{
			fis = new FileInputStream(ORDER_FILE_PATH);
			in = new ObjectInputStream(fis);
			
			Object obj = in.readObject();

			if (obj instanceof ArrayList<?>) {
				ArrayList<?> al = (ArrayList<?>) obj;

				if (al.size() > 0) {
					for (int objIndex = 0; objIndex < al.size(); objIndex++)
					{
						Object childObj = al.get(objIndex);

						if (childObj instanceof Order) {
							_completedOrders.add(((Order) childObj));
						}
					}
				}
			}

			in.close();
			
			if(!_completedOrders.isEmpty())
				System.out.println("'Transactions' data loaded successfully!");
			
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to load 'Transactions' data!");
		} catch (IOException ex) {
			System.out.println("Unable to load 'Transactions' data!");
		} catch (ClassNotFoundException ex) {
			System.out.println("Unable to load 'Transactions' data!");
		} catch (Exception ex) {
			System.out.println("Unable to load 'Transactions' data!");
		}
	}
	
	/**
	 * Saves the all order-related information to the order file<br>
	 * Should be called before exiting the application
	 */
	public void saveOrders()
	{
		FileOutputStream fos = null;
		ObjectOutputStream out = null;

		try {
			fos = new FileOutputStream(ORDER_FILE_PATH);
			out = new ObjectOutputStream(fos);
			
			out.writeObject(_completedOrders);
			
			out.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to save 'Transactions' data!");
		} catch (IOException ex) {
			System.out.println("Unable to save 'Transactions' data!");
		} catch (Exception ex) {
			System.out.println("Unable to save 'Transactions' data!");
		}
		
		System.out.println("Saved 'Transactions' data successfully!");
	}
	
	/**
	 * Displays the list of options for this Order submenu
	 */
	private void displayOrderSubmenuOptions()
	{
		System.out.println("\n" + new String(new char[50]).replace("\0", "="));
		System.out.print("|" + new String(new char[18]).replace("\0", " "));
		System.out.print("Order Submenu");
		System.out.println(new String(new char[17]).replace("\0", " ") + "|");
		System.out.println(new String(new char[50]).replace("\0", "="));
		
		System.out.println("0. Return to main menu");
		System.out.println("1. View current orders");
		System.out.println("2. Create order");
		System.out.println("3. Add item to existing order");
		System.out.println("4. Remove item from existing order");
		System.out.println("5. Make payment for existing order");
	}
	
	/**
	 * Prompts the user to choose a order function<br>
	 * Handles exceptions appropriately and allows the user to retry
	 * 
	 * @return The integer representing the order function that the user
	 * 		   has chosen
	 */
	public int getOrderChoice()
	{
		displayOrderSubmenuOptions();
		
		int maxOrderChoices = OrderSubmenuState.values().length;
		int orderChoice = -1;
		do
		{
			try
			{
				System.out.printf("%nPlease enter your choice (0-%d): ",
						maxOrderChoices);
				orderChoice = sc.nextInt();
				sc.nextLine();
			}
			catch(InputMismatchException ex)
			{
				System.out.println("Invalid input! Please try again..");
				sc.nextLine(); // Clear the garbage input
				continue;
			}
			catch(Exception ex)
			{
				System.out.println("Invalid input! Please try again..");
				sc.nextLine(); // Clear the garbage input
				continue;
			}
			
			if(orderChoice < 0 || orderChoice > maxOrderChoices)
				System.out.println("Invalid choice! Please try again..");
			
		} while (orderChoice < 0 || orderChoice > maxOrderChoices);
		
		if(orderChoice == 0)
			return orderChoice; // Go back to main menu
		else
		{
			switch(OrderSubmenuState.values()[orderChoice - 1])
			{
			case ViewCurrentOrders:
				viewCurrentOrders();
				break;
			
			case CreateOrder:
				createOrder();
				break;
				
			case AddItemToOrder:
				addItemToOrder();
				break;
				
			case RemoveItemFromOrder:
				removeItemFromOrder();
				break;
				
			case MakePaymentForOrder:
				makePaymentForOrder();
				break;
			}
		}
		
		return orderChoice;
	}
	
	/**
	 * Displays the ongoing orders (i.e. orders that have not been
	 * paid for)
	 */
	private void viewCurrentOrders()
	{
		if(_currentOrders.isEmpty())
		{
			System.out.print("\nWell, there are no orders"
					+ " taken at the moment!");
			System.out.println(" Try taking a new order? :-)");
			
			return;
		}
		
		System.out.print("\n" + new String(new char[40]).replace("\0", "*"));
		System.out.print(" Orders ");
		System.out.println(new String(new char[39]).replace("\0", "*"));
		
		int currOrderNo = 1;
		
		for(Order order : _currentOrders)
		{
			System.out.printf("%n%-5s", "(" + (currOrderNo++) + ")");
			order.displayOrderDetails();
		}
		
		System.out.print("\n" + new String(new char[40]).replace("\0", "*"));
		System.out.print(" Orders ");
		System.out.println(new String(new char[39]).replace("\0", "*"));
		
	}
	
	/**
	 * Creates a new order<p>
	 * 
	 * Steps taken to create a new order include selecting a waiter to take the order,
	 * <br>selecting a customer and checking if there is an existing reservation for this
	 * customer at<br>this point of time.<p>
	 * 
	 * If there is a reserved table, the customer will be assigned the reserved table.
	 * <br>Else, the system will ask for the number of people and attempt to allocate
	 * any available table that<br>accomodate the indicated number of people.<p>
	 * 
	 * If there are no available tables at the moment, an error message will be shown.
	 * <br>Once the customer has been assigned a table, the system will allow the user
	 * <br>to select from a list of menu items for the order. The order will be submitted
	 * <br>only if there are 1 or more item in the order.
	 */
	private void createOrder()
	{	
		try
		{
			Staff selectedWaiter = HumanResourceMgr.getHRMgr().selectWaiter();
			if(selectedWaiter == null)
			{
				System.out.println("\nSorry, the restaurant does not have any"
						+ " waiter, try hiring one? :-)");
				return;
			}
			
			Customer selectedCustomer = HumanResourceMgr.getHRMgr().selectCustomer();
			if(selectedCustomer == null)
			{
				System.out.println("\nSorry, the restaurant does not have any"
						+ " registered customer, try getting one? :-)");
				return;
			}
			
			Table table;
			
			System.out.print("\nChecking for reservations..");
			table = RestaurantMgr.getRestaurantMgr().getReservedTable(
					selectedCustomer.getCustomerID());
			
			int numOfPeople = 0;
			if(table == null)
			{
				System.out.println(" No reserved table found for this customer..");
				
				System.out.print("\nWe'll try to look for an empty table for you!");
				System.out.print(" Enter number of people (1-10): ");
				
				numOfPeople = sc.nextInt();
				sc.nextLine();
				
				if(numOfPeople < 1 || numOfPeople > 10)
				{
					System.out.println("\nInvalid input, only 1-10 people allowed!");
					System.out.println(" Failed to create order,"
							+ " please try again..");
					return;
				}
				
				table = RestaurantMgr.getRestaurantMgr().getAvailableTable(
						selectedCustomer.getCustomerID(), numOfPeople);
				
				if(table == null)
				{
					System.out.println("\nUnable to find an empty table"
							+ " right now! Failed to create order,"
							+ " please try again later..");
					return;
				}
				else
				{
					System.out.printf("\nFound an empty table.. Please go"
							+ " to Table '%d'!%n", table.getTableNumber());
				}
			}
			else
			{
				System.out.printf(" Found an reserved table.."
						+ " Please go to Table '%d'!%n", table.getTableNumber());
				
				numOfPeople = RestaurantMgr.getRestaurantMgr().
						getPaxFromReservation(selectedCustomer.getCustomerID());
			}
			
			Calendar currentInstant = GregorianCalendar.getInstance();
        	Date currentDateTime = currentInstant.getTime();
        	currentInstant.setTime(currentDateTime);
        	
        	Timestamp timestamp = new Timestamp(currentDateTime.getTime());
        	String timestampStr = timestamp.toString();
        	timestampStr = timestampStr.replaceAll("[^0-9]", "");
        	
        	String orderID = timestampStr + table.getTableNumber();
        	
        	Order newOrder = new Order(selectedWaiter.getStaffID(),
        			selectedCustomer.getCustomerID(), table.getTableNumber(),
        			numOfPeople, 0, 0, currentInstant, orderID);
        	
        	System.out.println("\nWhat would you like to have?");
        	
        	MenuItem selectedItem = null;
        	do
        	{
        		selectedItem = MenuMgr.getMenuMgr().selectMenuItem();
        		if(selectedItem != null)
        		{
        			System.out.print("Enter the quantity for this item: ");
        			int itemQuantity = sc.nextInt();
        			
        			if(itemQuantity < 1)
        			{
        				System.out.println("\nInvalid input!"
        						+ " Minimum quantity is 1..");
        				continue;
        			}
        			
        			newOrder.addItemToOrder(selectedItem, itemQuantity);
        		}
        		
        	} while(selectedItem != null);
        	
        	if(newOrder.getNumberOfOrderItems() == 0)
        	{
        		System.out.print("\nCannot create an order with 0 items!");
    			System.out.println(" Failed to create order,"
    					+ " please try again..");
    			return;
        	}
        	else
        	{
        		_currentOrders.add(newOrder);    		
        		System.out.println("\nSucessfully created new order!!");
        	}
		}
		catch(InputMismatchException ex)
		{
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to create order,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
		catch(Exception ex)
		{
			System.out.println("Failed to create order,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
	}

	/**
	 * Adds an menu item to the selected order<p>
	 * User can choose from a list of menu items to add to the order
	 */
	private void addItemToOrder() 
	{
		if(_currentOrders.isEmpty())
		{
			System.out.print("\nWell, there are no orders"
					+ " taken at the moment!");
			System.out.println(" Try taking a new order? :-)");
			
			return;
		}
		
		try
		{
        	int orderIndex = 0;
        	int maxOrders = _currentOrders.size();
        	
        	System.out.println();
        	System.out.printf("%5s%-25s", "", "Order ID");
    		System.out.printf("%-15s", "Staff ID");
    		System.out.printf("%-15s", "Customer ID");
    		System.out.printf("%-15s%n", "Table Number");
    		
        	for(Order order : _currentOrders)
        	{
        		System.out.printf("%-5s", "(" + (++orderIndex) + ")");
        		order.displayOrderSummary();
        	}
        	
        	System.out.print("\nPlease select the order to add"
        			+ " to (0 to cancel): ");
        	int selectedOrderIndex = sc.nextInt();
        	
        	if(selectedOrderIndex == 0)
        	{
        		System.out.println("\nNothing to be updated!");
				return;
        	}
        	
        	// Valid values from 1 to maxOrders
			if (selectedOrderIndex < 1 || selectedOrderIndex > maxOrders)
			{
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to update order,"
						+ " please try again..");
				return;
			}
			
			Order updatingOrder = _currentOrders.get(selectedOrderIndex - 1);			
			System.out.println("\nWhat would you like to add to the order?");
			
        	MenuItem selectedItem = null;
        	selectedItem = MenuMgr.getMenuMgr().selectMenuItem();
        	if(selectedItem != null)
        	{
        		System.out.print("Enter the quantity for this item: ");
        		int itemQuantity = sc.nextInt();

        		if(itemQuantity < 1)
        		{
        			System.out.println("\nInvalid input!"
        					+ " Minimum quantity is 1..");
        		}
        		else
        		{
        			updatingOrder.addItemToOrder(selectedItem, itemQuantity);
        			System.out.printf("%nSuccessfully added \"%dx"
        					+ " %s\" to the order!%n", itemQuantity,
        					selectedItem.getName());
        		}
        	}
		}
		catch(InputMismatchException ex)
		{
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to add item to order,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
		catch(Exception ex)
		{
			System.out.println("Failed to add item to order,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
	}

	/**
	 * Removes an item from the order<p>
	 * User can choose what to remove from the list of items that are
	 * already in the order
	 */
	private void removeItemFromOrder()
	{
		if(_currentOrders.isEmpty())
		{
			System.out.print("\nWell, there are no orders"
					+ " taken at the moment!");
			System.out.println(" Try taking a new order? :-)");
			
			return;
		}
		
		try
		{
        	int orderIndex = 0;
        	int maxOrders = _currentOrders.size();
        	
        	System.out.println();
        	System.out.printf("%5s%-25s", "", "Order ID");
    		System.out.printf("%-15s", "Staff ID");
    		System.out.printf("%-15s", "Customer ID");
    		System.out.printf("%-15s%n", "Table Number");
    		
        	for(Order order : _currentOrders)
        	{
        		System.out.printf("%-5s", "(" + (++orderIndex) + ")");
        		order.displayOrderSummary();
        	}
        	
        	System.out.print("\nPlease select the order to remove"
        			+ " from (0 to cancel): ");
        	int selectedOrderIndex = sc.nextInt();
        	
        	if(selectedOrderIndex == 0)
        	{
        		System.out.println("\nNothing to be updated!");
				return;
        	}
        	
        	// Valid values from 1 to maxOrders
			if (selectedOrderIndex < 1 || selectedOrderIndex > maxOrders)
			{
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to update order,"
						+ " please try again..");
				return;
			}
			
			Order updatingOrder = _currentOrders.get(selectedOrderIndex - 1);			
			System.out.println("\nWhat would you like to remove from the order?");
			
			List<OrderItem> orderItems = updatingOrder.getOrderItems();
			
			int orderItemIndex = 0;
        	int maxOrderItems = orderItems.size();
    		
        	System.out.println();
        	for(OrderItem orderItem : orderItems)
        	{
        		System.out.printf("%-5s", "(" + (++orderItemIndex) + ")");
        		orderItem.displayOrderItemSummary();
        	}
        	
        	System.out.print("\nPlease select the order item to remove"
        			+ " from (0 to cancel): ");
        	int selectedOrderItemIndex = sc.nextInt();
        	
        	if(selectedOrderItemIndex == 0)
        	{
        		System.out.println("\nNothing to be updated!");
				return;
        	}
        	
        	// Valid values from 1 to maxOrders
			if (selectedOrderItemIndex < 1 || 
					selectedOrderItemIndex > maxOrderItems)
			{
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to update order,"
						+ " please try again..");
				return;
			}
			
			OrderItem removedOrderItem = 
					orderItems.get(selectedOrderItemIndex - 1);
			
        	if(removedOrderItem != null)
        	{
        		int maxItemQuantity = updatingOrder.getOrderItemQuantity(
        				removedOrderItem.getName());
        		
        		System.out.print("Enter the quantity to be removed: ");
        		int itemQuantity = sc.nextInt();

        		if(itemQuantity < 1)
        		{
        			System.out.println("\nInvalid input!"
        					+ " Minimum quantity is 1..");
        			System.out.println("Failed to remove item from order,"
        					+ " please try again..");
        		}
        		else if(itemQuantity > maxItemQuantity)
        		{
        			System.out.printf("%nInvalid input!"
        					+ " Maximum quantity is %d..%n", maxItemQuantity);
        			System.out.println("Failed to remove item from order,"
        					+ " please try again..");
        		}
        		else
        		{
        			updatingOrder.removeItemFromOrder(
        					removedOrderItem.getName(), itemQuantity);
        			System.out.printf("%nSuccessfully removed \"%dx"
        					+ " %s\" from the order!%n", itemQuantity,
        					removedOrderItem.getName());
        			
        			if(updatingOrder.getNumberOfOrderItems() == 0)
                	{
                		System.out.print("\nThe order is now empty! Cannot"
                				+ " have an order with 0 items,");
            			System.out.println(" removing order..");
            			
            			_currentOrders.remove(updatingOrder);
            			return;
                	}
        		}
        	}
		}
		catch(InputMismatchException ex)
		{
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to remove item from order,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
		catch(Exception ex)
		{
			System.out.println("Failed to remove item from order,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
	}
	
	/**
	 * Allows the user to make a payment for a order<p>
	 * The user can choose from a list of orders to make payment for,
	 * and once<br>the payment has been made, the table will become
	 * available and<br>the order will be added to the list of
	 * completed transactions
	 */
	private void makePaymentForOrder()
	{
		if(_currentOrders.isEmpty())
		{
			System.out.print("\nWell, there are no orders"
					+ " taken at the moment!");
			System.out.println(" What is there to make payment for? :-)");
			
			return;
		}
		
		try
		{
        	int orderIndex = 0;
        	int maxOrders = _currentOrders.size();
        	
        	System.out.println();
        	System.out.printf("%5s%-25s", "", "Order ID");
    		System.out.printf("%-15s", "Staff ID");
    		System.out.printf("%-15s", "Customer ID");
    		System.out.printf("%-15s%n", "Table Number");
    		
        	for(Order order : _currentOrders)
        	{
        		System.out.printf("%-5s", "(" + (++orderIndex) + ")");
        		order.displayOrderSummary();
        	}
        	
        	System.out.print("\nPlease select the order to make payment"
        			+ " for (0 to cancel): ");
        	int selectedOrderIndex = sc.nextInt();
        	
        	if(selectedOrderIndex == 0)
        	{
        		System.out.println("\nNo payment made!");
				return;
        	}
        	
        	// Valid values from 1 to maxOrders
			if (selectedOrderIndex < 1 || selectedOrderIndex > maxOrders)
			{
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to make payment,"
						+ " please try again..");
				return;
			}
			
			// Display the order invoice
			Order payingOrder = _currentOrders.get(selectedOrderIndex - 1);
			payingOrder.displayOrderInvoice();
			
			// Free the occupied table
			Table occupiedTable = RestaurantMgr.getRestaurantMgr().
					getTableByNumber(payingOrder.getTableNumber());
			if(occupiedTable != null)
			{
				occupiedTable.freeTable();
				System.out.printf("%nTable \'%d\' is now available!%n",
						occupiedTable.getTableNumber());
			}
			
			// Remove it from the list of current orders
			// Add it to the list of completed transactions
			_currentOrders.remove(payingOrder);
			_completedOrders.add(payingOrder);
		}
		catch(InputMismatchException ex)
		{
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to make payment,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
		catch(Exception ex)
		{
			System.out.println("Failed to make payment,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
	}
	
	/**
	 * Allows the user to view the sale revenue report by day/month
	 */
	public void displaySaleRevenue()
	{
		if(_completedOrders.isEmpty())
		{
			System.out.println("\nNo completed orders!"
					+ " Nothing to be displayed..");
			return;
		}
		
		try
		{
			System.out.println("\n(1) Display sale revenue report by day");
			System.out.println("(2) Display sale revenue report by month");
			
			System.out.print("\nPlease select the mode of display"
					+ " (0 to cancel): ");
			
			int displayMode = sc.nextInt();
			
			if(displayMode == 0)
			{
				System.out.println("\nNothing to be displayed!");
				return;
			}
			
			if(displayMode < 1 || displayMode > 2)
			{
				System.out.println("\nInvalid choice,"
						+ " valid options are (1) & (2)!");
				System.out.println("Failed to display sale revenue report,"
						+ " please try again..");
				return;
			}
			
			if(displayMode == 1)
				displaySaleRevenueByDay();
			else
				displaySaleRevenueByMonth();
		}
		catch(InputMismatchException ex)
		{
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to display sale revenue report,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
		catch(Exception ex)
		{
			System.out.println("Failed to display sale revenue report,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
	}
	
	/**
	 * Allows the user to view the sale revenue by day
	 * <p>
	 * The user will only be able to view the sale revenue report for
	 * <br>the current day or a valid day in the past<p>
	 * 
	 * The system will display a list of order invoices made on the selected day
	 * if there are any,<br> and display the total revenue for that day<br>
	 * 
	 * Handles exceptions for invalid date input (e.g. 30/02/2014)
	 */
	private void displaySaleRevenueByDay()
	{
		try
		{	
			SimpleDateFormat sdf = new SimpleDateFormat();
        	sdf.applyPattern("dd/MM/yyyy");
        	sdf.setLenient(false);
        	
        	System.out.println("\nDisplaying sale revenue report by day..");
        	System.out.print("Enter date (dd/mm/yyyy): ");
        	String saleRevenueDateStr = sc.next();

        	Date saleRevenueDate = sdf.parse(saleRevenueDateStr);
        	Calendar saleRevenueCal = GregorianCalendar.getInstance();
        	saleRevenueCal.setTime(saleRevenueDate);
        	
			Calendar currentInstant = GregorianCalendar.getInstance();
        	Date currentDateTime = currentInstant.getTime();
        	currentInstant.setTime(currentDateTime);
        	currentInstant.set(Calendar.HOUR_OF_DAY, 23);
        	currentInstant.set(Calendar.MINUTE, 59);
        	
        	if(saleRevenueCal.after(currentInstant))
        	{
        		System.out.print("\nInvalid date! ");
    			System.out.println("Failed to check sale revenue report,"
    					+ " please try again..");
    			System.out.println("NOTE: You can only check sale revenue"
    					+ " reports for previous days!");		
    			return;
        	}
        	
        	double overallRevenue = 0.0;
        	
        	for(Order order : _completedOrders)
        	{
        		Calendar orderDateTime = order.getOrderDateTime();
        		
        		if( (orderDateTime.get(Calendar.YEAR) == 
        				saleRevenueCal.get(Calendar.YEAR)) &&
        				(orderDateTime.get(Calendar.MONTH) ==
        				saleRevenueCal.get(Calendar.MONTH)) &&
        				(orderDateTime.get(Calendar.DAY_OF_MONTH) ==
        				saleRevenueCal.get(Calendar.DAY_OF_MONTH)) )
        		{
        			overallRevenue += order.getNettTotalPrice();
        			order.displayOrderInvoice();
        		}
        	}
        	
        	SimpleDateFormat saleRevenueDateFormat;
        	saleRevenueDateFormat = new SimpleDateFormat("E, dd/MM/yyyy");
        	
        	if(overallRevenue == 0.0)
        	{
        		System.out.println("\nThere are no sales made on the selected"
        				+ " day, \"" + saleRevenueDateFormat.
        				format(saleRevenueDate) + "\"");
        	}
        	else
        	{
        		System.out.println("\nTotal sales for"
        				+ " \"" + saleRevenueDateFormat.
        				format(saleRevenueDate) + "\": " +
        				new DecimalFormat("$###,##0.00").
        				format(overallRevenue) );
        	}
		}
		catch(ParseException ex)
        {
        	System.out.print("\nInvalid date input! ");
			System.out.println("Failed to display sale revenue report,"
					+ " please try again..");
			System.out.println("NOTE: Date entered should"
        			+ " be in dd/mm/yyyy, e.g. 25/10/2014!");	
			return;
        }
		catch(InputMismatchException ex)
		{
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to display sale revenue report,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
		catch(Exception ex)
		{
			System.out.println("\nFailed to display sale revenue report,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
	}
	
	/**
	 * Allows the user to view the sale revenue by month
	 * <p>
	 * The user will only be able to view the sale revenue report for
	 * <br>the current month or a valid month in the past<p>
	 * 
	 * The system will display the highest sale of the month, lowest sale of
	 * <br>the month and the total revenue for the selected month<br>
	 * 
	 * Handles exceptions for invalid month input (e.g. 13/2014)
	 */
	private void displaySaleRevenueByMonth()
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat();
        	sdf.applyPattern("MM/yyyy");
        	sdf.setLenient(false);
        	
        	System.out.println("\nDisplaying sale revenue report by month..");
        	System.out.print("Enter month (mm/yyyy): ");
        	String saleRevenueDateStr = sc.next();

        	Date saleRevenueMonth = sdf.parse(saleRevenueDateStr);
        	Calendar saleRevenueCal = GregorianCalendar.getInstance();
        	saleRevenueCal.setTime(saleRevenueMonth);
        	
			Calendar currentInstant = GregorianCalendar.getInstance();
        	Date currentDateTime = currentInstant.getTime();
        	currentInstant.setTime(currentDateTime);
        	currentInstant.set(Calendar.DAY_OF_MONTH, 0);
        	
			if (saleRevenueCal.get(Calendar.YEAR) > currentInstant
					.get(Calendar.YEAR))
			{
				System.out.print("\nInvalid month! ");
				System.out.println("Failed to check sale revenue report,"
						+ " please try again..");
				System.out.println("NOTE: You can only check sale revenue"
						+ " reports for previous/current month!");
				return;
			}
			if (saleRevenueCal.get(Calendar.YEAR) == currentInstant
					.get(Calendar.YEAR))
			{
				if ((saleRevenueCal.get(Calendar.MONTH) - 1) > 
					currentInstant.get(Calendar.MONTH))
				{
					System.out.print("\nInvalid month! ");
					System.out.println("Failed to check sale revenue report,"
							+ " please try again..");
					System.out.println("NOTE: You can only check sale revenue"
							+ " reports for previous/current month!");
					return;
				}
			}
        	
        	double[] overallRevenue = new double[31];
        	double totalRevenue = 0.0;
        	
        	for(Order order : _completedOrders)
        	{
        		Calendar orderDateTime = order.getOrderDateTime();
        		
        		if( (orderDateTime.get(Calendar.YEAR) == 
        				saleRevenueCal.get(Calendar.YEAR)) &&
        				(orderDateTime.get(Calendar.MONTH) ==
        				saleRevenueCal.get(Calendar.MONTH)) )
        		{
        			overallRevenue[orderDateTime.get(Calendar.DAY_OF_MONTH) - 1]
        					+= order.getNettTotalPrice();
        			
        			totalRevenue += order.getNettTotalPrice();
        		}
        	}
        	
        	SimpleDateFormat saleRevenueDateFormat;
        	saleRevenueDateFormat = new SimpleDateFormat("MMMM yyyy");
        	
        	if(totalRevenue == 0.0)
        	{
        		System.out.println("\nThere are no sales made on the selected"
        				+ " month, \"" + saleRevenueDateFormat.
        				format(saleRevenueCal.getTime()) + "\"!");
        		return;
        	}
        	
        	int minDay = 0, maxDay = 0;
        	double minDayRevenue = 1000, maxDayRevenue = 0;
        	
        	for(int currDay = 0; currDay < overallRevenue.length; currDay++)
        	{
        		if(overallRevenue[currDay] == 0)
        			continue;
        		
        		if(overallRevenue[currDay] >= maxDayRevenue)
        		{
        			maxDayRevenue = overallRevenue[currDay];
        			maxDay = currDay;
        		}
        		
        		if(overallRevenue[currDay] <= minDayRevenue)
        		{
        			minDayRevenue = overallRevenue[currDay];
        			minDay = currDay;
        		}
        	}
        	
        	saleRevenueDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        	saleRevenueCal.set(Calendar.DAY_OF_MONTH, maxDay + 1);
        	System.out.printf("%nHighest sales for the month: \"%s\"\t",
        			saleRevenueDateFormat.format(saleRevenueCal.getTime()));
        	System.out.printf("Revenue for the day: %s%n",
        			new DecimalFormat("$###,##0.00").format(maxDayRevenue));
        	
        	saleRevenueCal.set(Calendar.DAY_OF_MONTH, minDay + 1);
        	System.out.printf("Lowest sales for the month: \"%s\"\t",
        			saleRevenueDateFormat.format(saleRevenueCal.getTime()));
        	System.out.printf("Revenue for the day: %s%n",
        			new DecimalFormat("$###,##0.00").format(minDayRevenue));
        	
        	saleRevenueDateFormat = new SimpleDateFormat("MMM yyyy");
        	System.out.printf("%nTotal sales for the month \"%s\": %s%n",
        			saleRevenueDateFormat.format(saleRevenueCal.getTime()),
        			new DecimalFormat("$###,##0.00").format(totalRevenue));
		}
		catch(ParseException ex)
        {
        	System.out.print("\nInvalid month input! ");
			System.out.println("Failed to display sale revenue report,"
					+ " please try again..");
			System.out.println("NOTE: Month entered should"
        			+ " be in mm/yyyy, e.g. 10/2014!");
			return;
        }
		catch(InputMismatchException ex)
		{
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to display sale revenue report,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
		catch(Exception ex)
		{
			System.out.println(ex.toString());
			System.out.println("\nFailed to display sale revenue report,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
	}
}