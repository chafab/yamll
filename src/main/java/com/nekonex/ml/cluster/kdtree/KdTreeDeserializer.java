package com.nekonex.ml.cluster.kdtree;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class KdTreeDeserializer extends JsonDeserializer<KdTree> {

    @Override
    public KdTree deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        int dimensionCount = node.get("dimensionCount").asInt();

        JsonNode rootNodeJson = node.get("rootNode");
        JsonNode leftNode = node.get("leftNode");
        JsonNode rightNode = node.get("rightNode");
        /*KdNode rootNode = new KdNode();
        // Assuming KdNode has a setter method for id
        rootNode.setId(rootNodeJson.get("@id").asText());
        // Add more setters here to set other properties of the root node

        KdTree kdTree = new KdTree();
        kdTree.setDimensionCount(dimensionCount);
        kdTree.setRootNode(rootNode);

        return kdTree;*/
        return null;
    }
}