package com.nullpointerworks.test.controlpanel.gui;

import java.awt.event.KeyEvent;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import com.nullpointerworks.test.controlpanel.Resources;
import com.nullpointerworks.test.controlpanel.gui.awt.AbsoluteLayout;
import com.nullpointerworks.test.controlpanel.gui.swing.CodeJScrollPane;
import com.nullpointerworks.test.controlpanel.gui.swing.JTextAreaScrollPane;

public class AssemblerView 
{
	private JFrame jfWindow;
	private JPanel jpInterface;
	private JTabbedPane jtpSourceTabs;
	private JTabbedPane jtpBottomTabs;
	
	private CodeJScrollPane cjspCode;
	
	private JButton jbNewFile;
	private JButton jbOpenFile;
	
	private JTextArea jtaConsoleOut;
	
	
	
	
	public AssemblerView()
	{
		
		jbNewFile = new JButton("New", Resources.getNewIcon() );
		jbNewFile.setVerticalTextPosition(AbstractButton.BOTTOM);
		jbNewFile.setHorizontalTextPosition(AbstractButton.CENTER);
		jbNewFile.setMnemonic(KeyEvent.VK_N);
		jbNewFile.setToolTipText("Create a new file.");
		jbNewFile.setSize(48, 48);
		
		jbOpenFile = new JButton("Open");
		
		/*
		 * construct higher tabbing pane
		 */
		cjspCode = new CodeJScrollPane();
		cjspCode.setSize(500, 300);
		cjspCode.appendLine("main:");
		cjspCode.appendLine("  load a, 12");
		cjspCode.appendLine("  load b, 31");
		cjspCode.appendLine("  add a, b");
		cjspCode.append("  int OUT_A");
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
		jpInterface.add(jtpBottomTabs);
		jpInterface.add(jbNewFile);
		
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
