package com.nullpointerworks.test.controlpanel.swing.highlight;

import javax.swing.text.MutableAttributeSet;

public interface HighlightValidator 
{
	public boolean isValid(String token);
	public void setHighlight(MutableAttributeSet asNew);
}
