package com.nullpointerworks;

import java.io.InputStream;
import java.net.URL;

public class Loader 
{
	private static Loader instance = null;
	private static Loader getInstance()
	{
		if (instance==null) instance = new Loader();
		return instance;
	}
	
	public static URL getResource(String path)
	{
		return getInstance().getClass().getResource(path);
	}
	
	public static InputStream getResourceAsStream(String path)
	{
		return getInstance().getClass().getResourceAsStream(path);
	}
}
