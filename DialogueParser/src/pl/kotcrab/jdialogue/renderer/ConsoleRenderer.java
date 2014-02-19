package pl.kotcrab.jdialogue.renderer;

import java.util.InputMismatchException;
import java.util.Scanner;

import pl.kotcrab.jdialogue.parser.CallbackListener;
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
		
		parser.addCallbackListener(new CallbackListener()
		{
			
			@Override
			public boolean handleCallbackCheck(String callbackText)
			{
				System.out.print("***Callback Check*** " + callbackText + " DEBUG INPUT: true/false");
				
				return nextValidBoolean();
			}
			
			@Override
			public void handleCallback(String callbackText)
			{
				System.out.println("***Callback*** " + callbackText);
			}
		});
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
				println(parser.getNextMsg());
				parser.nextComponent();
			}
			
			if(nextType == ComponentType.CHOICE)
			{
				println("===" + parser.getNextMsg() + "===");
				
				String[] choices = parser.getChoiceData();
				
				for(int i = 0; i < choices.length; i++)
				{
					println(i + 1 + ". " + choices[i]);
				}
				
				print("Option: ");
				int chosen = -1;
				do
				{
					try
					{
						chosen = scanner.nextInt() - 1;
					}
					catch (InputMismatchException e)
					{
						scanner.nextLine();
					}
					
					if(chosen >= choices.length || chosen < 0) println("Option not found, try again");
					
				} while (chosen >= choices.length || chosen < 0);
				
				parser.nextComponent(chosen);
				println("====================");
			}
			
			if(nextType == ComponentType.RANDOM) parser.processRandom();
			
			if(nextType == ComponentType.CBCHECK) parser.processCallbackCheck();
			
			if(nextType == ComponentType.RELAY || nextType == ComponentType.CALLBACK) parser.nextComponent();
			
		} while (nextType != ComponentType.END);
		
		scanner.close();
	}
	
	private void println(String line)
	{
		System.out.println(line);
	}
	
	private void print(String text)
	{
		System.out.print(text);
	}
	
	@Override
	public void startSequence(String name)
	{
		parser.startSequence(name);
	}
	
	private boolean nextValidBoolean()
	{
		boolean valid = false;
		boolean response = false;
		do
		{
			try
			{
				response = scanner.nextBoolean();
				valid = true;
			}
			catch (InputMismatchException e)
			{
				System.out.println("Input valid boolean!");
				scanner.nextLine();
				valid = false;
			}
		} while (valid == false);
		
		return response;
	}
}