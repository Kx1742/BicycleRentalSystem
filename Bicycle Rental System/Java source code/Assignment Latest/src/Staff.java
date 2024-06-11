
public class Staff extends User 
{	
	private String jobTitle;	
	public Staff (String staffName,String staffID, String staffPassword,String jobTitle)
	{
		super(staffName,staffID,staffPassword);
		this.jobTitle = jobTitle;
	}

	public String getJobTitle()
	{
		return jobTitle;
	}
}
