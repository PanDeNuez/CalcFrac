package parseTexto;

import java.util.regex.*;
import java.util.HashMap;
import java.util.Map;
import calcutron.Calcutron;

public class TextNum {
    private String input;
    private int num1, den1, num2, den2;
    private char op;
    
    // Diccionario básico para números en español.
    private static final Map<String, Integer> BASIC_NUMBERS = new HashMap<>();
    // Diccionario para denominadores especiales (enteros, medios, tercios, etc.).
    private static final Map<String, Integer> SPECIAL_DENOM = new HashMap<>();
    
    static {
        BASIC_NUMBERS.put("cero", 0);
        BASIC_NUMBERS.put("uno", 1);
        BASIC_NUMBERS.put("un", 1);
        BASIC_NUMBERS.put("dos", 2);
        BASIC_NUMBERS.put("tres", 3);
        BASIC_NUMBERS.put("cuatro", 4);
        BASIC_NUMBERS.put("cinco", 5);
        BASIC_NUMBERS.put("seis", 6);
        BASIC_NUMBERS.put("siete", 7);
        BASIC_NUMBERS.put("ocho", 8);
        BASIC_NUMBERS.put("nueve", 9);
        BASIC_NUMBERS.put("diez", 10);
        BASIC_NUMBERS.put("once", 11);
        BASIC_NUMBERS.put("doce", 12);
        BASIC_NUMBERS.put("trece", 13);
        BASIC_NUMBERS.put("catorce", 14);
        BASIC_NUMBERS.put("quince", 15);
        BASIC_NUMBERS.put("dieciseis", 16);
        BASIC_NUMBERS.put("dieciséis", 16);
        BASIC_NUMBERS.put("diecisiete", 17);
        BASIC_NUMBERS.put("dieciocho", 18);
        BASIC_NUMBERS.put("diecinueve", 19);
        BASIC_NUMBERS.put("veinte", 20);
        BASIC_NUMBERS.put("treinta", 30);
        BASIC_NUMBERS.put("cuarenta", 40);
        BASIC_NUMBERS.put("cincuenta", 50);
        BASIC_NUMBERS.put("sesenta", 60);
        BASIC_NUMBERS.put("setenta", 70);
        BASIC_NUMBERS.put("ochenta", 80);
        BASIC_NUMBERS.put("noventa", 90);
        
        SPECIAL_DENOM.put("entero", 1);
        SPECIAL_DENOM.put("enteros", 1);
        SPECIAL_DENOM.put("medio", 2);
        SPECIAL_DENOM.put("medios", 2);
        SPECIAL_DENOM.put("tercio", 3);
        SPECIAL_DENOM.put("tercios", 3);
        SPECIAL_DENOM.put("cuarto", 4);
        SPECIAL_DENOM.put("cuartos", 4);
        SPECIAL_DENOM.put("quinto", 5);
        SPECIAL_DENOM.put("quintos", 5);
        SPECIAL_DENOM.put("sexto", 6);
        SPECIAL_DENOM.put("sextos", 6);
        SPECIAL_DENOM.put("séptimo", 7);
        SPECIAL_DENOM.put("septimo", 7);
        SPECIAL_DENOM.put("séptimos", 7);
        SPECIAL_DENOM.put("septimos", 7);
        SPECIAL_DENOM.put("octavo", 8);
        SPECIAL_DENOM.put("octavos", 8);
        SPECIAL_DENOM.put("noveno", 9);
        SPECIAL_DENOM.put("novenos", 9);
        SPECIAL_DENOM.put("décimo", 10);
        SPECIAL_DENOM.put("decimo", 10);
        SPECIAL_DENOM.put("décimos", 10);
        SPECIAL_DENOM.put("decimos", 10);
    }
    
    public TextNum(String input) {
        this.input = input;
        parseText();
    }
    
    private void parseText() {
        // Verificar la cantidad de operadores en la cadena.
        int opCount = 0;
        String[] operators = {"más", "menos", "por", "entre"};
        for (String opStr : operators) {
            Pattern p = Pattern.compile(opStr, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(input);
            while(m.find()){
                opCount++;
            }
        }
        if(opCount == 0) {
            throw new IllegalArgumentException("Error: No se encontró operador en la entrada.");
        } else if(opCount > 1) {
            throw new IllegalArgumentException("Error: Se encontró más de un operador en la entrada.");
        }
        
        // La expresión esperada:
        // [numerador1] [denominador1] [operador] [numerador2] [denominador2]
        Pattern pattern = Pattern.compile("^\\s*([a-záéíóúñ\\s]+?)\\s+([a-záéíóúñ]+)\\s+(más|menos|por|entre)\\s+([a-záéíóúñ\\s]+?)\\s+([a-záéíóúñ]+)\\s*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        
        if(matcher.find()){
            String numStr1 = matcher.group(1).toLowerCase().trim();
            String denStr1 = matcher.group(2).toLowerCase().trim();
            String opStr   = matcher.group(3).toLowerCase().trim();
            String numStr2 = matcher.group(4).toLowerCase().trim();
            String denStr2 = matcher.group(5).toLowerCase().trim();
            
            // Si falta alguna cantidad, se lanza error.
            if(numStr1.isEmpty() || denStr1.isEmpty() || numStr2.isEmpty() || denStr2.isEmpty()){
                throw new IllegalArgumentException("Error: Falta alguna cantidad en la entrada.");
            }
            
            // Remover "avos" si está presente.
            if(denStr1.endsWith("avos")) {
                denStr1 = denStr1.substring(0, denStr1.length() - "avos".length());
            }
            if(denStr2.endsWith("avos")) {
                denStr2 = denStr2.substring(0, denStr2.length() - "avos".length());
            }
            
            num1 = convertWordsToNumber(numStr1);
            den1 = convertWordsToNumber(denStr1);
            num2 = convertWordsToNumber(numStr2);
            den2 = convertWordsToNumber(denStr2);
            op = convertOperator(opStr);
        } else {
            throw new IllegalArgumentException("Error: Formato de texto inválido.");
        }
    }
    
    private char convertOperator(String opText) {
        switch(opText){
            case "más": return '+';
            case "menos": return '-';
            case "por": return '*';
            case "entre": return '/';
            default: throw new IllegalArgumentException("Operador desconocido: " + opText);
        }
    }
    
    private int convertWordsToNumber(String text) {
        String s = text.replaceAll("\\s+", "");
        // Si coincide con algún denominador especial.
        if (SPECIAL_DENOM.containsKey(s)) {
            return SPECIAL_DENOM.get(s);
        }
        int value;
        if(s.contains("mil")){
            String[] parts = s.split("mil", 2);
            int thousands = parts[0].isEmpty() ? 1 : parseNumberUnder1000(parts[0]);
            int remainder = (parts.length > 1 && !parts[1].isEmpty()) ? parseNumberUnder1000(parts[1]) : 0;
            value = thousands * 1000 + remainder;
        } else {
            value = parseNumberUnder1000(s);
        }
        if(value == 0 && !s.equals("cero")){
            throw new IllegalArgumentException("Error: Número mal escrito: " + text);
        }
        return value;
    }
    
    private int parseNumberUnder1000(String s) {
        int result = 0;
        if(s.startsWith("novecientos")){
            result += 900;
            s = s.substring("novecientos".length());
        } else if(s.startsWith("ochocientos")){
            result += 800;
            s = s.substring("ochocientos".length());
        } else if(s.startsWith("setecientos")){
            result += 700;
            s = s.substring("setecientos".length());
        } else if(s.startsWith("seiscientos")){
            result += 600;
            s = s.substring("seiscientos".length());
        } else if(s.startsWith("quinientos")){
            result += 500;
            s = s.substring("quinientos".length());
        } else if(s.startsWith("cuatrocientos")){
            result += 400;
            s = s.substring("cuatrocientos".length());
        } else if(s.startsWith("trescientos")){
            result += 300;
            s = s.substring("trescientos".length());
        } else if(s.startsWith("doscientos")){
            result += 200;
            s = s.substring("doscientos".length());
        } else if(s.startsWith("cien")){
            if(s.equals("cien")){
                result += 100;
                s = "";
            } else if(s.startsWith("ciento")){
                result += 100;
                s = s.substring("ciento".length());
            }
        }
        
        if(!s.isEmpty()){
            if(s.startsWith("veinti")){
                result += 20;
                s = s.substring("veinti".length());
                Integer unit = lookupBasic(s);
                if(unit != null){
                    result += unit;
                    s = "";
                }
            } else {
                String[] tensWords = {"treinta", "cuarenta", "cincuenta", "sesenta", "setenta", "ochenta", "noventa"};
                boolean foundTens = false;
                for(String tensWord : tensWords){
                    if(s.startsWith(tensWord)){
                        result += BASIC_NUMBERS.get(tensWord);
                        s = s.substring(tensWord.length());
                        foundTens = true;
                        break;
                    }
                }
                if(foundTens){
                    if(s.startsWith("y")){
                        s = s.substring(1);
                        Integer unit = lookupBasic(s);
                        if(unit != null){
                            result += unit;
                            s = "";
                        } else {
                            for(String key : BASIC_NUMBERS.keySet()){
                                if(s.startsWith(key)){
                                    result += BASIC_NUMBERS.get(key);
                                    s = s.substring(key.length());
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    Integer basic = lookupBasic(s);
                    if(basic != null){
                        result += basic;
                        s = "";
                    } else {
                        for(String key : BASIC_NUMBERS.keySet()){
                            if(s.startsWith(key)){
                                result += BASIC_NUMBERS.get(key);
                                s = s.substring(key.length());
                                break;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
    
    private Integer lookupBasic(String s) {
        return BASIC_NUMBERS.containsKey(s) ? BASIC_NUMBERS.get(s) : null;
    }
    
    public String process() {
        Calcutron calcutron = new Calcutron(num1, den1, num2, den2, op);
        int[] res = calcutron.calculate();
        NumText numText = new NumText(res);
        String resultadoParseado = numText.convert();
        return resultadoParseado;
    }
    
    public int getNum1() { return num1; }
    public int getDen1() { return den1; }
    public char getOp() { return op; }
    public int getNum2() { return num2; }
    public int getDen2() { return den2; }
}
