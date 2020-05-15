package com.nullpointerworks.jasm.compiler;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.BuildError;
import com.nullpointerworks.jasm.Compiler;
import com.nullpointerworks.jasm.Preprocessor;
import com.nullpointerworks.jasm.preprocessor.DraftJASM;
import com.nullpointerworks.jasm.virtualmachine.instruction.Instruction;
import com.nullpointerworks.util.Log;

public class CompilerJASM implements Compiler
{
	private List<BuildError> errors = null;
	private List<Instruction> instructions;
	private boolean verbose_compiler = false;
	
	public CompilerJASM()
	{
		reset();
	}
	
	/**
	 * 
	 */
	@Override
	public Compiler setVerbose(boolean verbose)
	{
		verbose_compiler = verbose;
		return this;
	}
	
	/**
	 * 
	 */
	@Override
	public Compiler reset()
	{
		if (errors!=null) errors.clear();
		errors = new ArrayList<BuildError>();
		
		if (instructions!=null) instructions.clear();
		instructions = new ArrayList<Instruction>();
		
		return this;
	}
	
	/**
	 * 
	 */
	@Override
	public Compiler compile(Preprocessor preproc)
	{
		if (verbose_compiler)
		{
			Log.out("-------------------------------");
			Log.out("\n Compiling\n");
		}
		
		List<DraftJASM> draft = preproc.getDraft();
		for (DraftJASM d : draft)
		{
			instructions.add( d.getInstruction() );
		}
		
		if (verbose_compiler)
		{
			Log.out("\n Successful\n");
			Log.out("-------------------------------");
		}
		
		return this;
	}
	
	/**
	 * 
	 */
	@Override
	public Compiler save(String filepath)
	{
		if (hasErrors()) return this;
		List<Instruction> insts =  getInstructions();
		
		
		
		
		
		return this;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean hasErrors()
	{
		return errors.size() > 0;
	}
	
	/**
	 * 
	 */
	@Override
	public List<BuildError> getErrors()
	{
		return errors;
	}
	
	/**
	 * 
	 */
	@Override
	public List<Instruction> getInstructions()
	{
		return instructions;
	}
}
