package cartas;

/**
 * Carta, clase abstracta de la que exenderan los tipos de carta. 
 */
public abstract class Organo extends Carta{

    Carta[] atachadas = new Carta[2];
    boolean inmunizado;

    @Override
    void descartar() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    void jugar() {
        // TODO Auto-generated method stub
        
    }
    
}