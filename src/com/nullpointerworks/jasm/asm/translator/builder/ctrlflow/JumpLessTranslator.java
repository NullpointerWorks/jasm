package com.nullpointerworks.jasm.asm.translator.builder.ctrlflow;

import com.nullpointerworks.jasm.asm.ASMInstruction;

public class JumpLessTranslator extends GenericJumpTranslator
{
	private final String syntax = "\n  jl <label>";
	
	public JumpLessTranslator()
	{
		initTranslator(syntax, "jl", ASMInstruction.JL);
	}
}
