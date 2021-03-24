package com.nullpointerworks.jasm.asm.assembler;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.translator.Number;

public class Draft 
{
	private List<Number> numbers;
	
	public Draft() 
	{
		numbers = new ArrayList<Number>();
	}

	public void addValue(int number) 
	{
		addValue( new Number(number) );
	}

	public void addValue(Number number) 
	{
		numbers.add(number);
	}

	public List<Number> getValues() 
	{
		return numbers;
	}
}
