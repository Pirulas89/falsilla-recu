package proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "cancion")
public class Cancion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String titulo;
    private int duracion;

    public Cancion() {}
    public Cancion(String titulo, int duracion) { this.titulo = titulo; this.duracion = duracion; }

    public int    getId()             { return id; }
    public String getTitulo()         { return titulo; }
    public void   setTitulo(String t) { this.titulo = t; }
    public int    getDuracion()       { return duracion; }
    public void   setDuracion(int d)  { this.duracion = d; }
}
