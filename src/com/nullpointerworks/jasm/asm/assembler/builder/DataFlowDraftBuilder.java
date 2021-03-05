package com.nullpointerworks.jasm.asm.assembler.builder;

import static com.nullpointerworks.jasm.vm.VMInstruction.LOAD_MR;
import static com.nullpointerworks.jasm.vm.VMInstruction.LOAD_RM;
import static com.nullpointerworks.jasm.vm.VMInstruction.LOAD_RR;
import static com.nullpointerworks.jasm.vm.VMInstruction.LOAD_RV;
import static com.nullpointerworks.jasm.vm.VMInstruction.PUSH_R;
import static com.nullpointerworks.jasm.vm.VMInstruction.PUSH_V;
import static com.nullpointerworks.jasm.vm.VMInstruction.POP;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

class DataFlowDraftBuilder extends AbstractDraftBuilder
{
	private final String load_syntax = 
			"\n  load <reg>,<reg>" + 
			"\n  load <reg>,<val>" + 
			"\n  load <reg>,<mem>" + 
			"\n  load <mem>,<reg>";

	private final String push_syntax = 
			"\n  push <reg>";

	private final String pop_syntax = 
			"\n  pop <reg>";
	
	public DataFlowDraftBuilder()
	{
		
	}
	
	public boolean hasOperation(String instruct)
	{
		if (instruct.equals("load")) return true;
		if (instruct.equals("push")) return true;
		if (instruct.equals("pop")) return true;
		return false;
	}
	
	public List<Draft> buildDraft(SourceCode sc) 
	{
		List<Draft> draft = new ArrayList<Draft>();
		String[] parts = sc.getLine().split(" ");
		String instruct = parts[0].toLowerCase();
		String operands = "";
		if (parts.length > 1) operands = parts[1].toLowerCase();
		
		if (instruct.equals("load"))
		{
			buildLOAD(sc, draft, operands);
		}
		else
		if (instruct.equals("push"))
		{
			buildPUSH(sc, draft, operands);
		}
		else
		if (instruct.equals("pop"))
		{
			buildPOP(sc, draft, operands);
		}
		
		return draft;
	}
	
	// load <reg> , <reg>
	// load <reg> , <val>
	// load <reg> , <mem>
	// load <mem> , <reg>
	private void buildLOAD(SourceCode sc, List<Draft> draft, String operands) 
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) 
		{
			throwError(sc,"  Syntax error: LOAD instructions use two operands."+load_syntax);
			return;
		}

		Draft d = new Draft(sc);
		Operand op1 = new Operand(tokens[0]);
		Operand op2 = new Operand(tokens[1]);
		
		if (op1.isRegister())
		{
			if (op2.isRegister())
			{
				BuilderUtil.setCode(d, LOAD_RR, 
									op1.getRegister(), 
									op2.getRegister());
				draft.add(d);
				return;
			}
			else
			if (op2.isAddress())
			{
				BuilderUtil.setCode(d, LOAD_RM, 
									op1.getRegister(), 
									op2.getInteger());
				draft.add(d);
				return;
			}
			else
			if (op2.isInteger())
			{
				BuilderUtil.setCode(d, LOAD_RV, 
									op1.getRegister(), 
									op2.getInteger());
				draft.add(d);
				return;
			}
		}
		else
		if (op1.isAddress())
		{
			if (op2.isRegister())
			{
				BuilderUtil.setCode(d, LOAD_MR, op2.getRegister(), op1.getInteger() );
				draft.add(d);
				return;
			}
		}
		
		throwError(sc, "  Syntax error: LOAD instructions operands not recognized."+load_syntax);
	}

	// push <reg>
	// push <val>
	private void buildPUSH(SourceCode sc, List<Draft> draft, String operands) 
	{
		if (operands.contains(",")) 
		{
			throwError(sc,"  Syntax error: PUSH instructions use two operands."+push_syntax);
			return;
		}
		
		Draft d = new Draft(sc);
		Operand op = new Operand(operands);
		
		if (op.isRegister())
		{
			BuilderUtil.setCode(d, PUSH_R, op.getRegister());
			draft.add(d);
			return;
		}
		else
		if (op.isInteger())
		{
			BuilderUtil.setCode(d, PUSH_V, op.getInteger() );
			draft.add(d);
			return;
		}
		
		throwError(sc, "  Syntax error: PUSH instructions operands not recognized."+push_syntax);
	}
	
	// pop <reg>
	private void buildPOP(SourceCode sc, List<Draft> draft, String operands) 
	{
		if (operands.contains(",")) 
		{
			throwError(sc,"  Syntax error: POP instructions use two operands."+pop_syntax);
			return;
		}
		
		Draft d = new Draft(sc);
		Operand op = new Operand(operands);
		
		if (op.isRegister())
		{
			BuilderUtil.setCode(d, POP, op.getRegister());
			draft.add(d);
			return;
		}

		throwError(sc, "  Syntax error: POP instructions operands not recognized."+pop_syntax);
	}
}
