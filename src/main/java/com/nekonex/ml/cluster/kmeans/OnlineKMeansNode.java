package com.nekonex.ml.cluster.kmeans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.nekonex.ml.cluster.IClusterNode;
import com.nekonex.ml.data.IDataPoint;
import com.nekonex.ml.exceptions.DataPointsIncompatibleException;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class OnlineKMeansNode implements IClusterNode{
    private IDataPoint _coordinates = null;
    KmeansConfig _config;

    private double _coefficient = 0.1;
    private long _count = 0;


    @JsonCreator
    public OnlineKMeansNode(@JsonProperty("config") KmeansConfig config,
                            @JsonProperty("coordinates") IDataPoint coordinates,
                            @JsonProperty("coefficient") double coefficient,
                            @JsonProperty("count") long count) {
        _config = config;
        _coordinates = coordinates;
        _coefficient = coefficient;
        _count = count;
    }
    public OnlineKMeansNode(KmeansConfig config) {
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

    @JsonProperty("count")
    @Override
    public synchronized long count() {
        return _count;
    }

    @JsonIgnore
    @Override
    public IDataPoint getPoint() {
        return _coordinates;
    }

    @Override
    public IClusterNode clone() {
        OnlineKMeansNode clone = new OnlineKMeansNode(_config);
        clone.setCoordinates(this._coordinates.clone());
        clone.setCoefficient(_coefficient);
        clone.setCount(_count);
        return clone;
    }

    public synchronized void setCoordinates(IDataPoint value) {
        _coordinates = value;
    }

    @JsonProperty("coefficient")
    public synchronized double getCoefficient() {
        return _coefficient;
    }

    public synchronized void setCoefficient(double value) {
        _coefficient = value;
    }

    public synchronized  void setCount(long value) { _count = value;}

    @JsonProperty("coordinates")
    public IDataPoint getCoordinates() {
        return _coordinates;
    }

    @JsonProperty("config")
    public KmeansConfig getConfig() {
        return _config;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof OnlineKMeansNode)) {
            return false;
        }

        OnlineKMeansNode node = (OnlineKMeansNode) obj;
        return this._coordinates.equals(node.getCoordinates()) &&
                this._config.equals(node.getConfig()) &&
                this._coefficient == node.getCoefficient() &&
                this._count == node.count();
    }
}
