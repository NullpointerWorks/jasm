package com.nullpointerworks.jasm.util;

import com.nullpointerworks.jasm.jasm8.compiler.SourceCode;
import com.nullpointerworks.util.pack.Tuple;

/*
 * tuple delegate
 */
public class EquRecord extends Tuple<String, String, SourceCode> 
{
	public EquRecord(String a, String b, SourceCode c) {super(a, b, c);}
}
