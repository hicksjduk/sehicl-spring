package uk.org.sehicl.website.dataload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DirectoryFileLoader implements FileLoader
{
	private final String directoryName;

	public DirectoryFileLoader(String directoryName)
	{
		this.directoryName = directoryName;
	}

	@Override
	public InputStream getStream(String fileName)
	{
		try
		{
			InputStream answer = new FileInputStream(new File(directoryName, fileName));
			return answer;
		}
		catch (FileNotFoundException ex)
		{
			throw new RuntimeException(ex);
		}
	}
}
