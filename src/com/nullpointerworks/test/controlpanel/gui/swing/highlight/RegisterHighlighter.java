package com.nullpointerworks.test.controlpanel.gui.swing.highlight;

import java.awt.Color;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;

public class RegisterHighlighter implements HighlightValidator 
{
	private final Color REGISTER = new Color(0, 170, 0);
	
	@Override
	public boolean isValid(String token) 
	{
		if (token.equals("ip")) return true;
		if (token.equals("sp")) return true;
		
		if (token.equals("a")) return true;
		if (token.equals("b")) return true;
		if (token.equals("c")) return true;
		if (token.equals("d")) return true;
		
		if (token.equals("r0")) return true;
		if (token.equals("r1")) return true;
		if (token.equals("r2")) return true;
		if (token.equals("r3")) return true;
		if (token.equals("r4")) return true;
		if (token.equals("r5")) return true;
		if (token.equals("r6")) return true;
		if (token.equals("r7")) return true;
		if (token.equals("r8")) return true;
		if (token.equals("r9")) return true;
		return false;
	}

	@Override
	public void setHighlight(MutableAttributeSet asNew) 
	{
		StyleConstants.setForeground(asNew, REGISTER);
	}
}
