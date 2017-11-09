package main.java.database;

import java.util.List;
import java.util.Map;

/**
 * Created by Andrea on 07/11/2017.
 */
public abstract class RecommendRepository {

    public abstract Map<String, Map<String,Double>> getModulesByTags(List<String> tags);

}
