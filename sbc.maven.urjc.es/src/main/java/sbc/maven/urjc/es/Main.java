package sbc.maven.urjc.es;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import org.xml.sax.SAXException;


public class Main {
    public static void main(String[] args) throws IOException, SAXException {


        String auxJohnnyDepp =
                "PREFIX dbo: <http://dbpedia.org/ontology/>\r\n" +
                        "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\r\n" +
                        "PREFIX yago: <http://dbpedia.org/class/yago/>\r\n" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
                        "SELECT Distinct ?nombre_distribuidor ?nombre_pelicula   \r\n " +
                        "WHERE{ " +
                        "?pelicula rdf:type dbo:Film.\r\n" +
                        "?pelicula dbo:distributor ?distribuidor.\r\n" +
                        "?pelicula dbo:starring ?actor1.\r\n" +
                        "?actor1 foaf:name ?name.\r\n" +
                        "?distribuidor foaf:name ?nombre_distribuidor.\r\n" +
                        "?pelicula foaf:name ?nombre_pelicula.\r\n" +
                        "Filter(regex(str(?nombre_distribuidor),\"Walt Disney Studios Motion Pictures\"))" +
                        "Filter(regex(str(?name),\"Johnny Depp\"))\r\n" +
                        " }\r\n" +
                        "ORDER BY DESC(?nombre_pelicula)\r\n"
                        +"Limit 300\r\n";

        String auxnFilms =
                "PREFIX dbo: <http://dbpedia.org/ontology/>\r\n" +
                        "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\r\n" +
                        "PREFIX yago: <http://dbpedia.org/class/yago/>\r\n" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
                        "SELECT Distinct (count(?nombre_pelicula) as ?numero_peliculas) ?nombre_distribuidor \r\n " +
                        "WHERE{ " +
                        "?pelicula rdf:type dbo:Film.\r\n" +
                        "?pelicula dbo:distributor ?distribuidor.\r\n" +
                        "?distribuidor foaf:name ?nombre_distribuidor.\r\n" +
                        "?pelicula foaf:name ?nombre_pelicula.\r\n" +
                        "Filter(regex(str(?nombre_distribuidor),\"Disney\"))" +
                        " }\r\n" +
                        "GROUP BY ?nombre_distribuidor\r\n" +
                        "ORDER BY ?nombre_distribuidor\r\n";
        String auxFilms =
                "PREFIX dbo: <http://dbpedia.org/ontology/>\r\n" +
                        "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\r\n" +
                        "PREFIX yago: <http://dbpedia.org/class/yago/>\r\n" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
                        "SELECT Distinct ?nombre_distribuidor ?nombre_pelicula   \r\n " +
                        "WHERE{ " +
                        "?pelicula rdf:type dbo:Film.\r\n" +
                        "?pelicula dbo:distributor ?distribuidor.\r\n" +
                        "?distribuidor foaf:name ?nombre_distribuidor.\r\n" +
                        "?pelicula foaf:name ?nombre_pelicula.\r\n" +
                        "Filter(regex(str(?nombre_distribuidor),\"Walt Disney Studios Motion Pictures\"))" +
                        " }\r\n" +
                        "ORDER BY DESC(?nombre_pelicula)\r\n"
                        +"Limit 300\r\n";
        String auxContinent =
                "PREFIX other: <http://www.loc.gov/mads/rdf/v1#>\r\n" +
                        "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\r\n" +
                        "PREFIX yago: <http://dbpedia.org/class/yago/>\r\n" +
                        "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" +
                        "SELECT Distinct * \r\n " +
                        "WHERE{ " +
                        "?continente rdf:type other:Continent.\r\n" +
                        " }\r\n" +
                        "Limit 100\r\n";


        Jena jena = new Jena();
        //jena.executeQuery_aux(auxContinent);
        List<Pelicula> movies = new ArrayList<Pelicula>();
        List<Actor> actors = new ArrayList<Actor>();
        List<String> listado = jena.executeQuery(auxJohnnyDepp);
        for (String aux : listado) {
            JsonObject imdb = API_Connection.PeticionAPI(aux, "IMDB");
            if (imdb.get("Response").getAsString().equals("True") && imdb.has("Title") && imdb.has("imdbRating") && imdb.has("Country") && imdb.has("Genre") && imdb.has("Production"))
            {
                //System.out.println((imdb.get("Title").getAsString() +"           "+  imdb.get("imdbRating").getAsString()  +"           "+  imdb.get("Country").getAsString() +"           "+  imdb.get("Genre").getAsString() +"           "+  imdb.get("Production").getAsString()));

                Pelicula pelicula_aux = new Pelicula(imdb.get("Title").getAsString(), imdb.get("imdbRating").getAsString(), imdb.get("Country").getAsString(), imdb.get("Genre").getAsString(), imdb.get("Production").getAsString());
                movies.add(pelicula_aux);
                String[] auxActores = imdb.get("Actors").getAsString().split(",");
                for (String nombre : auxActores) {
                    Actor actor = new Actor(nombre);
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
        System.out.println("actores" + actors.size() + ", peliculas" + movies.size());
//        System.out.println(actors.toString());
        System.out.println(actors.toString());

    }

//        API_Connection.PeticionAPI(aux, "WIKI");
//        Ontol ontologia = new Ontol("MovieOntology.owl","http://www.movieontology.org/2010/01/movieontology.owl");
//        ontologia.createOntology();
//        ontologia.loadOntology();
//        ontologia.saveOntology();
    }














