package com.nullpointerworks.logger;

public class LoggerFactory 
{
	public static Logger getLogger()
	{
		return new GenericLogger();
	}
}
