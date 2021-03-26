package com.nullpointerworks.jasm.asm.assembler.builder;

import java.util.ArrayList;
import java.util.List;

import static com.nullpointerworks.jasm.asm.ASMInstruction.CALL;
import static com.nullpointerworks.jasm.asm.ASMInstruction.JE;
import static com.nullpointerworks.jasm.asm.ASMInstruction.JNE;
import static com.nullpointerworks.jasm.asm.ASMInstruction.JL;
import static com.nullpointerworks.jasm.asm.ASMInstruction.JLE;
import static com.nullpointerworks.jasm.asm.ASMInstruction.JG;
import static com.nullpointerworks.jasm.asm.ASMInstruction.JGE;

import com.nullpointerworks.jasm.asm.ASMInstruction;
import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.assembler.builder.arith.*;
import com.nullpointerworks.jasm.asm.assembler.builder.ctrlflow.*;
import com.nullpointerworks.jasm.asm.assembler.builder.dataflow.*;
import com.nullpointerworks.jasm.asm.assembler.builder.sys.*;
import com.nullpointerworks.jasm.asm.error.AssembleError;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.translator.Allocation;
import com.nullpointerworks.jasm.asm.translator.Definition;
import com.nullpointerworks.jasm.asm.translator.Label;
import com.nullpointerworks.jasm.asm.translator.Translation;
import com.nullpointerworks.jasm.vm.VMInstruction;

public class SuperDrafter implements Drafter
{
	private BuildError error;
	private List<Drafter> drafters;
	
	public SuperDrafter()
	{
		drafters = new ArrayList<Drafter>();
		
		drafters.add( new NopDrafter() );
		drafters.add( new IntDrafter() );
		
		drafters.add( new JumpDrafter() );
		drafters.add( new ReturnDrafter() );
		drafters.add( new JumpDrafter(CALL, VMInstruction.CALL) );
		drafters.add( new JumpDrafter(JE, VMInstruction.JE) );
		drafters.add( new JumpDrafter(JNE, VMInstruction.JNE) );
		drafters.add( new JumpDrafter(JL, VMInstruction.JL) );
		drafters.add( new JumpDrafter(JLE, VMInstruction.JLE) );
		drafters.add( new JumpDrafter(JG, VMInstruction.JG) );
		drafters.add( new JumpDrafter(JGE, VMInstruction.JGE) );
		
		drafters.add( new LoadDrafter() );
		
		drafters.add( new AddDrafter() );
		drafters.add( new SubDrafter() );
		drafters.add( new CmpDrafter() );
		drafters.add( new IncDrafter() );
		drafters.add( new DecDrafter() );
		drafters.add( new NegDrafter() );
		drafters.add( new ShlDrafter() );
		drafters.add( new ShrDrafter() );
		
		
		
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
	
	@Override
	public boolean isInstruction(ASMInstruction instruct) 
	{
		for (Drafter dr : drafters)
		{
			if (dr.isInstruction(instruct)) return true;
		}
		return false;
	}
	
	@Override
	public Draft draft(	Translation tr, 
						List<Definition> defs, 
						List<Allocation> allocs, 
						List<Label> lbls) 
	{
		error = null;
		ASMInstruction inst = tr.getInstruction();
		
		if (!isInstruction(inst))
		{
			error = new AssembleError(tr.getSourceCode(), "  Failed to find a drafter for this instruction.");
			return null;
		}
		
		for (Drafter dr : drafters)
		{
			if (dr.isInstruction(inst))
			{
				Draft d = dr.draft(tr, defs, allocs, lbls);
				if (dr.hasError())
				{
					error = dr.getError();
				}
				return d;
			}
		}
		error = new AssembleError(tr.getSourceCode(), "  Internal error trying to draft translation.");
		return null;
	}
}
