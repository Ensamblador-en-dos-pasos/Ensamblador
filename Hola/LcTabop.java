import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * @author Isaac Ulises
 * @author Saul
 * @version 1.41
 */
public class LcTabop{
    Operando valOpe = new Operando();

    /**
     * Metodo para buscar el codigo de operacion que se encuentra en el Tabop
     * @param nomtabop nombre del archivo
     * @param codop valor del codigo de operacion
     * @param oper valor del operando
     * @throws FileNotFoundException
     */
    public void BuscarCodop(String nomtabop, String et, String codop, String oper)throws FileNotFoundException {
        Scanner sc = null;
        boolean ban = false;//Bandera para salir del while
        int ope, BPC=0, TB;//Es 1 0 que indica si lleva o no operador
        String ADDR, ADDR1, CMC;
        
        try{
            sc = new Scanner(new FileReader(nomtabop));
            while(sc.hasNextLine()){//Leer hasta que temine
                
                String palabra = sc.next();
                
                    if (palabra.equals(codop)) {
                        /*
                        System.out.println("CODOP = "+ codop);
                        ope = sc.nextInt();
                        System.out.print("Operando: " + ope);
                        EOpe(ope,oper);
                        System.out.print(" ADDR: " + sc.next() +
                        " CMC: "+sc.next()+ 
                        " Bitspc: "+sc.next()+
                        " TotalBytes: " +sc.next()+"\n\n");
                        ban = true;*/

                        ope = sc.nextInt();
                        ADDR = sc.next();
                        CMC = sc.next();
                        BPC = sc.nextInt();
                        TB = sc.nextInt();

                        ADDR1 = valOpe.valOperando(oper, BPC);

                        if(ADDR1.equals(ADDR)){
                            System.out.print("Etiqueta:" +et+" ");
                            System.out.print("Codop: "+ codop+" ");                        
                            System.out.println("Operando:" +oper+" ");
                            if(EOpe(ope,oper)){
                            System.out.print(ADDR1+" ");
                            System.out.print(TB +" Bytes"+"\n");

                            ban = true;
                        }
                        }

                    }                   
                }
            if(!ban){
                System.out.println(" Error, CODOP no encontrado");
            }
        }finally{
            if(sc != null){
                sc.close();
            }
        }
    }

    /**
     * Metodo que evalua los operandos si requieren o no operando
     * @param op valor del operando , puede ser 0 o 1
     * @param operando valodr del oprando que puede ser n numero o vacio
     */
    public boolean EOpe(int op, String operando) {
        boolean ret = false;
        switch (op) {
            case 1:
                if (operando == null) {
                    System.out.println(" Error se requiere operando");
                    ret = false;
                }else{
                    ret = true;
                }
                break;
            case 0:
                if (operando != null) {
                    System.out.println(" Error no se requiere operando");
                    ret = false;
                }
                else{
                    ret = true;
                }
                break;
            default:
                
                break;
        }
        return ret;
    }
}
