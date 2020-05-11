package com.nullpointerworks.jasm.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * parser reference
 */
import com.nullpointerworks.jasm.parser.SourceCode;
import com.nullpointerworks.jasm.instructions.Instruction;
import com.nullpointerworks.jasm.parser.Parser;

import com.nullpointerworks.util.Log;

public class CompilerJASM implements Compiler
{
	private List<CompileError> errors = null;
	private boolean verbose_preproc = false;
	private boolean verbose_compiler = false;
	
	/*
	 * text to machine language utility
	 */
	private Map<String, Integer> labels = null;
	private List<DraftJASM> draft = null;
	private List<DraftJASM> labelled = null;
	private List<Instruction> instructions;
	
	/*
	 * text formatting utility
	 */
	private int instIndex = 0; // label instruction index
	
	public CompilerJASM()
	{
		reset();
	}

	// ==================================================================
	// 	public compiler code
	// ==================================================================
	
	/**
	 * 
	 */
	@Override
	public Compiler setVerbose(boolean verbose)
	{
		verbose_preproc = verbose;
		verbose_compiler = verbose;
		return this;
	}
	
	/**
	 * 
	 */
	@Override
	public Compiler reset()
	{
		instIndex = 0;
		
		if (labels!=null) labels.clear();
		labels = new HashMap<String, Integer>();
		
		if (errors!=null) errors.clear();
		errors = new ArrayList<CompileError>();
		
		if (draft!=null) draft.clear();
		draft = new ArrayList<DraftJASM>();
		
		if (labelled!=null) labelled.clear();
		labelled = new ArrayList<DraftJASM>();
		
		if (instructions!=null) instructions.clear();
		instructions = new ArrayList<Instruction>();
		
		return this;
	}
	
	/**
	 * 
	 */
	@Override
	public Compiler compile(Parser parser)
	{
		/*
		 * 
		 */
		if (verbose_preproc)
		{
			Log.out("-------------------------------");
			Log.out("\n Pre-processor\n");
		}
		preprocessor(parser.getSourceCode());
		if (verbose_preproc)
		{
			Log.out("\n Done\n");
			Log.out("-------------------------------");
		}
		
		/*
		 * 
		 */
		if (verbose_compiler)
		{
			Log.out("-------------------------------");
			Log.out("\n Compiling\n");
		}
		compileDraft(draft);
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
	 * pre-processor
	 * 
	 * responsibility:
	 * handles directives
	 * turns labels into addresses
	 * drafts machine instructions
	 * 
	 * ==================================================================
	 */
	private void preprocessor(List<SourceCode> code)
	{
		/*
		 * track directives and labels
		 * process instructions
		 */
		for (int i=0,l=code.size(); i<l; i++)
		{
			SourceCode loc = code.get(i);
			String line = loc.getLine();
			if (line.startsWith("."))
			{
				continue;
			}
			
			// is a label
			if (line.contains(":")) 
			{
				String label = line.substring(0,line.length()-1);
				labels.put(label, instIndex);
				continue;
			}
			
			instIndex += 1;
			var draft_inst = buildDraft(instIndex, loc);
			
			if (draft_inst.hasError())
			{
				
			}
			
			if (draft_inst.hasLabel())
			{
				labelled.add(draft_inst);
			}
			
			draft.add(draft_inst);
		}
		
		/*
		 * insert label addresses
		 */
		for (DraftJASM d : labelled)
		{
			String label = d.getLabel();
			if (!labels.containsKey(label))
			{
				addError(d.getLineOfCode(), "Unknown label reference");
				return;
			}
			int addr = labels.get(label);
			d.setLabelAddress(addr);
		}
	}
	
	/* ==================================================================
	 * 
	 * draft builder
	 * 
	 * responsibility:
	 * takes a line of code and turns it into an instruction
	 * 
	 * 
	 * ==================================================================
	 */
	private DraftJASM buildDraft(int index, SourceCode loc)
	{
		return new DraftJASM(index, loc);
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
	
	private void addError(SourceCode code, String message)
	{
		errors.add( new CompileError(code.getFilename(), code.getLinenumber(), code.getLine(), message) );
	}
}
