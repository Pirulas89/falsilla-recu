package proyecto.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "ordenador")
public class Ordenador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int tamRam;
    private int tamDisco;
    private int numUsb;
    private double precio;
    private int unidades;

    public Ordenador() {}
    public Ordenador(int tamRam, int tamDisco, int numUsb, double precio, int unidades) {
        this.tamRam = tamRam; this.tamDisco = tamDisco; this.numUsb = numUsb;
        this.precio = precio; this.unidades = unidades;
    }

    public int    getId()             { return id; }
    public int    getTamRam()         { return tamRam; }
    public void   setTamRam(int r)    { this.tamRam = r; }
    public int    getTamDisco()       { return tamDisco; }
    public void   setTamDisco(int d)  { this.tamDisco = d; }
    public int    getNumUsb()         { return numUsb; }
    public void   setNumUsb(int u)    { this.numUsb = u; }
    public double getPrecio()         { return precio; }
    public void   setPrecio(double p) { this.precio = p; }
    public int    getUnidades()       { return unidades; }
    public void   setUnidades(int u)  { this.unidades = u; }
}
