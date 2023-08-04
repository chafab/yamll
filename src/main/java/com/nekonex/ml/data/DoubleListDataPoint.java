package com.nekonex.ml.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.nekonex.ml.cluster.kmeans.OnlineKmeanConfig;
import com.nekonex.ml.exceptions.DataPointsIncompatibleException;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class DoubleListDataPoint  implements IDataPoint {
    List<Double> _data;


    @JsonCreator
    public DoubleListDataPoint(@JsonProperty("data") List<Double> data) {
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

    @JsonIgnore
    @Override
    public int getNumAxis() {
        return _data.size();
    }

    @Override
    public IDataPoint clone() {
        return new DoubleListDataPoint(_data.stream().toList());
    }


    @JsonProperty("data")
    public List<Double> getData() {
        return _data;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof DoubleListDataPoint)) {
            return false;
        }

        DoubleListDataPoint lstDataPt = (DoubleListDataPoint) obj;
        if (lstDataPt.getData().size() != _data.size() )
            return false;
        for (int i = 0; i< _data.size(); ++i) {
            if (Math.abs(_data.get(i) - lstDataPt.getData().get(i)) > 0.0001)
                return false;
        }
        return true;
    }
}
