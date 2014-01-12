/*******************************************************************************
 * Copyright 2013 - 2014 Pawel Pastuszak
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

package pl.kotcrab.dialoguelib.editor.components.types;

import pl.kotcrab.dialoguelib.editor.components.ComponentTableModel;
import pl.kotcrab.dialoguelib.editor.components.DComponent;

public class CallbackComponent extends DComponent
{
	public CallbackComponent(int x, int y, int id)
	{
		super("Callback", x, y, 1, 1, id);
		tableModel = new ComponentTableModel(
			//@formatter:off
			new Object[][]
				{
				    {"Callback Text", "Set Text"},
				}
			//@formatter:on
			);
	}
}
