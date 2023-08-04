package com.nekonex.ml.cluster.kdtree;

import com.fasterxml.jackson.annotation.*;
import com.nekonex.ml.cluster.IClusterNode;
import com.nekonex.ml.data.IDataPoint;

import java.util.List;

@JsonIdentityInfo(generator = ObjectIdGenerators.UUIDGenerator.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class KdNode {
    public final IClusterNode clusterNode;

    public final int depth;
    public final int axisIndex;

    @JsonBackReference
    private KdNode parentNode;
    private KdNode leftNode;
    private KdNode rightNode;

    @JsonCreator
    public KdNode(@JsonProperty("clusterNode") final IClusterNode clusterNode,
                  @JsonProperty("depth") final int depth,
                  @JsonProperty("axisIndex") final int axisIndex,
                  @JsonProperty("parentNode") final KdNode parentNode,
                  @JsonProperty("leftNode") final KdNode leftNode,
                  @JsonProperty("rightNode") final KdNode rightNode) {
        this.clusterNode = clusterNode;
        this.depth = depth;
        this.axisIndex = axisIndex;
        this.parentNode = parentNode;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public KdNode(final IClusterNode clusterNode, final int depth, final int axisIndex) {
        this.clusterNode = clusterNode;
        this.depth = depth;
        this.axisIndex = axisIndex;
    }

    @JsonProperty("parentNode")
    public KdNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(final KdNode parentNode) {
        this.parentNode = parentNode;
    }

    @JsonProperty("leftNode")
    public KdNode getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(final KdNode leftNode) {
        this.leftNode = leftNode;
    }

    @JsonProperty("rightNode")
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

    @JsonIgnore
    public IDataPoint getClusterPoint() {
        return clusterNode.getPoint();
    }

    @JsonProperty("depth")
    public int getDepth() {
        return depth;
    }
    @JsonProperty("clusterNode")
    public IClusterNode getClusterNode() {
        return clusterNode;
    }

    @JsonProperty("axisIndex")
    public int getAxisIndex() {
        return axisIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof KdNode)) {
            return false;
        }

        KdNode node = (KdNode) obj;
        return this.clusterNode.equals(node.getClusterNode()) &&
                this.depth == node.getDepth() &&
                this.axisIndex == node.getAxisIndex() &&
                this.leftNode != null ? this.leftNode.equals(node.getLeftNode()) : node.getLeftNode() == null &&
                this.rightNode != null ? this.rightNode.equals(node.getRightNode()) : node.getRightNode() == null;
    }
}