package proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "edificio")
public class Edificio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int numViviendas;
    private int anioEdificacion;
    private boolean rehabilitado;

    public Edificio() {}
    public Edificio(int numViviendas, int anioEdificacion, boolean rehabilitado) {
        this.numViviendas = numViviendas;
        this.anioEdificacion = anioEdificacion;
        this.rehabilitado = rehabilitado;
    }

    public int     getId()                    { return id; }
    public int     getNumViviendas()          { return numViviendas; }
    public void    setNumViviendas(int n)     { this.numViviendas = n; }
    public int     getAnioEdificacion()       { return anioEdificacion; }
    public void    setAnioEdificacion(int a)  { this.anioEdificacion = a; }
    public boolean isRehabilitado()           { return rehabilitado; }
    public void    setRehabilitado(boolean r) { this.rehabilitado = r; }
}
