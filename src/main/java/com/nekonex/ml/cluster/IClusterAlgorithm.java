package com.nekonex.ml.cluster;

import com.nekonex.ml.data.IDataPoint;

import java.util.List;

public interface IClusterAlgorithm {
    void buildCuster(List<IDataPoint> dataPoints, int num_cluster) throws Exception;
    IClusterNode getClusterNode(IDataPoint points);
}
