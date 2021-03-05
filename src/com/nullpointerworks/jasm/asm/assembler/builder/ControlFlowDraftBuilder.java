package com.nullpointerworks.jasm.asm.assembler.builder;

import static com.nullpointerworks.jasm.vm.VMInstruction.CALL;
import static com.nullpointerworks.jasm.vm.VMInstruction.JE;
import static com.nullpointerworks.jasm.vm.VMInstruction.JG;
import static com.nullpointerworks.jasm.vm.VMInstruction.JGE;
import static com.nullpointerworks.jasm.vm.VMInstruction.JL;
import static com.nullpointerworks.jasm.vm.VMInstruction.JLE;
import static com.nullpointerworks.jasm.vm.VMInstruction.JMP;
import static com.nullpointerworks.jasm.vm.VMInstruction.JNE;
import static com.nullpointerworks.jasm.vm.VMInstruction.RET;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.parser.SourceCode;
import com.nullpointerworks.jasm.vm.VMInstruction;

class ControlFlowDraftBuilder extends AbstractDraftBuilder
{
	private final String jmp_syntax = "\n  jmp <label>";
	
	public ControlFlowDraftBuilder()
	{
		
	}
	
	public boolean hasOperation(String instruct)
	{
		if (instruct.equals("jmp")) return true;
		if (instruct.equals("call")) return true;
		if (instruct.equals("ret")) return true;
		if (instruct.equals("je")) return true;
		if (instruct.equals("jne")) return true;
		if (instruct.equals("jl")) return true;
		if (instruct.equals("jle")) return true;
		if (instruct.equals("jg")) return true;
		if (instruct.equals("jge")) return true;
		return false;
	}
	
	public List<Draft> buildDraft(SourceCode sc) 
	{
		List<Draft> draft = new ArrayList<Draft>();
		String[] parts = sc.getLine().split(" ");
		String instruct = parts[0].toLowerCase();
		String operands = "";
		if (parts.length > 1) operands = parts[1].toLowerCase(); // labels are not case sensitive
		
		if (instruct.equals("call"))
		{
			buildJMP(sc, draft, CALL, jmp_syntax, operands);
		}
		else
		if (instruct.equals("ret"))
		{
			buildRET(sc, draft);
		}
		else
		if (instruct.equals("jmp"))
		{
			buildJMP(sc, draft, JMP, jmp_syntax, operands);
		}
		else
		if (instruct.equals("je"))
		{
			buildJMP(sc, draft, JE, jmp_syntax, operands);
		}
		else
		if (instruct.equals("jne"))
		{
			buildJMP(sc, draft, JNE, jmp_syntax, operands);
		}
		else
		if (instruct.equals("jl"))
		{
			buildJMP(sc, draft, JL, jmp_syntax, operands);
		}
		else
		if (instruct.equals("jle"))
		{
			buildJMP(sc, draft, JLE, jmp_syntax, operands);
		}
		else
		if (instruct.equals("gl"))
		{
			buildJMP(sc, draft, JG, jmp_syntax, operands);
		}
		else
		if (instruct.equals("gle"))
		{
			buildJMP(sc, draft, JGE, jmp_syntax, operands);
		}
		
		return draft;
	}
	
	// jmp <label>
	private void buildJMP(SourceCode sc, List<Draft> draft, VMInstruction inst, String syntax, String operands) 
	{
		if (operands.contains(",")) 
		{
			throwError(sc,"  Syntax error: "+inst.toString()+" instructions use one operand."+syntax);
			return;
		}
		
		Draft d = new Draft(sc);
		Operand op1 = new Operand(operands);
		
		// if the operand is not a number or register, it has to be a label
		if (!op1.isRegister())
		if (!op1.isInteger())
		{
			BuilderUtil.setCode(d, inst, 0);
			d.setLabel(operands);
			draft.add(d);
			return;
		}
		
		throwError(sc, "  Syntax error: "+inst.toString()+" instruction operand not recognized."+syntax);
	}
	
	private void buildRET(SourceCode sc, List<Draft> draft) 
	{
		Draft d = new Draft(sc);
		BuilderUtil.setCode(d, RET);
		draft.add(d);
	}
}
