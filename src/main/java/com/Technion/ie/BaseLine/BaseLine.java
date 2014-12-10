package com.Technion.ie.BaseLine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.Technion.ie.Emission.RuleParameters;
import com.Technion.ie.Utils.Utils;
import com.Technion.ie.dao.ResultForamt;

public class BaseLine {
	public static final String New_Count_File = "/parse_train_counts.txt";
	public static final String Parse_dev_File = "/parse_dev.txt";
	
	public static final String vert_new_count_file = "/parse_train_vert_count_new.txt";
	public static final String parse_vert_file = "/parse_train_vert.txt";
	
	public static final String parse_tree_out_2 = "/c:/h2p-NLP/parse_dev.out";
	public static final String parse_tree_out_3 = "/c:/h2p-NLP/parse_vert.out";
	
	
	public void createCkyAlg () throws IOException
	{
		List<String> fileCountLines = Utils.readFile(New_Count_File);
		List<String> fileSentences = Utils.readFile(Parse_dev_File);
		List<List<String>> sentencesWords = Utils.readFileSeparateIntoWords (fileSentences);
		RuleParameters ruleParameters = new RuleParameters(fileCountLines);
		List<String> parseTreeContents = new ArrayList<String>();
		ResultForamt result = new ResultForamt();
		
		for (List<String> sentence : sentencesWords)//loop over all sentences in test data 
		{
			CkyAlg ckyAlg = new CkyAlg();
			result = ckyAlg.cky_alg(sentence, ruleParameters);
			parseTreeContents.add(result.getSubtree());
			
		}
		//wirte to file
		Utils.writeResultsToTxt(parseTreeContents,parse_tree_out_2);
		
	}
	
	public void parseVerticalMarkovization () throws IOException
	{
		List<String> fileCountLines = Utils.readFile(vert_new_count_file);
		List<String> fileSentences = Utils.readFile(Parse_dev_File);
		List<List<String>> sentencesWords = Utils.readFileSeparateIntoWords (fileSentences);
		RuleParameters ruleParameters = new RuleParameters(fileCountLines);
		List<String> parseTreeContents = new ArrayList<String>();
		ResultForamt result = new ResultForamt();
		
		for (List<String> sentence : sentencesWords)
		{
			CkyAlg ckyAlg = new CkyAlg();
			result = ckyAlg.cky_alg(sentence, ruleParameters);
			parseTreeContents.add(result.getSubtree());
		}
		//wirte to file
		Utils.writeResultsToTxt(parseTreeContents,parse_tree_out_3);	
	}
}
