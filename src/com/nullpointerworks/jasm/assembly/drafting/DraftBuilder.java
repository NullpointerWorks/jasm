package com.nullpointerworks.jasm.assembly.drafting;

import com.nullpointerworks.jasm.assembly.errors.AssemblerError;
import com.nullpointerworks.jasm.assembly.errors.BuildError;
import com.nullpointerworks.jasm.assembly.parser.SourceCode;

/**
 * Turns SourceCode objects into Draft objects by interpreting it's text.
 * 
 * @author Michiel Drost - Nullpointer Works
 */
public class DraftBuilder
{	
	private SourceCode source;
	private BuildError error;
	
	public boolean hasError() {return error != null;}
	public void setError(BuildError e) {error = e;}
	public BuildError getError() {return error;}
	
	private SystemCallBuilder sysBuilder;
	private ArithmeticBuilder mathBuilder;
	private DataFlowBuilder dataBuilder;
	private ControlFlowBuilder ctrlBuilder;
	
	public DraftBuilder() 
	{
		sysBuilder = new SystemCallBuilder();
		mathBuilder = new ArithmeticBuilder();
		dataBuilder = new DataFlowBuilder();
		ctrlBuilder = new ControlFlowBuilder();
	}
	
	public Draft[] getDraft(SourceCode loc)
	{
		error = null;
		source = loc;
		
		Draft[] newDraft;
		String[] parts = loc.getLine().split(" ");
		String instruct = parts[0].toLowerCase();
		
		/*
		 * system
		 */
		if (sysBuilder.isSystemCall(instruct))
		{
			newDraft = sysBuilder.getDraft(loc);
			error = sysBuilder.getError();
		}
		else
		/*
		 * arithmetic
		 */
		if (mathBuilder.isArithmetic(instruct))
		{
			newDraft = mathBuilder.getDraft(loc);
			error = mathBuilder.getError();
		}
		else
		/*
		 * data transfer
		 */
		if (dataBuilder.isDataFlow(instruct))
		{
			newDraft = dataBuilder.getDraft(loc);
			error = dataBuilder.getError();
		}
		/*
		 * control flow
		 */
		else
		if (ctrlBuilder.isControlFlow(instruct))
		{
			newDraft = ctrlBuilder.getDraft(loc);
			error = ctrlBuilder.getError();
		}
		else
		{
			newDraft = new Draft[] { _nop() };
			error = new AssemblerError(source, "  Unrecognized instruction or parameters");
		}
		
		return newDraft;
	}
	
	private Draft _nop()
	{
		Draft d = new Draft(source, Operation.NOP);
		return d;
	}
}
