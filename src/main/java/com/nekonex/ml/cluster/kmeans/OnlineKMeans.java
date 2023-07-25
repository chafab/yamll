package com.nekonex.ml.cluster.kmeans;

import com.nekonex.ml.cluster.IClusterAlgorithm;
import com.nekonex.ml.cluster.IClusterNode;
import com.nekonex.ml.data.DistanceComputer;
import com.nekonex.ml.data.IDataPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OnlineKMeans implements IClusterAlgorithm {
    IOnlineKMeanNodeFactory _KMeanNodefactory = null;
    OnlineKmeanConfig _config;
    @Override
    public void buildCuster(List<IDataPoint> dataPoints, int num_cluster) throws Exception {
        List<OnlineKMeanNode> list = new ArrayList<>();
        Random rand = new Random(0);
        while (dataPoints.size() > 0) {
            if (num_cluster < list.size()) {
                OnlineKMeanNode node = _KMeanNodefactory.create(_config);
                list.add(node);


                int rndDataPnt = rand.nextInt(dataPoints.size());
                node.addDataPoint(dataPoints.get(rndDataPnt));
                dataPoints.remove(rndDataPnt);
            }
            else {
                //Poor performance algo but ok at build time for our use case
                int rndDataPnt = rand.nextInt(dataPoints.size());
                OnlineKMeanNode nearestNode = null;
                double min_distance = Double.MAX_VALUE;
                for (OnlineKMeanNode node : list) {
                    double distance =
                            DistanceComputer.computeDistance(node.getPoint(), dataPoints.get(rndDataPnt), DistanceComputer.DistanceType.Euclidean);
                    if (nearestNode == null || distance < min_distance) {
                        nearestNode = node;
                        min_distance = distance;
                    }
                }
                nearestNode.addDataPoint(dataPoints.get(rndDataPnt));
                dataPoints.remove(rndDataPnt);
            }
        }
    }

    @Override
    public synchronized IClusterNode getClusterNode(IDataPoint points) {
        return null;
    }
}