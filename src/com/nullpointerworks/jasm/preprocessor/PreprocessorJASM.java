package com.nullpointerworks.jasm.preprocessor;

import java.util.List;

import com.nullpointerworks.jasm.instructions.Instruction;
import com.nullpointerworks.jasm.parser.Parser;

public class PreprocessorJASM implements Preprocessor 
{
	public PreprocessorJASM() 
	{
		reset();
	}

	@Override
	public Preprocessor reset() 
	{
		return null;
	}

	@Override
	public Preprocessor setVerbose(boolean verbose) 
	{
		return null;
	}

	@Override
	public Preprocessor preprocess(Parser parser) 
	{
		return null;
	}

	@Override
	public boolean hasErrors() 
	{
		return false;
	}

	@Override
	public List<PreProcessingError> getErrors() 
	{
		return null;
	}

	@Override
	public List<Instruction> getInstructions() 
	{
		return null;
	}

}
