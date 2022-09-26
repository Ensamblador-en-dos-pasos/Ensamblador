import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author Isaac Ulises
 * @author Saul
 * @version 1.45
 */
public class Operando {
    Conversor conv = new Conversor();
    Validador val = new Validador();
    int valor;

    /**
     * Método para validar el operando
     * 
     * @param cadena valor del operando
     * @param oper   0 no operando 1 si operando
     * @return Retorna el ADDR
     */
    public String valOperando(String cadena, Integer oper, Integer addr2) {
        if (cadena == null) {
            cadena = "";
        }
        boolean ban = false;
        // Creo los evaluadores para los indizados
        Pattern ind5bits; // Indizado 5 bits
        Matcher buscador1;

        Pattern ind9_16bits; // Indizado 9 bits y 16 bits
        Matcher buscador2;

        Pattern prepost; // Pre-post Incremento-Decremento
        Matcher buscador3;

        Pattern acum;// Acumulador
        Matcher buscador4;

        Pattern acumindr;// Acumuldor indirecto
        Matcher buscador5;

        Pattern indr16bits;// Indirecto 16 bits
        Matcher buscador6;

        Pattern imm;// Inmediato
        Matcher buscador7;

        Pattern dr;// Directo
        Matcher buscador8;

        Pattern ext;// Extendid
        Matcher buscador9;

        // Desarrollo de la expresion regular
        // Indizados
        ind5bits = Pattern.compile("^[-]*[0-9]+,[X|Y]$|^[-]*[0-9]+,[S]P$|^[-]*[0-9]+,[P]C$|^,[X|Y]$|^,[S]P$|,[P]C$");
        buscador1 = ind5bits.matcher(cadena);
        boolean encontrado = buscador1.find();

        ind9_16bits = Pattern.compile("^[-]*[0-9]+,[X|Y]$|^[-]*[0-9]+,[S]P$|^[-]*[0-9]+,[P]C$");
        buscador2 = ind9_16bits.matcher(cadena);
        boolean encontrado2 = buscador2.find();

        prepost = Pattern.compile(
                "^[-]*[1-8]+,[+|-][X|Y]$|^[-]*[1-8]+,[X|Y][+|-]$|^[-]*[1-8]+,[+|-][S]P$|^[-]*[1-8]+,[S]P[+|-]$|^[-]*[1-8]+,[+|-][P]C$|^[-]*[1-8]+,[P]C[+|-]$");
        buscador3 = prepost.matcher(cadena);
        boolean encontrado3 = buscador3.find();

        acum = Pattern.compile("^[A|B|D]+,[X|Y]$|^[A|B|D]+,[S]P$|^[A|B|D]+,[P]C$");
        buscador4 = acum.matcher(cadena);
        boolean encontrado4 = buscador4.find();

        acumindr = Pattern
                .compile("^[\\[][D]+,[X|Y][\\]]$|^[\\[][D]+,[X|Y][\\]]$|^[\\[][D]+,[S]P[\\]]$|^[\\[][D]+,[P]C[\\]]$");
        buscador5 = acumindr.matcher(cadena);
        boolean encontrado5 = buscador5.find();

        indr16bits = Pattern.compile(
                "^[\\[][-]*[0-9]*,[X|Y][\\]]$|^[\\[][-]*[0-9]*,[X|Y][\\]]$|^[\\[][-]*[0-9]*,[S]P[\\]]$|^[\\[][-]*[0-9]*,[P]C[\\]]$");
        buscador6 = indr16bits.matcher(cadena);
        boolean encontrado6 = buscador6.find();

        // Inmediato
        imm = Pattern.compile("^[#][0-9]+$|^[#][\\$][0-9A-F]+$|^[#][@][0-7]+$|^[#][%][0-1]+$");
        buscador7 = imm.matcher(cadena);
        boolean encontrado7 = buscador7.find();

        // Directo-Extendido numérico
        dr = Pattern.compile("^[0-9]+$|^[\\$][0-9A-F]+$|^[@][0-7]+$|^[%][0-1]+$");
        buscador8 = dr.matcher(cadena);
        boolean encontrado8 = buscador8.find();

        // Extendido con etiqueta - Rel
        ext = Pattern.compile("^[a-zA-Z]+\\w*+$");
        buscador9 = ext.matcher(cadena);
        boolean encontrado9 = buscador9.find();

        if (encontrado || encontrado2) {// IDX 5 bits - IDX1 9 bits - IDX2 16
            int valor;
            String[] result = cadena.split(",");
            if (result[0] == "") {
                valor = 0;
            } else {
                valor = Integer.parseInt(result[0]);
            }

            if (range5(valor)) {
                cadena = "IDX";
                ban = true;
            } else if (range9(valor)) {
                cadena = "IDX1";
                ban = true;
            } else if (range16(valor)) {
                cadena = "IDX2";
                ban = true;
            } else {
                System.out.println("Error, el tamaño de bits no corresponde a una instrucción");
            }

        } else if (encontrado3) {// IDX Pre-post
            int valor;
            String[] result = cadena.split(",");
            valor = Integer.parseInt(result[0]);

            if (rangeprepost(valor)) {
                cadena = "IDX";
                ban = true;
            } else {
                System.out.println("Error, este codop debe tener un valor decimal de 1 - 8");
            }
        } else if (encontrado4) {// IDX acumulador
            cadena = "IDX";
            ban = true;
        } else if (encontrado5) {// IDX acumulador indirecto
            cadena = "[D,IDX]";
            ban = true;
        } else if (encontrado6) {// IDX 16bits indirecto
            String result = cadena.replaceAll("\\[", ",");
            int valor;
            String[] result1 = result.split(",");
            if (result1[1] == "") {
                valor = 0;
            } else {
                valor = Integer.parseInt(result1[1]);
            }
            if (rangeInd16(valor)) {
                cadena = "[IDX2]";
                ban = true;
            } else {
                System.out.println("Error, este operando debe estar entre 0 - 65535");
            }

        } else if (encontrado7) {// Inmediato
            switch (basesnum(cadena)) {
                case 1:// Hexadecimal
                    valor = conv.hextodecimm(cadena);
                        if (range8(valor)) {
                            cadena = "Inmediato";
                            ban = true;
                        } else if (range16(valor)) {
                            cadena = "Inmediato1";
                            ban = true;
                        }else{
                            System.out.println("Error, este codop debe ser de 8 o 16 bits");
                        }
                    break;
                case 2:// Octal
                    valor = conv.octtodecimm(cadena);
                        if (range8(valor)) {
                            cadena = "Inmediato";
                            ban = true;
                        } else if (range16(valor)) {
                            cadena = "Inmediato1";
                            ban = true;
                        } else {
                            System.out.println("Error, este codop debe ser de 8 o 16 bits");
                        }
                    break;
                case 3:// Binario
                    valor = conv.bintodecimm(cadena);
                        if (range8(valor)) {
                            cadena = "Inmediato";
                            ban = true;
                        } else if (range16(valor)) {
                            cadena = "Inmediato1";
                            ban = true;
                        } else {
                            System.out.println("Error, este codop debe ser de 8 o 16 bits");
                        }
                    break;
                case 4:// Decimal
                    int valor1 = conv.decimm(cadena);
                    valor = (valor1);
                        if (range8(valor)) {
                            cadena = "Inmediato";
                            ban = true;
                        }else if (range16(valor)) {
                            cadena = "Inmediato1";
                            ban = true;
                        } else {
                            System.out.println("Error, este codop debe tener ser de 8 o 16 bits");
                        }
                    break;
                default:
                    break;
            }
        } else if (encontrado8) {// DIRECTO-Extendido
            switch (basesnum(cadena)) {
                case 1:// Hexadecimal
                    valor = conv.hextodec(cadena);
                    if (range8(valor)) {
                        cadena = "Directo";
                        ban = true;
                    } else if (range16(valor)) {
                        cadena = "Extendido";
                        ban = true;
                    } else {
                        System.out.println("Error de longitud de 8 o 16 bits");
                    }
                    break;
                case 2:// Octal
                    valor = conv.octtodec(cadena);
                    if (range8(valor)) {
                        cadena = "Directo";
                        ban = true;
                    } else if (range16(valor)) {
                        cadena = "Extendido";
                        ban = true;
                    } else {
                        System.out.println("Error de longitud de 8 o 16 bits");
                    }
                    break;
                case 3:// Binario
                    valor = conv.bintodec(cadena);
                    if (range8(valor)) {
                        cadena = "Directo";
                        ban = true;
                    } else if (range16(valor)) {
                        cadena = "Extendido";
                        ban = true;
                    } else {
                        System.out.println("Error de longitud de 8 o 16 bits");
                    }
                    break;
                case 4:// Decimal
                    valor = Integer.parseInt(cadena);
                    if (range8(valor)) {
                        cadena = "Directo";
                        ban = true;
                    } else if (range16(valor)) {
                        cadena = "Extendido";
                        ban = true;
                    } else {
                        System.out.println("Error de longitud de 8 o 16 bits");
                    }
                    break;
                default:
                    break;
            }
        } else if (encontrado9) {// EXTENDIDO con etiqueta - Relativo
            if (val.valEtiqueta(cadena) != null) {
                switch (addr2) {
                    case 0:
                        cadena = "Extendido";
                        ban = true;
                        break;
                    case 1:
                        cadena = "REL";
                        ban = true;
                        break;

                    default:
                        break;
                }

            } else {
                System.out.println("Error operando con etiqueta");
            }

        } else if (cadena == "" && oper == 0) {// Inherente
            cadena = "Inherente";
            ban = true;
        }

        if (!ban) {
            System.out.println("Error en el operando");
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
    public boolean range9(int oper) {
        if (oper <= 255 && oper >= 16 || oper <= -17 && oper >= -256) {
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
