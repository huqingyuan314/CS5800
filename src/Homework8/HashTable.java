package Homework8;


//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//
//public class HashTable {
//  private static final int MAXHASH = 500; // Try m = MAXHASH: 30, 300, 1000
//  private LinkedList[] table;
//
//  // Constructor to initialize the hash table with linked lists
//  public HashTable() {
//    table = new LinkedList[MAXHASH];  // initialize an array of LinkedList
//    for (int i = 0; i < MAXHASH; i++) {
//      table[i] = new LinkedList();
//    }
//  }
//
//  // Custom hash function to map words to indices as integers
//  private int hashFunction(String key) {
//    int hash = 0;
//    for (int i = 0; i < key.length(); i++) {
//      hash = (31 * hash + key.charAt(i)) % MAXHASH;  // Simple polynomial rolling hash
//    }
//    return Math.abs(hash);  // Keep the hash non-negative
//  }
//
//  // Insert a word with a count
//  public void insert(String key, int value) {
//    int index = hashFunction(key);
//    int intKey = hashFunction(key);  // Use the hash value as the integer key
//
//    if (find(key) == -1) {
//      table[index].add(intKey, key + ":" + value);
//    } else {
//      increase(key);
//    }
//  }
//
//  // Increase the count of a word if it exists, else add it with count 1
//  public void increase(String key) {
//    int index = hashFunction(key);
//    int intKey = hashFunction(key);  // Use the custom hash as the key
//    LinkedList.Node current = table[index].head;
//
//    while (current != null) {
//      if (current.key == intKey && current.value.contains(key)) {
//        String[] split = current.value.split(":");
//        int newValue = Integer.parseInt(split[1]) + 1;
//        current.value = key + ":" + newValue;
//        return;
//      }
//      current = current.next;
//    }
//
//    // If not found, add it with count 1
//    insert(key, 1);
//  }
//
//  // Find the count for a word
//  public int find(String key) {
//    int index = hashFunction(key);
//    int intKey = hashFunction(key);
//    LinkedList.Node current = table[index].head;
//
//    while (current != null) {
//      if (current.key == intKey && current.value.contains(key)) {
//        String[] split = current.value.split(":");
//        return Integer.parseInt(split[1]);
//      }
//      current = current.next;
//    }
//    return -1; // Not found
//  }
//
//  // Delete a word from the hash table
//  public void delete(String key) {
//    int index = hashFunction(key);
//    int intKey = hashFunction(key);
//    table[index].remove(intKey);
//  }
//
//  // List all words with their counts
//  public void listAllKeys() {
//    for (int i = 0; i < MAXHASH; i++) {
//      if (table[i].head != null) {
//        table[i].display();
//      }
//    }
//  }
//
//
//  public void processFile(String fileName) {
//    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
//      String line;
//      while ((line = reader.readLine()) != null) {
//        // Split each line into words
//        String[] words = line.toLowerCase().split("\\W+"); // Split on non-word characters
//        for (String word : words) {
//          if (!word.isEmpty()) { // Only process non-empty words
//            increase(word);
//          }
//        }
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//
//  // Output the hash table to a file
//  public void outputToFile(String fileName) {
//    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
//      for (int i = 0; i < MAXHASH; i++) {
//        LinkedList.Node current = table[i].head;
//        while (current != null) {
//          writer.write(current.value + "\n");
//          current = current.next;
//        }
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
//
//  // Test with sample input
//  public static void main(String[] args) throws IOException {
//    HashTable hashTable = new HashTable();
//
//    // Sample text input to process
//    String text = "This is a sample text this text is for testing";
//
//
////    String[] words = text.split(" ");
////
////    for (String word : words) {
////      hashTable.increase(word.toLowerCase()); // Convert words to lowercase for uniformity
////    }
//
//    // Read and process the file
//    hashTable.processFile("alice_in_wonderland.txt");
//
//    // Display the hash table contents
//    System.out.println("Hash Table Contents:");
//    hashTable.listAllKeys();
//
//    // Output to a file
//    hashTable.outputToFile("word_counts.txt");
//  }
//}


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class HashTable {
  private int MAXHASH;
  private LinkedList[] table;


  static class LinkedList {
    Node head;

    static class Node {
      String value; // stores the word itself
      int count;    // keeps track of the frequency of that word
      Node next;    // reference to the next node in the linked list

      Node(String value, int count) {
        this.value = value;
        this.count = count;
        this.next = null;
      }
    }

    // method to add new node to the head
    public void add(String value, int count) {
      Node newNode = new Node(value, count);
      newNode.next = head;
      head = newNode;
    }

    // method to get the list's length
    public int getListLength() {
      int length = 0;
      Node current = head;
      while (current != null) {
        length++;
        current = current.next;
      }
      return length;
    }
  }




  // Constructor to initialize the hash table with linked lists
  public HashTable(int MAXHASH) {
    this.MAXHASH = MAXHASH;
    table = new LinkedList[MAXHASH];
    for (int i = 0; i < MAXHASH; i++) {
      table[i] = new LinkedList();
    }
  }

  // Hash function to map key to index
  private int hashFunction(String key) {
    int hash = 0;
    for (int i = 0; i < key.length(); i++) {
      hash = (31 * hash + key.charAt(i)) % MAXHASH;
    }
    return hash;
  }

//
//  // Insert a word with a specific initial count
//  public void insert(String key, int value) {
//    int index = hashFunction(key);
//    LinkedList.Node node = findNodeByKey(index, key);
//
//    if (node == null) {
//      table[index].add(key, value);  // Insert new node if word doesn't exist
//    } else {
//      node.count = value;  // Update count if it exists (resetting value)
//    }
//  }



  // Insert or increase word count in hash table
  public void insertOrIncrease(String key) {
    int index = hashFunction(key);
    LinkedList.Node current = table[index].head;

    while (current != null) {
      if (current.value.equals(key)) {
        current.count++;
        return;
      }
      current = current.next;
    }

    // If word not found, add with count 1
    table[index].add(key, 1);
  }

  // Process text file and insert words
  public void processFile(String fileName) throws IOException {
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] words = line.split("\\W+");
        for (String word : words) {
          if (!word.isEmpty()) {
            insertOrIncrease(word.toLowerCase());
          }
        }
      }
    }
  }

  // Calculate and return the length of each collision list
  private int[] getListLengths() {
    int[] lengths = new int[MAXHASH];
    for (int i = 0; i < MAXHASH; i++) {
      lengths[i] = table[i].getListLength();
    }
    return lengths;
  }

  // Calculate variance of the list lengths
  private double calculateVariance(int[] lengths) {
    double mean = 0;
    for (int length : lengths) {
      mean += length;
    }
    mean /= MAXHASH;

    double variance = 0;
    for (int length : lengths) {
      variance += (length - mean) * (length - mean);
    }
    return variance / MAXHASH;
  }

  // Print histogram of collision list lengths
  private void printHistogram(int[] lengths) {
    int max = 0;
    for (int length : lengths) {
      if (length > max) max = length;
    }

    System.out.println("Histogram of collision list lengths:");
    for (int i = 0; i <= max; i++) {
      int count = 0;
      for (int length : lengths) {
        if (length == i) count++;
      }
      if (count > 0) {
        System.out.println("Length " + i + ": " + count);
      }
    }
  }

  // Print the longest 10% of lists
  private void printLongestLists(int[] lengths) {
    ArrayList<Integer> lengthList = new ArrayList<>();
    for (int length : lengths) {
      lengthList.add(length);
    }
    Collections.sort(lengthList, Collections.reverseOrder());

    int top10PercentIndex = (int) (MAXHASH * 0.1);
    System.out.println("Lengths of longest 10% of lists:");
    for (int i = 0; i < top10PercentIndex; i++) {
      System.out.println(lengthList.get(i));
    }
  }

  // Display statistics for the hash table
  public void displayStatistics() {
    int[] lengths = getListLengths();
    printHistogram(lengths);
    System.out.println("Variance of list lengths: " + calculateVariance(lengths));
    printLongestLists(lengths);
  }


  public static void main(String[] args) {
    try {
      String fileName = "alice_in_wonderland.txt";

      // Try with different MAXHASH values
      int[] maxHashes = {30, 300, 1000};
      for (int m : maxHashes) {
        System.out.println("Testing with MAXHASH = " + m);
        HashTable hashTable = new HashTable(m);
        hashTable.processFile(fileName);
        hashTable.displayStatistics();
        System.out.println();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}





