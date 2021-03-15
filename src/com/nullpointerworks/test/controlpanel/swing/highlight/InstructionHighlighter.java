package com.nullpointerworks.test.controlpanel.swing.highlight;

import java.awt.Color;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;

public class InstructionHighlighter implements HighlightValidator 
{
	private final Color INSTRUCTION = new Color(0, 0, 255);
	
	@Override
	public boolean isValid(String token) 
	{
		if (token.equals("nop")) return true;
		if (token.equals("int")) return true;
		
		if (token.equals("or")) return true;
		if (token.equals("and")) return true;
		if (token.equals("xor")) return true;
		
		if (token.equals("load")) return true;
		if (token.equals("push")) return true;
		if (token.equals("pop")) return true;
		
		if (token.equals("jmp")) return true;
		if (token.equals("call")) return true;
		if (token.equals("ret")) return true;
		if (token.equals("je")) return true;
		if (token.equals("jne")) return true;
		if (token.equals("jl")) return true;
		if (token.equals("jle")) return true;
		if (token.equals("jg")) return true;
		if (token.equals("jge")) return true;
		
		if (token.equals("add")) return true;
		if (token.equals("sub")) return true;
		if (token.equals("cmp")) return true;
		if (token.equals("inc")) return true;
		if (token.equals("dec")) return true;
		if (token.equals("neg")) return true;
		if (token.equals("shl")) return true;
		if (token.equals("shr")) return true;
		return false;
	}

	@Override
	public void setHighlight(MutableAttributeSet asNew) 
	{
		StyleConstants.setForeground(asNew, INSTRUCTION);
	}
}
