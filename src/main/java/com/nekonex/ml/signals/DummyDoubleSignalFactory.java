package com.nekonex.ml.signals;

public class DummyDoubleSignalFactory implements ISignalFactory<Double> {

    @Override
    public ISignal<Double> getOrCreateByObject(Object obj) {
        return new DummyDoubleSignal();
    }
}
