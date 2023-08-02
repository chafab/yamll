package com.nekonex.ml.cluster.kdtree.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class JsonKdTreeTestHelper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static JsonNode parseAndRemoveIds(String json) throws IOException {
        JsonNode node = objectMapper.readTree(json);
        removeIds(node);
        return node;
    }

    private static void removeIds(JsonNode node) {
        if (node.isObject()) {
            ObjectNode objectNode = (ObjectNode) node;
            objectNode.remove("@id");
            objectNode.elements().forEachRemaining(JsonKdTreeTestHelper::removeIds);
        } else if (node.isArray()) {
            node.elements().forEachRemaining(JsonKdTreeTestHelper::removeIds);
        }
    }
}
