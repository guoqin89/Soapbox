package com.ardublock.translator.block.DuinoEDU;

import com.ardublock.translator.Translator;
import com.ardublock.translator.block.TranslatorBlock;
import com.ardublock.translator.block.exception.SocketNullException;
import com.ardublock.translator.block.exception.SubroutineNotDeclaredException;

public class TempPro extends TranslatorBlock {
	public TempPro(Long blockId, Translator translator, String codePrefix, String codeSuffix, String label)
	{
		super(blockId, translator, codePrefix, codeSuffix, label);
	}
	@Override
	public String toCode() throws SocketNullException, SubroutineNotDeclaredException
	{
		String Pin;
		TranslatorBlock translatorBlock = this.getRequiredTranslatorBlockAtSocket(0);
		Pin = translatorBlock.toCode();

		
		translator.addHeaderFile("DHT.h");
		
		translator.addDefinitionCommand("//libraries at http://www.duinoedu.com/ \nDHT monDHTPRO_Temp("+Pin +",DHT22);"	);
		translator.addSetupCommand("monDHTPRO_Temp.brancher();");
		String ret = "monDHTPRO_Temp.readTemperature()";
		

		return codePrefix + ret + codeSuffix;
	}
	
	
	
	
	
	
	
	
	
}
