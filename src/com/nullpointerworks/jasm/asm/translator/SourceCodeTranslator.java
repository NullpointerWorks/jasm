package com.nullpointerworks.jasm.asm.translator;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class SourceCodeTranslator implements Translator 
{
	private List<Translation> translation;
	private List<BuildError> errors;
	private VerboseListener verbose;
	
	public SourceCodeTranslator()
	{
		translation = new ArrayList<Translation>();
		errors = new ArrayList<BuildError>();
		verbose = (str)->{};
	}
	
	@Override
	public void setVerboseListener(VerboseListener v) 
	{
		verbose = v;
	}

	@Override
	public boolean hasErrors() 
	{
		return errors.size() > 0;
	}

	@Override
	public List<BuildError> getErrors() 
	{
		return errors;
	}
	
	@Override
	public List<Translation> getTranslation() 
	{
		return translation;
	}
	
	@Override
	public void translate(List<SourceCode> source) 
	{
		for(SourceCode sc : source)
		{
			process(sc);
		}
	}

	private void process(SourceCode sc) 
	{
		String line = sc.getLine();
		
		if (line.startsWith("."))
		{
			
			return;
		}
		
		if (line.contains(":"))
		{
			
			return;
		}
		
		
	}
}
