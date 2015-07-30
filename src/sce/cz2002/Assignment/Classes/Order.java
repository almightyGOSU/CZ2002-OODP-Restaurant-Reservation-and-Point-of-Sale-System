package sce.cz2002.Assignment.Classes;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import sce.cz2002.Assignment.Control.HumanResourceMgr;

/**
 * Stores a list of orderItems <p>
 * Includes the staffID of the staff who took the order,
 * customerID of the customer,<br> the table number and the number
 * of people<br>
 * Includes the orderID as well as the date/time the order was taken
 * 
 * @author Jin Yao
 *
 */
public class Order implements Serializable
{
	/**
	 * Generated serial version ID for serializable classes
	 */
	private static final long serialVersionUID = 7048025711997254699L;
	
	/**
	 * 10% Service charge for all orders
	 */
	private static final double SERVICE_CHARGE = 1.1;
	
	/**
	 * 7% Goods and Services Tax for all orders
	 */
	private static final double GOODS_SERVICES_TAX = 1.07;
	
	/**
	 * 10% Discount given to customers that are members
	 */
	private static final double MEMBERSHIP_DISCOUNT = 0.9;
	
	/**
	 * Staff ID of the staff who took the order
	 */
	private int _staffID;
	
	/**
	 * Customer ID of the customer who made this order
	 */
	private int _customerID;
	
	/**
	 * Table Number that this order is for
	 */
	private int _tableNumber;
	
	/**
	 * Number of people at the table
	 */
	private int _numOfPeople;
	
	/**
	 * Original price of the order<br>
	 * Calculated by adding the actual price of each order item
	 * <br>Actual price of each order item = 
	 * (order item price * order item quantity)
	 */
	private double _originalTotalPrice;
	
	/**
	 * Nett price of this item<br>
	 * Calculated by adding the service charge and GST to the original price
	 * <br> 10% discount will be given if the customer is a member
	 */
	private double _nettTotalPrice;
	
	/**
	 * The list of all order items that this order contains
	 */
	private List<OrderItem> _orderItems;
	
	/**
	 * The date/time that this order was taken
	 */
	private Calendar _orderDateTime;
	
	/**
	 * The order ID of this order
	 */
	private String _orderID;
	
	/**
	 * Simple date format used to format date/time for displaying
	 */
	private static final SimpleDateFormat _dateFormatter = 
			new SimpleDateFormat("E, dd/MM/yyyy, HH:mm");
	
	/**
	 * Creates a new order with the given information
	 * 
	 * @param staffID The staff ID of the staff who took this order
	 * @param customerID The customer ID of the customer who made this order
	 * @param tableNumber The table number that this order is for
	 * @param numOfPeople The number of people at the table
	 * @param originalTotalPrice The original price of the order
	 * @param nettTotalPrice The final nett price of the order
	 * @param orderDateTime The date/time that this order was made
	 * @param orderID The order ID for this order
	 */
	public Order(int staffID, int customerID, int tableNumber, 
			int numOfPeople, double originalTotalPrice,
			double nettTotalPrice, Calendar orderDateTime, String orderID)
	{
		_staffID = staffID;
		_customerID = customerID;
		
		_tableNumber = tableNumber;
		_numOfPeople = numOfPeople;
		
		_originalTotalPrice = originalTotalPrice;
		_nettTotalPrice = nettTotalPrice;
		
		_orderItems = new ArrayList<OrderItem>();
		
		_orderDateTime = orderDateTime;
		_orderID = orderID;
	}
	
	/**
	 * Creates a new order with the given information<br>
	 * Used for loading an existing order if the list of
	 * order items is provided
	 * 
	 * @param staffID The staff ID of the staff who took this order
	 * @param customerID The customer ID of the customer who made this order
	 * @param tableNumber The table number that this order is for
	 * @param numOfPeople The number of people at the table
	 * @param originalTotalPrice The original price of the order
	 * @param nettTotalPrice The final nett price of the order
	 * @param orderDateTime The date/time that this order was made
	 * @param orderID The order ID for this order
	 * @param orderItems The list of order items (if any)
	 */
	public Order(int staffID, int customerID, int tableNumber, 
			int numOfPeople, double originalTotalPrice,
			double nettTotalPrice, Calendar orderDateTime, String orderID,
			List<OrderItem> orderItems)
	{
		_staffID = staffID;
		_customerID = customerID;
		
		_tableNumber = tableNumber;
		_numOfPeople = numOfPeople;
		
		_originalTotalPrice = originalTotalPrice;
		_nettTotalPrice = nettTotalPrice;
		
		_orderDateTime = orderDateTime;
		_orderID = orderID;
		
		if(orderItems != null)
			_orderItems = orderItems;
		else
			_orderItems = new ArrayList<OrderItem>();
	}
	
	/**
	 * Gets the staff ID of the staff who took this order
	 * 
	 * @return the staff ID of the staff who took this order
	 */
	public int getStaffID() {
		return _staffID;
	}
	
	/**
	 * Gets the customer ID of the customer which this order is for
	 * 
	 * @return the customer ID of the customer which this order is for
	 */
	public int getCustomerID() {
		return _customerID;
	}
	
	/**
	 * Gets the table number that this order is for
	 * 
	 * @return the table number which this order is for
	 */
	public int getTableNumber() {
		return _tableNumber;
	}
	
	/**
	 * Gets the number of people at the table that this order is for
	 * 
	 * @return the number of people at the table that this order is for
	 */
	public int getNumOfPeople() {
		return _numOfPeople;
	}
	
	/**
	 * Gets the original price of this order
	 * 
	 * @return the original price for this order
	 */
	public double getOriginalTotalPrice() {
		return _originalTotalPrice;
	}
	
	/**
	 * Changes the original total price of this order 
	 * 
	 * @param newOriginalTotalPrice This order's new original total price
	 */
	private void setOriginalTotalPrice(double newOriginalTotalPrice) {
		_originalTotalPrice = newOriginalTotalPrice;
	}
	
	/**
	 * Gets the nett total price of this order, after tax and membership discount
	 * 
	 * @return the nett total price for this order
	 */
	public double getNettTotalPrice() {
		return _nettTotalPrice;
	}
	
	/**
	 * Changes the nett total price of this order
	 * 
	 * @param newNettTotalPrice The order's new nett total price
	 */
	private void setNettTotalPrice(double newNettTotalPrice) {
		_nettTotalPrice = newNettTotalPrice;
	}
	
	/**
	 * Gets the date/time that this order was made
	 * 
	 * @return the date/time that this order was made
	 */
	public Calendar getOrderDateTime() {
		return _orderDateTime;
	}
	
	/**
	 * Gets the order ID associated with this order
	 * 
	 * @return this order's orderID
	 */
	public String getOrderID() {
		return _orderID;
	}
	
	/**
	 * Adds a selected menuItem of a given quantity to the order<p>
	 * A new orderItem will be added to the order if this menuItem
	 * is not in the order<br>
	 * The quantity of the corresponding orderItem will be incremented
	 * if this menuItem is already in the order
	 * 
	 * @param menuItem the menu item that the customer wish to add to the order
	 * @param quantity the quantity of the menu item to be added
	 */
	public void addItemToOrder(MenuItem menuItem, int quantity)
	{
		if(!_orderItems.isEmpty())
		{
			for(OrderItem orderItem : _orderItems)
			{
				if(orderItem.getName() == menuItem.getName())
				{
					orderItem.incrementQuantity(quantity);
					recalculateOrderPrice();
					
					return;
				}
			}
		}
		
		// Empty order list or added menuItem not part of order list
		OrderItem newOrderItem;
		newOrderItem = new OrderItem(menuItem.getName(),
				menuItem.getPrice(), quantity);
		
		_orderItems.add(newOrderItem);
		recalculateOrderPrice();
	}
	
	/**
	 * Removes a given quantity of the selected menu item from the order<p>
	 * If the selected menu item has a quantity of 0
	 * after the removal process, <br>the order item will be removed
	 * from the order
	 * 
	 * @param itemName the menu item that the customer wish to remove from the order
	 * @param quantity the quantity of the menu item to be removed
	 */
	public void removeItemFromOrder(String itemName, int quantity)
	{
		if(getNumberOfOrderItems() == 0)
			return;
		
		Iterator<OrderItem> ordersIter = _orderItems.iterator();
		OrderItem orderItem = null;
		
		while(ordersIter.hasNext()) {
			
			orderItem = ordersIter.next();
			
			if(orderItem.getName().equals(itemName)) {
				
				orderItem.decrementQuantity(quantity);

				if(orderItem.getQuantity() == 0)
					ordersIter.remove();

				break;
			}
		}
		
		recalculateOrderPrice();
	}

	/**
	 * Gets the total number of order items inside this order
	 * 
	 * @return the total number of order items in this order
	 */
	public int getNumberOfOrderItems() {
		return _orderItems.size();
	}

	/**
	 * Checks whether this order is empty (contains no order items)
	 * 
	 * @return whether this order is empty (contains no order items)
	 */
	public boolean isEmpty() {
		return _orderItems.isEmpty();
	}

	/**
	 * Gets the list of all order items inside this order
	 * 
	 * @return the list of all order items inside this order
	 */
	public List<OrderItem> getOrderItems()	{
		return _orderItems;
	}
	
	/**
	 * Recalculates the order price based on the price of each order item<br>
	 * The price of each order item is determined by (price * quantity)
	 * <p>
	 * Based on the original total order price, the nett total price is
	 * calculated<br>
	 * This is done by adding the relevants taxes and giving a membership
	 * discount if the customer is a member
	 */
	private void recalculateOrderPrice()
	{
		if(getNumberOfOrderItems() == 0)
			return;

		double newOriginalOrderPrice = 0;
		for(OrderItem orderItem : _orderItems)
		{
			newOriginalOrderPrice += 
					(orderItem.getPrice() * orderItem.getQuantity());
		}

		setOriginalTotalPrice(newOriginalOrderPrice);
		
		double newNettTotalPrice = 0;
		newNettTotalPrice = (newOriginalOrderPrice * SERVICE_CHARGE
				* GOODS_SERVICES_TAX);
		
		if(HumanResourceMgr.getHRMgr().isMember(getCustomerID()))
		{
			newNettTotalPrice *= MEMBERSHIP_DISCOUNT;
		}
		
		setNettTotalPrice(newNettTotalPrice);
	}
	
	/**
	 * Gets the exact quantity of a specific order item based on the
	 * item name provided
	 * 
	 * @param itemName the name of the item to look for
	 * @return the quantity of the item being searched for
	 */
	public int getOrderItemQuantity(String itemName)
	{
		for(OrderItem orderItem : _orderItems)
		{
			if(orderItem.getName().equals(itemName))
				return orderItem.getQuantity();
		}
		
		return 0;
	}
	
	/**
	 * Display order details with proper formatting<br>
	 * Displayed information include order ID, staff ID,
	 * customer ID and the table number<br>
	 * The details of each order item included in the order is also displayed
	 */
	public void displayOrderDetails()
	{
		System.out.printf("%-35s", "Order ID: " + getOrderID());
		System.out.printf("%-17s",
				"Staff ID: " + getStaffID());
		System.out.printf("%-19s",
				"Customer ID: " + getCustomerID());
		System.out.printf("%-12s%n",
				"Table No: " + getTableNumber());

		int orderItemNo = 1;
		for(OrderItem orderItem : _orderItems)
		{
			System.out.printf("%5s%-5s: ", "", ("(" + (orderItemNo++) + ")") );
			orderItem.displayOrderItemDetails();
		}
	}
	
	/**
	 * Displays a summary of the order<br>
	 * Displayed information includes order ID, staff ID,
	 * customer ID and the table number
	 */
	public void displayOrderSummary()
	{
		System.out.printf("%-25s", getOrderID());
		System.out.printf("%-15d", getStaffID());
		System.out.printf("%-15d", getCustomerID());
		System.out.printf("%-15d%n", getTableNumber());
	}
	
	/**
	 * Display <b>complete order details for payment</b><br>
	 * Displayed information include the order ID, staff ID, customer ID,<br>
	 * the order date/time, table number and the number of people<p>
	 * Shows a <b>complete breakdown of the order items</b> included in this order<p>
	 * Displays the original total price of the order<br>
	 * Shows the relevant taxes being added to the original price<br>
	 * Shows the membership discount if customer is a member<br>
	 * Displays the <b>final nett total price</b> of the order
	 */
	public void displayOrderInvoice()
	{
		System.out.print("\n" + new String(new char[36]).replace("\0", "="));
		System.out.print(" Order Invoice ");
		System.out.println(new String(new char[36]).replace("\0", "="));
		
		System.out.printf("%n%-47s", "Order ID: " + getOrderID());
		System.out.printf("%-20s",
				"Staff ID: " + getStaffID());
		System.out.printf("%20s%n",
				"Customer ID: " + getCustomerID());
		
		System.out.printf("%-47s", "Order Date/Time: " + 
			_dateFormatter.format(_orderDateTime.getTime()));
		System.out.printf("%-20s",
				"Table No: " + getTableNumber());
		System.out.printf("%20s%n%n",
				"Pax: " + getNumOfPeople());

		int orderItemNo = 1;
		for(OrderItem orderItem : _orderItems)
		{
			System.out.printf("%5s%-5s: ", "", ("(" + (orderItemNo++) + ")") );
			orderItem.displayOrderItemDetails();
		}
		
		System.out.println("\n" + new String(new char[87]).replace("\0", "-"));

		System.out.printf("%87s%n", "Subtotal: " +
				new DecimalFormat("$###,##0.00").format(
						getOriginalTotalPrice()));

		System.out.printf("%n%87s%n", "+10% Service Charge");
		System.out.printf("%87s%n", "+7% Goods & Service Tax");
		if(HumanResourceMgr.getHRMgr().isMember(getCustomerID()))
		{
			System.out.printf("%87s%n", "-10% membership discount");
		}

		System.out.printf("%n%87s%n", "Total Payable: " + 
				new DecimalFormat("$###,##0.00").format(getNettTotalPrice()));
		
		System.out.println(new String(new char[87]).replace("\0", "-") + "\n");
		
		System.out.print(new String(new char[29]).replace("\0", " "));
		System.out.print("Thank you for dining with us!");
		System.out.println(new String(new char[29]).replace("\0", " "));
		
		System.out.print("\n" + new String(new char[36]).replace("\0", "="));
		System.out.print(" Order Invoice ");
		System.out.println(new String(new char[36]).replace("\0", "="));
	}
}