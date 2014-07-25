package model;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.io.*;
import java.util.Random;

/**
 * // TODO Write specifications for your JottoModel!
 */
public class JottoModel {
    private int puzzleNumber;
    private String urlProtoType;

    public JottoModel() {
        puzzleNumber = new Random().nextInt(99999);
        generateURLType();
    }

    public void changePuzzleNumber(int newPuzzleNumber) {
        puzzleNumber = newPuzzleNumber;
        generateURLType();
    }

    public int getPuzzleNumber() {
        return puzzleNumber;
    }

    private void generateURLType() {
        urlProtoType = "http://6.005.scripts.mit.edu/jotto.py?puzzle=" + puzzleNumber
                + "&guess=";
    }

    /**
     * // TODO Write specifications for the makeGuess function.
     * 
     * @throws IOException
     */
    public String makeGuess(String guess) throws IOException {
        String input;
        URL url = new URL(urlProtoType + guess);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                url.openStream()));
        input = in.readLine();
        return input;
    }
}
