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
		TranslatorBlock translatorBlock = this.getRequiredTranslatorBlockAtSocket(0);
		String portNum1 = translatorBlock.toCode();
		
		if (translatorBlock instanceof NumberBlock)
		{
			translator.addOutputPin(portNum1.trim());
		}
		else
		{
			String setupCode = "pinMode( " + portNum1 + " , OUTPUT);\n";
			translator.addSetupCommand(setupCode);
		}
		translatorBlock = this.getRequiredTranslatorBlockAtSocket(1);
		String value = translatorBlock.toCode();
		
		String ret = "analogWrite(" + portNum1 + " , " + value + ");\n";
		return ret;
	}

}