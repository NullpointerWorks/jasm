package com.nullpointerworks.test.controlpanel.swing;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ScrollingJTextArea extends JScrollPane
{
	private JTextArea jta;
	
	public ScrollingJTextArea()
	{
		jta = new JTextArea();
		jta.setLineWrap(true);
		jta.setEditable(false);
		jta.setVisible(true);
		setViewportView(jta);
	}
	
	public JTextArea getJTextArea()
	{
		return jta;
	}
	
	
}
