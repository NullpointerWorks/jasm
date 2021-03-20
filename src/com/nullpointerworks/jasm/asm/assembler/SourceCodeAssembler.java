package com.nullpointerworks.jasm.asm.assembler;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.assembler.builder.SuperDraftBuilder;
import com.nullpointerworks.jasm.asm.assembler.builder.DraftBuilder;
import com.nullpointerworks.jasm.asm.assembler.segment.CodeSegmentBuilder;
import com.nullpointerworks.jasm.asm.assembler.segment.DataSegmentBuilder;
import com.nullpointerworks.jasm.asm.assembler.segment.LabelManager;
import com.nullpointerworks.jasm.asm.assembler.segment.SegmentBuilder;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.Definition;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class SourceCodeAssembler implements Assembler
{
	private List<BuildError> errors; // contains errors
	private List<Integer> result; // resulting machine code
	private List<Integer> code; // code segment, byte code
	
	private VerboseListener verbose;
	
	private LabelManager manager;
	private DraftBuilder draftBuilder;
	private SegmentBuilder codeBuilder;
	private DataSegmentBuilder dataBuilder;
	
	public SourceCodeAssembler()
	{
		errors = new ArrayList<BuildError>();
		result = new ArrayList<Integer>();
		code = new ArrayList<Integer>();
		
		manager = new LabelManager();
		draftBuilder = new SuperDraftBuilder();
		codeBuilder = new CodeSegmentBuilder(manager);
		
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
		return result;
	}
	
	@Override
	public void draft(List<SourceCode> sourcecode, List<Definition> definitions, int origin)
	{
		codeBuilder.setVerboseListener(verbose);
		verbose.onPrint("-------------------------------");
		verbose.onPrint("Assembler Start\n");
		
		setOrigin(origin);
		insertDefinition(sourcecode, definitions);
		
		for (int i=0,l=sourcecode.size(); i<l; i++)
		{
			SourceCode sc = sourcecode.get(i);
			String line = sc.getLine();
			
			if (line.startsWith("."))
			{
				
			}
			else
			{
				codeBuilder.addSourceCode(sc);
				
			}
			
			if (hasErrors()) break;
		}
		
		//insertLabels(labelled, labels, code);
		
		verbose.onPrint("\nAssembler End");
		verbose.onPrint("-------------------------------");
	}
	
	private void setOrigin(int origin) 
	{
		for (int o = origin; o>0; o--) code.add(0);
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
}
