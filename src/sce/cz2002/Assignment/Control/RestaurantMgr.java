package sce.cz2002.Assignment.Control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import sce.cz2002.Assignment.Classes.Customer;
import sce.cz2002.Assignment.Classes.Reservation;
import sce.cz2002.Assignment.Classes.Table;

/**
 * A singleton class that takes care of all restaurant-related functionality<p>
 * 
 * Functionalities include saving/loading restaurant reservation information, viewing the
 * <br>table availability, viewing the list of all reservations, adding a new reservation and
 * <br> removing existing reservations<p>
 * 
 * The restaurant manager also automatically removes expired reservations through a check
 * reservations function<p>
 * 
 * The restaurant only has total of 5 tables, with sizes 2, 4, 4, 6 and 10.<br>
 * The opening hours are from 0900 to 2200.
 * 
 * @author Jin Yao
 *
 */
public class RestaurantMgr
{
	/**
	 * An enumeration type used to determine the submenu state<br>
	 * Performs the appropriate function based on the submenu state selected
	 * 
	 * @author Jin Yao
	 *
	 */
	private enum RestaurantSubmenuState
	{
		ViewTableAvailability,
		ViewReservations,
		AddReservation,
		RemoveReservation
	}
	
	/**
	 * A list of all the table sizes
	 */
	private static final int[] TABLE_SIZES = {2, 4, 4, 6, 10};
	
	/**
	 * The default reservation duration
	 */
	private static final int RESERVATION_DURATION = 2;
	
	/**
	 * Restaurant opening hour
	 */
	private static final int RESTAURANT_OPENING_HOUR = 9;
	
	/**
	 * Restaurant closing hour
	 */
	private static final int RESTAURANT_CLOSING_HOUR = 22;
	
	/**
	 * The file path indicating where the file used to store reservation
	 * information is saved
	 */
	private static final String RESTAURANT_FILE_PATH = "restaurant.dat";
	
	/**
	 * A static instance of this Restaurant manager
	 */
	private static RestaurantMgr _restaurantMgr = null;
	
	/**
	 * A list of physical tables within this restaurant
	 */
	private static List<Table> _tables;
	
	/**
	 * A list of all reservations made for this restaurant
	 */
	private static List<Reservation> _reservations;
	
	/**
	 * Standard Java Scaner used for processing user input
	 */
	private static Scanner sc;
	
	/**
	 * A simple date format used to format date/time related information
	 */
	private static SimpleDateFormat _dateFormatter = null;
	
	/**
	 * Private constructor used to support the Singleton design pattern
	 * <br>
	 * Creates a new Java Scanner object, and creates a new
	 * arraylist of tables as well a <br>new arraylist
	 * of reservations<p>
	 * Calls an internal function to create the tables
	 */
	private RestaurantMgr()
	{
		sc = new Scanner(System.in);
		
		_tables = new ArrayList<Table>();
		_reservations = new ArrayList<Reservation>();
		
		_dateFormatter = new SimpleDateFormat("E, dd/MM/yyyy, HH:mm");
		
		setUpTables();
	}
	
	/**
	 * Public static function used to get hold of the Restaurant manager
	 * 
	 * @return The static instance of the Restaurant manager
	 */
	public static RestaurantMgr getRestaurantMgr()
	{
		if(_restaurantMgr == null)
		{
			_restaurantMgr = new RestaurantMgr();
		}
		
		return _restaurantMgr;
	}
	
	/**
	 * Loads the reservation related information from the reservation file,
	 *  if it exists
	 * <br>Should be called at the start of the application
	 */
	public void loadRestaurant()
	{
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try
		{
			fis = new FileInputStream(RESTAURANT_FILE_PATH);
			in = new ObjectInputStream(fis);
			
			Object obj = in.readObject();

			if (obj instanceof ArrayList<?>) {
				ArrayList<?> al = (ArrayList<?>) obj;

				if (al.size() > 0) {
					for (int objIndex = 0; objIndex < al.size(); objIndex++) {
						Object childObj = al.get(objIndex);

						if (childObj instanceof Reservation) {
							_reservations.add(((Reservation) childObj));
						}
					}
				}
			}

			in.close();
			
			if(!_reservations.isEmpty())
				System.out.println("'Reservations' data loaded successfully!");
			
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to load 'Reservations' data!");
		} catch (IOException ex) {
			System.out.println("Unable to load 'Reservations' data!");
		} catch (ClassNotFoundException ex) {
			System.out.println("Unable to load 'Reservations' data!");
		} catch (Exception ex) {
			System.out.println("Unable to load 'Reservations' data!");
		}
	}
	
	/**
	 * Saves the all reservation related information to the reservation file<br>
	 * Should be called before exiting the application
	 */
	public void saveRestaurant()
	{
		FileOutputStream fos = null;
		ObjectOutputStream out = null;

		try {
			fos = new FileOutputStream(RESTAURANT_FILE_PATH);
			out = new ObjectOutputStream(fos);
			
			out.writeObject(_reservations);
			
			out.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to save 'Reservations' data!");
		} catch (IOException ex) {
			System.out.println("Unable to save 'Reservations' data!");
		} catch (Exception ex) {
			System.out.println("Unable to save 'Reservations' data!");
		}

		System.out.println("Saved 'Reservations' data successfully!");
	}
	
	/**
	 * Creates the list of tables with the indicated sizes
	 */
	private void setUpTables()
	{
		int tableNumber = 1;
		for(int tableSize : TABLE_SIZES)
		{		
			Table newTable;
			newTable = new Table(tableNumber++, tableSize, false,
					false, 0);
			
			_tables.add(newTable);
		}
	}

	/**
	 * Displays the list of options for this Restaurant management submenu
	 */
	private void displayRestaurantManagementOptions()
	{
		System.out.println("\n" + new String(new char[50]).replace("\0", "="));
		System.out.print("|" + new String(new char[10]).replace("\0", " "));
		System.out.print("Restaurant Management Submenu");
		System.out.println(new String(new char[9]).replace("\0", " ") + "|");
		System.out.println(new String(new char[50]).replace("\0", "="));
		
		System.out.println("0. Return to main menu");
		System.out.println("1. View table availability");
		System.out.println("2. View reservations");
		System.out.println("3. Add a new reservation");
		System.out.println("4. Remove a existing reservation");
	}
	
	/**
	 * Prompts the user to choose a restaurant management function<br>
	 * Handles exceptions appropriately and allows the user to retry
	 * 
	 * @return The integer representing the restaurant management function
	 *         that the user has chosen
	 */
	public int getRestaurantManagementChoice()
	{
		displayRestaurantManagementOptions();
		
		int maxRestaurantChoices = RestaurantSubmenuState.values().length;
		int restaurantChoice = -1;
		do
		{
			try
			{
				System.out.printf("%nPlease enter your choice (0-%d): ",
						maxRestaurantChoices);
				restaurantChoice = sc.nextInt();
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
			
			if(restaurantChoice < 0 || restaurantChoice > maxRestaurantChoices)
				System.out.println("Invalid choice! Please try again..");
			
		} while (restaurantChoice < 0 || restaurantChoice > maxRestaurantChoices);
		
		if(restaurantChoice == 0)
			return restaurantChoice; // Go back to main menu
		else
		{
			switch(RestaurantSubmenuState.values()[restaurantChoice - 1])
			{
			case ViewTableAvailability:
				viewTableAvailability();
				break;
			
			case ViewReservations:
				viewReservations();
				break;
				
			case AddReservation:
				addReservation();
				break;
				
			case RemoveReservation:
				removeReservation();
				break;
			}
		}
		
		return restaurantChoice;
	}

	/**
	 * Displays the current status of all the tables in the restaurant
	 * <p>
	 * Shows the CustomerID if the table is reserved/occupied at the moment
	 * <p>
	 * Internal function checkReservations is called to ensure that
	 * expired reservations are removed <br> and the table is released
	 */
	private void viewTableAvailability()
	{
		checkReservations();
		
		System.out.printf("%n%-20s", "Table Number");
		System.out.printf("%-20s", "Number of Seats");
		System.out.printf("%-20s", "Table Status");
		System.out.printf("%-20s%n", "Customer ID");
		
		for(Table table : _tables)
		{
			table.displayStatus();
		}
	}

	/**
	 * Displays the list of all the valid reservations in the restaurant
	 * <p>
	 * Internal function checkReservations is called to ensure that
	 * expired reservations are removed
	 */
	private void viewReservations()
	{
		checkReservations();
		
		if(_reservations.isEmpty())
		{
			System.out.print("\nWell, there are no reservations"
					+ " made at the moment!");
			System.out.println(" Try adding a new reservation? :-)");
			
			return;
		}
		
		System.out.print("\n" + new String(new char[43]).replace("\0", "*"));
		System.out.print(" Reservations ");
		System.out.println(new String(new char[43]).replace("\0", "*"));
		
		int currReservationNo = 1;
		
		for(Reservation reservation : _reservations)
		{
			System.out.printf("%n%-5s", "(" + (currReservationNo++) + ")");
			reservation.displayReservationDetails();
		}
		
		System.out.print("\n" + new String(new char[43]).replace("\0", "*"));
		System.out.print(" Reservations ");
		System.out.println(new String(new char[43]).replace("\0", "*"));
	}

	/**
	 * Allows the user to add a new reservation<p>
	 * The user has to select a valid date/time (within the restaurant's
	 * <br>opening hours), select a customer and indicate the number of people
	 * <br>The system will check for available tables during the specified
	 * timeframe, and try to<br>make the reservation.<p>
	 * The user will be informed if there is full reservation.
	 */
	private void addReservation()
	{
		checkReservations();
		
		System.out.println("\nRestaurant's opening hours: "
    					+ "0900 - 2200, Reservation Duration: 2 Hours");
		System.out.println("NOTE: Reservations will be cancelled 5 minutes"
				+ " after reservation date/time if you do not show up! :-)");
		
        try
        {
        	SimpleDateFormat sdf = new SimpleDateFormat();
        	sdf.applyPattern("dd/MM/yyyy HH:mm");
        	sdf.setLenient(false);
        	
        	System.out.print("\nEnter reservation date (dd/mm/yyyy): ");
        	String reservationDateStr = sc.next();

        	System.out.print("Enter reservation time,"
        			+ " in 24-hour format (hh:mm): ");
        	String reservationTimeStr = sc.next();

        	Date reservationDateTime = sdf.parse(reservationDateStr 
        			+ " " + reservationTimeStr);
        	
        	Calendar startDateTime = GregorianCalendar.getInstance();
        	startDateTime.setTime(reservationDateTime);
        	
        	Calendar endDateTime = (Calendar) startDateTime.clone();
        	endDateTime.add(Calendar.HOUR_OF_DAY, RESERVATION_DURATION);
        	
        	Calendar restOpeningTime = (Calendar) startDateTime.clone();
        	restOpeningTime.set(Calendar.HOUR_OF_DAY, 
        			(RESTAURANT_OPENING_HOUR - 1) );
        	restOpeningTime.set(Calendar.MINUTE, 59);
        	
        	Calendar restClosingTime = (Calendar) restOpeningTime.clone();
        	restClosingTime.set(Calendar.HOUR_OF_DAY, RESTAURANT_CLOSING_HOUR);
        	restClosingTime.set(Calendar.MINUTE, 1);
        	
        	// Only allow reservations from 0900 - 2200
        	if( !(startDateTime.after(restOpeningTime)) ||
        		!(endDateTime.before(restClosingTime)) )
        	{
        		System.out.print("\nInvalid reservation date/time! ");
    			System.out.println("Failed to add new reservation,"
    					+ " please try again..");
    			System.out.println("NOTE: The restaurant is only open"
    					+ " from 0900 - 2200!");
    			
    			return;
        	}
        	
        	Calendar currentInstant = GregorianCalendar.getInstance();
        	Date currentDateTime = currentInstant.getTime();
        	currentInstant.setTime(currentDateTime);
        	
        	if(startDateTime.before(currentInstant))
        	{
        		System.out.print("\nInvalid reservation date/time! ");
    			System.out.println("Failed to add new reservation,"
    					+ " please try again..");
    			System.out.println("NOTE: Reservation can only"
    					+ " be made in advance!");
    			
    			return;
        	}
            
            Customer customer = HumanResourceMgr.getHRMgr().selectCustomer();
            if(customer == null)
            {
            	System.out.println("Unable to add new reservation as there are"
            			+ " no registered customers!");
            	return;
            }
            	
        	System.out.print("Enter number of people (1-10): ");
        	int numOfPeople = sc.nextInt();
        	
        	if(numOfPeople < 1 || numOfPeople > 10)
            {
            	System.out.println("Invalid number of people, failed to"
            			+ " add new reservation!");
            	return;
            }
        	
        	// Attempt to allocate a available table
        	int availableTableNumber = 0;
        	int tableNumber = 1;
        	
    		for(int tableSize : TABLE_SIZES)
    		{
    			if(tableSize >= numOfPeople)
    			{
    				boolean isReserved = false;
    				
    				for(Reservation reservation : _reservations)
    	    		{
    					Calendar resStartDateTime =
    							reservation.getStartDateTime();
    					
    					Calendar resEndDateTime = 
    							(Calendar) resStartDateTime.clone();
    					resEndDateTime.add(Calendar.HOUR_OF_DAY, 
    							reservation.getDuration());
    					
    					// Check for existing reservation for this table number
    	    			if(reservation.getReservedTableNo() == tableNumber)
    	    			{
    	    				/* Assumption: Edges overlapping is not allowed
        					 * 
        					 * Therefore, overlapping occurs when
        					 * (StartA <= EndB) and (EndA >= StartB)
        					 * 
        					 * Let StartA be startDateTime, EndA be endDateTime
        					 * Let StartB be resStartDateTime,
        					 * 	   EndB   be resEndDateTime
        					 */
    	    				
    	    				if(startDateTime.before(resEndDateTime) &&
    	    				 endDateTime.after(resStartDateTime))
	    	    			{
	    	    				isReserved = true;
	    	    				break;
	    	    			}
    	    			}
    	    		} // End reservations for loop
    				
    				if(!isReserved)
    				{
    					availableTableNumber = tableNumber;
    					break;
    				}
    			}
    			
    			tableNumber++;
    		} // End tables for loop
    		
    		if(availableTableNumber == 0)
    		{
    			System.out.printf("\nSorry, there are no tables available"
    					+ " at the selected date/time that can accommodate"
    					+ " %d people!%n", numOfPeople);
    		}
    		else
    		{	
    			Reservation newReservation;
    			newReservation = new Reservation(availableTableNumber,
    					customer.getCustomerID(), customer.getName(),
    					customer.getContactNumber(), numOfPeople,
    					startDateTime, RESERVATION_DURATION);
    			
    			_reservations.add(newReservation);
    			
    			Collections.sort(_reservations);
    			
    			System.out.printf("\nSuccessfully allocated Table"
    					+ " '%d' to '%s'!%n", availableTableNumber, 
    					customer.getName());
    			System.out.printf("Reservation Date/Time: %s,"
    					+ " Reservation Duration: %d Hours%n",
    					_dateFormatter.format(startDateTime.getTime()),
    					RESERVATION_DURATION);
    		}
        }
        catch(ParseException ex)
        {
        	System.out.print("\nInvalid reservation date/time! ");
			System.out.println("Failed to add new reservation,"
					+ " please try again..");
			System.out.println("NOTE: Reservation date should"
        			+ " be in dd/mm/yyyy, e.g. 25/12/2014 and"
        			+ "\n      reservation time should be in"
        			+ " hh:mm (24-hour format), e.g. 19:30!");
			
			return;
        }
        catch(InputMismatchException ex)
        {
        	System.out.print("\nInvalid input! ");
			System.out.println("Failed to add new reservation,"
					+ " please try again..");
			return;
        }
        catch(Exception ex)
        {
        	System.out.print("\nInvalid input! ");
			System.out.println("Failed to add new reservation,"
					+ " please try again..");
			return;
        }
	}
	
	/**
	 * Allows the user to remove a reservation from the list of reservations
	 */
	private void removeReservation()
	{
		checkReservations();
		
		if(_reservations.isEmpty())
		{
			System.out.print("\nWell, there are no reservations"
					+ " made at the moment!");
			System.out.println(" What is there to remove? :-)");
			
			return;
		}

		try
		{
			int numOfReservations = 0;

			System.out.println();
			System.out.printf("%5s%-15s", "", "Table Number");
			System.out.printf("%-30s", "Customer Name");
			System.out.printf("%-25s", "Reservation Date/Time");
			System.out.printf("%-20s%n", "Reservation Duration");

			// Display reservations
			for(Reservation reservation : _reservations)
			{
				System.out.printf("%-5s", "(" + (++numOfReservations) + ")");
				reservation.displayReservationSummary();
			}

			System.out.printf("%nPlease select a reservation to remove "
					+ "(0 to cancel): ");

			int reservationIndex = sc.nextInt();
			sc.nextLine();

			// User chooses not to remove any reservation
			if(reservationIndex == 0)
			{
				System.out.println("\nNo reservation removed! :-)");
				return;
			}

			// Valid reservationIndex from 1 to numOfReservations
			if (reservationIndex < 1 || reservationIndex > numOfReservations)
			{
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to remove reservation,"
						+ " please try again..");
				return;
			}

			Reservation removedReservation = 
					_reservations.get(reservationIndex - 1);

			if(removedReservation != null)
			{
				Table table = getTableByNumber(
						removedReservation.getReservedTableNo());
				
				if(table.isReserved() && (table.getCustomerID() == 
						removedReservation.getCustomerID()) )
				{
					table.releaseTable();
				}
				
				System.out.printf("%nSuccessfully removed reservation made by"
						+ " \"%s\"!%n", removedReservation.getCustomerName());
			}
			
			_reservations.remove(removedReservation);
		}
		catch(InputMismatchException ex)
		{
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to remove reservation,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
		catch(Exception ex)
		{
			System.out.println("Failed to remove reservation,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
	}
	
	/**
	 * An internal function used to remove expired reservations
	 * <p>
	 * This function also reserves the tables which has ongoing reservations!
	 */
	private void checkReservations()
	{
		if(_reservations.isEmpty())
			return;
		
		Calendar currentInstant = GregorianCalendar.getInstance();
		Date currentDateTime = currentInstant.getTime();
		currentInstant.setTime(currentDateTime);

		Iterator<Reservation> resIter = _reservations.iterator();
		Reservation reservation = null;
		
		while(resIter.hasNext()) {
			
			reservation = resIter.next();
			
			Calendar restStartDateTime = reservation.getStartDateTime();
			Calendar restClone = (Calendar) restStartDateTime.clone();
			restClone.add(Calendar.MINUTE, 5);

			// If currentInstant is after restStartDateTime + 5 mins
			if(restClone.before(currentInstant))
			{
				Table table = 
						getTableByNumber(reservation.getReservedTableNo());
				
				if( (!table.isOccupied()) && table.isReserved()
						&& (table.getCustomerID() == 
						reservation.getCustomerID()) )
				{
					table.releaseTable();
				}
				
				// Remove expired reservation
				resIter.remove();
			}
			else
			{
				// If reservationTime <= currentInstant & not expired
				// Set table as reserved
				restStartDateTime = reservation.getStartDateTime();
				if(restStartDateTime.before(currentInstant))
				{
					Table table = 
							getTableByNumber(reservation.getReservedTableNo());
					
					if(!table.isReserved())
						table.reserveTable(reservation.getCustomerID());
					
					if(table.isOccupied()) {
						// Remove expired reservation
						resIter.remove();
					}
				}
			}
		}
	}
	
	/**
	 * Gets a table by the specified table number
	 * 
	 * @param tableNumber The table's table number
	 * 
	 * @return The table that matches the specified table number,
	 * 		   null if there isn't one
	 */
	public Table getTableByNumber(int tableNumber)
	{
		for(Table table : _tables)
		{
			if(table.getTableNumber() == tableNumber)
				return table;
		}
		
		return null;
	}
	
	/**
	 * Returns the table reserved by a customer specified
	 * 
	 * @param customerID The customer ID of the customer that
	 *        made the reservation
	 *        
	 * @return The reserved table, if any and null if there
	 * 		   is no table reserved for this customer
	 */
	public Table getReservedTable(int customerID)
	{
		checkReservations();
		
		for(Table table : _tables)
		{
			if(table.isReserved())
			{
				if(table.getCustomerID() == customerID)
				{
					table.assignTable(customerID);
					return table;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the number of people that this reservation is for,
	 * based on the specified customer ID
	 * 
	 * @param customerID The customer ID of the customer that made this reservation
	 * 
	 * @return The number of people that this reservation is for
	 */
	public int getPaxFromReservation(int customerID)
	{
		if(_reservations.isEmpty())
			return 0;
		
		for(Reservation reservation : _reservations)
		{
			if(reservation.getCustomerID() == customerID)
			{
				return reservation.getNumOfPeople();
			}
		}
		
		return 0;
	}
	
	/**
	 * Gets an available table for walk-in customers (No reservations made)
	 * 
	 * @param customerID The customerID of the customer
	 * @param numOfPeople The number of people that the table has to accomodate
	 * 
	 * @return The available table, if any and null if there are no
	 * 		   tables available at the moment
	 */
	public Table getAvailableTable(int customerID, int numOfPeople)
	{
		checkReservations();
		
		for(Table table : _tables)
		{
			if(!table.isOccupied())
			{
				if(!table.isReserved())
				{
					if(table.getNumOfSeats() >= numOfPeople)
					{
						table.assignTable(customerID);
						return table;
					}
				}
			}
		}
		
		System.out.println("\nNo available tables!");
		return null;
	}
}