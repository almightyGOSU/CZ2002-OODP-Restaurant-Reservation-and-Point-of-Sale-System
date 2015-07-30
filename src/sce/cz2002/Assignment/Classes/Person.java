package sce.cz2002.Assignment.Classes;

import java.io.Serializable;

/**
 * Represents a person who is at the restaurant<br>
 * A person can be working or patronising the restaurant
 * 
 * @author Alvin Ng Keng Hian
 *
 */
public class Person implements Serializable
{
	/**
	 * Generated serial version ID for serializable classes
	 */
	private static final long serialVersionUID = 5997970737618137343L;

	/**
	 * Enumeration type used to determine the gender of the person
	 * 
	 * @author Alvin Ng
	 *
	 */
	public static enum Gender
	{
		Male("Male"),
		Female("Female");
		
		private final String value;
		  
		private Gender(String value) {
			this.value = value;
		}
		
		public String toStrValue() {
			return value;
		}
	}
	
	/**
	 * The name of this person
	 */
	private String _name;
	
	/**
	 * The age of this person
	 */
	private int _age;
	
	/**
	 * The gender of this person
	 */
	private Gender _gender;
	
	/**
	 * Creates a new Person with the given name, age and gender
	 * 
	 * @param name This Person's name
	 * @param age This Person's age
	 * @param gender This Person's gender
	 */
	public Person(String name, int age, Gender gender)
	{
		_name = name;
		_age = age;
		_gender = gender;
	}
	
	/**
	 * Gets the name of this Person
	 * 
	 * @return this Person's name
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Changes the name of this Person
	 * 
	 * @param newName This Person's new name
	 */
	public void setName(String newName) {
		_name = newName;
	}
	
	/**
	 * Gets the age of this Person
	 * 
	 * @return this Person's age
	 */
	public int getAge() {
		return _age;
	}
	
	/**
	 * Changes the age of this Person
	 * 
	 * @param newAge This Person's new age
	 */
	public void setAge(int newAge) {
		_age = newAge;
	}
	
	/**
	 * Gets the gender of this Person
	 * 
	 * @return this Person's gender
	 */
	public Gender getGender() {
		return _gender;
	}
	
	/**
	 * Changes the gender of this Person
	 * 
	 * @param newGender This Person's new gender
	 */
	public void setGender(Gender newGender) {
		_gender = newGender;
	}
}
