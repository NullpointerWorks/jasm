package com.nullpointerworks.test.controlpanel.gui.swing;

import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;

public class NoSelectCaret extends DefaultCaret
{
	private static final long serialVersionUID = 7500988197036590784L;

	public NoSelectCaret(JTextComponent textComponent)
    {
        setBlinkRate( textComponent.getCaret().getBlinkRate() );
        textComponent.setHighlighter( null );
    }

    @Override
    public int getMark()
    {
        return getDot();
    }
}
