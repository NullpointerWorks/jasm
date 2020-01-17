package com.nullpointerworks.jasm.jasm8.compiler;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nullpointerworks.jasm.jasm8.Compiler;
import com.nullpointerworks.jasm.jasm8.LogListener;

import com.nullpointerworks.util.StringUtil;
import com.nullpointerworks.util.concurrency.Threading;
import com.nullpointerworks.util.file.textfile.TextFile;
import com.nullpointerworks.util.file.textfile.TextFileParser;

public class CompilerJASM8 implements Compiler
{
	public static final String version = "v1.3 alpha";
	
	private CompileError flag_error = CompileError.NO_ERROR;
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
	private List<String> code = null;
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
	public Compiler reset()
	{
		log = new LogListener()
		{
			@Override public void print(String msg) { }
			@Override public void println(String msg) { }
			@Override public void error(String msg) { }
			@Override public void save(String path) { }
		};
		
		flag_error = CompileError.NO_ERROR;
		strLeng = 2;
		rom_index = 0;
		machine_code = null;
		
		if (code!=null) code.clear();
		code = new ArrayList<String>();
		
		if (includes!=null) includes.clear();
		includes = new ArrayList<String>();
		
		if (labels!=null) labels.clear();
		labels = new HashMap<String, Integer>();
		
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
	public byte[] parse(String[] text)
	{
		log.println(
		"      _    _    _________  __    ___  \r\n" + 
		"     | |  / \\  / _____   \\/  |  ( _ ) \r\n" + 
		"  _  | | / _ \\ \\___ \\ | |\\/| |  / _ \\ \r\n" + 
		" | |_| // ___ \\____) || |  | | | (_) |\r\n" + 
		"  \\___//_/   \\______/ |_|  |_|  \\___/ \n");
		log.println(" compiler "+version+"\n");
		
		/*
		 * code parsing
		 */
		if (verbose_parser)
		{
			log.println("--------------------------------------");
			log.println("\n ### parsing ###\n");
		}
		
		parseCode(text);
		
		for (String inc : includes)
		{
			if (inc.endsWith(".jasm"))
			{
				if (verbose_parser)
				{
					log.println("\n include: "+inc+":\n");
				}
				String[] lines = loadCode(includePath+inc);
				if (lines!=null) parseCode(lines);
			}
		}
		
		if (flag_error != CompileError.NO_ERROR) return null;
		
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
		
		preprocessor(code);
		
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
		
		compileDraft(draft);
		
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
			log.error(inc+" (The system cannot find the file specified)");
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

	private void parseCode(String[] text)
	{
		int line = 0;
		int totalLines = text.length;
		strLeng = (""+totalLines).length() + 1;
		
		for (String l : text)
		{
			line++;
			
			l = l.replace("\t", " ");
			l = l.replaceAll(" +", " ");
			l = l.replace(", ", ",");
			l = l.replace(" ,", ",");
			l = l.trim();
			
			if (l.startsWith(";"))
			{
				continue;
			}
			
			if (l.contains(";"))
			{
				l = l.split(";")[0];
				l = l.trim();
			}
			
			if (l.contains(":"))
			{
				String t[] = l.split(":");
				
				if (!isValidLabel(t[0]))
					flag_error = CompileError.BAD_LABEL_NAME;
				
				if (t.length > 2)
				{
					if (t[1].contains(" "))
						flag_error = CompileError.BAD_LABEL_LOCATION;
				}
				
				processLine(line, t[0]+":");
				
				if (t.length == 2)
				{
					String i = t[1].trim();
					processLine(line, i);
				}
			}
			else
			{
				processLine(line, l);
			}
			
			if (flag_error != CompileError.NO_ERROR) return;
		}
	}
	
	private void processLine(int line, String l)
	{
		if (l.equalsIgnoreCase("")) return;
		
		/*
		 * include external code
		 */
		if (l.startsWith(".inc "))
		{
			includes.add(l.substring(5));
			return;
		}
		
		/*
		 * parse line of code
		 */
		if (flag_error != CompileError.NO_ERROR)
		{
			if (verbose_parser)
			{
				String linemarker = fillFromBack(""+line," ",strLeng)+"| "+l;
				log.println(linemarker);
			}
			else log.println(l);
			
			Threading.sleep(1);
			log.error("line: "+line+". "+flag_error.getDescription());
			return;
		}
		
		if (verbose_parser) 
		{
			String linemarker = fillFromBack(""+line," ",strLeng)+"| "+l;
			log.println(linemarker);
		}
		
		code.add(l);
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
	 * handles directives
	 * turns labels into addresses
	 * drafts machine instructions
	 * 
	 * ==================================================================
	 */
	
	private void preprocessor(List<String> code)
	{
		if (verbose_preproc) strLeng = 2;
		
		/*
		 * track directives and labels
		 * process instructions
		 */
		for (int i=0,l=code.size(); i<l; i++)
		{
			String line = code.get(i);
			if (line.startsWith("."))
			{
				
				// TODO resolve .equ directives etc
				
				continue;
			}
			
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
				continue;
			}
			
			var draft_inst = DraftBuilderJASM8.getDraft(rom_index, line);
			if (draft_inst != null)
			{
				rom_index += draft_inst.machineCode().length;
				if (draft_inst.hasLabel()) labeled.add(draft_inst);
				draft.add(draft_inst);
			}
			else
			{
				log.error(line); // TODO
			}
		}
		
		/*
		 * insert label addresses
		 */
		if (verbose_preproc)
		{
			log.println(" Addressing used labels:\n");
		}
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
		
		// unused labels
		if (verbose_preproc)
		{
			log.println("\n Unused labels:\n");
			for (String label : unused)
			{
				log.println(" "+label);
			}
		}
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
	
	private void compileDraft(List<DraftJASM8> draft)
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
			for (byte b : machine_code)
			{
				log.print(String.format("%02X ", b)+" ");
				count++;
				if (count > 7)
				{
					log.print("\n");
					count=0;
				}
			}
			log.print("\n");
		}
	}
}
