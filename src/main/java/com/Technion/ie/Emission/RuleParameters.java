package com.Technion.ie.Emission;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.Technion.ie.Utils.Utils;

public class RuleParameters {
	private final static Logger logger = Logger.getLogger(RuleParameters.class);
	
	public static final String NONTERMINAL = "NONTERMINAL";
	public static final String BINARYRULE = "BINARYRULE";
	public static final String UNARYRULE = "UNARYRULE";
	public static String BINARY_RULE_FORMAT = "%s-%s-%s";
	public static String UNARY_RULE_FORMAT = "%s-%s";
	
	private List<String> fileLines;
	public Map<String,Short> nonterminalCountMap;
	public Map<String,Short> binaryRuleCountMap;
	public Map<String,Short> unaryRuleCountMap;
	public Map<String,Double> unaryRuleProbMap;
	public Map<String,Double> binaryRuleProbMap;
	public List<String> trainWordList;
	public Map<String,List<String[]>> binaryRuleMap;
	
	
	
	public RuleParameters (List<String> allLines)
	{
		fileLines = allLines;
		this.nonterminalCountMap = parametersCount(NONTERMINAL);
		this.binaryRuleCountMap = parametersCount(BINARYRULE);
		this.unaryRuleCountMap = parametersCount(UNARYRULE);
		this.unaryRuleProbMap = unaryRuleProb(unaryRuleCountMap, nonterminalCountMap);
		this.binaryRuleProbMap = binaryRuleProb(binaryRuleCountMap, nonterminalCountMap);
		this.trainWordList = trainWordsList();
		this.binaryRuleMap = binaryRuleSet();
		
	}
	/**
	 * 
	 * @return Map of all noneterminals keys with their values
	 */
	public Map<String,Short> parametersCount(String rule)
	{
		Map<String,Short> parameterCountMap = new TreeMap<String,Short>();
		switch (rule) {
		case NONTERMINAL:	parameterCountMap = nonterminalsCount();
							break;
		case BINARYRULE:	parameterCountMap = binaryRuleCount();
							break;
		case UNARYRULE:		parameterCountMap = unaryRuleCount();
							break;
		default:			logger.debug("No parameter count found");
							break;
		
		}
		
		return parameterCountMap;
	}
	
	public Map<String,Short> nonterminalsCount ()
	{
		Map<String, Short> nonterminalCountMap = new TreeMap<String, Short> ();
		for (String line : fileLines) 
		{
			String[] strings = line.split(" ");
			if (strings[1].equals(NONTERMINAL))
			{
				nonterminalCountMap.put(strings[2], Short.parseShort(strings[0]));	
			}
		}
		
		return nonterminalCountMap;
	}
	/**
	 * for the rule: NP-> DET NOUN , Map looks like: (NP-DET-NOUN, value)
	 * for thr rule: VP->VP NP+NOUN, map looks like: (VP-VP-NP+NOUN, value)
	 * @return
	 */
	public Map<String,Short> binaryRuleCount ()
	{
		Map<String,Short> binaryRuleCountMap = new TreeMap<String,Short>();
		for(String line : fileLines)
		{
			String[] strings = line.split(" ");
			if (strings[1].equals(BINARYRULE))
			{
				String key = String.format(BINARY_RULE_FORMAT,strings[2],strings[3],strings[4]);
				binaryRuleCountMap.put(key, Short.parseShort(strings[0]));
			}
			
		}
		
		return binaryRuleCountMap;
	}
	/**
	 * for the rule: NP+NOUN->Australia , Map looks like: (NP+NOUN-Australia, value)
	 * for thr rule: NOUN->Kennedy, map looks like: (NOUN-Kennedy, value)
	 * @return
	 */
	public Map<String,Short> unaryRuleCount ()
	{
		Map<String,Short> unaryRuleCountMap = new TreeMap<String,Short>();
		for (String line: fileLines)
		{
			String[] strings = line.split(" ");
			if (strings[1].equals(UNARYRULE))
			{
				String key = String.format(UNARY_RULE_FORMAT,strings[2],strings[3]);
				unaryRuleCountMap.put(key, Short.parseShort(strings[0]));
			}
		}
		
		return unaryRuleCountMap;
	}
	
	public Map<String,Double> binaryRuleProb (Map<String,Short> binaryRuleCountMap,
													Map<String,Short> nonterminalCountMap)
		{
			Map<String,Double> binaryRuleProbMap = new TreeMap<String,Double>();
			for (Entry<String,Short> entry : binaryRuleCountMap.entrySet()) 
			{
				String[] entryKey = entry.getKey().split("-");
				String key = entryKey[0];
				Double prob = (double) ((entry.getValue())/nonterminalCountMap.get(key));
				binaryRuleProbMap.put(entry.getKey(), prob);
			}
			
			return binaryRuleProbMap;
		
		}
	
	public Map<String, Double> unaryRuleProb (Map<String,Short> unaryRuleCountMap,
														Map<String,Short> nonterminalCountMap)
		{
			Map<String,Double> unaryRuleProbMap = new TreeMap<String,Double>();
			for (Entry<String,Short> entry : unaryRuleCountMap.entrySet())
			{
				String[] entryKey = entry.getKey().split("-");
				String key = entryKey[0];
				Double prob = (double) ((entry.getValue())/nonterminalCountMap.get(key));
				unaryRuleProbMap.put(entry.getKey(), prob);
				
			}
			
			return unaryRuleProbMap;
		}
	
	public List<String> trainWordsList() {
		List<String> wordList = new ArrayList<String>();
		for (String line: fileLines)
		{
			String[] strings = line.split(" ");
			if (strings[1].equals(UNARYRULE))
			{
				if (!wordList.contains(strings[3]))
				{
					wordList.add(strings[3]);
				}
			}
		}
		
		return wordList;
	}
	
	public Map<String,List<String[]>> binaryRuleSet()
	{
		Map<String,List<String[]>> ruleSet = new TreeMap<String,List<String[]>>();
		for (String line: fileLines)
		{
			String[] strings = line.split(" ");
			if (strings[1].equals(BINARYRULE))
			{
				if (ruleSet.containsKey(strings[2]))
				{
						String[] str = {strings[3], strings[4]};
						List<String[]> rules = ruleSet.get(strings[2]);
						rules.add(str);
						ruleSet.put(strings[2], rules);
				}
				else
				{
					String[] str = {strings[3], strings[4]};
					List<String[]> rules = new ArrayList<String[]>();
					rules.add(str);
					ruleSet.put(strings[2], rules);
				}
			}
		}
		return ruleSet;
	}
}
