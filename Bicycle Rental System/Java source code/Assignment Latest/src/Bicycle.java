import java.io.Serializable;

public class Bicycle implements Serializable{
	private static final long serialVersionUID = 3L;
	private String bicycleName;
	private String bicycleID;
	private String bicycleType;
	private String bicycleDesc;
	private String bicycleSize;
	private double bicycleRentalPrice; 

	public Bicycle(String bicycleName,String bicycleID, String bicycleType, String bicycleDesc, String bicycleSize, double bicycleRentalPrice) {
		this.bicycleName = bicycleName;
		this.bicycleID = bicycleID;
		this.bicycleType = bicycleType;
		this.bicycleDesc = bicycleDesc;
		this.bicycleSize = bicycleSize;
		this.bicycleRentalPrice = bicycleRentalPrice;
	}

	public Bicycle(String bicycleName,String bicycleID, String bicycleType, String bicycleSize, double bicycleRentalPrice) {
		this.bicycleName = bicycleName;
		this.bicycleID = bicycleID;
		this.bicycleType = bicycleType;
		this.bicycleDesc = "N/A";
		this.bicycleSize = bicycleSize;
		this.bicycleRentalPrice = bicycleRentalPrice;
	}

	public String getBicycleName() {
		return bicycleName;
	}

	public String getID() {
		return bicycleID;
	}

	public String getType() {
		return bicycleType;
	}

	public String getDesc() {
		return bicycleDesc;
	}

	public String getSize() {
		return bicycleSize;
	}

	public double getPrice() {
		return bicycleRentalPrice;
	}

	public void setName(String bicycleName) {
		this.bicycleName = bicycleName;
	}

	public void setID(String bicycleID) {
		this.bicycleID = bicycleID;
	}

	public void setType(String bicycleType) {
		this.bicycleType = bicycleType;
	}

	public void setDesc(String bicycleDesc) {
		this.bicycleDesc = bicycleDesc;
	}

	public void setSize(String bicycleSize) {
		this.bicycleSize = bicycleSize;
	}

	public void setPrice(double bicyclePrice) {
		this.bicycleRentalPrice = bicyclePrice;
	}

	public  void displayBicycleDetails()
	{
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t   ~ WELCOME TO HOURLY BICYCLE RENTAL SYSTEM ~");
		System.out.println("------------------------------------------------------------------");
		System.out.println("\t\t    | BICYCLE DETAILS |" );
		System.out.println("------------------------------------------------------------------");
		System.out.printf(" Bicycle ID    : %-6s\n\n" , bicycleID);
		System.out.printf(" Bicycle Name  : %-20s\n", bicycleName);
		System.out.println("\n Bicycle Type  : " + bicycleType);
		System.out.println("\n Bicycle Size  : " + bicycleSize);
		System.out.println("\n Bicycle Price : " + bicycleRentalPrice);
		System.out.println("\n Description   : \n\n " + bicycleDesc);
		System.out.println("==================================================================\n");
	}
}
