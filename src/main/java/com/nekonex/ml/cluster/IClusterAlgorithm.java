package com.nekonex.ml.cluster;

import com.nekonex.ml.cluster.kdtree.KdTree;
import com.nekonex.ml.data.IDataPoint;

import java.util.List;

public interface IClusterAlgorithm {
    KdTree buildCluster(List<IDataPoint> dataPoints) throws Exception;
}
