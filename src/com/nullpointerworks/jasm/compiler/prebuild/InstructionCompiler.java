package com.nullpointerworks.jasm.compiler.prebuild;

import com.nullpointerworks.jasm.compiler.AbstractCompiler;
import com.nullpointerworks.jasm.compiler.Draft;
import com.nullpointerworks.jasm.compiler.SourceCode;
import com.nullpointerworks.jasm.virtualmachine.Instruction;

/**
 * 
 * 
 * @author Michiel Drost - Nullpointer Works
 */
public class InstructionCompiler extends AbstractCompiler<Instruction>
{
	@Override
	public Draft<Instruction> compile(int index, SourceCode loc) 
	{
		return new InstructionDraft(index,loc);
	}
}
