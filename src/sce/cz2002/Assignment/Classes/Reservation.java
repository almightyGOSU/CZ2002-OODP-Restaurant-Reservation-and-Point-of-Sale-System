package sce.cz2002.Assignment.Classes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Represents a reservation made by a customer<p>
 * Implements the Serializable interface to allow reservations
 * to be saved<br>
 * Implements the Comparable interface to allow reservations
 * to be sorted by chronological order
 * 
 * @author Chua Yong Lun
 *
 */
public class Reservation implements Serializable, Comparable<Reservation>
{
	/**
	 * Generated serial version ID for serializable classes
	 */
	private static final long serialVersionUID = -1362303644760942385L;

	/**
	 * The table number that this reservation is associated with
	 */
	private int _reservedTableNo;
	
	/**
	 * The customer ID of the customer that reserved this table
	 */
	private int _customerID;
	
	/**
	 * The name of the customer that reserved this table
	 */
	private String _customerName;
	
	/**
	 * The contact of the customer that reserved this table
	 */
	private int _customerContact;
	
	/**
	 * The number of people that this reservation is for
	 */
	private int _numOfPeople;
	
	/**
	 * The date/time that this reservation is made for
	 */
	private Calendar _startDateTime;
	
	/**
	 * The duration of this reservation
	 */
	private int _duration;
	
	/**
	 * A Simple Date Format used to format date/time related information
	 */
	private static final SimpleDateFormat _dateFormatter = 
			new SimpleDateFormat("E, dd/MM/yyyy, HH:mm");
	
	/**
	 * Creates a new reservation with the given information
	 * 
	 * @param reservedTableNo The table number that this reservation is for
	 * @param customerID The customer ID of the customer that made this
	 * 					 reservation
	 * @param customerName The name of the customer that made this reservation
	 * @param customerContact The contact number of the customer that made this
	 * 						  reservation
	 * @param numOfPeople The number of people that this reservation is made for
	 * @param startDateTime The date/time for this reservation
	 * @param duration The duration of this reservation
	 */
	public Reservation(int reservedTableNo, int customerID,
			String customerName, int customerContact, int numOfPeople,
			Calendar startDateTime, int duration) {
		
		_reservedTableNo = reservedTableNo;
		
		_customerID = customerID;
		_customerName = customerName;
		_customerContact = customerContact;
		
		_numOfPeople = numOfPeople;
		
		_startDateTime = startDateTime;
		_duration = duration;
	}
	
	/**
	 * Gets the table number of the table that the reservation is made for
	 * 
	 * @return This reservation's associated table number
	 */
	public int getReservedTableNo() {
		return _reservedTableNo;
	}
	
	/**
	 * Changes the table number that this reservation is made for
	 * 
	 * @param newReservedTableNo This reservation's new table number
	 */
	public void setReservedTableNo(int newReservedTableNo) {
		_reservedTableNo = newReservedTableNo;
	}
	
	/**
	 * Gets the customer ID of the customer that made this reservation
	 * 
	 * @return The customer ID of the customer that made this reservation
	 */
	public int getCustomerID() {
		return _customerID;
	}
	
	/**
	 * Gets the name of the customer that made this reservation
	 * 
	 * @return The name of the customer that made this reservation
	 */
	public String getCustomerName() {
		return _customerName;
	}
	
	/**
	 * Gets the contant number of the customer that made this reservation
	 * 
	 * @return The contant number of the customer that made this reservation
	 */
	public int getCustomerContact() {
		return _customerContact;
	}
	
	/**
	 * Gets the number of people that this reservation was made for
	 * 
	 * @return The number of people that this reservation was made for
	 */
	public int getNumOfPeople() {
		return _numOfPeople;
	}
	
	/**
	 * Changes the number of people for this reservation
	 * 
	 * @param newNumOfPeople The new number of people for this reservation
	 */
	public void setNumOfPeople(int newNumOfPeople) {
		_numOfPeople = newNumOfPeople;
	}
	
	/**
	 * Gets the date/time for this reservation
	 * 
	 * @return The date/time for this reservation
	 */
	public Calendar getStartDateTime() {
		return _startDateTime;
	}
	
	/**
	 * Changes the date/time for this reservation
	 * 
	 * @param newStartDateTime This reservation's new date/time
	 */
	public void setStartDateTime(Calendar newStartDateTime) {
		_startDateTime = newStartDateTime;
	}
	
	/**
	 * Gets the duration of this reservation
	 * 
	 * @return This reservation's duration
	 */
	public int getDuration() {
		return _duration;
	}
	
	/**
	 * Changes the duration of this reservation
	 * 
	 * @param newDuration This reservation's new duration
	 */
	public void setDuration(int newDuration) {
		_duration = newDuration;
	}
	
	/**
	 * Displays the reservation details with proper formatting<p>
	 * Displayed information include table number,
	 * customer ID, customer name, contact number,<br>
	 * the number of people, the reservation date/time and the
	 * duration of the reservation
	 */
	public void displayReservationDetails()
	{
		System.out.printf("%-20s%n", "Table Number: " + getReservedTableNo());
		
		System.out.printf("%5s%-25s", "",
				"Customer ID: " + getCustomerID());
		System.out.printf("%-50s", "Name: " + getCustomerName());
		System.out.printf("%-20s%n", "Contact: " + getCustomerContact());
		
		System.out.printf("%5s%-25s", "",
				"Number of People: " + getNumOfPeople());
		System.out.printf("%-50s", "Reservation Date/Time: " + 
				_dateFormatter.format(_startDateTime.getTime()));
		System.out.printf("%-20s%n", "Duration: " + getDuration() + 
				(getDuration() > 1 ? " Hours" : " Hour"));
	}
	
	/**
	 *  Display table number, customer name, reservation date/time and
	 *  reservation duration in a single line
	 *  <p>Provides a summary of the reservation
	 */
	public void displayReservationSummary()
	{
		// "Table Number"
		System.out.printf("%-15d", getReservedTableNo());
		
		// "Customer Name"
		System.out.printf("%-30s", getCustomerName());
		
		// "Reservation Date/Time"
		System.out.printf("%-25s", 
				_dateFormatter.format(_startDateTime.getTime()));
		
		// "Reservation Duration"
		System.out.printf("%-20s%n", getDuration() + 
				(getDuration() > 1 ? " Hours" : " Hour"));
	}

	/**
	 * Overrides the compareTo function of the Comparable interface<p>
	 * Allows a collection of reservations
	 * to be sorted in chronological order based on date and time
	 */
	@Override
	public int compareTo(Reservation res)
	{
		return this.getStartDateTime().compareTo(res.getStartDateTime());
	}
}