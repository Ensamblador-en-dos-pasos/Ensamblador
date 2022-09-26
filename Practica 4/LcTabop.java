import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @author Isaac Ulises
 * @author Saul
 * @version 1.45
 */
public class LcTabop {
    Operando valOpe = new Operando();
    Conversor conv = new Conversor();
    int Contloc, ContlocEqu = 0;
    int dec = 0;
    String dec1 = "";

    /**
     * Metodo para buscar el codigo de operacion que se encuentra en el Tabop
     * 
     * @param nomtabop nombre del archivo
     * @param codop    valor del codigo de operacion
     * @param oper     valor del operando
     * @throws IOException
     */
    public void BuscarCodop(String nomtabop, String et, String codop, String oper) throws IOException {
        if (et == "") {
            et = null;
        }
        Scanner sc = null;

        File tmp = new File("TMP.txt");
        File tab = new File("TABSIM.txt");

        PrintWriter TABSIM = null;
        PrintWriter TMP = null;
        boolean ban = false;// Bandera para salir del while
        int ope, bpc = 0, tb, addr2 = 0, bytes = 0;// Es 1 0 que indica si lleva o no operador
        String addr, addr1, cmc;

        try {
            sc = new Scanner(new FileReader(nomtabop));
            TABSIM = new PrintWriter(new FileWriter(tab, true));
            TMP = new PrintWriter(new FileWriter(tmp, true));

            while (sc.hasNextLine()) {// Leer hasta que temine

                String palabra = sc.next();

                if (palabra.equals(codop)) {
                    ope = sc.nextInt();

                    switch (ope) {
                        case 1:
                        case 0:// Caso de las directivas del tabop
                            addr = sc.next();
                            cmc = sc.next();
                            bpc = sc.nextInt();
                            tb = sc.nextInt();

                            if (addr.equals("Extendido")) {
                                addr2 = 0;
                            } else if (addr.equals("REL")) {
                                addr2 = 1;
                            }

                            addr1 = valOpe.valOperando(oper, ope, addr2);
                            if (addr1.equals(addr)) {

                                if (EOpe(ope, oper)) {
                                    TMP.printf("%s\t %s\t %s\t %s\t %s", "ContLoc", Contloc, et, codop, oper);
                                    TMP.println();
                                    if (et != null) {
                                        TABSIM.printf("%s\t %s\t %s\t", "ContLoc(Etiqueta relativa)", et, Contloc);
                                        TABSIM.println();
                                    }
                                    Contloc = Contloc + tb;
                                    ban = true;
                                }
                            }
                            break;// Break del caso 1 o 0

                        case 2:// Constantes
                            addr = sc.next();
                            bytes = sc.nextInt();
                            switch (bytes) {
                                case 1:
                                    TMP.printf("%s\t %s\t %s\t %s\t %s", "ContLoc", Contloc, et, codop, oper);
                                    TMP.println();
                                    if (et != null) {
                                        TABSIM.printf("%s\t %s\t %s\t", "ContLoc(Etiqueta relativa)", et, Contloc);
                                        TABSIM.println();
                                    }
                                    Contloc = Contloc + 1;
                                    ban = true;
                                    sc.nextInt();
                                    sc.nextInt();
                                    break;
                                case 2:
                                    TMP.printf("%s\t %s\t %s\t %s\t %s", "ContLoc", Contloc, et, codop, oper);
                                    TMP.println();
                                    if (et != null) {
                                        TABSIM.printf("%s\t %s\t %s\t", "ContLoc(Etiqueta relativa)", et, Contloc);
                                        TABSIM.println();
                                    }
                                    Contloc = Contloc + 2;
                                    ban = true;
                                    sc.nextInt();
                                    sc.nextInt();

                                    break;

                                default:
                                    break;
                            }

                            break;
                        case 3:// Directivas
                            addr = sc.next();
                            bytes = sc.nextInt();

                            if (addr.equals("Dire_Inic")) {
                                switch (valOpe.basesnum(oper)) {
                                    case 1:
                                        dec = conv.hextodec(oper);
                                        if (valOpe.rangeInd16(dec)) {
                                            dec1 = conv.dectohex(dec);
                                            TMP.printf("%s\t %s\t %s\t %s\t %s", "Dir_Inic", dec1, et, codop, oper);
                                            TMP.println();
                                            ban = true;
                                        }

                                        break;
                                    case 2:
                                        dec = conv.octtodec(oper);
                                        if (valOpe.rangeInd16(dec)) {
                                            dec1 = conv.dectohex(dec);
                                            TMP.printf("%s\t %s\t %s\t %s\t %s", "Dir_Inic", dec1, et, codop, oper);
                                            TMP.println();
                                            ban = true;
                                        }

                                        break;
                                    case 3:
                                        dec = conv.bintodec(oper);
                                        if (valOpe.rangeInd16(dec)) {
                                            dec1 = conv.dectohex(dec);
                                            TMP.printf("%s\t %s\t %s\t %s\t %s", "Dir_Inic", dec1, et, codop, oper);
                                            TMP.println();
                                            ban = true;
                                        }

                                        break;
                                    case 4:
                                        dec = Integer.parseInt(oper);
                                        if (valOpe.rangeInd16(dec)) {
                                            dec1 = conv.dectohex(dec);
                                            TMP.printf("%s\t %s\t %s\t %s\t %s", "Dir_Inic", dec1, et, codop, oper);
                                            TMP.println();
                                            ban = true;
                                        }
                                        break;

                                    default:
                                        break;
                                }
                            } else {
                                TMP.printf("%s\t %s\t %s\t %s\t %s", "ContLoc", Contloc, et, codop, oper);
                                TMP.println();
                                if (et != null) {
                                    TABSIM.printf("%s\t %s\t %s\t", "ContLoc(Etiqueta relativa)", et, Contloc);
                                    TABSIM.println();
                                }
                                ban = true;
                            }
                            break;
                        case 4:// Equate
                            addr = sc.next();
                            bytes = sc.nextInt();
                            // Convertir hexa
                            ContlocEqu = Integer.parseInt(oper);
                            dec1 = conv.dectohex(ContlocEqu);
                            TMP.printf("%s\t %s\t %s\t %s\t %s", "Valor EQU", dec1, et, codop, oper);
                            TMP.println();
                            TABSIM.printf("%s\t %s\t %s\t", "EQU(Etiqueta absoluta)", et, dec1);
                            TABSIM.println();

                            ban = true;

                            break;
                        case 5:// FCC
                            addr = sc.next();
                            bytes = sc.nextInt();
                            TMP.printf("%s\t %s\t %s\t %s\t %s", "ContLoc", Contloc, et, codop, oper);
                            TMP.println();
                            if (et != null) {
                                TABSIM.printf("%s\t %s\t %s\t", "ContLoc(Etiqueta relativa)", et, Contloc);
                                TABSIM.println();
                            }
                            Contloc = Contloc + (oper.length() - 2);
                            ban = true;

                            break;
                        case 6:// Memoria
                            addr = sc.next();
                            bytes = sc.nextInt();

                            switch (valOpe.basesnum(oper)) {
                                case 1:
                                    dec = conv.hextodec(oper);
                                    if (valOpe.rangeInd16(dec)) {
                                        dec1 = conv.dectohex(dec);
                                        TMP.printf("%s\t %s\t %s\t %s\t %s", "ContLoc", dec1, et, codop, oper);
                                        TMP.println();
                                        if (et != null) {
                                            TABSIM.printf("%s\t %s\t %s\t", "ContLoc(Etiqueta relativa)", et, dec1);
                                            TABSIM.println();
                                        }
                                        ban = true;
                                    }

                                    break;
                                case 2:
                                    dec = conv.octtodec(oper);
                                    if (valOpe.rangeInd16(dec)) {
                                        dec1 = conv.dectohex(dec);
                                        TMP.printf("%s\t %s\t %s\t %s\t %s", "ContLoc", dec1, et, codop, oper);
                                        TMP.println();
                                        if (et != null) {
                                            TABSIM.printf("%s\t %s\t %s\t", "ContLoc(Etiqueta relativa)", et, dec1);
                                            TABSIM.println();
                                        }
                                        ban = true;
                                    }

                                    break;
                                case 3:
                                    dec = conv.bintodec(oper);
                                    if (valOpe.rangeInd16(dec)) {
                                        dec1 = conv.dectohex(dec);
                                        TMP.printf("%s\t %s\t %s\t %s\t %s", "ContLoc", dec1, et, codop, oper);
                                        TMP.println();
                                        if (et != null) {
                                            TABSIM.printf("%s\t %s\t %s\t", "ContLoc(Etiqueta relativa)", et, dec1);
                                            TABSIM.println();
                                        }
                                        ban = true;
                                    }

                                    break;
                                case 4:
                                    dec = Integer.parseInt(oper);
                                    if (valOpe.rangeInd16(dec)) {
                                        dec1 = conv.dectohex(dec);
                                        TMP.printf("%s\t %s\t %s\t %s\t %s", "ContLoc", dec1, et, codop, oper);
                                        TMP.println();
                                        if (et != null) {
                                            TABSIM.printf("%s\t %s\t %s\t", "ContLoc(Etiqueta relativa)", et, dec1);
                                            TABSIM.println();
                                        }
                                        ban = true;
                                    }
                                    break;

                                default:
                                    break;
                            }
                            // Conversor de bases

                            // Multiplicarlo por 1 o 2 dependiendo los bytes
                            switch (bytes) {
                                case 1:
                                    TMP.printf("%s\t %s\t %s\t %s\t %s", "ContLoc", Contloc, et, codop, oper);
                                    TMP.println();
                                    if (et != null) {
                                        TABSIM.printf("%s\t %s\t %s\t", "ContLoc(Etiqueta relativa)", et, Contloc);
                                        TABSIM.println();
                                    }
                                    Contloc = Contloc + (Integer.parseInt(oper) * 1);
                                    ban = true;

                                    break;
                                case 2:
                                    TMP.printf("%s\t %s\t %s\t %s\t %s", "ContLoc", Contloc, et, codop, oper);
                                    TMP.println();
                                    if (et != null) {
                                        TABSIM.printf("%s\t %s\t %s\t", "ContLoc(Etiqueta relativa)", et, Contloc);
                                        TABSIM.println();
                                    }
                                    Contloc = Contloc + (Integer.parseInt(oper) * 2);
                                    ban = true;
                                    break;

                                default:
                                    break;
                            }
                            break;
                        default:
                            break;
                    }

                }
            }
            if (!ban) {
                System.out.println(" Error, CODOP no encontrado " + "[" + codop + "]");
            }
        } finally {
            if (sc != null) {
                sc.close();
                TMP.close();
                TABSIM.close();

            }
        }
    }

    /**
     * Metodo que evalua los operandos si requieren o no operando
     * 
     * @param op       valor del operando , puede ser 0 o 1
     * @param operando valor del oprando que puede ser n numero o vacio
     */
    public boolean EOpe(int op, String operando) {
        boolean ret = false;
        switch (op) {
            case 1:
                if (operando == null) {
                    System.out.println(" Error se requiere operando");
                    ret = false;
                } else {
                    ret = true;
                }
                break;
            case 0:
                if (operando != null) {
                    System.out.println(" Error no se requiere operando");
                    ret = false;
                } else {
                    ret = true;
                }
                break;
            default:

                break;
        }
        return ret;
    }
}