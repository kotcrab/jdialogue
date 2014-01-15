package pl.kotcrab.dialoguelib.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.thoughtworks.xstream.XStream;

public class IOUtils
{
	public static Object loadNormal(XStream xstream, File file)
	{
		Object result = null;
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			result = xstream.fromXML(reader);
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static Object loadGzip(XStream xstream, File file)
	{
		Object result = null;
		
		try
		{
			GZIPInputStream zIn = new GZIPInputStream(new FileInputStream(file));
			BufferedReader reader = new BufferedReader(new InputStreamReader(zIn));
			result = xstream.fromXML(reader);
			reader.close();
			zIn.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void saveNormal(XStream xstream, File file, Object object)
	{
		try
		{
			FileOutputStream fOut = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(fOut, "UTF-8");
			xstream.toXML(object, writer);
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void saveGzip(XStream xstream, File file, Object object)
	{
		try
		{
			GZIPOutputStream zOut = new GZIPOutputStream(new FileOutputStream(file));
			OutputStreamWriter writer = new OutputStreamWriter(zOut, "UTF-8");
			xstream.toXML(object, writer);
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
