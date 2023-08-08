package com.nekonex.ml.cluster.kmeans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nekonex.ml.data.DistanceComputer;

import java.security.InvalidParameterException;

public class KmeansConfig {
    private double _coefficient_decay = 0.99;
    private double _min_coefficient = 0.0001;

    private double _initial_coefficient = 0.01;
    private int _numCluster = 1;
    private DistanceComputer.DistanceType _distanceType = DistanceComputer.DistanceType.Euclidean;
    private int _minIter = 10;

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
        if (this._min_coefficient < _initial_coefficient)
            throw new InvalidParameterException();
        this._min_coefficient = _minCoefficient;
    }

    @JsonProperty("initialCoefficient")
    public double getInitialCoefficient() {
        return _initial_coefficient;
    }

    public void setInitialCoefficient(double initial_coefficient) {
        this._initial_coefficient = initial_coefficient;
    }

    @JsonProperty("numCluster")
    public int getNumCluster() {
        return _numCluster;
    }

    public void setNumCluster(int numCluster) {
        if (numCluster < 1)
            throw new InvalidParameterException();
        this._numCluster = numCluster;
    }

    @JsonProperty("distanceType")
    public DistanceComputer.DistanceType getDistanceType() {
        return _distanceType;
    }

    public void setDistanceType(DistanceComputer.DistanceType distanceType) {
        this._distanceType = distanceType;
    }

    @JsonProperty("minIter")
    public int getMinIter() {
        return _minIter;
    }

    public void setMinIter(int minIter) {
        if (minIter < 1)
            throw new InvalidParameterException();
        this._minIter = minIter;
    }

    @JsonCreator
    public KmeansConfig(@JsonProperty("coefficientDecay") double coefficient_decay,
                        @JsonProperty("minCoefficient") double min_coefficient,
                        @JsonProperty("initialCoefficient") double initial_coefficient,
                        @JsonProperty("numCluster") int numCluster,
                        @JsonProperty("minIter") int minIter,
                        @JsonProperty("distanceType") DistanceComputer.DistanceType distanceType
                             ) {
        this._coefficient_decay = coefficient_decay;
        this._min_coefficient = min_coefficient;
        this._initial_coefficient = initial_coefficient;
        this._numCluster = numCluster;
        this._distanceType = distanceType;
        this._minIter = minIter;
    }

    public KmeansConfig()
    {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof KmeansConfig)) {
            return false;
        }

        KmeansConfig cfg = (KmeansConfig) obj;
        return this._coefficient_decay == cfg.getCoefficientDecay() &&
                this._min_coefficient == cfg.getMinCoefficient() &&
                this._initial_coefficient == cfg.getInitialCoefficient() &&
                this._numCluster == cfg.getNumCluster() &&
                this._distanceType == cfg.getDistanceType();
    }

}
