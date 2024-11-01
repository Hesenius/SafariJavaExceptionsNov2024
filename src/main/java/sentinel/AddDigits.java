package sentinel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;

public class AddDigits {
  public static int readItForMe(Reader input) throws Throwable {
    int rv = input.read();
    System.out.println("rv is " + rv);
    if (rv >= '0' && rv <= '9') return rv;
    if (rv == -1) return rv;
    return -2; // additional "error code" -- client NOT UPDATED
    // KEY POINT:
    // How you fail, and how you report failure IS PART OF YOUR PUBLIC API!!!
    // So, wouldn't it be nice if the compiler could have told us that the client
    // needed to be updated!!!
  }
  public static int sumOfNDigits(Reader input, int count) throws Throwable {
    int sum = 0;
    for (int i = 0; i < count; i++) {
      int digit = readItForMe(input);
      if (digit == -1) break;
      digit = digit - '0'; // Convert UTF-8 Roman digit to int value
      sum += digit;
    }
    input.reset();
    return sum;
  }
  public static void main(String[] args) throws Throwable {
    Reader fr = new BufferedReader(new FileReader("Digits.txt"));
    fr.mark(64);
    System.out.println("Sum of first 4 digits is " + sumOfNDigits(fr, 4));
    System.out.println("Sum of first 5 digits is " + sumOfNDigits(fr, 5));
    System.out.println("Sum of first 9 digits is " + sumOfNDigits(fr, 9));
    System.out.println("Sum of first 20 digits is " + sumOfNDigits(fr, 20));
  }
}
