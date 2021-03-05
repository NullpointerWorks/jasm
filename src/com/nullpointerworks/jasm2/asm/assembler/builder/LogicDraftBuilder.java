package com.nullpointerworks.jasm2.asm.assembler.builder;

import java.util.ArrayList;
import java.util.List;

import static com.nullpointerworks.jasm2.vm.VMInstruction.OR;
import static com.nullpointerworks.jasm2.vm.VMInstruction.AND;
import static com.nullpointerworks.jasm2.vm.VMInstruction.XOR;

import com.nullpointerworks.jasm2.asm.assembler.Draft;
import com.nullpointerworks.jasm2.asm.parser.SourceCode;
import com.nullpointerworks.jasm2.vm.VMInstruction;

class LogicDraftBuilder extends AbstractDraftBuilder
{
	private final String or_syntax = 
			"\n  or <reg>,<reg>";
	
	private final String and_syntax = 
			"\n  and <reg>,<reg>";
	
	private final String xor_syntax = 
			"\n  xor <reg>,<reg>";
	
	public LogicDraftBuilder()
	{
		
	}
	
	public boolean hasOperation(String instruct)
	{
		if (instruct.equals("or")) return true;
		if (instruct.equals("and")) return true;
		if (instruct.equals("xor")) return true;
		return false;
	}
	
	public List<Draft> buildDraft(SourceCode sc) 
	{
		List<Draft> draft = new ArrayList<Draft>();
		String[] parts = sc.getLine().split(" ");
		String instruct = parts[0].toLowerCase();
		String operands = "";
		if (parts.length > 1) operands = parts[1].toLowerCase();
		
		if (instruct.equals("or"))
		{
			buildINST(sc, draft, OR, "OR", or_syntax, operands);
		}
		else
		if (instruct.equals("and"))
		{
			buildINST(sc, draft, AND, "AND", and_syntax, operands);
		}
		else
		if (instruct.equals("xor"))
		{
			buildINST(sc, draft, XOR, "XOR", xor_syntax, operands);
		}
		
		return draft;
	}
	
	// inst <reg> , <reg>
	private void buildINST(SourceCode sc, 
							List<Draft> draft, 
							VMInstruction rr,
							String inst,
							String syntax,
							String operands) 
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) 
		{
			throwError(sc,"  Syntax error: "+inst+" instructions use two operands."+syntax);
			return;
		}

		Draft d = new Draft(sc);
		Operand op1 = new Operand(tokens[0]);
		Operand op2 = new Operand(tokens[1]);
		
		if (op1.isRegister())
		{
			if (op2.isRegister())
			{
				BuilderUtil.setCode(d, rr, 
									op1.getRegister(), 
									op2.getRegister());
				draft.add(d);
				return;
			}
		}
		
		throwError(sc, "  Syntax error: "+inst+" instructions operands not recognized."+syntax);
	}
}
