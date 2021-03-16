package com.nullpointerworks.test.controlpanel.gui.swing.highlight;

import java.awt.Color;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;

public class DefaultHighlighter implements HighlightValidator 
{
	@Override
	public boolean isValid(String token) 
	{
		return true;
	}

	@Override
	public void setHighlight(MutableAttributeSet asNew) 
	{
		StyleConstants.setBackground(asNew, Color.WHITE);
		StyleConstants.setForeground(asNew, Color.BLACK);
	}
}
