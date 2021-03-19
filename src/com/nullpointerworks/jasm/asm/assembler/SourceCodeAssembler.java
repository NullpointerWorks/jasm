package com.nullpointerworks.jasm.asm.assembler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.assembler.builder.SuperDraftBuilder;
import com.nullpointerworks.jasm.asm.assembler.builder.DraftBuilder;
import com.nullpointerworks.jasm.asm.assembler.segment.CodeSegmentBuilder;
import com.nullpointerworks.jasm.asm.assembler.segment.DataSegmentBuilder;
import com.nullpointerworks.jasm.asm.assembler.segment.LabelManager;
import com.nullpointerworks.jasm.asm.assembler.segment.Pair;
import com.nullpointerworks.jasm.asm.assembler.segment.SegmentBuilder;
import com.nullpointerworks.jasm.asm.error.AssembleError;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.Definition;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class SourceCodeAssembler implements Assembler
{
	private Map<String, Integer> labels; // keeps track of instruction address and attached labels
	private List< Pair<Draft, Integer> > labelled; // 
	
	private List<BuildError> errors; // contains errors
	private List<Integer> result; // resulting machine code
	private List<Integer> code; // code segment, byte code
	
	private VerboseListener verbose;
	private int instIndex;
	
	private LabelManager manager;
	private DraftBuilder draftBuilder;
	private SegmentBuilder codeBuilder;
	private DataSegmentBuilder dataBuilder;
	
	public SourceCodeAssembler()
	{
		labels = new HashMap<String, Integer>();
		labelled = new ArrayList< Pair<Draft, Integer> >();
		
		errors = new ArrayList<BuildError>();
		result = new ArrayList<Integer>();
		code = new ArrayList<Integer>();
		
		manager = new LabelManager();
		draftBuilder = new SuperDraftBuilder();
		codeBuilder = new CodeSegmentBuilder(manager);
		
		instIndex = 0;
		verbose = (s)->{};
	}
	
	@Override
	public void setVerboseListener(VerboseListener verbose) 
	{
		this.verbose=verbose;
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
	public List<Integer> getMachineCode() 
	{
		return result;
	}
	
	@Override
	public void draft(List<SourceCode> sourcecode, List<Definition> definitions, int origin)
	{
		codeBuilder.setVerboseListener(verbose);
		verbose.onPrint("-------------------------------");
		verbose.onPrint("Assembler Start\n");
		
		setOrigin(origin);
		insertDefinition(sourcecode, definitions);
		//processCode(sourcecode);
		
		for (int i=0,l=sourcecode.size(); i<l; i++)
		{
			SourceCode sc = sourcecode.get(i);
			String line = sc.getLine();
			
			if (line.startsWith("."))
			{
				
			}
			else
			{
				codeBuilder.addSourceCode(sc);
				
				
				
				
			}
			
			if (hasErrors()) break;
		}
		
		insertLabels(labelled, labels, code);
		
		verbose.onPrint("\nAssembler End");
		verbose.onPrint("-------------------------------");
	}
	
	private void setOrigin(int origin) 
	{
		instIndex = origin;
		for (int o = origin; o>0; o--) code.add(0);
	}
	
	private void insertDefinition(List<SourceCode> code, List<Definition> defs) 
	{
		/*
		 * print definitions
		 */
		verbose.onPrint("Definitions");
		for (Definition d : defs)
		{
			verbose.onPrint( "  "+d.NAME +" = "+d.VALUE );
		}
		verbose.onPrint("");
		
		/*
		 * for each source code object, scan if it contains a definition
		 * if so, replace definition with it's value
		 */
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
		for (int i=0,l=code.size(); i<l; i++)
		{
			SourceCode loc = code.get(i);
			String line = loc.getLine();
			
			// is a directive
			if (line.startsWith("."))
			{
				//processDirective(loc);
			}
			
			// is line of code
			else
			{
				processLine(loc);
			}
			
			if (hasErrors()) return;
		}
	}
	
	private void processDirective(SourceCode sc) 
	{
		String line = sc.getLine();
		
		// the parser has already dealt with defines, includes and origin
		if (line.startsWith(".def")) return;
		if (line.startsWith(".inc")) return;
		if (line.startsWith(".org")) return;
		
		if (line.startsWith(".data"))
		{
			dataBuilder.processSourceCode(sc);
		}
		else
		if (line.startsWith(".res"))
		{
			dataBuilder.processSourceCode(sc);
		}
		else
		{
			String dir = line.split(" ")[0];
			addError(sc, "  Unknown directive; "+dir);
		}
	}

	private void processLine(SourceCode loc) 
	{
		String line = loc.getLine();
		
		/*
		 * is a label, store for later reference along with it's instruction address
		 */
		if (line.contains(":"))
		{
			String label = line.substring(0,line.length()-1);
			labels.put(label.toLowerCase(), instIndex); // labels are not case sensitive
			return;
		}
		
		/*
		 * build a draft from the source code
		 */
		List<Draft> draft_inst = draftBuilder.buildDraft(loc);
		if (draftBuilder.hasError())
		{
			errors.add( draftBuilder.getError() );
			return;
		}
		
		/*
		 * check all drafts, add it's machine code
		 */
		for (Draft d : draft_inst)
		{
			if (d.hasError())
			{
				errors.add( d.getError() );
				return;
			}
			
			List<Integer> c = d.getMachineCode();
			for (Integer i : c) code.add(i);
			
			// label insertion is only done with jump instructions
			if (d.hasLabel())
			{
				var p = new Pair<Draft, Integer>(d, instIndex + 1);
				labelled.add(p);
			}
			
			instIndex += c.size();
		}
	}
	
	private void insertLabels(List< Pair<Draft, Integer> > labelled, 
							  Map<String, Integer> labels,
							  List<Integer> code) 
	{
		verbose.onPrint("Labels");
		for (Pair<Draft, Integer> p : labelled)
		{
			Draft d = p.First;
			int index = p.Second;
			
			String label = d.getLabel();
			if (!labels.containsKey(label))
			{
				addError(d.getSourceCode(), "  Unknown label reference; "+label);
				return;
			}
			
			int addr = labels.get(label);
			code.set(index, addr);
			verbose.onPrint("  "+label+": 0x"+String.format("%x", addr) );
		}
	}

	private void addError(SourceCode code, String message)
	{
		errors.add( new AssembleError(code, message) );
	}
}
