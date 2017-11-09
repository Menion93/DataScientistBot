package main.java.recommending;

import main.java.ModuleSubscription;
import main.java.core.DataScienceModuleHandler;
import main.java.database.RecommendRepository;
import main.java.modules.Module;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Andrea on 07/11/2017.
 */
public class ModuleRecommendation extends Recommendation{

    private int threshold;

    public ModuleRecommendation(RecommendRepository repo, String name, DataScienceModuleHandler handler) {
        super(repo, name, handler);
        threshold = 1;
    }

    public List<Module> getBestModulesByPhase(ModuleSubscription.PIPELINE_STEPS step){

        Map<String, Map<String,Double>> tag2modules = repository.getModulesByTags(handler.getTags());

        Set<String> suggestedModules = new HashSet<>();

        for(Map.Entry<String, Map<String, Double>> entry : tag2modules.entrySet()){

            Map<String,Double> filteredModules = filterModulesByStep(step, entry.getValue());
            Map<String, Double> relevantModules = filterRelevantModules(filteredModules);
            suggestedModules.addAll(selectMostImportantModules(relevantModules));
        }

        return handler.getModuleSubscription().getModuleListByNames(new LinkedList<>(suggestedModules));
    }

    private Map<String,Double> filterRelevantModules(Map<String,Double> relevantModules) {
        Map<String,Double> result = new HashMap<>();

        for(Map.Entry<String, Double> entry : relevantModules.entrySet()){
            if(entry.getValue() >= threshold){
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return result;
    }

    private Map<String,Double> filterModulesByStep(ModuleSubscription.PIPELINE_STEPS step,
                                                   Map<String,Double> module2score) {

        Map<String,Double> filtered = new HashMap<>();
        List<String> allModules = handler.getModuleSubscription().getModuleByStep(step);

        for(Map.Entry<String,Double> entry : module2score.entrySet())
           if(allModules.contains(entry.getKey()))
               filtered.put(entry.getKey(), entry.getValue());

        return filtered;
    }

    /**
     * Find the most 10 used attribute
     */
    private List<String> selectMostImportantModules(Map<String, Double> filteredModules) {
        return filteredModules.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .limit(10)
                .collect(Collectors.toList());
    }




    @Override
    public String makeRecommendation() {
        return null;
    }
}
