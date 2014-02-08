package pl.kotcrab.jdialogue.renderer;

import java.util.Scanner;

import pl.kotcrab.jdialogue.parser.ComponentType;
import pl.kotcrab.jdialogue.parser.DialogueParser;

/**
 * Simplest 'renderer' posible, uses console for 'rendering' and handling input
 * 
 * @author Pawel Pastuszak
 */
public class ConsoleRenderer implements DialogueRenderer
{
	private Scanner scanner;
	private DialogueParser parser;
	
	public ConsoleRenderer(DialogueParser parser)
	{
		this.parser = parser;
		scanner = new Scanner(System.in);
	}
	
	@Override
	public void render()
	{
		ComponentType nextType;
		do
		{
			nextType = parser.getNextType();
			
			if(nextType == ComponentType.TEXT)
			{
				print(parser.getNextMsg());
				parser.nextComponent();
			}
			
			if(nextType == ComponentType.CHOICE)
			{
				print(parser.getNextMsg());
				
				String[] choices = parser.getChoiceData();
				
				for(int i = 0; i < choices.length; i++)
				{
					print(i + 1 + ". " + choices[i]);
				}
				
				int chosen;
				do
				{
					chosen = scanner.nextInt() - 1;
					
					if(chosen >= choices.length || chosen < 0)
						print("Option not found, try again");
					
				} while (chosen >= choices.length || chosen < 0);
				
				parser.nextComponent(chosen);
			}
		} while (nextType != ComponentType.END);
		
		scanner.close();
	}
	
	private void print(String line)
	{
		System.out.println(line);
	}
}