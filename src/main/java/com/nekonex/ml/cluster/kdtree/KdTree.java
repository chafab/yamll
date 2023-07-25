package com.nekonex.ml.cluster.kdtree;

import com.nekonex.ml.cluster.IClusterNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class KdTree {
    private static final Random random = new Random(0);

    public final int dimensionCount;

    public final KdNode rootNode;

    public KdTree(final List<IClusterNode> nodes) throws Exception {
        // Make sure at least one point was provided.

        if (nodes == null || nodes.isEmpty()) {
            throw new Exception();
        }

        this.dimensionCount = nodes.get(0).getPoint().getNumAxis();

        this.rootNode = buildNode(null, nodes, 0);
    }
    public int getAxisIndex(final int depth) {
        return depth % dimensionCount;
    }

    private KdNode buildNode(final KdNode parentNode, final List<IClusterNode> points, final int depth) {
        if (points.isEmpty()) {
            return null;
        }

        final int axisIndex = getAxisIndex(depth);

        final IClusterNode medianPoint = getMedianPoint(points, axisIndex);

        final KdNode newNode = new KdNode(medianPoint, depth, axisIndex);

        final List<IClusterNode> leftOfMedian = new ArrayList<>(points.size() / 2);
        final List<IClusterNode> rightOfMedian = new ArrayList<>(points.size() / 2);

        for (final IClusterNode point : points) {
            if (point == medianPoint) {
                continue;
            }

            if (point.getPoint().getCoordinate(axisIndex).compareTo(medianPoint.getPoint().getCoordinate(axisIndex)) > 0) {
                rightOfMedian.add(point);
            } else {
                leftOfMedian.add(point);
            }
        }

        final KdNode leftNode = buildNode(newNode, leftOfMedian, depth + 1);
        final KdNode rightNode = buildNode(newNode, rightOfMedian, depth + 1);

        newNode.setLeftNode(leftNode);
        newNode.setRightNode(rightNode);
        newNode.setParentNode(parentNode);

        return newNode;
    }
    private IClusterNode getMedianPoint(final List<IClusterNode> points, final int axisIndex) {

        sortByAxisIndex(points, axisIndex);

        return points.get(points.size() / 2);
    }

    private final void sortByAxisIndex(final List<IClusterNode> points, final int axisIndex) {
        points.sort(Comparator.comparing(point -> (point.getPoint().getCoordinate(axisIndex))));
    };


}