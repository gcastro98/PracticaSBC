package sbc.maven.urjc.es;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.jsonldjava.utils.JsonUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.xml.sax.SAXException;


public class Main {
    public static void main(String[] args) throws IOException, SAXException {
        List<Pelicula> movies = new ArrayList<Pelicula>();
        List<Actor> actors = new ArrayList<Actor>();

        String query_films_disney_by_jd =
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
                        + "Limit 300\r\n";

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
        String query_films_disney =
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
                        + "Limit 300\r\n";
        String query_films_disney_has_book =
                "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                        "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                        "\n" +
                        "SELECT Distinct ?nombre_pelicula\n" +
                        "WHERE\n" +
                        "{ \n" +
                        "?book rdf:type dbo:Book.\n" +
                        "?pelicula rdf:type dbo:Film.\n" +
                        "?book foaf:name ?nombre_pelicula.\n" +
                        "?pelicula foaf:name ?nombre_pelicula.\n" +
                        "\n" +
                        "Filter (regex(str(?nombre_pelicula),\"..*\"))\n" +
                        "\n" +
                        "?pelicula dbo:distributor ?productora.\n" +
                        "?productora foaf:name ?nombre_prod.\n" +
                        "Filter (regex(str(?nombre_prod),\"Disney\"))\n" +
                        "}";
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

//
        Jena jena = new Jena();
        List<String> listado = jena.executeQuery(query_films_disney_by_jd, "nombre_pelicula");
        Tuple<Actor,Pelicula> tuple = API_Connection.fromJSONtoObject(listado);
        actors.addAll(tuple.getActores());
        movies.addAll(tuple.getPelicula());


////      API_Connection.PeticionAPI(aux, "WIKI");
        Importer_office importer_office = new Importer_office();
        List<Pelicula> movies_from_xlsx = importer_office.movies_from_excel("res/view.xlsx").getPelicula();
        List<Pelicula> movies_from_word = importer_office.movies_from_word("res/Classics of cinema.docx").getPelicula();
        movies = importer_office.cribado(movies, movies_from_xlsx);
        movies = importer_office.cribado(movies, movies_from_word);

        Ontol ontologia = new Ontol("ontologias/MovieOntology.owl","http://sbc2019Movie/ont/");
        ontologia.loadOntology();
        ontologia.addPeliculas(movies);
        ontologia.addActores(actors);
        ontologia.saveOntology();
        //ontologia.razonador();

    }



}














