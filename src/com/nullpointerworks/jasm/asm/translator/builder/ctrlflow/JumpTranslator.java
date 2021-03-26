package com.nullpointerworks.jasm.asm.translator.builder.ctrlflow;

import com.nullpointerworks.jasm.asm.ASMInstruction;

public class JumpTranslator extends GenericJumpTranslator
{
	public JumpTranslator()
	{
		String syntax = "\n  jmp <label>";
		initTranslator(syntax, "jmp", ASMInstruction.JMP);
	}
	
	public JumpTranslator(String trig, ASMInstruction inst)
	{
		String syntax = "\n  "+trig+" <label>";
		initTranslator(syntax, trig, inst);
	}
}
