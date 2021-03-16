package com.nullpointerworks.test.controlpanel.gui.swing;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class JTextAreaScrollPane extends JScrollPane
{
	private static final long serialVersionUID = -6897121886373402620L;
	
	private JTextArea jta;
	
	public JTextAreaScrollPane()
	{
		jta = new JTextArea();
		jta.setLineWrap(true);
		jta.setEditable(false);
		jta.setVisible(true);
		setViewportView(jta);
	    setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	
	public JTextArea getJTextArea()
	{
		return jta;
	}
}
