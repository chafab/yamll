package com.nekonex.ml.cluster.kdtree.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.nekonex.ml.cluster.IClusterNode;
import com.nekonex.ml.cluster.kdtree.KdTree;
import com.nekonex.ml.cluster.kmeans.OnlineKMeansNode;
import com.nekonex.ml.cluster.kmeans.KmeansConfig;
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
        KmeansConfig config = new KmeansConfig();
        OnlineKMeansNode node = new OnlineKMeansNode(config);
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
        KmeansConfig config = new KmeansConfig();
        List<IClusterNode> clusterList = new ArrayList<>();
        OnlineKMeansNode node1 = new OnlineKMeansNode(config);
        DoubleListDataPoint ld1 = new DoubleListDataPoint(List.of(0.0));
        node1.addDataPoint(ld1);
        OnlineKMeansNode node2 = new OnlineKMeansNode(config);
        DoubleListDataPoint ld2 = new DoubleListDataPoint(List.of(1.0));
        node2.addDataPoint(ld2);
        OnlineKMeansNode node3 = new OnlineKMeansNode(config);
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
        KmeansConfig config = new KmeansConfig();
        OnlineKMeansNode node = new OnlineKMeansNode(config);
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
        String correctJson = "{\"@class\":\"com.nekonex.ml.cluster.kdtree.KdTree\",\"dimensionCount\":2,\"rootNode\":{\"@class\":\"com.nekonex.ml.cluster.kdtree.KdNode\",\"@id\":\"198905c4-41f9-46b4-a27e-cecd789e3d8c\",\"clusterNode\":{\"@class\":\"com.nekonex.ml.cluster.kmeans.OnlineKMeansNode\",\"config\":{\"coefficientDecay\":0.99,\"minCoefficient\":1.0E-4,\"initialCoefficient\":0.01,\"numCluster\":1,\"minIter\":10,\"distanceType\":\"Euclidean\"},\"coordinates\":{\"@class\":\"com.nekonex.ml.data.DoubleListDataPoint\",\"data\":[0.5,0.5]},\"coefficient\":0.01,\"count\":1},\"depth\":0,\"axisIndex\":0,\"leftNode\":{\"@class\":\"com.nekonex.ml.cluster.kdtree.KdNode\",\"@id\":\"91c0379b-a9d0-4abc-9169-95a44e525288\",\"clusterNode\":{\"@class\":\"com.nekonex.ml.cluster.kmeans.OnlineKMeansNode\",\"config\":{\"coefficientDecay\":0.99,\"minCoefficient\":1.0E-4,\"initialCoefficient\":0.01,\"numCluster\":1,\"minIter\":10,\"distanceType\":\"Euclidean\"},\"coordinates\":{\"@class\":\"com.nekonex.ml.data.DoubleListDataPoint\",\"data\":[0.0,1.0]},\"coefficient\":0.01,\"count\":1},\"depth\":1,\"axisIndex\":1,\"leftNode\":{\"@class\":\"com.nekonex.ml.cluster.kdtree.KdNode\",\"@id\":\"8711f2ed-aa06-4db8-b94d-5b610844ea1a\",\"clusterNode\":{\"@class\":\"com.nekonex.ml.cluster.kmeans.OnlineKMeansNode\",\"config\":{\"coefficientDecay\":0.99,\"minCoefficient\":1.0E-4,\"initialCoefficient\":0.01,\"numCluster\":1,\"minIter\":10,\"distanceType\":\"Euclidean\"},\"coordinates\":{\"@class\":\"com.nekonex.ml.data.DoubleListDataPoint\",\"data\":[0.0,0.0]},\"coefficient\":0.01,\"count\":1},\"depth\":2,\"axisIndex\":0,\"leftNode\":null,\"rightNode\":null},\"rightNode\":null},\"rightNode\":{\"@class\":\"com.nekonex.ml.cluster.kdtree.KdNode\",\"@id\":\"4d05d3e6-6679-4fde-bf3c-e8e8a8df289e\",\"clusterNode\":{\"@class\":\"com.nekonex.ml.cluster.kmeans.OnlineKMeansNode\",\"config\":{\"coefficientDecay\":0.99,\"minCoefficient\":1.0E-4,\"initialCoefficient\":0.01,\"numCluster\":1,\"minIter\":10,\"distanceType\":\"Euclidean\"},\"coordinates\":{\"@class\":\"com.nekonex.ml.data.DoubleListDataPoint\",\"data\":[1.0,1.0]},\"coefficient\":0.01,\"count\":1},\"depth\":1,\"axisIndex\":1,\"leftNode\":{\"@class\":\"com.nekonex.ml.cluster.kdtree.KdNode\",\"@id\":\"3f98f9b7-755a-449e-a7aa-b07c4ea16d15\",\"clusterNode\":{\"@class\":\"com.nekonex.ml.cluster.kmeans.OnlineKMeansNode\",\"config\":{\"coefficientDecay\":0.99,\"minCoefficient\":1.0E-4,\"initialCoefficient\":0.01,\"numCluster\":1,\"minIter\":10,\"distanceType\":\"Euclidean\"},\"coordinates\":{\"@class\":\"com.nekonex.ml.data.DoubleListDataPoint\",\"data\":[1.0,0.0]},\"coefficient\":0.01,\"count\":1},\"depth\":2,\"axisIndex\":0,\"leftNode\":null,\"rightNode\":null},\"rightNode\":null}},\"distType\":\"Euclidean\"}";
        JsonNode node1 = JsonKdTreeTestHelper.parseAndRemoveIds(correctJson);
        JsonNode node2 = JsonKdTreeTestHelper.parseAndRemoveIds(json);
        assertEquals(node1, node2);
        KdTree tree2 = KdTree.load(json);
        assertEquals(tree, tree2);
    }


}
