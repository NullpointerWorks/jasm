package com.nullpointerworks.jasm.asm.assembler;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.assembler.builder.DraftAssembler;
import com.nullpointerworks.jasm.asm.assembler.builder.SuperDraftAssembler;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.translator.Allocation;
import com.nullpointerworks.jasm.asm.translator.Definition;
import com.nullpointerworks.jasm.asm.translator.Instruction;
import com.nullpointerworks.jasm.asm.translator.Label;
import com.nullpointerworks.jasm.asm.translator.Translation;
import com.nullpointerworks.jasm.asm.translator.Number;

public class TranslationAssembler implements Assembler 
{
	private List<Number> temporary;
	private List<Integer> bytecode;
	private List<BuildError> errors;
	private VerboseListener verbose;
	private DraftAssembler drafter;
	
	public TranslationAssembler()
	{
		temporary = new ArrayList<Number>();
		bytecode = new ArrayList<Integer>();
		errors = new ArrayList<BuildError>();
		verbose = (str)->{};
		drafter = new SuperDraftAssembler();
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
		temporary.clear();
		bytecode.clear();
		errors.clear();
		
		for (Translation tr : translation)
		{
			if (hasErrors()) return;
			
			Instruction inst = tr.getInstruction();
			if (!drafter.hasOperation(inst))
			{
				//error
				return;
			}
			
			Draft d = drafter.draft(tr, definitions, allocations, labels);
			if (drafter.hasError())
			{
				errors.add( drafter.getError() );
				continue;
			}
			
			
			
			
		}
		
	}
	
	
	
	
	
	
}
