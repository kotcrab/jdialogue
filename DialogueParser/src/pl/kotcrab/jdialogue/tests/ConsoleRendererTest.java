package pl.kotcrab.jdialogue.tests;

import java.io.File;

import pl.kotcrab.jdialogue.loader.JavaLoader;
import pl.kotcrab.jdialogue.parser.DialogueParser;
import pl.kotcrab.jdialogue.parser.impl.JDOMDialogueParser;
import pl.kotcrab.jdialogue.renderer.ConsoleRenderer;

public class ConsoleRendererTest
{
	public static void main(String[] args)
	{
		ConsoleRenderer renderer = new ConsoleRenderer(new JDOMDialogueParser(new JavaLoader(new File("assets/testProj/project.xml")), DialogueParser.INFINITY));
		//renderer.startSequence("test");
		//renderer.render();
	}
	
}
