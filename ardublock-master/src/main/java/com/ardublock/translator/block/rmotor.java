package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class rmotor extends TranslatorBlock
{
	public rmotor(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		TranslatorBlock translatorBlock1 = this.getRequiredTranslatorBlockAtSocket(0);
		String speed = translatorBlock1.toCode();
		
		if (translatorBlock1 instanceof NumberBlock)
		{
			translator.addOutputPin("6");
			translator.addOutputPin("9");
		}
		else
		{
			String setupCode = "pinMode( 6 , OUTPUT);\npinMode( 9 , OUTPUT);";
			translator.addSetupCommand(setupCode);
		}
		
		String ret = "analogWrite(6 , " + speed + ");\nanalogWrite(9 , 0);";
		return ret;
	}

}