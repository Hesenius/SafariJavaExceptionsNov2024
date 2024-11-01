package test;

record X(String msg) {}

public class Test {
  public static void main(String[] args) {
    Object obj = new X("Java 21");
    System.out.println("Hello " + switch(obj) {
      case X(String s) -> s + " World!";
      default -> throw new RuntimeException("Unexpected value");
    });
  }
}
