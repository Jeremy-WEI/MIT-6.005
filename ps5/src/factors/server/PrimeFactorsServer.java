package factors.server;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import util.*;

/**
 * PrimeFactorsServer performs the "server-side" algorithm for counting prime
 * factors.
 *
 * Your PrimeFactorsServer should take in a single Program Argument indicating
 * which port your Server will be listening on. ex. arg of "4444" will make your
 * Server listen on 4444.
 * 
 * Your server will only need to handle one client at a time. If the connected
 * client disconnects, your server should go back to listening for future
 * clients to connect to.
 * 
 * The client messages that come in will indicate the value that is being
 * factored and the range of values this server will be processing over. Your
 * server will take this in and message back all factors for our value.
 */
public class PrimeFactorsServer {

    /** Certainty variable for BigInteger isProbablePrime() function. */
    private final static int PRIME_CERTAINTY = 10;

    /**
     * @param args
     *            String array containing Program arguments. It should only
     *            contain one String indicating the port it should connect to.
     *            Defaults to port 4444 if no Program argument is present.
     */
    public static void main(String[] args) {
        // TODO Complete this implementation.
        int portNumber = 0;
        switch (args.length) {
        case 0:
            portNumber = 4444;
            break;
        case 1:
            portNumber = Integer.parseInt(args[0]);
            break;
        default:
            System.exit(1);
        }
        try (ServerSocket serverSocket = new ServerSocket(portNumber);) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                        PrintWriter out = new PrintWriter(
                                clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                        clientSocket.getInputStream()))) {
                    String input;
                    String output;
                    while ((input = in.readLine()) != null) {
                        System.out.println(input);
                        String[] tokens = input.split(" ");
                        if (checkClientToServerMessage(tokens)) {
                            List<BigInteger> list = new ArrayList<BigInteger>();
                            BigInteger x;
                            BigInteger N = new BigInteger(tokens[1]), low = new BigInteger(
                                    tokens[2]), high = new BigInteger(tokens[3]);
                            for (x = low; x.compareTo(high) <= 0; x = x
                                    .add(new BigInteger("1"))) {
                                if (x.isProbablePrime(PRIME_CERTAINTY))
                                    while (N.remainder(x).equals(
                                            new BigInteger("0"))) {
                                        out.println("found " + tokens[1] + " " + x);
                                        list.add(x);
                                        N = N.divide(x);
                                    }
                            }
                            if (!N.equals(new BigInteger("1"))) list.add(N);
                            output = "done " + tokens[1];
                            for (BigInteger bi : list)
                                output = output + " " + bi;
                            out.println(output);
                        } else
                            out.println("invalid");
                    }
                }
            }
        } catch (IOException e) {
            System.out
                    .println("Exception caught when trying to listen on port "
                            + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public static boolean checkClientToServerMessage(String[] tokens) {
        if (tokens.length != 4)
            return false;
        else {
            if (!tokens[0].equals("factor"))
                return false;
            for (int i = 1; i < tokens.length; i++) {
                if (!tokens[i].matches("\\d+"))
                    return false;
            }
        }
        return true;
    }
}
