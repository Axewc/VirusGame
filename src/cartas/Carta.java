package cartas;




public abstract class Carta {
    
    /**Nombre de la carta. */
    private String nombre;

    /**Ruta de la imagen. */
    String imagen;

    /**Color de la carta. */
    String color;

    /** Descripción detallada sobre el funcionamiento de la carta. */
    String descripcion;

    /**
     * 
     */
    abstract void jugar();

    /**
     * 
     */
    abstract void descartar();
a
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    
}
