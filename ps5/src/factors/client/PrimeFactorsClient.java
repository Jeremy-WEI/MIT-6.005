package factors.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;

import util.BigMath;

/**
 * PrimeFactorsClient class for PrimeFactorsServer.
 * 
 * Your PrimeFactorsClient class should take in Program arguments
 * space-delimited indicating which PrimeFactorsServers it will connect to. ex.
 * args of "localhost:4444 localhost:4445 localhost:4446" will connect the
 * client to PrimeFactorsServers running on localhost:4444, localhost:4445,
 * localhost:4446
 *
 * Your client should take user input from standard input. The appropriate input
 * that can be processed is a number. If your input is not of the correct
 * format, you should ignore it and continue to the next one.
 * 
 * Your client should distribute to each server the appropriate range of values
 * to look for prime factors through.
 */
public class PrimeFactorsClient {

    /**
     * @param args
     *            String array containing Program arguments. Each String
     *            indicates a PrimeFactorsServer location in the form
     *            "host:port" If no program arguments are inputted, this Client
     *            will terminate.
     */
    public static void main(String[] args) {
        // TODO complete this implementation.
        if (args.length != 1)
            System.exit(1);
        String[] tokens = args[0].split(":");
        String hostName = tokens[0];
        int portNumber = Integer.parseInt(tokens[1]);
        try (Socket socket = new Socket(hostName, portNumber);
                PrintWriter out = new PrintWriter(socket.getOutputStream(),
                        true);
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                BufferedReader StdIn = new BufferedReader(
                        new InputStreamReader(System.in));) {
            System.out.println("Successfully connected to " + hostName);
            String userInput = null;
            String fromServer = null;
            String output = null;
            while ((userInput = StdIn.readLine()) != null) {
                out.println("factor " + userInput.trim() + " " + 2 + " "
                        + BigMath.sqrt(new BigInteger(userInput.trim())));
                while (!(fromServer = in.readLine()).startsWith("done")) {
                }
                String[] nums = fromServer.split(" ");
                output = nums[1] + "=";
                for (int i = 2; i < nums.length - 1; ++i)
                    output = output + nums[i] + "*";
                output += nums[nums.length - 1];
                System.out.println(">>> " + output);
                System.out.println(">>> finish!");
            }
            System.out.println("Ending Connection");
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to "
                    + hostName);
            System.exit(1);
        }
    }

}
