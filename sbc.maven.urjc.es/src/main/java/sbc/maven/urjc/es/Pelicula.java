package sbc.maven.urjc.es;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Pelicula {
    private String titulo;
    private List<Actor> reparto;
    private double calificacion;
    private List<String> pais;
    private List<String> generos;
    private List<String> productoras;
    private String MPAA_rating;
    private Long presupuesto;
    private Long beneficio_bruto;
    private String estreno;
    private String descripcion;
    private int duracion;

    public Pelicula(){
        List<String> unknown = new ArrayList<>();
        unknown.add("Unknown");
        this.titulo = "";
        this.calificacion = 0.0;
        this.generos =  unknown;
        this.MPAA_rating = "Unknown";
        this.presupuesto = 0L;
        this.beneficio_bruto = 0L;
        this.reparto = new ArrayList<>();
        this.productoras = new ArrayList<>();
        this.pais = new ArrayList<>();
        this.estreno = "";
        this.descripcion = "";
        this.duracion = 0;
    }

    public Pelicula(String titulo, List<Actor> reparto, String cal, String pais, String generos, String productoras) {

        this(titulo, cal, pais, generos, productoras);
        this.reparto = reparto;
    }

    public Pelicula(String titulo, String cal, String pais, String generos, String productoras) {
        this();
        List<String> unknown = new ArrayList<>();
        unknown.add("Unknown");
        this.reparto = new ArrayList<>();
        this.titulo = titulo.trim().replaceAll("[ :]", "_");
        this.calificacion = !cal.equals("N/A") ? Double.parseDouble(cal) : 0.0;
        this.pais = !pais.equals("N/A") ? replaceOnList(pais.split(",")) : unknown;
        this.generos = !generos.equals("N/A") ? replaceOnList(generos.split(",")) : unknown;
        this.productoras = !productoras.equals("N/A") ? replaceOnList(productoras.split(",")) : unknown;
    }

    public Pelicula(String titulo, String MPAA_rating, String presupuesto, String beneficio_bruto, String generos, String calificacion) {
        this();
        List<String> unknown = new ArrayList<>();
        unknown.add("Unknown");
        this.titulo = titulo.trim().replaceAll("[ :]", "_");
        this.calificacion = !calificacion.equals("N/A") ? Double.parseDouble(calificacion) : 0.0;
        this.generos = !generos.equals("N/A") ? replaceOnList(generos.split(",")) : unknown;
        this.MPAA_rating = MPAA_rating.trim();
        this.presupuesto = Long.parseLong(presupuesto);
        this.beneficio_bruto = Long.parseLong(beneficio_bruto);

    }

    public Pelicula(Map<String, String> map, Map<String, List<String>> map_list, List<Actor> actores){
        this();
        List<String> unknown = new ArrayList<>();
        unknown.add("Unknown");
        if( map != null && !map.isEmpty() ){
            this.titulo = map.containsKey("titulo") ? map.get("titulo").trim().replaceAll("[\\/ :]", "_") : this.titulo;
            this.calificacion = map.containsKey("evaluacion") ? Double.parseDouble(map.get("evaluacion").trim()) : this.calificacion;
            this.MPAA_rating = map.containsKey("MPAA_rating") ? map.get("MPAA_rating") : this.MPAA_rating;
            this.presupuesto = map.containsKey("presupuesto") ? Long.parseLong(map.get("presupuesto").trim()) : this.presupuesto;
            this.beneficio_bruto = map.containsKey("beneficio_bruto") ? Long.parseLong(map.get("beneficio_bruto").trim()) : this.beneficio_bruto;;
            this.estreno = map.containsKey("estreno") ? map.get("estreno").trim() : this.estreno;
            this.descripcion = map.containsKey("descripcion") ? map.get("descripcion").trim() : this.descripcion;
            this.duracion = map.containsKey("duracion") ? Integer.parseInt(map.get("duracion").trim()) : this.duracion;;
        }
        if(map_list != null && !map_list.isEmpty()  ) {
            this.generos = map_list.containsKey("generos") ? map_list.get("generos") : this.generos;
            this.productoras = map_list.containsKey("productoras") ? map_list.get("productoras") : this.productoras;
            this.pais = map_list.containsKey("pais") ? map_list.get("pais") : this.pais;
        }
        this.reparto = actores != null && !actores.isEmpty() ? actores : reparto;
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

    public String getMPAA_rating() {
        return MPAA_rating;
    }

    public void setMPAA_rating(String MPAA_rating) {
        this.MPAA_rating = MPAA_rating;
    }

    public Long getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Long presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Long getBeneficio_bruto() {
        return beneficio_bruto;
    }

    public String getEstreno() {
        return estreno;
    }

    public void setEstreno(String estreno) {
        this.estreno = estreno;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setBeneficio_bruto(Long beneficio_bruto) {
        this.beneficio_bruto = beneficio_bruto;
    }

    public List<String> replaceOnList(String[] entrada) {
        List<String> salida = new ArrayList<>();
        for (String individual : entrada) {
            salida.add(individual.trim().replaceAll("[ :]", "_"));
        }
        return salida;
    }
    public void fusion_movie( Pelicula candidata){
        if (titulo.equals(candidata.getTitulo())){
            this.MPAA_rating = candidata.getMPAA_rating().trim() != "Unknown" ? candidata.getMPAA_rating().trim() : this.MPAA_rating;
            this.presupuesto = candidata.getPresupuesto() != 0.0 ? candidata.getPresupuesto() : this.presupuesto;
            this.beneficio_bruto = candidata.getBeneficio_bruto() != 0.0 ? candidata.getBeneficio_bruto() : this.beneficio_bruto;
            this.calificacion = calificacion == 0.0 ? candidata.getCalificacion() : calificacion;
            this.estreno = candidata.getEstreno() != "" ? candidata.getEstreno() : this.estreno;
            this.descripcion = candidata.getDescripcion() != "" ? candidata.getDescripcion() : this.descripcion;
            this.duracion = candidata.getDuracion() != 0 ? candidata.getDuracion()  : this.duracion;;

        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pelicula pelicula = (Pelicula) o;
        return Objects.equals(titulo, pelicula.titulo);
    }

    @Override
    public String toString() {
        return "Pelicula{" +
                "titulo='" + titulo + '\'' +
                ", calificacion=" + calificacion +
                ", pais=" + pais +
                ", generos=" + generos +
                ", productoras=" + productoras +
                ", MPAA_rating='" + MPAA_rating + '\'' +
                ", presupuesto=" + presupuesto +
                ", beneficio_bruto=" + beneficio_bruto +
                ", estreno='" + estreno + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", duracion=" + duracion +
                ",\\n reparto=" + reparto +
                '}';
    }
}
