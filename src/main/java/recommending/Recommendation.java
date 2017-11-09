package main.java.recommending;

import main.java.core.DataScienceModuleHandler;
import main.java.database.RecommendRepository;

/**
 * Created by Andrea on 07/11/2017.
 */
public abstract class Recommendation {

    protected RecommendRepository repository;
    protected String name;
    protected DataScienceModuleHandler handler;

    public Recommendation(RecommendRepository repo, String name, DataScienceModuleHandler handler){
        this.repository = repo;
        this.name = name;
        this.handler = handler;
    }

    public abstract String makeRecommendation();

}
