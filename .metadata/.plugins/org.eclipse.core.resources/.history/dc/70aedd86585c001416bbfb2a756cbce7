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
		TranslatorBlock translatorBlock3 = this.getRequiredTranslatorBlockAtSocket(2);
		int value2 = Integer.valueOf(translatorBlock3.toCode());
		
		TranslatorBlock translatorBlock4 = this.getRequiredTranslatorBlockAtSocket(3);
		String value3 = translatorBlock4.toCode();
		
		if(value2==1)
		{
			String ret = "analogWrite(" + portNum1 + " , " + value3 + ");\nanalogWrite(" + portNum2 + " , " + 0 +");";
			return ret;
		}
		else
		{
			String ret = "analogWrite(" + portNum1 + " , " + 0 + ");\nanalogWrite(" + portNum2 + " , " + value3 +");";
			return ret;
		}
		
	}

}