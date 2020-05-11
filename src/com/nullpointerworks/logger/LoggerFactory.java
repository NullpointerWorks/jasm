package com.nullpointerworks.logger;

public class LoggerFactory 
{
	public static Logger getNullLogger()
	{
		return new NullLogger();
	}
	
	public static Logger getLogger()
	{
		return new GenericLogger();
	}
}
