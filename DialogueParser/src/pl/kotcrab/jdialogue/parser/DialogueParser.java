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

package pl.kotcrab.jdialogue.parser;

import pl.kotcrab.jdialogue.loader.Loader;

public abstract class DialogueParser
{
	public static final int INFINITY = -1;
	
	int maxChars;
	
	public DialogueParser(Loader projectFile, int maxChars)
	{
		this.maxChars = maxChars;
	}
	
	public abstract void startSequence();
	
	public abstract ComponentType getNextType();
	
	public abstract void nextComponent(int target);
	public abstract void nextComponent();
	
	//==============================FOR TEXT/CHOICE COMPONENT==================================
	public abstract String getNextMsg();
	public abstract boolean isCurrentMsgFinished();
	
	//==============================FOR CHOICE COMPONENT ONLY==================================
	public abstract String[] getChoiceData();
	
	//==============================FOR RANDOM COMPONENT ONLY==================================
	public abstract void processRandom();
	
	public abstract String getNextCharacterData();
	public abstract String getNextData();
}