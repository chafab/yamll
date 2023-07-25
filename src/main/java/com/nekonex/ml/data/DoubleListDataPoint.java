package com.nekonex.ml.data;

import com.nekonex.ml.exceptions.DataPointsIncompatibleException;

import java.util.List;

public class DoubleListDataPoint  implements IDataPoint {
    List<Double> _data;

    DoubleListDataPoint(List<Double> data) {
        this._data = data;
    }
    @Override
    public void moveTo(IDataPoint target_point, double coeff) {
        if (target_point == null ||
            target_point.getClass() != DoubleListDataPoint.class ||
            target_point.getNumAxis() != this.getNumAxis()) {
            throw new DataPointsIncompatibleException();
        }
        DoubleListDataPoint tgt = (DoubleListDataPoint) target_point;
        for (int i = 0; i < this.getNumAxis(); ++i) {
            _data.set(i, _data.get(i)*(1-coeff) + coeff*(Double)tgt.getCoordinate(i));
        }
    }

    @Override
    public Comparable getCoordinate(int axisNum) {
        return _data.get(axisNum);
    }

    @Override
    public int getNumAxis() {
        return _data.size();
    }

    @Override
    public IDataPoint cloneDataPoint() {
        return new DoubleListDataPoint(_data.stream().toList());
    }
}
