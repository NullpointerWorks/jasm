package com.nullpointerworks.logger;

public class GenericLogger implements Logger
{
	@Override
	public void info(String message) 
	{
		System.out.println(message);
	}

	@Override
	public void error(String message) 
	{
		System.err.println(message);
	}

}
