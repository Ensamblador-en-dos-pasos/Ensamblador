public class Nodo {
    private String etiqueta, codop, operando;
    private Nodo next;//Puntero del siguiente nodo

    public Nodo(String et, String codop, String oper, Nodo next){
        this.etiqueta = et;
        this.codop = codop;
        this.operando = oper;
        this.next =next;
    }

    public Nodo(String et, String codop, String oper){
        this.etiqueta = et;
        this.codop = codop;
        this.operando = oper;
        this.next = null;
    }

    public String getEtiqueta(){
        return etiqueta;
    }

    public String getCodop(){
        return codop;
    }
    
    public String getOperando(){
        return operando;
    }

    public void setEtiqueta(String et){
        this.etiqueta = et;
    } 
    
    public void setCodop(String cdp){
        this.codop = cdp;
    }

    public void setOperando(String op){
        this.operando = op;
    }

    public Nodo getNext(){
        return next;
    }

    public void setNext(Nodo next){
        this.next = next;
    }
}
