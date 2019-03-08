package matekgames.com.vreme;

import android.media.Image;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Dan {

    String datum;
    String delDneva;
    int icon;
    String temp;
    String razmere;
    String minTemp;
    String maxTemp;
    String veter;
    String regija;
    String stanjeOb;
    double lat;
    double lon;



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

    public String getRegija(){
        return regija;
    }
    public void setRegija(String regija){
        this.regija = regija;
    }

    public String getStanjeOb(){
        return stanjeOb;
    }
    public void setStanjeOb(String stanjeOb){
        this.stanjeOb = stanjeOb;
    }

    public int getIcon(){
        return icon;
    }
    public void setIcon(int icon){
        this.icon = icon;
    }

    public Double getLjubljanaLongtitude(){return lon=14.5172;}
    public Double getLjubljanaLatitude(){return lat=46.0658;}
    public Double getBiljeLongtitude(){return lon=13.6289;}
    public Double getBiljeLatitude(){return lat=45.8958;}





}
