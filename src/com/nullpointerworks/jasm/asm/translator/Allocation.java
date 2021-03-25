package com.nullpointerworks.jasm.asm.translator;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.parser.SourceCode;

public class Allocation 
{
	private SourceCode source;
	private Directive type = Directive.NULL;
	
	private String name;
	private Number address;
	private List<Number> number;
	
	private void init(Directive t, SourceCode sc, String n) 
	{
		type = t;
		source = sc;
		name = n;
		address = new Number(0);
		number = new ArrayList<Number>();
	}
	
	private void add(int i)
	{
		number.add( new Number(i) );
	}
	
	public Allocation(Directive t, SourceCode sc, String n, int[] v) 
	{
		init(t,sc,n);
		for (int i : v) add(i);
	}
	
	public Allocation(Directive t, SourceCode sc, String n, int v) 
	{
		init(t,sc,n);
		for (; v>0; v--) add(0);
	}
	
	public SourceCode getSourceCode()
	{
		return source;
	}
	
	public Directive getDirective()
	{
		return type;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Number getAddress()
	{
		return address;
	}
	
	public List<Number> getNumbers()
	{
		return number;
	}
}
