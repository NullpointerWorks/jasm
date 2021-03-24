package com.nullpointerworks.jasm.asm.assembler;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.translator.Allocation;
import com.nullpointerworks.jasm.asm.translator.Definition;
import com.nullpointerworks.jasm.asm.translator.Label;
import com.nullpointerworks.jasm.asm.translator.Translation;

public class TranslationAssembler implements Assembler 
{
	private List<Integer> bytecode;
	private List<BuildError> errors;
	private VerboseListener verbose;
	
	@Override
	public void setVerboseListener(VerboseListener v) 
	{
		bytecode = new ArrayList<Integer>();
		errors = new ArrayList<BuildError>();
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
	public List<Integer> getMachineCode() 
	{
		return bytecode;
	}
	
	@Override
	public void assemble(	List<Translation> translation, 
							List<Definition> definitions, 
							List<Allocation> allocations,
							List<Label> labels)
	{
		
		
		
		
		
		
		
		
		
		
	}
	
	private Definition findDefinition(List<Definition> definitions, String name)
	{
		for (Definition d : definitions)
		{
			if (d.getName().equalsIgnoreCase(name))
			{
				return d;
			}
		}
		return null;
	}
	
	
}
