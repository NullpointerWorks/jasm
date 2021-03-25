package com.nullpointerworks.jasm.asm.translator.builder.ctrlflow;

import com.nullpointerworks.jasm.asm.ASMInstruction;

public class CallTranslator extends GenericJumpTranslator
{
	private final String syntax = "\n  call <label>";
	
	public CallTranslator()
	{
		initTranslator(syntax, "call", ASMInstruction.CALL);
	}
}
