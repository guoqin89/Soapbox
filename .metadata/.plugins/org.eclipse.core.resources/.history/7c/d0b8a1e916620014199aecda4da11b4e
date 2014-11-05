package com.ardublock.translator.block;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class line_front extends TranslatorBlock
{
	public line_front(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}

	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		String setupCode = "pinMode( 3 , OUTPUT);\npinMode( 5 , OUTPUT);\npinMode( 6 , OUTPUT);\npinMode( 9 , OUTPUT);";
		translator.addSetupCommand(setupCode);
		
		String ret = "analogWrite(3 , 0 );\nanalogWrite(5 , 0);\nanalogWrite(6 , 0 );\nanalogWrite(9 , 0);";
		return ret;
	}

}