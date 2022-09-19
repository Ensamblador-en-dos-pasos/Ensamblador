import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Operando {
    Conversor conv = new Conversor();
    Validador val = new Validador();
    int valor;

    /**
     * Método para validar el operando
     * 
     * @param cadena valor del operando
     * @param BPC    Bytes por calcular
     * @return Retorna el ADDR
     */
    public String valOperando(String cadena, Integer BPC) {
        // Creo los evaluadores para los indizados
        Pattern ind5bits; // Indizado 5 bits
        Matcher buscador1;

        Pattern ind9bits; // Indizado 9 bits
        Matcher buscador2;

        Pattern ind16bits; // Indizado 16 bits
        Matcher buscador3;

        Pattern prepost; // Pre-post Incremento-Decremento
        Matcher buscador4;

        Pattern acum;// Acumulador
        Matcher buscador5;

        Pattern acumindr;// Acumuldor indirecto
        Matcher buscador6;

        Pattern indr16bits;// Indirecto 16 bits
        Matcher buscador7;

        Pattern imm;
        Matcher buscador8;

        Pattern dr;
        Matcher buscador9;

        Pattern ext;
        Matcher buscador10;

        // Desarrollo de la expresion regular
        // Indizados
        ind5bits = Pattern.compile("^[-]*[0-9]+,[X|Y]$|^[-]*[0-9]+,[S]P$|^[-]*[0-9]+,[P]C$|^,[X|Y]$|^,[S]P$|,[P]C$");
        buscador1 = ind5bits.matcher(cadena);
        boolean encontrado = buscador1.find();

        ind9bits = Pattern.compile("^[-]*[0-9]+,[X|Y]$|^[-]*[0-9]+,[S]P$|^[-]*[0-9]+,[P]C$");
        buscador2 = ind9bits.matcher(cadena);
        boolean encontrado2 = buscador2.find();

        ind16bits = Pattern.compile("^[-]*[0-9]+,[X|Y]$|^[-]*[0-9]+,[S]P$|^[-]*[0-9]+,[P]C$");
        buscador3 = ind16bits.matcher(cadena);
        boolean encontrado3 = buscador3.find();

        prepost = Pattern.compile(
                "^[-]*[1-8]+,[+|-][X|Y]$|^[-]*[1-8]+,[X|Y][+|-]$|^[-]*[1-8]+,[+|-][S]P$|^[-]*[1-8]+,[S]P[+|-]$|^[-]*[1-8]+,[+|-][P]C$|^[-]*[1-8]+,[P]C[+|-]$");
        buscador4 = prepost.matcher(cadena);
        boolean encontrado4 = buscador4.find();

        acum = Pattern.compile("^[A|B|D]+,[X|Y]$|^[A|B|D]+,[S]P$|^[A|B|D]+,[P]C$");
        buscador5 = acum.matcher(cadena);
        boolean encontrado5 = buscador5.find();

        acumindr = Pattern
                .compile("^[\\[][D]+,[X|Y][\\]]$|^[\\[][D]+,[X|Y][\\]]$|^[\\[][D]+,[S]P[\\]]$|^[\\[][D]+,[P]C[\\]]$");
        buscador6 = acumindr.matcher(cadena);
        boolean encontrado6 = buscador6.find();

        indr16bits = Pattern.compile(
                "^[\\[][-]*[0-9]+,[X|Y][\\]]$|^[\\[][-]*[0-9]+,[X|Y][\\]]$|^[\\[][-]*[0-9]+,[S]P[\\]]$|^[\\[][-]*[0-9]+,[P]C[\\]]$");
        buscador7 = indr16bits.matcher(cadena);
        boolean encontrado7 = buscador7.find();

        // Inmediato
        imm = Pattern.compile("^[#][0-9]+$|^[#][\\$][0-9A-F]+$|^[#][@][0-7]+$|^[#][%][0-1]+$");
        buscador8 = imm.matcher(cadena);
        boolean encontrado8 = buscador8.find();

        // Directo-Extendido numérico
        dr = Pattern.compile("^[0-9]+$|^[\\$][0-9A-F]+$|^[@][0-7]+$|^[%][0-1]+$");
        buscador9 = dr.matcher(cadena);
        boolean encontrado9 = buscador9.find();

        // Extendido con etiqueta
        ext = Pattern.compile("^[a-zA-Z]+\\w*+$");
        buscador10 = ext.matcher(cadena);
        boolean encontrado10 = buscador10.find();

        if (encontrado) {// IDX 5 bits
            int valor;
            String[] result = cadena.split(",");
            if (result[0] == "") {
                valor = 0;
            } else {
                valor = Integer.parseInt(result[0]);
            }

            if (range5(valor)) {
                cadena = "IDX";
            } else {
                System.out.println("Error, este codop debe ser de 5 bits");
            }

        } else if (encontrado2) {// IDX 9 bits

        } else if (encontrado3) {// IDX 16 bits

        } else if (encontrado4) {// IDX Pre-post

        } else if (encontrado5) {// IDX acumulador

        } else if (encontrado6) {// IDX acumulador indirecto

        } else if (encontrado7) {// IDX 16bits

        } else if (encontrado8) {// Inmediato
            switch (basesnum(cadena)) {
                case 1:// Hexadecimal
                    valor = conv.hextodecimm(cadena);
                    switch (BPC) {
                        case 1:
                            if (range8(valor)) {
                                cadena = "Inmediato";
                            } else {
                                System.out.println("Error, este codop debe ser de 8bits");
                            }

                            break;
                        case 2:
                            if (range16(valor)) {
                                cadena = "Inmediato";
                            } else {
                                System.out.println("Error, este codop debe ser de 16bits");
                            }
                            break;

                        default:
                            break;
                    }
                    break;
                case 2:// Octal
                    valor = conv.octtodecimm(cadena);
                    switch (BPC) {
                        case 1:
                            if (range8(valor)) {
                                cadena = "Inmediato";
                            } else {
                                System.out.println("Error, este codop debe ser de 8bits");
                            }

                            break;
                        case 2:
                            if (range16(valor)) {
                                cadena = "Inmediato";
                            } else {
                                System.out.println("Error, este codop debe ser de 16bits");
                            }
                            break;

                        default:
                            break;
                    }

                    break;
                case 3:// Binario
                    valor = conv.bintodecimm(cadena);
                    switch (BPC) {
                        case 1:
                            if (range8(valor)) {
                                cadena = "Inmediato";
                            } else {
                                System.out.println("Error, este codop debe ser de 8bits");
                            }

                            break;
                        case 2:
                            if (range16(valor)) {
                                cadena = "Inmediato";
                            } else {
                                System.out.println("Error, este codop debe ser de 16bits");
                            }
                            break;

                        default:
                            break;
                    }

                    break;
                case 4:// Decimal
                    valor = conv.decimm(cadena);
                    switch (BPC) {
                        case 1:
                            if (range8(valor)) {
                                cadena = "Inmediato";
                            } else {
                                System.out.println("Error, este codop debe ser de 8bits");
                            }

                            break;
                        case 2:
                            if (range16(valor)) {
                                cadena = "Inmediato";
                            } else {
                                System.out.println("Error, este codop debe ser de 16bits");
                            }
                            break;

                        default:
                            break;
                    }
                    break;

                default:
                    break;
            }

        } else if (encontrado9) {// DIRECTO-Extendido
            switch (basesnum(cadena)) {
                case 1:// Hexadecimal
                    valor = conv.hextodec(cadena);
                    if (range8(valor)) {
                        cadena = "Directo";
                    } else if (range16(valor)) {
                        cadena = "Extendido";
                    } else {
                        System.out.println("Error de longitud de bits");
                    }
                    break;
                case 2:// Octal
                    valor = conv.octtodec(cadena);
                    if (range8(valor)) {
                        cadena = "Directo";
                    } else if (range16(valor)) {
                        cadena = "Extendido";
                    } else {
                        System.out.println("Error de longitud de bits");
                    }

                    break;
                case 3:// Binario
                    valor = conv.bintodec(cadena);
                    if (range8(valor)) {
                        cadena = "Directo";
                    } else if (range16(valor)) {
                        cadena = "Extendido";
                    } else {
                        System.out.println("Error de longitud de bits");
                    }

                    break;
                case 4:// Decimal
                    valor = Integer.parseInt(cadena);
                    if (range8(valor)) {
                        cadena = "Directo";
                    } else if (range16(valor)) {
                        cadena = "Extendido";
                    } else {
                        System.out.println("Error de longitud de bits");
                    }
                    break;
                default:
                    break;
            }
        } else if (encontrado10) {// EXTENDIDO con etiqueta
            if (val.valEtiqueta(cadena) != null) {
                cadena = "Extendido";
            } else {
                System.out.println("Error operando con etiqueta");
            }

        }
        return cadena;
    }

    /**
     * metodo para verificar el rango de 5bits
     * 
     * @param oper
     * @return
     */
    public boolean range5(int oper) {
        if (oper <= 15 && oper >= -16) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * metodo para verificar el rango de 9bits
     * 
     * @param oper
     * @return
     */
    public boolean range9(String oper) {
        if (Integer.parseInt(oper) <= 255 && Integer.parseInt(oper) >= 16
                || Integer.parseInt(oper) <= -17 && Integer.parseInt(oper) >= -256) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * metodo para verificar el rango de 16bits
     * 
     * @param oper
     * @return
     */
    public boolean range16(int oper) {
        if (oper <= 65535 && oper >= 256) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * metodo para verificar el rango del prepost
     * 
     * @param oper
     * @return
     */
    public boolean rangeprepost(int oper) {
        if (oper <= 8 && oper >= 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * metodo para verificar el rango de 8bits
     * 
     * @param oper
     * @return
     */
    public boolean range8(int oper) {
        if (oper <= 255 && oper >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * metodo para verificar el rango de 16 bits del indizado indirecto
     * 
     * @param oper
     * @return
     */
    public boolean rangeInd16(int oper) {
        if (oper <= 65535 && oper >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verificar que sea un hexadecimal
     * 
     * @param cadena valor de la cadena
     * @return retorno de valor para entrar a switch
     */
    public int basesnum(String cadena) {
        // Creo los evaluadores
        Pattern hexadecimal;
        Matcher buscador;

        // Expresion Hexadecimal
        hexadecimal = Pattern.compile("^[\\$][0-9A-F]+$");
        buscador = hexadecimal.matcher(cadena);
        boolean encontrado = buscador.find(); // verdadero si la expresion se cumple

        // Creo los evaluadores
        Pattern octal;
        Matcher buscador1;

        // Expresion octal
        octal = Pattern.compile("^[@][0-7]+$");
        buscador1 = octal.matcher(cadena);
        boolean encontrado1 = buscador1.find(); // verdadero si la expresion se cumple

        // Creo los evaluadores
        Pattern binario;
        Matcher buscador2;

        // Expresion para Binario
        binario = Pattern.compile("^[%][0-1]+$");
        buscador2 = binario.matcher(cadena);
        boolean encontrado2 = buscador2.find(); // verdadero si la expresion se cumple

        Pattern decimal;
        Matcher buscador3;

        // Expresion decimal
        decimal = Pattern.compile("^[0-9]+$");
        buscador3 = decimal.matcher(cadena);
        boolean encontrado3 = buscador3.find(); // verdadero si la expresion se cumple

        // Valores inmediatos
        Pattern hexaimm;
        Matcher buscador4;

        // Expresion Hexadecimal
        hexaimm = Pattern.compile("^[#][\\$][0-9A-F]+$");
        buscador4 = hexaimm.matcher(cadena);
        boolean encontrado4 = buscador4.find(); // verdadero si la expresion se cumple

        // Creo los evaluadores
        Pattern octalimm;
        Matcher buscador5;

        // Expresion octal
        octalimm = Pattern.compile("^[#][@][0-7]+$");
        buscador5 = octalimm.matcher(cadena);
        boolean encontrado5 = buscador5.find(); // verdadero si la expresion se cumple

        // Creo los evaluadores
        Pattern binimm;
        Matcher buscador6;

        // Expresion para Binario
        binimm = Pattern.compile("^[#][%][0-1]+$");
        buscador6 = binimm.matcher(cadena);
        boolean encontrado6 = buscador6.find(); // verdadero si la expresion se cumple

        Pattern decimm;
        Matcher buscador7;

        // Expresion decimal
        decimm = Pattern.compile("^[#][0-9]+$");
        buscador7 = decimm.matcher(cadena);
        boolean encontrado7 = buscador7.find(); // verdadero si la expresion se cumple

        // Evaluo para determinar si es un hexadecimal o no
        // Retorno el valor resultante
        if (encontrado || encontrado4) {// Hexadecimal
            return 1;
        } else if (encontrado1 || encontrado5) {// Octal
            return 2;
        } else if (encontrado2 || encontrado6) {// Binario
            return 3;
        } else if (encontrado3 || encontrado7) {// Decimal
            return 4;
        } else {// No entra
            return 0;
        }
    }
}
