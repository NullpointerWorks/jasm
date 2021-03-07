package com.nullpointerworks.test.controlpanel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class HighlightedJTextPane extends JTextPane
{
	private static final long serialVersionUID = 6407970407357102776L;
	final Font fCourier = new Font("Courier New", Font.PLAIN, 14);
	
	
	public HighlightedJTextPane()
	{
		
		
		
		
		
	}
	
	/*
	 * convoluted way of adding text to the textpane
	 */
	public void append(String str)
	{
		StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.black);
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
		
		int len = getDocument().getLength();
        setCaretPosition(len);
        setCharacterAttributes(aset, false);
        replaceSelection(str);
	}
	
	/*
	 * scan for text tokens and highlight them accordingly
	 */
	public void updateHighlight()
	{
		String text = getText();
		int leng = text.length();
		int start = 0;
		String token = "";
		for (int i=0, l=leng; i<l; i++)
		{
			String character = text.substring(i, i+1);
			if (character.equals("\n")) character = " ";
			if (character.equals(",")) character = " ";
			if (character.equals(" "))
			{
				if (token.length() > 0)
					highlightToken(start, token);
				
				start = i;
				token = "";
				continue;
			}
			token += character;
		}
	}

	private void highlightToken(int offset, String token) 
	{
		//System.out.println("highlighting: "+token);
		
		StyledDocument doc = (StyledDocument)getDocument();
		Element element = doc.getCharacterElement(offset);
	    AttributeSet as = element.getAttributes();
	    MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes());
	    
	    StyleConstants.setForeground(asNew, Color.blue);
	    
	    doc.setCharacterAttributes(offset, offset + token.length(), asNew, true);
	}
	
	
	
}
