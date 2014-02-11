/*******************************************************************************
    DialogueEditor
    Copyright (C) 2013-2014 Pawel Pastuszak

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package pl.kotcrab.jdialogue.editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;

public class IOUtils
{
	public static Object loadNormal(XStream xstream, File file)
	{
		Object result = null;
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
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
			BufferedReader reader = new BufferedReader(new InputStreamReader(zIn, "UTF-8"));
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
