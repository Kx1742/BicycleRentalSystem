import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Rental
{
	private int rentalID;
	private int rentalHour;
	private int noOfBicycleOrder;
	String staffID;
	private String placeRentalDate;
	private String rentalDate;
	private String rentalDueDate;
	private Customer customer;
	private Staff staff;
	String status;
	private double totalRentalPrice;
	private static int max_no_of_bicycle_order = 5; //only maximum 5 bicycle allow to rent
	private Bicycle [] bicycleRentalList;
	String[] bicycleIDList ;

	public Rental(String placeRentalDate,int rentalHour,String rentalDate, String rentalDueDate, Staff staff, Customer customer)
	{
		this.placeRentalDate = placeRentalDate;
		this.rentalID = generateRentalID();
		this.rentalHour = rentalHour;
		this.rentalDate = rentalDate;
		this.rentalDueDate = rentalDueDate;
		this.customer = customer;
		this.staff = staff;
		bicycleRentalList = new Bicycle[max_no_of_bicycle_order];
		status ="Processing";
		noOfBicycleOrder = 0;
	}

	public Rental(String placeRentalDate,int rentalID,int rentalHour,String rentalDate,Customer customer,String status, String staffID, int noOfBicycleOrder)
	{
		this.placeRentalDate = placeRentalDate;
		this.rentalID = rentalID;
		this.rentalHour = rentalHour;
		this.rentalDate = rentalDate;
		this.customer = customer;
		this.status = status;
		this.staffID = staffID;
		this.noOfBicycleOrder = noOfBicycleOrder;
		bicycleRentalList = new Bicycle[max_no_of_bicycle_order];	
	}

	public Rental(Rental rental) {
		// TODO Auto-generated constructor stub
		bicycleRentalList=new Bicycle[max_no_of_bicycle_order];
	}

	public int generateRentalID()
	{
		//generate ID randomly
		int min = 1000;
		int max = 9999;
		int rentID = (int)(Math.random()*(max-min+1)+min); 
		return rentID;
	}

	public void setStaff(Staff staff)
	{
		this.staff = staff;
	}

	public Customer getCustomer()
	{
		return customer;
	}

	public String getPlaceRentalDate()
	{
		return placeRentalDate;
	}

	public int getRentalID()
	{
		return rentalID;
	}

	public int getRentalHour()
	{
		return rentalHour;
	}

	public String getRentalDate()
	{
		return rentalDate;
	}

	public Staff getStaff()
	{
		return staff;
	}

	public String getStatus()
	{
		return status;
	}

	public Bicycle[] getBicycleRentalList()
	{
		return bicycleRentalList;
	}

	public int getNoOfBicycle()
	{
		return noOfBicycleOrder;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public void addBicycle(Bicycle b)
	{
		if(noOfBicycleOrder < max_no_of_bicycle_order)
		{
			bicycleRentalList[noOfBicycleOrder] = b;
			noOfBicycleOrder++;
		}
	}

	public void addBicycleFromFile(int no,Bicycle b)
	{
		bicycleRentalList[no] = b;	
	}

	public void displayBicycleOrderList()
	{
		System.out.println(" Number of bicycle ordered: "+ noOfBicycleOrder);
		System.out.println("------------------------------------------------------------------");
		System.out.println("  No.  |     ID     | Type |     Bicycle Name     |  Price / hr  |");
		System.out.println("------------------------------------------------------------------");
		for(int i = 0; i < noOfBicycleOrder; i++)	
		{
			System.out.printf("  %-2d.      %-6s    %-6s\t%-15s\t\t%6.2f\n",i+1,bicycleRentalList[i].getID(),bicycleRentalList[i].getType(),bicycleRentalList[i].getBicycleName(),bicycleRentalList[i].getPrice());
		}
	}

	public void displayRentalOrderDetails()
	{
		calculateTotalRentalPrice();
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t\t ~ HOURLY BICYCLE RENTAL SYSTEM ~");
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t\t    | RENTAL ORDER DETAILS |");
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t\t    ABC Bicycle Rental SDN BHD");
		System.out.println("\t\t     Phone no: 012-3456789");
		System.out.println("------------------------------------------------------------------");
		System.out.printf(" Customer ID        : %5s\t\tStaff ID   : %5s\n", customer.getID(), staff.getID());
		System.out.printf(" Order Date         : %10s\tRental ID  : %4d\n", placeRentalDate, rentalID);
		System.out.println(" Date of Rental     : "+ rentalDate);
		System.out.println(" Date of Return     : "+ rentalDueDate);
		System.out.println(" Total rental period (hour(s)) : "+ rentalHour);
		System.out.printf(" Total rental price : RM %.2f\n", totalRentalPrice);
		System.out.println("==================================================================");
		displayBicycleOrderList();
		System.out.println(" Total bicycle rent out: "+ noOfBicycleOrder);
		System.out.println(" Order status : "+status);
		System.out.println("========================== Order Closed ==========================");
	}

	public void calculateTotalRentalPrice()
	{
		double total = 0;
		for(int i = 0;i < noOfBicycleOrder; i++)
			total += (bicycleRentalList[i].getPrice()*rentalHour);
		totalRentalPrice = total;
	}

	// generate a receipt in text file
	public void generateRentalReceipt(Boolean append)
	{
		String receipt = "Receipt";
		String receipt1 = ".txt";
		int receiptID = rentalID;
		String id = Integer.toString(receiptID);
		String file = receipt+ id+ receipt1;
		try 
		{ 
			File f = new File(file);
			FileWriter fileWriter = new FileWriter(f,append);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.println("------------------------------------------------------------------");
			printWriter.println("\t\t ~ HOURLY BICYCLE RENTAL SYSTEM ~");
			printWriter.println("------------------------------------------------------------------");
			printWriter.println("\t\t    | RENTAL ORDER DETAILS |");
			printWriter.println("------------------------------------------------------------------");
			printWriter.println("\t\t    ABC Bicycle Rental SDN BHD");
			printWriter.println("\t\t     Phone no: 012-3456789");
			printWriter.println("------------------------------------------------------------------");
			printWriter.println("                                   Rental ID : "+ rentalID);
			printWriter.println("                                   Rental Date : "+ placeRentalDate);
			printWriter.println(" Customer ID        : "+ customer.getID());
			printWriter.println(" Date of Rental     : "+ rentalDate);
			printWriter.println(" Date of Return     : "+ rentalDueDate);
			printWriter.println(" Total rental period (hour(s)) : "+ rentalHour);
			printWriter.printf(" Total rental price : RM %.2f\n", totalRentalPrice);
			printWriter.println("==================================================================");
			printWriter.println("  No.  |     ID     | Type |     Bicycle Name     |  Price / hr  |");

			for(int i = 0; i < noOfBicycleOrder; i++)
			{
				String line = String.format("  %-2d.      %-6s    %-6s\t   %-15s\t%6.2f\n", (i+1), bicycleRentalList[i].getID(), 
						bicycleRentalList[i].getType(), bicycleRentalList[i].getBicycleName(), bicycleRentalList[i].getPrice());
				printWriter.println(line);
			}
			printWriter.println(" Total bicycle rent out: "+ noOfBicycleOrder);
			printWriter.close();
		}
		catch (IOException e)
		{
			System.out.println(" Error");
		}
	}
}
