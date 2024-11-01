package sentinel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;

public class AddDigits {
  public static int someOfNDigits(Reader input, int count) throws Throwable {
    int sum = 0;
    for (int i = 0; i < count; i++) {
      int digit = input.read();
      digit = digit - '0'; // Convert UTF-8 Roman digit to int value
      sum += digit;
    }
    input.reset();
    return sum;
  }
  public static void main(String[] args) throws Throwable {
    Reader fr = new BufferedReader(new FileReader("Digits.txt"));
    fr.mark(64);
    System.out.println("Sum of first 4 digits is " + someOfNDigits(fr, 4));
    System.out.println("Sum of first 5 digits is " + someOfNDigits(fr, 5));
    System.out.println("Sum of first 9 digits is " + someOfNDigits(fr, 9));
    System.out.println("Sum of first 20 digits is " + someOfNDigits(fr, 20));
  }
}
