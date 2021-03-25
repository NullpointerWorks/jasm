package com.nullpointerworks.jasm.asm.translator;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.ParserUtility;
import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.error.TranslationError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.asm.translator.builder.CodeTranslator;
import com.nullpointerworks.jasm.asm.translator.builder.SuperTranslator;

public class SourceCodeTranslator implements Translator 
{
	private List<Definition> definitions;
	private List<Allocation> allocations;
	private List<Label> labels;
	private List<Translation> translation;
	
	private List<BuildError> errors;
	private VerboseListener verbose;
	private CodeTranslator translator;
	
	public SourceCodeTranslator()
	{
		definitions = new ArrayList<Definition>();
		allocations = new ArrayList<Allocation>();
		labels = new ArrayList<Label>();
		translation = new ArrayList<Translation>();
		errors = new ArrayList<BuildError>();
		verbose = (str)->{};
		translator = new SuperTranslator();
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
	public List<Label> getLabels()
	{
		return labels;
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
		labels.clear();
		translation.clear();
		errors.clear();
		
		verbose.onPrint("-------------------------------");
		verbose.onPrint("Translation Start\n");
		
		for (int i=0, l=source.size(); i<l; i++)
		{
			if (hasErrors()) return;
			
			SourceCode sc = source.get(i);
			String line = sc.getLine();
			
			if (line.startsWith("."))
			{
				processDirective(sc);
				continue;
			}
			
			if (line.contains(":")) // TODO probably causes an error when labeled multiple times in a row
			{
				SourceCode scn = source.get(i+1);
				processLabel(sc,scn);
				i+=1;
				continue;
			}
			
			processCode(sc);
		}
		
		verbose.onPrint("\nTranslation End");
		verbose.onPrint("-------------------------------");
	}
	
	private List<Translation> processCode(SourceCode sc) 
	{
		List<Translation> list = translator.getTranslation(sc);
		
		if (translator.hasErrors())
		{
			errors.add( translator.getError() );
			return null;
		}
		
		if (list == null)
		{
			errors.add( new TranslationError(sc, "  Uncaught translation exception in the code translator.") );
			return null;
		}
		
		if (list.size() == 0)
		{
			errors.add( new TranslationError(sc, "  No translation has been applied. Review the instruction documentation version.") );
			return null;
		}
		
		for (Translation tr : list) translation.add(tr);
		return list;
	}
	
	private void processLabel(SourceCode sc, SourceCode next) 
	{
		String line = sc.getLine();
		String name = line.substring(0,line.length()-1);
		
		if (!ParserUtility.isValidLabel(name))
		{
			errors.add( new TranslationError(sc, "  Invalid label characters used.") );
			return;
		}
		
		String l = name.toLowerCase();
		List<Translation> t = processCode(next);
		if (hasErrors()) return;
		
		t.get(0).setLabel(l);
		labels.add( new Label(sc, l));
	}

	private void processDirective(SourceCode sc) 
	{
		String line = sc.getLine();
		
		if (line.startsWith(".def"))
		{
			String[] tokens = line.split(" ");
			if (tokens.length != 3)
			{
				errors.add( new TranslationError(sc, "  Syntax error: definitions take two arguments.") );
				return;
			}
			
			Operand op1 = new Operand(tokens[1]);
			if (!op1.isLabel())
			{
				errors.add( new TranslationError(sc, "") ); // TODO
				return;
			}
			
			Operand op2 = new Operand(tokens[2]);
			if (!op2.isNumber() || op2.isAddress())
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
			if (!op2.isNumber() || op2.isAddress())
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
			
			// TODO add text handling
			
			int[] values = {0};
			if (tokens.length == 3)
			{
				values = getValues(sc, tokens[2]);
			}
			else
			{
				errors.add( new TranslationError(sc, "") ); // TODO
			}
			if (hasErrors()) return;
			
			Allocation d = new Allocation(Directive.DATA, sc, op1.getOperand(), values);
			allocations.add(d);
		}
	}

	private int[] getValues(SourceCode sc, String string) 
	{
		String[] tokens = string.split(",");
		int[] values = new int[tokens.length];
		
		for (int i=0, l=tokens.length; i<l; i++)
		{
			String t = tokens[i];
			//System.out.println(">> "+t);
			if (ParserUtility.isValidNumber(t))
			{
				values[i] = ParserUtility.getIntegerValue(t);
			}
			else
			{
				errors.add( new TranslationError(sc, "") ); // TODO
			}
		}
		return values;
	}

	private String combine(String[] tokens, int start) 
	{
		String res = "";
		for (int i=start, l=tokens.length; i<l; i++)
		{
			res += (","+tokens[i]);
		}
		return res.substring(1);
	}
}
