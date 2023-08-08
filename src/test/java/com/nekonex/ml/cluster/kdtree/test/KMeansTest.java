package com.nekonex.ml.cluster.kdtree.test;

import com.nekonex.ml.cluster.IClusterAlgorithm;
import com.nekonex.ml.cluster.kdtree.KdTree;
import com.nekonex.ml.cluster.kmeans.KMeans;
import com.nekonex.ml.cluster.kmeans.OnlineKMeans;
import com.nekonex.ml.cluster.kmeans.KmeansConfig;
import com.nekonex.ml.data.DoubleListDataPoint;
import com.nekonex.ml.data.IDataPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class KMeansTest {

    private void testKMeansAlgorithm(IClusterAlgorithm algo) throws Exception {
        Random rand = new Random();
        Double[] array = {0.0,0.0,1.0,1.0,0.0,1.0,1.0,1.0,1.0,0.0,1.0,1.0,0.0,1.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0,1.0,1.0,0.0,1.0,1.0,0.0,1.0,0.0,1.0,1.0,0.0,0.0,1.0,1.0,0.0,0.0,1.0,0.0,1.0,1.0,0.0,1.0,1.0,0.0,1.0,1.0,1.0,1.0,1.0,1.0,0.0,0.0,1.0,0.0,0.0,1.0,0.0,1.0,0.0,1.0,1.0,1.0,1.0,0.0,1.0,1.0,0.0,1.0,1.0,1.0,0.0,0.0,1.0,0.0,0.0,1.0,1.0,1.0,0.0,1.0,1.0,1.0,0.0,1.0,1.0,0.0,0.0,1.0,0.0,0.0,1.0,1.0,0.0,1.0,0.0,1.0,1.0};
        List<IDataPoint> list = new ArrayList<>();
        for (double val : array)
            list.add(new DoubleListDataPoint(List.of(val)));
        KdTree tree = algo.buildCluster(list);
        IDataPoint point = tree.getClusterPoint(new DoubleListDataPoint(List.of(0.0)));
        Assertions.assertTrue((Double)point.getCoordinate(0) < 0.0001);
        point = tree.getClusterPoint(new DoubleListDataPoint(List.of(0.9)));
        Assertions.assertTrue((Double)point.getCoordinate(0) > 0.0009);
    }
    @Test
    void testKmeansInit() throws Exception {
        assertThrows(NullPointerException.class, ()-> { new OnlineKMeans(null);});
        assertThrows(NullPointerException.class, ()-> { new KMeans(null);});
        OnlineKMeans kmeans =  new OnlineKMeans(new KmeansConfig());
        assertThrows(InvalidParameterException.class, ()-> { kmeans.getConfig().setNumCluster(-1);});
        kmeans.getConfig().setNumCluster(2);
        assertThrows(InvalidParameterException.class, ()-> { kmeans.getConfig().setMinCoefficient(1.0);});
        kmeans.getConfig().setInitialCoefficient(1.0);
        assertThrows(InvalidParameterException.class, ()-> { kmeans.getConfig().setMinIter(-1);});
        kmeans.getConfig().setMinIter(10);
    }

    @Test
    void testKmeansConfig() throws Exception {
        KmeansConfig config = new KmeansConfig();
        assertThrows(InvalidParameterException.class, ()-> { config.setNumCluster(-1);});
        config.setNumCluster(2);
        assertThrows(InvalidParameterException.class, ()-> { config.setMinCoefficient(1.0);});
        config.setInitialCoefficient(1.0);
        assertThrows(InvalidParameterException.class, ()-> { config.setMinIter(-1);});
        config.setMinIter(10);
    }

    @Test
    void testOnlineKmeans() throws Exception {
        KmeansConfig config = new KmeansConfig();
        config.setNumCluster(2);
        config.setInitialCoefficient(1.0);
        config.setMinIter(10);
        testKMeansAlgorithm(new OnlineKMeans(config));
    }

    @Test
    void testKmeans() throws Exception {
        KmeansConfig config = new KmeansConfig();
        config.setNumCluster(2);
        config.setInitialCoefficient(1.0);
        config.setMinIter(10);
        testKMeansAlgorithm(new KMeans(config));
    }
}
