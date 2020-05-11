package com.nullpointerworks.jasm.processor;

public interface Processor {

	void resetFlags();

	void setFlag(Select zero, boolean b);

	Register getRegister(Select sa);

	void setSelection(Select sa, int res);

}
