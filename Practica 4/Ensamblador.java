
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Isaac Ulises
 * @author Saul
 * @version 1.45
 */
public class Ensamblador {
        public static void main(String[] args) throws FileNotFoundException, IOException {
                
                //Leer read = new Leer();
                //read.leerArc("P2ASM.txt");
                //read.Lcc();
                //read.buscar();
                Operando oper = new Operando();
                Conversor con = new Conversor();
                String val = oper.Val_directivas("@77");
                int valor = (con.hextodec(val)*2)+ 11;
                val = con.dectohex(valor);
                System.out.println(val);

        }
        
}
