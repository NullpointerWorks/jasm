package com.nullpointerworks.test.controlpanel.gui.swing.highlight;

import java.awt.Color;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;

public class DeclarationHighlighter implements HighlightValidator 
{
	private final Color DECLARATION = new Color(128, 0, 255);
	
	@Override
	public boolean isValid(String token) 
	{
		if (token.equals(".def")) return true;
		if (token.equals(".inc")) return true;
		if (token.equals(".org")) return true;
		return false;
	}

	@Override
	public void setHighlight(MutableAttributeSet asNew) 
	{
		StyleConstants.setForeground(asNew, DECLARATION);
		StyleConstants.setItalic(asNew, true);
	}
}
