package com.nekonex.ml.data;

import com.nekonex.ml.utils.GenericCloneable;

public interface IDataPoint extends GenericCloneable<IDataPoint> {
    //move in the direction of targetPoint
    void moveTo(IDataPoint target_point, double coeff);
    Comparable getCoordinate(int axisNum);
    int getNumAxis();
}
