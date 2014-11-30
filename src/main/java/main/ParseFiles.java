package main;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.simple.parser.ParseException;

import com.Technion.ie.Utils.Utils;

public class ParseFiles {

	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException, URISyntaxException, ParseException {
		Map<String,Short> wordCountMap = new TreeMap<String,Short>();
		List<JSONArray> parseTree = new ArrayList<JSONArray>();
		wordCountMap = Utils.readCountsFile();
		parseTree = Utils.buildRareTrees(wordCountMap);
		Utils.writeNewParseTrees(parseTree);
	}

}
