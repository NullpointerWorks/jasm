package com.nullpointerworks.test.monitor;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MonitorView 
{
	JFrame jfWindow;
	JPanel jpInterface;
	
	
	public MonitorView()
	{
		
		
		
		
		
		
		
		
		jpInterface = new JPanel();
		jpInterface.setSize(800, 600);
		
		
		
		
		
		
		
		jfWindow = new JFrame();
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
