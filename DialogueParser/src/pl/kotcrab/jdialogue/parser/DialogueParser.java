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

import pl.kotcrab.jdialogue.loader.DialogueLoader;

public abstract class DialogueParser
{
	public static final int INFINITY = -1;
	
	protected int maxChars;
	
	public DialogueParser(DialogueLoader projectFile, int maxChars)
	{
		this.maxChars = maxChars;
	}
	
	public abstract void startSequence(String name);
	
	public abstract ComponentType processNextComponent();
	
	public abstract void moveToNextComponent(int target);
	public abstract void moveToNextComponent();
	
	public abstract int getId();
	
	//===================================FOR TEXT COMPONENT====================================
	public abstract PCharacter getCharacterData();
	
	//==============================FOR TEXT/CHOICE COMPONENT==================================
	public abstract String getMsg();
	public abstract boolean isCurrentMsgFinished();
	
	//==============================FOR CHOICE COMPONENT ONLY==================================
	public abstract String[] getChoiceData();
	
	//==============================FOR CALLBACK HADNLING======================================
	public abstract void addCallbackListener(CallbackListener listener);
	public abstract boolean removeCallbackListener(CallbackListener listener);
}