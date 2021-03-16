package com.nullpointerworks.test.controlpanel;

import com.nullpointerworks.test.controlpanel.gui.AssemblerView;
import com.nullpointerworks.test.controlpanel.gui.swing.UILookAndFeel;

public class MainControlPanel 
{
	public static void main(String[] args) 
	{
		UILookAndFeel.setLookAndFeel( UILookAndFeel.WINDOWS );
		
		
		
		AssemblerView asmView = new AssemblerView();
		asmView.setVisible(true);
		
		
		//ControlPanelView view = new ControlPanelView();
		//ControlPanelController ctrl = new ControlPanelController();
		//view.setVisible(true);
	}
}
