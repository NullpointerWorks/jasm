package com.nullpointerworks.jasm.asm.assembler.builder;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.BuilderUtility;
import com.nullpointerworks.jasm.asm.ParserUtility;
import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.vm.VMInstruction;

class SystemDraftBuilder extends AbstractDraftBuilder
{
	private final String int_syntax = 
			"\n  int <val>";
	
	public SystemDraftBuilder()
	{
		
	}
	
	public boolean hasOperation(String instruct)
	{
		if (instruct.equals("nop")) return true;
		if (instruct.equals("int")) return true;
		return false;
	}
	
	public List<Draft> buildDraft(SourceCode sc) 
	{
		List<Draft> draft = new ArrayList<Draft>();
		String[] parts = sc.getLine().split(" ");
		String instruct = parts[0].toLowerCase();
		String operands = "";
		if (parts.length > 1) operands = parts[1].toLowerCase();
		
		if (instruct.equals("nop"))
		{
			buildNOP(sc, draft);
		}
		else
		if (instruct.equals("int"))
		{
			buildINT(sc, draft, operands);
		}
		
		return draft;
	}
	
	private void buildINT(SourceCode sc, List<Draft> draft, String operands) 
	{
		if (operands.contains(","))
		{
			throwError(sc, "  Syntax error: Interrupt instructions only accept one operand."+int_syntax);
			return;
		}

		VMInstruction inst = VMInstruction.INT;
		int value = 0;
		if ( ParserUtility.isInteger(operands) )
		{
			value = Integer.parseInt(operands);
		}
		else
		if ( ParserUtility.isHexadec(operands) )
		{
			String opp = operands.replace("0x", "");
			value = Integer.parseInt(opp, 16);
		}
		else
		{
			throwError(sc, "  Syntax error: Interrupts only accept immediate values."+int_syntax);
			return;
		}
		
		Draft d = new Draft(sc);
		BuilderUtility.setCodeImmidiate(d, inst, value);
		draft.add(d);
	}
}
