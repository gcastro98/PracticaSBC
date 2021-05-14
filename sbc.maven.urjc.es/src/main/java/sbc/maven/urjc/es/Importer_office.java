package sbc.maven.urjc.es;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

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
        int[] position = {1, 2, 3, 4, 6, 8};

        int fila = 0;
        do {
            List<String> contenido_pelicula = new ArrayList();
            XSSFSheet xlsxsheet = XLSXworkBook.getSheet("Movies");
            XSSFRow xlsxrow = xlsxsheet.getRow(1 + fila);
            for (int pos : position) {
                String valor = xlsxrow.getCell(pos).getCellType().toString().equals("NUMERIC") ? xlsxrow.getCell(pos).getRawValue() : xlsxrow.getCell(pos).getStringCellValue();
                contenido_pelicula.add(valor);

            }
            Pelicula aux = new Pelicula(contenido_pelicula.get(0), contenido_pelicula.get(1), contenido_pelicula.get(2), contenido_pelicula.get(3), contenido_pelicula.get(4), contenido_pelicula.get(5));
            this.tupla.getPelicula().add(aux);
            fila++;

        } while (fila < 50);
        return this.tupla;
    }

    public List<Pelicula> cribado(List<Pelicula> originales, List<Pelicula> candidatas){
        for (Pelicula candidata : candidatas){
            if (originales.contains(candidata)) {
                System.out.println( originales.get(originales.indexOf(candidata)).getTitulo().equals(candidata.getTitulo()));
                originales.get(originales.indexOf(candidata)).fusion_movie(candidata);
            } else {
                originales.add(candidata);
            }
        }
        return originales;
    }
}
