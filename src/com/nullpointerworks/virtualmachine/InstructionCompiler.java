package com.nullpointerworks.virtualmachine;

import com.nullpointerworks.jasm.Draft;
import com.nullpointerworks.jasm.compiler.CompilerJASM;
import com.nullpointerworks.jasm.compiler.SourceCode;
import com.nullpointerworks.jasm.virtualmachine.instruction.Instruction;

public class InstructionCompiler extends CompilerJASM<Instruction>
{
	@Override
	public Draft<Instruction> compile(int index, SourceCode loc) 
	{
		return new InstructionDraft(index,loc);
	}
}
