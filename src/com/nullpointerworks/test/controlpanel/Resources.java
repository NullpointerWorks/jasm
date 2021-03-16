package com.nullpointerworks.test.controlpanel;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public final class Resources 
{
	private static String[] iconsURL = 
	{
		"/com/nullpointerworks/test/resources/IP.png", // instruction pointer arrow
		"/com/nullpointerworks/test/resources/SP.png", // stack pointer arrow
		"/com/nullpointerworks/test/resources/IPSP.png" ,// IP and SP combination
		"/com/nullpointerworks/test/resources/btn_NEW.png",
		"/com/nullpointerworks/test/resources/btn_OPEN.png",
		"/com/nullpointerworks/test/resources/btn_SAVE.png"
	};
	
	public static ImageIcon getStreamedIcon(String path) 
	{
		InputStream is = Loader.getResourceAsStream(path);
        BufferedImage img = null;
		try 
		{
			img = ImageIO.read(is);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if (img==null) return null;
		return new ImageIcon(img);
	}
	
	public static ImageIcon getIPIcon()
	{return getStreamedIcon(iconsURL[0]);}
	
	public static ImageIcon getSPIcon()
	{return getStreamedIcon(iconsURL[1]);}
	
	public static ImageIcon getIPSPIcon()
	{return getStreamedIcon(iconsURL[2]);}
	
	public static ImageIcon getNewIcon()
	{return getStreamedIcon(iconsURL[3]);}
	
	public static ImageIcon getOpenIcon()
	{return getStreamedIcon(iconsURL[4]);}
	
	public static ImageIcon getSaveIcon()
	{return getStreamedIcon(iconsURL[5]);}
}
