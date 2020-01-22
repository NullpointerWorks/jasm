package com.nullpointerworks.jasm;

public interface Compiler
{
	Compiler setParserVerbose(boolean verbose);
	Compiler setPreprocessorVerbose(boolean verbose);
	Compiler setCompilerVerbose(boolean verbose);
	Compiler setLogListener(LogListener logging);
	Compiler setIncludesPath(String path);
	Compiler reset();
	byte[] parse(String filename, String[] lines);
}
