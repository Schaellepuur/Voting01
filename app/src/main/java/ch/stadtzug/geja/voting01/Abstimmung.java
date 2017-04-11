package ch.stadtzug.geja.voting01;

/**
 * Created by gerlach jan on 11.04.2017.
 */

class Abstimmung {
    private int id;
    private String Name;
    private String abstimmungstext;
    private String option1, option2, option3;

    public Abstimmung(int id, String name, String abstimmungstext) {
        this.id = id;
        Name = name;
        this.abstimmungstext = abstimmungstext;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAbstimmungstext() {
        return abstimmungstext;
    }

    public void setAbstimmungstext(String abstimmungstext) {
        this.abstimmungstext = abstimmungstext;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }
}
