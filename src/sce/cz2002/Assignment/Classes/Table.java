package sce.cz2002.Assignment.Classes;

/**
 * A table within the restaurant<br>
 * Each table has a different number of seats<br>
 * A table can be either available, reserved or occupied
 * 
 * @author Chua Yong Lun
 *
 */
public class Table
{
	/**
	 * The table number for this table
	 */
	private int _tableNumber;
	
	/**
	 * The number of seats that this table has
	 */
	private int _numOfSeats;
	
	/**
	 * Indicates whether this table is reserved at the moment
	 */
	private boolean _isReserved;
	
	/**
	 * Indicates whether this table is occupied at the moment
	 */
	private boolean _isOccupied;
	
	/**
	 * Customer ID of the customer who reserved/occupied the table (if any)
	 */
	private int _customerID;
	
	/**
	 * Creates a new Table object using the given information
	 * 
	 * @param tableNumber The table number associated with this table
	 * @param numOfSeats The number of seats that this table has
	 * @param isReserved Indictates whether this table is reserved
	 * @param isOccupied Indicates whether this table is currently occupied
	 * @param customerID Indicates the customer ID of the customer at this table
	 */
	public Table(int tableNumber, int numOfSeats, boolean isReserved,
			boolean isOccupied, int customerID) {
		
		_tableNumber = tableNumber;
		_numOfSeats = numOfSeats;
		
		_isReserved = isReserved;
		
		_isOccupied = isOccupied;
		_customerID = customerID;
	}
	
	/**
	 * Gets the table number associated with this table
	 * 
	 * @return This table's table number
	 */
	public int getTableNumber() {
		return _tableNumber;
	}
	
	/**
	 * Changes this table's table number
	 * 
	 * @param newTableNumber This table's new table number
	 */
	public void setTableNumber(int newTableNumber) {
		_tableNumber = newTableNumber;
	}
	
	/**
	 * Gets the number of seats that this table has
	 * 
	 * @return This table's number of seats
	 */
	public int getNumOfSeats() {
		return _numOfSeats;
	}
	
	/**
	 * Changes the number of seats that this table has
	 * 
	 * @param newNumOfSeats This table's new number of seats
	 */
	public void setNumOfSeats(int newNumOfSeats) {
		_numOfSeats = newNumOfSeats;
	}
	
	/**
	 * Indicates whether this table is reserved at the moment
	 * 
	 * @return True if this table is reserved at the moment
	 */
	public boolean isReserved() {
		return _isReserved;
	}
	
	/**
	 * Indicates whether this table is occupied at the moment
	 * 
	 * @return True if this table is occupied at the moment
	 */
	public boolean isOccupied() {
		return _isOccupied;
	}
	
	/**
	 * Gets the customer ID of the customer at this table
	 * 
	 * @return The customer ID of the customer at this table
	 */
	public int getCustomerID() {
		return _customerID;
	}
	
	/**
	 * Marks a table as being reserved
	 * 
	 * @param customerID The customer ID of the customer that
	 *                   reserved this table
	 */
	public void reserveTable(int customerID)
	{
		_isReserved = true;
		_customerID = customerID;
	}
	
	/**
	 * Releases a table that was previously reserved
	 */
	public void releaseTable()
	{
		_isReserved = false;
		_customerID = 0;
	}
	
	/**
	 * Assigns a table to a customer
	 * 
	 * @param customerID The customer ID of the customer
	 *                   that is at this table
	 */
	public void assignTable(int customerID)
	{
		_isReserved = false;
		_isOccupied = true;
		_customerID = customerID;
	}
	
	/**
	 * Frees the table that was previously occupied by some customer
	 */
	public void freeTable()
	{
		_isReserved = false;
		_isOccupied = false;
		_customerID = 0;
	}
	
	/**
	 * Displays the table information<p>
	 * Displayed information include the table number, number of seats,<br>
	 * the current status of this table (Available, Reserved, Occupied)<br>
	 * and the customer ID of the customer if it is reserved/occupied
	 */
	public void displayStatus()
	{
		System.out.printf("%-20s", getTableNumber());
		System.out.printf("%-20s", getNumOfSeats());
		
		System.out.printf("%-20s",
				isOccupied() ? "Occupied" :
				(isReserved() ? "Reserved" : "Available"));
		
		System.out.printf("%-20s%n", (getCustomerID() != 0)
				? getCustomerID() : "N/A");	
	}
}