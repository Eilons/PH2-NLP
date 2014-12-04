package com.Technion.ie.BaseLine;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import com.Technion.ie.Emission.RuleParameters;

public class CkyAlg {
	private final static Logger logger = Logger.getLogger(CkyAlg.class);
	private final static String SBARQ = "SBARQ";
	private final static String S = "S";
	public static String BINARY_RULE_FORMAT = "%s-%s-%s";
	public static String UNARY_RULE_FORMAT = "%s-%s";
	
	
	public JSONArray cky_alg (List<String> sentence, RuleParameters rp)
	{
		logger.debug(sentence);
		int n = sentence.size();
		
		if (sentence.get(n).equals("?"))// check if sentence is question
			return cky_help (0, n-1, sentence, SBARQ, rp);
		return cky_help (0, n-1, sentence, S, rp);
		
		
		
		
	}
	/**
	 * Accepting the starting position i, ending position j, sentence, parent tag X, and a  
     * probability generator pg, it returns a string with the most likely subtree given the  
     * info and the probability of that subtree

	 * @param i
	 * @param j
	 * @param sent
	 * @param X
	 * @param rp
	 * @return
	 */
	private JSONArray cky_help (int i, int j, List<String> sent, String X, RuleParameters rp)
	{
		double prob;
		if (i == j)//Unary Rule
		{
			String key = String.format(UNARY_RULE_FORMAT,X,sent.get(i));
			prob = UnaryRuleProb (key, sent.get(i), X , rp);
			
			
		}
		else// Binary Rule
		{
			
		}
		
		return null;
		
	}
	
	private void get_max_of_all (int i, int j, List<String> sent, String X, RuleParameters rp)
	{
		List<String[]> ruleSet = rp.binaryRuleMap.get(X);//check only the set of rules made by X as root
		
	}
	
	
	
	/**
	 * 
	 * @param word
	 * @param X
	 * @param rp
	 * @return correct prob: if exist rule x->word return prob.
	 * 						 else if word exist return 0
	 * 						 else return prob x->_RARE_
	 */
	
	private Double UnaryRuleProb (String key, String word, String X, RuleParameters rp)
	{
		if (rp.unaryRuleProbMap.containsKey(key))
			return rp.binaryRuleProbMap.get(key);
		
		if (rp.trainWordList.contains(word))
			return 0;
		
		String RareKey = String.format(UNARY_RULE_FORMAT,X,"_RARE_");
		return rp.unaryRuleProbMap.get(RareKey);

	}

}
