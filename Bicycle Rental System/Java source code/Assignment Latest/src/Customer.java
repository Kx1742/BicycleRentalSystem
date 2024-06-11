//import java.io.Serializable;
import java.util.ArrayList;
//import java.util.Scanner;

public class Customer extends User
{
	private char gender;
	private String dateOfBirth;
	private int telNo;
	private String email;
	private String address;
	private ArrayList<Rental> rentalList;

	public Customer(String customerID,String customerName, String customerPassword,char gender, String dateOfBirth,int telNo,String email,String address)
	{
		super(customerName,customerID,customerPassword);
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.telNo = telNo;
		this.email = email;
		this.address = address;
		rentalList = new ArrayList<Rental>();	
	}

	public Customer(Customer c)
	{
		super(c.getName(),c.getID(),c.getPassword());
		this.gender = c.gender;
		this.dateOfBirth = c.dateOfBirth;
		this.telNo = c.telNo;
		this.email = c.email;
		this.address = c.address;
		rentalList = new ArrayList<Rental>();	
	}

	public char getCustomerGender ()
	{
		return gender;
	}

	public String getDateOfBirth ()
	{
		return dateOfBirth;
	}

	public int getTelNo()
	{
		return telNo;
	}

	public String getEmail ()
	{
		return email;
	}

	public String getAddress ()
	{
		return address;
	}

	public Rental [] getRentalList()
	{
		int count = rentalList.size();
		Rental [] rentList = new Rental[count];
		for (int i = 0; i < rentList.length; i++)
		{
			rentList[i] = rentalList.get(i);
		}
		return rentList;
	}

	public void setCustomerGender (char gender)
	{
		this.gender = gender;
	}

	public void setDateOfBirth (String dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}

	public void setTelNo(int telNo)
	{
		this.telNo = telNo;
	}

	public void setEmail (String email)
	{
		this.email = email;
	}

	public void setAddress (String address)
	{
		this.address = address;
	}

	public void customerMenu()
	{
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t   ~ WELCOME TO HOURLY BICYCLE RENTAL SYSTEM ~");
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t\t       | CUSTOMER MENU |");
		System.out.println("------------------------------------------------------------------\n");
		System.out.println("\t\t   1. View Personal Details\n");
		System.out.println("\t\t   2. Update Customer Personal Details\n");
		System.out.println("\t\t   3. View Bicycle List\n");
		System.out.println("\t\t   4. Place Rental Order\n");
		System.out.println("\t\t   5. Check Rental History\n");
		System.out.println("==================================================================");
		System.out.println(" Enter 0 to quit this interface. ");
		System.out.print(" Option: ");
	}

	public  void displayCustomerDetails()
	{
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t   ~ WELCOME TO HOURLY BICYCLE RENTAL SYSTEM ~");
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t\t    | CUSTOMER DETAILS |\t\tTotal: " );
		System.out.println("------------------------------------------------------------------");
		System.out.printf(" Customer ID   : %-10s\t\tPassword : %-10s\n" , super.getID() , super.getPassword());
		System.out.printf("\n Name          : %-20s	Gender   : %s\n" , super.getName() , gender);
		System.out.println("\n Date of Birth : " + dateOfBirth);
		System.out.println("\n TelNo\t       : " + telNo);
		System.out.println("\n Email\t       : " + email);
		System.out.println("\n Address       : \n\n " + address);
		System.out.println("==================================================================\n");	
	}

	public void placeARental(Rental rental)
	{
		rentalList.add(rental);
	}
}
