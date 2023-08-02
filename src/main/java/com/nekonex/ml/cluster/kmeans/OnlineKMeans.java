package com.nekonex.ml.cluster.kmeans;

import com.nekonex.ml.cluster.IClusterAlgorithm;
import com.nekonex.ml.cluster.kdtree.KdTree;
import com.nekonex.ml.data.DistanceComputer;
import com.nekonex.ml.data.IDataPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OnlineKMeans implements IClusterAlgorithm {
    OnlineKmeanConfig _config;
    List<OnlineKMeanNode> list = new ArrayList<>();
    public OnlineKMeans(OnlineKmeanConfig config) {
        _config = config;
    }
    @Override
    public KdTree buildCluster(List<IDataPoint> dataPoints) throws Exception {
        Random rand = new Random(0);
        while (dataPoints.size() > 0) {
            if (_config.getNumCluster() < list.size()) {
                OnlineKMeanNode node = new OnlineKMeanNode(_config);
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
                            DistanceComputer.computeDistance(node.getPoint(), dataPoints.get(rndDataPnt), _config.getDistanceType());
                    if (nearestNode == null || distance < min_distance) {
                        nearestNode = node;
                        min_distance = distance;
                    }
                }
                nearestNode.addDataPoint(dataPoints.get(rndDataPnt));
                dataPoints.remove(rndDataPnt);
            }
        }
        return new KdTree(list.stream().map(OnlineKMeanNode::clone).toList(), _config.getDistanceType());
    }


}