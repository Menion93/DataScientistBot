package main.java.modules.JGTModule;

import main.java.thirdParty.acsdb.AcsdbToolsManager;
import main.java.thirdParty.acsdb.model.Schema;
import main.java.thirdParty.acsdb.tools.joinGraphTraversal.ranking.KNN.PointComparable;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.*;

public class JGTMapper {
	
	public JGTMapper(){}
	
	public Map<String, List<List<String>>> getKRankings(List<String> schema){
		
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
		
		
        Map<String, List<List<String>>> mappedResult = new HashMap<>();
        
        for(Map.Entry<String,TreeSet<PointComparable>> entry : result.entrySet()){
        	List<List<String>> clusterGroup = new LinkedList<>();
        
        	for(PointComparable pc : entry.getValue()){
        		clusterGroup.add(pc.getPoint().getSchema().toList());
        	}
        	mappedResult.put(entry.getKey(), clusterGroup);
        }

       
        return mappedResult;
    }
	
}