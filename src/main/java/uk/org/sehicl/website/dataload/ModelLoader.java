package uk.org.sehicl.website.dataload;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import uk.org.sehicl.website.Constants;
import uk.org.sehicl.website.data.Model;

public class ModelLoader
{
	private static FileLoader fileLoader = new ClasspathFileLoader();
	private static final Map<Integer, Model> models = new HashMap<>();
	public final static int DEFAULT_SEASON = Constants.CURRENT_SEASON;

	public static void setFileLoader(FileLoader loader)
	{
		fileLoader = loader;
	}
	
	public static Model getModel()
	{
	    return getModel(DEFAULT_SEASON);
	}

	public static Model getModel(int season)
	{
	    Model answer = models.get(season);
	    if (answer == null)
	    {
	        String fileName = String.format("data/20%02d-%02d.xml", season - 1, season);
	        InputStream str = fileLoader.getStream(fileName);
	        try (InputStream source = str)
	        {
	            ObjectMapper xmlMapper = new XmlMapper();
	            xmlMapper.setTimeZone(TimeZone.getDefault());
	            answer = xmlMapper.readValue(source, Model.class);
	        }
	        catch (Exception ex)
	        {
	            throw new RuntimeException(String.format("Unable to load model file %s", fileName),
	                    ex);
	        }
	    }
	    return answer;
	}
}
