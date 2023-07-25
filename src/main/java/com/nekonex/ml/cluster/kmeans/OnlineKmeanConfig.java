package com.nekonex.ml.cluster.kmeans;

public class OnlineKmeanConfig {
    private String _aggregatorType;
    private double _coefficient_decay = 0.99;
    private double _min_coefficient = 0.0001;

    private double _initial_coefficient = 0.01;
    public double getCoefficientDecay() {
        return _coefficient_decay;
    }

    public void setCoefficientDecay(double _coefficient_decay) {
        this._coefficient_decay = _coefficient_decay;
    }


    public double getMinCoefficient() {
        return _min_coefficient;
    }

    public void setMinCoefficient(double _minCoefficient) {
        this._min_coefficient = _minCoefficient;
    }

    public double getInitialCoefficient() {
        return _initial_coefficient;
    }

    public void setInitialCoefficient(double _initial_coefficient) {
        this._initial_coefficient = _initial_coefficient;
    }

}
