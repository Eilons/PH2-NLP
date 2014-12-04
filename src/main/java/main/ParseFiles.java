package main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONException;
import org.json.simple.parser.ParseException;

import com.Technion.ie.Utils.Utils;

public class ParseFiles {
	public static final String Original_Count_File = "/cfgCounts.txt";
	public static final String New_Count_File = "/parse_train_counts.txt";
	
	 
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, URISyntaxException, ParseException, JSONException {
		Map<String,Short> wordCountMap = new TreeMap<String,Short>();
		wordCountMap = Utils.readCountsFileOriginal(Original_Count_File);
		List<org.json.simple.JSONArray> parseTree = Utils.buildRareTrees(wordCountMap);
		Utils.writeNewParseTrees(parseTree);
	}

}
