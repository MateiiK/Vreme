package matekgames.com.vreme;

public class Dan {
    public final static String Postaje_danVTednu="danVTednu";  // name of employee
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
    String vlaga;
    String smerVetra;
    String tlak;
    String danVTednu;
    String SunekVetra;
    double lat;
    double lon;

    public String getDanVTednu() {
        return danVTednu;
    }
    public void setDanVTednu(String danVTednu) {
        this.danVTednu = danVTednu;
    }

    public String getRegija() {
        return regija;
    }
    public void setRegija(String regija) {
        this.regija = regija;
    }

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

    public String getSmerVetra(){
        return smerVetra;
    }
    public void setSmerVetra(String smerVetra){
        this.smerVetra = smerVetra;
    }

    public String getSunekVetra(){
        return SunekVetra;
    }
    public void setSunekVetra(String SunekVetra){
        this.SunekVetra = SunekVetra;
    }

    public String getVlaga(){
        return vlaga;
    }
    public void setVlaga(String vlaga){
        this.vlaga = vlaga;
    }

    public String getTlak(){return tlak;}
    public void setTlak(String tlak){this.tlak = tlak;}

    public String getStanjeOb(){return stanjeOb;}
    public void setStanjeOb(String stanjeOb){this.stanjeOb = stanjeOb;}

    public int getIcon(){
        return icon;
    }
    public void setIcon(int icon){
        this.icon = icon;
    }






}
