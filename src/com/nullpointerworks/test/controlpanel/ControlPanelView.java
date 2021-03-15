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
	
	private HighlightedJTextPane jtaCode;
	
	private String[] saBytecodeColumn;
	private List<Object[]> tableDataSet;
	
	private JFrame jfWindow;
	private JPanel jpInterface;
	private JTable jtBytecode;
	
	private JTextField tjfRegisterIP;
	private JTextField tjfRegisterSP;
	private JTextField tjfRegisterDP;
	private JTextField tjfRegisterA;
	private JTextField tjfRegisterB;
	private JTextField tjfRegisterC;
	private JTextField tjfRegisterD;
	private JTextField tjfRegister0;
	private JTextField tjfRegister1;
	private JTextField tjfRegister2;
	private JTextField tjfRegister3;
	private JTextField tjfRegister4;
	private JTextField tjfRegister5;
	private JTextField tjfRegister6;
	private JTextField tjfRegister7;
	private JTextField tjfRegister8;
	private JTextField tjfRegister9;
	
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
		
		JLabel jlRegIP = new JLabel("IP");
		JLabel jlRegSP = new JLabel("SP");
		JLabel jlRegDP = new JLabel("DP");
		JLabel jlRegA = new JLabel("A");
		JLabel jlRegB = new JLabel("B");
		JLabel jlRegC = new JLabel("C");
		JLabel jlRegD = new JLabel("D");
		JLabel jlReg0 = new JLabel("R0");
		JLabel jlReg1 = new JLabel("R1");
		JLabel jlReg2 = new JLabel("R2");
		JLabel jlReg3 = new JLabel("R3");
		JLabel jlReg4 = new JLabel("R4");
		JLabel jlReg5 = new JLabel("R5");
		JLabel jlReg6 = new JLabel("R6");
		JLabel jlReg7 = new JLabel("R7");
		JLabel jlReg8 = new JLabel("R8");
		JLabel jlReg9 = new JLabel("R9");
		tjfRegisterIP = new JTextField("00 00 00 00");
		tjfRegisterSP = new JTextField("00 00 00 00");
		tjfRegisterDP = new JTextField("00 00 00 00");
		tjfRegisterA = new JTextField("00 00 00 00");
		tjfRegisterB = new JTextField("00 00 00 00");
		tjfRegisterC = new JTextField("00 00 00 00");
		tjfRegisterD = new JTextField("00 00 00 00");
		tjfRegister0 = new JTextField("00 00 00 00");
		tjfRegister1 = new JTextField("00 00 00 00");
		tjfRegister2 = new JTextField("00 00 00 00");
		tjfRegister3 = new JTextField("00 00 00 00");
		tjfRegister4 = new JTextField("00 00 00 00");
		tjfRegister5 = new JTextField("00 00 00 00");
		tjfRegister6 = new JTextField("00 00 00 00");
		tjfRegister7 = new JTextField("00 00 00 00");
		tjfRegister8 = new JTextField("00 00 00 00");
		tjfRegister9 = new JTextField("00 00 00 00");
		makeRegisterReadOut(jlRegIP, tjfRegisterIP, 25);
		makeRegisterReadOut(jlRegSP, tjfRegisterSP, 55);
		makeRegisterReadOut(jlRegDP, tjfRegisterDP, 85);
		makeRegisterReadOut(jlRegA, tjfRegisterA, 140);
		makeRegisterReadOut(jlRegB, tjfRegisterB, 170);
		makeRegisterReadOut(jlRegC, tjfRegisterC, 200);
		makeRegisterReadOut(jlRegD, tjfRegisterD, 230);
		makeRegisterReadOut(jlReg0, tjfRegister0, 285);
		makeRegisterReadOut(jlReg1, tjfRegister1, 315);
		makeRegisterReadOut(jlReg2, tjfRegister2, 345);
		makeRegisterReadOut(jlReg3, tjfRegister3, 375);
		makeRegisterReadOut(jlReg4, tjfRegister4, 405);
		makeRegisterReadOut(jlReg5, tjfRegister5, 435);
		makeRegisterReadOut(jlReg6, tjfRegister6, 465);
		makeRegisterReadOut(jlReg7, tjfRegister7, 495);
		makeRegisterReadOut(jlReg8, tjfRegister8, 525);
		makeRegisterReadOut(jlReg9, tjfRegister9, 555);
		
		JPanel jpRegisterPanel = new JPanel();
		jpRegisterPanel.setLayout( new AbsoluteLayout() );
		jpRegisterPanel.setLocation(265, 5);
		jpRegisterPanel.setSize(180, 590);
		jpRegisterPanel.setBorder(BorderFactory.createTitledBorder("Registers"));
		jpRegisterPanel.add(jlRegIP);
		jpRegisterPanel.add(jlRegSP);
		jpRegisterPanel.add(jlRegDP);
		jpRegisterPanel.add(jlRegA);
		jpRegisterPanel.add(jlRegB);
		jpRegisterPanel.add(jlRegC);
		jpRegisterPanel.add(jlRegD);
		jpRegisterPanel.add(jlReg0);
		jpRegisterPanel.add(jlReg1);
		jpRegisterPanel.add(jlReg2);
		jpRegisterPanel.add(jlReg3);
		jpRegisterPanel.add(jlReg4);
		jpRegisterPanel.add(jlReg5);
		jpRegisterPanel.add(jlReg6);
		jpRegisterPanel.add(jlReg7);
		jpRegisterPanel.add(jlReg8);
		jpRegisterPanel.add(jlReg9);
		jpRegisterPanel.add(tjfRegisterIP);
		jpRegisterPanel.add(tjfRegisterSP);
		jpRegisterPanel.add(tjfRegisterDP);
		jpRegisterPanel.add(tjfRegisterA);
		jpRegisterPanel.add(tjfRegisterB);
		jpRegisterPanel.add(tjfRegisterC);
		jpRegisterPanel.add(tjfRegisterD);
		jpRegisterPanel.add(tjfRegister0);
		jpRegisterPanel.add(tjfRegister1);
		jpRegisterPanel.add(tjfRegister2);
		jpRegisterPanel.add(tjfRegister3);
		jpRegisterPanel.add(tjfRegister4);
		jpRegisterPanel.add(tjfRegister5);
		jpRegisterPanel.add(tjfRegister6);
		jpRegisterPanel.add(tjfRegister7);
		jpRegisterPanel.add(tjfRegister8);
		jpRegisterPanel.add(tjfRegister9);
		
		
		JPanel jpStatusFlagPanel = new JPanel();
		jpStatusFlagPanel.setLayout( new AbsoluteLayout() );
		jpStatusFlagPanel.setLocation(450, 5);
		jpStatusFlagPanel.setSize(180, 150);
		jpStatusFlagPanel.setBorder(BorderFactory.createTitledBorder("Flags"));
		
		
		
		
		
		jpInterface = new JPanel();
		jpInterface.setLayout( new AbsoluteLayout() );
		jpInterface.setLocation(0, 0);
		jpInterface.setSize(1000, 600);
		jpInterface.add(jtBytecode);
		jpInterface.add(jpRegisterPanel);
		jpInterface.add(jpStatusFlagPanel);
		
		
		
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
	
	private void makeRegisterReadOut(JLabel jlRegA, JTextField tjfRegisterA, int y)
	{
		jlRegA.setLocation(15, y);
		jlRegA.setSize(20, 24);
		tjfRegisterA.setLocation(50, y);
		tjfRegisterA.setSize(115, 24);
		tjfRegisterA.setEditable(false);
		tjfRegisterA.setFont(fCourier);
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
