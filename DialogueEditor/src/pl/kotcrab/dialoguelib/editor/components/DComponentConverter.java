package pl.kotcrab.dialoguelib.editor.components;

import pl.kotcrab.dialoguelib.editor.EditorException;
import pl.kotcrab.dialoguelib.editor.components.types.CallbackComponent;
import pl.kotcrab.dialoguelib.editor.components.types.ChoiceComponent;
import pl.kotcrab.dialoguelib.editor.components.types.EndComponent;
import pl.kotcrab.dialoguelib.editor.components.types.RandomComponent;
import pl.kotcrab.dialoguelib.editor.components.types.RelayComponent;
import pl.kotcrab.dialoguelib.editor.components.types.StartComponent;
import pl.kotcrab.dialoguelib.editor.components.types.TextComponent;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DComponentConverter implements Converter
{
	
	public DComponentConverter()
	{
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean canConvert(Class type)
	{
		//@formatter:off
		if(type.equals(TextComponent.class)
		|| type.equals(ChoiceComponent.class)
		|| type.equals(RelayComponent.class)
		|| type.equals(StartComponent.class)
		|| type.equals(EndComponent.class)
		|| type.equals(CallbackComponent.class)
		|| type.equals(RandomComponent.class)
		)
		//@formatter:on
			return true;
		else
			return false;
	}
	
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context)
	{
		DComponent comp = (DComponent) source;
		
		if(comp.getClass().equals(TextComponent.class))
			writer.addAttribute("type", "text");
		else if(comp.getClass().equals(ChoiceComponent.class))
			writer.addAttribute("type", "choice");
		else if(comp.getClass().equals(RelayComponent.class))
			writer.addAttribute("type", "relay");
		else if(comp.getClass().equals(StartComponent.class))
			writer.addAttribute("type", "start");
		else if(comp.getClass().equals(EndComponent.class))
			writer.addAttribute("type", "end");
		else if(comp.getClass().equals(CallbackComponent.class))
			writer.addAttribute("type", "callback");
		else if(comp.getClass().equals(RandomComponent.class))
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
		
		// writer.startNode(")
	}
	
	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context)
	{
		String type = reader.getAttribute("type");
		DComponent comp;
		
		if(type.equals("text"))
			comp = new TextComponent(0, 0, 0);
		else if(type.equals("choice"))
			comp = new ChoiceComponent(0, 0, 0);
		else if(type.equals("relay"))
			comp = new RelayComponent(0, 0, 0);
		else if(type.equals("start"))
			comp = new StartComponent(0, 0);
		else if(type.equals("end"))
			comp = new EndComponent(0, 0, 0);
		else if(type.equals("callback"))
			comp = new CallbackComponent(0, 0, 0);
		else if(type.equals("random"))
			comp = new RandomComponent(0, 0, 0);
		else
			throw new EditorException("Error while loading XML file. Unrecognized component type: " + type); //TODO change to normaln exception from runtime exception
			
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
		
		return comp;
	}
}