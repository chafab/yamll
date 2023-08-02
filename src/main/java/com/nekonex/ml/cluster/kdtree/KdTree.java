package com.nekonex.ml.cluster.kdtree;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nekonex.ml.cluster.IClusterNode;
import com.nekonex.ml.data.DistanceComputer;
import com.nekonex.ml.data.IDataPoint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nekonex.ml.exceptions.InvalidNodesException;



@JsonDeserialize(using = KdTreeDeserializer.class)
public class KdTree {
    private static final Random random = new Random(0);

    public final int dimensionCount;

    public final KdNode rootNode;

    private DistanceComputer.DistanceType _distType;

    public KdTree(List<? extends IClusterNode> nodes, DistanceComputer.DistanceType distType) {
        _distType = distType;
        if (nodes == null || nodes.isEmpty() || nodes.get(0).getPoint() == null) {
            throw new InvalidNodesException();
        }

        this.dimensionCount = nodes.get(0).getPoint().getNumAxis();
        if (this.dimensionCount <= 0)
            throw new InvalidNodesException();
        for (IClusterNode node : nodes) {
            if (node.getPoint() == null || node.getPoint().getNumAxis() != this.dimensionCount)
                throw new InvalidNodesException();
            for (int i = 0; i < dimensionCount; ++i)
                if (node.getPoint().getCoordinate(i) == null)
                    throw new InvalidNodesException();
        }

        this.rootNode = buildNode(null, nodes, 0);
    }
    public int getAxisIndex(final int depth) {
        return depth % dimensionCount;
    }

    private KdNode buildNode(final KdNode parentNode, final List<? extends IClusterNode> points, final int depth) {
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
    private IClusterNode getMedianPoint(final List<? extends IClusterNode> points, final int axisIndex) {

        sortByAxisIndex(points, axisIndex);

        return points.get(points.size() / 2);
    }

    private final void sortByAxisIndex(final List<? extends IClusterNode> points, final int axisIndex) {
        points.sort(Comparator.comparing(point -> (point.getPoint().getCoordinate(axisIndex))));
    };

    public synchronized IDataPoint getClusterPoint(IDataPoint point) {
        return findLeaf(rootNode, point).getClusterPoint();
    }

    private KdNode findLeaf(KdNode node, IDataPoint point) {
        KdNode bestNode = null;
        double curDistance = DistanceComputer.computeDistance(point, node.getClusterPoint(), _distType);
        if (node.hasChildren()) {
            if (node.numberOfChildren() == 1) {
                if (node.hasLeftNode()) {
                    bestNode = findLeaf(node.getLeftNode(), point);
                } else {
                    bestNode = findLeaf(node.getRightNode(), point);
                }
            } else {
                final Comparable searchValue = point.getCoordinate(node.axisIndex);
                final Comparable nodeValue = node.getClusterPoint().getCoordinate(node.axisIndex);

                KdNode rightBestNode = null;
                KdNode leftBestNode = null;
                double rightDistance;
                double leftDistance;
                if (searchValue.compareTo(nodeValue) > 0) {
                    rightBestNode = findLeaf(node.getRightNode(), point);
                    rightDistance = DistanceComputer.computeDistance(point, rightBestNode.getClusterPoint(), _distType);
                    leftDistance = Double.MAX_VALUE;
                    leftBestNode = null;
                    if (rightDistance > curDistance) // intersection with the plane need to look on the other side
                    {
                        leftBestNode = findLeaf(node.getLeftNode(), point);
                        leftDistance = DistanceComputer.computeDistance(point, leftBestNode.getClusterPoint(), _distType);
                    }

                } else {
                    leftBestNode = findLeaf(node.getLeftNode(), point);
                    leftDistance = DistanceComputer.computeDistance(point, leftBestNode.getClusterPoint(), _distType);
                    rightDistance = Double.MAX_VALUE;
                    if (leftDistance > curDistance) // intersection with the plane need to look on the other side
                    {
                        rightBestNode = findLeaf(node.getRightNode(), point);
                        rightDistance = DistanceComputer.computeDistance(point, rightBestNode.getClusterPoint(), _distType);
                    }
                }

                if (rightDistance < leftDistance) {
                    bestNode = rightBestNode;
                }
                else bestNode = leftBestNode;
            }
        } else {
            // No children. We reached a leaf node, return it.
            return node;
        }
        double bestNodeDistance = DistanceComputer.computeDistance(point, bestNode.getClusterPoint(), _distType);
        if (bestNodeDistance < curDistance)
            return bestNode;
        return node;
    }

    public String saveAsJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    public static KdTree load(String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, KdTree.class);
    }

}