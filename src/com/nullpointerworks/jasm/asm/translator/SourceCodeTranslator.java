package com.nullpointerworks.jasm.asm.translator;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.error.TranslationError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class SourceCodeTranslator implements Translator 
{
	private List<Definition> definitions;
	private List<Allocation> allocations;
	private List<Translation> translation;
	private List<BuildError> errors;
	private VerboseListener verbose;
	
	public SourceCodeTranslator()
	{
		definitions = new ArrayList<Definition>();
		allocations = new ArrayList<Allocation>();
		translation = new ArrayList<Translation>();
		errors = new ArrayList<BuildError>();
		verbose = (str)->{};
	}
	
	@Override
	public void setVerboseListener(VerboseListener v) 
	{
		verbose = v;
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
	public List<Definition> getDefinitions()
	{
		return definitions;
	}
	
	@Override
	public List<Allocation> getAllocations()
	{
		return allocations;
	}
	
	@Override
	public List<Translation> getTranslation() 
	{
		return translation;
	}
	
	@Override
	public void translate(List<SourceCode> source) 
	{
		definitions.clear();
		allocations.clear();
		translation.clear();
		errors.clear();
		
		for (int i=0, l=source.size(); i<l; i++)
		{
			SourceCode sc = source.get(i);
			
			if (hasErrors()) return;
			String line = sc.getLine();
			
			if (line.startsWith("."))
			{
				processDirective(sc);
				continue;
			}
			
			if (line.contains(":"))
			{
				SourceCode scn = source.get(i+1);
				processLabel(sc,scn);
				i+=1;
				continue;
			}
			
			processCode(sc);
		}
	}
	
	private Translation processCode(SourceCode sc) 
	{
		String[] parts = sc.getLine().split(" ");
		String instruct = parts[0].toLowerCase();
		String operands = "";
		if (parts.length > 1) operands = parts[1].toLowerCase();
		Translation t = new Translation(sc);
		
		
		translation.add(t);
		return t;
	}
	
	private void processLabel(SourceCode sc, SourceCode next) 
	{
		String line = sc.getLine();
		String name = line.substring(0,line.length()-1);
		
		String l = name.toLowerCase();
		Translation t = processCode(next);
		t.setLabel(l);
	}

	private void processDirective(SourceCode sc) 
	{
		String line = sc.getLine();
		
		if (line.startsWith(".def"))
		{
			String[] tokens = line.split(" ");
			if (tokens.length != 3)
			{
				errors.add( new TranslationError(sc, "") ); // TODO
				return;
			}

			Operand op1 = new Operand(tokens[1]);
			if (!op1.isLabel())
			{
				errors.add( new TranslationError(sc, "") ); // TODO
				return;
			}
			
			Operand op2 = new Operand(tokens[2]);
			if (!op2.isNumber())
			{
				errors.add( new TranslationError(sc, "") ); // TODO
				return;
			}
			
			Definition d = new Definition(Directive.DEF, sc, op1.getOperand(), op2.getInteger());
			definitions.add(d);
			return;
		}
		
		if (line.startsWith(".res"))
		{
			String[] tokens = line.split(" ");
			if (tokens.length != 3)
			{
				errors.add( new TranslationError(sc, "") ); // TODO
				return;
			}
			
			Operand op1 = new Operand(tokens[1]);
			if (!op1.isLabel())
			{
				errors.add( new TranslationError(sc, "") ); // TODO
				return;
			}
			
			Operand op2 = new Operand(tokens[2]);
			if (!op2.isNumber())
			{
				errors.add( new TranslationError(sc, "") ); // TODO
				return;
			}
			
			Allocation d = new Allocation(Directive.RES, sc, op1.getOperand(), op2.getInteger());
			allocations.add(d);
		}
		
		if (line.startsWith(".data"))
		{
			//System.out.println(">> "+line);
			String[] tokens = line.split(" ");
			if (tokens.length < 3)
			{
				errors.add( new TranslationError(sc, "") ); // TODO
				return;
			}
			
			Operand op1 = new Operand(tokens[1]);
			if (!op1.isLabel())
			{
				errors.add( new TranslationError(sc, "") ); // TODO
				return;
			}
			
			
			// TODO
			
		}
	}
}
