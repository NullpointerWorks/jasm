package com.nullpointerworks.test.controlpanel;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import com.nullpointerworks.test.controlpanel.awt.AbsoluteLayout;
import com.nullpointerworks.test.controlpanel.swing.highlight.*;

public class AssemblerView 
{
	private JFrame jfWindow;
	private JPanel jpInterface;
	
	private HighlightingJTextPane jtaCode;
	
	private JButton jbLoadMain;
	private JTextArea jtaConsoleOut;
	
	
	
	
	public AssemblerView()
	{
		
		
		jtaCode = new HighlightingJTextPane();
		jtaCode.setSize(500, 300);
		jtaCode.addHighlightValidator( new DeclarationHighlighter() );
		jtaCode.addHighlightValidator( new InstructionHighlighter() );
		jtaCode.addHighlightValidator( new RegisterHighlighter() );
		jtaCode.addHighlightValidator( new NumberHighlighter() );
		jtaCode.addHighlightValidator( new DefaultHighlighter() );
		jtaCode.appendLine("main:");
		jtaCode.appendLine("  load a, 12");
		jtaCode.appendLine("  load b, 31");
		jtaCode.appendLine("  add a, b");
		jtaCode.append("  int OUT_A");
		
		
		
		
		jtaConsoleOut = new JTextArea();
		jtaConsoleOut.setLineWrap(true);
		jtaConsoleOut.setEditable(false);
		jtaConsoleOut.setVisible(true);
	    JScrollPane jcpConsoleOut = new JScrollPane(jtaConsoleOut);
	    jcpConsoleOut.setLocation(0, 400);
	    jcpConsoleOut.setSize(800, 200); 
	    jcpConsoleOut.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    jcpConsoleOut.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
	    
	    JTabbedPane jtpBottomTabs = new JTabbedPane();  
	    jtpBottomTabs.setBounds(0,400, 800,200);
	    jtpBottomTabs.add("Console", jcpConsoleOut);
		
		jpInterface = new JPanel();
		jpInterface.setLayout( new AbsoluteLayout() );
		jpInterface.setLocation(0, 0);
		jpInterface.setSize(800, 600);
		//jpInterface.add(jtaCode);
		jpInterface.add(jtpBottomTabs);
		
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
