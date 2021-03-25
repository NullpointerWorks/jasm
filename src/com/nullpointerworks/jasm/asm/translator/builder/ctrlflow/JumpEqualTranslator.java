package com.nullpointerworks.jasm.asm.translator.builder.ctrlflow;

import com.nullpointerworks.jasm.asm.translator.Instruction;

public class JumpEqualTranslator extends GenericJumpTranslator
{
	private final String syntax = "\n  je <label>";
	
	public JumpEqualTranslator()
	{
		initTranslator(syntax, "je", Instruction.JE);
	}
}
