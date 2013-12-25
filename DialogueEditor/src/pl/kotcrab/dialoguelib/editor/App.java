/*******************************************************************************
 * Copyright 2013 Pawel Pastuszak
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

package pl.kotcrab.dialoguelib.editor;

public class App
{
	private static int screenId = 0;
	
	public static void parseArguments(String[] args) throws ArgsParserException
	{
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equals("-screen") )
			{
				if(i + 1 > args.length - 1)
				{
					throw new ArgsParserException("-screen requires moniotr id. Example: -screen 1");
				}
				
				try
				{
					screenId = Integer.parseInt(args[i + 1]);
				}
				catch(NumberFormatException e)
				{
					System.err.println("-screen requires moniotr id. Example: -screen 1");
					e.printStackTrace();
				}
				
				i++;
				continue;
				
			}
			
			throw new ArgsParserException("Unrecognized parameter: " + args[i]);

		}
		
	}

	public static int getScreenId()
	{
		return screenId;
	}
	
	
}
