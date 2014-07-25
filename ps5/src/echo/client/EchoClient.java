package echo.client;

import java.io.*;
import java.net.*;

/**
 * A simple client that will interact with an EchoServer.
 */
public class EchoClient {

    /**
     * @param args
     *            String array containing Program arguments. It should only
     *            contain exactly one String indicating which server to connect
     *            to. We require that this string be in the form
     *            hostname:portnumber.
     */
    public static void main(String[] args) {
        // TODO Complete this implementation.
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
            String userInput;
            while ((userInput = StdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println(">>> " + in.readLine());
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
