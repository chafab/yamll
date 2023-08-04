package com.nekonex.ml.cluster;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.nekonex.ml.data.IDataPoint;
import com.nekonex.ml.utils.GenericCloneable;

import java.util.Collection;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public interface IClusterNode extends GenericCloneable<IClusterNode> {

    void addDataPoint(IDataPoint data_point);
    long count();
    IDataPoint getPoint();
    IClusterNode clone();
}
