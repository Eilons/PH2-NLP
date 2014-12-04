package com.Technion.ie.Emission;

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
	
	public RuleParameters (List<String> allLines)
	{
		fileLines = allLines;
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
	
	
	

}
