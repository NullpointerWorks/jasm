package com.nullpointerworks.jasm.compiler;

import com.nullpointerworks.jasm.compiler.errors.BuildError;
import com.nullpointerworks.jasm.compiler.errors.CompilerError;

import com.nullpointerworks.jasm.virtualmachine.Select;
import com.nullpointerworks.jasm.virtualmachine.Instruction;
import com.nullpointerworks.jasm.virtualmachine.Register;
import com.nullpointerworks.jasm.virtualmachine.instruction.arithmetic.*;
import com.nullpointerworks.jasm.virtualmachine.instruction.controlflow.*;
import com.nullpointerworks.jasm.virtualmachine.instruction.dataflow.*;
import com.nullpointerworks.jasm.virtualmachine.instruction.system.*;

import com.nullpointerworks.util.StringUtil;

/**
 * 
 * @author Michiel Drost - Nullpointer Works
 */
public class Draft
{
	private final SourceCode source;
	
	public Draft(SourceCode sc)
	{
		source = sc;
		
		
		
		
		
	}

	public SourceCode getSourceCode() 
	{
		return source;
	}
	
	/*
	 * labels
	 */
	public final boolean hasLabel() {return label.length() > 0;}
	
	public final String getLabel() {return label;}
	
	public final void setJumpAddress(int addr) {address.setValue(addr);}
	
	/*
	 * errors
	 */
	public boolean hasError() {return err != null;}
	
	public BuildError getError() {return err;}
}
