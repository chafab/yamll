package com.nekonex.ml.cluster.kmeans;

import com.nekonex.ml.cluster.IClusterNode;
import com.nekonex.ml.data.IDataPoint;
import com.nekonex.ml.exceptions.DataPointsIncompatibleException;

public class OnlineKMeanNode implements IClusterNode{
    private IDataPoint _coordinates = null;
    OnlineKmeanConfig _config;

    private double _coefficient = 0.1;
    private long _count = 0;
    public OnlineKMeanNode(OnlineKmeanConfig config) {
        _config = config;
        _coefficient = _config.getInitialCoefficient();
    }
    @Override
    public synchronized void addDataPoint(IDataPoint data_point) {
        if (_coordinates == null) {
            _coordinates = data_point.clone();
        }
        else
        {
            if (_coordinates.getNumAxis() != data_point.getNumAxis())
                throw new DataPointsIncompatibleException();
            _coordinates.moveTo(data_point, _coefficient);
            _coefficient = Math.min(_config.getMinCoefficient(), _coefficient * _config.getCoefficientDecay());
        }
        ++_count;

    }

    @Override
    public synchronized long count() {
        return _count;
    }

    @Override
    public IDataPoint getPoint() {
        return _coordinates;
    }

    @Override
    public IClusterNode clone() {
        OnlineKMeanNode clone = new OnlineKMeanNode(_config);
        clone.setCoordinates(this._coordinates.clone());
        clone.setCoefficient(_coefficient);
        clone.setCount(_count);
        return clone;
    }

    public synchronized void setCoordinates(IDataPoint value) {
        _coordinates = value;
    }

    public synchronized double getCoefficient() {
        return _coefficient;
    }

    public synchronized void setCoefficient(double value) {
        _coefficient = value;
    }

    public synchronized  void setCount(long value) { _count = value;}
}
