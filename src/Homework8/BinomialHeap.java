package Homework8;
import java.util.*;

class BinomialHeap {
  private Node head;

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
  }

  // Make-heap: Initializes the heap to null
  public void makeHeap() {
    head = null;
  }


  // Insert a key into the binomial heap
  public void insert(int key) {
    BinomialHeap tempHeap = new BinomialHeap();
    tempHeap.head = new Node(key);
    head = union(head, tempHeap.head);
  }


  // Find the minimum value in the heap
  public int minimum() {
    if (head == null) {
      throw new NoSuchElementException("Heap is empty.");
    }

    int min = Integer.MAX_VALUE;
    Node temp = head;
    while (temp != null) {  // traverse the heap to find min
      min = Math.min(min, temp.key);
      temp = temp.sibling;
    }
    return min;
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
    BinomialHeap bh = new BinomialHeap();

    bh.insert(10);
    bh.insert(20);
    bh.insert(30);
    bh.insert(5);
    bh.insert(15);

    System.out.println("Heap after insertion:");
    bh.printHeap();

    System.out.println("\nMinimum value: " + bh.minimum());

    System.out.println("\nExtract minimum: " + bh.extractMin());
    System.out.println("Heap after extractMin:");
    bh.printHeap();

    bh.decreaseKey(20, 6);
    System.out.println("\nHeap after decreasing key 20 to 6:");
    bh.printHeap();

    bh.decreaseKey(30, 4);
    System.out.println("\nHeap after decreasing key 30 to 4:");
    bh.printHeap();

    bh.delete(15);
    System.out.println("\nHeap after deleting 15:");
    bh.printHeap();
  }
}