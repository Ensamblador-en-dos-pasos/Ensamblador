import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Isaac Ulises
 * @author Saul
 * @version 1.41
 */
public class Leer {
    static Nodo head = null;

    public final String nom1 = "P1ASMtmp.txt";
    public int cont = 1;
    public String codop = null, oper = null, et = null;
    public static Validador val = new Validador();
    public static LcTabop bus = new LcTabop();

    public void setCodop(String codop) {
        this.codop = codop;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public String getCodop() {
        return codop;
    }

    public String getOper() {
        return oper;
    }

    /**
     * Método para leer el archivo
     * 
     * @param nom nombre del archivo
     * @throws FileNotFoundException
     */
    public void leerArc(String nom) throws FileNotFoundException, IOException {
        PrintWriter out = null;

        try {// abre el fichero

            // Creo un nuevo arcivo "Temporal" de escritura
            out = new PrintWriter(new FileWriter(nom1));

            // Genero el bufferedReader para la lectura de las lineas
            BufferedReader ej = new BufferedReader(new FileReader(nom));
            String linea;
            // Lee el archivo origial y reemplaza todos los espacios en blanco
            // por comas en el archivo temporal
            while ((linea = ej.readLine()) != null) {
                out.write(reemplBlkPlcs(linea.toUpperCase()));
                out.println();
            }
        } finally {
            if (out != null) {
                out.close();
            } // Fin del if
        } // Fin de try

    }// Fin de método

    /**
     * Método para leer el archivo caracter por caracter
     * 
     * @param nom nombre del archivo
     * @throws FileNotFoundException
     */
    public void Lcc() throws FileNotFoundException, IOException {
        FileReader in = null;
        File archivo = new File(nom1);
        boolean ban = true;
        BufferedReader ej = new BufferedReader(new FileReader(nom1));
        try {// abre el fichero
            String linea;
            while ((linea = ej.readLine()) != null && ban) {
                EvdCasos(linea);
                ++cont;
                if (linea.contains("&END")) {
                    linea = null;
                    ban = false;
                    archivo.delete();
                }
            }
            if (archivo.exists()) {
                archivo.delete();// Elimina el archivo
            }
            
        } finally {
            if (in != null) {
                in.close();
                ej.close();

            } // Fin del if
        } // Fin de try
    }

    /**
     * En este metodo se busca quitar las comas y evaluar
     * para implementar los valores en un arreglo que va
     * ayudar a determinar la posicion evaluada
     * 
     * @param cadena cadena de caracteres sacada del archivo temporal
     */
    public void EvdCasos(String cadena) throws FileNotFoundException, IOException {
        boolean ver = true;
        String[] arr = cadena.split("&");

        // Creo los evaluadores
        Pattern Icomplete;
        Matcher buscador;

        Pattern onlycodop;
        Matcher buscador1;

        Pattern cod_oper;
        Matcher buscador2;

        Pattern et_codop;
        Matcher buscador3;

        Pattern vacio;
        Matcher buscador4;

        // Desarrollo de la expresion regular
        Icomplete = Pattern.compile("^[\\w]+&[\\w]+&[$|@|%|,\\w]+&?$");
        buscador = Icomplete.matcher(cadena);
        boolean encontrado = buscador.find();// verdadero si la expresion se cumple

        onlycodop = Pattern.compile("^&[\\w]+&?$");
        buscador1 = onlycodop.matcher(cadena);
        boolean encontrado1 = buscador1.find();

        cod_oper = Pattern.compile("^&[\\w]+&[$|@|%|,\\w]+&?$");
        buscador2 = cod_oper.matcher(cadena);
        boolean encontrado2 = buscador2.find();

        et_codop = Pattern.compile("^[\\w]+&[\\w]+&?$");
        buscador3 = et_codop.matcher(cadena);
        boolean encontrado3 = buscador3.find();

        vacio = Pattern.compile("^&$");
        buscador4 = vacio.matcher(cadena);
        boolean encontrado4 = buscador4.find();
        int caso = 0;

        // Evaluadores de los casos para el switch
        if (cadena.startsWith(";") || cadena.startsWith("&;")) {// solo comentarios
            caso = 1; 
        } else if (encontrado) {
            et = arr[0];
            codop = arr[1];
            oper = arr[2];
            caso = 2;
        } else if (encontrado1) {
            et = null;
            codop = arr[1];
            oper = null;
            caso = 2;
        } else if (encontrado2) {
            et = null;
            codop = arr[1];
            oper = arr[2];
            caso = 2;
        } else if (encontrado3) {
            et = arr[0];
            codop = arr[1];
            oper = null;
            caso = 2;
        }
        else{// Error, no hay codop
            caso = 3;
        } 

        switch (caso) {
            case 1:// Caso del comentario
                if (val.valComentario(cadena) == true) {
                    System.out.println("COMENTARIO");
                    System.out.println();
                } else {
                    System.out.println("ERROR en el comentario en la línea: " + cont);
                    System.out.println();
                }
                break;
            
            case 2:
                if(et != null){//Validar etiqueta
                    if(val.valEtiqueta(et) == null){
                        System.out.println("Error en la etiqueta en la línea: "+cont);
                        ver = false;
                    }
                }
                if(val.valCodop(codop) == null){//Validar codop
                    System.out.println("Error en el código de operación de la línea: "+cont);
                    ver = false;
                }
                
                if(ver){//Guardar los valores en la lista
                    add(et, codop, oper);
                }
                break;
            case 3://En caso de hacer una línea vacía o que la línea no tenga un operando valido
                if(!encontrado4){
                    System.out.println("Error en la instrucción de la línea: " + cont + " No existe un CODOP\n");
                }
                
                break;
            default:
                break;
        }

    }// Fin de metodo

    /**
     * Metodo para eliminar los espacios en blanco y ponerles una coma
     * 
     * @param frase linea de texto a evaluarse
     */
    public static String reemplBlkPlcs(String frase) {
        String result = frase.replaceAll("\\s+", "&");
        // System.out.println(result); esto se hacia para comprobar el resultado
        return result;

    }

    /**
     * Método de impresión de las validaciones
     * Valida errores en etiqueta y codop, proximammente en OP
     * 
     * @param arreglo entrada del arreglo a evaluar
     * @throws FileNotFoundException
     */
    public void impresionMetods(Nodo head) throws FileNotFoundException {
        bus.BuscarCodop("Tabop.txt",head.getEtiqueta(), head.getCodop(), head.getOperando());
    }

    /**
     * Método para guardar los valores en una lista
     * @param et
     * @param cp
     * @param op
     */
    static void add(String et, String cp, String op){
        Nodo nuevo = new Nodo(et, cp, op);

        if(clear() == true){
            head = nuevo;
        }
        else{
            Nodo anterior = head;
            Nodo actual = anterior;
            
            while(actual != null){
                anterior = actual;
                actual = actual.getNext();
            }
            anterior.setNext(nuevo);
        }
    }

    //*Método para verificar cuando la lista está vacía */
    private static boolean clear(){
        return head==null;
    }

    /**
     * Método para buscar los valores de la lista en el tabop
     * @throws FileNotFoundException
     */
    public void buscar() throws FileNotFoundException{
        Nodo aux = head;
            if(!clear()){
                while(aux!=null){
                    impresionMetods(aux);
                    aux = aux.getNext();
                }
            }else{
            System.out.println("No hay instrucciones");
            }
    }

}