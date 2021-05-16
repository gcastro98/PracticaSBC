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
        /**
         * Declaración de variables.
         */
        List<Pelicula> movies = new ArrayList<Pelicula>();
        List<Actor> actors = new ArrayList<Actor>();
        Jena jena = new Jena();
        Ontol ontologia = new Ontol("ontologias/MovieOntology.owl","http://sbc2019Movie/ont/");
        Importer_office importer_office = new Importer_office();
        /**
         * máximo 300 Peliculas y su distribuidora en las que actua Johnny Depp, y la productora sea Disney.
         */
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
        /**
         * máximo 300 Peliculas de Disney.
         */
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
        /**
         * Peliculas en las que han trabajado 2 deportistas del mismo equipo.
         */
        String query_film_Deportistas =
            "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
            "PREFIX yago: <http://dbpedia.org/class/yago/>\n" +
            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
            "SELECT DISTINCT ?nombre_pelicula\n" +
            "WHERE{  \n" +
            "?pelicula rdf:type dbo:Film.\n" +
            "?pelicula dbo:starring ?actor2.\n" +
            "?pelicula dbo:starring ?actor1.\n" +
            "?actor1 dbo:team ?equipo.\n" +
            "?actor2 dbo:team ?equipo.\n" +
            "?pelicula foaf:name ?nombre_pelicula.\n" +
            "Filter(?actor1 != ?actor2)\n" +
            " }\n" +
            "ORDER BY DESC(?nombre_pelicula)\n";

        /**
         * Peliculas de Warner en las que ha participado un deportista que pertenece a un equipo.
         */
        String query_film_Warner =
                "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                "PREFIX yago: <http://dbpedia.org/class/yago/>\n" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "SELECT DISTINCT ?nombre_pelicula\n" +
                "WHERE{  \n" +
                "?pelicula rdf:type dbo:Film.\n" +
                "?pelicula dbo:starring ?actor.\n" +
                "?actor dbo:team ?equipo.\n" +
                "?pelicula foaf:name ?nombre_pelicula.\n" +
                "?pelicula dbo:distributor ?distribuidora.\n" +
                "?distribuidora foaf:name ?nombre_distribuidor.\n" +
                "Filter(regex(str(?nombre_distribuidor),\"Warner\"))\n" +
                " }\n" +
                "ORDER BY DESC(?nombre_pelicula)";

/**
 * Opteción de información
 */
        System.out.println("Realizando consultas SPARQL...");
        String[][] queries = {{query_films_disney,"nombre_pelicula"},{query_films_disney_by_jd, "nombre_pelicula"},{query_film_Deportistas,"nombre_pelicula"},{query_film_Warner,"nombre_pelicula"}};
        List<String> listado = jena.executeQueries(queries);

        System.out.println("Realizando consultas a APIs...");
        Tuple<Actor,Pelicula> tuple = API_Connection.fromJSONtoObject(listado);
        actors.addAll(tuple.getActores());
        movies.addAll(tuple.getPelicula());

        System.out.println("Importando de Office...");
        List<Pelicula> movies_from_xlsx = importer_office.movies_from_excel("res/view.xlsx").getPelicula();
        List<Pelicula> movies_from_word = importer_office.movies_from_word("res/Classics of cinema.docx").getPelicula();
        movies = importer_office.cribado(movies, movies_from_xlsx);
        movies = importer_office.cribado(movies, movies_from_word);
/**
 * Uso de la información obtenida
 */
        System.out.println("Aplicando Ontologia...");
        ontologia.loadOntology();
        ontologia.addPeliculas(movies);
        ontologia.addActores(actors);
        ontologia.addSubClass("http://sbc2019Movie/ont/Estrella","http://dbpedia.org/ontology/Actor");
        ontologia.addExpresion("http://sbc2019Movie/ont/esLaEstrellaDe","http://www.movieontology.org/2009/11/09/Movie","http://sbc2019Movie/ont/Estrella");
        ontologia.addSubClass("http://sbc2019Movie/ont/Comico","http://dbpedia.org/ontology/Actor");
        ontologia.addExpresion("http://sbc2019Movie/ont/esGraciosoEn","http://www.movieontology.org/2009/11/09/Movie","http://sbc2019Movie/ont/Comico");
        ontologia.addSubClass("http://sbc2019Movie/ont/Exitazo","http://www.movieontology.org/2009/11/09/Movie");
        ontologia.addExpresion("http://sbc2019Movie/ont/esUnExitoDe","http://www.movieontology.org/2009/10/01/movieontology.owl#Production_Company","http://sbc2019Movie/ont/Exitazo");
        ontologia.inferringInfo(movies);
        ontologia.saveOntology();
        /**
         * Comentamos la linea del razonador porque ELK da errores y borra axiomas.
         * Usamos el razonador HermiT de Protégé.
         */
        //ontologia.razonador();
        System.out.println("FIN Ejecucion");

    }




}














