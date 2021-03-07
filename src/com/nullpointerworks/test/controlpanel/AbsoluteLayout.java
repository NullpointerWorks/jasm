package com.nullpointerworks.test.controlpanel;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

/**
 * On-The-Pixel positioning layout. 
 * @author Michiel Drost - Nullpointer Works
 */
public class AbsoluteLayout implements LayoutManager 
{
	@Override
	public void layoutContainer(Container parent) 
	{
		update(parent);
	}
	
	@Override
	public Dimension minimumLayoutSize(Container parent) 			
	{
		return update(parent);
	}
	
	@Override
	public Dimension preferredLayoutSize(Container parent) 			
	{
		return update(parent);
	}
	
	private Dimension update(Container parent)
	{
		synchronized (parent.getTreeLock())
		{
			int x = Integer.MAX_VALUE;
			int y = Integer.MAX_VALUE;
			int w = Integer.MIN_VALUE;
			int h = Integer.MIN_VALUE;
			
			int cNum = parent.getComponentCount();
			for (int i = 0 ; i < cNum ; i++)
	        {
	            Component c = parent.getComponent(i);
	            if (c.isVisible()) 
	            {
	            	int cx = c.getX();
	        		int cy = c.getY();
	        		int cw = c.getWidth();
	        		int ch = c.getHeight();
	        		c.setBounds(cx,cy,cw,ch);
	        		cw += cx;
	        		ch += cy;
	            	if (cx < x) x = cx;
	            	if (cy < y) y = cy;
	            	if (cw > w) w = cw;
	            	if (ch > h) h = ch;
	            }
	        }
			
			return new Dimension(w-x,h-y);
		}
	}
	
	@Override
    public void addLayoutComponent(String name, Component comp) {}
	
	@Override
    public void removeLayoutComponent(Component comp) {}
}

