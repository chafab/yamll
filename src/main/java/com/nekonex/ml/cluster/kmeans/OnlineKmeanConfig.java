package com.nekonex.ml.cluster.kmeans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nekonex.ml.data.DistanceComputer;

public class OnlineKmeanConfig {
    private double _coefficient_decay = 0.99;
    private double _min_coefficient = 0.0001;

    private double _initial_coefficient = 0.01;
    private int _numCluster = 0;
    private DistanceComputer.DistanceType _distanceType = DistanceComputer.DistanceType.Euclidean;

    @JsonProperty("coefficientDecay")
    public double getCoefficientDecay() {
        return _coefficient_decay;
    }

    public void setCoefficientDecay(double _coefficient_decay) {
        this._coefficient_decay = _coefficient_decay;
    }

    @JsonProperty("minCoefficient")
    public double getMinCoefficient() {
        return _min_coefficient;
    }

    public void setMinCoefficient(double _minCoefficient) {
        this._min_coefficient = _minCoefficient;
    }

    @JsonProperty("initialCoefficient")
    public double getInitialCoefficient() {
        return _initial_coefficient;
    }

    public void setInitialCoefficient(double _initial_coefficient) {
        this._initial_coefficient = _initial_coefficient;
    }

    @JsonProperty("numCluster")
    public int getNumCluster() {
        return _numCluster;
    }

    public void setNumCluster(int _numCluster) {
        this._numCluster = _numCluster;
    }

    @JsonProperty("distanceType")
    public DistanceComputer.DistanceType getDistanceType() {
        return _distanceType;
    }

    public void setDistanceType(DistanceComputer.DistanceType distanceType) {
        this._distanceType = distanceType;
    }

    @JsonCreator
    public OnlineKmeanConfig(@JsonProperty("coefficientDecay") double coefficient_decay,
                             @JsonProperty("minCoefficient") double min_coefficient,
                             @JsonProperty("initialCoefficient") double initial_coefficient,
                             @JsonProperty("numCluster") int numCluster,
                             @JsonProperty("distanceType") DistanceComputer.DistanceType distanceType) {
        this._coefficient_decay = coefficient_decay;
        this._min_coefficient = min_coefficient;
        this._initial_coefficient = initial_coefficient;
        this._numCluster = numCluster;
        this._distanceType = distanceType;
    }

    public OnlineKmeanConfig()
    {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof OnlineKmeanConfig)) {
            return false;
        }

        OnlineKmeanConfig cfg = (OnlineKmeanConfig) obj;
        return this._coefficient_decay == cfg.getCoefficientDecay() &&
                this._min_coefficient == cfg.getMinCoefficient() &&
                this._initial_coefficient == cfg.getInitialCoefficient() &&
                this._numCluster == cfg.getNumCluster() &&
                this._distanceType == cfg.getDistanceType();
    }

}
