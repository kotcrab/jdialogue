package pl.kotcrab.jdialogue.parser;

public class PCallback
{
	private int id;
	private String name;
	
	public PCallback(int id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return name + " (id: " + id + ")";
	}

	public int getId()
	{
		return id;
	}
}
