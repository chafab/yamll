package com.nekonex.ml.data;

public interface IDataPoint {
    //move in the direction of targetPoint
    void moveTo(IDataPoint target_point, double coeff);
    Comparable getCoordinate(int axisNum);
    int getNumAxis();
    IDataPoint cloneDataPoint();
}
