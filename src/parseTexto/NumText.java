package parseTexto;

public class NumText {
    private int[] fraction; // fraction[0]: numerador, fraction[1]: denominador

    public NumText(int[] fraction) {
        if (fraction == null || fraction.length != 2) {
            throw new IllegalArgumentException("El arreglo debe tener dos enteros.");
        }
        this.fraction = fraction;
    }

    public String convert() {
        int num = fraction[0];
        int den = fraction[1];

        if (den == 0) {
            throw new ArithmeticException("Error: El denominador del resultado es cero.");
        }
        
        String numeratorWord = (num == 1) ? "un" : convertNumberToWords(num);
        String denominatorWord;
        if (den >= 1 && den <= 10) {
            denominatorWord = getSpecialDenomName(den, num);
        } else if (den >= 11 && den <= 15) {
            denominatorWord = getTeensDenomName(den, num);
        } else {
            denominatorWord = convertNumberToWordsConcatenated(den) + (num == 1 ? "avo" : "avos");
        }
        return numeratorWord + " " + denominatorWord;
    }

    private String getSpecialDenomName(int den, int num) {
        String name = "";
        switch (den) {
            case 1: name = "entero"; break;
            case 2: name = "medio"; break;
            case 3: name = "tercio"; break;
            case 4: name = "cuarto"; break;
            case 5: name = "quinto"; break;
            case 6: name = "sexto"; break;
            case 7: name = "séptimo"; break;
            case 8: name = "octavo"; break;
            case 9: name = "noveno"; break;
            case 10: name = "décimo"; break;
        }
        if (num != 1) {
            name += "s";
        }
        return name;
    }

    private String getTeensDenomName(int den, int num) {
        String name = "";
        switch (den) {
            case 11: name = "once"; break;
            case 12: name = "doce"; break;
            case 13: name = "trece"; break;
            case 14: name = "catorce"; break;
            case 15: name = "quince"; break;
        }
        name += (num == 1 ? "avo" : "avos");
        return name;
    }

    private String convertNumberToWords(int n) {
        if (n < 0) {
            return "menos " + convertNumberToWords(-n);
        }
        if (n == 0) {
            return "cero";
        }
        StringBuilder words = new StringBuilder();
        if (n >= 1000) {
            int thousands = n / 1000;
            int remainder = n % 1000;
            if (thousands == 1) {
                words.append("mil");
            } else {
                words.append(convertNumberToWords(thousands)).append(" mil");
            }
            if (remainder > 0) {
                words.append(" ").append(convertNumberToWords(remainder));
            }
        } else if (n < 1000) {
            if (n < 100) {
                words.append(convertDoubleDigit(n));
            } else {
                int hundreds = n / 100;
                int remainder = n % 100;
                if (n == 100) {
                    words.append("cien");
                } else {
                    switch (hundreds) {
                        case 1: words.append("ciento"); break;
                        case 2: words.append("doscientos"); break;
                        case 3: words.append("trescientos"); break;
                        case 4: words.append("cuatrocientos"); break;
                        case 5: words.append("quinientos"); break;
                        case 6: words.append("seiscientos"); break;
                        case 7: words.append("setecientos"); break;
                        case 8: words.append("ochocientos"); break;
                        case 9: words.append("novecientos"); break;
                    }
                    if (remainder > 0) {
                        words.append(" ").append(convertDoubleDigit(remainder));
                    }
                }
            }
        }
        return words.toString();
    }

    private String convertDoubleDigit(int n) {
        if (n < 16) {
            switch (n) {
                case 0: return "";
                case 1: return "uno";
                case 2: return "dos";
                case 3: return "tres";
                case 4: return "cuatro";
                case 5: return "cinco";
                case 6: return "seis";
                case 7: return "siete";
                case 8: return "ocho";
                case 9: return "nueve";
                case 10: return "diez";
                case 11: return "once";
                case 12: return "doce";
                case 13: return "trece";
                case 14: return "catorce";
                case 15: return "quince";
            }
        }
        if (n < 20) {
            return "dieci" + convertDoubleDigit(n - 10);
        }
        if (n < 30) {
            if (n == 20) return "veinte";
            else return "veinti" + convertDoubleDigit(n - 20);
        }
        int tens = n / 10;
        int ones = n % 10;
        String tensWord = "";
        switch (tens) {
            case 3: tensWord = "treinta"; break;
            case 4: tensWord = "cuarenta"; break;
            case 5: tensWord = "cincuenta"; break;
            case 6: tensWord = "sesenta"; break;
            case 7: tensWord = "setenta"; break;
            case 8: tensWord = "ochenta"; break;
            case 9: tensWord = "noventa"; break;
        }
        if (ones == 0) {
            return tensWord;
        } else {
            return tensWord + " y " + convertDoubleDigit(ones);
        }
    }

    private String convertNumberToWordsConcatenated(int n) {
        if (n < 0) {
            return "menos" + convertNumberToWordsConcatenated(-n);
        }
        if (n == 0) {
            return "cero";
        }
        String result = "";
        if (n >= 1000) {
            int thousands = n / 1000;
            int remainder = n % 1000;
            if (thousands == 1) {
                result += "mil";
            } else {
                result += convertNumberToWordsConcatenated(thousands) + "mil";
            }
            if (remainder > 0) {
                result += convertHundredsConcatenated(remainder);
            }
        } else {
            result += convertHundredsConcatenated(n);
        }
        return result;
    }

    private String convertHundredsConcatenated(int n) {
        String res = "";
        if (n < 100) {
            res += convertDoubleDigitConcatenated(n);
        } else {
            int hundreds = n / 100;
            int remainder = n % 100;
            if (n == 100) {
                res += "cien";
            } else {
                switch (hundreds) {
                    case 1: res += "ciento"; break;
                    case 2: res += "doscientos"; break;
                    case 3: res += "trescientos"; break;
                    case 4: res += "cuatrocientos"; break;
                    case 5: res += "quinientos"; break;
                    case 6: res += "seiscientos"; break;
                    case 7: res += "setecientos"; break;
                    case 8: res += "ochocientos"; break;
                    case 9: res += "novecientos"; break;
                }
                if (remainder > 0) {
                    res += convertDoubleDigitConcatenated(remainder);
                }
            }
        }
        return res;
    }

    private String convertDoubleDigitConcatenated(int n) {
        if (n < 16) {
            switch (n) {
                case 0: return "";
                case 1: return "uno";
                case 2: return "dos";
                case 3: return "tres";
                case 4: return "cuatro";
                case 5: return "cinco";
                case 6: return "seis";
                case 7: return "siete";
                case 8: return "ocho";
                case 9: return "nueve";
                case 10: return "diez";
                case 11: return "once";
                case 12: return "doce";
                case 13: return "trece";
                case 14: return "catorce";
                case 15: return "quince";
            }
        }
        if (n < 20) {
            return "dieci" + convertDoubleDigitConcatenated(n - 10);
        }
        if (n < 30) {
            if (n == 20) return "veinte";
            else return "veinti" + convertDoubleDigitConcatenated(n - 20);
        }
        int tens = n / 10;
        int ones = n % 10;
        String tensWord = "";
        switch (tens) {
            case 3: tensWord = "treinta"; break;
            case 4: tensWord = "cuarenta"; break;
            case 5: tensWord = "cincuenta"; break;
            case 6: tensWord = "sesenta"; break;
            case 7: tensWord = "setenta"; break;
            case 8: tensWord = "ochenta"; break;
            case 9: tensWord = "noventa"; break;
        }
        if (ones == 0) {
            return tensWord;
        } else {
            return tensWord + "y" + convertDoubleDigitConcatenated(ones);
        }
    }
}
