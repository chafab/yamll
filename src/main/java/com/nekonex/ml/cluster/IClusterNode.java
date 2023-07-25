package com.nekonex.ml.cluster;

import com.nekonex.ml.data.IDataPoint;

import java.util.Collection;
import java.util.List;
public interface IClusterNode {

    void addDataPoint(IDataPoint data_point);
    long count();
    IDataPoint getPoint();
}
