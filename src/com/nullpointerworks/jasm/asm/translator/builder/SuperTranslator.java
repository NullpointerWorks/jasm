package com.nullpointerworks.jasm.asm.translator.builder;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.error.TranslationError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.translator.Translation;

public class SuperTranslator implements CodeTranslator 
{
	private BuildError error;
	private SystemTranslator sysTranslator;
	
	public SuperTranslator()
	{
		error = null;
		sysTranslator = new SystemTranslator();
		
	}
	
	@Override
	public boolean hasErrors()
	{
		return error != null;
	}

	@Override
	public BuildError getError() 
	{
		return error;
	}
	
	@Override
	public boolean hasOperation(String instruct) 
	{
		if (sysTranslator.hasOperation(instruct)) return true;
		return true;
	}

	@Override
	public List<Translation> getTranslation(SourceCode sc)
	{
		String line = sc.getLine();
		String[] tokens = line.split(" ");
		if (tokens.length < 1)
		{
			error = new TranslationError(sc, ""); // TODO
			return null;
		}
		
		String instruct = tokens[0];
		if (instruct.length() < 1)
		{
			error = new TranslationError(sc, ""); // TODO
			return null;
		}
		
		List<Translation> translation = new ArrayList<Translation>();
		if (sysTranslator.hasOperation(instruct)) 
		{
			var list = sysTranslator.getTranslation(sc);
			if (sysTranslator.hasErrors())
			{
				error = sysTranslator.getError();
				return translation;
			}
			for (Translation tr : list) translation.add(tr);
		}
		
		
		
		
		
		
		return translation;
	}

}
