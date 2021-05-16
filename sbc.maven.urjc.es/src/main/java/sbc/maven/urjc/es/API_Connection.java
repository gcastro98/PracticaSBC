package sbc.maven.urjc.es;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de proporcionar acceso a las APIs y recopilacion de informacion de las mismas,
 * incluyendo metodos que permiten extraer informacion de los JSON proporcionados por la API de IMDB.
 */
public class API_Connection {
    public static JsonObject PeticionAPI(String title, String tipo) {
        String url = (tipo.equals("WIKI") ? "https://es.wikipedia.org/w/api.php?action=categorytree&category=Libros&format=json" : "http://www.omdbapi.com/?apikey=6142688e&plot=sort&t=") + fromStringtoQueryString(title);

        try {
            ClientConfig cc = new ClientConfig();
            Client client = ClientBuilder.newClient(cc);
            URI uri = UriBuilder.fromUri(url).build();
            WebTarget wt = client.target(uri);
            JsonObject convertedObject = new Gson().fromJson(wt.request().accept(MediaType.APPLICATION_JSON).get(String.class), JsonObject.class);
            return convertedObject;
        }catch (Exception e) {
            System.out.println(e);
        }
    return null;
    }

/**
 *  Para hacer la consulta a la api hay que cambiar los espacios por "+".
 */
   public static String fromStringtoQueryString(String entrada) {
	   return entrada.trim().replace(" ", "+");
   }

    /**
     * Realiza petición a la api para objeter el JSON y parsearlo para obtener la tupla.
     */
    public static Tuple<Actor,Pelicula> fromJSONtoObject(List<String> listado){
        List<Pelicula> movies = new ArrayList<Pelicula>();
        List<Actor> actors = new ArrayList<Actor>();
        for (String aux : listado) {
            JsonObject imdb = API_Connection.PeticionAPI(aux, "IMDB");
            if (imdb.get("Response").getAsString().equals("True") && imdb.has("Title") && imdb.has("imdbRating") && imdb.has("Country") && imdb.has("Genre") && imdb.has("Production"))
            {
                Pelicula pelicula_aux = new Pelicula(imdb.get("Title").getAsString(), imdb.get("imdbRating").getAsString(), imdb.get("Country").getAsString(), imdb.get("Genre").getAsString(), imdb.get("Production").getAsString());
                
                if (!movies.contains(pelicula_aux))
                {
                	movies.add(pelicula_aux);
                	String[] auxActores = imdb.get("Actors").getAsString().split(",");
	                for (String actorInAux : auxActores) {
	                    Actor actor = new Actor(actorInAux);
	                    if (actors.contains(actor)) {
	                        actor = actors.get(actors.indexOf(actor));
	                    } else {
	                        actors.add(actor);
	                    }
	                    actor.addFilm(String.valueOf(pelicula_aux.getCalificacion()));
	                    pelicula_aux.addActor(actor);
	                }
                }
            }
        }
        Tuple<Actor,Pelicula> returnedTuple = new Tuple<>(actors,movies);
        return returnedTuple;
    }
}

