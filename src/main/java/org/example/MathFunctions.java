package org.example.calculator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class MathFunctions {

    public static final double PI = 3.141592653589793;

    public static double add(double num1, double num2) {
        return num1 + num2;
    }

    public static double subtract(double num1, double num2) {
        return num1 - num2;
    }

    public static double multiply(double num1, double num2) {
        return num1 * num2;
    }

    public static double divide(double num1, double num2) {
        if (num2 == 0) {
            return Double.NaN;
        }
        return num1 / num2;
    }

    public static double abs(double num1) {
        return (num1 < 0) ? -num1 : num1;
    }

    public static double pow(double num1, double num2) {
        return Math.pow(num1, num2);
    }

    public static double sqrt(double num) {
        if (num < 0.0) {
            return Double.NaN;
        }

        if (num == 0.0 || num == 1.0) {
            return num;
        }

        double x = num;
        double y = (x + 1.0) / 2.0;
        while (y < x) {
            x = y;
            y = (x + num / x) / 2.0;
        }

        return x;
    }

    public static long factorial(long num) {
        if (num == 0) {
            return 1;
        }
        long result = 1;
        for (long i = 1; i <= num; i++) {
            result *= i;
        }
        return result;
    }

    public static double cos(double num) {
        num = num % (2 * PI);
        double result = 1;
        double term = 1;

        for (int n = 1; n <= 10; n++) {
            term *= -num * num / (2 * n * (2 * n - 1));
            result += term;
        }

        return result;
    }

    public static double sin(double num) {
        num %= (2 * PI);

        if (num < -PI) {
            num += 2 * PI;
        } else if (num > PI) {
            num -= 2 * PI;
        }

        double result = 0;
        double term = num;
        double numerator = num;
        double denominator = 1;
        int sign = 1;

        for (int i = 1; i <= 15; i += 2) {
            result += sign * term;
            numerator *= num * num;
            denominator *= (i + 1) * (i + 2);
            sign = -sign;
            term = numerator / denominator;
        }

        return result;
    }

    public static double tan(double num) {
        return Math.tan(num);
    }

    public static double cot(double num) {
        return cos(num) / sin(num);
    }

    public static String decimalToOctal(String decimalNumber) {
        if (isDecimalNumber(decimalNumber)) {
            String[] parts = decimalNumber.split("\\.");
            BigInteger intPart = new BigInteger(parts[0]);

            StringBuilder hexResult = new StringBuilder(intPart.toString(8));

            if (parts.length > 1) {
                BigDecimal fracPart = new BigDecimal("0." + parts[1]);

                hexResult.append('.');

                for (int i = 0; i < parts[1].length(); i++) {
                    fracPart = fracPart.multiply(BigDecimal.valueOf(8));
                    int digit = fracPart.setScale(0, RoundingMode.DOWN).intValue();
                    hexResult.append(Integer.toHexString(digit));
                    fracPart = fracPart.subtract(BigDecimal.valueOf(digit));
                }
            }
            return removeTrailingZeros(hexResult.toString().toUpperCase());
        } else {
            return "";
        }
    }

    public static String decimalToBinary(String decimalNumber) {
        if (isDecimalNumber(decimalNumber)) {
            String[] parts = decimalNumber.split("\\.");
            BigInteger intPart = new BigInteger(parts[0]);

            StringBuilder hexResult = new StringBuilder(intPart.toString(2));

            if (parts.length > 1) {
                BigDecimal fracPart = new BigDecimal("0." + parts[1]);

                hexResult.append('.');

                for (int i = 0; i < parts[1].length(); i++) {
                    fracPart = fracPart.multiply(BigDecimal.valueOf(2));
                    int digit = fracPart.setScale(0, RoundingMode.DOWN).intValue();
                    hexResult.append(Integer.toHexString(digit));
                    fracPart = fracPart.subtract(BigDecimal.valueOf(digit));
                }
            }
            return removeTrailingZeros(hexResult.toString().toUpperCase());
        } else {
            return "";
        }
    }

    public static String decimalToHexadecimal(String decimalNumber) {
        if (isDecimalNumber(decimalNumber)) {
            String[] parts = decimalNumber.split("\\.");
            BigInteger intPart = new BigInteger(parts[0]);

            StringBuilder hexResult = new StringBuilder(intPart.toString(16));

            if (parts.length > 1) {
                BigDecimal fracPart = new BigDecimal("0." + parts[1]);

                hexResult.append('.');

                for (int i = 0; i < parts[1].length(); i++) {
                    fracPart = fracPart.multiply(BigDecimal.valueOf(16));
                    int digit = fracPart.setScale(0, RoundingMode.DOWN).intValue();
                    hexResult.append(Integer.toHexString(digit));
                    fracPart = fracPart.subtract(BigDecimal.valueOf(digit));
                }
            }
            return removeTrailingZeros(hexResult.toString().toUpperCase());
        } else {
            return "";
        }
    }

    public static String octalToDecimal(String octalNumber) {
        if (isOctalNumber(octalNumber)) {
            // Split the string into integer and fractional parts
            String[] parts = octalNumber.split("\\.");
            String wholePart = parts[0];
            String fractionalPart = (parts.length > 1) ? parts[1] : "0";

            // Convert the integer part
            BigInteger decimalWholePart = new BigInteger(wholePart, 8);

            // Convert the fractional part
            BigDecimal decimalFractionalPart = BigDecimal.ZERO;
            BigDecimal fractionBase = BigDecimal.ONE.divide(BigDecimal.valueOf(8)); // For fractional digits

            for (int i = 0; i < fractionalPart.length(); i++) {
                char digit = fractionalPart.charAt(i);
                int digitValue = digit - '0';
                decimalFractionalPart = decimalFractionalPart.add(BigDecimal.valueOf(digitValue).multiply(fractionBase));
                fractionBase = fractionBase.divide(BigDecimal.valueOf(8));
            }

            // Adding whole and fractional parts
            return removeTrailingZeros(new BigDecimal(decimalWholePart).add(decimalFractionalPart).toString());
        } else {
            return "";
        }
    }

    public static String octalToBinary(String octalNumber) {
        if (isOctalNumber(octalNumber)) {
            return removeTrailingZeros(decimalToBinary(octalToDecimal(octalNumber)));
        } else {
            return "";
        }
    }

    public static String octalToHexadecimal(String octalNumber) {
        if (isOctalNumber(octalNumber)) {
            return removeTrailingZeros(decimalToHexadecimal(octalToDecimal(octalNumber)));
        } else {
            return "";
        }
    }

    public static String binaryToDecimal(String binaryNumber) {
        if (isBinaryNumber(binaryNumber)) {
            // Split the string into integer and fractional parts
            String[] parts = binaryNumber.split("\\.");
            String wholePart = parts[0];
            String fractionalPart = (parts.length > 1) ? parts[1] : "0";

            // Convert the integer part
            BigInteger decimalWholePart = new BigInteger(wholePart, 2);

            // Convert the fractional part
            BigDecimal decimalFractionalPart = BigDecimal.ZERO;
            BigDecimal fractionBase = BigDecimal.ONE.divide(BigDecimal.valueOf(2)); // For fractional digits

            for (int i = 0; i < fractionalPart.length(); i++) {
                char digit = fractionalPart.charAt(i);
                int digitValue = digit - '0';
                decimalFractionalPart = decimalFractionalPart.add(BigDecimal.valueOf(digitValue).multiply(fractionBase));
                fractionBase = fractionBase.divide(BigDecimal.valueOf(2));
            }

            // Adding whole and fractional parts
            return removeTrailingZeros(new BigDecimal(decimalWholePart).add(decimalFractionalPart).toString());
        } else {
            return "";
        }
    }

    public static String binaryToOctal(String binaryNumber) {
        if (isBinaryNumber(binaryNumber)) {
            return removeTrailingZeros(decimalToOctal(binaryToDecimal(binaryNumber)));
        } else {
            return "";
        }
    }

    public static String binaryToHexadecimal(String binaryNumber) {
        if (isBinaryNumber(binaryNumber)) {
            return removeTrailingZeros(decimalToHexadecimal(binaryToDecimal(binaryNumber)));
        } else {
            return "";
        }
    }

    public static String hexadecimalToDecimal(String hexadecimalNumber) {
        if (isHexadecimalNumber(hexadecimalNumber)) {
            // Split the string into integer and fractional parts
            String[] parts = hexadecimalNumber.split("\\.");
            String wholePart = parts[0];
            String fractionalPart = (parts.length > 1) ? parts[1] : "0";

            // Convert the integer part
            BigInteger decimalWholePart = new BigInteger(wholePart, 16);

            // Convert the fractional part
            BigDecimal decimalFractionalPart = BigDecimal.ZERO;
            BigDecimal fractionBase = BigDecimal.ONE.divide(BigDecimal.valueOf(16)); // For fractional digits

            for (int i = 0; i < fractionalPart.length(); i++) {
                char digit = fractionalPart.charAt(i);
                int digitValue = digit - '0';
                decimalFractionalPart = decimalFractionalPart.add(BigDecimal.valueOf(digitValue).multiply(fractionBase));
                fractionBase = fractionBase.divide(BigDecimal.valueOf(16));
            }

            // Adding whole and fractional parts
            return removeTrailingZeros(new BigDecimal(decimalWholePart).add(decimalFractionalPart).toString());
        } else {
            return "";
        }
    }

    public static String hexadecimalToOctal(String hexadecimalNumber) {
        if (isHexadecimalNumber(hexadecimalNumber)) {
            return removeTrailingZeros(decimalToOctal(hexadecimalToDecimal(hexadecimalNumber)));
        } else {
            return "";
        }
    }

    public static String hexadecimalToBinary(String hexadecimalNumber) {
        if (isHexadecimalNumber(hexadecimalNumber)) {
            return removeTrailingZeros(decimalToBinary(hexadecimalToDecimal(hexadecimalNumber)));
        } else {
            return "";
        }
    }

    public static String removeTrailingZeros(String input) {
        String[] parts = input.split("\\.");

        if (parts.length < 2) {
            return input;
        }

        String fractionPart = parts[1].replaceAll("0*$", "");

        if (fractionPart.isEmpty()) {
            return parts[0];
        }

        return parts[0] + "." + fractionPart;
    }

    public static boolean isDecimalNumber(String str) {
        return str.matches("\\d+(\\.\\d+)?");
    }

    public static boolean isOctalNumber(String str) {
        return str.matches("[0-7]+(\\.[0-7]+)?");
    }

    public static boolean isBinaryNumber(String str) {
        return str.matches("[0-1]+(\\.[0-1]+)?");
    }

    public static boolean isHexadecimalNumber(String str) {
        return str.matches("[0-9a-fA-F]+(\\.[0-9a-fA-F]+)?");
    }
}
