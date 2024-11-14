package Homework8;
import java.util.*;

class BinomialHeap {
  private Node head;

  // Find the minimum node and store it as a reference
  private Node minNode;

  // Node class
  private static class Node {
    int key;
    int degree;
    Node parent, child, sibling;

    Node(int key) {
      this.key = key;
      this.degree = 0;
      this.parent = null;
      this.child = null;
      this.sibling = null;
    }
  }

  // Constructor: create a new empty heap
  public BinomialHeap() {
    head = null;
    minNode = null;  // Keep track of minimum node
  }

  // Make-heap: Initializes the heap to null
  public void makeHeap() {
    head = null;
  }


  // Insert a key into the binomial heap and update minNode
  public void insert(int key) {
    BinomialHeap tempHeap = new BinomialHeap();
    Node newNode = new Node(key);
    tempHeap.head = newNode;
    head = union(head, tempHeap.head);

    if (minNode == null || key < minNode.key) {
      minNode = newNode;  // Update minNode reference
    }
  }


  // Find the minimum value in the heap
  public int minimum() {
    if (minNode == null) {
      throw new NoSuchElementException("Heap is empty.");
    }
    return minNode.key;
  }


  // Extract the minimum value from the heap
  public int extractMin() {
    if (head == null) {
      throw new NoSuchElementException("Heap is empty.");
    }

    Node minNode = null;
    Node minPrev = null;
    Node prev = null;
    Node curr = head;

    // Find the minimum node
    int min = Integer.MAX_VALUE;
    while (curr != null) {
      if (curr.key < min) {
        min = curr.key;
        minPrev = prev;
        minNode = curr;
      }
      prev = curr;
      curr = curr.sibling;
    }

    if (minNode == head) {
      head = head.sibling;
    } else if (minPrev != null) {
      minPrev.sibling = minNode.sibling;
    }

    // Reverse the children of the minimum node
    Node child = minNode.child;
    Node prevChild = null;
    while (child != null) {
      Node nextChild = child.sibling;
      child.sibling = prevChild;
      prevChild = child;
      child = nextChild;
    }

    // Union the children of the minimum node with the rest of the heap
    BinomialHeap tempHeap = new BinomialHeap();
    tempHeap.head = prevChild;
    head = union(head, tempHeap.head);

    // Update the minNode reference
    minNode = findMinNode();
    return min;
  }

  // Helper method to find and set the minimum node
  private Node findMinNode() {
    Node temp = head;
    Node min = head;

    while (temp != null) {
      if (temp.key < min.key) {
        min = temp;
      }
      temp = temp.sibling;
    }
    return min;
  }


  // Union of two binomial heaps
  private Node union(Node h1, Node h2) {
    if (h1 == null) return h2;
    if (h2 == null) return h1;

    // Merge the roots
    Node merged = merge(h1, h2);
    Node prev = null;
    Node curr = merged;
    Node next = merged.sibling;

    // Iterate through the merged list of roots
    while (next != null) {
      if (curr.degree != next.degree || (next.sibling != null
              && next.sibling.degree == curr.degree)) {
        prev = curr;
        curr = next;
      } else if (curr.key <= next.key) {
        curr.sibling = next.sibling;
        link(next, curr);
      } else {
        if (prev == null) {
          merged = next;
        } else {
          prev.sibling = next;
        }
        link(curr, next);
        curr = next;
      }
      next = curr.sibling;
    }

    return merged;
  }

  // Helper method: Merge two binomial heaps
  private Node merge(Node h1, Node h2) {
    if (h1 == null) return h2;
    if (h2 == null) return h1;

    // Merge the two heaps based on the degree of the trees
    Node head = null;
    Node tail = null;

    while (h1 != null && h2 != null) {
      if (h1.degree <= h2.degree) {
        if (head == null) {
          head = h1;
          tail = head;
        } else {
          tail.sibling = h1;
          tail = tail.sibling;
        }
        h1 = h1.sibling;
      } else {
        if (head == null) {
          head = h2;
          tail = head;
        } else {
          tail.sibling = h2;
          tail = tail.sibling;
        }
        h2 = h2.sibling;
      }
    }

    if (h1 != null) tail.sibling = h1;
    if (h2 != null) tail.sibling = h2;

    return head;
  }

  // Helper method: Link two binomial trees of the same degree
  private void link(Node y, Node z) {
    y.parent = z;
    y.sibling = z.child;
    z.child = y;
    z.degree++;
  }


  // Decrease the key of a node in the binomial heap
  public void decreaseKey(int oldKey, int newKey) {
    Node node = findNode(head, oldKey);
    if (node == null) {
      throw new NoSuchElementException("Key not found.");
    }

    if (newKey > node.key) {
      throw new IllegalArgumentException("New key is greater than the current key.");
    }

    node.key = newKey;

    // Bubble up within the tree if needed
    Node parent = node.parent;
    while (parent != null && node.key < parent.key) {
      // Swap the keys of the node and its parent
      int temp = node.key;
      node.key = parent.key;
      parent.key = temp;

      // Move up the tree
      node = parent;
      parent = node.parent;
    }

    // Update minNode if the new key is the smallest
    if (minNode == null || node.key < minNode.key) {
      minNode = node;
    }
  }

  // Helper method: Find a node by key recursively
  private Node findNode(Node root, int key) {
    if (root == null) return null;
    if (root.key == key) return root;

    Node result = findNode(root.child, key);
    if (result != null) return result;

    return findNode(root.sibling, key);
  }


  // Delete a node by value
  public void delete(int key) {
    decreaseKey(key, Integer.MIN_VALUE);
    extractMin();
  }


  // Print the binomial heap with structure and lines
  public void printHeap() {
    System.out.println("Binomial Heap:");
    Node temp = head;
    int treeIndex = 1;

    while (temp != null) {
      System.out.println("Tree " + treeIndex++ + " (Degree " + temp.degree + "):");
      printTree(temp, 0, true);
      temp = temp.sibling;
      System.out.println();
    }
  }

  // Recursive helper method to print each binomial tree with lines
  private void printTree(Node node, int level, boolean isRoot) {
    if (node == null) return;

    // Print leading spaces for indentation
    for (int i = 0; i < level; i++) {
      System.out.print("   ");
    }

    // Print node value and connect with lines if needed
    if (!isRoot) {
      System.out.print("|-- ");
    }
    System.out.println(node.key);

    // Print the children of the current node with an additional indentation level
    if (node.child != null) {
      printChildTree(node.child, level + 1);
    }
  }

  // Helper method to recursively print children nodes with connecting lines
  private void printChildTree(Node node, int level) {
    while (node != null) {
      // Print leading spaces for indentation
      for (int i = 0; i < level; i++) {
        System.out.print("   ");
      }

      // Print node with connector line
      System.out.print("|-- ");
      System.out.println(node.key);

      // Print the subtree of this child node
      if (node.child != null) {
        printChildTree(node.child, level + 1);
      }

      // Move to the sibling node if it exists
      node = node.sibling;
    }
  }


  //////////////////////////////////////////

  public static void main(String[] args) {
    BinomialHeap bh1 = new BinomialHeap();

    // Insert
    bh1.insert(5);
    bh1.insert(10);
    bh1.insert(15);
    bh1.insert(20);
    bh1.insert(30);
    bh1.insert(40);
    bh1.insert(50);

    System.out.println("Heap 1 after insertion:");
    bh1.printHeap();

    // Minimum
    System.out.println("\nMinimum value: " + bh1.minimum());

    // Extract-Min
    System.out.println("\nExtract minimum: " + bh1.extractMin());
    System.out.println("Heap 1 after extractMin:");
    bh1.printHeap();

    // Decrease-Key
    bh1.decreaseKey(20, 12);
    System.out.println("\nHeap after decreasing key 20 to 12:");
    bh1.printHeap();

    // Delete
    bh1.delete(15);
    System.out.println("\nHeap after deleting 15:");
    bh1.printHeap();


    // Union
    BinomialHeap bh2 = new BinomialHeap();
    bh2.insert(25);
    bh2.insert(35);
    bh2.insert(45);
    bh2.insert(55);
    System.out.println("Heap 2 after insertion:");
    bh2.printHeap();

    // Perform union of heap1 and heap2
    BinomialHeap unionHeap = new BinomialHeap();
    unionHeap.head = bh1.union(bh1.head, bh2.head);

    // Print the resulting union heap structure
    System.out.println("\nUnion of Heap 1 and Heap 2:");
    unionHeap.printHeap();

  }
}