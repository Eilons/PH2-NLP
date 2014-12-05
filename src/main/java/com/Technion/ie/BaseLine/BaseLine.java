package com.Technion.ie.BaseLine;

import java.io.IOException;
import java.util.List;

import com.Technion.ie.Emission.RuleParameters;
import com.Technion.ie.Utils.Utils;

public class BaseLine {
	public static final String New_Count_File = "/parse_train_counts.txt";
	public static final String Parse_dev_File = "/parse_dev.txt";
	
	public void createCkyAlg () throws IOException
	{
		List<String> fileCountLines = Utils.readFile(New_Count_File);
		List<String> fileSentences = Utils.readFile(Parse_dev_File);
		List<List<String>> sentencesWords = Utils.readFileSeparateIntoWords (fileSentences);
		RuleParameters ruleParameters = new RuleParameters(fileCountLines);
		CkyAlg ckyAlg = new CkyAlg();
		ckyAlg.cky_alg(sentencesWords.get(0), ruleParameters);//get(0) for test, later ill do for loop over all sentences;
		
		
		
	}

}
