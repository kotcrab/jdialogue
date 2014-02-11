package pl.kotcrab.jdialogue.editor.project;

public class Character
{
	
	private String name;
	private String textureName;
	
	public Character(String name, String textureName)
	{
		this.name = name;
		this.textureName = textureName;
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

	public void setTextureName(String textureName)
	{
		this.textureName = textureName;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}
