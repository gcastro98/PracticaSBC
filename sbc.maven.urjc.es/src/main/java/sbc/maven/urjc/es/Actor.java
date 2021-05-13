package sbc.maven.urjc.es;

import java.util.Objects;

public class Actor {
    private String name;
    private double avg_calification;
    private int nmovies;

    public Actor(String name) {
        this.name = name.trim().replace(" ","_");
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

    public void addFilm(String cal){
        if(!cal.equals("N/A")){
            double calificacion = Double.parseDouble(cal);
            this.avg_calification = (avg_calification * nmovies + calificacion) / (nmovies +1);
            this.nmovies++;
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
