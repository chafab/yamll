package com.nekonex.ml.data;

import com.nekonex.ml.exceptions.DataPointsIncompatibleException;

public class DistanceComputer {
    public enum DistanceType {
        Euclidean
    }
    public static double computeDistance(IDataPoint p1, IDataPoint p2, DistanceType distType) {
        if (p1.getNumAxis() != p2.getNumAxis())
            throw new DataPointsIncompatibleException();
        if (distType == DistanceType.Euclidean) {
            return computeEuclideanDistance(p1, p2);
        }
        throw new UnsupportedOperationException();
    }

    private static double computeEuclideanDistance(IDataPoint p1, IDataPoint p2) {
        double result = 0;
        for (int i = 0; i < p1.getNumAxis(); ++i) {
            Object ax1 = p1.getCoordinate(i);
            Object ax2 = p1.getCoordinate(i);
            if (ax1.getClass() != ax2.getClass())
                throw new DataPointsIncompatibleException();
            if (ax1.getClass() != Double.class) //Very limited type support
                throw new DataPointsIncompatibleException();
            result += Math.pow((Double) ax1 - (Double) ax2, 2);
        }
        return Math.sqrt(result);
    }
}
