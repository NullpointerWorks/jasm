package com.nullpointerworks.jasm.asm.assembler.builder.ctrlflow;

import java.util.List;

import com.nullpointerworks.jasm.asm.ASMInstruction;
import com.nullpointerworks.jasm.asm.TranslatorUtility;
import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.assembler.builder.Drafter;
import com.nullpointerworks.jasm.asm.error.AssembleError;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.translator.Allocation;
import com.nullpointerworks.jasm.asm.translator.Definition;
import com.nullpointerworks.jasm.asm.translator.Label;
import com.nullpointerworks.jasm.asm.translator.Operand;
import com.nullpointerworks.jasm.asm.translator.Translation;

public class JumpDrafter implements Drafter
{
	private BuildError error;
	
	public JumpDrafter() 
	{
		error = null;
	}

	@Override
	public boolean hasError() 
	{
		return error != null;
	}

	@Override
	public BuildError getError() 
	{
		return error;
	}
	
	public boolean isInstruction(ASMInstruction instruct)
	{
		return instruct == ASMInstruction.JMP;
	}
	
	public Draft draft(	Translation translation, 
						List<Definition> defs, 
						List<Allocation> allocs,
						List<Label> lbls)
	{
		error = null;
		Draft d = new Draft();
		build(translation, defs, allocs, lbls, d);
		return d;
	}
	
	private void build(	Translation tr, 
						List<Definition> defs, 
						List<Allocation> allocs,
						List<Label> lbls, 
						Draft d) 
	{
		Operand op = tr.getOperand(0);
		if (op.isLabel())
		{
			String name = op.getOperand();
			Label lbl = TranslatorUtility.findLabel(lbls, name);
			if (lbl!=null)
			{
				d.addValue( 0x20 << 24 );
				d.addValue( lbl.getNumber() );
			}
			else
			{
				error = new AssembleError(tr.getSourceCode(), "  Label \""+name+"\" cannot be found.");
			}
		}
	}
}
