package main.java.recommending;

import main.java.ModuleSubscription;
import main.java.database.RecommendRepository;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrea on 07/11/2017.
 */
public class ModuleRecommendation extends Recommendation{

    public ModuleRecommendation(RecommendRepository repo, String name) {
        super(repo, name);
    }

    public List<String> getBestModulesByPhase(ModuleSubscription.PIPELINE_STEPS step){
        return new LinkedList<>();
    }

    @Override
    public String makeRecommendation() {
        return null;
    }
}
