import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class CodMaquina {
    String valor, contloc, et, codop, oper, addr, cmc, cmf;
    int op = 0, base = 0;
    Operando valOpe = new Operando();
    Conversor conv = new Conversor();

    /**
     * Método para leer el archivo
     * 
     * @param nom nombre del archivo
     * @throws IOException
     */
    public void cMaquina() throws FileNotFoundException {
        Scanner sc = new Scanner(new FileReader("TMP.txt"));
        boolean ban = sc.hasNextLine();
        try {// abre el fichero
             // Lee el archivo origial y reemplaza todos los espacios en blanco
             // por comas en el archivo temporal
            while (ban) {
                valor = sc.next();
                contloc = sc.next();
                et = sc.next();
                codop = sc.next();
                oper = sc.next();
                addr = sc.next();
                cmc = sc.next();

                switch (addr) {
                    case "Inherente":
                    System.out.println("CODOP = " + codop + "\tCMC = " + cmc);
                        break;

                    case "Directo":
                    case "Extendido":
                        System.out.print("CODOP = " + codop + "\tCMC = " + cmc);
                        op = valOpe.basesnum(oper);
                        switch (op) {
                            case 1:// Hex
                                base = conv.hextodec(oper);
                                cmf = conv.dectohex(base);
                                System.out.println(" CMF: " + cmf);
                                break;
                            case 2:// Oct
                                base = conv.octtodec(oper);
                                cmf = conv.dectohex(base);
                                System.out.println(" CMF: " + cmf);
                                break;
                            case 3:// Bin
                                base = conv.bintodec(oper);
                                cmf = conv.dectohex(base);
                                System.out.println(" CMF: " + cmf);
                                break;
                            case 4:// dec
                                cmf = conv.dectohex(Integer.parseInt(oper));
                                System.out.println(" CMF: " + cmf);
                                break;

                            default:
                                break;
                        }
                        break;
                    case "Inmediato":
                    case "Inmediato1":
                        System.out.print("CODOP = " + codop + "\tCMC = " + cmc);
                        op = valOpe.basesnum(oper);
                        switch (op) {
                            case 1:// Hex
                                base = conv.hextodecimm(oper);
                                cmf = conv.dectohex(base);
                                System.out.println(" CMF: " + oper);
                                break;
                            case 2:// Oct
                                base = conv.octtodecimm(oper);
                                cmf = conv.dectohex(base);
                                System.out.println(" CMF: " + cmf);
                                break;
                            case 3:// Bin
                                base = conv.bintodecimm(oper);
                                cmf = conv.dectohex(base);
                                System.out.println(" CMF: " + cmf);
                                break;
                            case 4:// dec
                                base = conv.decimm(oper);
                                cmf = conv.dectohex(base);
                                System.out.println(" CMF: " + cmf);
                                break;

                            default:
                                break;
                        }
                        break;

                    default:
                        if (codop.equals("END")) {
                            ban = false;
                        }
                        break;
                }

            }
        } finally {
            if (sc != null) {
                sc.close();
            } // Fin del if
        } // Fin de try

    }// Fin de método
}
