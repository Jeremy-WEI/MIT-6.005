package ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import model.JottoModel;

/**
 * // TODO Write specifications for your JottoGUI class.
 */
public class JottoGUI extends JFrame implements ActionListener {

    private JButton newPuzzleButton;
    private JTextField newPuzzleNumber;
    private JLabel puzzleNumber;
    private JLabel typeGuessHere;
    private JTextField guess;
    private JTable guessTable;
    private MyTableModel tableModel;
    private JottoModel jottoModel;

    private class MyTableModel extends AbstractTableModel {
        private ArrayList<String[]> data;

        public MyTableModel() {
            data = new ArrayList<String[]>();
        }

        public int getRowCount() {
            return data.size();
        }

        public int getColumnCount() {
            return 3;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            assert columnIndex < getColumnCount() && rowIndex < getRowCount();
            return data.get(rowIndex)[columnIndex];
        }
        public void addRow(String guess) {
            data.add(new String[] {
                guess, "", ""
            });
        }
        public void updateRow(int i, String returnMessage) {
            String[] str = data.get(i);
            String[] tokens = returnMessage.split("\\s+");
            if (tokens.length == 2) {
                int errorCase = Integer.parseInt(tokens[1]);
                switch (errorCase) {
                case 0:
                    str[1] = "Ill-formatted request.";
                    break;
                case 1:
                    str[1] = "Non-number puzzle ID.";
                    break;
                case 2:
                    str[1] = "Invalid guess.";
                    break;
                }
            }
            if (tokens.length == 3) {
                if (tokens[1].equals(5) && tokens.equals("5")) {
                    str[1] = "YOU WIN! CONG!!";
                    str[2] = "";
                } else {
                    str[1] = tokens[1];
                    str[2] = tokens[2];
                }
            }
        }

        public void reset() {
            data = new ArrayList<String[]>();
        }
    }

    public JottoGUI() {
        jottoModel = new JottoModel();
        newPuzzleButton = new JButton();
        newPuzzleButton.setName("newPuzzleButton");
        newPuzzleButton.setText("New Puzzle");
        newPuzzleButton.setPreferredSize(new Dimension(150, 40));
        newPuzzleButton.addActionListener(this);
        newPuzzleNumber = new JTextField();
        newPuzzleNumber.setName("newPuzzleNumber");
        newPuzzleNumber.setPreferredSize(new Dimension(150, 30));
        newPuzzleNumber.addActionListener(this);
        puzzleNumber = new JLabel();
        puzzleNumber.setName("puzzleNumber");
        puzzleNumber.setText("Puzzle #" + jottoModel.getPuzzleNumber());
        puzzleNumber.setPreferredSize(new Dimension(80, 30));
        typeGuessHere = new JLabel();
        typeGuessHere.setText("Type a guess here: ");
        typeGuessHere.setPreferredSize(new Dimension(100, 40));
        guess = new JTextField();
        guess.setName("guess");
        guess.setPreferredSize(new Dimension(300, 30));
        guess.addActionListener(this);
        tableModel = new MyTableModel();
        guessTable = new JTable(tableModel);
        guessTable.setName("guessTable");
        guessTable.setMinimumSize(new Dimension(500, 300));
        GroupLayout layout = new GroupLayout(this.getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout
                .createParallelGroup(Alignment.LEADING)
                .addGroup(
                        layout.createSequentialGroup()
                                .addComponent(puzzleNumber)
                                .addComponent(newPuzzleButton)
                                .addComponent(newPuzzleNumber))
                .addGroup(
                        layout.createSequentialGroup()
                                .addComponent(typeGuessHere)
                                .addComponent(guess)).addComponent(guessTable));
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(
                layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(puzzleNumber)
                        .addComponent(newPuzzleButton)
                        .addComponent(newPuzzleNumber))
                .addGroup(
                        layout.createParallelGroup(Alignment.BASELINE)
                                .addComponent(typeGuessHere)
                                .addComponent(guess)).addComponent(guessTable);
        layout.setHorizontalGroup(hGroup);
        layout.setVerticalGroup(vGroup);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newPuzzleButton
                || e.getSource() == newPuzzleNumber) {
            if (isValidPuzzleNumber(newPuzzleNumber.getText())) {
                jottoModel.changePuzzleNumber(Integer.parseInt(newPuzzleNumber
                        .getText()));
                puzzleNumber.setText("Puzzle #" + jottoModel.getPuzzleNumber());
                newPuzzleNumber.setText("");
                tableModel.reset();
                guessTable.updateUI();
            }
        } else if (e.getSource() == guess) {
            String guessWord = guess.getText();
            tableModel.addRow(guessWord);
            new Thread(new Runnable() {
                public void run() {
                    try {
                        tableModel.updateRow(tableModel.getRowCount() - 1, jottoModel.makeGuess(guessWord));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    guessTable.updateUI();
                }
            }).start();
            guessTable.updateUI();
            guess.setText("");
        }
    }

    public boolean isValidPuzzleNumber(String str) {
        return str.matches("\\d+");
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JottoGUI main = new JottoGUI();
                main.pack();
                main.setVisible(true);
            }
        });
    }
}
