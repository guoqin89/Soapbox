package com.ardublock.translator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import com.ardublock.translator.adaptor.BlockAdaptor;
import com.ardublock.translator.adaptor.OpenBlocksAdaptor;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.TranslatorBlockFactory;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNameDuplicatedException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

import edu.mit.blocks.codeblocks.Block;
import edu.mit.blocks.renderable.RenderableBlock;
import edu.mit.blocks.workspace.Workspace;

public class Translator
{
	private static final String variablePrefix = "_ABVAR_";

	private Set<String> headerFileSet;
	private Set<String> definitionSet;
	private List<String> setupCommand;
	private List<String> char1Command;
	private Set<String> functionNameSet;
	private Set<String> functionNameSetLineSensor1;
	private Set<String> functionNameSetLineSensor2;
	private Set<String> functionNameSetLineSensor3;
	private Set<String> functionNameSetLineSensor4;
	private Set<String> functionNameSetLineLeftEye;
	private Set<String> functionNameSetLineNose;
	private Set<String> functionNameSetLineRightEye;
	private Set<TranslatorBlock> bodyTranslatreFinishCallbackSet;
	private BlockAdaptor blockAdaptor;
	
	private Set<String> inputPinSet;
	private Set<String> outputPinSet;
	
	private Map<String, String> numberVariableSet;
	private Map<String, String> booleanVariableSet;
	
	private Workspace workspace;
	
	private String rootBlockName;
	
	private int variableCnt;
	private boolean isScoopProgram;

	public Translator(Workspace ws)
	{
		workspace = ws;
		reset();
	}
	
	public String genreateHeaderCommand()
	{
		StringBuilder headerCommand = new StringBuilder();
		
		if (!headerFileSet.isEmpty())
		{
			for (String file:headerFileSet)
			{
				headerCommand.append("#include <" + file + ">\n");
			}
			headerCommand.append("\n");
		}
		
		if (!definitionSet.isEmpty())
		{
			for (String command:definitionSet)
			{
				headerCommand.append(command + "\n");
			}
			headerCommand.append("\n");
		}
		
		if (!functionNameSet.isEmpty())
		{
			for (String functionName:functionNameSet)
			{
				headerCommand.append("void " + functionName + "();\n");
			}
			headerCommand.append("\n");
		}
		
		if (!functionNameSetLineSensor1.isEmpty())
		{
			for (String functionName:functionNameSetLineSensor1)
			{
				headerCommand.append("int " + functionName + "(){if(analogRead(0)>500) return 1;\nelse return 0;\n}");
			}
			headerCommand.append("\n");
		}
		
		if (!functionNameSetLineSensor2.isEmpty())
		{
			for (String functionName:functionNameSetLineSensor2)
			{
				headerCommand.append("int " + functionName + "(){if(analogRead(1)>500) return 1;\nelse return 0;\n}");
			}
			headerCommand.append("\n");
		}
		
		if (!functionNameSetLineSensor3.isEmpty())
		{
			for (String functionName:functionNameSetLineSensor3)
			{
				headerCommand.append("int " + functionName + "(){if(analogRead(2)>500) return 1;\nelse return 0;\n}");
			}
			headerCommand.append("\n");
		}
		
		if (!functionNameSetLineSensor4.isEmpty())
		{
			for (String functionName:functionNameSetLineSensor4)
			{
				headerCommand.append("int " + functionName + "(){if(analogRead(3)>500) return 1;\nelse return 0;\n}");
			}
			headerCommand.append("\n");
		}
		
		if (!functionNameSetLineLeftEye.isEmpty())
		{
			for (String functionName:functionNameSetLineLeftEye)
			{
				headerCommand.append("int " + functionName + "(){if(analogRead(0)>500) return 1;\nelse return 0;\n}");
			}
			headerCommand.append("\n");
		}
		
		if (!functionNameSetLineNose.isEmpty())
		{
			for (String functionName:functionNameSetLineNose)
			{
				headerCommand.append("int " + functionName + "(){if(analogRead(1)>500) return 1;\nelse return 0;\n}");
			}
			headerCommand.append("\n");
		}
		
		if (!functionNameSetLineRightEye.isEmpty())
		{
			for (String functionName:functionNameSetLineRightEye)
			{
				headerCommand.append("int " + functionName + "(){if(analogRead(2)>500) return 1;\nelse return 0;\n}");
			}
			headerCommand.append("\n");
		}
		
		return headerCommand.toString() + generateSetupFunction();
	}
	
	public String generateSetupFunction()
	{
		StringBuilder setupFunction = new StringBuilder();
		setupFunction.append("void setup()\n{\n");
		
		if (!inputPinSet.isEmpty())
		{
			for (String pinNumber:inputPinSet)
			{
				setupFunction.append("pinMode( " + pinNumber + " , INPUT);\n");
			}
		}
		if (!outputPinSet.isEmpty())
		{
			for (String pinNumber:outputPinSet)
			{
				setupFunction.append("pinMode( " + pinNumber + " , OUTPUT);\n");
			}
		}
		
		if (!setupCommand.isEmpty())
		{
			for (String command:setupCommand)
			{
				setupFunction.append(command + "\n");
			}
		}
		
		setupFunction.append("}\n\n");
		
		return setupFunction.toString();
	}
	
	public String generateChar1Function(String functionName)
	{
		StringBuilder setupFunction = new StringBuilder();
		setupFunction.append("char" + functionName +"()\n{\n");
		
		if (!char1Command.isEmpty())
		{
			for (String command:char1Command)
			{
				setupFunction.append(command + "\n");
			}
		}
		
		setupFunction.append("}\n\n");
		
		return setupFunction.toString();
	}
	
	public String translate(Long blockId) throws SocketNullException, SubroutineNotDeclaredException
	{
		TranslatorBlockFactory translatorBlockFactory = new TranslatorBlockFactory();
		Block block = workspace.getEnv().getBlock(blockId);
		TranslatorBlock rootTranslatorBlock = translatorBlockFactory.buildTranslatorBlock(this, blockId, block.getGenusName(), "", "", block.getBlockLabel());
		return rootTranslatorBlock.toCode();
	}
	
	public BlockAdaptor getBlockAdaptor()
	{
		return blockAdaptor;
	}
	
	public void reset()
	{
		headerFileSet = new LinkedHashSet<String>();
		definitionSet = new LinkedHashSet<String>();
		setupCommand = new LinkedList<String>();
		char1Command = new LinkedList<String>();
		functionNameSet = new HashSet<String>();
		functionNameSetLineSensor1 = new HashSet<String>();
		functionNameSetLineSensor2 = new HashSet<String>();
		functionNameSetLineSensor3 = new HashSet<String>();
		functionNameSetLineSensor4 = new HashSet<String>();
		functionNameSetLineLeftEye = new HashSet<String>();
		functionNameSetLineNose = new HashSet<String>();
		functionNameSetLineRightEye = new HashSet<String>();
		inputPinSet = new HashSet<String>();
		outputPinSet = new HashSet<String>();
		bodyTranslatreFinishCallbackSet = new HashSet<TranslatorBlock>();
		
		numberVariableSet = new HashMap<String, String>();
		booleanVariableSet = new HashMap<String, String>();
		
		blockAdaptor = buildOpenBlocksAdaptor();
		
		variableCnt = 0;
		
		rootBlockName = null;
		isScoopProgram = false;
	}
	
	private BlockAdaptor buildOpenBlocksAdaptor()
	{
		return new OpenBlocksAdaptor();
	}
	
	public void addHeaderFile(String headerFile)
	{
		if (!headerFileSet.contains(headerFile))
		{
			headerFileSet.add(headerFile);
		}
	}
	
	public void addSetupCommand(String command)
	{
		if (!setupCommand.contains(command))
		{
			setupCommand.add(command);
		}
	}
	
	public void addChar1Command(String command)
	{
		if (!char1Command.contains(command))
		{
			char1Command.add(command);
		}
	}
	
	public void addSetupCommandForced(String command)
	{
		setupCommand.add(command);
	}
	
	public void addDefinitionCommand(String command)
	{
		definitionSet.add(command);
	}
	
	public void addInputPin(String pinNumber)
	{
		inputPinSet.add(pinNumber);
	}
	
	public void addOutputPin(String pinNumber)
	{
		outputPinSet.add(pinNumber);
	}
	
	public String getNumberVariable(String userVarName)
	{
		return numberVariableSet.get(userVarName);
	}
	
	public String getBooleanVariable(String userVarName)
	{
		return booleanVariableSet.get(userVarName);
	}
	
	public void addNumberVariable(String userVarName, String internalName)
	{
		numberVariableSet.put(userVarName, internalName);
	}
	
	public void addBooleanVariable(String userVarName, String internalName)
	{
		booleanVariableSet.put(userVarName, internalName);
	}
	
	public void addFunctionName(Long blockId, String functionName) throws SubroutineNameDuplicatedException
	{
		if (functionName.equals("loop") ||functionName.equals("setup") || functionNameSet.contains(functionName))
		{
			throw new SubroutineNameDuplicatedException(blockId);
		}
		if(functionName.equals("linesensor1")) functionNameSetLineSensor1.add(functionName);
		else if(functionName.equals("linesensor2")) functionNameSetLineSensor2.add(functionName);
		else if(functionName.equals("linesensor3")) functionNameSetLineSensor3.add(functionName);
		else if(functionName.equals("linesensor4")) functionNameSetLineSensor4.add(functionName);
		else if(functionName.equals("Right_Eye")) functionNameSetLineRightEye.add("RIGHT_EYE");
		else if(functionName.equals("Left_Eye")) functionNameSetLineLeftEye.add("LEFT_EYE");
		else if(functionName.equals("Nose")) functionNameSetLineNose.add("NOSE");
		else {};
	}
	
	public boolean containFunctionName(String name)
	{
		return functionNameSet.contains(name.trim());
	}
	
	
	public String buildVariableName()
	{
		return buildVariableName("");
	}
	
	public String buildVariableName(String reference)
	{
		variableCnt = variableCnt + 1;
		String varName = variablePrefix + variableCnt + "_";
		int i;
		for (i=0; i<reference.length(); ++i)
		{
			char c = reference.charAt(i);
			if (Character.isLetter(c) || Character.isDigit(c) || (c == '_'))
			{
				varName = varName + c;
			}
		}
		return varName;
	}
	
	public Workspace getWorkspace() {
		return workspace;
	}
	
	public Block getBlock(Long blockId) {
		return workspace.getEnv().getBlock(blockId);
	}
	
	public void registerBodyTranslateFinishCallback(TranslatorBlock translatorBlock)
	{
		bodyTranslatreFinishCallbackSet.add(translatorBlock);
	}

	public void beforeGenerateHeader() {
		for (TranslatorBlock translatorBlock : bodyTranslatreFinishCallbackSet)
		{
			translatorBlock.onTranslateBodyFinished();
		}
		
	}

	public String getRootBlockName() {
		return rootBlockName;
	}

	public void setRootBlockName(String rootBlockName) {
		this.rootBlockName = rootBlockName;
	}

	public boolean isScoopProgram() {
		return isScoopProgram;
	}

	public void setScoopProgram(boolean isScoopProgram) {
		this.isScoopProgram = isScoopProgram;
	}
	
	public Set<RenderableBlock> findEntryBlocks()
	{
		Set<RenderableBlock> loopBlockSet = new HashSet<RenderableBlock>();
		Iterable<RenderableBlock> renderableBlocks = workspace.getRenderableBlocks();
		
		for (RenderableBlock renderableBlock:renderableBlocks)
		{
			Block block = renderableBlock.getBlock();
			
			if (!block.hasPlug() && (Block.NULL.equals(block.getBeforeBlockID())))
			{
				if(block.getGenusName().equals("loop"))
				{
					loopBlockSet.add(renderableBlock);
				}
				if(block.getGenusName().equals("loop1"))
				{
					loopBlockSet.add(renderableBlock);
				}
				if(block.getGenusName().equals("loop2"))
				{
					loopBlockSet.add(renderableBlock);
				}
				if(block.getGenusName().equals("loop3"))
				{
					loopBlockSet.add(renderableBlock);
				}
				if(block.getGenusName().equals("program"))
				{
					loopBlockSet.add(renderableBlock);
				}
				if(block.getGenusName().equals("setup"))
				{
					loopBlockSet.add(renderableBlock);
				}
			}
		}
		
		return loopBlockSet;
	}
	
	public Set<RenderableBlock> findSubroutineBlocks() throws SubroutineNameDuplicatedException
	{
		Set<RenderableBlock> subroutineBlockSet = new HashSet<RenderableBlock>();
		Iterable<RenderableBlock> renderableBlocks = workspace.getRenderableBlocks();
		
		for (RenderableBlock renderableBlock:renderableBlocks)
		{
			Block block = renderableBlock.getBlock();
			
			if (!block.hasPlug() && (Block.NULL.equals(block.getBeforeBlockID())))
			{
				if (block.getGenusName().equals("subroutine"))
				{
					String functionName = block.getBlockLabel().trim();
					this.addFunctionName(block.getBlockID(), functionName);
					subroutineBlockSet.add(renderableBlock);
				}
				
			}
		}
		
		return subroutineBlockSet;
	}
	
	public String translate(Set<RenderableBlock> loopBlocks, Set<RenderableBlock> subroutineBlocks) throws SocketNullException, SubroutineNotDeclaredException
	{
		StringBuilder code = new StringBuilder();
		for (RenderableBlock renderableBlock : loopBlocks)
		{
			Block loopBlock = renderableBlock.getBlock();
			code.append(translate(loopBlock.getBlockID()));
		}
		
		for (RenderableBlock renderableBlock : subroutineBlocks)
		{
			Block subroutineBlock = renderableBlock.getBlock();
			code.append(translate(subroutineBlock.getBlockID()));
		}
		beforeGenerateHeader();
		code.insert(0, genreateHeaderCommand());
		
		return code.toString();
	}
}
