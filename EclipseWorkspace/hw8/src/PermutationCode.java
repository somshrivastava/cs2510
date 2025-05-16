import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import tester.*;

/**
 * A class that defines a new permutation code, as well as methods for encoding
 * and decoding messages that use this code.
 */
class PermutationCode {
  // The original list of characters to be encoded
  ArrayList<Character> alphabet = new ArrayList<Character>(
      Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
          'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));

  ArrayList<Character> code = new ArrayList<Character>(26);

  // A random number generator
  Random rand = new Random();

  // Create a new instance of the encoder/decoder with a new permutation code
  PermutationCode() {
    this.code = this.initEncoder();
  }

  // Create a new instance of the encoder/decoder with the given code
  PermutationCode(ArrayList<Character> code) {
    this.code = code;
  }

  // Initialize the encoding permutation of the characters
  ArrayList<Character> initEncoder() {
    ArrayList<Character> shuffledAlphabet = new ArrayList<>(this.alphabet);
    ArrayList<Character> result = new ArrayList<>();
    while (!shuffledAlphabet.isEmpty()) {
      int randomIndex = rand.nextInt(shuffledAlphabet.size());
      result.add(shuffledAlphabet.remove(randomIndex));
    }
    return result;
  }

  // produce an encoded String from the given String
  String encode(String originalString) {
    String encoded = "";
    for (int i = 0; i < originalString.length(); i++) {
      char character = originalString.charAt(i);
      int index = alphabet.indexOf(character);
      if (index != -1) {
        encoded += code.get(index);
      }
    }
    return encoded;
  }

  // produce a decoded String from the given String
  String decode(String encodedString) {
    String decoded = "";
    for (int i = 0; i < encodedString.length(); i++) {
      char character = encodedString.charAt(i);
      int index = code.indexOf(character);
      if (index != -1) {
        decoded += alphabet.get(index);
      }
    }
    return decoded;
  }
}

// examples and tests for PermutationCode
class ExamplesPermutationCode {
  ArrayList<Character> testCode = new ArrayList<>(
      Arrays.asList('b', 'e', 'a', 'c', 'd', 'g', 'f', 'h', 'j', 'i', 'l', 'k', 'n', 'm', 'o', 'p',
          'r', 'q', 't', 's', 'v', 'u', 'x', 'w', 'z', 'y'));
  PermutationCode pc = new PermutationCode(testCode);

  // tests the encode method
  public void testEncode(Tester t) {
    // encoding a simple string
    t.checkExpect(pc.encode("abc"), "bea");
    // encoding last three letters
    t.checkExpect(pc.encode("xyz"), "wzy");
    // encoding a word
    t.checkExpect(pc.encode("hello"), "hdkko");
    // encoding repeating characters
    t.checkExpect(pc.encode("aabbcc"), "bbeeaa");
    // encoding mixed order letters
    t.checkExpect(pc.encode("zxyabc"), "ywzbea");
    // encoding an empty string
    t.checkExpect(pc.encode(""), "");
    // encoding the full alphabet
    t.checkExpect(pc.encode("abcdefghijklmnopqrstuvwxyz"), "beacdgfhjilknmoprqtsvuxwzy");
  }

  // tests the decode method
  public void testDecode(Tester t) {
    // decoding a simple string
    t.checkExpect(pc.decode("bea"), "abc");
    // decoding last three letters
    t.checkExpect(pc.decode("wzy"), "xyz");
    // decoding a word
    t.checkExpect(pc.decode("hdkko"), "hello");
    // decoding repeating characters
    t.checkExpect(pc.decode("bbeebb"), "aabbaa");
    // decoding mixed order letters
    t.checkExpect(pc.decode("ywzbea"), "zxyabc");
    // decoding an empty string
    t.checkExpect(pc.decode(""), "");
    // decoding the full alphabet
    t.checkExpect(pc.decode("beacdgfhjilk nmo p rqtsvuwxwzy"), "abcdefghijklmnopqrstuvxwxyz");
  }

  // tests the initEncoder
  public void testInitEncoder(Tester t) {
    // generate a new encoding permutation
    ArrayList<Character> encodedAlphabet = pc.initEncoder();
    // ensure it contains 26 letters
    t.checkExpect(encodedAlphabet.size(), 26);
    // ensure it contains all letters
    t.checkExpect(encodedAlphabet.containsAll(pc.alphabet), true);
    // ensure it's a permutation
    t.checkExpect(!encodedAlphabet.equals(pc.alphabet), true);
  }
}
