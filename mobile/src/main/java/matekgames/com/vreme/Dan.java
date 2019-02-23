package matekgames.com.vreme;

import android.media.Image;

public class Dan {

    String datum;
    String delDneva;
    Image icona;
    String temp;
    String razmere;
    String minTemp;
    String maxTemp;
    String veter;



    public String getDatum() {
        return datum;
    }
    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getDelDneva() {
        return delDneva;
    }
    public void setDelDneva(String delDneva) {
        this.delDneva = delDneva;
    }

    public String getRazmere() {
        return razmere;
    }
    public void setRazmere(String razmere) {
        this.razmere = razmere;
    }

    public String getTemp(){
        return temp;
    }
    public void setTemp(String temp){
        this.temp = temp;
    }

    public String getMinTemp(){
        return minTemp;
    }
    public void setMinTemp(String minTemp){
        this.minTemp = minTemp;
    }

    public String getMaxTemp(){
        return maxTemp;
    }
    public void setMaxTemp(String maxTemp){
        this.maxTemp = maxTemp;
    }

    public String getVeter(){
        return veter;
    }
    public void setVeter(String veter){
        this.veter = veter;
    }




}
