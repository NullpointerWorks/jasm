package com.nullpointerworks.test.controlpanel.gui.swing.highlight;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.InputEvent;
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

public class HighlightingJTextPane extends JTextPane
{
	private static final long serialVersionUID = 6407970407357102776L;
	
	private String TAB_SIZE;
	private List<HighlightValidator> highlights;
	private AttributeSet aset;
	private KeyListener keyListener;
	
	public HighlightingJTextPane()
	{
        highlights = new ArrayList<HighlightValidator>();
		setFontFamily("Lucida Console", 12);
		setTabSize(2);
		
		keyListener = new KeyListener() // I prefer to keep anonymous classes as brief as possible
		{
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {parseKeyPressed(e);}
			@Override
			public void keyReleased(KeyEvent e) {parseKeyReleased(e);}
		};
		addKeyListener(keyListener);
		
		
	}
	
	/**
	 * 
	 */
	public void setTabSize(int spaces)
	{
		if (spaces < 1) return;
		TAB_SIZE = "";
		for (; spaces>0; spaces--) TAB_SIZE+=" ";
	}
	
	@Override
	public void setFont(Font font)
	{
		setFontFamily(font);
	}
	
	/**
	 * 
	 */
	public void addHighlightValidator(HighlightValidator hlv)
	{
		highlights.add(hlv);
	}
	
	/**
	 * 
	 */
	public void clearHighlightValidators()
	{
		highlights.clear();
	}
	
	/**
	 * Add a string of text to the textpane.
	 */
	public void append(String str)
	{
		str = str.replace("\t", TAB_SIZE);
        setCaretPosition(getDocument().getLength());
        setCharacterAttributes(aset, false);
        replaceSelection(str);
        setCaretPosition(getDocument().getLength());
        updateHighlight();
	}
	
	/**
	 * Add a string of text to the textpane at the location specified.
	 * @param str
	 */
	public void insert(int index, String str)
	{
		str = str.replace("\t", TAB_SIZE);
        setCaretPosition(index);
        setCharacterAttributes(aset, false);
        replaceSelection(str);
        setCaretPosition(index + str.length());
        updateHighlight();
	}
	
	/**
	 * Add a line of text to the textpane.
	 */
	public void appendLine(String str)
	{
		append(str+"\n");
	}
	
	/**
	 * scan for text tokens and highlight them accordingly
	 */
	public void updateHighlight()
	{
		String text = getText();
		text = text.replace("\r\n", " ");
		text = text.replace("\n", " ");
		
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
	
	// ==================================================================================
	
	private void parseKeyPressed(KeyEvent e)
	{
		if (isKeystrokeUndo(e))
		{
			// TODO implement momento to undo edit
			return;
		}
		
		// replace tabs with spaces
		if (e.getKeyChar() == KeyEvent.VK_TAB) 
		{
			e.consume();
	        setCharacterAttributes(aset, false);
	        replaceSelection(TAB_SIZE);
			return;
        }
		
		// add a new line. continue from the same indentation as the previous line
		if (e.getKeyChar() == KeyEvent.VK_ENTER) 
		{
			e.consume();
			int caret = getCaretPosition();
			String text = getText().substring(0, caret+1);
			
			int index = text.lastIndexOf("\n");
			String segment = text.substring(index+1);
			
			int count = 0;
			for (int i=0, l=segment.length(); i<l; i++)
			{
				String chr = segment.substring(i,i+1);
				if (chr.equals("\s")) count++;
				else break;
			}
			String whitespace = "\n";
			for (; count>0; count--) whitespace+=" ";
			
			insert( caret, whitespace);
			return;
		}
	}
	
	private void parseKeyReleased(KeyEvent e) 
	{
		if (e.getKeyChar() == KeyEvent.VK_ENTER) 
		{
			updateHighlight();
        }
	}
	
	private void highlightToken(int offset, String token) 
	{
		// get document and current text style
		StyledDocument doc 			= (StyledDocument)getDocument();
		Element element 			= doc.getCharacterElement(offset);
	    AttributeSet as 			= element.getAttributes();
	    MutableAttributeSet asNew 	= new SimpleAttributeSet(as.copyAttributes());
	    StyleConstants.setItalic(asNew, false); // reset anything italic
	    
	    // style the text according to keyword styles, then commit changes
	    applyHighlight(asNew, token);
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
	}
	
	private void setFontFamily(String fam, int size) 
	{
		setFontFamily( new Font(fam, Font.PLAIN, size) );
	}
	
	private void setFontFamily(Font font) 
	{
		String fontFamily = font.getFamily();
		int fontSize = font.getSize();
		StyleContext sc = StyleContext.getDefaultStyleContext();
        aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.BLACK);
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, fontFamily);
        aset = sc.addAttribute(aset, StyleConstants.FontSize, fontSize);
        super.setFont( font );
	}
	
	private boolean isKeystrokeUndo(KeyEvent e) 
	{
		int code = e.getKeyCode();
		int mod = e.getModifiersEx();
		if (mod == InputEvent.CTRL_DOWN_MASK)
		{
			return code == KeyEvent.VK_Z;
		}
		return false;
	}
}
