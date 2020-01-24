package com.nullpointerworks.jasm;

import java.util.List;

public interface Compiler
{
	Compiler setParserVerbose(boolean verbose);
	Compiler setPreprocessorVerbose(boolean verbose);
	Compiler setCompilerVerbose(boolean verbose);
	Compiler setVerifyOnly(boolean verify);
	Compiler setLogListener(LogListener logging);
	Compiler setIncludesPath(List<String> path);
	Compiler reset();
	byte[] parse(String filename, String[] lines);
}
