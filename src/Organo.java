
/**
 * Carta, clase abstracta de la que exenderan los tipos de carta. 
 */
public abstract class Organo {

    /** Nombre de la carta. */
    String nombre;
    
    /** Organo, Virus, Medicina, Tratamiento. */
    String tipo;
    
    /** Descripción detallada sobre el funcionamiento de la carta. */
    String descripcion;

    public String getNombre() {
        return nombre;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void jugarCarta() {
        return;
    }



    
}