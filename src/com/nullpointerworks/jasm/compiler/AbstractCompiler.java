package com.nullpointerworks.jasm.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nullpointerworks.jasm.compiler.errors.BuildError;
import com.nullpointerworks.jasm.compiler.errors.PreProcessorError;

public abstract class AbstractCompiler<T> implements Compiler<T>
{
	private int instIndex = 0; // label instruction index
	private boolean verbose;
	
	private List<BuildError> errors;
	private Map<String, Integer> labels;
	private List<T> instructions;
	private List< Draft<T> > labelled;
	
	public AbstractCompiler() 
	{
		verbose = false;
		errors = new ArrayList<BuildError>();
		labels = new HashMap<String, Integer>();
		instructions = new ArrayList<T>();
		labelled = new ArrayList<Draft<T>>();
	}

	@Override
	public void setVerbose(boolean verbose) 
	{
		this.verbose = verbose;
	}
	
	@Override
	public void preprocess(Parser parser) 
	{
		List<SourceCode> code = parser.getSourceCode();
		List<Definition> defs = parser.getDefinitions();
		
		out("-------------------------------");
		out("\n Compiling\n");
		insert(code, defs);
		process(code);
		out("\n Done\n");
		out("-------------------------------");
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
	public List<T> getInstructions()
	{
		return instructions;
	}
	
	/* ==================================================================
	 * 
	 * pre-processor
	 * 
	 * responsibility:
	 * insert "def" directives
	 * turns labels into addresses
	 * drafts machine instructions
	 * inserts addresses into draft
	 * 
	 * ==================================================================
	 */
	private Compiler<T> process(List<SourceCode> code) 
	{
		/*
		 * track directives and labels
		 * process instructions
		 */
		for (int i=0,l=code.size(); i<l; i++)
		{
			SourceCode loc = code.get(i);
			processLine(loc);
			if (hasErrors()) return this;
		}
		
		/*
		 * insert label addresses
		 */
		for (Draft<T> d : labelled)
		{
			String label = d.getLabel();
			if (!labels.containsKey(label))
			{
				addPreProcError(d.getLineOfCode(), "  Unknown label reference");
				return this;
			}
			int addr = labels.get(label);
			d.setJumpAddress(addr);
		}
		
		return this;
	}

	private void processLine(SourceCode loc) 
	{
		String line = loc.getLine();
		
		/*
		 * is a definition
		 */
		if (line.startsWith("."))
		{
			
			return;
		}
		
		/*
		 * is a label
		 */
		if (line.contains(":"))
		{
			String label = line.substring(0,line.length()-1);
			labels.put(label, instIndex);
			return;
		}
		
		instIndex += 1;
		var draft_inst = compile(instIndex, loc);
		instructions.add( draft_inst.getInstruction() );
		
		if (draft_inst.hasError())
		{
			errors.add(draft_inst.getError());
			return;
		}
		
		if (draft_inst.hasLabel())
		{
			labelled.add(draft_inst);
		}
	}
	
	private void insert(List<SourceCode> code, List<Definition> defs) 
	{
		/*
		 * print definitions
		 */
		if (verbose)
		{
			out("Definitions");
			for (Definition d : defs)
			{
				out( "  "+d.NAME +" = "+d.VALUE );
			}
		}
		
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
	
	private void addPreProcError(SourceCode code, String message)
	{
		errors.add( new PreProcessorError(code, message) );
	}
	
	private void out(String string)
	{
		if (verbose) System.out.println(string);
	}
}
