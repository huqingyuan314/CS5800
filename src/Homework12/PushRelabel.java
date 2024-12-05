package Homework12;

import java.util.*;

public class PushRelabel {
  private int[][] capacity; // Residual capacity matrix
  private int[][] flow;     // Flow matrix
  private int[] height;     // Height of each vertex
  private int[] excess;     // Excess flow at each vertex
  private List<Integer>[] adj; // Adjacency list
  private int numVertices;  // Number of vertices

  public PushRelabel(int numVertices) {
    this.numVertices = numVertices;
    this.capacity = new int[numVertices][numVertices];
    this.flow = new int[numVertices][numVertices];
    this.height = new int[numVertices];
    this.excess = new int[numVertices];
    this.adj = new ArrayList[numVertices];
    for (int i = 0; i < numVertices; i++) {
      adj[i] = new ArrayList<>();
    }
  }

  // Add an edge with a given capacity
  public void addEdge(int u, int v, int cap) {
    capacity[u][v] = cap;
    adj[u].add(v);
    adj[v].add(u); // Add reverse edge for residual graph
  }

  // Push operation
  private void push(int u, int v) {
    int pushFlow = Math.min(excess[u], capacity[u][v] - flow[u][v]);
    flow[u][v] += pushFlow;
    flow[v][u] -= pushFlow;
    excess[u] -= pushFlow;
    excess[v] += pushFlow;
  }

  // Relabel operation
  private void relabel(int u) {
    int minHeight = Integer.MAX_VALUE;
    for (int v : adj[u]) {
      if (capacity[u][v] - flow[u][v] > 0) {
        minHeight = Math.min(minHeight, height[v]);
      }
    }
    height[u] = minHeight + 1;
  }

  // Discharge operation
  private void discharge(int u) {
    while (excess[u] > 0) {
      for (int i = 0; i < adj[u].size(); i++) {
        int v = adj[u].get(i);
        if (capacity[u][v] - flow[u][v] > 0 && height[u] == height[v] + 1) {
          push(u, v);
          if (excess[u] == 0) break;
        }
      }
      if (excess[u] > 0) {
        relabel(u);
      }
    }
  }

  // Push-Relabel with Relabel-to-Front
  public int maxFlow(int source, int sink) {
    // Initialize preflow
    height[source] = numVertices;
    for (int v : adj[source]) {
      flow[source][v] = capacity[source][v];
      flow[v][source] = -flow[source][v];
      excess[v] += flow[source][v];
      excess[source] -= flow[source][v];
    }

    // Initialize list of vertices excluding source and sink
    List<Integer> vertices = new LinkedList<>();
    for (int i = 0; i < numVertices; i++) {
      if (i != source && i != sink) vertices.add(i);
    }

    // Relabel-to-Front logic
    Iterator<Integer> iterator = vertices.iterator();
    while (iterator.hasNext()) {
      int u = iterator.next();
      int oldHeight = height[u];
      discharge(u);
      if (height[u] > oldHeight) {
        iterator.remove();
        vertices.add(0, u); // Move to the front
        iterator = vertices.iterator(); // Restart the iteration
      }
    }

    // Compute maximum flow
    return excess[sink];
  }

  public static void main(String[] args) {
    // Example usage
    PushRelabel pr = new PushRelabel(6);

    pr.addEdge(0, 1, 16);
    pr.addEdge(0, 2, 13);
    pr.addEdge(1, 3, 12);
    pr.addEdge(2, 1, 4);
    pr.addEdge(2, 4, 14);
    pr.addEdge(3, 2, 9);
    pr.addEdge(3, 5, 20);
    pr.addEdge(4, 3, 7);
    pr.addEdge(4, 5, 4);

    System.out.println("Maximum flow: " + pr.maxFlow(0, 5));
  }
}