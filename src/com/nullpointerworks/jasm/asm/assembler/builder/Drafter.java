package com.nullpointerworks.jasm.asm.assembler.builder;

import java.util.List;

import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.translator.Allocation;
import com.nullpointerworks.jasm.asm.translator.Definition;
import com.nullpointerworks.jasm.asm.translator.Instruction;
import com.nullpointerworks.jasm.asm.translator.Label;
import com.nullpointerworks.jasm.asm.translator.Translation;

public interface Drafter 
{
	boolean hasError();
	
	BuildError getError();
	
	boolean isInstruction(Instruction instruct);
	
	Draft draft(Translation translation, 
				List<Definition> defs, 
				List<Allocation> allocs,
				List<Label> lbls);
	
}
