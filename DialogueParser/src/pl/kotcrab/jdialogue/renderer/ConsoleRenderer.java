package pl.kotcrab.jdialogue.renderer;

import pl.kotcrab.jdialogue.parser.ComponentType;
import pl.kotcrab.jdialogue.parser.DialogueParser;

/**
 * Simplest 'renderer' posible, uses console for 'rendering' and handling input
 * 
 * @author Pawel Pastuszak
 */
public class ConsoleRenderer implements DialogueRenderer
{
	DialogueParser parser;
	
	public ConsoleRenderer(DialogueParser parser)
	{
		this.parser = parser;
	}
	
	@Override
	public void render()
	{
		do
		{
			if(parser.getNextType() == ComponentType.TEXT)
			{
				System.out.println(parser.getNextMsg());
				parser.nextComponent();
			}
		} while (parser.getNextType() != ComponentType.END);
	}
}