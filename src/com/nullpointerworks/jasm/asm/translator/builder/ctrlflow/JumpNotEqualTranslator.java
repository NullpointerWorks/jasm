package com.nullpointerworks.jasm.asm.translator.builder.ctrlflow;

import com.nullpointerworks.jasm.asm.ASMInstruction;

public class JumpNotEqualTranslator extends GenericJumpTranslator
{
	private final String syntax = "\n  jne <label>";
	
	public JumpNotEqualTranslator()
	{
		initTranslator(syntax, "jne", ASMInstruction.JNE);
	}
}
