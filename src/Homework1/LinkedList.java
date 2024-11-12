package Homework1;

public class LinkedList<T> {
  Node<T> head;

  void add(T data) {
    if (head == null) {
      head = new Node<>(data);
      return;
    }

    Node<T> current = head;
    while (current.next != null) {
      current = current.next;
    }
    current.next = new Node<>(data);

  }

  Node<T> getHead() {
    return head;
  }

  // Get the length of the linked list
  static <T> int getLength(Node<T> head){
    int length = 0;
    while (head != null) {
      length++;
      head = head.next;
    }
    return length;
  }

  // Move the head of a list forward by the given number of steps
  static <T> Node<T> moveForward(Node<T> head, int steps) {
    for (int i = 0; i < steps; i++) {
      if (head != null) {
        head = head.next;
      }
    }
    return head;
  }

}