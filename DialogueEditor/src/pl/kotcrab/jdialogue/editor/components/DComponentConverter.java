/*******************************************************************************
 * DialogueEditor
 * Copyright (C) 2013-2014 Pawel Pastuszak
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package pl.kotcrab.jdialogue.editor.components;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import pl.kotcrab.jdialogue.editor.EditorException;
import pl.kotcrab.jdialogue.editor.components.types.CallbackCheckComponent;
import pl.kotcrab.jdialogue.editor.components.types.CallbackComponent;
import pl.kotcrab.jdialogue.editor.components.types.ChoiceComponent;
import pl.kotcrab.jdialogue.editor.components.types.EndComponent;
import pl.kotcrab.jdialogue.editor.components.types.RandomComponent;
import pl.kotcrab.jdialogue.editor.components.types.RelayComponent;
import pl.kotcrab.jdialogue.editor.components.types.StartComponent;
import pl.kotcrab.jdialogue.editor.components.types.TextComponent;
import pl.kotcrab.jdialogue.editor.project.PCallback;
import pl.kotcrab.jdialogue.editor.project.PCharacter;

public class DComponentConverter implements Converter {
	public static boolean exportMode = false;

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert (Class type) {
		//@formatter:off
		if(type.equals(TextComponent.class)
		|| type.equals(ChoiceComponent.class)
		|| type.equals(RelayComponent.class)
		|| type.equals(StartComponent.class)
		|| type.equals(EndComponent.class)
		|| type.equals(CallbackComponent.class)
		|| type.equals(CallbackCheckComponent.class)
		|| type.equals(RandomComponent.class)
		)
		//@formatter:on
			return true;
		else
			return false;
	}

	@Override
	public void marshal (Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		DComponent comp = (DComponent) source;

		if (exportMode) {
			writer.startNode("id");
			writer.setValue(Integer.toString(comp.getId()));
			writer.endNode();

			Connector[] outputs = comp.getOutputs();

			for (int i = 0; i < outputs.length; i++) {
				writer.startNode("target" + i);
				writer.setValue(Integer.toString(outputs[i].getTarget().getParrentComponent().getId()));
				writer.endNode();

			}

			Object[][] data = comp.getTableModel().getData();

			if (data != null) {
				for (int i = 0; i < data.length; i++) {
					if (data[i][0].equals("Info")) continue;
					if (data[i][0].equals("Choices text")) continue;
					if (data[i][0].equals("Outputs")) continue;
					if (data[i][0].equals("Inputs")) continue;
					if (data[i][0].equals("Invisible connection")) continue;

					String nodeName = data[i][0].toString().toLowerCase();

					writer.startNode(nodeName);

					if (nodeName.equals("character")) {
						PCharacter charater = (PCharacter) data[i][1];
						writer.setValue(String.valueOf(charater.getId()));
					} else if (nodeName.equals("callback")) {
						PCallback callback = (PCallback) data[i][1];
						writer.setValue(String.valueOf(callback.getId()));
					} else
						writer.setValue(data[i][1].toString());

					writer.endNode();
				}
			}

			if (comp instanceof ChoiceComponent) {
				ChoiceComponent choiceComp = (ChoiceComponent) comp;
				writer.startNode("choiceData");
				context.convertAnother(choiceComp.getChoices().getChoicesTable());
				writer.endNode();
			}
		} else {
			if (comp.getClass().equals(TextComponent.class)) // probably not needed
				writer.addAttribute("type", "text");
			else if (comp.getClass().equals(ChoiceComponent.class))
				writer.addAttribute("type", "choice");
			else if (comp.getClass().equals(RelayComponent.class))
				writer.addAttribute("type", "relay");
			else if (comp.getClass().equals(StartComponent.class))
				writer.addAttribute("type", "start");
			else if (comp.getClass().equals(EndComponent.class))
				writer.addAttribute("type", "end");
			else if (comp.getClass().equals(CallbackComponent.class))
				writer.addAttribute("type", "callback");
			else if (comp.getClass().equals(CallbackCheckComponent.class))
				writer.addAttribute("type", "callbackCheck");
			else if (comp.getClass().equals(RandomComponent.class))
				writer.addAttribute("type", "random");
			else
				writer.addAttribute("type", "unknown");

			writer.startNode("x");
			writer.setValue(Integer.toString(comp.getX()));
			writer.endNode();

			writer.startNode("y");
			writer.setValue(Integer.toString(comp.getY()));
			writer.endNode();

			writer.startNode("outputs");
			context.convertAnother(comp.getOutputs());
			writer.endNode();

			writer.startNode("inputs");
			context.convertAnother(comp.getInputs());
			writer.endNode();

			writer.startNode("data");
			context.convertAnother(comp.getTableModel().getData());
			writer.endNode();

			if (comp instanceof ChoiceComponent) {
				ChoiceComponent choiceComp = (ChoiceComponent) comp;
				writer.startNode("choicesData");
				context.convertAnother(choiceComp.getChoices());
				writer.endNode();
			}
		}
	}

	@Override
	public Object unmarshal (HierarchicalStreamReader reader, UnmarshallingContext context) {
		String type = reader.getAttribute("type");
		DComponent comp;

		if (type.equals("text"))
			comp = new TextComponent(0, 0);
		else if (type.equals("choice"))
			comp = new ChoiceComponent(0, 0);
		else if (type.equals("relay"))
			comp = new RelayComponent(0, 0);
		else if (type.equals("start"))
			comp = new StartComponent(0, 0);
		else if (type.equals("end"))
			comp = new EndComponent(0, 0);
		else if (type.equals("callback"))
			comp = new CallbackComponent(0, 0);
		else if (type.equals("callbackCheck"))
			comp = new CallbackCheckComponent(0, 0);
		else if (type.equals("random"))
			comp = new RandomComponent(0, 0);
		else
			throw new EditorException("Error while loading XML file. Unrecognized component type: " + type); // TODO change to normaln exception from runtime exception

		reader.moveDown();
		int x = Integer.valueOf(reader.getValue());
		comp.setX(x);
		reader.moveUp();

		reader.moveDown();
		int y = Integer.valueOf(reader.getValue());
		comp.setY(y);
		reader.moveUp();

		reader.moveDown();
		Connector[] outputs = (Connector[]) context.convertAnother(comp, Connector[].class);
		comp.setOutputs(outputs);
		reader.moveUp();

		reader.moveDown();
		Connector[] inputs = (Connector[]) context.convertAnother(comp, Connector[].class);
		comp.setInputs(inputs);
		reader.moveUp();

		reader.moveDown();
		Object[][] data = (Object[][]) context.convertAnother(comp, Object[][].class);
		comp.setTableModelData(data);
		reader.moveUp();

		if (comp instanceof ChoiceComponent) {
			ChoiceComponent choiceComp = (ChoiceComponent) comp;
			reader.moveDown();
			choiceComp.setChoices((ChoiceComponentChoices) context.convertAnother(comp, ChoiceComponentChoices.class));
			reader.moveUp();
		}

		comp.setup();

		return comp;
	}
}
