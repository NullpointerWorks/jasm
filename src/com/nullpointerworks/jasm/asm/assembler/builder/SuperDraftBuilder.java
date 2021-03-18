package com.nullpointerworks.jasm.asm.assembler.builder;

import java.util.List;

import com.nullpointerworks.jasm.asm.assembler.Draft;
import com.nullpointerworks.jasm.asm.error.AssembleError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class SuperDraftBuilder extends AbstractDraftBuilder
{
	private SystemDraftBuilder sys;
	private DataFlowDraftBuilder data;
	private ControlFlowDraftBuilder ctrl;
	private ArithmeticDraftBuilder math;
	private LogicDraftBuilder logic;
	
	public SuperDraftBuilder()
	{
		sys = new SystemDraftBuilder();
		data = new DataFlowDraftBuilder();
		ctrl = new ControlFlowDraftBuilder();
		math = new ArithmeticDraftBuilder();
		logic = new LogicDraftBuilder();
	}
	
	public List<Draft> buildDraft(SourceCode sc) 
	{
		List<Draft> draft = null;
		String[] parts = sc.getLine().split(" ");
		String instruct = parts[0].toLowerCase();
		
		if (sys.hasOperation(instruct))
		{
			draft = sys.buildDraft(sc);
			setError( sys.getError() );
		}
		else
		if (data.hasOperation(instruct))
		{
			draft = data.buildDraft(sc);
			setError( data.getError() );
		}
		else
		if (ctrl.hasOperation(instruct))
		{
			draft = ctrl.buildDraft(sc);
			setError( ctrl.getError() );
		}
		else
		if (math.hasOperation(instruct))
		{
			draft = math.buildDraft(sc);
			setError( math.getError() );
		}
		else
		if (logic.hasOperation(instruct))
		{
			draft = logic.buildDraft(sc);
			setError( logic.getError() );
		}
		else
		{
			setError( new AssembleError(sc, "  Instruction not recognized") );
		}
		
		return draft;
	}
	
	@Override
	public boolean hasOperation(String instruct)
	{
		return true;
	}
}
