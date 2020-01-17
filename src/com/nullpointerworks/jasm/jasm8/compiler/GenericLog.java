package com.nullpointerworks.jasm.jasm8.compiler;

import java.io.IOException;

import com.nullpointerworks.jasm.jasm8.LogListener;

import com.nullpointerworks.util.Log;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

public class GenericLog implements LogListener 
{
	private TextFile tfLog = null;
	
	public GenericLog()
	{
		tfLog = new TextFile();
		tfLog.setEncoding("UTF-8");
	}
	
	@Override
	public void print(String msg) 
	{
		if (tfLog!=null) tfLog.addLine(msg);
		System.out.print(msg);
	}
	
	@Override
	public void println(String msg) 
	{
		if (tfLog!=null) tfLog.addLine(msg+"\n");
		Log.out(msg);
	}
	
	@Override
	public void error(String msg) 
	{
		if (tfLog!=null) tfLog.addLine(" error >> "+msg+"\n");
		Log.err(" error >> "+msg);
	}
	
	@Override
	public void save(String path) 
	{
		try 
		{
			TextFileParser.write(path, tfLog);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
