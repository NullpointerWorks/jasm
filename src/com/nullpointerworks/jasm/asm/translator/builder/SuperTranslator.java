package com.nullpointerworks.jasm.asm.translator.builder;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.error.TranslationError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.translator.Translation;

public class SuperTranslator
{
	private BuildError error;
	
	private List<CodeTranslator> translators;
	
	public SuperTranslator()
	{
		error = null;
		translators = new ArrayList<CodeTranslator>();
		translators.add( new NopTranslator() );
		translators.add( new IntTranslator() );
		translators.add( new JumpTranslator() );
		
	}
	
	public boolean hasErrors()
	{
		return error != null;
	}
	
	public BuildError getError() 
	{
		return error;
	}
	
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
		for (CodeTranslator translator : translators)
		{
			if (translator.isInstruction(instruct))
			{
				List<Translation> trlist = translator.getTranslation(sc);
				if (translator.hasErrors())
				{
					error = translator.getError();
				}
				
				for (Translation tr : trlist)
				{
					translation.add(tr);
				}
				
				break;
			}
		}
		
		return translation;
	}
}
