package com.nullpointerworks.jasm.asm.assembler.segment;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.ParserUtility;
import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.error.AssembleError;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class DataSegmentBuilder implements SegmentBuilder
{
	private final String res_syntax = "\n  .res <name> <integer>";
	
	private LabelManager manager;
	private List<Number> adrs; // addresses
	private List<Number> data; // byte code
	private VerboseListener verbose;
	private BuildError error;
	
	public DataSegmentBuilder(LabelManager m) 
	{
		manager = m;
		adrs = new ArrayList<Number>();
		data = new ArrayList<Number>();
		error = null;
		verbose = (s)->{};
	}
	
	@Override
	public void setVerboseListener(VerboseListener v) 
	{
		verbose = v;
	}
	
	@Override
	public boolean hasError() 
	{
		return error != null;
	}
	
	@Override
	public BuildError getError() 
	{
		return error;
	}
	
	@Override
	public void addSourceCode(SourceCode sc) 
	{
		String line = sc.getLine();
		error = null;
		
		if (line.startsWith(".data"))
		{
			processData(sc);
			return;
		}
		
		if (line.startsWith(".res"))
		{
			processReserved(sc);
			return;
		}
	}
	
	@Override
	public void setOffset(int offset) 
	{
		for (Number n : adrs) n.addValue(offset);
	}
	
	@Override
	public List<Number> getByteCode() 
	{
		return data;
	}
	
	private void processData(SourceCode sc) 
	{
		String line = sc.getLine();
		
	}
	
	private void processReserved(SourceCode sc) 
	{
		String line = sc.getLine();
		String[] tokens = line.split(" ");
		if (tokens.length != 3)
		{
			setError(sc, "  Bad memory allocation syntax."+res_syntax);
			return;
		}
		
		String num = tokens[2];
		if (!ParserUtility.isInteger(num))
		{
			setError(sc, "  Memory allocation should be an integer number.");
			return;
		}
		
		String label = tokens[1];
		Number number = new Number( data.size() );
		
		// add allocation address as label, pad the data segment with the amount to be allocated
		manager.addLabelPointer(label, number);
		adrs.add(number);
		
		int value = ParserUtility.getIntegerValue(num);
		for (int i=0,l=value; i<l; i++) data.add( new Number(0) );
	}
	
	private void setError(SourceCode sc, String message)
	{
		error = new AssembleError(sc,message);
	}
}
