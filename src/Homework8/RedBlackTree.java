package Homework8;
import java.io.*;
import java.util.*;

public class RedBlackTree {
  private static final boolean RED = false;
  private static final boolean BLACK = true;

  private Node root;

  // Red-Black Tree node
  private class Node {
    int data;
    Node left, right, parent; // references to related nodes
    boolean color;

    public Node(int data) {
      this.data = data;
      this.left = this.right = this.parent = null;
      this.color = RED; // New nodes are always red
    }
  }


  // Function to perform a left rotation
  private void leftRotate(Node x) {
    Node y = x.right;
    x.right = y.left;
    if (y.left != null) {
      y.left.parent = x;
    }
    y.parent = x.parent;
    if (x.parent == null) {
      root = y;
    } else if (x == x.parent.left) {
      x.parent.left = y;
    } else {
      x.parent.right = y;
    }
    y.left = x;
    x.parent = y;
  }


  // Function to perform a right rotation
  private void rightRotate(Node y) {
    Node x = y.left;
    y.left = x.right;
    if (x.right != null) {
      x.right.parent = y;
    }
    x.parent = y.parent;
    if (y.parent == null) {
      root = x;
    } else if (y == y.parent.right) {
      y.parent.right = x;
    } else {
      y.parent.left = x;
    }
    x.right = y;
    y.parent = x;
  }


  // Function to insert a new node
  public void insert(int data) {
    Node newNode = new Node(data);
    Node x = root;
    Node y = null;

    // Standard BST insert
    while (x != null) {
      y = x;
      if (newNode.data < x.data) {
        x = x.left;
      } else {
        x = x.right;
      }
    }

    newNode.parent = y; // found the location, insert newNode with parent y

    if (y == null) {    // tree was empty
      root = newNode;
    } else if (newNode.data < y.data) {
      y.left = newNode;
    } else {
      y.right = newNode;
    }

    // Fix the red-black tree properties
    fixInsert(newNode);

    System.out.println("Inserted " + data);
    printHeight();
  }


  // Function to fix the red-black tree properties after insertion
  private void fixInsert(Node z) {
    while (z.parent != null && z.parent.color == RED) {
      if (z.parent == z.parent.parent.left) {
        Node y = z.parent.parent.right; // Uncle

        if (y != null && y.color == RED) {
          // Case 1: Uncle is red
          z.parent.color = BLACK;
          y.color = BLACK;
          z.parent.parent.color = RED;
          z = z.parent.parent;
        } else {

          if (z == z.parent.right) {
            // Case 2: z is right child
            z = z.parent;
            leftRotate(z);
          }

          // Case 3: z is left child
          z.parent.color = BLACK;
          z.parent.parent.color = RED;
          rightRotate(z.parent.parent);
        }

      } else {
        Node y = z.parent.parent.left; // Uncle

        if (y != null && y.color == RED) {
          // Case 1: Uncle is red
          z.parent.color = BLACK;
          y.color = BLACK;
          z.parent.parent.color = RED;
          z = z.parent.parent;
        } else {
          if (z == z.parent.left) {
            // Case 2: z is left child
            z = z.parent;
            rightRotate(z);
          }

          // Case 3: z is right child
          z.parent.color = BLACK;
          z.parent.parent.color = RED;
          leftRotate(z.parent.parent);
        }
      }
    }

    root.color = BLACK;
  }


  // Function to find the minimum node
  public Node min(Node node) {
    while (node.left != null) {
      node = node.left;
    }
    return node;
  }

  // Function to find the maximum node
  public Node max(Node node) {
    while (node.right != null) {
      node = node.right;
    }
    return node;
  }

  // Function to find the successor of a given node
  public Node successor(Node node) {
    if (node.right != null) {
      return min(node.right);
    }

    Node parent = node.parent;
    while (parent != null && node == parent.right) {
      node = parent;
      parent = parent.parent;
    }
    return parent;
  }

  // Function to find the predecessor of a given node
  public Node predecessor(Node node) {
    if (node.left != null) {
      return max(node.left);
    }

    Node parent = node.parent;
    while (parent != null && node == parent.left) {
      node = parent;
      parent = parent.parent;
    }
    return parent;
  }

  // Function to perform an in-order traversal and sort elements
  public void sort() {
    inorder(root);
    System.out.println();
  }

  // Helper function for in-order traversal
  private void inorder(Node node) {
    if (node != null) {
      inorder(node.left);
      System.out.print(node.data + " ");
      inorder(node.right);
    }
  }


  // Function to search for a node
  public Node search(int data) {
    Node x = root;
    while (x != null) {
      if (data < x.data) {
        x = x.left;
      } else if (data > x.data) {
        x = x.right;
      } else {
        return x;
      }
    }
    return null;  // fail to find
  }


  // Function to calculate the height of the tree
  private int height(Node node) {
    if (node == null) {
      return 0;
    } else {  // calculate the height of subtree recursively
      int leftHeight = height(node.left);
      int rightHeight = height(node.right);
      return Math.max(leftHeight, rightHeight) + 1;
    }
  }

  // Function to print the height of the tree
  public void printHeight() {
    System.out.println("Tree height: " + height(root));
  }


  // Function to print the whole tree in a structured format
  public void printTree() {
    if (root == null) {
      System.out.println("Tree is empty.");
    } else {
      printTreeHelper(root, "", true);
    }
  }

  // Helper function to recursively print the tree
  private void printTreeHelper(Node node, String indent, boolean isLast) {
    if (node != null) {
      // Print the current node
      System.out.print(indent);
      if (isLast) {
        System.out.print("└── ");
        indent += "    ";
      } else {
        System.out.print("├── ");
        indent += "|   ";
      }

      // Print node color and value
      String color = node.color == RED ? "RED" : "BLACK";
      System.out.println(node.data + " (" + color + ")");

      // Recurse on left and right children
      printTreeHelper(node.left, indent, false);
      printTreeHelper(node.right, indent, true);
    }
  }


/////////////////////////////////////////////////////////

  // Main function to interact with the user
  public static void main(String[] args) {
    RedBlackTree rbt = new RedBlackTree();

    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter filename: ");
    String filename = scanner.nextLine();
    try {
      BufferedReader br = new BufferedReader(new FileReader(filename));
      String line;
      while ((line = br.readLine()) != null) {
        String[] tokens = line.split(" ");
        for (String token : tokens) {
          rbt.insert(Integer.parseInt(token));  // build a red-black tree with this
                                                // input by sequence of “inserts”
        }
      }
    } catch (IOException e) {
      System.out.println("Error reading file.");
      e.printStackTrace();
      return;
    }


    // Interactive command loop
    while (true) {
      System.out.print("\nEnter command (insert <x>, search <x>, sort, min, max, " +
                        "successor <x>, predecessor <x>, print, exit): ");
      String command = scanner.nextLine();

      if (command.startsWith("insert")) {
        try {
          int value = Integer.parseInt(command.split(" ")[1]);
          rbt.insert(value);
        } catch (NumberFormatException e) {
          System.out.println("Invalid input: please enter an integer.");
        } catch (ArrayIndexOutOfBoundsException e) {
          System.out.println("Error: no value provided for insertion.");
        }

      } else if (command.startsWith("search")) {
        try {
          int value = Integer.parseInt(command.split(" ")[1]);
          Node result = rbt.search(value);
          if (result != null) {
            System.out.println("Found " + value);
          } else {
            System.out.println(value + " not found");
          }
        } catch (NumberFormatException e) {
          System.out.println("Invalid input: please enter an integer.");
        } catch (ArrayIndexOutOfBoundsException e) {
          System.out.println("Error: no value provided for insertion.");
        }


      } else if (command.equals("sort")) {
        rbt.sort();

      } else if (command.equals("min")) {
        System.out.println(rbt.min(rbt.root).data);

      } else if (command.equals("max")) {
        System.out.println(rbt.max(rbt.root).data);

      } else if (command.startsWith("successor")) {
        int value = Integer.parseInt(command.split(" ")[1]);
        Node node = rbt.search(value);
        if (node != null) {
          Node succ = rbt.successor(node);
          if (succ != null) {
            System.out.println("Successor of " + value + " is " + succ.data);
          } else {
            System.out.println(value + " has no successor");
          }
        } else {
          System.out.println(value + " not found");
        }

      } else if (command.startsWith("predecessor")) {
        int value = Integer.parseInt(command.split(" ")[1]);
        Node node = rbt.search(value);
        if (node != null) {
          Node pred = rbt.predecessor(node);
          if (pred != null) {
            System.out.println("Predecessor of " + value + " is " + pred.data);
          } else {
            System.out.println(value + " has no predecessor");
          }
        } else {
          System.out.println(value + " not found");
        }

      } else if (command.equals("print")) {
        rbt.printTree();

      } else if (command.equals("exit")) {
        break;

      } else {
        System.out.println("Invalid command.");
      }

      if (!command.startsWith("insert")) {
        rbt.printHeight(); // After each operation print out tree height
      }

    }

    scanner.close();
  }
}