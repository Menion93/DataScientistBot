package main.java.thirdParty.acsdb;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import  main.java.thirdParty.acsdb.adapters.postgres.JdbcAdapter;
import  main.java.thirdParty.acsdb.tools.joinGraphTraversal.JoinGraph;
import  main.java.thirdParty.acsdb.tools.schemaAutocomplete.SchemaAutocomplete;
import main.java.thirdParty.acsdb.tools.synonymFinder.SynFinding;

public class AcsdbToolsManager {
	
	private static AcsdbToolsManager instance;
	
	private JoinGraph joinGraph;
	private SchemaAutocomplete schemaAutocomplete;
	private SynFinding synFinding;
	
	private AcsdbRepo repo;
	
	protected AcsdbToolsManager() throws ClassNotFoundException, FileNotFoundException, UnsupportedEncodingException{
		ConnectionDb connection = new ConnectionDb();
		DBAdapter adapter = new JdbcAdapter(connection);
		AcsdbRepo repo = new AcsdbRepo(adapter);
		
		joinGraph = new JoinGraph(repo);
		schemaAutocomplete = new SchemaAutocomplete(repo);
		synFinding = new SynFinding(repo);
	}
	
	public void initialize() throws FileNotFoundException, UnsupportedEncodingException{		
		repo.getAcsdb1Value();
		repo.getAcsdb2Values();
		repo.getAcsdb3Values();		
	}
	
	public static AcsdbToolsManager getInstance() throws ClassNotFoundException, FileNotFoundException, UnsupportedEncodingException{
		if(instance == null){
			instance = new AcsdbToolsManager();
		}
		return instance;
	}
	
	public JoinGraph getJoinGraphTool(){
		return joinGraph;
	}
	
	public SchemaAutocomplete getSchemaAutocompleteTool(){
		return schemaAutocomplete;
	}
	
	public SynFinding getSynFindingTool(){
		return synFinding;
	}
}
	
