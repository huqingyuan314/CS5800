package Homework8;

public class LinkedList2 {
  Node head; // head node of the list

  // Node class to represent each element in the linked list
  static class Node {
    int key;
    String value;
    Node next;

    // Constructor to create a new node with key-value pair
    Node(int key, String value) {
      this.key = key;
      this.value = value;
      this.next = null;
    }
  }

  // Method to add a new node at the head of the list
  public void add(int key, String value) {
    Node newNode = new Node(key, value);

    if (head == null) {
      head = newNode;
    } else {
      Node current = head;
      head = newNode;
      head.next = current;
    }
  }

  // Method to remove a node with the specified key
  public void remove(int key) {
    // Check if the head node is the one to remove
    if (head != null && head.key == key) {
      head = head.next; // Move head to the next node
      return;
    }

    // Otherwise, find the node to remove
    Node current = head;
    while (current != null && current.next != null) {
      if (current.next.key == key) {
        current.next = current.next.next; // Bypass the node to be deleted
        return;
      }
      current = current.next;
    }
  }

  // Method to display the linked list elements
  public void display() {
    Node current = head;
    while (current != null) {
      System.out.print("[" + current.key + ": " + current.value + "] -> ");
      current = current.next;
    }
    System.out.println("null");
  }


  // Test cases
  public static void main(String[] args) {
    LinkedList2 list = new LinkedList2();

    // Add elements with key-value pairs
    list.add(1, "One");
    list.add(2, "Two");
    list.add(3, "Three");
    list.add(4, "Four");

    // Display list
    System.out.print("Linked List: ");
    list.display();

    // Add a node
    list.add(5, "Five");
    System.out.print("After adding key 5: ");
    list.display();

    // Remove an element by key
    list.remove(3);
    System.out.print("After removing key 3: ");
    list.display();
  }
}