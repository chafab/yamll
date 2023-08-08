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
public class KMeans implements IClusterAlgorithm {
    private final KmeansConfig _config;
    private List<OnlineKMeansNode> list = new ArrayList<>();
    public KMeans(KmeansConfig config) {
        if (config == null)
            throw new NullPointerException();
        _config = config;
    }


    @JsonCreator
    public KMeans(@JsonProperty("config") KmeansConfig config, @JsonProperty("nodelist") List<OnlineKMeansNode> list) {
        this._config = config;
        this.list = list;
    }
    @Override
    public KdTree buildCluster(List<IDataPoint> dataPoints) throws Exception {
        HashMap<OnlineKMeansNode, List<IDataPoint>> mapDataPointsByNode = new HashMap<>();
        Collections.shuffle(dataPoints);
        for (int i = 0; i < _config.getMinIter() ; ++i) {
            for (IDataPoint point : dataPoints) {
                if (list.size() < _config.getNumCluster()) {
                    OnlineKMeansNode node = new OnlineKMeansNode(_config);
                    list.add(node);
                    node.addDataPoint(point);
                    mapDataPointsByNode.put(node, new ArrayList<>());
                }
                else  {
                    OnlineKMeansNode nearestNode = null;
                    double min_distance = Double.MAX_VALUE;
                    for (OnlineKMeansNode node : mapDataPointsByNode.keySet()) {
                        double distance =
                                DistanceComputer.computeDistance(node.getPoint(), point, _config.getDistanceType());
                        if (nearestNode == null || distance < min_distance) {
                            nearestNode = node;
                            min_distance = distance;
                        }
                    }
                    List<IDataPoint> pts = mapDataPointsByNode.get(nearestNode);
                    pts.add(point);
                    mapDataPointsByNode.put(nearestNode, pts);
                }
            }
            for (Map.Entry<OnlineKMeansNode, List<IDataPoint>> pair : mapDataPointsByNode.entrySet()) {
                pair.getKey().getCoordinates().center(pair.getValue());
                pair.getValue().clear();
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
