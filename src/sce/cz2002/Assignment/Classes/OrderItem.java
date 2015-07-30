package sce.cz2002.Assignment.Classes;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * A single item within the order<br>
 * Includes the name of this order item, price of this order item and
 * the quantity of this order item
 * 
 * @author Jin Yao
 */
public class OrderItem implements Serializable
{
	/**
	 * Generated serial version ID for serializable classes
	 */
	private static final long serialVersionUID = -2799143503609964306L;

	/**
	 * The name of this order item
	 */
	private String _name;
	
	/**
	 * The price of this order item
	 */
	private double _price;
	
	/**
	 * The quantity of this order item
	 */
	private int _quantity;
	
	/**
	 * Creates a new order item with the given name, price
	 * as well as the quantity
	 * 
	 * @param name The name for this order item
	 * @param price The price for this order item
	 * @param quantity The quantity of this order item
	 */
	public OrderItem(String name, double price, int quantity)
	{
		_name = name;
		_price = price;
		_quantity = quantity;
	}
	
	/**
	 * Gets the name of this order item
	 * @return this OrderItem's name
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Changes the name of this order item
	 * @param newName This orderItem's new name
	 */
	public void setName(String newName) {
		_name = newName;
	}
	
	/**
	 * Gets the price of this order item
	 * @return this orderItem's price
	 */
	public double getPrice() {
		return _price;
	}
	
	/**
	 * Changes the price of this order item
	 * @param newPrice This orderItem's new price
	 */
	public void setPrice(double newPrice) {
		_price = newPrice;
	}
	
	/**
	 * Gets the quantity of this order item
	 * @return this orderItem's quantity
	 */
	public int getQuantity() {
		return _quantity;
	}
	
	/**
	 * Changes the quantity of this order item
	 * @param newQuantity This orderItem's new quantity
	 */
	public void setQuantity(int newQuantity) {
		_quantity = newQuantity;
	}
	
	/**
	 * Increments the quantity of this order item by the given value
	 * @param incValue the value added to this orderItem's 
	 * 				   current quantity
	 */
	public void incrementQuantity(int incValue) {
		_quantity += incValue;
	}
	
	/**
	 * Decrements the quantity of this order item by the given value
	 * @param decValue the value subtracted from this orderItem's 
	 * 				   current quantity
	 */
	public void decrementQuantity(int decValue) {
		_quantity -= decValue;
	}
	
	/**
	 * Formats and display the details of this order item,
	 * including information such as quantity, name and the price
	 * of the order item (price of each item * quantity)
	 */
	public void displayOrderItemDetails()
	{
		System.out.printf("%-5s", getQuantity() + "x");
		System.out.printf("%-30s", getName());
		System.out.printf("%40s%n",
				new DecimalFormat("$###,##0.00").format(
						getPrice() * getQuantity()));
	}
	
	/**
	 * Displays a summary of this order item,
	 * including quantity and item name
	 */
	public void displayOrderItemSummary()
	{
		System.out.printf("%-5s", getQuantity() + "x");
		System.out.printf("%-30s%n", getName());
	}
}