package Homework8;

public class SkipList {
  private static final int MAX_LEVEL = 50;
  private int level; // Current maximum level in the skiplist
  private Node head; // Header node

  // SkipList's Node structure
  private static class Node {
    int value;
    Node[] forward; // array to hold references to different levels

    public Node(int value, int level) {
      this.value = value;
      forward = new Node[level + 1]; // one extra for level 0
    }
  }

  // Constructor of SkipList
  public SkipList() {
    level = 0;
    head = new Node(Integer.MIN_VALUE, MAX_LEVEL);
  }

  // Random level generator (simulate the coin flip)
  private int randomLevel() {
    int level = 0;
    while (Math.random() < 0.5 && level < MAX_LEVEL) {
      level++;
    }
    return level;
  }


  // Insert operation
  public void insert(int value) {
    System.out.println("Insert " + value);

    Node[] update = new Node[MAX_LEVEL + 1];
    Node current = head;

    // Start from the top level and move down until find the right insertion point
    for (int i = level; i >= 0; i--) {
      while (current.forward[i] != null && current.forward[i].value < value) {
        current = current.forward[i];
      }
      update[i] = current; // Store the previous node at each level
    }

    current = current.forward[0]; // Move to level 0 after finishing the top levels

    // If the node with the value already exists, do nothing
    if (current != null && current.value == value) {
      System.out.println("*Duplicate value " + value + "*");
      System.out.println();
      return;
    }

    // Otherwise, insert the new node
    int newLevel = randomLevel();

    // If newLevel is greater than the current level, update the header
    if (newLevel > level) {
      for (int i = level + 1; i <= newLevel; i++) {
        update[i] = head; // Set the header at new levels
      }
      level = newLevel;
    }

    // Create the new node and link it to the SkipList
    Node newNode = new Node(value, newLevel);
    for (int i = 0; i <= newLevel; i++) {
      newNode.forward[i] = update[i].forward[i];
      update[i].forward[i] = newNode;
    }

    // Print the SkipList after insertion
    printSkipList();
  }


  // Delete operation
  public void delete(int value) {
    System.out.println("Delete " + value);

    Node[] update = new Node[MAX_LEVEL + 1];
    Node current = head;

    // Find the nodes to update at each level
    for (int i = level; i >= 0; i--) {
      while (current.forward[i] != null && current.forward[i].value < value) {
        current = current.forward[i];
      }
      update[i] = current;
    }

    current = current.forward[0];

    // If the node to delete is not found, do nothing
    if (current == null || current.value != value) {
      System.out.println("*Not Found " + value + "*");
      System.out.println();
      return;
    }

    // Remove the node by updating forward references
    for (int i = 0; i <= level; i++) {
      if (update[i].forward[i] != current) {
        break;
      }
      update[i].forward[i] = current.forward[i];
    }

    // Decrease the level of the SkipList if necessary
    while (level > 0 && head.forward[level] == null) {
      level--;
    }

    // Print the SkipList after deletion
    printSkipList();
  }


  // Lookup operation
  public boolean lookup(int value) {
    System.out.println("Lookup " + value);

    Node current = head;

    for (int i = level; i >= 0; i--) {
      while (current.forward[i] != null && current.forward[i].value < value) {
        current = current.forward[i];
      }
    }

    current = current.forward[0];

    if (current != null && current.value == value) {
      System.out.println("Found " + value);
      System.out.println();
      return true;

    } else {
      System.out.println("*Not Found " + value + "*");
      System.out.println();
      return false;
    }
  }


  // Print the SkipList for visualization
  public void printSkipList() {
    System.out.println("SkipList:");
    for (int i = level; i >= 0; i--) {
      Node current = head.forward[i];
      System.out.print("Level " + i + ": ");
      while (current != null) {
        System.out.print(current.value + " ");
        current = current.forward[i];
      }
      System.out.println();
    }
    System.out.println();
  }


  ///////////////////////////////////////////

  // Main function to test the skiplist
  public static void main(String[] args) {
    SkipList skiplist = new SkipList();

    skiplist.insert(20);
    skiplist.insert(40);
    skiplist.insert(10);
    skiplist.insert(20); // duplicate
    skiplist.insert(5);
    skiplist.insert(80);
    skiplist.delete(20);
    skiplist.insert(100);
    skiplist.insert(20);
    skiplist.insert(30);
    skiplist.delete(5);
    skiplist.delete(5); // delete the value not exist
    skiplist.insert(50);

    skiplist.lookup(80);
    skiplist.lookup(81);
  }
}