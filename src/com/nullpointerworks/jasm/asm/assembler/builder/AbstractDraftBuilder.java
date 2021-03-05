package com.nullpointerworks.jasm.asm.assembler.builder;

import java.util.List;

import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.error.AssembleError;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

abstract class AbstractDraftBuilder implements IDraftBuilder
{
	private BuildError error;
	
	public AbstractDraftBuilder()
	{
		error = null;
	}
	
	protected void buildNOP(SourceCode sc, List<Draft> draft) 
	{
		Draft d = new Draft(sc);
		d.addMachineCode(0);
		draft.add(d);
	}
	
	protected void throwError(SourceCode sc, String desc) 
	{
		setError( new AssembleError(sc, desc) );
	}
	
	@Override
	public boolean hasError() 
	{
		return error != null;
	}
	
	@Override
	public void setError(BuildError err)
	{
		error = err;
	}
	
	@Override
	public BuildError getError()
	{
		return error;
	}
}
