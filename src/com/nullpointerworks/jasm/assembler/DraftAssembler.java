package com.nullpointerworks.jasm.assembler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nullpointerworks.jasm.assembler.errors.BuildError;
import com.nullpointerworks.jasm.assembler.errors.PreProcessorError;

public class DraftAssembler implements Assembler
{
	private int instIndex = 0; // label instruction index
	private boolean verbose;
	
	private List<SourceCode> code;
	private List<Definition> defs;
	private final DraftBuilder builder;
	private List<BuildError> errors;
	private Map<String, Integer> labels;
	private List<Draft> draft;
	private List<Draft> labelled;
	
	public DraftAssembler()
	{
		builder = new DraftBuilder();
		verbose = false;
		errors = new ArrayList<BuildError>();
		code = new ArrayList<SourceCode>();
		defs = new ArrayList<Definition>();
		labels = new HashMap<String, Integer>();
		draft = new ArrayList<Draft>();
		labelled = new ArrayList<Draft>();
	}
	
	@Override
	public void setSourceCode(List<SourceCode> sc)
	{
		code.clear();
		code = sc;
	}
	
	@Override
	public void setDefinitions(List<Definition> df)
	{
		defs.clear();
		defs = df;
	}

	@Override
	public void setVerbose(boolean verbose) 
	{
		this.verbose = verbose;
	}
	
	@Override
	public void assemble()
	{
		out("-------------------------------");
		out("\n Assembling\n");
		insertDefinition(code, defs);
		processCode(code);
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
	public List<Draft> getDraft()
	{
		return draft;
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
	
	private void insertDefinition(List<SourceCode> code, List<Definition> defs) 
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
	
	private void processCode(List<SourceCode> code) 
	{
		/*
		 * track directives and labels
		 * process instructions
		 */
		for (int i=0,l=code.size(); i<l; i++)
		{
			SourceCode loc = code.get(i);
			processLine(loc);
			if (hasErrors()) return;
		}
		
		/*
		 * insert label addresses
		 */
		for (Draft d : labelled)
		{
			String label = d.getLabel();
			if (!labels.containsKey(label))
			{
				addPreProcError(d.getSourceCode(), "  Unknown label reference");
				return;
			}
			int addr = labels.get(label);
			d.setJumpAddress(addr);
		}
		
		return;
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
		
		/*
		 * build a draft from the source code
		 */
		instIndex += 1;
		Draft[] draft_inst = builder.getDraft(loc);
		if (builder.hasError())
		{
			errors.add(builder.getError());
			return;
		}
		
		/*
		 * add new draft objects
		 */
		for (Draft d : draft_inst)
		{
			if (d.hasError())
			{
				errors.add(d.getError());
				return;
			}
			draft.add( d );
			
			if (d.hasLabel())
			{
				labelled.add(d);
			}
		}
	}
	
	/*
	 * ==================================================================
	 */
	
	private void addPreProcError(SourceCode code, String message)
	{
		errors.add( new PreProcessorError(code, message) );
	}
	
	private void out(String string)
	{
		if (verbose) System.out.println(string);
	}
}
