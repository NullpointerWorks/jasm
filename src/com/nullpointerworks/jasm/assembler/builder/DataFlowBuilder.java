package com.nullpointerworks.jasm.assembler.builder;

import com.nullpointerworks.jasm.assembler.AssemblerConstants;
import com.nullpointerworks.jasm.assembler.Draft;
import com.nullpointerworks.jasm.assembler.Operand;
import com.nullpointerworks.jasm.assembler.Operation;
import com.nullpointerworks.jasm.assembler.SourceCode;

/*
	
	
	load [x+1],a
	
	push x
	add x,1
	load &x,a
	pop x
	
	
	
	
	load a,[x+1]
	
	push x
	add x,1
	load a,&x
	pop x


*/

public class DataFlowBuilder extends AbstractDraftBuilder
{
	private SourceCode source;
	
	public DataFlowBuilder() {}
	
	public boolean isDataFlow(String instruct) 
	{
		if (instruct.equals("load")) return true;
		if (instruct.equals("push")) return true;
		if (instruct.equals("pop")) return true;
		return false;
	}
	
	public Draft[] getDraft(SourceCode loc)
	{
		setError(null);
		source = loc;
		String[] parts = loc.getLine().split(" ");
		String instruct = parts[0].toLowerCase();
		String operands = "";
		if (parts.length > 1) operands = parts[1].toLowerCase();
		
		
		
		
		
		return new Draft[] {getDraft(instruct, operands)};
	}
	
	// ================================================================================

	private Draft[] getBlockDraft(String instruct, String operands)
	{
		
		if (instruct.equals("load"))
		{
			
			
			
			
		}
		
		throwError("  Unrecognized instruction or parameters");
		return new Draft[] {null};
	}
	
	private Draft getDraft(String instruct, String operands)
	{
		/*
		 * data flow
		 */
		if (instruct.equals("load")) {return _load(operands);}
		if (instruct.equals("push")) {return _push(operands);}
		if (instruct.equals("pop")) {return _pop(operands);}
		
		throwError("  Unrecognized instruction or parameters");
		return null;
	}
	
	/* ================================================================================
	 * 
	 * 		data transfer
	 * 
	 * ============================================================================= */
	
	/*
	 * load <reg>,<reg>
	 * load <reg>,<imm>
	 */
	private final String load_syntax = 
						"\n  load <reg>,<reg>" + 
						"\n  load <reg>,<imm>";
	
	private Draft _load(String operands)
	{
		String[] tokens = operands.split(",");
		if (tokens.length != 2) 
		{
			throwError("  Syntax error: "+Operation.LOAD+" instructions use two operands."+load_syntax);
			return null; // error
		}
		
		Operand op1 = new Operand(tokens[0]);
		Operand op2 = new Operand(tokens[1]);
		boolean memory = op1.isAddress() || op2.isAddress();
		
		// if no memory manipulation is used, draft a "normal" load instruction
		if (!memory)
		{
			return _generic_instruction(source, Operation.LOAD, operands, "Load", load_syntax);
		}
		
		if (op1.isAddress() && !op2.isAddress())
		{
			return _load_sto(op1, op2);
		}
		
		if (op2.isAddress() && !op1.isAddress())
		{
			return _load_read(op1, op2);
		}
		
		throwError("  Syntax error: "+Operation.LOAD+" instructions operands not recognized."+load_syntax);
		return null; // error
	}
	
	/*
	 * load &<reg>,<reg>
	 * load &<imm>,<reg>
	 */
	private final String sto_syntax = "\n  load "+AssemblerConstants.ADDRESS+"<reg>,<reg>"
									+ "\n  load "+AssemblerConstants.ADDRESS+"<imm>,<reg>";
	private Draft _load_sto(Operand op1, Operand op2)
	{
		Draft d = new Draft(source, Operation.LOAD);
		
		/*
		 * check source
		 */
		if (!op2.isRegister())
		{
			throwError("  Syntax error: Store source operand must be a register."+sto_syntax);
			return null;
		}
		
		/*
		 * check target location. must be an address
		 */
		if (!op1.isAddress())
		{
			throwError("  Syntax error: Store target operand must be an address."+sto_syntax);
			return null;
		}
		
		if (!op1.hasError())
		{
			d.addOperand(op1);
			d.addOperand(op2);
		}
		else
		{
			throwError("  Syntax error: Store target operand must be either a register or an immediate value."+sto_syntax);
		}
		
		return d;
	}
	
	/*
	 * load <reg>,&<reg>
	 * load <reg>,&<imm>
	 */
	private final String read_syntax = "\n  load <reg>,"+AssemblerConstants.ADDRESS+"<reg>"
									+ "\n  load <reg>,"+AssemblerConstants.ADDRESS+"<imm>";
	private Draft _load_read(Operand op1, Operand op2)
	{
		Draft d = new Draft(source, Operation.LOAD);
		
		/*
		 * check target
		 */
		if (!op1.isRegister())
		{
			throwError("  Syntax error: Read target operand must be a register."+read_syntax);
			return null;
		}
		d.addOperand(op1);
		
		/*
		 * check source location. must be an address
		 */
		if (!op2.isAddress())
		{
			throwError("  Syntax error: Read source operand must be an address."+read_syntax);
			return null; // error
		}
		
		if (!op2.hasError())
		{
			d.addOperand(op2);
		}
		else
		{
			throwError("  Syntax error: Read source operand must be either a register or an immediate value."+read_syntax);
		}
		return d;
	}
	
	/*
	 * push <reg>
	 * push <imm>
	 */
	private final String push_syntax = "\n  push <reg>\n  push <imm>";
	private Draft _push(String operands)
	{
		if (operands.contains(","))
		{
			throwError("  Syntax error: Stack instructions accept only one operand."+push_syntax);
			return null;
		}
		
		Draft d = new Draft(source, Operation.PUSH);
		Operand op1 = new Operand(operands);
		if (!op1.hasError())
		{
			d.addOperand(op1);
		}
		else
		{
			throwError("  Syntax error: Stack target operand must be a register."+push_syntax);
		}
		
		return d;
	}
	
	/*
	 * pop <reg>
	 */
	private final String pop_syntax = "\n  pop <reg>";
	private Draft _pop(String operands)
	{
		return _generic_register(source, Operation.POP, operands, "Pop", pop_syntax);
	}
	
	/*
	 * ===========================================================
	 */
	
	private void throwError(String str) 
	{
		super.setError(source, str);
	}
}
