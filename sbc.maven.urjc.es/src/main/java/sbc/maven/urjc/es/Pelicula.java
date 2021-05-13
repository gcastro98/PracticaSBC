package sbc.maven.urjc.es;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Pelicula {
    private String titulo;
    private List<Actor> reparto;
    private double calificacion;
    private List<String> pais;
    private List<String> generos;
    private List<String> productoras;

    public Pelicula(String titulo, List<Actor> reparto, String cal, String pais, String generos, String productoras) {
        this(titulo, cal, pais, generos, productoras);
        this.reparto = reparto;
    }

    public Pelicula(String titulo, String cal, String pais, String generos, String productoras) {
        List<String> unknown = new ArrayList<>();
        unknown.add("Unknown");
        this.reparto = new ArrayList<>();
        this.titulo = titulo.trim().replaceAll("[ :]", "_");
        this.calificacion = !cal.equals("N/A") ? Double.parseDouble(cal) : 0.0;
        this.pais = !pais.equals("N/A") ? replaceOnList(pais.split(",")) : unknown;
        this.generos = !generos.equals("N/A") ? replaceOnList(generos.split(",")) : unknown;
        this.productoras = !productoras.equals("N/A") ? replaceOnList(productoras.split(",")) : unknown;
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

    public List<String> getPais() {
        return pais;
    }

    public void setPais(List<String> pais) {
        this.pais = pais;
    }

    public List<String> getGeneros() {
        return generos;
    }

    public void setGeneros(List<String> generos) {
        this.generos = generos;
    }

    public List<String> getProductoras() {
        return productoras;
    }

    public void setProductoras(List<String> productoras) {
        this.productoras = productoras;
    }

    public void addActor(Actor actor) {
        if (!reparto.contains(actor)) this.reparto.add(actor);
    }

    public List<String> replaceOnList(String[] entrada) {
        List<String> salida = new ArrayList<>();
        for (String individual : entrada) {
            salida.add(individual.trim().replaceAll("[ :]", "_"));
        }
        return salida;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pelicula pelicula = (Pelicula) o;
        return Objects.equals(titulo, pelicula.titulo);
    }


}
