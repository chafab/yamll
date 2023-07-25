package com.nekonex.ml.cluster.kdtree;

import com.nekonex.ml.cluster.IClusterNode;

import java.util.List;

public class KdNode {
    public final IClusterNode point;

    public final int depth;
    public final int axisIndex;

    private KdNode parentNode;
    private KdNode leftNode;
    private KdNode rightNode;

    public KdNode(final IClusterNode point, final int depth, final int axisIndex) {
        this.point = point;
        this.depth = depth;
        this.axisIndex = axisIndex;
    }

    public KdNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(final KdNode parentNode) {
        this.parentNode = parentNode;
    }

    public KdNode getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(final KdNode leftNode) {
        this.leftNode = leftNode;
    }

    public KdNode getRightNode() {
        return rightNode;
    }

    public void setRightNode(final KdNode rightNode) {
        this.rightNode = rightNode;
    }

    public boolean hasParentNode() {
        return parentNode != null;
    }

    public boolean hasLeftNode() {
        return leftNode != null;
    }

    public boolean hasRightNode() {
        return rightNode != null;
    }

    public boolean hasChildren() {
        return leftNode != null || rightNode != null;
    }

    public int numberOfChildren() {
        return (leftNode != null ? 1 : 0) + (rightNode != null ? 1 : 0);
    }
}