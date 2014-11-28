package com.Technion.ie.Utils;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Utils {

	public static Map<String,Short> readQueries() throws IOException, URISyntaxException {
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
	
	private static List<String> readAllLines(InputStream inputFile) throws IOException {
		List<String> countsList = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputFile));
		String line = "";
		while ((line = reader.readLine()) != null) {
			countsList.add(line);
		}
		return countsList;
	}
}
