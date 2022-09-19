
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Isaac Ulises
 * @author Saul
 * @version 1.41
 */
public class Ensamblador {

        public static void main(String[] args) throws FileNotFoundException, IOException {
                Conversor con = new Conversor();
                Validador val = new Validador();
                Operando oper = new Operando();
                // Leer read = new Leer();
                // read.leerArc("P2ASM.txt");
                // read.Lcc();
                // read.buscar();

                System.out.println(oper.valOperando(",X", 0));
        }
}
