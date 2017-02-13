package uk.org.sehicl.website.dataload;

import java.io.InputStream;

import uk.org.sehicl.website.data.Model;

public class ClasspathFileLoader implements FileLoader
{
	@Override
	public InputStream getStream(String fileName)
	{
        InputStream answer = Model.class.getClassLoader().getResourceAsStream(fileName);
        return answer;
	}
}
