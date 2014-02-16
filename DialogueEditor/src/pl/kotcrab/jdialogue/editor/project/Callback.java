package pl.kotcrab.jdialogue.editor.project;

public class Callback
{
	private int id;
	private String name;
	
	public Callback(int id, String name)
	{
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
