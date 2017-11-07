package main.java.recommending;

import main.java.database.RecommendRepository;

/**
 * Created by Andrea on 07/11/2017.
 */
public abstract class Recommendation {

    protected RecommendRepository repository;
    protected String name;

    public Recommendation(RecommendRepository repo, String name){
        this.repository = repo;
        this.name = name;
    }

    public abstract String makeRecommendation();
}
