package Homework8;
import java.util.*;

class BinomialHeap {
  private Node head;

  // Node class to represent each node in the heap
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

  // Create a new empty heap
  public BinomialHeap() {
    head = null;
  }

  // Make-heap: Create an empty binomial heap
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
    while (temp != null) {
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
      if (curr.degree != next.degree || (next.sibling != null && next.sibling.degree == curr.degree)) {
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

  // Merge two binomial heaps
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

  // Link two binomial trees of the same degree
  private void link(Node y, Node z) {
    y.parent = z;
    y.sibling = z.child;
    z.child = y;
    z.degree++;
  }

  // Decrease the key of a node
  public void decreaseKey(int oldKey, int newKey) {
    Node node = findNode(head, oldKey);
    if (node == null) {
      throw new NoSuchElementException("Key not found.");
    }

    if (newKey > node.key) {
      throw new IllegalArgumentException("New key is greater than the current key.");
    }

    node.key = newKey;

    // Bubble up the node if needed
    Node parent = node.parent;
    while (parent != null && node.key < parent.key) {
      int temp = node.key;
      node.key = parent.key;
      parent.key = temp;

      node = parent;
      parent = node.parent;
    }
  }

  // Find a node by key
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

  // Print the binomial heap
  public void printHeap() {
    Node temp = head;
    while (temp != null) {
      printTree(temp);
      temp = temp.sibling;
    }
  }

  // Print a binomial tree
  private void printTree(Node node) {
    if (node == null) return;
    System.out.print(node.key + " ");
    printTree(node.child);
    printTree(node.sibling);
  }

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

    bh.decreaseKey(20, 3);
    System.out.println("\nHeap after decreasing key 20 to 3:");
    bh.printHeap();

    bh.delete(15);
    System.out.println("\nHeap after deleting 15:");
    bh.printHeap();
  }
}