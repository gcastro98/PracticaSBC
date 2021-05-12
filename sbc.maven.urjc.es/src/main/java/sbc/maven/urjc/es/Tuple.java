package sbc.maven.urjc.es;

import java.util.List;

public class Tuple<Actores, Peliculas> {
	public final List<Actor> actores;
	public final List<Pelicula> peliculas;

	public Tuple(List<Actor> x, List<Pelicula> y) {
		this.actores = x;
		this.peliculas = y;
	}

	public List<Actor> getActores() {
		return actores;

	}

	public List<Pelicula> getPelicula() {
		return peliculas;
	}
}
