package com.nullpointerworks.jasm.compiler;

import java.util.ArrayList;
import java.util.List;

/*
 * parser reference
 */
import com.nullpointerworks.jasm.preprocessor.DraftJASM;
import com.nullpointerworks.jasm.preprocessor.Preprocessor;
import com.nullpointerworks.jasm.instruction.Instruction;

import com.nullpointerworks.util.Log;

public class CompilerJASM implements Compiler
{
	private List<CompileError> errors = null;
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
		errors = new ArrayList<CompileError>();
		
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
		
		compileDraft(preproc.getDraft());
		
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
	public boolean hasErrors()
	{
		return errors.size() > 0;
	}
	
	/**
	 * 
	 */
	@Override
	public List<CompileError> getErrors()
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
	
	/* ==================================================================
	 * 
	 * compiler
	 * 
	 * responsibility:
	 * takes the machine code from the draft 
	 * creates the program byte array
	 * 
	 * ==================================================================
	 */
	private void compileDraft(List<DraftJASM> draft)
	{
		/*
		 * get machine code from each draft
		 */
		for (DraftJASM d : draft)
		{
			instructions.add( d.getInstruction() );
		}
	}
}
