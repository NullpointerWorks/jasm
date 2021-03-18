package com.nullpointerworks.jasm.asm.assembler.segment;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.error.ParseError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class DataSegmentBuilder
{
	private List< Pair<Draft, Integer> > labels;
	private List<Integer> data; // data segment, allocated data
	private BuildError error;
	
	public DataSegmentBuilder()
	{
		labels = new ArrayList< Pair<Draft, Integer> >();
		data = new ArrayList<Integer>();
		
	}
	
	public void processSourceCode(SourceCode sc) 
	{
		String line = sc.getLine();
		
		// the parser has already dealt with defines, includes and origin
		if (line.startsWith(".def")) return;
		if (line.startsWith(".inc")) return;
		if (line.startsWith(".org")) return;
		
		if (line.startsWith(".data"))
		{
			processData(sc);
			
			
			//String label = line.substring(0,line.length()-1);
			//labels.put(label.toLowerCase(), instIndex); // labels are not case sensitive
			
			
			return;
		}
		
		if (line.startsWith(".res"))
		{
			processReserved(sc);
			return;
		}
	}
	
	private void processData(SourceCode sc) 
	{
		String line = sc.getLine();
		
		
		
	}
	
	private void processReserved(SourceCode sc) 
	{
		String line = sc.getLine();
		
		
		
		
		
		
	}
	
	private void setError(SourceCode sc, String message)
	{
		error = new ParseError(sc,message);
	}
}
