package com.nullpointerworks.logger;

public class NoLogger implements Logger
{
	@Override
	public void info(String message) {}

	@Override
	public void error(String message) {}

}
