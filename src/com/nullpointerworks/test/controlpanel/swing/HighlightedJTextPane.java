package com.nullpointerworks.test.controlpanel.swing;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.nullpointerworks.jasm.asm.ParserUtility;

public class HighlightedJTextPane extends JTextPane
{
	private static final long serialVersionUID = 6407970407357102776L;

	private final Color INSTRUCTION = new Color(0, 0, 255);
	private final Color REGISTER = new Color(0, 170, 0);
	
	private AttributeSet aset;
	
	public HighlightedJTextPane()
	{
		StyleContext sc = StyleContext.getDefaultStyleContext();
        aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, Color.black);
        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
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
	
	/*
	 * 
	 */
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
		// instructions are marked blue
		if (isInstruction(token))
		{
			StyleConstants.setForeground(asNew, INSTRUCTION);
			return;
		}
		
		// registers are dark green
		if (isRegister(token))
		{
			StyleConstants.setForeground(asNew, REGISTER);
			return;
		}
		
		// numbers are red
		if (ParserUtility.isValidNumber(token))
		{
			StyleConstants.setForeground(asNew, Color.RED);
			return;
		}
		
		StyleConstants.setBackground(asNew, Color.WHITE);
		StyleConstants.setForeground(asNew, Color.BLACK);
	}
	
	private boolean isInstruction(String token) 
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
	
	private boolean isRegister(String token) 
	{
		if (token.equals("a")) return true;
		if (token.equals("b")) return true;
		if (token.equals("c")) return true;
		if (token.equals("d")) return true;

		if (token.equals("ip")) return true;
		if (token.equals("sp")) return true;
		if (token.equals("dp")) return true;

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
}
