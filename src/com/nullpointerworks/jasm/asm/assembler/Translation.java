package com.nullpointerworks.jasm.asm.assembler;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.assembler.builder.Instruction;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class Translation 
{
	private SourceCode source;
	private Instruction inst = Instruction.NULL;
	private List<Operand> operands;
	
	public Translation(SourceCode sc) 
	{
		source = sc;
		operands = new ArrayList<Operand>();
		
		
		
	}
	
	
}
