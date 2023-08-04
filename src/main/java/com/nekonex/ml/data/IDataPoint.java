package com.nekonex.ml.data;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.nekonex.ml.utils.GenericCloneable;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public interface IDataPoint extends GenericCloneable<IDataPoint> {
    //move in the direction of targetPoint
    void moveTo(IDataPoint target_point, double coeff);
    Comparable getCoordinate(int axisNum);
    int getNumAxis();
}
