package com.nullpointerworks.jasm2.asm.assembler.builder;

import java.util.ArrayList;
import java.util.List;

import static com.nullpointerworks.jasm2.vm.VMInstruction.ADD_RR;
import static com.nullpointerworks.jasm2.vm.VMInstruction.ADD_RV;
import static com.nullpointerworks.jasm2.vm.VMInstruction.SUB_RR;
import static com.nullpointerworks.jasm2.vm.VMInstruction.SUB_RV;
import static com.nullpointerworks.jasm2.vm.VMInstruction.CMP_RR;
import static com.nullpointerworks.jasm2.vm.VMInstruction.CMP_RV;
import static com.nullpointerworks.jasm2.vm.VMInstruction.INC;
import static com.nullpointerworks.jasm2.vm.VMInstruction.DEC;
import static com.nullpointerworks.jasm2.vm.VMInstruction.NEG;
import static com.nullpointerworks.jasm2.vm.VMInstruction.SHL;
import static com.nullpointerworks.jasm2.vm.VMInstruction.SHR;

import com.nullpointerworks.jasm2.asm.assembler.Draft;
import com.nullpointerworks.jasm2.asm.parser.SourceCode;
import com.nullpointerworks.jasm2.vm.VMInstruction;

class ArithmeticDraftBuilder extends AbstractDraftBuilder
{
	private final String add_syntax = 
			"\n  add <reg>,<reg>" + 
			"\n  add <reg>,<val>";

	private final String sub_syntax = 
			"\n  sub <reg>,<reg>" + 
			"\n  sub <reg>,<val>";

	private final String cmp_syntax = 
			"\n  cmp <reg>,<reg>" + 
			"\n  cmp <reg>,<val>";
	
	private final String inc_syntax = 
			"\n  inc <reg>";
	
	private final String dec_syntax = 
			"\n  dec <reg>";
	
	private final String neg_syntax = 
			"\n  neg <reg>";
	
	private final String shl_syntax = 
			"\n  shl <reg>";
	
	private final String shr_syntax = 
			"\n  shr <reg>";
	
	public ArithmeticDraftBuilder()
	{
		
	}
	
	public boolean hasOperation(String instruct)
	{
		if (instruct.equals("add")) return true;
		if (instruct.equals("sub")) return true;
		if (instruct.equals("cmp")) return true;
		if (instruct.equals("inc")) return true;
		if (instruct.equals("dec")) return true;
		if (instruct.equals("neg")) return true;
		if (instruct.equals("shl")) return true;
		if (instruct.equals("shr")) return true;
		return false;
	}
	
	public List<Draft> buildDraft(SourceCode sc) 
	{
		List<Draft> draft = new ArrayList<Draft>();
		String[] parts = sc.getLine().split(" ");
		String instruct = parts[0].toLowerCase();
		String operands = "";
		if (parts.length > 1) operands = parts[1].toLowerCase();
		
		if (instruct.equals("add"))
		{
			buildINST(sc, draft, ADD_RR, ADD_RV, "ADD", add_syntax, operands);
		}
		else
		if (instruct.equals("sub"))
		{
			buildINST(sc, draft, SUB_RR, SUB_RV, "SUB", sub_syntax, operands);
		}
		else
		if (instruct.equals("cmp"))
		{
			buildINST(sc, draft, CMP_RR, CMP_RV, "CMP", cmp_syntax, operands);
		}
		else
		if (instruct.equals("inc"))
		{
			buildINST(sc, draft, INC, "INC", inc_syntax, operands);
		}
		else
		if (instruct.equals("dec"))
		{
			buildINST(sc, draft, DEC, "DEC", dec_syntax, operands);
		}
		else
		if (instruct.equals("neg"))
		{
			buildINST(sc, draft, NEG, "NEG", neg_syntax, operands);
		}
		else
		if (instruct.equals("shl"))
		{
			buildINST(sc, draft, SHL, "SHL", shl_syntax, operands);
		}
		else
		if (instruct.equals("shr"))
		{
			buildINST(sc, draft, SHR, "SHR", shr_syntax, operands);
		}
		
		return draft;
	}
	
	// inst <reg>
	private void buildINST(SourceCode sc, 
							List<Draft> draft, 
							VMInstruction r, 
							String inst,
							String syntax,
							String operands) 
	{
		if (operands.contains(",")) 
		{
			throwError(sc,"  Syntax error: "+inst+" instructions use two operands."+syntax);
			return;
		}
		
		Draft d = new Draft(sc);
		Operand op = new Operand(operands);
		
		if (op.isRegister())
		{
			BuilderUtil.setCode(d, r, op.getRegister());
			draft.add(d);
			return;
		}

		throwError(sc, "  Syntax error: "+inst+" instructions operands not recognized."+syntax);
	}
	
	// inst <reg> , <reg>
	// inst <reg> , <val>
	private void buildINST(SourceCode sc, 
							List<Draft> draft, 
							VMInstruction rr, 
							VMInstruction rv,
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
			else
			if (op2.isInteger())
			{
				BuilderUtil.setCode(d, rv, 
									op1.getRegister(), 
									op2.getInteger());
				draft.add(d);
				return;
			}
		}
		
		throwError(sc, "  Syntax error: "+inst+" instructions operands not recognized."+syntax);
	}
}
