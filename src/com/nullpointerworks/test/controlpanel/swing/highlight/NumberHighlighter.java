package com.nullpointerworks.test.controlpanel.swing.highlight;

import java.awt.Color;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;

public class NumberHighlighter implements HighlightValidator 
{
	private final String INTEGER = "[+-]?\\d+";
	private final String HEXADEC = "^(0x|0X|#).[0-9A-Fa-f]+";
	
	@Override
	public boolean isValid(String token) 
	{
		if (isInteger(token)) return true;
		if (isHexadec(token)) return true;
		return false;
	}

	@Override
	public void setHighlight(MutableAttributeSet asNew) 
	{
		StyleConstants.setForeground(asNew, Color.RED);
	}
	
	private boolean isInteger(String str) {return str.matches(INTEGER);}
	private boolean isHexadec(String str) {return str.matches(HEXADEC);}
}
