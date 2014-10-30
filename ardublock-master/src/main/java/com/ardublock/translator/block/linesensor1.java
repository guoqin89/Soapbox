package com.ardublock.translator.block;

import com.ardublock.translator.Translator;

public class linesensor1 extends TranslatorBlock
{
	public linesensor1(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode()
	{
		try {
			translator.addFunctionName(blockId,label);
		}
		catch (Exception e) {		    
		}
	    return codePrefix + label+ "()" + codeSuffix;
	}

}
