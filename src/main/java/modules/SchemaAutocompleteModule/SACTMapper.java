package main.java.modules.SchemaAutocompleteModule;

import main.java.thirdParty.acsdb.AcsdbToolsManager;
import main.java.thirdParty.acsdb.model.Schema;
import main.java.thirdParty.acsdb.tools.schemaAutocomplete.Attrib2Score;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;


public class SACTMapper{
	
	public SACTMapper(){}
	
	public Map<String,Double> getTopKProbabileAttributes(List<String> context, int k){

		AcsdbToolsManager manager = null;
		
		try {
			 manager = AcsdbToolsManager.getInstance();
		} catch (ClassNotFoundException | FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Attrib2Score> result = null;
		
		try {
			result = manager.getSchemaAutocompleteTool().getNextAttributes(new Schema(context.toArray(new String[0])));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(result == null)
			return null;

        Map<String,Double> attr2prob = new HashMap<>();

		for(Attrib2Score attr : result)
			attr2prob.put(replaceBadChar(attr.getAttrib()), (double) attr.getScore());

		Map<String,Double> sortedMap =
				attr2prob.entrySet().stream()
						.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
						.limit(k)
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
		(e1, e2) -> e1, LinkedHashMap::new));

        return sortedMap;
    }

	private String replaceBadChar(String attrib) {
		return attrib.replace(".","").replace(",", " ");
	}

}