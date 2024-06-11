
public class User 
{
	private String name;
	private String ID;
	private String password;

	//Setter
	public void setName(String name)
	{
		this.name = name;
	}

	public void setID(String ID)
	{
		boolean validID = false;
		//L-DDD
		if(ID.length() == 5)
		{
			char IDCase = ID.charAt(0);
			if(IDCase == 'A' || IDCase == 'C')
			{
				String IDDigit = ID.substring(2);
				boolean allDigit = IDDigit.matches("[0-9]+");
				if (allDigit == true)
				{
					validID = true;
					this.ID = ID;
				}		
			}
		}
		if(validID == false)
		{
			this.ID = "INVALID USER ID";
		}
	}

	//Getter
	public String getName()
	{
		return name;
	}

	public String getID()
	{
		return ID;
	}

	public String getPassword()
	{
		return password;
	}

	//Constructor
	public User(String name,String ID,String password)
	{
		this.name = name;
		this.password = password;
		setID(ID);
	}
}


