package  main.java.thirdParty.acsdb.tools.joinGraphTraversal;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.*;
import  main.java.thirdParty.acsdb.AcsdbRepo;
import  main.java.thirdParty.acsdb.model.Schema;
import  main.java.thirdParty.acsdb.tools.joinGraphTraversal.clustering.AcsdbPoint;
import  main.java.thirdParty.acsdb.tools.joinGraphTraversal.clustering.DistanceFunction;
import  main.java.thirdParty.acsdb.tools.joinGraphTraversal.clustering.NeighborSim;
import main.java.thirdParty.acsdb.tools.joinGraphTraversal.ranking.KNN.KNearestNeighbor;
import main.java.thirdParty.acsdb.tools.joinGraphTraversal.ranking.KNN.PointComparable;


/**
 * Created by Andrea on 23/07/2017.
 */
public class JoinGraph {

    private AcsdbRepo acsdbRepo;

    public JoinGraph(AcsdbRepo acsdbRepo){
        this.acsdbRepo = acsdbRepo;
    }

    public Map<String, TreeSet<PointComparable>> getKRankings(Schema schema) throws FileNotFoundException, UnsupportedEncodingException, SQLException {

        Map<String, TreeSet<PointComparable>> joinAttr2Schema = new HashMap<>();

        NeighborSim df = new NeighborSim(acsdbRepo);

        for(String head : schema.toList()){

            List<Schema> partialDb = acsdbRepo.getSchemasWithAttrib(head);
            df.setAttrib(head);
            df.setDataSize(partialDb.size());
            joinAttr2Schema.put(head, runRankingAlgorithm(partialDb, df, schema , 5));
        }

        return joinAttr2Schema;
    }

    private TreeSet<PointComparable> runRankingAlgorithm(List<Schema> dataset, DistanceFunction df, Schema schema, int k) {

        List<AcsdbPoint> points = new LinkedList<>();

        for (Schema s : dataset) {
            AcsdbPoint newPoint = new AcsdbPoint();
            newPoint.setSchema(s);
            points.add(newPoint);
        }

        return new KNearestNeighbor(k).getNearestKNeighbor(points, schema, df);

    }



}