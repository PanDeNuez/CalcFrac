package calcutron;

public class Calcutron {
    private int num1, den1, num2, den2;
    private char operator;

    public Calcutron(int num1, int den1, int num2, int den2, char operator) {
        if (den1 == 0 || den2 == 0) {
            throw new ArithmeticException("Error: Denominador ingresado es cero.");
        }
        this.num1 = num1;
        this.den1 = den1;
        this.num2 = num2;
        this.den2 = den2;
        this.operator = operator;
    }

    public int[] calculate() {
        int resultNum = 0, resultDen = 0;

        switch (operator) {
            case '+':
                resultNum = (num1 * den2) + (num2 * den1);
                resultDen = den1 * den2;
                break;
            case '-':
                resultNum = (num1 * den2) - (num2 * den1);
                resultDen = den1 * den2;
                break;
            case '*':
                resultNum = num1 * num2;
                resultDen = den1 * den2;
                break;
            case '/':
                if (num2 == 0) {
                    throw new ArithmeticException("Error: División por cero en la operación.");
                }
                resultNum = num1 * den2;
                resultDen = den1 * num2;
                break;
            default:
                throw new IllegalArgumentException("Operador no válido");
        }
        if (resultDen == 0) {
            throw new ArithmeticException("Error: Denominador resultante es cero.");
        }
        return new int[]{resultNum, resultDen};
    }
}
