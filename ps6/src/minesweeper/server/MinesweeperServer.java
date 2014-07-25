package minesweeper.server;

import java.net.*;
import java.io.*;
import minesweeper.Board;

public class MinesweeperServer {

    private final static int PORT = 4444;
    private ServerSocket serverSocket;
    private Board board;
    private int userCount;
    private boolean isDebugging;

    private class SocketState {
        public boolean isConnected;

        public SocketState() {
            isConnected = true;
        }
    }

    /**
     * Make a MinesweeperServer that listens for connections on port.
     * 
     * @param port
     *            port number, requires 0 <= port <= 65535.
     */
    public MinesweeperServer(int port, int boardSize, boolean isDebugging)
            throws IOException {
        serverSocket = new ServerSocket(port);
        board = new Board(boardSize);
        this.isDebugging = isDebugging;
    }

    /**
     * Run the server, listening for client connections and handling them. Never
     * returns unless an exception is thrown.
     * 
     * @throws IOException
     *             if the main server socket is broken (IOExceptions from
     *             individual clients do *not* terminate serve()).
     */
    public void serve() throws IOException {
        System.err.println(">>> Server Started, waiting for users.");
        while (true) {
            // block until a client connects
            final Socket socket = serverSocket.accept();
            userCount++;
            Thread t = new Thread(new Runnable() {
                public void run() {
                    handleConnection(socket);
                }
            });
            t.start();
        }
    }

    /**
     * Handle a single client connection. Returns when client disconnects.
     * 
     * @param socket
     *            socket where client is connected
     */
    private void handleConnection(Socket socket) {
        System.err.println(">>> " + Thread.currentThread().getName()
                + ": User " + userCount + " Connected.");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(),
                        true);) {
            SocketState state = new SocketState();
            String input, output;
            while ((input = in.readLine()) != null) {
                output = handleRequest(input, state);
                if (output != null) {
                    out.println(output);
                    if (!state.isConnected)
                        break;
                }
            }
            System.err.println(">>> " + Thread.currentThread().getName()
                    + ": User " + userCount + " Disconnected.");
            userCount--;
        } catch (IOException e) {
            System.err.println(">>> " + Thread.currentThread().getName()
                    + ": catches an IOException.");
            userCount--;
        }
    }

    /**
     * handler for client input
     * 
     * make requested mutations on game state if applicable, then return
     * appropriate message to the user
     * 
     * @param input
     * @return
     */
    private String handleRequest(String input, SocketState state) {

        String regex = "(look)|(dig \\d+ \\d+)|(flag \\d+ \\d+)|(deflag \\d+ \\d+)|(help)|(bye)";
        if (!input.matches(regex)) {
            // invalid input
            return null;
        }
        String[] tokens = input.split(" ");
        if (tokens[0].equals("look")) {
            // 'look' request
            return board.toString();
        } else if (tokens[0].equals("help")) {
            // 'help' request
            return "HELP: Mineweeper -version MITOCW" + System.lineSeparator()
                    + "There are " + board.getBombNumber() + " bombs.";
        } else if (tokens[0].equals("bye")) {
            // 'bye' request
            state.isConnected = false;
            return "BYE";
        } else {
            int x = Integer.parseInt(tokens[1]);
            int y = Integer.parseInt(tokens[2]);
            if (tokens[0].equals("dig")) {
                // 'dig x y' request
                if (board.dig(x, y)) {
                    state.isConnected = false;
                    return "BOOM! YOU has been blown up!";
                } else
                    return board.toString();
            } else if (tokens[0].equals("flag")) {
                // 'flag x y' request
                board.flag(x, y);
                return board.toString();
            } else if (tokens[0].equals("deflag")) {
                // 'deflag x y' request
                board.deflag(x, y);
                return board.toString();
            }
        }
        assert false : "Should never reached here!";
        return "";
    }

    /**
     * Start a MinesweeperServer running on the default port.
     */
    public static void main(String[] args) {
        try {
            MinesweeperServer server = new MinesweeperServer(PORT, 5, false);
            server.serve();
        } catch (IOException e) {
            System.err.println(">>> Server Collapsed.");
        }
    }

}