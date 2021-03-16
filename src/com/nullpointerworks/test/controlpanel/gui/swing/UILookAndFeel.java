package com.nullpointerworks.test.controlpanel.gui.swing;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class UILookAndFeel 
{
	public static final String BASIC 	= "javax.swing.plaf.basic.BasicLookAndFeel";
	
	public static final String MULTI 	= "javax.swing.plaf.multi.MultiLookAndFeel";
	
	public static final String SYNTH 	= "javax.swing.plaf.synth.SynthLookAndFeel";
	
	public static final String METAL 	= "javax.swing.plaf.metal.MetalLookAndFeel";
	
	public static final String NIMBUS 	= "javax.swing.plaf.nimbus.NimbusLookAndFeel";
	
	public static final String MOTIF 	= "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
	
	public static final String WINDOWS 	= "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	
	/**
	 * Set the pluggable look and feel (plaf)
	 */
	public static void setLookAndFeel(final String lookandfeel)
	{
		try 
		{
			UIManager.setLookAndFeel(lookandfeel);
		} 
		catch (ClassNotFoundException | 
				InstantiationException | 
				IllegalAccessException | 
				UnsupportedLookAndFeelException e) 
		{
			e.printStackTrace();
		}
	}
}
