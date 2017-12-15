package main.java.modules.JGIModule;

import main.java.thirdParty.acsdb.AcsdbToolsManager;
import main.java.thirdParty.acsdb.model.Schema;
import main.java.thirdParty.acsdb.tools.joinGraphTraversal.ranking.KNN.PointComparable;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.*;

public class JGIMapper {
	
	public JGIMapper(){}
	
	public Set<List<String>> getKRankings(List<String> schema){
		
		AcsdbToolsManager manager = null;
		try {
			manager = AcsdbToolsManager.getInstance();
		} catch (ClassNotFoundException | FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Map<String, TreeSet<PointComparable>> result = null;
		try {
			result = manager.getJoinGraphTool()
					.getKRankings(new Schema(schema.toArray(new String[0])));
		} catch (FileNotFoundException | UnsupportedEncodingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
        Set<List<String>> mappedResult = new HashSet<>();
        
        for(Map.Entry<String,TreeSet<PointComparable>> entry : result.entrySet()){
	       	for(PointComparable pc : entry.getValue()){
        		mappedResult.add(pc.getPoint().getSchema().toList());
        	}
        }

       
        return mappedResult;
    }
	
}