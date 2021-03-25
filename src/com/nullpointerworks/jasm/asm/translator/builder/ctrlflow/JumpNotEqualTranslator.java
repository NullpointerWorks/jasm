package com.nullpointerworks.jasm.asm.translator.builder.ctrlflow;

import com.nullpointerworks.jasm.asm.translator.Instruction;

public class JumpNotEqualTranslator extends GenericJumpTranslator
{
	private final String syntax = "\n  jne <label>";
	
	public JumpNotEqualTranslator()
	{
		initTranslator(syntax, "jne", Instruction.JNE);
	}
}
