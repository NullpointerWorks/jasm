package com.nullpointerworks.test.controlpanel;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.nullpointerworks.test.controlpanel.awt.AbsoluteLayout;
import com.nullpointerworks.test.controlpanel.swing.HighlightedJTextPane;
import com.nullpointerworks.test.controlpanel.swing.StaticTableModel;

public class ControlPanelView implements KeyListener
{
	final Font fCourier = new Font("Courier New", Font.PLAIN, 16);
	
	HighlightedJTextPane jtaCode;
	
	String[] saBytecodeColumn;
	List<Object[]> tableDataSet;
	
	JFrame jfWindow;
	JPanel jpInterface;
	JTable jtBytecode;
	
	JTextField tjfRegisterA;
	JTextField tjfRegisterB;
	JTextField tjfRegisterC;
	JTextField tjfRegisterD;
	
	public ControlPanelView()
	{
		
		/*
		jtaCode = new HighlightedJTextPane();
		jtaCode.setSize(1000, 600);
		jtaCode.appendLine("load a, 12");
		jtaCode.appendLine("load b, 31");
		jtaCode.appendLine("add a, b");
		jtaCode.appendLine("int OUT_A");
		jtaCode.updateHighlight();
		jtaCode.addKeyListener(this);
		
		JPanel jpCodeScreen = new JPanel();
		jpCodeScreen.setLayout( new AbsoluteLayout() );
		jpCodeScreen.setSize(1000, 600);
		jpCodeScreen.add(jtaCode);
		//*/
		
		
		
		
		saBytecodeColumn = new String[] {"","Address","Bytecode"};
		tableDataSet = new ArrayList<Object[]>();
		
		StaticTableModel stmBytecode = new StaticTableModel();
		stmBytecode.addColumn("");
		stmBytecode.addColumn("Address");
		stmBytecode.addColumn("Bytecode");
		
		jtBytecode = new JTable(stmBytecode);
		jtBytecode.setLocation(5, 5);
		jtBytecode.setSize(250, 590);
		jtBytecode.setRowHeight(22);
		jtBytecode.setFont(fCourier);

		addTableEntry("00000000h", "00 00 00 00");
		addTableEntry("00000000h", "00 00 00 00");
		addTableEntry("00000000h", "00 00 00 00");
		addTableEntry("00000000h", "00 00 00 00");
		addTableEntry("00000000h", "00 00 00 00");
		

		JLabel jlRegA = new JLabel("A");
		jlRegA.setLocation(15, 25);
		jlRegA.setSize(10, 24);
		
		JLabel jlRegB = new JLabel("B");
		jlRegB.setLocation(15, 55);
		jlRegB.setSize(10, 24);
		
		JLabel jlRegC = new JLabel("C");
		jlRegC.setLocation(15, 85);
		jlRegC.setSize(10, 24);
		
		JLabel jlRegD = new JLabel("D");
		jlRegD.setLocation(15, 115);
		jlRegD.setSize(10, 24);
		
		tjfRegisterA = new JTextField("00 00 00 00");
		tjfRegisterA.setLocation(40, 25);
		tjfRegisterA.setSize(115, 24);
		tjfRegisterA.setEditable(false);
		tjfRegisterA.setFont(fCourier);
		
		tjfRegisterB = new JTextField("00 00 00 00");
		tjfRegisterB.setLocation(40, 55);
		tjfRegisterB.setSize(115, 24);
		tjfRegisterB.setEditable(false);
		tjfRegisterB.setFont(fCourier);
		
		tjfRegisterC = new JTextField("00 00 00 00");
		tjfRegisterC.setLocation(40, 85);
		tjfRegisterC.setSize(115, 24);
		tjfRegisterC.setEditable(false);
		tjfRegisterC.setFont(fCourier);
		
		tjfRegisterD = new JTextField("00 00 00 00");
		tjfRegisterD.setLocation(40, 115);
		tjfRegisterD.setSize(115, 24);
		tjfRegisterD.setEditable(false);
		tjfRegisterD.setFont(fCourier);
		

		JPanel jpRegisterPanel = new JPanel();
		jpRegisterPanel.setLayout( new AbsoluteLayout() );
		jpRegisterPanel.setLocation(270, 5);
		jpRegisterPanel.setSize(170, 400);
		jpRegisterPanel.setBorder(BorderFactory.createTitledBorder("Registers"));
		jpRegisterPanel.add(jlRegA);
		jpRegisterPanel.add(jlRegB);
		jpRegisterPanel.add(jlRegC);
		jpRegisterPanel.add(jlRegD);
		jpRegisterPanel.add(tjfRegisterA);
		jpRegisterPanel.add(tjfRegisterB);
		jpRegisterPanel.add(tjfRegisterC);
		jpRegisterPanel.add(tjfRegisterD);
		
		
		
		
		jpInterface = new JPanel();
		jpInterface.setLayout( new AbsoluteLayout() );
		jpInterface.setSize(1000, 600);
		jpInterface.add(jtBytecode);
		jpInterface.add(jpRegisterPanel);
		
		jfWindow = new JFrame();
		jfWindow.setTitle("Monitor VM");
		jfWindow.setLayout( new AbsoluteLayout() );
		jfWindow.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		jfWindow.setResizable(false);
		jfWindow.add(jpInterface);
		//jfWindow.add(jpCodeScreen);
		jfWindow.pack();
		jfWindow.validate();
		jfWindow.setLocationRelativeTo(null);
	}
	
	public void setVisible(boolean b)
	{
		jfWindow.setVisible(b);
	}
	
	
	
	
	public void clearTable() 
	{
		tableDataSet.clear();
		Object[][] data = new Object[][] {};
		setDataVector(jtBytecode, data, saBytecodeColumn);
	}
	
	public synchronized void addTableEntry(String addr, String bytecode)
	{
		tableDataSet.add( new Object[] {"", addr, bytecode} );
		Object[][] data = tableDataSet.toArray(new Object[][] {});
		setDataVector(jtBytecode, data, saBytecodeColumn);
	}
	
	private void setDataVector(JTable jtable, Object[][] data, String[] columns)
	{
		var model = (DefaultTableModel) jtable.getModel();
        model.setDataVector(data, columns);
        
        var col = jtable.getColumnModel().getColumn(0);
        col.setMinWidth(32);
        col.setMaxWidth(32);

        col = jtable.getColumnModel().getColumn(1);
        col.setMinWidth(105);
        col.setMaxWidth(105);
        
        jtable.revalidate();
	}
	
	
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) 
	{
		jtaCode.updateHighlight();
	}
}
