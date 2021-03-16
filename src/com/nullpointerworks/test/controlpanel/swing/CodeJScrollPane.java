package com.nullpointerworks.test.controlpanel.swing;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;

import com.nullpointerworks.test.controlpanel.swing.highlight.DeclarationHighlighter;
import com.nullpointerworks.test.controlpanel.swing.highlight.DefaultHighlighter;
import com.nullpointerworks.test.controlpanel.swing.highlight.HighlightingJTextPane;
import com.nullpointerworks.test.controlpanel.swing.highlight.InstructionHighlighter;
import com.nullpointerworks.test.controlpanel.swing.highlight.NumberHighlighter;
import com.nullpointerworks.test.controlpanel.swing.highlight.RegisterHighlighter;

public class CodeJScrollPane extends JScrollPane
{
	private static final long serialVersionUID = 1L;
	
	private JTextArea lines;
	private HighlightingJTextPane jtp;
	
	public CodeJScrollPane()
	{
		Font font = new Font("Lucida Console", Font.PLAIN, 12);
		
		lines = new JTextArea("1 ");
		lines.setBackground(Color.LIGHT_GRAY);
		lines.setEditable(false);
		lines.setFont(font);
		lines.setCaret( new NoSelectCaret(lines) );
		
		jtp = new HighlightingJTextPane();
		jtp.setBorder(null); // removes the margin from the pane, else it wont line of with the numbering
		jtp.setFont(font);
		jtp.addHighlightValidator( new DeclarationHighlighter() );
		jtp.addHighlightValidator( new InstructionHighlighter() );
		jtp.addHighlightValidator( new RegisterHighlighter() );
		jtp.addHighlightValidator( new NumberHighlighter() );
		jtp.addHighlightValidator( new DefaultHighlighter() );
		jtp.getDocument().addDocumentListener(new DocumentListener()
		{
			public String getText()
			{
				int caretPosition = jtp.getDocument().getLength();
				Element root = jtp.getDocument().getDefaultRootElement();
				String text = "1 " + System.getProperty("line.separator");
				for(int i = 2; i < root.getElementIndex( caretPosition ) + 2; i++)
				{
					text += i + " " + System.getProperty("line.separator");
				}
				return text;
			}
			
			@Override
			public void changedUpdate(DocumentEvent de) 
			{
				lines.setText(getText());
			}
 
			@Override
			public void insertUpdate(DocumentEvent de) 
			{
				lines.setText(getText());
			}
 
			@Override
			public void removeUpdate(DocumentEvent de) 
			{
				lines.setText(getText());
			}
		});
		
		setViewportView(jtp);
		setRowHeaderView(lines);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	}
	
	
}
