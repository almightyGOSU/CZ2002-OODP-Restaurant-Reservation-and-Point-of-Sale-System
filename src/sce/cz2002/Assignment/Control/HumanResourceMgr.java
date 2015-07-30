package sce.cz2002.Assignment.Control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import sce.cz2002.Assignment.Classes.Person;
import sce.cz2002.Assignment.Classes.Staff;
import sce.cz2002.Assignment.Classes.Customer;

/**
 * A singleton class that takes care of all staff and customer related functionality<p>
 * 
 * Functionalities include saving/loading staff and customer information, viewing all staff,
 * <br> adding/updating/removing staff members, viewing all customers and adding/updating/removing
 * <br> customers
 * 
 * @author Jin Yao
 *
 */
public class HumanResourceMgr
{
	/**
	 * An enumeration type used to determine the submenu state<br>
	 * Performs the appropriate function based on the submenu state selected
	 * 
	 * @author Jin Yao
	 *
	 */
	private enum HRSubmenuState
	{
		ViewAllStaff,
		CreateStaff,
		UpdateStaff,
		RemoveStaff,
		ViewAllCustomer,
		CreateCustomer,
		UpdateCustomer,
		RemoveCustomer
	}
	
	/**
	 * The file path indicating where the file used to store staff
	 * and customer information is saved
	 */
	private static final String HR_FILE_PATH = "humanResource.dat";
	
	/**
	 * A static instance of this Human Resource manager
	 */
	private static HumanResourceMgr _HRMgr = null;
	
	/**
	 * A static list of the staff in this restaurant
	 */
	private static List<Staff> _staff;
	
	/**
	 * A static list of the customers registered in the system
	 */
	private static List<Customer> _customer;
	
	/**
	 * Standard Java Scanner used for processing user inputs
	 */
	private static Scanner sc;
	
	/**
	 * Keeps track of the current staff ID, prevents duplicate staff ID
	 * <br>when creating new staff
	 */
	private static int currStaffID = 0;
	
	/**
	 * Keeps track of the current customer ID, prevents duplicate customer
	 * <br>ID when adding new customers
	 */
	private static int currCustomerID = 0;
	
	/**
	 * Private constructor used to support the Singleton design pattern
	 * <p>
	 * Creates a new Java Scanner object, and creates a new
	 * arraylist of staff members<br>as well as a new arraylist
	 * of customers
	 */
	private HumanResourceMgr()
	{
		sc = new Scanner(System.in);
		
		_staff = new ArrayList<Staff>();
		_customer = new ArrayList<Customer>();
	}
	
	/**
	 * Public static function used to get hold of the Human Resource manager
	 * 
	 * @return The static instance of the Human Resource manager
	 */
	public static HumanResourceMgr getHRMgr()
	{
		if(_HRMgr == null)
		{
			_HRMgr = new HumanResourceMgr();
		}
		
		return _HRMgr;
	}
	
	/**
	 * Loads the staff and customer related information from the human<br>
	 * resource file, if it exists
	 * <br>Should be called at the start of the application
	 */
	public void loadPeople()
	{
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try
		{
			fis = new FileInputStream(HR_FILE_PATH);
			in = new ObjectInputStream(fis);
			
			Object obj = in.readObject();

			if (obj instanceof ArrayList<?>) {
				ArrayList<?> al = (ArrayList<?>) obj;

				if (al.size() > 0) {
					for (int objIndex = 0; objIndex < al.size(); objIndex++) {
						Object childObj = al.get(objIndex);

						if (childObj instanceof Staff) {
							_staff.add(((Staff) childObj));
						}
					}
				}
			}
			
			obj = in.readObject();

			if (obj instanceof ArrayList<?>) {
				ArrayList<?> al = (ArrayList<?>) obj;

				if (al.size() > 0) {
					for (int objIndex = 0; objIndex < al.size(); objIndex++) {
						Object childObj = al.get(objIndex);

						if (childObj instanceof Customer) {
							_customer.add(((Customer) childObj));
						}
					}
				}
			}
			
			// Read the current staff ID & current customer ID
			// Used to increment the IDs correctly
			currStaffID = in.readInt();
			currCustomerID = in.readInt();

			in.close();
			
			if(!_staff.isEmpty())
				System.out.println("'Staff' data loaded successfully!");
			
			if(!_customer.isEmpty())
				System.out.println("'Customer' data loaded successfully!");
			
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to load 'Staff/Customer' data!");
		} catch (IOException ex) {
			System.out.println("Unable to load 'Staff/Customer' data!");
		} catch (ClassNotFoundException ex) {
			System.out.println("Unable to load 'Staff/Customer' data!");
		} catch (Exception ex) {
			System.out.println("Unable to load 'Staff/Customer' data!");
		}
	}
	
	/**
	 * Saves the all staff and customer related information to the human
	 * resource file<br>
	 * Should be called before exiting the application
	 */
	public void savePeople() {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;

		try {
			fos = new FileOutputStream(HR_FILE_PATH);
			out = new ObjectOutputStream(fos);
			
			out.writeObject(_staff);
			out.writeObject(_customer);
			
			out.writeInt(currStaffID);
			out.writeInt(currCustomerID);
			
			out.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to save 'Staff/Customer' data!");
		} catch (IOException ex) {
			System.out.println("Unable to save 'Staff/Customer' data!");
		} catch (Exception ex) {
			System.out.println("Unable to save 'Staff/Customer' data!");
		}

		System.out.println("Saved 'Staff/Customer' data successfully!");
	}
	
	/**
	 * Displays the list of options for this Human Resource Management submenu
	 */
	private void displayHRManagementOptions()
	{
		System.out.println("\n" + new String(new char[50]).replace("\0", "="));
		System.out.print("|" + new String(new char[8]).replace("\0", " "));
		System.out.print("Human Resource Management Submenu");
		System.out.println(new String(new char[7]).replace("\0", " ") + "|");
		System.out.println(new String(new char[50]).replace("\0", "="));
		
		System.out.println("0. Return to main menu");
		System.out.println("1. View all staff");
		System.out.println("2. Add a new staff");
		System.out.println("3. Update a existing staff");
		System.out.println("4. Remove a existing staff");
		System.out.println("5. View all customer");
		System.out.println("6. Add a new customer");
		System.out.println("7. Update a existing customer");
		System.out.println("8. Remove a existing customer");
	}
	
	/**
	 * Prompts the user to choose a human resource management function<br>
	 * Handles exceptions appropriately and allows the user to retry
	 * 
	 * @return The integer representing the human resource management<br>
	 *  	   function that the user has chosen
	 */
	public int getHRManagementChoice()
	{
		displayHRManagementOptions();
		
		int HRChoice = -1;
		do
		{
			try
			{
				System.out.print("\nPlease enter your choice (0-8): ");
				HRChoice = sc.nextInt();
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
			
			if(HRChoice < 0 || HRChoice > 8)
				System.out.println("Invalid choice! Please try again..");
			
		} while (HRChoice < 0 || HRChoice > 8);
		
		if(HRChoice == 0)
			return HRChoice; // Go back to main menu
		else
		{
			switch(HRSubmenuState.values()[HRChoice - 1])
			{
			case ViewAllStaff:
				displayStaff();
				break;
			
			case CreateStaff:
				addStaff();
				break;
				
			case UpdateStaff:
				updateStaff();
				break;
				
			case RemoveStaff:
				removeStaff();
				break;
				
			case ViewAllCustomer:
				displayCustomer();
				break;
				
			case CreateCustomer:
				addCustomer();
				break;
				
			case UpdateCustomer:
				updateCustomer();
				break;
				
			case RemoveCustomer:
				removeCustomer();
				break;
			}
		}
		
		return HRChoice;
	}
	
	/**
	 * Displays information about the staff that are currently
	 * working in the restaurant
	 */
	private void displayStaff()
	{
		if(_staff.isEmpty())
		{
			System.out.print("\nWell, there are no staff"
					+ " hired at the moment!");
			System.out.println(" Try hiring new staff? :-)");
			
			return;
		}

		System.out.println();
		System.out.print(new String(new char[37]).replace("\0", "*"));
		System.out.print(" STAFF ");
		System.out.println(new String(new char[36]).replace("\0", "*"));

		// Display staff information
		for(Staff staff : _staff)
		{
			System.out.println();
			staff.displayStaffDetails();
		}

		System.out.println();
		System.out.print(new String(new char[37]).replace("\0", "*"));
		System.out.print(" STAFF ");
		System.out.println(new String(new char[36]).replace("\0", "*"));
	}
	
	/**
	 * Allows the user to add a new staff<p>
	 * Required staff information include the name, age, gender, job title
	 * and salary
	 */
	private void addStaff()
	{
		Staff newStaff = null;
		
		try
		{
			System.out.print("\nEnter the name of the staff: ");
			String staffName = sc.nextLine();
			
			System.out.print("Enter the age of the staff: ");
			int staffAge = sc.nextInt();
			
			System.out.print("Enter the gender of the staff"
					+ " (M/F): ");
			char staffGenderValue = sc.next().charAt(0);
			staffGenderValue = Character.toUpperCase(staffGenderValue);
			
			Person.Gender staffGender; 
			if(staffGenderValue == 'M')
			{
				staffGender = Person.Gender.Male;
			}
			else if(staffGenderValue == 'F')
			{
				staffGender = Person.Gender.Female;
			}
			else
			{
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to add new staff,"
						+ " please try again..");
				return;
			}
			
			int numOfJobs = Staff.JobTitle.values().length;
			int jobIndex = 0;
			for(Staff.JobTitle jobTitle : Staff.JobTitle.values())
			{
				System.out.printf("%n%-5s", "(" + (++jobIndex) + ")");
				System.out.print(jobTitle.toStrValue());
			}
			
			System.out.printf("%n%nChoose the job title (1-%d): ", numOfJobs);
			int jobTitleValue = sc.nextInt();
			sc.nextLine();
			
			// Valid jobTitleValue from 1 to numOfJobs
			if(jobTitleValue < 1 || jobTitleValue > numOfJobs)
			{
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to add new staff,"
						+ " please try again..");
				return;
			}
			
			Staff.JobTitle staffJobTitle =
					Staff.JobTitle.values()[jobTitleValue - 1];
			
			System.out.print("Enter the salary of the staff: ");
			double staffSalary = sc.nextDouble();
			sc.nextLine();
			
			newStaff = new Staff(staffName, staffAge, staffGender,
					++currStaffID, staffJobTitle, staffSalary);
			
			_staff.add(newStaff);
			
			if(newStaff != null)
			{
				System.out.printf("%nSuccessfully hired \"%s\""
						+ " as a \"%s\"!%n", newStaff.getName(),
						newStaff.getJobTitle().toStrValue());
			}
		}
		catch(InputMismatchException ex)
		{
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to add new staff,"
					+ " please try again..");
			
			sc.nextLine(); // Clear the garbage input
			return;
		}
		catch(Exception ex)
		{
			System.out.println("\nFailed to add new staff,"
					+ " please try again..");
			
			sc.nextLine(); // Clear the garbage input
			return;
		}
	}
	
	/**
	 * Allows the user to update information about an existing staff
	 */
	private void updateStaff()
	{
		if(_staff.isEmpty())
		{
			System.out.print("\nWell, there are no staff"
					+ " hired at the moment!");
			System.out.println(" Seems like there is"
					+ " nothing to update? :-)");
			
			return;
		}
		
		try
		{
			int numOfStaff = 0;

			System.out.println();
			System.out.printf("%5s%-30s", "", "Staff Name");
			System.out.printf("%-30s", "Staff Job Title");
			System.out.printf("%-20s%n", "Staff Salary");

			// Display staff
			for(Staff staff : _staff)
			{
				System.out.printf("%-5s", "(" + (++numOfStaff) + ")");
				staff.displayStaffSummary();
			}

			System.out.printf("%nPlease select a staff to update "
					+ "(0 to cancel): ");

			int staffIndex = sc.nextInt();
			sc.nextLine();

			// User chooses not to update any staff
			if(staffIndex == 0)
			{
				System.out.println("\nNothing to be updated!");
				return;
			}

			// Valid staffIndex from 1 to numOfStaff
			if (staffIndex < 1 || staffIndex > numOfStaff) {
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to update staff,"
						+ " please try again..");
				return;
			}

			Staff updatingStaff = _staff.get(staffIndex - 1);
			updateStaffInfo(updatingStaff);
		}
		catch(InputMismatchException ex)
		{
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to update staff,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
		catch(Exception ex)
		{
			System.out.println("\nFailed to update staff,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
	}

	/**
	 * Allows the user to fire an existing staff in the restaurant
	 */
	private void removeStaff()
	{
		if(_staff.isEmpty())
		{
			System.out.print("\nWell, there are no staff"
					+ " hired at the moment!");
			System.out.println(" Seems like there is"
					+ " nobody to fire? :-)");
			
			return;
		}

		try
		{
			int numOfStaff = 0;
			String firedStaffName = null;

			System.out.println();
			System.out.printf("%5s%-30s", "", "Staff Name");
			System.out.printf("%-30s", "Staff Job Title");
			System.out.printf("%-20s%n", "Staff Salary");

			// Display staff
			for(Staff staff : _staff)
			{
				System.out.printf("%-5s", "(" + (++numOfStaff) + ")");
				staff.displayStaffSummary();
			}

			System.out.printf("%nPlease select a staff to fire "
					+ "(0 to cancel): ");

			int staffIndex = sc.nextInt();
			sc.nextLine();

			// User chooses not to fire any staff
			if(staffIndex == 0)
			{
				System.out.println("\nNo one has been fired! :-)");
				return;
			}

			// Valid staffIndex from 1 to numOfStaff
			if (staffIndex < 1 || staffIndex > numOfStaff) {
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to fire staff,"
						+ " please try again..");
				return;
			}

			firedStaffName = _staff.get(staffIndex - 1).getName();
			_staff.remove(staffIndex - 1);

			if(firedStaffName != null)
			{
				System.out.printf("%nSuccessfully fired"
						+ " \"%s\"!%n", firedStaffName);
			}
		}
		catch(InputMismatchException ex)
		{
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to fire staff,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
		catch(Exception ex)
		{
			System.out.println("\nFailed to fire staff,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
	}
	
	/**
	 * Displays the information about all the customers that are
	 * currently registered in the system
	 */
	private void displayCustomer()
	{
		if(_customer.isEmpty())
		{
			System.out.print("\nWell, there are no customers"
					+ " registered in the system!");
			System.out.println(" Try getting some customers? :-)");
			
			return;
		}

		System.out.println();
		System.out.print(new String(new char[35]).replace("\0", "*"));
		System.out.print(" CUSTOMER ");
		System.out.println(new String(new char[35]).replace("\0", "*"));

		// Display customer
		for(Customer customer : _customer)
		{
			System.out.println();
			customer.displayCustomerDetails();
		}

		System.out.println();
		System.out.print(new String(new char[35]).replace("\0", "*"));
		System.out.print(" CUSTOMER ");
		System.out.println(new String(new char[35]).replace("\0", "*"));
	}
	
	/**
	 * Allows the user to add a new customer into the system<p>
	 * Required customer information include the name, age, gender,
	 * contact number and membership status
	 */
	private void addCustomer()
	{
		Customer newCustomer = null;
		
		try
		{
			System.out.print("\nEnter the name of the customer: ");
			String customerName = sc.nextLine();
			
			System.out.print("Enter the age of the customer: ");
			int customerAge = sc.nextInt();
			
			System.out.print("Enter the gender of the customer"
					+ " (M/F): ");
			char customerGenderValue = sc.next().charAt(0);
			customerGenderValue = Character.toUpperCase(customerGenderValue);
			
			Person.Gender customerGender; 
			if(customerGenderValue == 'M')
			{
				customerGender = Person.Gender.Male;
			}
			else if(customerGenderValue == 'F')
			{
				customerGender = Person.Gender.Female;
			}
			else
			{
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to add new customer,"
						+ " please try again..");
				return;
			}
			
			System.out.print("Enter the contact number of the customer: ");
			int customerContact = sc.nextInt();
			sc.nextLine();
			
			System.out.print("Is this customer a member? (Y/N): ");
			char customerMembershipValue = sc.next().charAt(0);
			customerMembershipValue = 
					Character.toUpperCase(customerMembershipValue);
			
			boolean isMember = false;
			if(customerMembershipValue == 'Y')
			{
				isMember = true;
			}
			else if(customerMembershipValue == 'N')
			{
				isMember = false;
			}
			else
			{
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to add new customer,"
						+ " please try again..");
				return;
			}
			
			newCustomer = new Customer(customerName, customerAge, customerGender,
					++currCustomerID, customerContact, isMember);
			
			_customer.add(newCustomer);
			
			if(newCustomer != null)
			{
				System.out.printf("%nSuccessfully registered \"%s\""
						+ "!%n", newCustomer.getName());
			}
		}
		catch(InputMismatchException ex)
		{
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to add new customer,"
					+ " please try again..");
			
			sc.nextLine(); // Clear the garbage input
			return;
		}
		catch(Exception ex)
		{
			System.out.println("\nFailed to add new customer,"
					+ " please try again..");
			
			sc.nextLine(); // Clear the garbage input
			return;
		}
	}
	
	/**
	 * Allows the user to update the information about an existing customer
	 */
	private void updateCustomer()
	{
		if(_customer.isEmpty())
		{
			System.out.print("\nWell, there are no customers"
					+ " registered in the system!");
			System.out.println(" What is there to update? :-)");
			
			return;
		}

		try
		{
			int numOfCustomer = 0;

			System.out.println();
			System.out.printf("%5s%-30s", "", "Customer Name");
			System.out.printf("%-30s", "Contact Number");
			System.out.printf("%-20s%n", "Existing Member");

			// Display customer
			for(Customer customer : _customer)
			{
				System.out.printf("%-5s", "(" + (++numOfCustomer) + ")");
				customer.displayCustomerSummary();
			}

			System.out.printf("%nPlease select a customer to update "
					+ "(0 to cancel): ");

			int customerIndex = sc.nextInt();
			sc.nextLine();

			// User chooses not to update any customer
			if(customerIndex == 0)
			{
				System.out.println("\nNothing to be updated!");
				return;
			}

			// Valid customerIndex from 1 to numOfCustomer
			if (customerIndex < 1 || customerIndex > numOfCustomer) {
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to update customer,"
						+ " please try again..");
				return;
			}

			Customer updatingCustomer = _customer.get(customerIndex - 1);
			updateCustomerInfo(updatingCustomer);
		}
		catch(InputMismatchException ex)
		{
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to update customer,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
		catch(Exception ex)
		{
			System.out.println("\nFailed to update customer,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
	}
	
	/**
	 * Allows the user to remove an existing customer in the system
	 */
	private void removeCustomer()
	{
		if(_customer.isEmpty())
		{
			System.out.print("\nWell, there are no customers"
					+ " registered in the system!");
			System.out.println(" What is there to remove? :-)");
			
			return;
		}

		try
		{
			int numOfCustomer = 0;
			String removedCustName = null;

			System.out.println();
			System.out.printf("%5s%-30s", "", "Customer Name");
			System.out.printf("%-30s", "Customer Contact Number");
			System.out.printf("%-20s%n", "Member");

			// Display customer
			for(Customer customer : _customer)
			{
				System.out.printf("%-5s", "(" + (++numOfCustomer) + ")");
				customer.displayCustomerSummary();
			}

			System.out.printf("%nPlease select a customer to remove "
					+ "(0 to cancel): ");

			int customerIndex = sc.nextInt();
			sc.nextLine();

			// User chooses not to remove any customer
			if(customerIndex == 0)
			{
				System.out.println("\nNo customer removed! :-)");
				return;
			}

			// Valid customerIndex from 1 to numOfCustomer
			if (customerIndex < 1 || customerIndex > numOfCustomer) {
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to remove customer,"
						+ " please try again..");
				return;
			}

			removedCustName = _customer.get(customerIndex - 1).getName();

			_customer.remove(customerIndex - 1);

			if(removedCustName != null)
			{
				System.out.printf("%nSuccessfully removed"
						+ " \"%s\"!%n", removedCustName);
			}
		}
		catch(InputMismatchException ex)
		{
			System.out.print("\nInvalid input! ");
			System.out.println("Failed to remove customer,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
		catch(Exception ex)
		{
			System.out.println("Failed to remove customer,"
					+ " please try again..");

			sc.nextLine(); // Clear the garbage input
			return;
		}
	}
	
	/**
	 * Updates the information of a selected staff<p>
	 * The user can choose to update the basic information such as name, age,
	 * gender<br>or update the job title and salary
	 * 
	 * @param updatingStaff The staff to be updated
	 * 
	 * @throws InputMismatchException Exception thrown for input mismatch
	 * @throws Exception General exceptions
	 */
	private void updateStaffInfo(Staff updatingStaff) 
			throws InputMismatchException, Exception
	{
		int updateOption = -1;
		do
		{
			System.out.println();
			System.out.println("(1) Update staff name, age & gender");
			System.out.println("(2) Update job title & salary");
			
			System.out.printf("%nPlease select the action to be"
					+ " performed (0 to cancel): ");
		
			updateOption = sc.nextInt();
			sc.nextLine();
			
			// User chooses not to update any staff
			if(updateOption == 0)
			{
				System.out.println("\nNothing to be updated!");
				return;
			}
			
			// Valid values from 1 to 2
			if (updateOption < 1 || updateOption > 2) {
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to update staff,"
						+ " please try again..");
			}
			
			if(updateOption == 1)
			{
				System.out.printf("%n%-40s", "Current Name: \"" + 
						updatingStaff.getName() + "\"");
				System.out.print("\tEnter a new name: ");
				String newStaffName = sc.nextLine();
				
				System.out.printf("%-40s", "Current age: \"" + 
						updatingStaff.getAge() + "\"");
				System.out.print("\tEnter a new age: ");
				int newStaffAge = sc.nextInt();
				
				System.out.printf("%-40s", "Current gender: \"" + 
						updatingStaff.getGender().toStrValue() + "\"");

				System.out.print("\tEnter the gender of the staff"
						+ " (M/F): ");
				char staffGenderValue = sc.next().charAt(0);
				staffGenderValue = Character.toUpperCase(staffGenderValue);
				
				Person.Gender staffGender; 
				if(staffGenderValue == 'M')
				{
					staffGender = Person.Gender.Male;
				}
				else if(staffGenderValue == 'F')
				{
					staffGender = Person.Gender.Female;
				}
				else
				{
					System.out.print("\nInvalid input! ");
					System.out.println("Failed to update staff,"
							+ " please try again..");
					continue;
				}
				
				updatingStaff.setName(newStaffName);
				updatingStaff.setAge(newStaffAge);
				updatingStaff.setGender(staffGender);
				
				System.out.printf("%nSuccessfully updated information"
						+ " for \"%s\"!%n", updatingStaff.getName());
			}
			else if(updateOption == 2)
			{
				System.out.printf("%n%-40s%n", "Current Job Title: \"" + 
						updatingStaff.getJobTitle().toStrValue() + "\"");
				
				int numOfJobs = Staff.JobTitle.values().length;
				int jobIndex = 0;
				for(Staff.JobTitle jobTitle : Staff.JobTitle.values())
				{
					System.out.printf("%n%-5s", "(" + (++jobIndex) + ")");
					System.out.print(jobTitle.toStrValue());
				}
				
				System.out.printf("%n%nChoose the new job title (1-%d): ",
						numOfJobs);
				int jobTitleValue = sc.nextInt();
				sc.nextLine();
				
				// Valid jobTitleValue from 1 to numOfJobs
				if(jobTitleValue < 1 || jobTitleValue > numOfJobs)
				{
					System.out.print("\nInvalid input! ");
					System.out.println("Failed to update staff,"
							+ " please try again..");
					return;
				}
				
				Staff.JobTitle newStaffJobTitle =
						Staff.JobTitle.values()[jobTitleValue - 1];
				
				System.out.printf("%-40s", "Current salary: " + 
						new DecimalFormat("$###,##0.00").format(
								updatingStaff.getSalary()));
				
				System.out.print("\tEnter the new salary of the staff: ");
				double newStaffSalary = sc.nextDouble();
				sc.nextLine();
				
				updatingStaff.setJobTitle(newStaffJobTitle);
				updatingStaff.setSalary(newStaffSalary);
				
				System.out.printf("%nSuccessfully updated job title & salary"
						+ " for \"%s\"!%n", updatingStaff.getName());
			}
			
		} while(updateOption != 0);
	}
	
	/**
	 * Allows the user to update information regarding an existing user
	 * 
	 * @param updatingCustomer The customer to be updated
	 * 
	 * @throws InputMismatchException Exception thrown for input mismatch
	 * @throws Exception General Exception thrown
	 */
	private void updateCustomerInfo(Customer updatingCustomer)
			throws InputMismatchException, Exception
	{
		int updateOption = -1;
		do
		{
			System.out.println();
			System.out.println("(1) Update customer name, age & gender");
			System.out.println("(2) Update customer contact number"
					+ " & membership status");
			
			System.out.printf("%nPlease select the action to be"
					+ " performed (0 to cancel): ");
		
			updateOption = sc.nextInt();
			sc.nextLine();
			
			// User chooses not to update any customer
			if(updateOption == 0)
			{
				System.out.println("\nNothing to be updated!");
				return;
			}
			
			// Valid values from 1 to 2
			if (updateOption < 1 || updateOption > 2) {
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to update customer,"
						+ " please try again..");
			}
			
			if(updateOption == 1)
			{
				System.out.printf("%n%-40s", "Current Name: \"" + 
						updatingCustomer.getName() + "\"");
				System.out.print("\tEnter a new name: ");
				String newCustomerName = sc.nextLine();
				
				System.out.printf("%-40s", "Current age: \"" + 
						updatingCustomer.getAge() + "\"");
				System.out.print("\tEnter a new age: ");
				int newCustomerAge = sc.nextInt();
				
				System.out.printf("%-40s", "Current gender: \"" + 
						updatingCustomer.getGender().toStrValue() + "\"");

				System.out.print("\tEnter the gender of the customer"
						+ " (M/F): ");
				char custGenderValue = sc.next().charAt(0);
				custGenderValue = Character.toUpperCase(custGenderValue);
				
				Person.Gender custGender; 
				if(custGenderValue == 'M')
				{
					custGender = Person.Gender.Male;
				}
				else if(custGenderValue == 'F')
				{
					custGender = Person.Gender.Female;
				}
				else
				{
					System.out.print("\nInvalid input! ");
					System.out.println("Failed to update customer,"
							+ " please try again..");
					continue;
				}
				
				updatingCustomer.setName(newCustomerName);
				updatingCustomer.setAge(newCustomerAge);
				updatingCustomer.setGender(custGender);
				
				System.out.printf("%nSuccessfully updated information"
						+ " for \"%s\"!%n", updatingCustomer.getName());
			}
			else if(updateOption == 2)
			{
				System.out.printf("%-40s", "Current contact number: \"" + 
						updatingCustomer.getContactNumber() + "\"");
				System.out.print("\tEnter a new contact number: ");
				int newCustomerContact = sc.nextInt();
				
				System.out.printf("%-40s", "Current membership status: \"" + 
						(updatingCustomer.checkMembership() ? 
							"Existing Member" : "Not a member")  + "\"");
				
				System.out.print("\tIs this customer a member? (Y/N): ");
				char customerMembershipValue = sc.next().charAt(0);
				customerMembershipValue = 
						Character.toUpperCase(customerMembershipValue);
				
				boolean isMember = false;
				if(customerMembershipValue == 'Y')
				{
					isMember = true;
				}
				else if(customerMembershipValue == 'N')
				{
					isMember = false;
				}
				else
				{
					System.out.print("\nInvalid input! ");
					System.out.println("Failed to update customer,"
							+ " please try again..");
					return;
				}
				
				updatingCustomer.setContactNumber(newCustomerContact);
				updatingCustomer.setMembership(isMember);
				
				System.out.printf("%nSuccessfully updated contact number"
						+ " & membership status"
						+ " for \"%s\"!%n", updatingCustomer.getName());
			}
			
		} while(updateOption != 0);
	}
	
	/**
	 * Allows the user to select a customer from the list of all customers
	 * 
	 * @return The selected customer, if any and null if there is no customers registered
	 * 		   <br>or the user did not make a valid selection
	 */
	public Customer selectCustomer()
	{
		if(_customer.isEmpty())
		{
			System.out.print("\nThere are no customers"
					+ " registered in the system!");
			
			return null;
		}

		int customerIndex = 0;
		int maxCustomerIndex = _customer.size();

		do
		{
			try
			{
				int currIndex = 0;

				System.out.println();
				System.out.printf("%5s%-30s", "", "Customer Name");
				System.out.printf("%-30s", "Customer Contact Number");
				System.out.printf("%-20s%n", "Member");

				// Display customer
				for(Customer customer : _customer)
				{
					System.out.printf("%-5s", "(" + (++currIndex) + ")");
					customer.displayCustomerSummary();
				}

				System.out.printf("%nPlease select a customer: ");

				customerIndex = sc.nextInt();
				sc.nextLine();

				// Valid customerIndex from 1 to maxCustomerIndex
				if (customerIndex < 1 || customerIndex > maxCustomerIndex)
				{
					System.out.print("\nInvalid input! ");
					System.out.println("Failed to select customer,"
							+ " please try again..");
					continue;
				}
			}
			catch(InputMismatchException ex)
			{
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to select customer,"
						+ " please try again..");

				sc.nextLine(); // Clear the garbage input
			}
			catch(Exception ex)
			{
				System.out.println("Failed to select customer,"
						+ " please try again..");

				sc.nextLine(); // Clear the garbage input
			}

		} while(customerIndex < 1 || customerIndex > maxCustomerIndex);

		return _customer.get(customerIndex - 1);
	}
	
	/**
	 * Allows other control classes to check whether this particular customer
	 * <br>is a member
	 * 
	 * @param customerID The customer ID of the customer to check for
	 * 
	 * @return True if the provided customer ID is a member
	 */
	public boolean isMember(int customerID)
	{
		if(_customer.isEmpty())
			return false;
		
		for(Customer customer : _customer)
		{
			if(customer.getCustomerID() == customerID)
				return customer.checkMembership();
		}
		
		return false;
	}
	
	/**
	 * Allows the user to select a waiter from the list of all waiters
	 * 
	 * @return The selected waiter, if any and null if there are no waiters hired
	 * 		   <br>or the user did not make a valid selection
	 */
	public Staff selectWaiter()
	{
		if(_staff.isEmpty())
			return null;
		
		List<Staff> waiters = new ArrayList<Staff>();
		
		for(Staff staff : _staff)
		{
			if(staff.getJobTitle() == Staff.JobTitle.Waiter)
				waiters.add(staff);
		}
		
		if(waiters.isEmpty())
			return null;
		
		int waiterIndex = 0;
		int maxWaiterIndex = waiters.size();

		do
		{
			try
			{
				int currIndex = 0;

				System.out.println();
				System.out.printf("%5s%-15s", "", "Staff ID");
				System.out.printf("%-30s%n", "Waiter Name");

				// Display waiters
				for(Staff waiter : waiters)
				{
					System.out.printf("%-5s", "(" + (++currIndex) + ")");
					System.out.printf("%-15s", waiter.getStaffID());
					System.out.printf("%-30s%n", waiter.getName());
				}

				System.out.printf("%nPlease select a waiter"
						+ " to take the order: ");

				waiterIndex = sc.nextInt();
				sc.nextLine();

				// Valid waiterIndex from 1 to maxWaiterIndex
				if (waiterIndex < 1 || waiterIndex > maxWaiterIndex)
				{
					System.out.print("\nInvalid input! ");
					System.out.println("Failed to select waiter,"
							+ " please try again..");
					continue;
				}
			}
			catch(InputMismatchException ex)
			{
				System.out.print("\nInvalid input! ");
				System.out.println("Failed to select waiter,"
						+ " please try again..");

				sc.nextLine(); // Clear the garbage input
			}
			catch(Exception ex)
			{
				System.out.println("Failed to select waiter,"
						+ " please try again..");

				sc.nextLine(); // Clear the garbage input
			}

		} while(waiterIndex < 1 || waiterIndex > maxWaiterIndex);

		return waiters.get(waiterIndex - 1);
	}
}