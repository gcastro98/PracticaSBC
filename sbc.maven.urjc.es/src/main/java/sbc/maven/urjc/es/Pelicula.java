package sbc.maven.urjc.es;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pelicula {
    private String titulo;
    private List<Actor> reparto;
    private double calificacion;
    private String pais;
    private String genero;
    private String productora;

    public Pelicula(String titulo, List<Actor> reparto, String cal, String pais, String genero, String productora) {
        this.titulo = titulo;
        this.reparto = reparto;
        this.calificacion = !cal.equals("N/A") ? Double.parseDouble(cal) : 0.0;
        this.pais = pais;
        this.genero = genero;
        this.productora = productora;
    }
    public Pelicula(String titulo, String cal, String pais, String genero, String productora) {
        this.titulo = titulo;
        this.reparto = new ArrayList<Actor>();
        this.calificacion = !cal.equals("N/A") ? Double.parseDouble(cal) : 0.0;
        this.pais = pais;
        this.genero = genero;
        this.productora = productora;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Actor> getReparto() {
        return reparto;
    }

    public void setReparto(List<Actor> reparto) {
        this.reparto = reparto;
    }

    public double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getProductora() {
        return productora;
    }

    public void setProductora(String productora) {
        this.productora = productora;
    }
    public void addActor(Actor actor){
        if(!reparto.contains(actor)) this.reparto.add(actor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pelicula pelicula = (Pelicula) o;
        return Objects.equals(titulo, pelicula.titulo);
    }


}
