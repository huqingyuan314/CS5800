package Homework8;

import java.util.Objects;

public class LinkedList {
  Node head; // head node of the list

  // Node class to represent each element in the linked list
  static class Node {
    String key; // store the word
    int count;  // store the number of times the word appears
    Node next;

    // Constructor to create a new node with key-value pair
    Node(String key, int count) {
      this.key = key;
      this.count = count;
      this.next = null;
    }
  }

  // Method to add a new node at the head of the list
  public void add(String key, int count) {
    Node newNode = new Node(key, count);

    if (head == null) {
      head = newNode;
    } else {
      Node current = head;
      head = newNode;
      head.next = current;
    }
  }

  // Method to remove a node with the specified key
  public void remove(String key) {
    // Check if the head node is the one to remove
    if (head != null && Objects.equals(head.key, key)) {
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
      System.out.print("[" + current.key + ": " + current.count + "] -> ");
      current = current.next;
    }
    System.out.println("null");
  }


  // Test cases
  public static void main(String[] args) {
    LinkedList list = new LinkedList();

    // Add elements with key-value pairs
    list.add("today", 1);
    list.add("is", 1);
    list.add("a", 1);
    list.add("nice", 1);


    // Display list
    System.out.print("Linked List: ");
    list.display();

    // Add a node
    list.add("day", 1);
    System.out.print("After adding a key 'day': ");
    list.display();

    // Remove an element by key
    list.remove("today");
    System.out.print("After removing key 'today': ");
    list.display();
  }
}