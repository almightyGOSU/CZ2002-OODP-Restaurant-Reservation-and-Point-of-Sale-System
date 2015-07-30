package sce.cz2002.Assignment.Control;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Main entry point for the restaurant reservation &
 * point of sales application
 * <p>
 * Displays the list of submenus, invokes other control classes to
 * provide the relevant functionalities
 * 
 * @author Jin Yao
 *
 */
public class rrpssApplication
{
	/**
	 * Enumeration used to identify different application states
	 * 
	 * @author Jin Yao
	 *
	 */
	public enum ApplicationState
	{
		MainMenu,
		Menu,
		Order,
		RestaurantManagement,
		HRManagement,
		SaleRevenueReport,
		ExitApplication
	}
	
	/**
	 * A private static application state indicating
	 * the current application state
	 */
	private static ApplicationState currAppState = ApplicationState.MainMenu;
	
	/**
	 * Standard Java Scanner object used for processing inputs
	 */
	private static Scanner sc = new Scanner(System.in);
	
	/**
	 * Main function for the application<p>
	 * 
	 * Uses a do-while loop and switch statement to perform
	 * the relevant program logic
	 * 
	 * @param args Additonal arguments to be passed in
	 */
	public static void main(String[] args)
	{
		System.out.println("Initalizing system.. Loading data files..\n");
		
		doSystemInitialization();
		
		System.out.println("\nSystem initialization completed!");
		
		System.out.println("\nWelcome to the Restaurant Reservation & "
				+ "Point of Sales Application!");
		
		do
		{
			switch(currAppState)
			{
			case MainMenu:
				displayMainMenu();	
				currAppState = ApplicationState.values()[getMainMenuChoice()];
				break;
				
			case Menu:
				if(MenuMgr.getMenuMgr().getMenuChoice() == 0)
				{
					// Return to main menu
					currAppState = ApplicationState.values()[0];
				}
				break;
				
			case Order:
				if(OrderMgr.getOrderMgr().getOrderChoice() == 0)
				{
					// Return to main menu
					currAppState = ApplicationState.values()[0];
				}
				break;
				
			case RestaurantManagement:
				if(RestaurantMgr.getRestaurantMgr().
						getRestaurantManagementChoice() == 0)
				{
					// Return to main menu
					currAppState = ApplicationState.values()[0];
				}
				break;
				
			case HRManagement:
				if(HumanResourceMgr.getHRMgr().getHRManagementChoice() == 0)
				{
					// Return to main menu
					currAppState = ApplicationState.values()[0];
				}
				break;
			
			case SaleRevenueReport:
				OrderMgr.getOrderMgr().displaySaleRevenue();
				
				// Return to main menu
				currAppState = ApplicationState.values()[0];
				break;
				
			case ExitApplication:
				currAppState = ApplicationState.ExitApplication;
				break;
			}
			
		} while (currAppState != ApplicationState.ExitApplication);
		
		saveSystemState();
		
		System.out.println("\nThank you for using the application.");
	}

	/**
	 * Displays the list of available submenus<p>
	 * List of submenus include "Menu", "Order", "Restaurant Managment",
	 * <br>"Human Resource Management" and "Sales Revenue Report"
	 */
	public static void displayMainMenu()
	{
		System.out.println("\n" + new String(new char[50]).replace("\0", "="));
		System.out.print("|" + new String(new char[20]).replace("\0", " "));
		System.out.print("Main Menu");
		System.out.println(new String(new char[19]).replace("\0", " ") + "|");
		System.out.println(new String(new char[50]).replace("\0", "="));
		
		System.out.println("1. View the 'Menu' submenu");
		System.out.println("2. View the 'Order' submenu");
		System.out.println("3. View the 'Restaurant Management' submenu");
		System.out.println("4. View the 'Human Resource Management' submenu");
		System.out.println("5. View the 'Sales Revenue Report' submenu");
		System.out.println("6. Exit the application");
	}
	
	/**
	 * Process the user's input to determine the application state
	 * 
	 * @return The integer representing a valid application state
	 */
	public static int getMainMenuChoice()
	{
		int maxChoices = ApplicationState.values().length - 1;
		int mainMenuChoice = 0;
		
		do
		{
			try
			{
				System.out.printf("%nPlease enter your choice (1-%d): ",
						maxChoices);
				mainMenuChoice = sc.nextInt();
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
			
			// Valid mainMenuChoice from 1 to maxChoices
			if(mainMenuChoice < 1 || mainMenuChoice > maxChoices)
				System.out.println("Invalid choice! Please try again..");
			
		} while (mainMenuChoice < 1 || mainMenuChoice > maxChoices);
		
		return mainMenuChoice;
	}
	
	/**
	 * Loads all the required data files (if they exist)<p>
	 * Task is delegated to the different managers
	 */
	private static void doSystemInitialization()
	{
		MenuMgr.getMenuMgr().loadMenu();
		HumanResourceMgr.getHRMgr().loadPeople();
		RestaurantMgr.getRestaurantMgr().loadRestaurant();
		OrderMgr.getOrderMgr().loadOrders();
	}
	
	/**
	 * Save all the necessary data files<br>
	 * Task is delegated to the different managers<p>
	 * 
	 * User is given the option to save/not save the current session
	 */
	private static void saveSystemState()
	{
		int saveDataChoice = 0;
		do
		{
			try
			{
				System.out.println("\nWould you like to save the changes"
						+ " made during the current session?");
				System.out.print("\nPlease enter your choice [(1) Yes (2) No ]: ");
				
				saveDataChoice = sc.nextInt();
				sc.nextLine();
			}
			catch(InputMismatchException ex1)
			{
				System.out.println("Invalid input! Please try again..");
				sc.nextLine(); // Clear the garbage input
				continue;
			}
			catch(Exception ex2)
			{
				System.out.println("Invalid input! Please try again..");
				sc.nextLine(); // Clear the garbage input
				continue;
			}
			
			if(saveDataChoice < 1 || saveDataChoice > 2)
				System.out.println("Invalid choice! Please try again..");
			
		} while (saveDataChoice < 1 || saveDataChoice > 2);
		
		if(saveDataChoice == 1)
		{
			System.out.println();
			
			MenuMgr.getMenuMgr().saveMenu();
			HumanResourceMgr.getHRMgr().savePeople();
			RestaurantMgr.getRestaurantMgr().saveRestaurant();
			OrderMgr.getOrderMgr().saveOrders();
		}
	}
}