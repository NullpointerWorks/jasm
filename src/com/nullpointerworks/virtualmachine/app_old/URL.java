package com.nullpointerworks.virtualmachine.app_old;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A URL string builder
 * @author Michiel Drost - Nullpointer Works
 * @since 1.0.0
 */
public class URL 
{
	private List<String> folders = null;
	private String file = "";
	
	/**
	 * 
	 * @since 1.0.0
	 */
	public URL()
	{
		folders = new ArrayList<String>();
	}
	
	/**
	 * 
	 * @since 1.0.0
	 */
	public URL(String path) 
	{
		this();
		boolean fend = false;
		path = path.replace("\\", "/");
		String[] tokens = path.split("/");
		
		if (tokens.length < 1) return;
		
		String finalDir = tokens[tokens.length-1];
		if (finalDir.contains("."))
		{
			fend = true;
			file( tokens[tokens.length-1] );
		}
		
		int i=0;
		int l=tokens.length - ((fend)?1:0);
		for (; i<l; i++)
		{
			folders.add(tokens[i]);
		}
	}
	
	/*
	 * 
	 * @since 1.0.0
	 */
	private URL(List<String> d, String file) 
	{
		this();
		for (String dir : d) folder(dir);
		this.file(file);
	}
	
	// ========================================================================
	
	/**
	 * 
	 * @since 1.0.0
	 */
	public URL folder(String folder)
	{
		folder = folder.replace("\\", "/");
		String[] nav = folder.split("/");
		for (int i=0,l=nav.length; i<l; i++)
		{
			String s = nav[i];
			if (s.equalsIgnoreCase("")) continue;
			{
				folders.add(s);
			}
		}
		return this;
	}
	
	/**
	 * 
	 * @since 1.0.0
	 */
	public URL pop()
	{
		folders.remove(folders.size()-1);
		return this;
	}
	
	/**
	 * 
	 * @since 1.0.0
	 */
	public String folderPath()
	{
		String res = "";
		for (String f : folders)
		{
			res = res + f + "/";
		}
		return res;
	}
	
	/**
	 * 
	 * @since 1.0.0
	 */
	public URL file(String file)
	{
		this.file=file.trim();
		return this;
	}
	
	/**
	 * returns the complete folder path with the name of the target file
	 * @since 1.0.0
	 */
	public String filePath()
	{
		return folderPath() + fileName();
	}
	
	/**
	 * returns the file name
	 * @since 1.0.0
	 */
	public String fileName()
	{
		return file;
	}
	
	/**
	 * 
	 * @since 1.0.0
	 */
	public URL fileName(String file)
	{
		this.file=file;
		return this;
	}
	
	// ========================================================================
	
	/**
	 * 
	 * @since 1.0.0
	 */
	public boolean folderOnly()
	{
		return file.equalsIgnoreCase("");
	}
	
	/**
	 * 
	 * @since 1.0.0
	 */
	public URL copy()
	{
		return new URL(folders,file);
	}
	
	/**
	 * 
	 * @since 1.0.0
	 */
	public URL add(URL url)
	{
		this.folder(url.folderPath());
		this.file(url.fileName());
		return this;
	}
	
	/**
	 * 
	 * @return the name of the folder popped
	 * @since 1.0.0
	 */
	public String popFolder()
	{
		int index = folders.size()-1;
		String name = folders.get(index);
		folders.remove(index);
		return name;
	}
	
	/**
	 * 
	 * @since 1.0.0
	 */
	public boolean exists() 
	{
		return new File(filePath()).exists();
	}
	
	/**
	 * 
	 * @since 1.0.0
	 */
	public void create() 
	{
		new File(folderPath()).mkdirs();
		
		if (file==null) return;
		if (file.equals("")) return;
		try 
		{
			new File(filePath()).createNewFile();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
