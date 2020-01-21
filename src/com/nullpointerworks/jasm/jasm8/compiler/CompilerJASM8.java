package com.nullpointerworks.jasm.jasm8.compiler;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nullpointerworks.jasm.jasm8.Compiler;
import com.nullpointerworks.jasm.jasm8.LogListener;
import com.nullpointerworks.util.StringUtil;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

public class CompilerJASM8 implements Compiler
{
	public static final String version = "v1.5 alpha";
	
	private boolean verbose_parser = false;
	private boolean verbose_preproc = false;
	private boolean verbose_compiler = false;
	private String includePath = "";
	private LogListener log;
	
	/*
	 * text to machine language utility
	 */
	private Map<String, Integer> labels = null;
	private List<String> unused = null;
	private Map<String, String> equals = null;
	private List<CodeLine> code = null;
	private List<String> includes = null;
	private List<DraftJASM8> draft = null;
	private List<DraftJASM8> labeled = null;
	private byte[] machine_code = null;
	
	/*
	 * text formatting utility
	 */
	private int strLeng = 2;
	private int rom_index = 0;
	
	public CompilerJASM8()
	{
		reset();
	}
	
	/**
	 * 
	 */
	@Override
	public Compiler reset()
	{
		log = new LogListener()
		{
			@Override public void print(String msg) { }
			@Override public void println(String msg) { }
			@Override public void error(String msg) { }
			@Override public void save(String path) { }
		};
		
		strLeng = 2;
		rom_index = 0;
		machine_code = null;
		
		if (code!=null) code.clear();
		code = new ArrayList<CodeLine>();
		
		if (includes!=null) includes.clear();
		includes = new ArrayList<String>();
		
		if (labels!=null) labels.clear();
		labels = new HashMap<String, Integer>();
		
		if (equals!=null) equals.clear();
		equals = new HashMap<String, String>();
		
		if (unused!=null) unused.clear();
		unused = new ArrayList<String>();
		
		if (draft!=null) draft.clear();
		draft = new ArrayList<DraftJASM8>();
		
		if (labeled!=null) labeled.clear();
		labeled = new ArrayList<DraftJASM8>();
		
		return this;
	}
	
	/**
	 * 
	 */
	@Override
	public Compiler setParserVerbose(boolean verbose)
	{
		verbose_parser = verbose;
		return this;
	}
	
	/**
	 * 
	 */
	@Override
	public Compiler setPreprocessorVerbose(boolean verbose)
	{
		verbose_preproc = verbose;
		return this;
	}
	
	/**
	 * 
	 */
	@Override
	public Compiler setCompilerVerbose(boolean verbose)
	{
		verbose_compiler = verbose;
		return this;
	}
	
	/**
	 * 
	 */
	@Override
	public Compiler setLogListener(LogListener logging)
	{
		log = logging;
		return this;
	}
	
	@Override
	public Compiler setIncludesPath(String path)
	{
		if (path.endsWith("/") || path.endsWith("\\"))
		{
			includePath = path;
		}
		else
		{
			includePath = path+"\\";
		}
		
		return this;
	}
	
	/**
	 * 
	 */
	@Override
	public byte[] parse(String[] text)
	{
		log.println(
		"      _    _    _________  __    ___  \r\n" + 
		"     | |  / \\  / _____   \\/  |  ( _ ) \r\n" + 
		"  _  | | / _ \\ \\___ \\ | |\\/| |  / _ \\ \r\n" + 
		" | |_| // ___ \\____) || |  | | | (_) |\r\n" + 
		"  \\___//_/   \\______/ |_|  |_|  \\___/ \n");
		log.println(" compiler "+version+"\n");
		var compile_results = CompileError.NO_ERROR;
		
		/*
		 * code parsing
		 */
		if (verbose_parser)
		{
			log.println("--------------------------------------");
			log.println("\n ### parsing ###\n");
		}
		
		compile_results = parseCode(text);
		if (compile_results != CompileError.NO_ERROR)
		{
			log.error(compile_results.getDescription());
			return null;
		}
		
		for (String inc : includes)
		{
			if (inc.endsWith(".jasm"))
			{
				if (verbose_parser)
				{
					log.println("\n include: "+inc+":\n");
				}
				String[] lines = loadCode(includePath+inc);
				if (lines!=null) 
				{
					compile_results = parseCode(lines);
					if (compile_results != CompileError.NO_ERROR) 
					{
						log.error(compile_results.getDescription());
						return null;
					}
				}
				else
				{
					log.error(inc+" (The system cannot find the file specified)");
					return null;
				}
			}
		}
		
		if (verbose_parser)
		{
			log.println("\n parsing done\n");
			log.println("--------------------------------------");
		}
		
		/*
		 * pre-processing
		 */
		if (verbose_preproc)
		{
			log.println("--------------------------------------");
			log.println("\n ### Pre-processor ###\n");
		}
		
		compile_results = preprocessor(code);
		if (compile_results != CompileError.NO_ERROR)
		{
			log.error(compile_results.getDescription());
			return null;
		}
		
		if (verbose_preproc)
		{
			log.println("\n pre-processing done\n");
			log.println("--------------------------------------");
		}
		
		/*
		 * compiler
		 */
		if (verbose_compiler)
		{
			log.println("--------------------------------------");
			log.println("\n ### Compiling ### \n");
		}
		
		compile_results = compileDraft(draft);
		if (compile_results != CompileError.NO_ERROR)
		{
			log.error(compile_results.getDescription());
			return null;
		}
		
		if (!verbose_compiler) log.println("--------------------------------------");
		log.println("\n compiling successful\n");
		log.println(" external files  : "+includes.size());
		log.println(" instructions    : "+draft.size());
		log.println(" program size    : "+rom_index+" bytes");
		log.println("\n--------------------------------------\n");
		return machine_code;
	}
	
	private String[] loadCode(String inc)
	{
		TextFile tf = null;
		try
		{
			tf = TextFileParser.file(inc);
		} 
		catch (FileNotFoundException e)
		{
			return null;
		}
		if (tf == null) return null;
		
		return tf.getLines();
	}
	
	/* ==================================================================
	 * 
	 * text file parser
	 * 
	 * responsibility:
	 * format text so it can be easily processed into machine code
	 * 
	 * ==================================================================
	 */
	private CompileError parseCode(String[] text)
	{
		CompileError error;
		int line = 0;
		int totalLines = text.length;
		strLeng = (""+totalLines).length() + 1;
		
		for (String l : text)
		{
			error = CompileError.NO_ERROR;
			l = l.replace("\t", " ");
			l = l.replaceAll(" +", " ");
			l = l.replace(", ", ",");
			l = l.replace(" ,", ",");
			l = l.trim();
			line++;
			
			if (l.startsWith(";"))
			{
				continue; // skip comment
			}
			
			if (l.contains(";")) // skip everything after semicolon
			{
				l = l.split(";")[0];
				l = l.trim();
			}
			
			if (l.contains(":")) // if label, split to (possibly) parse code after label
			{
				String t[] = l.split(":");
				
				if (!isValidLabel(t[0]))// error, bad label
				{
					return CompileError.labelError(line, l); 
				}
				
				if (t.length > 2)
				{
					if (t[1].contains(" "))// error label has spaces
					{
						return CompileError.labelError(line, l); 
					}
				}
				
				error = parseLine(line, t[0]+":"); // parse label
				
				if (t.length == 2)// parse possibly code on the same line
				{
					String i = t[1].trim(); 
					error = parseLine(line, i);
				}
			}
			else
			{
				error = parseLine(line, l); // parse code
			}
			
			if (error != CompileError.NO_ERROR)
			{
				return error; // return the found error
			}
		}
		
		return CompileError.NO_ERROR;
	}
	
	private CompileError parseLine(int line, String l)
	{
		if (l.equalsIgnoreCase("")) return CompileError.NO_ERROR; // skip empty lines
		
		/*
		 * include external code
		 */
		if (l.startsWith(".inc "))
		{
			String include = l.substring(5);
			if (include.startsWith("\"") && include.endsWith("\"")) 
			{
				include = include.replace("\"", "");
				includes.add(include);
				return CompileError.NO_ERROR;
			}
			else
			{
				return CompileError.includeError(line, l); // error
			}
		}
		
		/*
		 * store constant naming
		 */
		if (l.startsWith(".equ "))
		{
			String equates = l.substring(5);
			String[] tokens = equates.split(" ");
			if (tokens.length != 2)
			{
				return CompileError.equateError(line, l);
			}
			
			String name = tokens[0];
			if (!isValidLabel(name))
			{
				return CompileError.labelError(line, l);
			}
			
			String value = tokens[1];
			if (!isValidNumber(value))
			{
				return CompileError.numberError(line, l);
			}
			
			equals.put(name, value);
		}
		
		/*
		 * parse line of code
		 */
		if (verbose_parser) 
		{
			String linemarker = fillFromBack(""+line," ",strLeng)+"| "+l;
			log.println(linemarker);
		}
		
		code.add( new CodeLine(line, l) );
		return CompileError.NO_ERROR;
	}
	
	private boolean isValidNumber(String number)
	{
		if (number.startsWith("&")) number = number.substring(1);
		if (! StringUtil.isInteger(number)) return false;
		if (! StringUtil.isHexadec(number)) return false;
		return true;
	}
	
	private boolean isValidLabel(String label)
	{
		if ( label.matches("\\D[a-zA-Z0-9\\_]+") ) return true;
		return false;
	}
	
	private String fillFromBack(String msg, String chr, int leng) 
	{
		String filler = "";
		for (int s=0; s<leng; s++) filler += chr;
		String concat = filler+msg;
		int strLeng = concat.length();
		return concat.substring(strLeng-leng, strLeng);
	}
	
	/* ==================================================================
	 * 
	 * pre-processor
	 * 
	 * responsibility:
	 * handles declarations
	 * turns labels into addresses
	 * drafts machine instructions
	 * 
	 * ==================================================================
	 */
	
	private CompileError preprocessor(List<CodeLine> code)
	{
		if (verbose_preproc) strLeng = 2;
		
		/*
		 * track declaration and labels
		 * process instructions
		 */
		for (int i=0,l=code.size(); i<l; i++)
		{
			CodeLine line = code.get(i);
			CompileError error = processLine(line.getLineNumber(), line.getLineCode());
			if (error != CompileError.NO_ERROR) return error;
		}
		
		if (verbose_preproc)
		{
			log.println(" Addressed labels:\n");
		}
		
		/*
		 * insert label addresses
		 */
		for (DraftJASM8 d : labeled)
		{
			String label = d.getLabel();
			int addr = labels.get(label);
			
			if (verbose_preproc)
			{
				String fill = StringUtil.fill(label, " ", strLeng);
				log.println(" "+fill+" 0x"+String.format("%04X", addr) );
			}
			
			d.setLabelAddress(addr);
			unused.remove(label);
		}
		
		/*
		 * notify about unused labels
		 */
		if (verbose_preproc)
		{
			log.println("\n Unused labels:\n");
			for (String label : unused)
			{
				log.println(" "+label);
			}
		}
		
		return CompileError.NO_ERROR;
	}
	
	private CompileError processLine(int num, String line) 
	{
		// is a label
		if (line.contains(":")) 
		{
			String label = line.substring(0,line.length()-1);
			
			if (verbose_preproc)
			{
				int leng = label.length() + 2;
				strLeng = (leng > strLeng)?leng:strLeng;
				if (strLeng > 24) strLeng = 24;
			}
			
			unused.add(label);
			labels.put(label, rom_index);
			return CompileError.NO_ERROR;
		}
		
		// is instruction
		var draft_inst = DraftBuilderJASM8.getDraft(rom_index, line);
		if (draft_inst != null)
		{
			rom_index += draft_inst.machineCode().length;
			if (draft_inst.hasLabel()) labeled.add(draft_inst);
			draft.add(draft_inst);
		}
		else
		{
			return CompileError.lineError(num, line);
		}
		
		return CompileError.NO_ERROR;
	}
	
	/* ==================================================================
	 * 
	 * compiler
	 * 
	 * responsibility:
	 * takes the machine code from the draft 
	 * creates the program byte array
	 * 
	 * ==================================================================
	 */

	private CompileError compileDraft(List<DraftJASM8> draft)
	{
		machine_code = new byte[rom_index];
		int index = 0;
		
		for (DraftJASM8 d : draft)
		{
			byte[] mc = d.machineCode();
			for (byte b : mc)
			{
				machine_code[index++] = b;
			}
		}
		
		if (verbose_compiler)
		{
			int count = 0;
			int leng = machine_code.length >> 4;
			if (leng<2)leng=2;
			
			log.print(String.format("%0"+leng+"X | ", count));
			for (byte b : machine_code)
			{
				log.print(String.format("%02X ", b));
				count++;
				if (count > 15)
				{
					log.print(String.format("%0"+leng+"X | ", count)+"\n");
					count=0;
				}
			}
			log.print("\n");
		}
		
		return CompileError.NO_ERROR;
	}
}
