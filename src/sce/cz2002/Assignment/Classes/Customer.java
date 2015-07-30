package sce.cz2002.Assignment.Classes;

/**
 * A customer is someone who patronises the restaurant<br>
 * A customer is also a {@link Person}
 * 
 * @author Alvin Ng Keng Hian
 *
 */
public class Customer extends Person
{
	/**
	 * Generated serial version ID for serializable classes
	 */
	private static final long serialVersionUID = 2611166586818608297L;
	
	/**
	 * Unique customer ID for customer identification
	 */
	private int _customerID;
	
	/**
	 * Contact Number for this customer
	 */
	private int _contactNumber;
	
	/**
	 * Indicates if the customer is already a member in the restaurant
	 */
	private boolean _isMember;
	
	/**
	 * Creates a new Customer with the given name, age, gender, customerID,
	 * contact number and the membership status
	 * 
	 * @param name The customer's name
	 * @param age  The customer's age
	 * @param gender The customer's gender
	 * @param customerID The unique ID used to identify this customer
	 * @param contactNumber The customer's contact number
	 * @param isMember Indicates if customer is already a member in the restaurant
	 */
	public Customer(String name, int age, Gender gender,
			int customerID, int contactNumber, boolean isMember) {
		
		super(name, age, gender);
		
		_customerID = customerID;
		_contactNumber = contactNumber;
		_isMember = isMember;
	}
	
	/**
	 * Gets the customer ID of this customer<br>
	 * Used to <u>uniquely identify</u> each customer
	 * 
	 * @return this Customer's customer ID
	 */
	public int getCustomerID() {
		return _customerID;
	}
	
	/**
	 * Changes the customer ID of this customer
	 * 
	 * @param newCustomerID This customer's newCustomerID
	 */
	public void setCustomerID(int newCustomerID) {
		_customerID = newCustomerID;
	}
	
	/**
	 * Gets the contact number of this customer
	 * 
	 * @return this Customer's contact number
	 */
	public int getContactNumber() {
		return _contactNumber;
	}
	
	/**
	 * Changes the contact number of this customer
	 * 
	 * @param newContactNumber This customer's new contact number
	 */
	public void setContactNumber(int newContactNumber) {
		_contactNumber = newContactNumber;
	}
	
	/**
	 * Check if the customer is already a member in the restaurant
	 * 
	 * @return true if customer is a member
	 */
	public boolean checkMembership() {
		return _isMember;
	}
	
	/**
	 * Changes membership details of this customer
	 * 
	 * @param isMember This customer's new membership status
	 */
	public void setMembership(boolean isMember) {
		_isMember = isMember;
	}
	
	/**
	 * Display customer details with proper formatting<br>
	 * Displayed information includes name, age, gender, customer ID,
	 * contact number and the membership status
	 */
	public void displayCustomerDetails() {
		System.out.printf("%-30s", "Name: " + getName());
		System.out.printf("%-30s", "Age: " + getAge());
		System.out.printf("%-20s%n", "Gender: " + 
				getGender().toStrValue());
		System.out.printf("%-30s", "Customer ID: " + getCustomerID());
		System.out.printf("%-30s", "Contact Number: " + 
				getContactNumber());
		System.out.printf("%-20s%n", "Is Member: " + 
				(checkMembership() ? "Yes" : "No"));
	}
	
	/**
	 * Display customer name, contact number and membership status in a single line
	 * <br>Provides a summary of this customer
	 */
	public void displayCustomerSummary() {
		System.out.printf("%-30s", getName());
		System.out.printf("%-30s", getContactNumber());
		System.out.printf("%-20s%n", (checkMembership() ? "Yes" : "No"));
	}
}
