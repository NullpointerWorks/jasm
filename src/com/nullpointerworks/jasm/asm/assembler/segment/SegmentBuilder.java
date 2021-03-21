package com.nullpointerworks.jasm.asm.assembler.segment;

import java.util.List;

import com.nullpointerworks.jasm.asm.VerboseListener;
import com.nullpointerworks.jasm.asm.error.BuildError;
import com.nullpointerworks.jasm.asm.parser.SourceCode;

public interface SegmentBuilder 
{
	void setVerboseListener(VerboseListener v);
	boolean hasError();
	BuildError getError();
	void addSourceCode(SourceCode sc);
	void setOffset(int offset);
	List<Number> getByteCode();
}
