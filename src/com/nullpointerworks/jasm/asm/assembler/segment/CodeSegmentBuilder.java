package com.nullpointerworks.jasm.asm.assembler.segment;

import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class CodeSegmentBuilder implements SegmentBuilder 
{
	private VerboseListener verbose;
	private BuildError error;
	
	public CodeSegmentBuilder()
	{
		verbose = (str)->{};
		error = null;
		
		
		
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
	public void setOffset(int offset) 
	{
		
	}

	@Override
	public List<Number> getByteCode() 
	{
		return null;
	}

	@Override
	public void addSourceCode(SourceCode sc) 
	{
		String line = sc.getLine();
		
		if (line.contains(":"))
		{
			
			return;
		}
		
		
		
		
	}

}
