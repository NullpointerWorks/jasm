package com.nullpointerworks.test.controlpanel.gui;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.nullpointerworks.test.controlpanel.Resources;
import com.nullpointerworks.test.controlpanel.gui.awt.AbsoluteLayout;
import com.nullpointerworks.test.controlpanel.gui.swing.CodeJScrollPane;
import com.nullpointerworks.test.controlpanel.gui.swing.JTextAreaScrollPane;

public class AssemblerView 
{
	private JFrame jfWindow;
	private JPanel jpInterface;
	private JPanel jpToolRibbon;
	private JTabbedPane jtpSourceTabs;
	private JTabbedPane jtpBottomTabs;
	
	private CodeJScrollPane cjspCode;
	
	private JButton jbNewFile;
	private JButton jbOpenFile;
	private JButton jbSaveFile;
	private JButton jbSaveAll;
	private JButton jbAssemble;
	private JButton jbRunVM;
	
	private JTextArea jtaConsoleOut;
	
	
	
	
	public AssemblerView()
	{
		/*
		 * tool ribbon
		 */
		jbNewFile = new JButton(Resources.getNewIcon() );
		jbNewFile.setVerticalTextPosition(AbstractButton.BOTTOM);
		jbNewFile.setHorizontalTextPosition(AbstractButton.CENTER);
		jbNewFile.setToolTipText("Create a new source file.");
		jbNewFile.setLocation(0, 0);
		jbNewFile.setSize(32, 32);
		
		jbOpenFile = new JButton(Resources.getOpenIcon() );
		jbOpenFile.setVerticalTextPosition(AbstractButton.BOTTOM);
		jbOpenFile.setHorizontalTextPosition(AbstractButton.CENTER);
		jbOpenFile.setToolTipText("Open an existing source file.");
		jbOpenFile.setLocation(32, 0);
		jbOpenFile.setSize(32, 32);
		
		jbSaveFile = new JButton(Resources.getSaveIcon() );
		jbSaveFile.setVerticalTextPosition(AbstractButton.BOTTOM);
		jbSaveFile.setHorizontalTextPosition(AbstractButton.CENTER);
		jbSaveFile.setToolTipText("Save the currently editing file.");
		jbSaveFile.setLocation(64, 0);
		jbSaveFile.setSize(32, 32);
		
		jbSaveAll = new JButton(Resources.getSaveAllIcon() );
		jbSaveAll.setVerticalTextPosition(AbstractButton.BOTTOM);
		jbSaveAll.setHorizontalTextPosition(AbstractButton.CENTER);
		jbSaveAll.setToolTipText("Save the currently editing file.");
		jbSaveAll.setLocation(96, 0);
		jbSaveAll.setSize(32, 32);
		
		jbAssemble = new JButton(Resources.getAssembleIcon() );
		jbAssemble.setVerticalTextPosition(AbstractButton.BOTTOM);
		jbAssemble.setHorizontalTextPosition(AbstractButton.CENTER);
		jbAssemble.setToolTipText("Save the currently editing file.");
		jbAssemble.setLocation(128, 0);
		jbAssemble.setSize(32, 32);
		
		jbRunVM = new JButton(Resources.getRunIcon() );
		jbRunVM.setVerticalTextPosition(AbstractButton.BOTTOM);
		jbRunVM.setHorizontalTextPosition(AbstractButton.CENTER);
		jbRunVM.setToolTipText("Save the currently editing file.");
		jbRunVM.setLocation(160, 0);
		jbRunVM.setSize(32, 32);
		
		jpToolRibbon = new JPanel();
		jpToolRibbon.setLayout( new AbsoluteLayout() );
		jpToolRibbon.setLocation(0, 0);
		jpToolRibbon.setSize(800, 64);
		jpToolRibbon.add(jbNewFile);
		jpToolRibbon.add(jbOpenFile);
		jpToolRibbon.add(jbSaveFile);
		jpToolRibbon.add(jbSaveAll);
		jpToolRibbon.add(jbAssemble);
		jpToolRibbon.add(jbRunVM);
		
		
		
		
		/*
		 * construct higher tab pane
		 */
		cjspCode = new CodeJScrollPane();
		cjspCode.setSize(500, 300);
		cjspCode.appendLine(".def EXIT 0");
		cjspCode.appendLine(".def OUT_A 1");
		cjspCode.appendLine("");
		cjspCode.appendLine("main:");
		cjspCode.appendLine("  load a,10");
		cjspCode.appendLine("loop:");
		cjspCode.appendLine("  dec a");
		cjspCode.appendLine("  int OUT_A");
		cjspCode.appendLine("  jne loop");
		cjspCode.append("  int EXIT");
		
		jtpSourceTabs = new JTabbedPane();  
		jtpSourceTabs.setSize(800,350);
		jtpSourceTabs.setPreferredSize(jtpSourceTabs.getSize());
		jtpSourceTabs.add("main.jasm", cjspCode);
		
		
		
		
		/*
		 * construct lower tab pane
		 */
		JTextAreaScrollPane jtaScrolling = new JTextAreaScrollPane();
		jtaScrolling.setLocation(0, 0);
		jtaScrolling.setSize(800, 200);
		jtaConsoleOut = jtaScrolling.getJTextArea();
		
	    jtpBottomTabs = new JTabbedPane();
	    jtpBottomTabs.add("Console", jtaScrolling);
		
	    /*
	     * construct window
	     */
	    JSplitPane jspSplitScreen = new JSplitPane(SwingConstants.HORIZONTAL);
	    jspSplitScreen.setLocation(0, 64);
		jspSplitScreen.setSize(800, 536);
		jspSplitScreen.add(jtpSourceTabs);
		jspSplitScreen.add(jtpBottomTabs);
		
		jpInterface = new JPanel();
		jpInterface.setLayout( new AbsoluteLayout() );
		jpInterface.setLocation(0, 0);
		jpInterface.setSize(800, 600);
		jpInterface.add(jpToolRibbon);
		jpInterface.add(jspSplitScreen);
		
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
