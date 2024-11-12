package Homework8;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class HashTable {
  private static final int TABLE_SIZE = 300; // Adjust the size as needed for testing
  private LinkedList[] table;

  // Constructor to initialize the hash table with linked lists
  public HashTable() {
    table = new LinkedList[TABLE_SIZE];
    for (int i = 0; i < TABLE_SIZE; i++) {
      table[i] = new LinkedList();
    }
  }

  // Hash function to map words to indices based on their hashCode
  private int hashFunction(String key) {
    return Math.abs(key.hashCode()) % TABLE_SIZE;
  }

  // Insert a word with a count
  public void insert(String key, int value) {
    int index = hashFunction(key);
    if (find(key) == -1) {
      table[index].add(key.hashCode(), key + ":" + value);
    } else {
      increase(key);
    }
  }

  // Increase the count of a word if it exists, else add it with count 1
  public void increase(String key) {
    int index = hashFunction(key);
    LinkedList.Node current = table[index].head;

    while (current != null) {
      if (current.key == key.hashCode() && current.value.contains(key)) {
        String[] split = current.value.split(":");
        int newValue = Integer.parseInt(split[1]) + 1;
        current.value = key + ":" + newValue;
        return;
      }
      current = current.next;
    }

    // If not found, add it with count 1
    insert(key, 1);
  }

  // Find the count for a word
  public int find(String key) {
    int index = hashFunction(key);
    LinkedList.Node current = table[index].head;

    while (current != null) {
      if (current.key == key.hashCode() && current.value.contains(key)) {
        String[] split = current.value.split(":");
        return Integer.parseInt(split[1]);
      }
      current = current.next;
    }
    return -1; // Not found
  }

  // Delete a word from the hash table
  public void delete(String key) {
    int index = hashFunction(key);
    table[index].remove(key.hashCode());
  }

  // List all words with their counts
  public void listAllKeys() {
    for (int i = 0; i < TABLE_SIZE; i++) {
      if (table[i].head != null) {
        table[i].display();
      }
    }
  }

  // Output the hash table to a file
  public void outputToFile(String fileName) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
      for (int i = 0; i < TABLE_SIZE; i++) {
        LinkedList.Node current = table[i].head;
        while (current != null) {
          writer.write(current.value + "\n");
          current = current.next;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Test with sample input
  public static void main(String[] args) {
    HashTable hashTable = new HashTable();

    // Sample text input to process
    String text = "This is a sample text this text is for testing";
    String[] words = text.split(" ");

    for (String word : words) {
      hashTable.increase(word.toLowerCase()); // Convert words to lowercase for uniformity
    }

    // Display the hash table contents
    System.out.println("Hash Table Contents:");
    hashTable.listAllKeys();

    // Output to a file
    hashTable.outputToFile("word_counts.txt");
  }
}