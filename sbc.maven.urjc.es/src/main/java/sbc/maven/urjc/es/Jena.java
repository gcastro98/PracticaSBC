package sbc.maven.urjc.es;


import org.apache.jena.query.*;

import java.util.ArrayList;
import java.util.List;

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

    public List<String> executeQuery(String aux_consulta) {

        Query query = QueryFactory.create(aux_consulta);
        QueryExecution exe = QueryExecutionFactory.sparqlService(service, query);
        result = exe.execSelect();
//        ResultSetFormatter.out(result);
        List<String> listado = new ArrayList<String>();

        if (result != null) {
            while (result.hasNext()) {
                QuerySolution qResult = result.next();
                String aux = qResult.get("nombre_pelicula").toString();
                String title = (String) aux.subSequence(0, aux.length() - 3);
                listado.add(title);
//			 Iterator<String> itNames = qResult.varNames();

//			 while(itNames.hasNext()){
//				 String varNames = itNames.next();
//				 if(itNames.equals("nombre_pelicula") listado.add(q))
//				 System.out.println(varNames+"\t"+qResult.get(varNames));
//			 }

            }


        }
//        System.out.println(listado);
        return listado;
    }

    public void executeQuery_aux(String aux_consulta) {

        Query query = QueryFactory.create(aux_consulta);
        QueryExecution exe = QueryExecutionFactory.sparqlService(service, query);
        result = exe.execSelect();
        ResultSetFormatter.out(result);

    }
}
