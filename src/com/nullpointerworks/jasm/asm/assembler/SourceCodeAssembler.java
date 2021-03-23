package com.nullpointerworks.jasm.asm.assembler;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.assembler.segment.CodeSegmentBuilder;
import com.nullpointerworks.jasm.asm.assembler.segment.LabelManager;
import com.nullpointerworks.jasm.asm.assembler.segment.Number;
import com.nullpointerworks.jasm.asm.assembler.segment.SegmentBuilder;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class SourceCodeAssembler implements Assembler
{
	private List<BuildError> errors; // contains errors
	private List<Integer> result; // resulting machine code
	
	private VerboseListener verbose;

	private LabelManager labelManager;
	private LabelManager defineManager;
	private LabelManager referenceManager;
	
	private SegmentBuilder codeBuilder;
	
	public SourceCodeAssembler()
	{
		errors = new ArrayList<BuildError>();
		result = new ArrayList<Integer>();

		labelManager = new LabelManager();
		defineManager = new LabelManager();
		referenceManager = new LabelManager();
		
		codeBuilder = new CodeSegmentBuilder();
		
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
	public void draft(List<SourceCode> sourcecode)
	{
		labelManager.setVerboseListener(verbose);
		codeBuilder.setVerboseListener(verbose);
		
		verbose.onPrint("-------------------------------");
		verbose.onPrint("Assembler Start\n");
		
		for (int i=0,l=sourcecode.size(); i<l; i++)
		{
			SourceCode sc = sourcecode.get(i);
			processCode(sc);
			if (hasErrors()) return;
		}
		
		codeBuilder.setOffset( 0 );
		
		
		labelManager.setCommitLabels();
		if (labelManager.hasError())
		{
			errors.add( labelManager.getError() );
		}
		if (hasErrors()) return;
		
		result.clear();
		addToResult(codeBuilder);
		
		verbose.onPrint("\nAssembler End");
		verbose.onPrint("-------------------------------");
	}
	
	private void processCode(SourceCode sc) 
	{
		String line = sc.getLine();
		
		if (line.startsWith(".def"))
		{
			return;
		}
		if (line.startsWith(".")) return;
		
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
