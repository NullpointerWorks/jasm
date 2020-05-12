package com.nullpointerworks.logger;

public class LoggerFactory 
{
	public static Logger getNoLogger()
	{
		return new NoLogger();
	}
	
	public static Logger getLogger()
	{
		return new GenericLogger();
	}
}
