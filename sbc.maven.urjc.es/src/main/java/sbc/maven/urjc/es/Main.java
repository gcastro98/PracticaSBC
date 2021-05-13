package sbc.maven.urjc.es;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;


public class Main {
    public static void main(String[] args) throws IOException, SAXException {
    	List<Pelicula> movies = new ArrayList<Pelicula>();
    	List<Actor> actors = new ArrayList<Actor>();

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

//        List<String> listado = jena.executeQuery(auxJohnnyDepp);
//        Tuple<Actor,Pelicula> tuple = API_Connection.fromJSONtoObject(listado);
//        actors.addAll(tuple.getActores());
//        movies.addAll(tuple.getPelicula());
//        System.out.println("actores" + actors.size() + ", peliculas" + movies.size());
//        System.out.println(actors.toString());
//        System.out.println(actors.toString());


//        API_Connection.PeticionAPI(aux, "WIKI");
        Ontol ontologia = new Ontol("NuevaOntologia.owl","http://sbc2019/ont");
        ontologia.createOntology();
        ontologia.addClass("Animal");
        ontologia.addSubClass("Vaca", "Animal");
        ontologia.addSubClass("Tigre", "Animal");
        ontologia.addObjectProperty("come","Animal", "Animal");
        ontologia.addExpresion("come", "Animal","Carnivoro");
        ontologia.createInstanciaWithProperty("come","Tigreton","Tigre","Querie","Vaca");
        ontologia.addObjectProperty("come", "Tigre","Vaca");
//        ontologia.addDataProperty("Calificacion", "xsd:double");
//        ontologia.addObjectProperty("calificacion", "Actor", "Calificacion");
//        ontologia.addPeliculas(movies);
//        ontologia.addActores(actors);
        ontologia.saveOntology();
        ontologia.razonador();

    }
}














