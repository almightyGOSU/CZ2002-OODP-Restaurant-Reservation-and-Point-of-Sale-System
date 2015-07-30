package sce.cz2002.Assignment.Classes;

import java.text.DecimalFormat;

/**
 * A staff is someone who works in the restaurant<br>
 * A staff is also a {@link Person}
 * 
 * @author Alvin Ng Keng Hian
 *
 */
public class Staff extends Person
{
	/**
	 * Generated serial version ID for serializable classes
	 */
	private static final long serialVersionUID = -4817599530795176367L;

	/**
	 * Enumeration type used to determine the job title of the staff
	 * 
	 * @author Alvin Ng
	 */
	public static enum JobTitle
	{
		Waiter("Waiter"),
		Cashier("Cashier"),
		Cleaner("Cleaner"),
		LineCook("Line Cook"),
		SousChef("Sous Chef"),
		Chef("Chef");
		
		private final String value;
		  
		private JobTitle(String value) {
			this.value = value;
		}
		
		public String toStrValue() {
			return value;
		}
	}
	
	/**
	 * The staff ID used to uniquely identify this staff
	 */
	private int _staffID;
	
	/**
	 * The job title of this staff
	 */
	private JobTitle _jobTitle;
	
	/**
	 * The salary of this staff
	 */
	private double _salary;
	
	
	/**
	 * Creates a new Staff with the given name, age, gender, 
	 * staff ID, job title and salary
	 * 
	 * @param name this Staff's name
	 * @param age this Staff's age
	 * @param gender this Staff's gender
	 * @param staffID this Staff's staff ID
	 * @param jobTitle this Staff's job title
	 * @param salary this Staff's salary
	 */
	public Staff(String name, int age, Gender gender,
			int staffID, JobTitle jobTitle, double salary) {
		
		super(name, age, gender);
		
		_staffID = staffID;
		_jobTitle = jobTitle;
		_salary = salary;
	}
	
	/**
	 * Gets the staffID of this Staff
	 * 
	 * @return this Staff's ID
	 */
	public int getStaffID() {
		return _staffID;
	}
	
	/**
	 * Changes the staff ID for this Staff
	 * 
	 * @param newStaffID This Staff's new staff ID
	 */
	public void setStaffID(int newStaffID) {
		_staffID = newStaffID;
	}
	
	/**
	 * Gets the job title of this Staff
	 * 
	 * @return this Staff's job title
	 */
	public JobTitle getJobTitle() {
		return _jobTitle;
	}
	
	/**
	 * Changes the job title for this Staff
	 * 
	 * @param newJobTitle This Staff's new job title
	 */
	public void setJobTitle(JobTitle newJobTitle) {
		_jobTitle = newJobTitle;
	}
	
	/**
	 * Gets the salary for this Staff
	 * 
	 * @return This Staff's salary
	 */
	public double getSalary() {
		return _salary;
	}
	
	/**
	 * Changes the salary for this Staff
	 * 
	 * @param newSalary This Staff's new salary
	 */
	public void setSalary(double newSalary) {
		_salary = newSalary;
	}
	
	/**
	 * Display staff details with proper formatting<br>
	 * Displayed information include name, age, gender, staff ID,
	 * job title and salary
	 */
	public void displayStaffDetails() 
	{
		System.out.printf("%-30s", "Name: " + getName());
		System.out.printf("%-30s", "Age: " + getAge());
		System.out.printf("%-20s%n", "Gender: " + 
				getGender().toStrValue());
		System.out.printf("%-30s", "Staff ID: " + getStaffID());
		System.out.printf("%-30s", "Job Title: " + 
				getJobTitle().toStrValue());
		System.out.printf("%-20s%n", "Salary: " + 
				new DecimalFormat("$###,##0.00").format(getSalary()));
	}
	
	/**
	 * Display staff name, job title and salary in a single line<br>
	 * Provides a summary of this particular Staff
	 */
	public void displayStaffSummary()
	{
		System.out.printf("%-30s", getName());
		System.out.printf("%-30s", "[" + getJobTitle().toStrValue() + "]");
		System.out.printf("%-20s%n",
				new DecimalFormat("$###,##0.00").format(getSalary()));
	}
}