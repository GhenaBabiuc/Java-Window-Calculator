package org.example;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private static final List<String> operations = Arrays.asList("+", "-", "/", "*", "x\u00B2", "\u221A", "x!", "|x|", "°", "cos", "sin", "tan", "cot", "MS", "MR", "MC", "M+", "M-", "MRC", "DEL", "C", "=");
    private static final List<String> numbers = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ".");

    public static void main(String[] args) {
        JFrame frame = new JFrame("Calculator");
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setFocusable(false);

        tabbedPane.addTab("Calculator", getCalculatorPanel());
        tabbedPane.addTab("Converter", getConverterPanel());
        frame.add(tabbedPane);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static JPanel getCalculatorPanel() {
        JTextField result = new JTextField(32);
        result.setEnabled(false);
        result.setDisabledTextColor(Color.BLACK);
        result.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel memory = new JLabel("Memory: ");

        ActionListener action = e -> {
            String resultText = result.getText();

            if (e.getActionCommand().matches("[0-9]") || e.getActionCommand().equals(".")) {
                result.setText(resultText + e.getActionCommand());
            } else if (e.getActionCommand().equals("+") || e.getActionCommand().equals("-") || e.getActionCommand().equals("/") || e.getActionCommand().equals("*")) {
                result.setText(resultText + e.getActionCommand());
            } else if (e.getActionCommand().equals("C")) {
                result.setText(null);
            } else if (e.getActionCommand().equals("DEL")) {
                if (!resultText.isEmpty()) {
                    result.setText(resultText.substring(0, resultText.length() - 1));
                }
            } else if (e.getActionCommand().equals("=")) {
                if (isValidExpression(resultText)) {
                    double doubleResult = calculate(resultText);

                    if (doubleResult % 1 == 0) {
                        int intResult = (int) doubleResult;
                        result.setText(String.valueOf(intResult));
                    } else {
                        result.setText(String.valueOf(doubleResult));
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Error", "Error", JOptionPane.ERROR_MESSAGE);
                    result.setText(null);
                }
            } else if (e.getActionCommand().equals("°")) {
                if (isNumericDouble(resultText)) {
                    result.setText(resultText + "°");
                }
            } else if (e.getActionCommand().equals("cos")) {
                String number = resultText.substring(0, resultText.length() - 1);
                if (isNumericDouble(number) && !endsWithDegreeSymbol(resultText)) {
                    result.setText(String.valueOf(MathFunctions.cos(Double.parseDouble(resultText))));
                } else if (isNumericDouble(number) && endsWithDegreeSymbol(resultText)) {
                    double angleInRadians = Math.toRadians(Double.parseDouble(number));
                    result.setText(String.valueOf(Math.round(Math.cos(angleInRadians) * 1e15) / 1e15));
                } else {
                    result.setText(null);
                }
            } else if (e.getActionCommand().equals("sin")) {
                String number = resultText.substring(0, resultText.length() - 1);
                if (isNumericDouble(number) && !endsWithDegreeSymbol(resultText)) {
                    result.setText(String.valueOf(MathFunctions.sin(Double.parseDouble(resultText))));
                } else if (isNumericDouble(number) && endsWithDegreeSymbol(resultText)) {
                    double angleInRadians = Math.toRadians(Double.parseDouble(number));
                    result.setText(String.valueOf(Math.round(Math.sin(angleInRadians) * 1e15) / 1e15));
                } else {
                    result.setText(null);
                }
            } else if (e.getActionCommand().equals("tan")) {
                String number = resultText.substring(0, resultText.length() - 1);
                if (isNumericDouble(number) && !endsWithDegreeSymbol(resultText)) {
                    result.setText(String.valueOf(MathFunctions.tan(Double.parseDouble(resultText))));
                } else if (isNumericDouble(number) && endsWithDegreeSymbol(resultText)) {
                    double angleInRadians = Math.toRadians(Double.parseDouble(number));
                    result.setText(String.valueOf(Math.round(Math.tan(angleInRadians) * 1e15) / 1e15));
                } else {
                    result.setText(null);
                }
            } else if (e.getActionCommand().equals("cot")) {
                String number = resultText.substring(0, resultText.length() - 1);
                if (isNumericDouble(number) && !endsWithDegreeSymbol(resultText)) {
                    result.setText(String.valueOf(1.0 / MathFunctions.tan(Double.parseDouble(resultText))));
                } else if (isNumericDouble(number) && endsWithDegreeSymbol(resultText)) {
                    double angleInRadians = Math.toRadians(Double.parseDouble(number));
                    result.setText(String.valueOf(1.0 / (Math.round(Math.tan(angleInRadians) * 1e15) / 1e15)));
                } else {
                    result.setText(null);
                }
            } else if (e.getActionCommand().equals("x\u00B2")) {
                if (isNumericDouble(resultText)) {
                    double doubleResult = MathFunctions.pow(Double.parseDouble(resultText), 2);
                    if (doubleResult % 1 == 0) {
                        int intResult = (int) doubleResult;
                        result.setText(String.valueOf(intResult));
                    } else {
                        result.setText(String.valueOf(doubleResult));
                    }
                }
            } else if (e.getActionCommand().equals("\u221A")) {
                if (isNumericDouble(resultText)) {
                    double num = Double.parseDouble(resultText);
                    if (num >= 0) {
                        double doubleResult = MathFunctions.sqrt(num);

                        if (doubleResult % 1 == 0) {
                            int intResult = (int) doubleResult;
                            result.setText(String.valueOf(intResult));
                        } else {
                            result.setText(String.valueOf(doubleResult));
                        }
                    }
                }
            } else if (e.getActionCommand().equals("x!")) {
                if (isNumericInteger(resultText)) {
                    result.setText(String.valueOf(MathFunctions.factorial(Long.parseLong(resultText))));
                }
            } else if (e.getActionCommand().equals("|x|")) {
                if (isNumericDouble(resultText)) {
                    double doubleResult = MathFunctions.abs(Double.parseDouble(resultText));
                    if (doubleResult % 1 == 0) {
                        int intResult = (int) doubleResult;
                        result.setText(String.valueOf(intResult));
                    } else {
                        result.setText(String.valueOf(doubleResult));
                    }
                }
            } else if (e.getActionCommand().equals("MS")) {
                if (isNumericDouble(resultText)) {
                    memory.setText("Memory: " + resultText);
                }
            } else if (e.getActionCommand().equals("MR")) {
                String num = getNumberFromString(memory.getText());
                if (!num.isEmpty()) {
                    result.setText(resultText + num);
                }
            } else if (e.getActionCommand().equals("MC")) {
                memory.setText("Memory: ");
            } else if (e.getActionCommand().equals("M+")) {
                if (isNumericDouble(resultText)) {
                    String num = getNumberFromString(memory.getText());
                    if (!num.isEmpty()) {
                        memory.setText("Memory: " + MathFunctions.add(Double.parseDouble(num), Double.parseDouble(resultText)));
                    }
                }
            } else if (e.getActionCommand().equals("M-")) {
                if (isNumericDouble(resultText)) {
                    String num = getNumberFromString(memory.getText());
                    if (!num.isEmpty()) {
                        memory.setText("Memory: " + MathFunctions.subtract(Double.parseDouble(num), Double.parseDouble(resultText)));
                    }
                }
            } else if (e.getActionCommand().equals("MRC")) {
                String num = getNumberFromString(memory.getText());
                if (!num.isEmpty()) {
                    result.setText(resultText + num);
                    memory.setText("Memory: ");
                }
            } else {
                result.setText("Error");
            }
        };

        JPanel numButtons = new JPanel();
        numButtons.setLayout(new GridLayout(4, 3));
        numButtons.setBackground(Color.WHITE);
        numbers.forEach(btn -> {
            JButton jButton = new JButton(btn);
            jButton.addActionListener(action);
            jButton.setOpaque(false);
            jButton.setContentAreaFilled(false);
            jButton.setForeground(Color.BLACK);
            jButton.setFocusPainted(false);
            jButton.setFont(new Font("Tahoma", Font.BOLD, 12));
            numButtons.add(jButton);
        });

        JPanel opButtons = new JPanel();
        opButtons.setLayout(new GridLayout(6, 4));
        opButtons.setBackground(Color.WHITE);
        operations.forEach(op -> {
            JButton jButton = new JButton(op);
            jButton.addActionListener(action);
            jButton.setOpaque(false);
            jButton.setContentAreaFilled(false);
            jButton.setForeground(Color.BLACK);
            jButton.setFocusPainted(false);
            jButton.setFont(new Font("Tahoma", Font.BOLD, 12));
            opButtons.add(jButton);
        });

        JPanel buttons = new JPanel();
        buttons.setBackground(Color.WHITE);
        buttons.add(numButtons);
        buttons.add(opButtons);

        JPanel calculatorPanel = new JPanel();
        calculatorPanel.setBackground(Color.WHITE);
        calculatorPanel.add(memory);
        calculatorPanel.add(result);
        calculatorPanel.add(buttons);

        return calculatorPanel;
    }

    public static JPanel getConverterPanel() {
        JPanel converterPanel = new JPanel();
        converterPanel.setBackground(Color.WHITE);

        JPanel binaryPanel = new JPanel(new GridLayout(2, 1));
        JTextField binaryField = new JTextField(30);
        JLabel binaryResult = new JLabel("Binary: ");
        binaryPanel.setBackground(Color.WHITE);
        binaryPanel.add(binaryResult);
        binaryPanel.add(binaryField);

        JPanel decimalPanel = new JPanel(new GridLayout(2, 1));
        JTextField decimalField = new JTextField(30);
        JLabel decimalResult = new JLabel("Decimal: ");
        decimalPanel.setBackground(Color.WHITE);
        decimalPanel.add(decimalResult);
        decimalPanel.add(decimalField);

        JPanel octalPanel = new JPanel(new GridLayout(2, 1));
        JTextField octalField = new JTextField(30);
        JLabel octalResult = new JLabel("Octal: ");
        octalPanel.setBackground(Color.WHITE);
        octalPanel.add(octalResult);
        octalPanel.add(octalField);

        JPanel hexadecimalPanel = new JPanel(new GridLayout(2, 1));
        JTextField hexadecimalField = new JTextField(30);
        JLabel hexadecimalResult = new JLabel("Hexadecimal: ");
        hexadecimalPanel.setBackground(Color.WHITE);
        hexadecimalPanel.add(hexadecimalResult);
        hexadecimalPanel.add(hexadecimalField);

        binaryField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(c == '0' || c == '1' || c == '.' || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                    e.consume();
                }
            }
        });

        decimalField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || c == '.' || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                    e.consume();
                }
            }
        });

        octalField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(c >= '0' && c <= '7' || c == '.' || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                    e.consume();
                }
            }
        });

        hexadecimalField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                c = Character.toUpperCase(c);
                e.setKeyChar(c);

                if (!(Character.isDigit(c) || (c >= 'A' && c <= 'F') || c == '.' || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                    e.consume();
                }
            }
        });

        ActionListener action = e -> {
            Object source = e.getSource();
            if (source.equals(binaryField)) {
                try {
                    decimalField.setText(MathFunctions.binaryToDecimal(binaryField.getText()));
                    octalField.setText(MathFunctions.binaryToOctal(binaryField.getText()));
                    hexadecimalField.setText(MathFunctions.binaryToHexadecimal(binaryField.getText()));
                } catch (NumberFormatException exception) {
                    decimalField.setText(null);
                    octalField.setText(null);
                    hexadecimalField.setText(null);
                }
            } else if (source.equals(decimalField)) {
                try {
                    binaryField.setText(MathFunctions.decimalToBinary(decimalField.getText()));
                    octalField.setText(MathFunctions.decimalToOctal(decimalField.getText()));
                    hexadecimalField.setText(MathFunctions.decimalToHexadecimal(decimalField.getText()));
                } catch (NumberFormatException exception) {
                    binaryField.setText(null);
                    octalField.setText(null);
                    hexadecimalField.setText(null);
                }
            } else if (source.equals(octalField)) {
                try {
                    binaryField.setText(MathFunctions.octalToBinary(octalField.getText()));
                    decimalField.setText(MathFunctions.octalToDecimal(octalField.getText()));
                    hexadecimalField.setText(MathFunctions.octalToHexadecimal(octalField.getText()));
                } catch (NumberFormatException exception) {
                    binaryField.setText(null);
                    decimalField.setText(null);
                    hexadecimalField.setText(null);
                }
            } else if (source.equals(hexadecimalField)) {
                try {
                    binaryField.setText(MathFunctions.hexadecimalToBinary(hexadecimalField.getText()));
                    decimalField.setText(MathFunctions.hexadecimalToDecimal(hexadecimalField.getText()));
                    octalField.setText(MathFunctions.hexadecimalToOctal(hexadecimalField.getText()));
                } catch (NumberFormatException exception) {
                    binaryField.setText(null);
                    decimalField.setText(null);
                    octalField.setText(null);
                }
            }
        };

       /* binaryField.addActionListener(action);
        decimalField.addActionListener(action);
        octalField.addActionListener(action);
        hexadecimalField.addActionListener(action);*/

        final boolean[] isUpdating = {false};
//        Use a flag or state variable to track whether an update is currently in progress within the listener.
//        For example, you can create an isUpdating flag that indicates whether a text update operation is currently in progress.
//        If isUpdating is set to true, the listener should ignore additional changes to avoid infinite recursion.
        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                action(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                action(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            private void action(DocumentEvent e) {
                if (!isUpdating[0]) {
                    isUpdating[0] = true;
                    JTextField field = null;
                    if (e.getDocument() == binaryField.getDocument()) {
                        field = binaryField;
                    } else if (e.getDocument() == decimalField.getDocument()) {
                        field = decimalField;
                    } else if (e.getDocument() == octalField.getDocument()) {
                        field = octalField;
                    } else if (e.getDocument() == hexadecimalField.getDocument()) {
                        field = hexadecimalField;
                    }

                    if (field != null) {
                        // Call the action associated with the specific field
                        /*field.getActionListeners()[0].actionPerformed(new ActionEvent(field, ActionEvent.ACTION_PERFORMED, null));*/
                        action.actionPerformed(new ActionEvent(field, ActionEvent.ACTION_PERFORMED, null));
                    }
                    isUpdating[0] = false;
                }
            }
        };

        binaryField.getDocument().addDocumentListener(documentListener);
        decimalField.getDocument().addDocumentListener(documentListener);
        octalField.getDocument().addDocumentListener(documentListener);
        hexadecimalField.getDocument().addDocumentListener(documentListener);

        converterPanel.add(binaryPanel);
        converterPanel.add(decimalPanel);
        converterPanel.add(octalPanel);
        converterPanel.add(hexadecimalPanel);

        return converterPanel;
    }

    private static double calculate(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (Character.isDigit(c) || (c == '-' && (i == 0 || !Character.isDigit(expression.charAt(i - 1))))) {
                StringBuilder numBuilder = new StringBuilder();
                numBuilder.append(c);

                // Read the whole number
                while (i + 1 < expression.length() && (Character.isDigit(expression.charAt(i + 1)) || expression.charAt(i + 1) == '.')) {
                    numBuilder.append(expression.charAt(++i));
                }

                double num = Double.parseDouble(numBuilder.toString());
                numbers.push(num);
            } else if (c == '*' || c == '/') {
                // If the operation is multiplication or division, perform them at once
                while (!operators.isEmpty() && (operators.peek() == '*' || operators.peek() == '/')) {
                    double b = numbers.pop();
                    double a = numbers.pop();
                    char op = operators.pop();
                    double result = performOperation(a, b, op);
                    numbers.push(result);
                }
                operators.push(c);
            } else if (c == '+' || c == '-') {
                // If the operation is addition or subtraction, perform operations with lower priority
                while (!operators.isEmpty() && (operators.peek() == '+' || operators.peek() == '-' || operators.peek() == '*' || operators.peek() == '/')) {
                    double b = numbers.pop();
                    double a = numbers.pop();
                    char op = operators.pop();
                    double result = performOperation(a, b, op);
                    numbers.push(result);
                }
                operators.push(c);
            }
        }

        // Perform the remaining operations
        while (!operators.isEmpty()) {
            double b = numbers.pop();
            double a = numbers.pop();
            char op = operators.pop();
            double result = performOperation(a, b, op);
            numbers.push(result);
        }

        return numbers.pop();
    }

    private static double performOperation(double a, double b, char operator) {
        return switch (operator) {
            case '+' -> MathFunctions.add(a, b);
            case '-' -> MathFunctions.subtract(a, b);
            case '*' -> MathFunctions.multiply(a, b);
            case '/' -> MathFunctions.divide(a, b);
            default -> throw new IllegalArgumentException("Inadmissible operation: " + operator);
        };
    }

    private static boolean isNumericDouble(String str) {
        // Use a regular expression to check for an double
        String pattern = "-?\\d+(\\.\\d+)?";

        return str.matches(pattern);
    }

    private static boolean isNumericInteger(String str) {
        // Use a regular expression to check for an integer
        String pattern = "-?\\d+";

        return str.matches(pattern);
    }

    private static boolean isValidExpression(String input) {
        // A pattern to check if only numbers and +-*/ operators are present and correctly positioned
        String pattern = "^-?\\d+(\\.\\d+)?([-+*/]-?\\d+(\\.\\d+)?)*$";

        return input.matches(pattern);
    }

    private static String getNumberFromString(String str) {
        Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "";
        }
    }

    private static boolean endsWithDegreeSymbol(String input) {
        return input.endsWith("°");
    }
}