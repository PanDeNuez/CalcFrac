package calcfrac;

import java.util.Scanner;
import parseTexto.TextNum;

public class CalcFrac {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Inserte la operación en texto:");
        String input = scanner.nextLine();
        
        try {
            TextNum textNum = new TextNum(input);
            String resultadoParseado = textNum.process();
            System.out.println("Resultado: " + resultadoParseado);
        } catch(Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
