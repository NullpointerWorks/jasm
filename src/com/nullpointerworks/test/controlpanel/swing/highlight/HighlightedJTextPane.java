package com.nullpointerworks.test.controlpanel.swing.highlight;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class HighlightedJTextPane extends JTextPane implements KeyListener
{
	private static final long serialVersionUID = 6407970407357102776L;

	private List<HighlightValidator> highlights;
	private HighlightValidator unrecognizedHilite;
	private AttributeSet aset;
	
	public HighlightedJTextPane()
	{
		StyleContext sc = StyleContext.getDefaultStyleContext();
        aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.black);
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
        
        highlights = new ArrayList<HighlightValidator>();
        highlights.add( new InstructionHighlighter() );
        highlights.add( new RegisterHighlighter() );
        highlights.add( new NumberHighlighter() );
        unrecognizedHilite = new UnrecognizedHighlighter();
        
		addKeyListener(this);
	}
	
	/*
	 * convoluted way of adding text to the textpane
	 */
	public void append(String str)
	{
		int len = getDocument().getLength();
        setCaretPosition(len);
        setCharacterAttributes(aset, false);
        replaceSelection(str);
	}
	
	public void appendLine(String str)
	{
		append(str+"\n");
	}
	
	/*
	 * scan for text tokens and highlight them accordingly
	 */
	public void updateHighlight()
	{
		String text = getText();
		text = text.replace("\r\n", " ");
		
		int leng = text.length();
		String token = "";
		for (int i=0, l=leng; i<l; i++)
		{
			String character = text.substring(i, i+1);
			if (character.equals(" ") || character.equals(","))
			{
				if (token.length()>0)
				{
					highlightToken(i-token.length(), token);
					token = "";
				}
				continue;
			}
			
			token += character;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) 
	{
		if (e.getKeyChar() == KeyEvent.VK_TAB) 
		{
			e.consume();
	        setCharacterAttributes(aset, false);
	        replaceSelection("    "); // add 4 spaces instead of a tab
        }
	}
	
	@Override
	public void keyReleased(KeyEvent e) 
	{
		if (e.getKeyChar() == KeyEvent.VK_ENTER) 
		{
			updateHighlight();
        }
	}
	
	// ==================================================================================
	
	private void highlightToken(int offset, String token) 
	{
		// get style document
		StyledDocument doc = (StyledDocument)getDocument();
		
		// get current text style
		Element element = doc.getCharacterElement(offset);
	    AttributeSet as = element.getAttributes();
	    MutableAttributeSet asNew = new SimpleAttributeSet(as.copyAttributes());
	    
	    // style the text according to keyword styles
	    applyHighlight(asNew, token);
	    
	    // commit changes for the given token
	    doc.setCharacterAttributes(offset, token.length(), asNew, true);
	}
	
	private void applyHighlight(MutableAttributeSet asNew, String token)
	{
		for (HighlightValidator hilite : highlights)
		{
			if (hilite.isValid(token))
			{
				hilite.setHighlight(asNew);
				return;
			}
		}
		unrecognizedHilite.setHighlight(asNew);
	}
}
