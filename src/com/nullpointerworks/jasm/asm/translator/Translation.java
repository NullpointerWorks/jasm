package com.nullpointerworks.jasm.asm.translator;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class Translation 
{
	private SourceCode source;
	private Instruction inst = Instruction.NULL;
	private List<Operand> operands;
	private String label;
	
	public Translation(SourceCode sc) 
	{
		source = sc;
		operands = new ArrayList<Operand>();
		label = null;
	}
	
	public SourceCode getSourceCode()
	{
		return source;
	}
	
	public Instruction getInstruction()
	{
		return inst;
	}
	
	public Operand getOperand(int i)
	{
		return operands.get(i);
	}
	
	public List<Operand> getOperands()
	{
		return operands;
	}
	
	public boolean hasLabel()
	{
		return label != null;
	}
	
	public void setLabel(String l) 
	{
		label = l;
	}
	
	public String getLabel() 
	{
		return label;
	}
}
