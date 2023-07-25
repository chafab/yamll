package com.nekonex.ml.signals;
public interface ISignalFactory<T> {
    ISignal<T> getOrCreateByObject(Object obj);
}
