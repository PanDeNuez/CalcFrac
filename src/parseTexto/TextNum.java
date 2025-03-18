package parseTexto;

import java.text.Normalizer;
import java.util.regex.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import calcutron.Calcutron;

public class TextNum {
    private String input;
    private int num1, den1, num2, den2;
    private char op;
    
    // Diccionario básico para números en español (se esperan coincidencias exactas).
    private static final Map<String, Integer> BASIC_NUMBERS = new HashMap<>();
    // Diccionario para denominadores válidos.
    private static final Map<String, Integer> SPECIAL_DENOM = new HashMap<>();
    
    static {
        // Números básicos
        BASIC_NUMBERS.put("cero", 0);
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
        BASIC_NUMBERS.put("diecisiete", 17);
        BASIC_NUMBERS.put("dieciocho", 18);
        BASIC_NUMBERS.put("diecinueve", 19);
        BASIC_NUMBERS.put("veinte", 20);
        // Compuestos de veintiuno a veintinueve
        BASIC_NUMBERS.put("veintiuno", 21);
        BASIC_NUMBERS.put("veintidos", 22);
        BASIC_NUMBERS.put("veintitres", 23);
        BASIC_NUMBERS.put("veinticuatro", 24);
        BASIC_NUMBERS.put("veinticinco", 25);
        BASIC_NUMBERS.put("veintiseis", 26);
        BASIC_NUMBERS.put("veintisiete", 27);
        BASIC_NUMBERS.put("veintiocho", 28);
        BASIC_NUMBERS.put("veintinueve", 29);
        BASIC_NUMBERS.put("treinta", 30);
        BASIC_NUMBERS.put("cuarenta", 40);
        BASIC_NUMBERS.put("cincuenta", 50);
        BASIC_NUMBERS.put("sesenta", 60);
        BASIC_NUMBERS.put("setenta", 70);
        BASIC_NUMBERS.put("ochenta", 80);
        BASIC_NUMBERS.put("noventa", 90);
        
        // Denominadores válidos (se deben escribir exactamente)
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
        SPECIAL_DENOM.put("septimo", 7);
        SPECIAL_DENOM.put("septimos", 7);
        SPECIAL_DENOM.put("octavo", 8);
        SPECIAL_DENOM.put("octavos", 8);
        SPECIAL_DENOM.put("noveno", 9);
        SPECIAL_DENOM.put("novenos", 9);
        SPECIAL_DENOM.put("decimo", 10);
        SPECIAL_DENOM.put("decimos", 10);
    }
    
    public TextNum(String input) {
        // Normaliza la entrada (minúsculas, sin acentos)
        this.input = normalize(input);
        parseText();
    }
    
    private String normalize(String input) {
        String lower = input.toLowerCase().trim();
        String normalized = Normalizer.normalize(lower, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        return normalized;
    }
    
    private void parseText() {
        // Verificar la cantidad de operadores en la cadena.
        int opCount = 0;
        String[] operators = {"mas", "menos", "por", "entre"};
        for (String opStr : operators) {
            Pattern p = Pattern.compile("\\b" + opStr + "\\b", Pattern.CASE_INSENSITIVE);
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
        
        // Se espera el formato:
        // [numerador1] [denominador1] [operador] [numerador2] [denominador2]
        Pattern pattern = Pattern.compile("^\\s*([a-z\\s]+?)\\s+([a-z]+)\\s+(mas|menos|por|entre)\\s+([a-z\\s]+?)\\s+([a-z]+)\\s*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        
        if(matcher.find()){
            String numStr1 = matcher.group(1).trim();
            String denStr1 = matcher.group(2).trim();
            String opStr   = matcher.group(3).trim();
            String numStr2 = matcher.group(4).trim();
            String denStr2 = matcher.group(5).trim();
            
            // Validar que los denominadores se escriban correctamente.
            if(!SPECIAL_DENOM.containsKey(denStr1)) {
                throw new IllegalArgumentException("Error: Denominador mal escrito: " + denStr1);
            }
            if(!SPECIAL_DENOM.containsKey(denStr2)) {
                throw new IllegalArgumentException("Error: Denominador mal escrito: " + denStr2);
            }
            
            num1 = convertWordsToNumber(numStr1, false);
            den1 = SPECIAL_DENOM.get(denStr1);
            num2 = convertWordsToNumber(numStr2, false);
            den2 = SPECIAL_DENOM.get(denStr2);
            op = convertOperator(opStr);
        } else {
            throw new IllegalArgumentException("Error: Formato de texto inválido.");
        }
    }
    
    private char convertOperator(String opText) {
        switch(opText){
            case "mas": return '+';
            case "menos": return '-';
            case "por": return '*';
            case "entre": return '/';
            default: throw new IllegalArgumentException("Operador desconocido: " + opText);
        }
    }
    
    /**
     * Convierte la representación en palabras a número (para numeradores).
     * Para denominadores se exige validación exacta externa.
     */
    private int convertWordsToNumber(String text, boolean isDenom) {
        String normalizedText = normalize(text);
        // Para denominadores se espera que la entrada ya sea exacta, por lo que no se procesa aquí.
        if(isDenom) {
            if(!SPECIAL_DENOM.containsKey(normalizedText)) {
                throw new IllegalArgumentException("Error: Denominador mal escrito: " + text);
            }
            return SPECIAL_DENOM.get(normalizedText);
        }
        // Tokenizar la cadena (se espera un formato estricto).
        String[] tokens = normalizedText.split("\\s+");
        int value = parseNumberTokens(tokens);
        if(value == 0 && !normalizedText.equals("cero")){
            throw new IllegalArgumentException("Error: Número mal escrito: " + text);
        }
        return value;
    }
    
    /**
     * Parsea la secuencia de tokens y devuelve el número resultante.
     * Soporta números hasta 999 y la palabra "mil" para números en los miles.
     */
    private int parseNumberTokens(String[] tokens) {
        int result = 0;
        // Buscar si se especifica la parte de "mil"
        for (int j = 0; j < tokens.length; j++) {
            if (tokens[j].equals("mil")) {
                int thousandPart = 0;
                if (j == 0) {
                    thousandPart = 1; // Solo "mil" significa 1000.
                } else {
                    thousandPart = parseUnderThousand(tokens, 0, j);
                }
                result += thousandPart * 1000;
                if(j + 1 < tokens.length) {
                    result += parseUnderThousand(tokens, j + 1, tokens.length);
                }
                return result;
            }
        }
        // Si no se encuentra "mil", se parsea como número menor a 1000.
        result = parseUnderThousand(tokens, 0, tokens.length);
        return result;
    }
    
    /**
     * Parsea tokens que representan un número menor a 1000 siguiendo el formato estricto.
     */
    private int parseUnderThousand(String[] tokens, int start, int end) {
        int result = 0;
        int i = start;
        
        // Si es un solo token y coincide exactamente con un número básico, se retorna.
        if (end - start == 1) {
            String token = tokens[start];
            if (BASIC_NUMBERS.containsKey(token)) {
                return BASIC_NUMBERS.get(token);
            } else {
                throw new IllegalArgumentException("Error: Número desconocido: " + token);
            }
        }
        
        // Procesar la parte de las centenas
        if (i < end) {
            String token = tokens[i];
            if(token.equals("cien")) {
                if(end - start == 1) {
                    return 100;
                } else {
                    result += 100;
                    i++;
                }
            } else if(token.equals("ciento")) {
                result += 100;
                i++;
            } else if(token.equals("doscientos")) {
                result += 200;
                i++;
            } else if(token.equals("trescientos")) {
                result += 300;
                i++;
            } else if(token.equals("cuatrocientos")) {
                result += 400;
                i++;
            } else if(token.equals("quinientos")) {
                result += 500;
                i++;
            } else if(token.equals("seiscientos")) {
                result += 600;
                i++;
            } else if(token.equals("setecientos")) {
                result += 700;
                i++;
            } else if(token.equals("ochocientos")) {
                result += 800;
                i++;
            } else if(token.equals("novecientos")) {
                result += 900;
                i++;
            }
        }
        
        // Procesar decenas y unidades
        if (i < end) {
            // Si queda un solo token, debe coincidir exactamente con uno de BASIC_NUMBERS.
            if (end - i == 1) {
                String token = tokens[i];
                if(BASIC_NUMBERS.containsKey(token)) {
                    result += BASIC_NUMBERS.get(token);
                    i++;
                } else {
                    throw new IllegalArgumentException("Error: Número desconocido: " + token);
                }
            } else {
                // Se espera el formato: [decena] ["y" unidad] o simplemente una decena compuesta.
                String token = tokens[i];
                if(BASIC_NUMBERS.containsKey(token) && BASIC_NUMBERS.get(token) >= 20 && BASIC_NUMBERS.get(token) % 10 == 0) {
                    result += BASIC_NUMBERS.get(token);
                    i++;
                    if(i < end) {
                        if(tokens[i].equals("y")) {
                            i++;
                            if(i < end && BASIC_NUMBERS.containsKey(tokens[i]) && BASIC_NUMBERS.get(tokens[i]) < 10) {
                                result += BASIC_NUMBERS.get(tokens[i]);
                                i++;
                            } else {
                                throw new IllegalArgumentException("Error: Se esperaba un número entre uno y nueve después de 'y'");
                            }
                        } else {
                            if(BASIC_NUMBERS.containsKey(tokens[i]) && BASIC_NUMBERS.get(tokens[i]) < 10) {
                                result += BASIC_NUMBERS.get(tokens[i]);
                                i++;
                            } else {
                                throw new IllegalArgumentException("Error: Número desconocido en posición de unidad: " + tokens[i]);
                            }
                        }
                    }
                } else {
                    // Si no es una decena, verificar que el token esté en BASIC_NUMBERS.
                    if(BASIC_NUMBERS.containsKey(token)) {
                        result += BASIC_NUMBERS.get(token);
                        i++;
                    } else {
                        throw new IllegalArgumentException("Error: Número desconocido: " + token);
                    }
                }
            }
        }
        if(i != end) {
            throw new IllegalArgumentException("Error: Secuencia de números no interpretada completamente: " +
                    String.join(" ", Arrays.copyOfRange(tokens, start, end)));
        }
        return result;
    }
    
    public String process() {
        // Se delega el cálculo a la clase Calcutron.
        Calcutron calcutron = new Calcutron(num1, den1, num2, den2, op);
        int[] res = calcutron.calculate();
        NumText numText = new NumText(res);
        String resultadoParseado = numText.convert();
        return resultadoParseado;
    }
    
    // Métodos de acceso
    public int getNum1() { return num1; }
    public int getDen1() { return den1; }
    public char getOp() { return op; }
    public int getNum2() { return num2; }
    public int getDen2() { return den2; }
}