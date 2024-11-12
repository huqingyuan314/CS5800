package Homework1;
//import java.util.LinkedList;
import java.lang.Math;
import static Homework1.LinkedList.getLength;
import static Homework1.LinkedList.moveForward;

public class FindIntersection {

  private static <T> void findIntersection(Node<T> headA, Node<T> headB) {

    // Count the lists first
    int m = getLength(headA);
    int n = getLength(headB);

    // Count difference as an offset in the longer list
    int offset = Math.abs(m - n);

    // Initialize the indexes of two lists
    int indexA = 0;
    int indexB = 0;

    // Align both lists so that they have the same number of nodes to compare
    if (m > n){
      headA = moveForward(headA, offset);
      indexA = offset;
    }

    if (m < n){
      headB = moveForward(headB, offset);
      indexB = offset;
    }


    // Traverse both lists together and find the intersection
    while (headA != null && headB != null) {
      if (headA == headB) { // Compare by reference
        System.out.println("The first common element : " + headA.data);
        System.out.println("At index " + (indexA + 1) + " of LinkedListA");
        System.out.println("At index " + (indexB + 1) + " of LinkedListB");
        return;
      }
      indexA++;
      indexB++;
      headA = headA.next;
      headB = headB.next;
    }

    // If no intersection
    System.out.println("No intersection found.");

  }


  public static void main(String[] args) {

    // Create linked lists A, B and add nodes
    LinkedList<Integer> A = new LinkedList<Integer>();
    LinkedList<Integer> B = new LinkedList<Integer>();

    A.add(1);
    A.add(1);
    A.add(1);
//    A.add(5);


    B.add(7);
    B.add(5);
//    B.add(1);


    // Create linked lists C and add common nodes (tail)
    LinkedList<Integer> C = new LinkedList<Integer>();
    for (int i = 11; i <= 20; i++){
      C.add(i);
    }


    // Attach common nodes to both A and B
    Node<Integer> currentA = A.getHead();
    while (currentA.next != null) currentA = currentA.next;
    currentA.next = C.getHead();

    Node<Integer> currentB = B.getHead();
    while (currentB.next != null) currentB = currentB.next;
    currentB.next = C.getHead();


    // Find the intersection
    findIntersection(A.getHead(), B.getHead());
  }
}
