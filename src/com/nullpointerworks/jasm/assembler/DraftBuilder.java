package com.nullpointerworks.jasm.assembler;

import com.nullpointerworks.jasm.assembler.errors.BuildError;
import com.nullpointerworks.jasm.assembler.builder.ArithmeticBuilder;
import com.nullpointerworks.jasm.assembler.builder.ControlFlowBuilder;
import com.nullpointerworks.jasm.assembler.builder.DataFlowBuilder;
import com.nullpointerworks.jasm.assembler.builder.SystemCallBuilder;

/**
 * Turns SourceCode objects into Draft objects by interpreting it's text.
 * 
 * @author Michiel Drost - Nullpointer Works
 */
public class DraftBuilder
{
	private BuildError error;
	private ControlFlowBuilder cfBuilder;
	private ArithmeticBuilder arBuilder;
	private DataFlowBuilder dfBuilder;
	private SystemCallBuilder scBuilder;
	
	public boolean hasError() {return error != null;}
	public void setError(BuildError e) {error = e;}
	public BuildError getError() {return error;}
	
	public DraftBuilder()
	{
		cfBuilder = new ControlFlowBuilder();
		arBuilder = new ArithmeticBuilder();
		dfBuilder = new DataFlowBuilder();
		scBuilder = new SystemCallBuilder();
	}
	
	public Draft[] getDraft(SourceCode loc)
	{
		error = null;
		String[] parts = loc.getLine().split(" ");
		String instruct = parts[0].toLowerCase();
		Draft[] result = {};
		
		/*
		 * system call
		 */
		if (scBuilder.isSystemCall(instruct))
		{
			result = scBuilder.getDraft(loc);
			error = scBuilder.getError();
		}
		
		/*
		 * arithmetic
		 */
		if (arBuilder.isArithmetic(instruct))
		{
			result = arBuilder.getDraft(loc);
			error = arBuilder.getError();
		}
		
		/*
		 * data transfer
		 */
		if (dfBuilder.isDataFlow(instruct))
		{
			result = dfBuilder.getDraft(loc);
			error = dfBuilder.getError();
		}
		
		/*
		 * control flow
		 */
		if (cfBuilder.isControlFlow(instruct))
		{
			result = cfBuilder.getDraft(loc);
			error = cfBuilder.getError();
		}
		
		return result;
	}
}
