package com.nullpointerworks.jasm.asm.translator.builder.ctrlflow;

import com.nullpointerworks.jasm.asm.ASMInstruction;

public class JumpTranslator extends GenericJumpTranslator
{
	private final String syntax = "\n  jmp <label>";
	
	public JumpTranslator()
	{
		initTranslator(syntax, "jmp", ASMInstruction.JMP);
	}
}
