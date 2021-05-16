package sbc.maven.urjc.es;


import org.apache.jena.query.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase necesaria para la realizacion de peticiones SPARQL a repositorios RDF mnediante el uso de JENA y extraccion de la informacion devuelta.
 */
public class Jena {
    String service;    //Servicio de la consulta
    ResultSet result; //ResultSet que devuelve la consulta

    public Jena() {
        this.service = "http://dbpedia.org/sparql";
        this.result = null;
    }

    public Jena(String service) {
        this.service = service;
        this.result = null;
    }

    public List<String> executeQueries(String[][] consultas_vars) {
        List<String> resultado = new ArrayList<String>();
        for (int i = 0; i < consultas_vars.length; i++) {
            resultado = resultado.isEmpty() ? executeQuery(consultas_vars[i][0], consultas_vars[i][1]) : Pelicula.fusion_list_string(resultado, executeQuery(consultas_vars[i][0], consultas_vars[i][1]));
        }
        return resultado;
    }

    /**
     * Funcion encargada de hacer hacer las consultas SPARQL
     */
    public List<String> executeQuery(String aux_consulta, String var_query) {
        Query query = QueryFactory.create(aux_consulta);
        QueryExecution exe = QueryExecutionFactory.sparqlService(service, query);
        result = exe.execSelect();
        List<String> listado = new ArrayList<String>();

        if (result != null) {
            while (result.hasNext()) {
                QuerySolution qResult = result.next();
                for (String var : var_query.split(";")) {
                    String aux = qResult.get(var).toString();
                    String title = (String) aux.subSequence(0, aux.length() - 3);
                    listado.add(title);
                }
            }
        }
        return listado;
    }

}
