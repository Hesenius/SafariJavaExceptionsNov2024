package pointofsale;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

class ModemDidNotConnectException extends Exception {}

class Modem {
  public static void dialModem(String number) throws ModemDidNotConnectException {

  }
}

// DO NOT DO THIS!!! IOException is almost certainly the correct way to represent this
class InfrastructureFailure extends Exception {

  public InfrastructureFailure() {
  }

  public InfrastructureFailure(String message) {
    super(message);
  }

  public InfrastructureFailure(String message, Throwable cause) {
    super(message, cause);
  }

  public InfrastructureFailure(Throwable cause) {
    super(cause);
  }

  public InfrastructureFailure(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
public class PointOfSale {
  public static boolean USE_MODEM = false;

//  public static void getPaidByCC(int amount) throws ModemDidNotConnectException, IOException {
  public static void getPaidByCC(int amount) throws InfrastructureFailure {
    int retries = 3;
    // stuff

    while (retries-- > 0) {
      try {
        if (USE_MODEM) {
          Modem.dialModem("1 800 Pay by visa");
        } else {
          Socket s = new Socket("127.0.0.1", 9000);
        }
      } catch (ModemDidNotConnectException | IOException me) {
        if (retries == 0) {
//          throw me;
          throw new InfrastructureFailure(me);
        }
//      } catch (IOException me) {
//        if (retries == 0) {
//          throw me;
//        }
      }
    }
    // stuff
  }

  public static void sellSomething() {
    // collect items
    // get total price

    // ask for payment mechanism
    // if credit...
    int amount = 1000;
    try {
      getPaidByCC(amount);
//    } catch (ModemDidNotConnectException me) {
    } catch (InfrastructureFailure me) {
      // ask for alternative payment!
    }
  }
}
