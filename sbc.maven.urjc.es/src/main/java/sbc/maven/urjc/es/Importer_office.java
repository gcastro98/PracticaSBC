package sbc.maven.urjc.es;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.*;

public class Importer_office {

    private Tuple<Actor, Pelicula> tupla;

    public Importer_office() {
        List<Actor> actors = new ArrayList<Actor>();
        List<Pelicula> movies = new ArrayList<Pelicula>();
        this.tupla = new Tuple<Actor, Pelicula>(actors, movies);
    }

    public Importer_office(List<Actor> actors, List<Pelicula> movies) {
        this.tupla = new Tuple<Actor, Pelicula>(actors, movies);
    }

    public Tuple<Actor, Pelicula> movies_from_excel(String ruta) throws IOException {

        //xlsx
        FileInputStream docXlsx = new FileInputStream(new File(ruta));
        XSSFWorkbook XLSXworkBook = new XSSFWorkbook(docXlsx);

        Map<Integer, String> map = new HashMap();
        map.put(1, "titulo");
        map.put(2, "MPAA_rating");
        map.put(3, "presupuesto");
        map.put(4, "beneficio_bruto");
        map.put(6, "genero");
        map.put(8, "calificacion");


        int fila = 0;
        do {
            List<String> contenido_pelicula = new ArrayList();
            XSSFSheet xlsxsheet = XLSXworkBook.getSheet("Movies");
            XSSFRow xlsxrow = xlsxsheet.getRow(1 + fila);
            Map<String, String> aux_map = new HashMap();
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                String valor = xlsxrow.getCell((int) pair.getKey()).getCellType().toString().equals("NUMERIC") ? xlsxrow.getCell((int) pair.getKey()).getRawValue() : xlsxrow.getCell((int) pair.getKey()).getStringCellValue();
                aux_map.put((String) pair.getValue(), valor);
            }
            Pelicula aux = new Pelicula(aux_map, null, null);
            this.tupla.getPelicula().add(aux);
            fila++;

        } while (fila < 50);
        return this.tupla;
    }

    public Tuple<Actor, Pelicula> movies_from_word(String ruta) throws IOException {

        //xlsx
        FileInputStream docxFile = new FileInputStream(ruta);
        XWPFDocument docx = new XWPFDocument(docxFile);
        XWPFWordExtractor docxExt = new XWPFWordExtractor(docx);
        String[] aux = docxExt.getText().split("\n");
        String[] lineas = Arrays.copyOfRange(aux,1,aux.length-1);
        for (String linea: lineas) {
            tupla.getPelicula().add(line_to_Movie(linea));
        }
        return this.tupla;
    }

    public List<Pelicula> cribado(List<Pelicula> originales, List<Pelicula> candidatas){
        for (Pelicula candidata : candidatas){
            if (originales.contains(candidata)) {
                originales.get(originales.indexOf(candidata)).fusion_movie(candidata);
            } else {
                originales.add(candidata);
            }
        }
        return originales;
    }
    public Pelicula line_to_Movie(String linea){
        Map<String, String> map = new HashMap<>();
        String[] aux = linea.split("min\\) --> ");
        map.put("descripcion",aux[1]);
        String[] primera_parte = aux[0].split("\\(");
        map.put("titulo", primera_parte[0]);
        map.put("estreno", primera_parte[1].split("/")[0]);
        map.put("duracion", primera_parte[1].split("/")[1]);
        return new Pelicula(map, null, null);
    }
}
