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
		String motorNum = translatorBlock1.toCode();
		
		String portNum1;
		String portNum2;
		
		if(Integer.valueOf(motorNum)==1)
		{
			portNum1 = "3";
			portNum2 = "5";
		}
		else if(Integer.valueOf(motorNum)==2)
		{
			portNum1 = "4";
			portNum2 = "6";
		}
		else
		{
			portNum1 = "0";
			portNum2 = "0";
		}
		
		if (translatorBlock1 instanceof NumberBlock)
		{
			translator.addOutputPin(portNum1);
			translator.addOutputPin(portNum2);
		}
		else
		{
			String setupCode = "pinMode( " + portNum1 + " , OUTPUT);\npinMode( " + portNum2 + " , OUTPUT);";
			translator.addSetupCommand(setupCode);
		}
		TranslatorBlock translatorBlock2 = this.getRequiredTranslatorBlockAtSocket(1);
		int direction = Integer.valueOf(translatorBlock2.toCode());
		
		TranslatorBlock translatorBlock3 = this.getRequiredTranslatorBlockAtSocket(2);
		String speed = translatorBlock3.toCode();
		
		if(direction==1)
		{
			String ret = "analogWrite(" + portNum1 + " , " + speed + ");\nanalogWrite(" + portNum2 + " , " + 0 +");";
			return ret;
		}
		else
		{
			String ret = "analogWrite(" + portNum1 + " , " + 0 + ");\nanalogWrite(" + portNum2 + " , " + speed +");";
			return ret;
		}
		
	}

}