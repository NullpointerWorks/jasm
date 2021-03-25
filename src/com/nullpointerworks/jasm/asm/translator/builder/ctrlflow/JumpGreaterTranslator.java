package com.nullpointerworks.jasm.asm.translator.builder.ctrlflow;

import com.nullpointerworks.jasm.asm.ASMInstruction;

public class JumpGreaterTranslator extends GenericJumpTranslator
{
	private final String syntax = "\n  jg <label>";
	
	public JumpGreaterTranslator()
	{
		initTranslator(syntax, "jg", ASMInstruction.JG);
	}
}
