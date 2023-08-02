package com.nekonex.ml.cluster.kdtree.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.nekonex.ml.cluster.IClusterNode;
import com.nekonex.ml.cluster.kdtree.KdTree;
import com.nekonex.ml.cluster.kmeans.OnlineKMeanNode;
import com.nekonex.ml.cluster.kmeans.OnlineKmeanConfig;
import com.nekonex.ml.data.DistanceComputer;
import com.nekonex.ml.data.DoubleListDataPoint;
import com.nekonex.ml.data.IDataPoint;
import com.nekonex.ml.exceptions.InvalidNodesException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class KdTreeTest {

    @Test
    void testInit() throws Exception {
        assertThrows(InvalidNodesException.class, ()-> { new KdTree(null, DistanceComputer.DistanceType.Euclidean);});
        assertThrows(InvalidNodesException.class, ()-> { new KdTree(new ArrayList<>(), DistanceComputer.DistanceType.Euclidean);});
        OnlineKmeanConfig config = new OnlineKmeanConfig();
        OnlineKMeanNode node = new OnlineKMeanNode(config);
        List<IClusterNode> list = new ArrayList<>();
        list.add(node);
        //Node does not countain any point
        assertThrows(InvalidNodesException.class, ()-> { new KdTree(list, DistanceComputer.DistanceType.Euclidean);});
        IDataPoint point = Mockito.mock(IDataPoint.class);
        Mockito.when(point.clone()).thenReturn(point);
        node.addDataPoint(point);
        assertThrows(InvalidNodesException.class, ()-> { new KdTree(list, DistanceComputer.DistanceType.Euclidean);});
        Mockito.when(point.getNumAxis()).thenReturn(0);
        assertThrows(InvalidNodesException.class, ()-> { new KdTree(list, DistanceComputer.DistanceType.Euclidean);});
        Mockito.when(point.getNumAxis()).thenReturn(1);
        Mockito.when(point.getCoordinate(0)).thenReturn(null);
        //Invalid coordinates
        assertThrows(InvalidNodesException.class, ()-> { new KdTree(list, DistanceComputer.DistanceType.Euclidean);});
        Mockito.when(point.getCoordinate(0)).thenReturn(0.0);
        assertDoesNotThrow(()-> { new KdTree(list, DistanceComputer.DistanceType.Euclidean);});
    }

    @Test
    void testValidNodes() {
        OnlineKmeanConfig config = new OnlineKmeanConfig();
        List<IClusterNode> clusterList = new ArrayList<>();
        OnlineKMeanNode node1 = new OnlineKMeanNode(config);
        DoubleListDataPoint ld1 = new DoubleListDataPoint(List.of(0.0));
        node1.addDataPoint(ld1);
        OnlineKMeanNode node2 = new OnlineKMeanNode(config);
        DoubleListDataPoint ld2 = new DoubleListDataPoint(List.of(1.0));
        node2.addDataPoint(ld2);
        OnlineKMeanNode node3 = new OnlineKMeanNode(config);
        DoubleListDataPoint ld3 = new DoubleListDataPoint(List.of(2.0));
        node3.addDataPoint(ld3);
        clusterList.add(node1);
        clusterList.add(node2);
        clusterList.add(node3);
        KdTree tree = new KdTree(clusterList, DistanceComputer.DistanceType.Euclidean);
        assertEquals(tree.getClusterPoint(ld1).getCoordinate(0), 0.0);
        assertEquals(tree.getClusterPoint(ld2).getCoordinate(0), 1.0);
        assertEquals(tree.getClusterPoint(ld3).getCoordinate(0), 2.0);
        DoubleListDataPoint ld4 = new DoubleListDataPoint(List.of(1.1));
        assertEquals(tree.getClusterPoint(ld4).getCoordinate(0), 1.0);
    }

    void addClusterNode2D(List<IClusterNode> clusterList, double x, double y) {
        OnlineKmeanConfig config = new OnlineKmeanConfig();
        OnlineKMeanNode node = new OnlineKMeanNode(config);
        DoubleListDataPoint ld = new DoubleListDataPoint(List.of(x,y));
        node.addDataPoint(ld);
        clusterList.add(node);

    }
    @Test
    void testValidNodes2D() {
        List<IClusterNode> clusterList = new ArrayList<>();
        addClusterNode2D(clusterList,0.0,0.0);
        addClusterNode2D(clusterList,0.0,1.0);
        addClusterNode2D(clusterList,0.5,0.5);
        addClusterNode2D(clusterList,1.0,0.0);
        addClusterNode2D(clusterList,1.0,1.0);
        DoubleListDataPoint ld = new DoubleListDataPoint(List.of(1.1,1.0));
        KdTree tree = new KdTree(clusterList, DistanceComputer.DistanceType.Euclidean);
        IDataPoint point = tree.getClusterPoint(ld);
        assertEquals(point.getCoordinate(0), 1.0);
        assertEquals(point.getCoordinate(1), 1.0);
         ld = new DoubleListDataPoint(List.of(-0.1,0.0));
        point = tree.getClusterPoint(ld);
        assertEquals(point.getCoordinate(0), 0.0);
        assertEquals(point.getCoordinate(1), 0.0);
        ld = new DoubleListDataPoint(List.of(-0.1,1.1));
        point = tree.getClusterPoint(ld);
        assertEquals(point.getCoordinate(0), 0.0);
        assertEquals(point.getCoordinate(1), 1.0);
        ld = new DoubleListDataPoint(List.of(0.4,0.6));
        point = tree.getClusterPoint(ld);
        assertEquals(point.getCoordinate(0), 0.5);
        assertEquals(point.getCoordinate(1), 0.5);
        ld = new DoubleListDataPoint(List.of(1.0,1.0));
        point = tree.getClusterPoint(ld);
        assertEquals(point.getCoordinate(0), 1.0);
        assertEquals(point.getCoordinate(1), 1.0);
    }

    @Test
    void testSerialization() throws IOException {
        List<IClusterNode> clusterList = new ArrayList<>();
        addClusterNode2D(clusterList,0.0,0.0);
        addClusterNode2D(clusterList,0.0,1.0);
        addClusterNode2D(clusterList,0.5,0.5);
        addClusterNode2D(clusterList,1.0,0.0);
        addClusterNode2D(clusterList,1.0,1.0);
        DoubleListDataPoint ld = new DoubleListDataPoint(List.of(1.1,1.0));
        KdTree tree = new KdTree(clusterList, DistanceComputer.DistanceType.Euclidean);
        String json = tree.saveAsJson();
        String correctJson = "{\"dimensionCount\":2,\"rootNode\":{\"@id\":\"0210f6f6-d5e1-4997-be73-ba35667536b9\",\"point\":{\"coefficient\":0.01,\"point\":{\"numAxis\":2}},\"depth\":0,\"axisIndex\":0,\"leftNode\":{\"@id\":\"22b4c914-1e68-4f46-b346-7eb6b01954b4\",\"point\":{\"coefficient\":0.01,\"point\":{\"numAxis\":2}},\"depth\":1,\"axisIndex\":1,\"leftNode\":{\"@id\":\"97487b94-39a9-4da0-bc97-79a6e819e9fb\",\"point\":{\"coefficient\":0.01,\"point\":{\"numAxis\":2}},\"depth\":2,\"axisIndex\":0,\"leftNode\":null,\"rightNode\":null,\"clusterPoint\":{\"numAxis\":2}},\"rightNode\":null,\"clusterPoint\":{\"numAxis\":2}},\"rightNode\":{\"@id\":\"1e056267-641a-4378-8870-f753ecef7c01\",\"point\":{\"coefficient\":0.01,\"point\":{\"numAxis\":2}},\"depth\":1,\"axisIndex\":1,\"leftNode\":{\"@id\":\"63bec7e6-602f-4f1e-8715-87617ffd1936\",\"point\":{\"coefficient\":0.01,\"point\":{\"numAxis\":2}},\"depth\":2,\"axisIndex\":0,\"leftNode\":null,\"rightNode\":null,\"clusterPoint\":{\"numAxis\":2}},\"rightNode\":null,\"clusterPoint\":{\"numAxis\":2}},\"clusterPoint\":{\"numAxis\":2}}}";
        JsonNode node1 = JsonKdTreeTestHelper.parseAndRemoveIds(correctJson);
        JsonNode node2 = JsonKdTreeTestHelper.parseAndRemoveIds(json);
        assertEquals(node1, node2);
        tree = KdTree.load(json);
    }


}
