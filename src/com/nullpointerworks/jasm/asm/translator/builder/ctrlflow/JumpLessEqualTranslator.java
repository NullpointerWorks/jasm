package com.nullpointerworks.jasm.asm.translator.builder.ctrlflow;

import com.nullpointerworks.jasm.asm.ASMInstruction;

public class JumpLessEqualTranslator extends GenericJumpTranslator
{
	private final String syntax = "\n  jle <label>";
	
	public JumpLessEqualTranslator()
	{
		initTranslator(syntax, "jle", ASMInstruction.JLE);
	}
}
