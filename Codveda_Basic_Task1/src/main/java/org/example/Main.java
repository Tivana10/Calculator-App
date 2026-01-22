package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {

    private JTextField display;
    private double firstNumber = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    public Main() {
        // JSON-style button layout with C and ← buttons
        String[][] buttonLayout = {
                {"C", "←", "/", "*"},
                {"1", "2", "3", "="},
                {"4", "5", "6", "+"},
                {"7", "8", "9", "-"},
                {"0", ".", "", ""}
        };

        JFrame frame = new JFrame("GUI Calculator");
        frame.setSize(350, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Display field
        display = new JTextField();
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.BOLD, 28));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        frame.add(display, BorderLayout.NORTH);

        // Panel for buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(buttonLayout.length, buttonLayout[0].length, 5, 5));

        // Create buttons dynamically
        for (String[] row : buttonLayout) {
            for (String label : row) {
                if (label.equals("")) {
                    panel.add(new JLabel()); // empty space
                    continue;
                }
                JButton button = new JButton(label);
                button.setFont(new Font("Arial", Font.BOLD, 22));
                button.addActionListener(new ButtonClickListener());
                panel.add(button);
            }
        }

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if ("0123456789.".contains(command)) {
                if (startNewNumber) {
                    display.setText(command);
                    startNewNumber = false;
                } else {
                    display.setText(display.getText() + command);
                }
            } else if ("+-*/".contains(command)) {
                try {
                    firstNumber = Double.parseDouble(display.getText());
                    operator = command;
                    startNewNumber = true;
                } catch (NumberFormatException ex) {
                    display.setText("Error");
                }
            } else if ("=".equals(command)) {
                try {
                    double secondNumber = Double.parseDouble(display.getText());
                    double result = 0;

                    switch (operator) {
                        case "+": result = Calculator.add(firstNumber, secondNumber); break;
                        case "-": result = Calculator.subtract(firstNumber, secondNumber); break;
                        case "*": result = Calculator.multiply(firstNumber, secondNumber); break;
                        case "/":
                            result = Calculator.divide(firstNumber, secondNumber);
                            if (Double.isNaN(result)) {
                                display.setText("Cannot divide by zero");
                                startNewNumber = true;
                                return;
                            }
                            break;
                    }

                    display.setText(String.valueOf(result));
                    startNewNumber = true;
                } catch (NumberFormatException ex) {
                    display.setText("Error");
                }
            } else if ("C".equals(command)) {
                display.setText("");
                firstNumber = 0;
                operator = "";
                startNewNumber = true;
            } else if ("←".equals(command)) {
                String text = display.getText();
                if (!text.isEmpty()) {
                    display.setText(text.substring(0, text.length() - 1));
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}
