package com.nullpointerworks.test.controlpanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class ControlPanelView 
{
	
	JFrame jfWindow;
	JPanel jpInterface;
	JTable jtBytecode;
	
	String[] saBytecodeColumn = {"","Address","Bytecode"};
	
	
	JTextField tjfRegisterA;
	JTextField tjfRegisterB;
	JTextField tjfRegisterC;
	JTextField tjfRegisterD;
	
	public ControlPanelView()
	{
		
		
		HighlightedJTextPane jtaCode = new HighlightedJTextPane();
		jtaCode.setSize(1000, 600);
		jtaCode.append("load a, 12\n");
		jtaCode.append("load b, 31\n");
		jtaCode.append("add a, b\n");
		jtaCode.append("int OUT_A\n");
		jtaCode.updateHighlight();

		
		
		
		JPanel jpCodeScreen = new JPanel();
		jpCodeScreen.setLayout( new AbsoluteLayout() );
		jpCodeScreen.setSize(1000, 600);
		jpCodeScreen.add(jtaCode);
		
		
		
		
		
		
		StaticTableModel stmBytecode = new StaticTableModel();
		stmBytecode.addColumn("");
		stmBytecode.addColumn("Address");
		stmBytecode.addColumn("Bytecode");
		
		jtBytecode = new JTable(stmBytecode);
		jtBytecode.setSize(300, 590);
		jtBytecode.setLocation(5, 5);
		
		
		

		tjfRegisterA = new JTextField();
		tjfRegisterA.setLocation(310, 5);
		tjfRegisterA.setSize(150, 24);
		tjfRegisterA.setEditable(false);
		
		
		
		
		
		jpInterface = new JPanel();
		jpInterface.setLayout( new AbsoluteLayout() );
		jpInterface.setSize(1000, 600);
		jpInterface.add(jtBytecode);
		jpInterface.add(tjfRegisterA);
		
		jfWindow = new JFrame();
		jfWindow.setTitle("Monitor VM");
		jfWindow.setLayout( new AbsoluteLayout() );
		jfWindow.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		jfWindow.setResizable(false);
		//jfWindow.add(jpInterface);
		jfWindow.add(jpCodeScreen);
		jfWindow.pack();
		jfWindow.validate();
		jfWindow.setLocationRelativeTo(null);
	}
	
	public void setVisible(boolean b)
	{
		jfWindow.setVisible(b);
	}
	
	
	
}
