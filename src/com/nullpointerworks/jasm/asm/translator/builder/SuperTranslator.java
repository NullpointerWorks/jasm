package com.nullpointerworks.jasm.asm.translator.builder;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.ASMInstruction;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.error.TranslationError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.translator.Translation;
import com.nullpointerworks.jasm.asm.translator.builder.arith.*;
import com.nullpointerworks.jasm.asm.translator.builder.ctrlflow.*;
import com.nullpointerworks.jasm.asm.translator.builder.dataflow.*;
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
		translators.add( new RetTranslator() );
		translators.add( new JumpTranslator("call", ASMInstruction.CALL) );
		translators.add( new JumpTranslator("je", ASMInstruction.JE) );
		translators.add( new JumpTranslator("jne", ASMInstruction.JNE) );
		translators.add( new JumpTranslator("jl", ASMInstruction.JL) );
		translators.add( new JumpTranslator("jle", ASMInstruction.JLE) );
		translators.add( new JumpTranslator("jg", ASMInstruction.JG) );
		translators.add( new JumpTranslator("jge", ASMInstruction.JGE) );
		
		translators.add( new LoadTranslator() );
		
		
		translators.add( new AddTranslator() );
		translators.add( new SubTranslator() );
		translators.add( new CmpTranslator() );
		translators.add( new IncTranslator() );
		translators.add( new DecTranslator() );
		translators.add( new NegTranslator() );
		translators.add( new ShlTranslator() );
		translators.add( new ShrTranslator() );
		
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
		
		String instruct = tokens[0].toLowerCase();
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
