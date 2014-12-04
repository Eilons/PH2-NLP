package com.Technion.ie.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author Eilon
 *
 */

public class Utils {
	
	private final Logger logger = Logger.getLogger(Utils.class);
	public static String RESULT_LINE_FORMAT = "%s\n";

	public static Map<String,Short> readCountsFileOriginal() throws IOException, URISyntaxException {
		String countsFile = "/cfgCounts.txt";
		Map<String,Short> wordCountMap = new TreeMap<String, Short>();
		InputStream wordCountFile = Utils.class.getResourceAsStream(countsFile);
		List<String> allLines = Utils.readAllLines(wordCountFile);
		for (String line : allLines) {
			String[] strings = line.split(" ");
			if (strings[1].equals("UNARYRULE"))
			{
				if (wordCountMap.containsKey(strings[3]))
				{
					Short countValue = wordCountMap.get(strings[3]);
					wordCountMap.put(strings[3] , (short) (countValue + 
													Short.parseShort(strings[0])));		
				}
				else 
				{
					wordCountMap.put(strings[3], Short.parseShort(strings[0]));	
				}
				
			}
		}
		
		return wordCountMap;
	}
	
	/**
	 * @param wordCountMap
	 * @throws IOException
	 * @throws ParseException
	 * Main func : read line by line from a file, each line is tree.
	 * return each tree with _RARE_ symbol for rare words
	 * @throws JSONException 
	 */
	public static List<JSONArray> buildRareTrees (Map<String,Short> wordCountMap ) throws IOException, ParseException, JSONException
	{
		List<String> treeLines = new ArrayList<String>();//contain all tree rows
		List<JSONArray> parseTrees = new ArrayList<JSONArray>();
		treeLines = Utils.readTreeFile("/parse_train.dat");
		
		for (String line : treeLines) 
		//each line is tree to parse
		{
			JSONArray tree = new JSONArray();
			tree = Utils.constructJsonTrees(line);
			Utils.replaceRareWords(tree,wordCountMap);
			parseTrees.add(tree);
		}
		return parseTrees;
	}
	
	private static List<String> readTreeFile (String path) throws IOException {
		InputStream wordFile = Utils.class.getResourceAsStream(path);
		List<String> allLines = Utils.readAllLines(wordFile);
		ArrayList<String> treeList  = new ArrayList<String>();
		for (String line : allLines) {
				treeList.add(line);	
			
			
		}
		return treeList;
	}
	
	private static List<String> readAllLines(InputStream inputFile) throws IOException {
		List<String> countsList = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputFile));
		String line = "";
		while ((line = reader.readLine()) != null) {
			countsList.add(line);
		}
		return countsList;
	}



	private static JSONArray constructJsonTrees(String line) throws JsonParseException, JsonMappingException, IOException, ParseException
	{
		org.json.simple.JSONArray rootNode = null;
		JSONParser parser = new JSONParser();
		rootNode = (org.json.simple.JSONArray) parser.parse(line);
		return rootNode;
	}
	
	private static void replaceRareWords (JSONArray rootNode , Map<String,Short> wordCountMap) throws JSONException
	//Takes a nested list in loaded JSON format and a set of rare (word, tag) pairs 
	//    and returns the new rare_tree     
	{
		if (rootNode.size() == 3)
		{
			replaceRareWords((JSONArray)rootNode.get(1) , wordCountMap);
			replaceRareWords((JSONArray)rootNode.get(2) , wordCountMap);
		}
		else 
		{
			if (rootNode.size() == 2) 
			{
				String word;
				word = rootNode.get(1).toString();
				if (Utils.isRareWord(word , wordCountMap))
				{
					rootNode.remove(1);
					rootNode.add( "_RARE_");
					
				}
			}
		}
	}

	private static boolean isRareWord(String word, Map<String, Short> wordCountMap) 
	{
		if (wordCountMap.get(word) < 5)
			return true;
		return false;
	}
	
	public static void writeNewParseTrees (List<JSONArray> parseTree)
	{
		String content = "";
		for (JSONArray tree : parseTree) 
		{
			content += (String.format(RESULT_LINE_FORMAT, tree.toString()));
		}
		Utils.writeToFile ("/c:/h2p-NLP/parse_train_new.dat",content);
		
	}

	private static void writeToFile(String path, String content) 
	{
		try{
			File file = new File(path);
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			
			System.out.println("Done");
		}
			catch (IOException e) {
					e.printStackTrace();
			}
		
	}
	
	//part 2
	
	private List<String> readFile (String path) throws IOException
	{
		Map<String,Short> wordCountMap = new TreeMap<String, Short>();
		InputStream wordCountFile = Utils.class.getResourceAsStream(path);
		List<String> allLines = Utils.readAllLines(wordCountFile);
		
		return allLines;
	}
	
}
