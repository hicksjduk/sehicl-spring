package uk.org.sehicl.website.dataload;

import java.io.InputStream;

public interface FileLoader
{
	InputStream getStream(String fileName);
}
