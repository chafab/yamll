package com.nekonex.ml.signals;

public class DummyDoubleSignal implements ISignal<Double> {

    private double _value;
    @Override
    public Double getValue() {
        return null;
    }

    public void setValue(double value) {
        _value = value;
    }
}
