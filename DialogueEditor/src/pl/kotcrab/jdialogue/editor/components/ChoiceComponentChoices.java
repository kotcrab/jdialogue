/*******************************************************************************
    DialogueEditor
    Copyright (C) 2013-2014 Pawel Pastuszak

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package pl.kotcrab.jdialogue.editor.components;


public class ChoiceComponentChoices
{
	private String[] choices;
	private String stringReprsentation;
	
	public ChoiceComponentChoices(int initValues)
	{
		choices = new String[initValues];
		
		for(int i = 0; i < initValues; i++)
			choices[i] = new String("Set Value");
		
		makeStringRepresentation();
	}
	
	public String[] getChoicesTable()
	{
		return choices;
	}
	
	public void setChoices(String[] choices)
	{
		this.choices = choices;
		makeStringRepresentation();
	}
	
	private void makeStringRepresentation()
	{
		stringReprsentation = "Values: [";
		
		if(choices.length >= 1) stringReprsentation += choices[0];
		if(choices.length >= 2) stringReprsentation += ", " + choices[1];
		if(choices.length >= 3) stringReprsentation += ", " + choices[2];
		
		stringReprsentation += "] ";
		
		if(choices.length > 3) stringReprsentation += "(" + (choices.length - 3) + " more) ";
		
		stringReprsentation += "(click to edit)";
	}
	
	public void resize(int newLength)
	{
		if(newLength == choices.length) return;
		
		if(newLength > choices.length)
		{
			String[] newChoices = new String[newLength];
			System.arraycopy(choices, 0, newChoices, 0, choices.length);
			
			for(int i = choices.length; i < newLength; i++)
				newChoices[i] = new String("Set Value");
			
			choices = newChoices;
		}
		else
		{
			String[] newChoices = new String[newLength];
			System.arraycopy(choices, 0, newChoices, 0, newLength);
			
			choices = newChoices;
		}
		
		makeStringRepresentation();
	}
	
	@Override
	public String toString()
	{
		return stringReprsentation;
	}
}
