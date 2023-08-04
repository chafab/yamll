package com.nekonex.ml.cluster;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.nekonex.ml.cluster.kdtree.KdTree;
import com.nekonex.ml.data.IDataPoint;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public interface IClusterAlgorithm {
    KdTree buildCluster(List<IDataPoint> dataPoints) throws Exception;
}
