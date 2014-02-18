package pl.kotcrab.jdialogue.parser;

public class PCharacter
{
	private int id;
	private String name;
	private String textureName;
	
	public PCharacter(int id, String name, String textureName)
	{
		this.name = name;
		this.textureName = textureName;
		this.id = id;
	}
	
	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getTextureName()
	{
		return textureName;
	}

	public int getId()
	{
		return id;
	}

	public void setTextureName(String textureName)
	{
		this.textureName = textureName;
	}
	
	@Override
	public String toString()
	{
		return name + " (id: " + id + ")";
	}
}
