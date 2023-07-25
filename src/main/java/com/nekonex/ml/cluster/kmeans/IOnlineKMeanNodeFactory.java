package com.nekonex.ml.cluster.kmeans;

public interface IOnlineKMeanNodeFactory {
    OnlineKMeanNode create(OnlineKmeanConfig config) throws Exception;
}
