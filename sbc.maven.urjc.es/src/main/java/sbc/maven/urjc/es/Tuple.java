package sbc.maven.urjc.es;

import java.util.List;

/**
 *
 * @param <Actores>
 * @param <Peliculas>
 *
 * Clase auxiliar para el uso de Tuplas, dado que Java no incluye esa funcionalidad.
 */
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

	public void addActor(Actor actor){
		actores.add(actor);
	}

	public void addPelicula(Pelicula pelicula){
		peliculas.add(pelicula);
	}
}
