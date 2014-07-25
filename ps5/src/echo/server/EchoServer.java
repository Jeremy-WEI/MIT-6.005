package echo.server;

import java.io.*;
import java.net.*;

/**
 * A simple server that will echo client inputs.
 */
public class EchoServer {

    /**
     * @param args
     *            String array containing Program arguments. It should only
     *            contain at most one String indicating the port it should
     *            connect to. The String should be parseable into an int. If no
     *            arguments, we default to port 4444.
     */
    public static void main(String[] args) {
        // TODO complete this implementation.
        if (args.length > 1)
            System.exit(1);
        int portNumber;
        if (args.length == 0)
            portNumber = 4444;
        else
            portNumber = Integer.valueOf(args[0]);
        try (ServerSocket serverSocket = new ServerSocket(portNumber);) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                        PrintWriter out = new PrintWriter(
                                clientSocket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                        clientSocket.getInputStream()));) {
                    System.out.println("Client is Connecting");
                    String input;
                    while ((input = in.readLine()) != null) {
                        out.println(input);
                    }
                    System.out.println("Client ended Connection");
                }
            }
        } catch (IOException e) {
            System.out
                    .println("Exception caught when trying to listen on port "
                            + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
