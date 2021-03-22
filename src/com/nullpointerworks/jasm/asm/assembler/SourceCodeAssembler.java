package com.nullpointerworks.jasm.asm.assembler;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.assembler.segment.CodeSegmentBuilder;
import com.nullpointerworks.jasm.asm.assembler.segment.DataSegmentBuilder;
import com.nullpointerworks.jasm.asm.assembler.segment.LabelManager;
import com.nullpointerworks.jasm.asm.assembler.segment.Number;
import com.nullpointerworks.jasm.asm.assembler.segment.SegmentBuilder;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.Definition;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class SourceCodeAssembler implements Assembler
{
	private List<BuildError> errors; // contains errors
	private List<Integer> result; // resulting machine code
	
	private VerboseListener verbose;
	
	private LabelManager manager;
	private SegmentBuilder codeBuilder;
	private SegmentBuilder dataBuilder;
	
	public SourceCodeAssembler()
	{
		errors = new ArrayList<BuildError>();
		result = new ArrayList<Integer>();
		
		manager = new LabelManager();
		codeBuilder = new CodeSegmentBuilder(manager);
		dataBuilder = new DataSegmentBuilder(manager);
		
		verbose = (s)->{};
	}
	
	@Override
	public void setVerboseListener(VerboseListener verbose) 
	{
		this.verbose=verbose;
	}
	
	@Override
	public boolean hasErrors() 
	{
		return errors.size() > 0;
	}
	
	@Override
	public List<BuildError> getErrors() 
	{
		return errors;
	}
	
	@Override
	public List<Integer> getMachineCode() 
	{
		result.clear();
		addToResult(codeBuilder);
		addToResult(dataBuilder);
		return result;
	}
	
	@Override
	public void draft(List<SourceCode> sourcecode, List<Definition> definitions)
	{
		manager.setVerboseListener(verbose);
		codeBuilder.setVerboseListener(verbose);
		
		verbose.onPrint("-------------------------------");
		verbose.onPrint("Assembler Start\n");
		
		insertDefinition(sourcecode, definitions);
		
		for (int i=0,l=sourcecode.size(); i<l; i++)
		{
			SourceCode sc = sourcecode.get(i);
			processCode(sc);
			if (hasErrors()) return;
		}
		
		codeBuilder.setOffset( 0 );
		dataBuilder.setOffset( codeBuilder.getByteCode().size() );
		
		manager.setCommitLabels();
		if (manager.hasError())
		{
			errors.add( manager.getError() );
		}
		
		verbose.onPrint("\nAssembler End");
		verbose.onPrint("-------------------------------");
	}
	
	private void insertDefinition(List<SourceCode> code, List<Definition> defs) 
	{
		/*
		 * print definitions
		 */
		verbose.onPrint("Definitions");
		for (Definition d : defs)
		{
			verbose.onPrint( "  "+d.NAME +" = "+d.VALUE );
		}
		verbose.onPrint("");
		
		/*
		 * for each source code object, scan if it contains a definition
		 * if so, replace definition with it's value
		 */
		for (int i=0,l=code.size(); i<l; i++)
		{
			SourceCode loc = code.get(i);
			String line = loc.getLine();
			
			for (Definition d : defs)
			{
				String name = d.NAME;
				if (line.contains(name))
				{
					line = line.replace(name, d.VALUE);
					loc.setLine(line);
					break;
				}
			}
		}
	}
	
	private void processCode(SourceCode sc) 
	{
		String line = sc.getLine();
		
		// the parser already dealt with defines, includes and origin
		if (line.startsWith(".def")) return;
		if (line.startsWith(".inc")) return;
		if (line.startsWith(".org")) return;
		
		if (line.startsWith(".data"))
		{
			dataBuilder.addSourceCode(sc);
			if (dataBuilder.hasError()) errors.add( dataBuilder.getError() );
			return;
		}
		
		if (line.startsWith(".res"))
		{
			dataBuilder.addSourceCode(sc);
			if (dataBuilder.hasError()) errors.add( dataBuilder.getError() );
			return;
		}
		
		codeBuilder.addSourceCode(sc);
		if (codeBuilder.hasError()) errors.add( codeBuilder.getError() );
	}
	
	private void addToResult(SegmentBuilder builder) 
	{
		List<Number> bc = builder.getByteCode();
		for (Number n : bc)
		{
			result.add( n.getValue() );
		}
	}
}
