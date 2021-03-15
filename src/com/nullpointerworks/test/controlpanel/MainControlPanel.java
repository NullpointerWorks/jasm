package com.nullpointerworks.test.controlpanel;

import com.nullpointerworks.test.controlpanel.swing.UILookAndFeel;

public class MainControlPanel 
{

	public static void main(String[] args) 
	{
		UILookAndFeel.setLookAndFeel( UILookAndFeel.WINDOWS );
		
		
		
		
		ControlPanelView view = new ControlPanelView();
		
		
		ControlPanelController ctrl = new ControlPanelController();
		
		
		
		
		view.setVisible(true);
	}

}
