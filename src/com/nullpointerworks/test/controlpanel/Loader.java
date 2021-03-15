package com.nullpointerworks.test.controlpanel;

import java.io.InputStream;
import java.net.URL;

/*
 * responsible for loading resources. the path of this class is important. do not refactor.
 */
public final class Loader 
{
	public static URL getResource(String path)
	{
		return instance.getClass().getResource(path);
	}
	
	public static InputStream getResourceAsStream(String path)
	{
		return instance.getClass().getResourceAsStream(path);
	}
	
	private Loader() {}
	private static Loader instance = new Loader();
}
