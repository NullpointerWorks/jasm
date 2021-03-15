package com.nullpointerworks.test.controlpanel;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.nullpointerworks.test.controlpanel.awt.AbsoluteLayout;
import com.nullpointerworks.test.controlpanel.swing.highlight.*;

public class AssemblerView 
{
	private JFrame jfWindow;
	private JPanel jpInterface;
	
	private HighlightingJTextPane jtaCode;
	
	private JButton jbLoadMain;
	
	
	public AssemblerView()
	{
		
		jtaCode = new HighlightingJTextPane();
		jtaCode.setSize(500, 300);
		jtaCode.addHighlightValidator( new InstructionHighlighter() );
		jtaCode.addHighlightValidator( new RegisterHighlighter() );
		jtaCode.addHighlightValidator( new NumberHighlighter() );
		jtaCode.addHighlightValidator( new DefaultHighlighter() );
		jtaCode.appendLine("load a, 12");
		jtaCode.appendLine("load b, 31");
		jtaCode.appendLine("add a, b");
		jtaCode.appendLine("int OUT_A");
		jtaCode.updateHighlight();
		
		
		
		
		jpInterface = new JPanel();
		jpInterface.setLayout( new AbsoluteLayout() );
		jpInterface.setLocation(0, 0);
		jpInterface.setSize(800, 600);
		jpInterface.add(jtaCode);
		
		jfWindow = new JFrame();
		jfWindow.setTitle("JASM Assembler");
		jfWindow.setLayout( new AbsoluteLayout() );
		jfWindow.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		jfWindow.setResizable(false);
		jfWindow.add(jpInterface);
		jfWindow.pack();
		jfWindow.validate();
		jfWindow.setLocationRelativeTo(null);
	}
	
	public void setVisible(boolean b)
	{
		jfWindow.setVisible(b);
	}
	
	
	
}
