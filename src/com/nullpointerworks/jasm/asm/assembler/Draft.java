package com.nullpointerworks.jasm.asm.assembler;

import java.util.ArrayList;
import java.util.List;

import com.nullpointerworks.jasm.asm.translator.Number;
import com.nullpointerworks.jasm.asm.translator.Translation;

public class Draft 
{
	private Translation tr;
	private List<Number> numbers;
	
	public Draft(Translation tr) 
	{
		this.tr=tr;
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
