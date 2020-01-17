package com.nullpointerworks.jasm.jasm8;

public interface Compiler
{
	Compiler setParserVerbose(boolean verbose);
	Compiler setPreprocessorVerbose(boolean verbose);
	Compiler setCompilerVerbose(boolean verbose);
	Compiler setIncludesPath(String path);
	Compiler reset();
	byte[] parse(String[] lines);
}
