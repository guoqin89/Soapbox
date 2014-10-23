package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class motor extends TranslatorBlock
{
	public motor(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		TranslatorBlock translatorBlock1 = this.getRequiredTranslatorBlockAtSocket(0);
		String portNum1 = translatorBlock1.toCode();
		
		TranslatorBlock translatorBlock2 = this.getRequiredTranslatorBlockAtSocket(1);
		String portNum2 = translatorBlock2.toCode();
		
		
		if (translatorBlock1 instanceof NumberBlock)
		{
			translator.addOutputPin(portNum1.trim());
			translator.addOutputPin(portNum2.trim());
		}
		else
		{
			String setupCode = "pinMode( " + portNum1 + " , OUTPUT);\npinMode( " + portNum2 + " , OUTPUT);";
			translator.addSetupCommand(setupCode);
		}
		translatorBlock1 = this.getRequiredTranslatorBlockAtSocket(1);
		String value = translatorBlock1.toCode();
		
		String ret = "analogWrite(" + portNum1 + " , " + value + ");\n" + portNum2 + " , " + value + ");";
		return ret;
	}

}