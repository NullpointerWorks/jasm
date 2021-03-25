package com.nullpointerworks.jasm.asm.translator.builder;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.error.TranslationError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.translator.Translation;
import com.nullpointerworks.jasm.asm.translator.builder.ctrlflow.*;
import com.nullpointerworks.jasm.asm.translator.builder.sys.*;

public class SuperTranslator implements CodeTranslator
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
		translators.add( new CallTranslator() );
		translators.add( new RetTranslator() );
		translators.add( new JumpEqualTranslator() );
		translators.add( new JumpNotEqualTranslator() );
		translators.add( new JumpLessTranslator() );
		translators.add( new JumpLessEqualTranslator() );
		translators.add( new JumpGreaterTranslator() );
		translators.add( new JumpGreaterEqualTranslator() );
		
		
		
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
	public boolean isInstruction(String instruct) 
	{
		for (CodeTranslator translator : translators)
		{
			if (translator.isInstruction(instruct)) return true;
		}
		return false;
	}
	
	@Override
	public List<Translation> getTranslation(SourceCode sc)
	{
		error = null;
		
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
		
		if (!isInstruction(instruct))
		{
			error = new TranslationError(sc, "  Failed to find a translator for this instruction.");
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
