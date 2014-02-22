/*******************************************************************************
 * Copyright 2014 Pawel Pastuszak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

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
		renderer.startSequence("test");
		renderer.render();
		renderer.dispose();
	}
	
}
