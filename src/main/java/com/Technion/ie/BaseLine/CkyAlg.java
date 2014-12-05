package com.Technion.ie.BaseLine;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;

import com.Technion.ie.Emission.RuleParameters;
import com.Technion.ie.dao.ResultForamt;

public class CkyAlg {
	private final static Logger logger = Logger.getLogger(CkyAlg.class);
	private final static String SBARQ = "SBARQ";
	private final static String S = "S";
	public static String BINARY_RULE_FORMAT = "%s-%s-%s";
	public static String UNARY_RULE_FORMAT = "%s-%s";
	public Map <String , Map<String,Double>> pi;// key: (1-1-NP) -> value: [(PP-VP)-> 0.5]
	
	
	public CkyAlg ()
	{
		pi = new TreeMap<String, Map<String,Double>>();
	}
	
	public ResultForamt cky_alg ( List<String> sentence, RuleParameters rp)
	{
		logger.debug(sentence);
		int n = sentence.size();
		
		if (sentence.get(n-1).equals("?"))// check if sentence is question
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
	private ResultForamt cky_help (int i, int j, List<String> sent, String X, RuleParameters rp)
	{
		double prob =0.0;
		String left = "";
		String right = "";
		ResultForamt rf = new ResultForamt();
		if (i == j)//Unary Rule
		{
			String key = String.format(UNARY_RULE_FORMAT,X,sent.get(i));
			prob = UnaryRuleProb (key, sent.get(i), X , rp);
			String[] subtree = {X , sent.get(i)}; 
			rf.setSubtree(subtree);
			rf.setProb(prob);
			return rf;
		}
		else// Binary Rule
		{
			String piSeqKey = String.format(BINARY_RULE_FORMAT, Integer.toString(i) ,Integer.toString(j) , X);
			if (!pi.containsKey(piSeqKey));
			{
				ResultForamt maxResultMap = new ResultForamt();
				maxResultMap = get_max_of_all(i, j, sent, X, rp);
				left = (maxResultMap.getSubtree())[0];
				right = (maxResultMap.getSubtree())[1];
				String SeqRuleKey = String.format(UNARY_RULE_FORMAT, left ,right);
				Map<String,Double> ruleMap = new TreeMap<String,Double>();
				prob = maxResultMap.getProb();
				ruleMap.put(SeqRuleKey, prob);
				pi.put(piSeqKey, ruleMap);
			}
		}
		String[] subtree = { X,left,right};
		rf.setSubtree(subtree);
		rf.setProb(prob);
		
		
		return rf;
		
	}
	
	private ResultForamt get_max_of_all (int i, int j, List<String> sent, String X, RuleParameters rp)
	{
		List<String[]> ruleSet = rp.binaryRuleMap.get(X);//check only the set of rules made by X as root
		String Y = "";//Left noneterminal
		String Z = "";//right noneterminal
		double ruleProb;
		double allProb;
		double best = -1;
		ResultForamt left_Rule = new ResultForamt();
		ResultForamt right_Rule = new ResultForamt();
		ResultForamt resultRule = new ResultForamt();

		
		if (ruleSet != null)
		{
			for (String[] rule : ruleSet) 
			{
				for (int s = i; s < j; s++) // s - split point
				{
					String left = rule[0];
					String right = rule[1];
					String key = String.format(BINARY_RULE_FORMAT,X,left,right);
					int splitPlus1 = s + 1; //only for printing debug
					logger.info("checking:" + "pi(" + i + "," + j + "," + X + ") = " + "q(" + X + "->" + left + right +")" + "* pi(" + i + "," + s + "," + left + ") * pi (" + splitPlus1 + "," + j + "," + right + ")");
					ruleProb = rp.binaryRuleProbMap.get(key);
					left_Rule = cky_help(i, s, sent, left, rp);
					right_Rule = cky_help (s+1,j,sent,right,rp);
					logger.info("value is :" + ruleProb + "*" + left_Rule.getProb() + "*" + right_Rule.getProb() );
					allProb = ruleProb * left_Rule.getProb() * right_Rule.getProb();
					if (allProb > best)//Update
					{
						best = allProb;
						Y = left;
						Z = right;
					}
					
					
				}
			}
		}
		String[] resultKey = {Y,Z};
		resultRule.setSubtree(resultKey);
		resultRule.setProb(best);
		return resultRule;
		
		
	}
	
	
	
	/**
	 * 
	 * @param word
	 * @param X
	 * @param rp
	 * @return correct prob: if exist rule x->word return prob.
	 * 						 else if word exist return 0
	 * 						 else if rule prob x->_RARE_ exist return his value
	 * 						 else return 0
	 */
	
	private Double UnaryRuleProb (String key, String word, String X, RuleParameters rp)
	{
		if (rp.unaryRuleProbMap.containsKey(key))
			return rp.unaryRuleProbMap.get(key);
		
		if (rp.trainWordList.contains(word))
			return (double) 0;
		
		String RareKey = String.format(UNARY_RULE_FORMAT,X,"_RARE_");
		if (rp.unaryRuleProbMap.containsKey(RareKey))
			return rp.unaryRuleProbMap.get(RareKey);
		return 0.0;

	}

}
