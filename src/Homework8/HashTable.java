package Homework8;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HashTable {
  private static final int MAXHASH = 30; // Try m = MAXHASH: 30, 300, 1000
  private LinkedList[] table;

  // Constructor to initialize the hash table with linked lists
  public HashTable() {
    table = new LinkedList[MAXHASH];  // Initialize an array of LinkedList
    for (int i = 0; i < MAXHASH; i++) {
      table[i] = new LinkedList();
    }
  }

  // Custom hash function to map words to indices as integers
  private int hashFunction(String key) {
    int hash = 0;

    // take values and multiply it by a prime number
    for (int i = 0; i < key.length(); i++) {
      hash = (53 * hash + key.charAt(i)) % MAXHASH;
    }
    return Math.abs(hash);  // Keep the hash non-negative
  }

  // Insert a word with a count
  public void insert(String key, int count) {
    int index = hashFunction(key);

    if (find(key) == -1) {  // If not found, add the new word
      table[index].add(key, count);
    } else {
      increase(key);
    }
  }

  // Increase the count of a word if it exists, else add it with count 1
  public void increase(String key) {
    int index = hashFunction(key);
    LinkedList.Node current = table[index].head;

    while (current != null) {
      if (current.key.equals(key)) {  // Found the word, increase count
        current.count++;
        return;
      }
      current = current.next;
    }

    // If not found, add it with count 1
    insert(key, 1);
  }

  // Find a word
  public int find(String key) {
    int index = hashFunction(key);
    LinkedList.Node current = table[index].head;

    while (current != null) {
      if (current.key.equals(key)) {  // Found the word
        return 0;
      }
      current = current.next;
    }
    return -1; // Not found
  }

  public void isFind(int find, String key) {
    if (find == 0) {
      System.out.println("Find " + key);
    } else {
      System.out.println("Not found " + key); // Not found
    }
  }

  // Delete a word from the hash table
  public void delete(String key) {
    int index = hashFunction(key);
    table[index].remove(key);
  }

  // List all words with their counts
  public void listAllKeys() {
    for (int i = 0; i < MAXHASH; i++) {
      if (table[i].head != null) {
        System.out.print("Hash " + i + ": ");
        table[i].display();
      }
    }
    System.out.println();
  }

  // Process text file and insert or update words in the hash table
  public void processFile(String fileName) {
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
      String line;
      while ((line = reader.readLine()) != null) {
        // Split each line into words
        String[] words = line.toLowerCase().split("\\W+"); // Split on non-word characters
        for (String word : words) {
          // Only process non-empty, alphabetic words
          if (!word.isEmpty() && word.matches("^[a-zA-Z]+$")) {
            increase(word);
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Output the word counts to a file
  public void outputToFile(String fileName) {
    List<WordCount> wordCounts = new ArrayList<>();

    // Collect all words and counts from the hash table
    for (int i = 0; i < MAXHASH; i++) {
      LinkedList.Node current = table[i].head;
      while (current != null) {
        wordCounts.add(new WordCount(current.key, current.count));
        current = current.next;
      }
    }

    // Sort the list of WordCount objects alphabetically by word
    Collections.sort(wordCounts, Comparator.comparing(wc -> wc.word));

    // Write the sorted words and counts to the output file
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
      for (WordCount wc : wordCounts) {
        writer.write(wc.word + ": " + wc.count + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Helper class to store word and count together
  private static class WordCount {
    String word;
    int count;

    WordCount(String word, int count) {
      this.word = word;
      this.count = count;
    }
  }


  // Method to gather collision list lengths and print the histogram and variance
  public void printHistogram() {
    int[] collisionLengths = new int[200];  // Array to store frequency of list lengths
    int totalNonEmptyLists = 0;
    int sumOfLengths = 0;
    List<Integer> lengths = new ArrayList<>();  // To store individual lengths of non-empty lists

    // Traverse the hash table to find the length of each linked list
    for (int i = 0; i < MAXHASH; i++) {
      LinkedList.Node current = table[i].head;
      int length = 0;
      while (current != null) {
        length++;
        current = current.next;
      }
      if (length > 0) {
        collisionLengths[length]++;
        totalNonEmptyLists++;
        sumOfLengths += length;
        lengths.add(length);  // Store the length of the current list
      }
    }

    // Print the histogram (number of lists at each length)
    System.out.println("Collision Length Histogram:");
    for (int length = 1; length < collisionLengths.length; length++) {
      if (collisionLengths[length] > 0) {
        System.out.println("Length " + length + ": " + collisionLengths[length] + " lists");
      }
    }

    // Calculate the mean length
    double meanLength = (double) sumOfLengths / totalNonEmptyLists;

    // Calculate the variance
    double varianceSum = 0;
    for (int len : lengths) {
      varianceSum += Math.pow(len - meanLength, 2);  // Sum of squared differences from mean
    }
    double variance = varianceSum / totalNonEmptyLists;

    // Print the variance
    System.out.println("\nVariance of the collision list lengths: " + variance);
  }



  ////////////////////////////////////////////////////////////

  // Test with sample input
  public static void main(String[] args) throws IOException {
    HashTable hashTable = new HashTable();

    // Read and process the file
    hashTable.processFile("alice_in_wonderland.txt");

    // Display the hash table contents
    System.out.println("Hash Table Contents:");
    hashTable.listAllKeys();

    // Output to a file
    hashTable.outputToFile("word_counts.txt");

    // Print the collision length histogram and variance
    hashTable.printHistogram();


//    // Insert
//    hashTable.insert("Qingyuan", 1);
//    hashTable.listAllKeys();
//
//    // Delete
//    hashTable.delete("confirmation");
//    hashTable.listAllKeys();
//
//    // Increase
//    hashTable.increase("Qingyuan");
//    hashTable.listAllKeys();
//
//    // Find
//    hashTable.isFind(hashTable.find("Qingyuan"), "Qingyuan");
//    hashTable.isFind(hashTable.find("Nianlong"), "Nianlong");

  }
}