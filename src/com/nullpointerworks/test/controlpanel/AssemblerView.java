package com.nullpointerworks.test.controlpanel;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import com.nullpointerworks.test.controlpanel.awt.AbsoluteLayout;
import com.nullpointerworks.test.controlpanel.swing.CodeJScrollPane;
import com.nullpointerworks.test.controlpanel.swing.JTextAreaScrollPane;
import com.nullpointerworks.test.controlpanel.swing.highlight.*;

public class AssemblerView 
{
	private JFrame jfWindow;
	private JPanel jpInterface;
	private JTabbedPane jtpSourceTabs;
	private JTabbedPane jtpBottomTabs;
	
	private HighlightingJTextPane jtaCode;
	
	private JButton jbNewFile;
	private JButton jbOpenFile;
	
	private JTextArea jtaConsoleOut;
	
	
	
	
	public AssemblerView()
	{
		
		jbNewFile = new JButton();
		
		jbOpenFile = new JButton();
		
		/*
		 * construct higher tabbing pane
		 */
		CodeJScrollPane cjspCode = new CodeJScrollPane();
		cjspCode.setSize(500, 300);
		
		/*
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
		//*/
		
		
		jtpSourceTabs = new JTabbedPane();  
		jtpSourceTabs.setBounds(0,50, 800,400);
		jtpSourceTabs.add("main.jasm", cjspCode);
		
		
		
		/*
		 * construct lower tabbing pane
		 */
		JTextAreaScrollPane jtaScrolling = new JTextAreaScrollPane();
		jtaScrolling.setLocation(0, 400);
		jtaScrolling.setSize(800, 200); 
		jtaConsoleOut = jtaScrolling.getJTextArea();
		
	    jtpBottomTabs = new JTabbedPane();  
	    jtpBottomTabs.setBounds(0,400, 800,200);
	    jtpBottomTabs.add("Console", jtaScrolling);
		
	    /*
	     * construct window
	     */
		jpInterface = new JPanel();
		jpInterface.setLayout( new AbsoluteLayout() );
		jpInterface.setLocation(0, 0);
		jpInterface.setSize(800, 600);
		jpInterface.add(jtpSourceTabs);
		//jpInterface.add(jtaCode);
		//jpInterface.add(jtpBottomTabs);
		
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
