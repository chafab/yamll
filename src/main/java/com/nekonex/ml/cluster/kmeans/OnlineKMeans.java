package com.nekonex.ml.cluster.kmeans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.nekonex.ml.cluster.IClusterAlgorithm;
import com.nekonex.ml.cluster.kdtree.KdTree;
import com.nekonex.ml.data.DistanceComputer;
import com.nekonex.ml.data.IDataPoint;

import java.util.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class OnlineKMeans implements IClusterAlgorithm {
    private final KmeansConfig _config;
    private List<OnlineKMeansNode> list = new ArrayList<>();
    public OnlineKMeans(KmeansConfig config) {
        if (config == null)
            throw new NullPointerException();
        _config = config;
    }


    @JsonCreator
    public OnlineKMeans(@JsonProperty("config") KmeansConfig config, @JsonProperty("nodelist") List<OnlineKMeansNode> list) {
        this._config = config;
        this.list = list;
    }
    @Override
    public KdTree buildCluster(List<IDataPoint> dataPoints) throws Exception {
        Random rand = new Random(0);
        while (dataPoints.size() > 0) {
            if (list.size() < _config.getNumCluster()) {
                OnlineKMeansNode node = new OnlineKMeansNode(_config);
                list.add(node);


                int rndDataPnt = rand.nextInt(dataPoints.size());
                node.addDataPoint(dataPoints.get(rndDataPnt));
                dataPoints.remove(rndDataPnt);
            }
            else {
                //Poor performance algo but ok at build time for our use case
                int rndDataPnt = rand.nextInt(dataPoints.size());
                OnlineKMeansNode nearestNode = null;
                double min_distance = Double.MAX_VALUE;
                for (OnlineKMeansNode node : list) {
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
        return new KdTree(list.stream().map(OnlineKMeansNode::clone).toList(), _config.getDistanceType());
    }

    @JsonProperty("config")
    public KmeansConfig getConfig() {
        return _config;
    }

    @JsonProperty("nodelist")
    public List<OnlineKMeansNode> getListNode() {
        return list;
    }
}