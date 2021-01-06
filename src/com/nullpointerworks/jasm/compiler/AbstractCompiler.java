package com.nullpointerworks.jasm.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nullpointerworks.jasm.compiler.errors.BuildError;
import com.nullpointerworks.jasm.compiler.errors.CompilerError;
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
	private void process(List<SourceCode> code) 
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
		for (Draft<T> d : labelled)
		{
			String label = d.getLabel();
			if (!labels.containsKey(label))
			{
				addPreProcError(d.getLineOfCode(), "  Unknown label reference");
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
		
		instIndex += 1;
		var draft_inst = compile(instIndex, loc);
		if (draft_inst == null)
		{
			addCompilerError(loc, "  Unrecognized instruction or parameters");
			return; // compile error
		}
		
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
	
	/*
	 * ==================================================================
	 * 
	 * compiler
	 * 
	 * ==================================================================
	 */
	
	@Override
	public Draft<T> compile(int index, SourceCode loc) 
	{
		String[] parts = loc.getLine().split(" ");
		String instruct = parts[0].toLowerCase();
		String operands = "";
		if (parts.length > 1) operands = parts[1].toLowerCase();
		
		Operation op = findOperation(loc, instruct);
		if (op==null) return null;
		return draft(loc, op, operands);
	}
	
	@Override
	public abstract Draft<T> draft(SourceCode loc, Operation op, String operands);
	
	private Operation findOperation(SourceCode loc, String instruct) 
	{
		/*
		 * system
		 */
		if (instruct.equals("nop")) return Operation.NOP;
		if (instruct.equals("int")) return Operation.INT;
		
		/*
		 * arithmetic
		 */
		if (instruct.equals("add")) return Operation.ADD;
		if (instruct.equals("sub")) return Operation.SUB;
		if (instruct.equals("cmp")) return Operation.CMP;
		if (instruct.equals("shl")) return Operation.SHL;
		if (instruct.equals("shr")) return Operation.SHR;
		if (instruct.equals("inc")) return Operation.INC;
		if (instruct.equals("dec")) return Operation.DEC;
		if (instruct.equals("neg")) return Operation.NEG;
		
		/*
		 * data transfer
		 */
		if (instruct.equals("load")) return Operation.LOAD;
		if (instruct.equals("push")) return Operation.PUSH;
		if (instruct.equals("pop")) return Operation.POP;
		if (instruct.equals("sto")) return Operation.STO;
		if (instruct.equals("read")) return Operation.READ;
		
		/*
		 * control flow
		 */
		if (instruct.equals("call")) return Operation.CALL;
		if (instruct.equals("jmp")) return Operation.JMP;
		if (instruct.equals("je")) return Operation.JE;
		if (instruct.equals("jne")) return Operation.JNE;
		if (instruct.equals("jl")) return Operation.JL;
		if (instruct.equals("jle")) return Operation.JLE;
		if (instruct.equals("jg")) return Operation.JG;
		if (instruct.equals("jge")) return Operation.JGE;
		if (instruct.equals("ret")) return Operation.RET;
		
		return null;
	}

	/*
	 * ==================================================================
	 */
	protected void addPreProcError(SourceCode code, String message)
	{
		errors.add( new PreProcessorError(code, message) );
	}
	
	protected void addCompilerError(SourceCode code, String message)
	{
		errors.add( new CompilerError(code, message) );
	}
	
	protected void out(String string)
	{
		if (verbose) System.out.println(string);
	}
}
