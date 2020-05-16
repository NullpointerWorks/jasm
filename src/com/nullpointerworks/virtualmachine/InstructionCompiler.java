package com.nullpointerworks.virtualmachine;

import com.nullpointerworks.jasm.compiler.Draft;
import com.nullpointerworks.jasm.compiler.DraftJASM;
import com.nullpointerworks.jasm.compiler.CompilerJASM;
import com.nullpointerworks.jasm.parser.SourceCode;
import com.nullpointerworks.jasm.virtualmachine.instruction.Instruction;

public class InstructionCompiler extends CompilerJASM<Instruction>
{
	@Override
	public Draft<Instruction> compile(int index, SourceCode loc) 
	{
		return new DraftJASM(index,loc);
	}
}
