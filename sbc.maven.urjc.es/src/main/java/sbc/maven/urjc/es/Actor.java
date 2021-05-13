package sbc.maven.urjc.es;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;

public class Actor {
    private String name;
    private double avg_calification;
    private int nmovies;

    public Actor(String name) {
        this.name = name.trim().replace(" ", "_");
        this.avg_calification = 0;
        this.nmovies = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAvg_calification() {
        return avg_calification;
    }

    public void setAvg_calification(double avg_calification) {
        this.avg_calification = avg_calification;
    }

    public int getNmovies() {
        return nmovies;
    }

    public void setNmovies(int nmovies) {
        this.nmovies = nmovies;
    }

    public void addFilm(String cal) {
        if (!cal.equals("N/A")) {
            DecimalFormat formato1 = new DecimalFormat("#,###");
            double calificacion = Double.parseDouble(cal);
            this.avg_calification = (avg_calification * nmovies + calificacion) / (nmovies + 1);
            this.avg_calification = truncateDecimal(avg_calification, 2).doubleValue();
            this.nmovies++;
        }
    }

    //https://www.it-swarm-es.com/es/java/como-puedo-truncar-un-doble-solo-dos-decimales-en-java/939826465/#:~:text=Si%20necesita%20decimales%2C%20use%20una,DecimalFormat%20para%20obtener%20una%20String%20.&text=Si%2C%20por%20alguna%20raz%C3%B3n%2C%20no,simplemente%20lance%20a%20int
    private static BigDecimal truncateDecimal(double x, int numberofDecimals) {
        if (x > 0) {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_FLOOR);
        } else {
            return new BigDecimal(String.valueOf(x)).setScale(numberofDecimals, BigDecimal.ROUND_CEILING);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return Objects.equals(name.trim(), actor.name.trim());
    }

    @Override
    public String toString() {
        return "Actor{" +
                "name='" + name + '\'' +
                ", avg_calification=" + avg_calification +
                ", nmovies=" + nmovies +
                "}\n";
    }
}
