package pl.kotcrab.jdialogue.editor.project;

public class Callback
{
	
	private String name;
	
	public Callback(String name)
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
		return name;
	}
}
