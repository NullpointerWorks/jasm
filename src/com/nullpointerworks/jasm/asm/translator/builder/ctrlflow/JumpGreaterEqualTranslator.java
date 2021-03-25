package com.nullpointerworks.jasm.asm.translator.builder.ctrlflow;

import com.nullpointerworks.jasm.asm.translator.Instruction;

public class JumpGreaterEqualTranslator extends GenericJumpTranslator
{
	private final String syntax = "\n  jge <label>";
	
	public JumpGreaterEqualTranslator()
	{
		initTranslator(syntax, "jge", Instruction.JGE);
	}
}
