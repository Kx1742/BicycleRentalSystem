import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class BicycleRentalSystemTest
{
	static Scanner input = new Scanner(System.in);
	static ArrayList <Customer> customer = new ArrayList<Customer> ();
	static ArrayList <Rental> rental = new ArrayList<Rental> ();
	static ArrayList <Bicycle> bicycle = new ArrayList<Bicycle> ();
	static ArrayList <Staff> staff = new ArrayList<Staff> ();
	public static void main(String[] args) throws FileNotFoundException 
	{
		//read all the file needed
		readCustomerFile();
		readBicycle();
		readStaffFile();
		readRentalDetails();

		boolean found = false;
		int no = -1;
		do 
		{
			try 
			{
				System.out.println("------------------------------------------------------------------");
				System.out.println("\t   ~ WELCOME TO HOURLY BICYCLE RENTAL SYSTEM ~");
				System.out.println("------------------------------------------------------------------");
				System.out.println("		         | LOGIN ACCOUNT |");
				System.out.println("------------------------------------------------------------------\n");
				System.out.println("		         1. STAFF\n");
				System.out.println("		         2. CUSTOMER\n");
				System.out.println("		         3. EXIT SYSTEM\n");
				System.out.println("==================================================================");
				System.out.print("		        Option: ");
				int loginOpt = input.nextInt(); //get input from user
				input.nextLine();

				if (loginOpt == 3)
				{
					no = 0;
					saveToRentalFile();
					saveBicycle();
					saveToCustomerFile();
					System.out.println(" The information has been saved to file...");
					System.out.println("============== Exiting Hourly Bicycle Rental System ==============");
					System.exit(0);
				}
				else if (loginOpt == 1 || loginOpt == 2)
				{
					System.out.println("------------------------------------------------------------------");
					System.out.println("\t   ~ WELCOME TO HOURLY BICYCLE RENTAL SYSTEM ~");
					System.out.println("------------------------------------------------------------------");
					System.out.println("		         | LOGIN ACCOUNT |");
					System.out.println("------------------------------------------------------------------");
					System.out.println("		   Please provide your...\n");
					System.out.print("	   User ID       : ");
					String userID = input.nextLine(); //get userID
					System.out.print("	   User password : ");
					String userPassword = input.nextLine(); //get userPassword
					System.out.println("==================================================================");

					if (loginOpt == 1) //if option 1 chosen, proceed to administrator login
					{
						no = loginStaff(userID, userPassword);
						if (no >= 0) //if the ID found in database
						{
							staffMenu(no); //display administrator menu
						}
						else
							System.out.println(" Please enter valid ID.");
					}
					else if (loginOpt == 2) //proceed option 2 which is customer login
					{
						no = loginCustomer(userID, userPassword); //if found, will return no >= 0

						int cusMenuOpt = 0;
						if (no >= 0)
						{
							do 
							{
								try 
								{
									Customer cc = new Customer(customer.get(no));
									cc.customerMenu(); //display customer menu
									cusMenuOpt = input.nextInt();
									if (cusMenuOpt == 0) //if option 0 chosen, exit customer interface
									{
										System.out.println(" Logout customer account...");
										no = -1;
									}
									if (cusMenuOpt == 1) //if option 1 chosen, display customer personal details
									{
										cc.displayCustomerDetails(); //display personal details

									}
									else if (cusMenuOpt == 2) //if option 2 chosen, update customer personal details
									{	
										updateCustomerPersDetails(no); //update personal details						
									}
									else if (cusMenuOpt == 3) //if option 3 chosen, display bicycle list
									{							
										input.nextLine();
										viewBicycleList();
									}
									else if (cusMenuOpt == 4) //if option 4 chosen, place rental order
									{
										placeRental(no);
									}
									else if (cusMenuOpt == 5) //if option 5 chosen, display the rental history
									{
										displayCustomerRentalHistory(no);

										boolean valid = true;
										do 
										{
											try 
											{
												System.out.println(" ( 0-Quit	1-Search rental )");
												System.out.print(" Option: ");
												int opt = input.nextInt();
												if (opt == 1)
												{
													searchRental(); //if option 1 chosen, proceed search rental function
												}
												if(opt != 0 && opt != 1)
												{
													valid = false;
													System.out.println(" Please enter correct option.");
												}
												else
													valid = true;
											}
											catch(InputMismatchException exp)
											{
												System.out.println(" Please enter correct option."); 
												valid = false;
												input.nextLine();
											}
										}while(valid == false);								
									}
								}
								catch(InputMismatchException exp)
								{
									System.out.println(" Please enter correct option.");
									cusMenuOpt = 1;
									input.nextLine();
								}

							}while(cusMenuOpt != 0);
						}
						else
						{
							int regOpt = 0;
							System.out.println(" No customer found.");
							System.out.println(" Would you like to register a new account? ( 1 = Yes  0 = No)\n");
							System.out.print(" Option: ");
							regOpt = input.nextInt();

							if(regOpt == 1)
							{
								registerCustomer();
							}
							else
								no = -1;
						}
					}					
				}
				else
				{
					System.out.println(" Error option entered.");
					no = -1;
				}
			}
			catch(InputMismatchException i)
			{
				System.out.println(" Please enter correct option.");
				input.nextLine();
				no = -1;
			}
		}while(found == false || no == -1 );
	}

	//Read Administrator information from the file
	public static void readStaffFile()
	{
		String source="Staff.txt"; //put file name here
		String line = "";
		try 
		{
			BufferedReader br = new BufferedReader (new FileReader (source)); //read in file--create obj
			while ((line = br.readLine()) != null) //file line not empty--go into end
			{
				String [] staffList = line.split("#"); //split line by '#'
				Staff a1 = null;
				String staffName = staffList[0];
				String staffID = staffList[1];
				String password = staffList[2];
				String jobTitle = staffList[3];

				a1 = new Staff(staffName, staffID, password, jobTitle);
				staff.add(a1);
			}	
			br.close();
		} 
		catch (FileNotFoundException exp) //print out error if the file is not found
		{
			exp.printStackTrace();
		}
		catch (IOException exp) //line end
		{
			exp.printStackTrace();
		}
	}

	//Read in all rental data from file
	public static void readRentalDetails() 
	{
		String source = "Rental.txt"; //put file name here
		String line = "";
		try 
		{
			BufferedReader br = new BufferedReader (new FileReader (source)); //read in file--create object
			while ((line = br.readLine()) != null) //file line not empty--go into end
			{
				String [] rentalLine = line.split("#");//split line by '#'
				Rental r = null;

				String placeRentalDate = rentalLine[0];
				int rentalID = Integer.parseInt(rentalLine[1]);
				int rentalHour = Integer.parseInt(rentalLine[2]);
				String rentalDate = rentalLine[3];
				String customerID = rentalLine[4];

				Customer c = findCustomer(customerID);
				String staffID = rentalLine[5];
				Staff a = findStaff(staffID);
				String status = rentalLine[6];
				int noOfBicycleOrder = Integer.parseInt(rentalLine[7]);
				r = new Rental(placeRentalDate, rentalID, rentalHour, rentalDate, c, status,staffID, noOfBicycleOrder);
				r.setStaff(a);
				rental.add(r);

				String [] bicycleListID = new String[noOfBicycleOrder];
				for(int j = 0; j < noOfBicycleOrder; j++)
				{
					bicycleListID[j] = rentalLine[8+j];
				}

				Bicycle b = null;
				for(int j = 0; j < noOfBicycleOrder; j++)
				{
					boolean found = false;
					do
					{
						int y = 0;
						while(y < bicycle.size() && !found)
						{
							b = bicycle.get(y);
							if(b.getID().equals(bicycleListID[j]))
							{
								found = true;
								break;
							}
							else
								y++;
						}

					}while(found != true);
					r.addBicycleFromFile(j,b);
				}			
			}	
			br.close();
		} 
		catch (FileNotFoundException exp) //print out error if the file is not found
		{
			exp.printStackTrace();
		}
		catch (IOException exp) //line end
		{
			exp.printStackTrace();
		}
	}

	//read in all bicycle data from file
	@SuppressWarnings("unchecked")
	public static void readBicycle() 
	{
		try
		{
			FileInputStream bicycleFileIn = new FileInputStream("Bicycle.ser");
			ObjectInputStream in = new ObjectInputStream(bicycleFileIn);		
			bicycle = (ArrayList<Bicycle>)in.readObject();
			in.close();
			bicycleFileIn.close();

		}
		catch (IOException i) 
		{
			i.printStackTrace();
			return;
		}
		catch (ClassNotFoundException c) 
		{
			System.out.println("Bicycle class not found");
			c.printStackTrace();
			return;
		}
	}

	//read customer data from file
	public static void readCustomerFile()
	{
		String source = "Customer.txt"; //put file name here
		String line = "";
		try 
		{
			BufferedReader br = new BufferedReader (new FileReader (source)); //read in file--create obj
			while ((line = br.readLine()) != null) //file line not empty--go into end
			{				
				String [] c=line.split("#");//split line by comma
				Customer c1 = null;				
				String customerID = c[0];
				String customerName = c[1];
				String password = c[2];
				char gender = c[3].charAt(0);
				String date = c[4];
				int teleNo = Integer.parseInt(c[5]);
				String email = c[6];
				String address = c[7];
				c1 = new Customer(customerID, customerName, password, gender, date, teleNo, email, address);				

				customer.add(c1);
			}	
			br.close();
		} 
		catch (FileNotFoundException exp) //print out error if the file is not found
		{
			exp.printStackTrace();
		}
		catch (IOException exp) //line end
		{
			exp.printStackTrace();
		}
	}

	//login to the system with administrator ID and password
	public static int loginStaff(String userID, String userPassword)
	{
		boolean found = false;
		int no = -100;
		int i = 0;
		Staff stf = null;
		while(found == false && i < staff.size())
		{
			stf = staff.get(i); //get the first administrator for matching
			if(stf.getID().equals(userID) && stf.getPassword().equals(userPassword)) //find whether the input match with database or not
			{
				found = true; //return true if match with input(for checking only)
				System.out.println(" Valid user. WELCOME TO HOURLY BIKE RENTAL SYSTEM. ");
				no = i;
			}
			else i++;
		}
		return no;
	}

	//login to the system with customer ID and password
	public static int loginCustomer(String userID,String userPassword)
	{
		boolean found = false;
		int i = 0;
		Customer c = null;
		while(found == false && i < customer.size())
		{
			c = customer.get(i); //get the first customer for matching
			if(c.getID().equals(userID) && c.getPassword().equals(userPassword)) //find whether the input match with database or not
			{
				found = true; //return true if match
				System.out.println(" Valid user. WELCOME TO HOURLY BIKE RENTAL SYSTEM. ");
			}
			else i++; //repeat until match
		}
		if(found)
			return i; //return the number
		else return -1;
	}

	// Register new customer into the system
	public static void registerCustomer() 
	{
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t   ~ WELCOME TO HOURLY BICYCLE RENTAL SYSTEM ~");
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t\t   | CUSTOMER REGISTRATION |");
		System.out.println("------------------------------------------------------------------");
		Scanner input = new Scanner(System.in);
		//request customer ID from user with validation
		boolean validID = false;
		String customerID;
		do 
		{
			System.out.printf(" (CXXXX\tX - Digits)\n Enter customer ID : ");
			customerID = input.nextLine().toUpperCase();
			validID = isValidID(customerID); //customerID validation
		} while (validID == false);
		//request customer name from user with validation
		boolean validName = false;
		String customerName;
		do 
		{
			System.out.printf(" Enter customer name : ");
			customerName = input.nextLine();
			validName = isValidName(customerName); //customer name validation
		} while (validName == false);
		//request customer password from user with validation
		boolean validPassword = false;
		String customerPassword;
		do 
		{
			System.out.printf(" Password Can Only Be 6-22 Characters.\n Enter customer password  : ");
			customerPassword = input.nextLine();
			validPassword = isValidPassword(customerPassword); //password validation
		} while (validPassword == false);
		//request customer gender from user with validation
		boolean validGender = false;
		char gender;
		do 
		{
			System.out.printf(" (M-Male\tF-Female\tO-Others/Unspecified)\n Enter customer gender   : ");
			gender = input.next().toUpperCase().charAt(0);
			validGender = isValidGender(gender); //gender validation
		} while (validGender == false);
		input.nextLine();
		//request customer DOB from user with validation
		boolean validDate = false;
		String dateOfBirth = null;
		do {
			System.out.printf(" Enter customer birthday date (dd/mm/yyyy): ");
			dateOfBirth = input.nextLine();
			validDate = isValidDate(dateOfBirth); //date validation
		} while (validDate == false);
		//request customer TelNo from user with validation
		boolean validTeleNo = false;
		int teleNo = 0;
		do 
		{
			System.out.printf(" Enter customer Tel.No (+60)  : ");
			try 
			{
				teleNo = Integer.parseInt(input.nextLine());
				validTeleNo = isValidTeleNo(teleNo); //telephone no validation
			}
			catch (Exception e) 
			{
				System.out.println(" Please Only Enter Digits");
			}
		} while (validTeleNo == false);
		//request customer email from user with validation
		boolean validEmail = false;
		String email;
		do 
		{
			System.out.printf(" Enter customer email address : ");
			email = input.nextLine();
			validEmail = isValidEmail(email); //email validation
		} while (validEmail == false);
		//request customer address from user with validation
		boolean validAddress = false;
		String address;
		do 
		{
			System.out.printf(" Enter customer address  : ");
			address = input.nextLine();
			validAddress = isValidAddress(address); //address validation
		} while (validAddress == false);
		System.out.println("==================================================================");

		Customer c1 = new Customer(customerID, customerName, customerPassword, gender, dateOfBirth, teleNo, email, address);
		customer.add(c1);
	}

	//Display administrator menu
	public static void staffMenu(int no)
	{
		Scanner input = new Scanner(System.in);
		int staffOpt = 0;
		do
		{
			try 
			{
				System.out.println("------------------------------------------------------------------");
				System.out.println("\t   ~ WELCOME TO HOURLY BICYCLE RENTAL SYSTEM ~");
				System.out.println("------------------------------------------------------------------");
				System.out.println("\t\t\t| STAFF MENU |");
				System.out.println("------------------------------------------------------------------\n");
				System.out.println("\t\t   1. View Staff List\n");
				System.out.println("\t\t   2. View Customer List\n");
				System.out.println("\t\t   3. Delete Customer\n");
				System.out.println("\t\t   4. View Rental Order List\n");
				System.out.println("\t\t   5. Update Rental Order Details\n");
				System.out.println("\t\t   6. View Bicycle List\n");
				System.out.println("\t\t   7. Add Bicycle Data\n");
				System.out.println("\t\t   8. Delete Bicycle Data\n");
				System.out.println("\t\t   9. Update Bicycle Data\n");
				System.out.println("==================================================================");
				System.out.println(" Enter 0 to quit. ");
				System.out.print(" Option: ");
				staffOpt = input.nextInt();
				if (staffOpt == 0) //if option 0 chosen, exit administrator interface
				{
					System.out.println(" Logout staff account...");
				}
				if (staffOpt == 1) //if option 1 chosen, display administrator list
				{
					viewStaffList(); //display administrator list
				}
				else if (staffOpt == 2) //if option 2 chosen, display customer list
				{
					viewCustomerList(); //display customer list
				}
				else if (staffOpt == 3) //if option 3 chosen, delete customer 
				{
					deleteCustomer();
				}
				else if (staffOpt == 4) //if option 4 chosen, display rental list of all customer
				{
					viewRentalList();
				}
				else if (staffOpt == 5) //if option 5 chosen, update the rental order
				{
					updateRental(no);
				}
				else if (staffOpt == 6) //if option 6 chosen, display bicycle list
				{
					viewBicycleList();
				}
				else if (staffOpt == 7) //if option 7 chosen, add bicycle data
				{
					addBicycle();
				}
				else if (staffOpt == 8) //if option 8 chosen, delete bicycle data
				{
					deleteBicycle();
				}
				else if (staffOpt == 9) //if option 9 chosen, update bicycle details
				{
					updateBicycle();
				}
			}
			catch(InputMismatchException i)
			{
				System.out.println(" Please enter correct option.");
				input.nextLine();
				staffOpt = 1;
			}
		}while(staffOpt != 0);
	}

	//Display administrator list for administrator view only
	public static void viewStaffList()
	{
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t   ~ WELCOME TO HOURLY BICYCLE RENTAL SYSTEM ~");
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t\t\t| STAFF LIST |\t\tTotal: " + staff.size());
		System.out.println("------------------------------------------------------------------");
		System.out.println("  No.  | Staff ID |\t   Name   	| Password |\tRole\t|");
		System.out.println("------------------------------------------------------------------");

		for(int i = 0; i < staff.size();i++)
		{
			System.out.printf("  %-2d.\t %-5s\t\t%-15s\t  %-8s\t%-10s\n", (i+1), staff.get(i).getID(), staff.get(i).getName(), staff.get(i).getPassword(),staff.get(i).getJobTitle());
		}
		System.out.println("==================================================================\n");
	}

	//Display customer list for administrator only
	public static void viewCustomerList()
	{
		Scanner input = new Scanner(System.in);
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t   ~ WELCOME TO HOURLY BICYCLE RENTAL SYSTEM ~");
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t\t       | CUSTOMER LIST |\t\tTotal: " + customer.size());
		System.out.println("------------------------------------------------------------------");
		System.out.println("  No.  |     ID     |        Customer Name        |  TelNo (+60) | ");
		System.out.println("------------------------------------------------------------------");
		for(int i = 0; i < customer.size();i++)
		{
			System.out.printf("  %-2d.     %-8s\t%-25s    %-11d\n" , i+1, customer.get(i).getID() , customer.get(i).getName(), customer.get(i).getTelNo());
		}
		System.out.println("==================================================================");

		boolean found = false;
		int option = 0;
		do 
		{
			found = searchCustomer();
			if(found == false)
				System.out.println(" Customer ID not found . Please enter again .");
			boolean valid = true;
			do 
			{
				try 
				{
					System.out.print(" Do you want to search Customer ID again ?( 1 = Yes  0 = No)\n");
					System.out.print(" Option: ");
					option = input.nextInt();
					if (option == 0||option == 1)
						valid = true;
					else
						valid = false;
				}
				catch(InputMismatchException exp)
				{
					System.out.println(" Please enter correct option.");
					input.nextLine();
					valid = false;
				}
			}while(valid == false);
		}while(option != 0);
	}

	//Delete customer
	public static void deleteCustomer()
	{

		String userID;
		boolean found = false;
		do 
		{
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t   ~ WELCOME TO HOURLY BICYCLE RENTAL SYSTEM ~");
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t\t   | Delete Customer Data |");
			System.out.println("------------------------------------------------------------------");
			System.out.print(" Enter customer ID to delete : ");
			userID = input.nextLine();

			int i = 0;
			Customer c = null;
			while(found == false && i < customer.size())
			{ 
				int delete=2;
				c = customer.get(i); //get the first customer for matching
				if(c.getID().equals(userID) ) //find whether the input match with database or not
				{
					found = true;
					c.displayCustomerDetails();
					boolean validOption = true;
					do {
						try {
							System.out.println(" Do you want to delete this user ?( 1 = Yes  0 = No)");
							System.out.print(" Option: ");
							delete = input.nextInt();
							if(delete != 0 && delete != 1)
							{
								validOption = false;
								System.out.println(" Error: Please enter correct input");
							}
							else
								validOption = true;
						}catch(InputMismatchException exp)
						{
							validOption = false;
							System.out.println(" Error: Please enter correct input");
							input.nextLine();
						}
					}while(validOption == false);
					//Check whether the customer ID is in any rental order or not, if yes, cannot delete customer
					boolean inRental = false;
					int j = 0;
					Rental r = null;
					while(inRental == false && j < rental.size())
					{
						r = rental.get(j);
						if(r.getCustomer().getID().contentEquals(userID))
						{
							inRental = true;
							System.out.println(" You are not allowed to delete Customer ID that contains in rental.");
						}
						else
							j++;
					}

					if(delete == 1&&inRental==false) //if option 1 chosen, delete the customer
					{	
						customer.remove(i); 
						System.out.println(" Customer data has been deleted.");
					}
					break;
				}
				else i++; //repeat until match
			}
			if(found != true)
			{
				System.out.println(" Customer ID is not in the list . Please enter again ");
				i = 100;
			}
		}while(found != true);
	}

	//display all the rental list 
	public static void viewRentalList()
	{
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t   ~ WELCOME TO HOURLY BICYCLE RENTAL SYSTEM ~");
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t\t    | RENTAL LIST |\t\t");
		System.out.println("------------------------------------------------------------------");
		System.out.println("  No.  |\tRental ID\t|\tCustomer ID\t|");
		System.out.println("------------------------------------------------------------------");
		for(int i = 0;i < rental.size();i++)
		{		
			System.out.printf("  %-2d.\t\t%-4d\t\t\t%-6s\n", (i+1),  rental.get(i).getRentalID(), rental.get(i).getCustomer().getID());
		}

		System.out.println("==================================================================\n");

		int option = 1;
		do 
		{
			searchRental();
			boolean valid = true;
			do 
			{
				try 
				{
					System.out.print(" Do you want to search Rental ID again ?( 1 = Yes  0 = No)\n");
					System.out.print(" Option: ");
					option = input.nextInt();
					if (option == 0 || option == 1)
						valid = true;
					else
						valid = false;
				}
				catch(InputMismatchException exp)
				{
					System.out.println(" Please enter correct option.");
					input.nextLine();
					valid = false;
				}
			}while(valid == false);
		}while(option == 1);
	}

	//update rental status
	public static void updateRental(int no)
	{		
		boolean found = false;
		boolean valid = true;
		int rentalID = 0;
		do
		{
			do 
			{
				try 
				{
					viewRentalList();
					System.out.println(" You are allow to view rental details before update.");
					System.out.print(" Enter Rental ID to update details: ");
					rentalID = input.nextInt();
					valid = true;			
				}
				catch(InputMismatchException exp)
				{
					System.out.println(" Please enter valid Rental ID.");
					valid = false;
					input.nextLine();
				}	
			}while(valid == false);

			int i = 0;
			Rental r = null;
			while(found == false && i < rental.size())
			{
				r = rental.get(i); //get the first customer for matching
				String r1 = Integer.toString(rentalID);
				String r2 = Integer.toString(r.getRentalID());

				if(r2.equals(r1) ) //find whether the input match with database or not
				{
					int updateRentOpt = 0;
					do 
					{
						try
						{
							System.out.println("\n\t~ Update Rental Details ~ ");
							System.out.println(" -------------------------------------------");
							System.out.println(" 1. Update Rental Status\n ");
							System.out.println(" 2. Update Staff ID\n ");
							System.out.println(" -------------------------------------------");
							System.out.println(" Enter 0 to exit Update Rental Function. ");
							System.out.print(" Option: ");
							updateRentOpt = input.nextInt();
							input.nextLine();
							if(updateRentOpt == 1)
							{
								boolean isValidStatus = true;
								do 
								{
									found = true;
									r.displayRentalOrderDetails();
									System.out.print(" Enter rental status : ");
									String rentalStatus = input.nextLine();
									if (isValidName(rentalStatus) == true)
										r.setStatus(rentalStatus);
									else
									{
										System.out.println(" Invalid status entered.");
										isValidStatus = false;
										break;
									}
								}while(isValidStatus != true);
							}
							else if(updateRentOpt == 2)
							{
								found = true;
								r.displayRentalOrderDetails();
								r.setStaff(staff.get(no));
								r.displayRentalOrderDetails();
								break;
							}
							else if(updateRentOpt == 0)
							{
								found = true;
								System.out.println(" Exit update rental function. ");
								rental.set(i,r);
								updateRentOpt = 0;
								break;
							}
							else
							{
								System.out.println(" Invalid update rental option entered. ");
								updateRentOpt = 1;
							}
						}
						catch(InputMismatchException e)
						{
							System.out.println(" Please enter a valid option.");
							input.nextLine();
						}
					}while (updateRentOpt != 0);
				}
				else i++; //repeat until match
			}
			if(found == false)
				System.out.println(" Can't found any rental order. Please enter again.");
		}while(found != true);	
	}

	//Display bicycle list for both customer and administrator view
	public static void viewBicycleList()
	{

		System.out.println("------------------------------------------------------------------");
		System.out.println("\t   ~ WELCOME TO HOURLY BICYCLE RENTAL SYSTEM ~");
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t\t    | Bicycle LIST |\t\tTotal: " + bicycle.size());
		System.out.println("------------------------------------------------------------------");
		System.out.println("  No.  |\tBicycle ID\t|\tBicycle Name\t\t|");
		System.out.println("------------------------------------------------------------------");

		for(int i = 0; i < bicycle.size();i++)
		{
			System.out.printf("  %-2d.\t\t%-6s\t\t\t%-15s\n", (i+1), bicycle.get(i).getID(), bicycle.get(i).getBicycleName());
		}
		System.out.println("==================================================================\n");

		boolean found = false;
		int option = 0;
		do 
		{
			found = searchBicycle();
			if(found == false)
				System.out.println(" Bicycle ID not found . Please enter again .");
			boolean valid = true;
			do 
			{
				try 
				{
					System.out.print(" Do you want to search Bicycle ID again ?( 1 = Yes  0 = No)\n");
					System.out.print(" Option: ");
					option = input.nextInt();
					input.nextLine();
					if (option == 0 || option == 1)
						valid = true;
					else
						valid = false;
				}
				catch(InputMismatchException exp)
				{
					System.out.println(" Please enter correct option.");
					input.nextLine();
					valid = false;
				}
			}while(valid == false);
		}while(option != 0);
	}

	//Add new bicycle into the array list
	public static void addBicycle()
	{
		Scanner inputBicycle = new Scanner(System.in);
		String bicycleName,bicycleID, bicycleType, bicycleDesc, bicycleSize;
		double bicyclePrice = 0;
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t   ~ WELCOME TO HOURLY BICYCLE RENTAL SYSTEM ~");
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t\t   | Add Bicycle |");
		System.out.println("------------------------------------------------------------------");
		//request bicycle ID from user with validation
		boolean validBikeID = false;
		do 
		{
			System.out.print(" (BXXXXX\tX - Digits)\n Enter Bicycle ID : ");
			bicycleID = inputBicycle.nextLine().toUpperCase();
			validBikeID = isValidBikeID(bicycleID); //bicycleID validation
		} while (validBikeID == false);
		//request customer name from user with validation
		boolean validBikeName = false;
		do 
		{
			System.out.print(" Enter Bicycle Name: ");
			bicycleName = inputBicycle.nextLine();
			validBikeName = isValidBikeName(bicycleName); //bicycle name validation
		} while (validBikeName == false);
		//request customer size from user with validation
		boolean validSize = false;
		do 
		{
			System.out.print(" (Bicycle Size: S, M or L)\n Enter Bicycle Size: ");
			bicycleSize = inputBicycle.nextLine().toUpperCase();
			validSize = isValidSize(bicycleSize); //bicycle size validation
		}while (validSize == false);
		boolean validBikeType = false;
		do
		{
			System.out.print(" Enter Bicycle Type: ");
			bicycleType = inputBicycle.nextLine();
			validBikeType = isValidType(bicycleType);
		}while (validBikeType == false);

		boolean validBikePrice = false;
		do
		{
			System.out.print(" Enter Bicycle Price (RM) : ");
			try
			{
				bicyclePrice = Double.parseDouble(input.nextLine());
				validBikePrice = true;
			}
			catch ( Exception e)
			{
				System.out.println(" Invalid Input. Please Enter a Number.");

			}
		}while (validBikePrice == false);

		boolean validDesc = false;
		do
		{
			System.out.print(" Enter Bicycle Description: ");
			bicycleDesc = inputBicycle.nextLine();
			validDesc = isValidDesc(bicycleDesc);
		}while (validDesc == false);
		System.out.println("==================================================================");

		Bicycle addBike = new Bicycle(bicycleName,bicycleID, bicycleType, bicycleDesc, bicycleSize, bicyclePrice);
		bicycle.add(addBike);
	}

	//Delete bicycle
	public static void deleteBicycle()
	{
		Scanner input = new Scanner (System.in);
		String bicycleID;
		Bicycle b = null;
		boolean found = false;
		do
		{
			int j = 0;
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t   ~ WELCOME TO HOURLY BICYCLE RENTAL SYSTEM ~");
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t\t   | Delete Bicycle Data |");
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t\t    | Bicycle LIST |\t\tTotal: " + bicycle.size());
			System.out.println("------------------------------------------------------------------");
			System.out.println("  No.  |\tBicycle ID\t|\tBicycle Name\t\t|");
			System.out.println("------------------------------------------------------------------");

			for(int i = 0; i < bicycle.size();i++)
			{
				System.out.printf("  %-2d.\t\t%-6s\t\t\t%-15s\n", (i+1), bicycle.get(i).getID(), bicycle.get(i).getBicycleName());
			}
			System.out.println("==================================================================\n");
			System.out.print(" Enter Bicycle ID to delete : ");
			bicycleID = input.nextLine().toUpperCase();
			while(j < bicycle.size() && !found)
			{
				b = bicycle.get(j);
				if(b.getID().equals(bicycleID)) //if the bicycle ID match with database
				{
					found = true;
					int delete = 2;
					boolean validOption=true;
					do {
						try {
							System.out.println(" Do you want to delete this bicycle ?( 1 = Yes  0 = No)");
							System.out.print(" Option: ");
							delete = input.nextInt();
							if(delete != 0 && delete != 1)
							{
								validOption = false;
								System.out.println(" Error: Please enter correct input");
							}
							else
								validOption = true;
						}catch(InputMismatchException exp)
						{
							validOption = false;
							System.out.println(" Error: Please enter correct input");
							input.nextLine();
						}
					}while(validOption == false);
					//Check whether the bicycle ID is in any rental order or not, if yes, cannot delete customer
					boolean inRental = false;
					int k = 0;
					Rental r = null;
					while(inRental == false && k < rental.size())
					{
						r = rental.get(k);
						Bicycle [] list = new Bicycle[r.getNoOfBicycle()];
						list = r.getBicycleRentalList();
						for(int l = 0; l < r.getNoOfBicycle(); l++)
						{
							if(list[l].getID().contentEquals(bicycleID))
							{
								inRental = true;
								System.out.println(" You are not allowed to delete Bicycle ID that contains in rental.");
							}
						}
						k++;
					}
					if(delete == 1 && inRental == false) //option 1 chosen to delete bicycle details
					{bicycle.remove(j);
					System.out.println(" Delete bicycle");
					}
				}
				else
					j++;
			}
			if(found != true)
			{
				System.out.println(" Bicycle ID is not in the list . Please enter again ");
				j = 100;
			}
		}while(found != true);
	}

	//update bicycle data
	public static void updateBicycle()
	{
		String bicycleID;	
		Bicycle b = null;
		boolean found = false;
		do
		{
			int j = 0;
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t   ~ WELCOME TO HOURLY BICYCLE RENTAL SYSTEM ~");
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t\t   | Update Bicycle Data |");
			System.out.println("\t\t    | Bicycle LIST |\t\tTotal: " + bicycle.size());
			System.out.println("------------------------------------------------------------------");
			System.out.println("  No.  |\tBicycle ID\t|\tBicycle Name\t\t|");
			System.out.println("------------------------------------------------------------------");

			for(int i = 0; i < bicycle.size();i++)
			{
				System.out.printf("  %-2d.\t\t%-6s\t\t\t%-15s\n", (i+1), bicycle.get(i).getID(), bicycle.get(i).getBicycleName());
			}
			System.out.println("==================================================================\n");
			System.out.print(" Enter Bicycle ID to update: ");
			bicycleID = input.nextLine().toUpperCase();
			while(j < bicycle.size() && !found)
			{
				b = bicycle.get(j);
				if(b.getID().equals(bicycleID))
				{
					b.displayBicycleDetails();
					found = true;
					boolean again = true;
					boolean valid = true;
					int option;
					do 
					{
						System.out.println("\t  Update bicycle details ");
						System.out.println("\t--------------------------");
						System.out.println(" 1. Update Bicycle Name\n ");
						System.out.println(" 2. Update Bicycle Type\n ");
						System.out.println(" 3. Update Bicycle Description\n ");
						System.out.println(" 4. Update Bicycle Size\n ");
						System.out.println(" 5. Update Bicycle Price per hour\n");
						System.out.println(" 6. Exit update\n");
						do 
						{
							try 
							{
								System.out.print(" Please enter your option : ");
								option = input.nextInt();
								switch (option)
								{
								case 1:
								{
									boolean validBikeName = false;
									do
									{
										input.nextLine();
										System.out.println(" Update Bicycle Name");
										System.out.println(" Original Bicycle Name : "+b.getBicycleName());
										System.out.print(" New Bicycle Name      : ");
										String bicycleName = input.nextLine();
										validBikeName = isValidBikeName(bicycleName);

										if (validBikeName == true)
										{	
											b.setName(bicycleName);
										}

									}while (validBikeName == false);					
									break;

								}
								case 2:
								{
									boolean validBikeType = false;
									do
									{
										input.nextLine();
										System.out.println(" Update Bicycle Type");
										System.out.println(" Original Bicycle Type : "+b.getType());
										System.out.print(" New Bicycle Type      : ");
										String bicycleType = input.nextLine();
										validBikeType = isValidType(bicycleType);

										if (validBikeType == true)
										{
											b.setType(bicycleType);
										}
									}while (validBikeType == false);
									break;
								}
								case 3:
								{
									boolean validDesc = false;
									do
									{
										input.nextLine();
										System.out.println(" Update Bicycle Description");
										System.out.println(" Original Bicycle Description : "+b.getDesc());
										System.out.print(" New Bicycle Description      : ");
										String bicycleDesc = input.nextLine();
										validDesc = isValidDesc(bicycleDesc);
										if (validDesc == true)
										{
											b.setDesc(bicycleDesc );										}

									}while (validDesc == false);
									break;

								} 
								case 4:
								{
									boolean validSize = false;
									do
									{
										input.nextLine();
										System.out.println(" Update Bicycle Size");
										System.out.println(" Original Bicycle Size : "+b.getSize());
										System.out.print(" New Bicycle Size      : ");
										String bicycleSize = input.nextLine();
										validSize=isValidSize(bicycleSize);
										if (validSize == true)
										{
											b.setSize(bicycleSize);
										}
									}while (validSize == false);
									break;
								} 

								case 5:
								{
									boolean validPrice = false;
									double price;
									do
									{
										input.nextLine();
										System.out.println(" Update Bicycle Price per hour");
										System.out.println(" Original Bicycle Price per hours : "+b.getPrice());
										System.out.print(" New Bicycle Price per hour       : ");
										try
										{
											price = Double.parseDouble(input.nextLine());
											if (price < 15)
											{
												validPrice = true;
												b.setPrice(price);
											}
											else
											{
												System.out.println(" Unrelevant price, please enter again");
												validPrice = false;
											}
										}
										catch (Exception e)
										{
											System.out.println(" Invalid input. Please enter a number.");

											System.out.print(" New Bicycle Price per hour       : ");
											validPrice = false;
										}
									}while (validPrice == false);

									break;
								}
								case 6:
								{
									System.out.println(" Exit updating");
									again = false;
									break;
								}
								default:
								{
									System.out.println(" Wrong option");
									break;
								}
								}
								valid = true;	
							}
							catch(InputMismatchException exp)
							{
								System.out.println(" Please enter correct option.");
								input.nextLine();
								valid = false;
							}  
						}while(valid == false);
					}while(again == true);

					bicycle.set(j,b);
					break;
				}
				else
					j++;
			}
			if(found != true)
			{
				System.out.println(" Bicycle ID is not in the list . Please enter again ");
				j = 100;
			}	
		}while(found != true);
	}
	//Search customer with customer ID
	public static boolean searchCustomer()
	{
		Scanner input = new Scanner(System.in);
		String userID;
		System.out.print(" Enter customer ID to look into details: ");
		userID = input.nextLine();
		boolean found = false;
		int i = 0;
		Customer c = null;
		while(found == false && i < customer.size())
		{
			c = customer.get(i); //get the first customer for matching
			if(c.getID().equals(userID) ) //find whether the input match with database or not
			{
				found = true; 
				System.out.println(" Customer found!");
				c.displayCustomerDetails();
				break;
			}
			else i++; //repeat until match
		}
		return found;
	}

	//search  bicycle with bicycle ID
	public static boolean searchBicycle()
	{
		Scanner input = new Scanner(System.in);
		String bicycleID;
		System.out.print(" Enter bicycle ID to look into details: ");
		bicycleID = input.nextLine();
		boolean found = false;
		int i = 0;
		Bicycle b = null;
		while(found == false && i < bicycle.size())
		{
			b = bicycle.get(i); //get the first customer for matching
			if(b.getID().equals(bicycleID) ) //find whether the input match with database or not
			{
				found = true;
				System.out.println(" Bicycle found!");
				b.displayBicycleDetails();
				break;
			}
			else i++; //repeat until match
		}
		return found;
	}

	//search rental order by rental ID   
	public static boolean searchRental()
	{
		boolean found = false;
		boolean valid = true;
		int rentalID = 0;
		do
		{
			do 
			{
				try 
				{
					System.out.print(" Enter Rental ID to look into details : ");
					rentalID = input.nextInt();
					valid = true;

				}
				catch(InputMismatchException exp)
				{
					System.out.println(" Please enter valid Rental ID.");
					valid = false;
					input.nextLine();
				}
			}while(valid == false);

			int i = 0;
			Rental r = null;
			while(found == false && i < rental.size())
			{
				r = rental.get(i); //get the first customer for matching
				String r1 = Integer.toString(rentalID);
				String r2 = Integer.toString(r.getRentalID());
				if(r2.equals(r1) ) //find whether the input match with database or not
				{
					found = true;
					r.displayRentalOrderDetails();
					break;
				}
				else i++; //repeat until match
			}
			if(found == false)
				System.out.println(" Can't found any rental order. Please enter again.");
		}while(found != true);
		return found;
	}

	//find a customer which match customer ID for finding customer object details after read in rental file
	public static Customer findCustomer(String customerID)
	{
		boolean found = false; //must find a customer obj
		int i = 0;

		Customer c = null;
		while(found == false && i < customer.size())
		{
			c = customer.get(i); //get the first customer for matching
			if(c.getID().equals(customerID) ) //find whether the input match with database or not
			{
				found = true;
				break;
			}
			else i++; //repeat until match
		}
		return c;
	}

	//find administrator ID
	public static Staff findStaff(String staffID)
	{
		boolean found = false;
		int i = 0;

		Staff a = null;
		while(found == false && i < staff.size())
		{
			a = staff.get(i); //get the first customer for matching
			if(a.getID().equals(staffID) ) //find whether the input match with database or not
			{
				found = true;
				break;
			}
			else 
			{i++; //repeat until match
			a = staff.get(0); //if not found, null
			}
		}
		return a; 
	}

	//update customer personal details
	public static void updateCustomerPersDetails(int no)
	{
		Customer cc = new Customer(customer.get(no));
		boolean again = true;
		boolean valid = true;
		int option;
		cc.displayCustomerDetails();
		do 
		{
			System.out.println("\t Update customer details ");
			System.out.println("\t-------------------------");
			System.out.println(" 1. Customer Name");
			System.out.println(" 2. Customer Gender");
			System.out.println(" 3. Customer Birthday Date");
			System.out.println(" 4. Customer Tel.No (+60)");
			System.out.println(" 5. Customer email address");
			System.out.println(" 6. Exit update");
			System.out.println("\t-------------------------");
			do 
			{
				try 
				{
					System.out.print(" Please enter your option : ");
					option = input.nextInt();
					switch (option)
					{
					case 1:
					{
						input.nextLine();
						System.out.println(" Update Customer Name");
						System.out.println(" Original Customer Name : "+cc.getName());
						//request customer name from user with validation
						boolean validName = false;
						String name;
						do 
						{
							System.out.print(" New Customer Name      : ");
							name = input.nextLine();
							validName = isValidName(name); //customer name validation
						} while (validName == false);

						cc.setName(name);
						break;
					}
					case 2:
					{
						System.out.println(" Update Customer Gender");
						System.out.println(" Original Customer Gender : "+cc.getCustomerGender());
						boolean validGender = false;
						char gender;
						do 
						{
							System.out.print(" (M-Male\tF-Female\tO-Others/Unspecified)\n New Customer Gender      : ");
							gender = input.next().toUpperCase().charAt(0);
							validGender = isValidGender(gender); //gender validation
						} while (validGender == false);
						input.nextLine();
						cc.setCustomerGender(gender);
						break;
					}
					case 3:
					{
						input.nextLine();
						System.out.println(" Update Customer Birthday Date");
						System.out.println(" Original Customer Birthday Date\t : "+cc.getDateOfBirth());
						boolean validDate = false;
						String date = null;
						do 
						{	
							System.out.print(" New Customer Birthday Date (dd/mm/yyyy) : ");
							date = input.nextLine();
							validDate = isValidDate(date); //date validation
						}while(validDate == false);
						cc.setDateOfBirth(date);
						break;
					} 
					case 4:
					{
						input.nextLine();
						System.out.println(" Update Customer  Tel.No (+60)");
						System.out.println(" Original Customer  Tel.No (+60) : "+cc.getTelNo());
						boolean validTelNo = false;
						int telNo = 0;
						do 
						{
							System.out.print(" New Customer Tel.No (+60)       : ");
							try 
							{
								telNo = Integer.parseInt(input.nextLine());
								validTelNo = isValidTeleNo(telNo); //telephone no validation
							}
							catch (Exception e) 
							{
								System.out.println(" Please Only Enter Digits");
							}
						} while (validTelNo == false);
						cc.setTelNo(telNo);
						break;
					} 
					case 5:
					{
						input.nextLine();
						System.out.println(" Update Customer email address");
						System.out.println(" Original Customer email address : "+cc.getEmail());
						boolean validEmail = false;
						String email;
						do 
						{
							System.out.print(" New Customer email address      : ");
							email = input.nextLine();
							validEmail = isValidEmail(email); //email validation
						} while (validEmail == false);
						cc.setEmail(email);
						break;
					}
					case 6:
					{
						System.out.println(" Exit updating");
						again = false;
						break;
					}
					default:
					{
						System.out.println(" Wrong option");
						break;
					}
					}
					valid = true;
				}
				catch(InputMismatchException exp)
				{
					System.out.println(" Please enter correct option.");
					input.nextLine();
					valid = false;
				}
			}while(valid == false);
		}while(again == true);

		customer.set(no,cc);
	}
	//Place rental order
	public static void placeRental(int i)
	{
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t   ~ WELCOME TO HOURLY BICYCLE RENTAL SYSTEM ~");
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t\t  | PLACE RENTAL ORDER |\t\t");
		System.out.println("------------------------------------------------------------------");
		boolean validOrderDate = false;
		String orderDate = null;
		input.nextLine();
		do 
		{
			System.out.print(" Enter Order Date : ");
			orderDate = input.nextLine();
			validOrderDate = isValidDate(orderDate); //date validation
		}while(validOrderDate == false);

		boolean validRentalHour = false;
		int rentalHour = 0;
		do 
		{
			System.out.print(" Enter rental hour(s) : ");
			try 
			{
				rentalHour = Integer.parseInt(input.nextLine());
				validRentalHour = true;
			} 
			catch (Exception e) 
			{
				System.out.println(" Invalid Input. Please Enter a Whole Number.");
			}
		} while (validRentalHour == false);

		boolean validRentalDate = false;
		String rentalDate =  null;
		do 
		{
			System.out.print(" Enter Rental Date : ");
			rentalDate = input.nextLine();
			validRentalDate = isValidDate(rentalDate); //date validation
		}while(validRentalDate == false);

		boolean validRentalDueDate = false;
		String rentalDueDate =  null;
		if (rentalHour < 24)
		{
			rentalDueDate = rentalDate;
		}
		else
		{
			do 
			{
				System.out.print(" Enter Rental Due Date : ");
				rentalDueDate = input.nextLine();
				validRentalDueDate = isValidDate(rentalDueDate); //date validation
			}while(validRentalDueDate == false);			
		}

		System.out.println("==================================================================");
		Rental newRental = new Rental(orderDate, rentalHour, rentalDate, rentalDueDate, staff.get(0),customer.get(i));
		rental.add(newRental);

		String bicycleID;
		boolean found = false;
		int add = 1;
		do 
		{
			System.out.print("\t\t   ~ Choose Your Bicycle ~\n");
			Bicycle b = null;
			System.out.println("------------------------------------------------------------------");
			System.out.println("\t\t    | Bicycle LIST |\t\tTotal: " + bicycle.size());
			System.out.println("------------------------------------------------------------------");
			System.out.println("  No.  |\tBicycle ID\t|\tBicycle Name\t\t|");
			System.out.println("------------------------------------------------------------------");

			for(int t = 0; t < bicycle.size();t++)
			{
				System.out.printf("  %-2d.\t\t%-6s\t\t\t%-15s\n", (t+1), bicycle.get(t).getID(), bicycle.get(t).getBicycleName());
			}
			System.out.println("==================================================================\n");
			System.out.print(" Enter Bicycle ID: ");
			do
			{
				found = false;
				int j = 0;
				int count = bicycle.size();
				bicycleID = input.nextLine();
				while(j < count && !found)
				{
					b = bicycle.get(j);
					if(b.getID().equals(bicycleID)) //match bicycle ID entered with data set
					{
						found = true;
						System.out.println(" Added Successfully! ");
					}
					else
						j++;
				}
				if(found != true)
				{
					System.out.println(" Bicycle ID is not in the list . Please enter again ");
					System.out.print(" Enter Bicycle ID:");
				}
			}while(found != true);

			newRental.addBicycle(b);

			do
			{boolean validOpt = false;
			do
			{
				try
				{
					System.out.print(" [ MAXIMUM 5 bicycle can be added ]\n");
					System.out.print(" Do you want to add bicycle again ?( 1 = Yes  0 = No)\n");
					System.out.print(" Option: ");
					add = input.nextInt();
					validOpt = true;
				}
				catch (Exception e)
				{
					System.out.println(" Invalid Input. Please only enter 1 or 0.");
					input.nextLine();
				}
			}while (validOpt == false);
			if (add != 1 && add != 0)
			{
				System.out.println(" Wrong Input.Please only choose 1 or 0.");
				input.nextLine();
			}
			}while (add != 1 && add!=0);
			input.nextLine();

		}while(add == 1); //terminate when user choose 0

		newRental.displayRentalOrderDetails();
		newRental.generateRentalReceipt(false);
	}
	//display only customer rental list
	public static void displayCustomerRentalHistory(int no)
	{
		ArrayList <Rental> rr = new ArrayList <Rental> ();
		int i = 0;
		Rental rent = null;
		while( i < rental.size())
		{
			rent = rental.get(i); //get the first customer for matching
			if(rent.getCustomer().getID().equals(customer.get(no).getID()) ) //find whether the input match with database or not
			{
				rr.add(rent);
			}
			i++; //repeat until match
		}
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t   ~ WELCOME TO HOURLY BICYCLE RENTAL SYSTEM ~");
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t\t   | CHECK RENTAL HISTORY |\t\t");
		System.out.println("------------------------------------------------------------------");
		System.out.println("  No.  |\tRental ID\t|  Date order  |");
		for(int z = 0; z < rr.size(); z++)
		{
			System.out.printf("  %2d.\t\t%4d\t\t   %10s\n", (z+1), rr.get(z).getRentalID(), rr.get(z).getRentalDate());
		}
		System.out.println("==================================================================");
	}

	//save customer data to file
	public static void saveToCustomerFile()
	{
		try 
		{ //try first
			File customerFile = new File("Customer.txt");
			FileWriter fileWriter = new FileWriter(customerFile,false);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			for(int i = 0; i < customer.size(); i++)
			{
				printWriter.println(customer.get(i).getID()+"#"+customer.get(i).getName()+"#"+customer.get(i).getPassword()+"#"+customer.get(i).getCustomerGender()+"#"+customer.get(i).getDateOfBirth()+"#"+customer.get(i).getTelNo()+"#"+customer.get(i).getEmail()+"#"+customer.get(i).getAddress());
			}
			printWriter.close();		
		}
		catch (IOException e)
		{
			System.out.println(" Error");
		}
	}

	//write in all bicycle data into the file
	public static void saveBicycle() 
	{
		try
		{
			FileOutputStream fileOut = new  FileOutputStream("Bicycle.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fileOut);
			oos.writeObject(bicycle);
			oos.close();
			fileOut.close();
			System.out.printf(" Serialized data for bicycle is saved.\n");
		} 
		catch (IOException i) 
		{
			i.printStackTrace();
		}
	}

	//write in all rental details into the file
	public static void saveToRentalFile()
	{
		try 
		{ //try first
			File rentalFile = new File("Rental.txt");
			FileWriter fileWriter = new FileWriter(rentalFile,false);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			for(int i = 0;i < rental.size();i++)
			{
				Bicycle[] bicycleList = new Bicycle[rental.get(i).getNoOfBicycle()];
				bicycleList = rental.get(i).getBicycleRentalList();
				String line = "";
				for(int j = 0; j < rental.get(i).getNoOfBicycle(); j++)
				{
					if(j != rental.get(i).getNoOfBicycle()-1)
						line += bicycleList[j].getID()+ "#";
					else
						line += bicycleList[j].getID();
				}
				printWriter.println(rental.get(i).getPlaceRentalDate()+'#'+ rental.get(i).getRentalID()+'#'+ rental.get(i).getRentalHour()
						+'#'+rental.get(i).getRentalDate()+'#'+rental.get(i).getCustomer().getID()+'#'+rental.get(i).getStaff().getID()
						+'#'+rental.get(i).getStatus()+'#'+rental.get(i).getNoOfBicycle()+'#'+line);
			}
			printWriter.close();
			System.out.println(" Rental data saved.");
		}
		catch (IOException e)
		{
			System.out.println(" Error!");
		}
	}

	//validation for date
	@SuppressWarnings("finally")
	public static boolean  isValidDate(String d)
	{
		boolean validDate = true;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu",Locale.CHINA).withResolverStyle(ResolverStyle.STRICT);
		try
		{
			formatter.parse(d);
		}
		catch (Exception e)
		{
			validDate = false;
			System.out.println(" Invalid Date. Please enter again.");
		}
		finally
		{
			return validDate;
		}
	}

	//customer ID validation
	public static boolean isValidID(String id) 
	{
		boolean validID;
		if (id.matches("[C]\\d{4}")) 
		{
			boolean conflict = false;
			int i = 0;
			while (conflict == false && i < customer.size()) 
			{
				if (id.equals(customer.get(i).getID()))
					conflict = true;
				else 
				{
					conflict = false;
					i++;
				}
			}
			if (conflict == false) 
			{
				validID = true;
			} 
			else 
			{
				validID = false;
				System.out.println(" ID is already in use. Try a different one.");
			}
		} 
		else 
		{
			validID = false;
			System.out.println(" Invalid ID. Enter again.");
		}
		return validID;
	}

	//customer name validation
	public static boolean isValidName(String name) 
	{
		boolean validName;
		if (name.matches("[a-zA-Z\\s\\.]+")) 
		{
			validName = true;
		} 
		else 
		{
			validName = false;
			System.out.println(" Please write in lowercase or uppercase letters only. Enter again.");
		}
		return validName;
	}

	//password validation
	public static boolean isValidPassword(String password) 
	{
		boolean validPassword;
		if (password.matches("\\S+")) 
		{
			if (password.length() >= 6 && password.length() <= 22)
				validPassword = true;
			else 
			{
				validPassword = false;
				System.out.println(" Password Invalid");
			}
		} 
		else 
		{
			validPassword = false;
			System.out.println(" No Spaces Are Allowed In The Password");
		}
		return validPassword;
	}

	//gender validation
	public static boolean isValidGender(char gender) 
	{
		boolean validGender;
		if (Character.toString(gender).matches("[MFO]")) 
		{
			validGender = true;
		} 
		else 
		{
			validGender = false;
			System.out.println(" Invalid Input");
		}
		return validGender;
	}

	//TelNo validation
	@SuppressWarnings("finally")
	public static boolean isValidTeleNo(int telNo) 
	{
		boolean validTeleNo;
		if (telNo >= 100000000 && telNo <= 1999999999) 
		{
			validTeleNo = true;
		} 
		else 
		{
			validTeleNo = false;
			System.out.println(" Invalid Telephone Number. Please Enter Again.");
		}
		return validTeleNo;
	}

	//email validation
	public static boolean isValidEmail(String email) 
	{
		boolean validEmail;
		if (email.matches("[a-zA-Z0-9\\.\\_]+\\@[a-z\\.]+")) 
		{
			validEmail = true;
		} 
		else 
		{
			validEmail = false;
			System.out.println(" Invalid E-mail. Please Enter Again.");
		}
		return validEmail;
	}

	//address validation
	public static boolean isValidAddress(String address) 
	{
		boolean validAddress;
		if (address.matches("[\\w\\s\\,\\.\\/\\-]+")) 
		{
			validAddress = true;
		} 
		else 
		{
			validAddress = false;
			System.out.println(" Invalid Address. Please Enter Again.");
		}
		return validAddress;
	}

	//Bicycle ID validation
	public static boolean isValidBikeID (String bicycleID) 
	{
		boolean validBikeID;
		if (bicycleID.matches("[B]\\d{5}"))
		{
			boolean crash = false;
			int i = 0;
			while (crash == false && i < bicycle.size()) 
			{
				if (bicycleID.equals(bicycle.get(i).getID()))
					crash = true;
				else 
				{
					crash = false;
					i++;
				}
			}
			if (crash == false) 
			{
				validBikeID = true;
			} 
			else 
			{
				validBikeID = false;
				System.out.println(" ID is already in use. Try a different ID.");
			}
		}
		else
		{
			validBikeID = false;
			System.out.println(" Invalid ID Format. Please Key In Again");		
		}
		return validBikeID;
	}

	//bicycle name validation
	public static boolean isValidBikeName (String bicycleName) 
	{
		boolean validBikeName;
		if (bicycleName.matches("[a-zA-Z]+")) 
		{
			validBikeName = true;
		} 
		else 
		{
			validBikeName = false;
			System.out.println(" Only Upper and Lower case Alphabets are allowed. Please Try Again ");
		}
		return validBikeName;
	}

	//bicycle size validation
	public static boolean isValidSize (String bicycleSize) 
	{
		boolean validSize;
		if (bicycleSize.matches("[S]+")) 
		{
			validSize = true;
		}
		else if (bicycleSize.matches("[M]+")) 
		{
			validSize = true;
		}
		else if (bicycleSize.matches("[L]+")) 
		{
			validSize = true;
		}
		else 
		{
			validSize = false;
			System.out.println(" Please only enter 'S', 'M' or 'L'");
		}
		return validSize;
	}

	//bicycle type validation
	public static boolean isValidType (String bicycleType)
	{
		boolean validBikeType;
		if (bicycleType.matches("[a-zA-z\\s]+"))
		{
			validBikeType = true;
		}
		else
		{
			validBikeType = false;
			System.out.println(" Please use only upper and lower case Alphabets");
		}
		return validBikeType;
	}

	//bicycle description validation
	public static boolean isValidDesc (String bicycleDesc)
	{ 
		boolean validDesc;
		if (bicycleDesc.matches("[a-zA-z\\s\\,?.]+"))
		{
			validDesc = true;
		}
		else
		{
			validDesc=false;
			System.out.println(" Invalid input, please enter in words.");
		}
		return validDesc;
	}
}


