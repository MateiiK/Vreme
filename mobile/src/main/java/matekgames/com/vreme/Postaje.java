package matekgames.com.vreme;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Postaje {


    GPSTracker gps;
    Context mContext;
    double lat;
    double lon;
    String postaja;
    String najblizjaPostaja;
    String base = ("http://meteo.arso.gov.si/uploads/probase/www/");
    int count;
    double latMin = 0;
    double lonMin = 0;
    double latMax = 0;
    double lonMax = 0;
    float[] izid = new float[1];
    double testing = 10000000;


    public String getPostaja() {return postaja;}
    public void setPostaja(String postaja) {this.postaja = postaja;}

    public double getLat() {return lat;}
    public void setLat(double lat) {this.lat = lat;}

    public double getLon() {return lon;}
    public void setLon(double lon) {this.lon = lon;}


    String[] postajeTrenutnoPovezave = new String[]{
            "observ/surface/text/sl/observation_NOVA-GOR_latest.xml",
            "observ/surface/text/sl/observation_CELJE_latest.xml",
            "observ/surface/text/sl/observation_CRNOMELJ_latest.xml",
            "observ/surface/text/sl/observation_KATARINA_latest.xml",
            "observ/surface/text/sl/observation_KOCEVJE_latest.xml",
            "observ/surface/text/sl/observation_KREDA-ICA_latest.xml",
            "observ/surface/text/sl/observation_CERKLJE_LETAL-SCE_latest.xml",
            "observ/surface/text/sl/observation_MARIBOR_SLIVNICA_latest.xml",
            "observ/surface/text/sl/observation_LJUBL-ANA_BRNIK_latest.xml",
            "observ/surface/text/sl/observation_LESCE_latest.xml",
            "observ/surface/text/sl/observation_PORTOROZ_SECOVLJE_latest.xml",
            "observ/surface/text/sl/observation_LISCA_latest.xml",
            "observ/surface/text/sl/observation_LJUBL-ANA_BEZIGRAD_latest.xml",
            "observ/surface/text/sl/observation_MURSK-SOB_latest.xml",
            "observ/surface/text/sl/observation_NOVO-MES_latest.xml",
            "observ/surface/text/sl/observation_POSTOJNA_latest.xml",
            "observ/surface/text/sl/observation_RATECE_latest.xml",
            "observ/surface/text/sl/observation_SLOVE-GRA_latest.xml",
            "observ/surface/text/sl/observation_VOGEL_latest.xml",
            "observ/surface/text/sl/observation_VOJSKO_latest.xml"
    };

    XmlPullParserFactory postajePullParserFactory;


    public String getPostaje(Context context) {

        gps = new GPSTracker(context);


        try {
            postajePullParserFactory = XmlPullParserFactory.newInstance();
            final XmlPullParser parserPostajeTrenutno = postajePullParserFactory.newPullParser();

            parserPostajeTrenutno.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            Thread threadVremeTrenutno = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for(count = 0; count<postajeTrenutnoPovezave.length;count++) {
                            final URL postajeTrenutnoURL = new URL(base+postajeTrenutnoPovezave[count]);
                            final InputStream trenutno = postajeTrenutnoURL.openStream();
                            parserPostajeTrenutno.setInput(trenutno, null);
                            ArrayList<Postaje> postajeTrenutnoList = parsePostajaTrenutno(parserPostajeTrenutno);

                            String text;
                            String text2;

                            for (Postaje postaje : postajeTrenutnoList) {
                                double latMin = postaje.getLat();
                                double lonMin = postaje.getLon();
                                Log.e("lat postaje", String.valueOf(postaje.getLat()));
                                Log.e("lon postaje", String.valueOf(postaje.getLon()));
                                Log.e("lat gps get", String.valueOf(gps.getLatitude()));
                                Log.e("lon gps get", String.valueOf(gps.getLongitude()));
                                Location.distanceBetween(gps.getLatitude(),gps.getLongitude(),postaje.getLat(),postaje.getLon(),izid);
                                if(testing> izid[0]){
                                    testing=izid[0];
                                    najblizjaPostaja = base+postajeTrenutnoPovezave[count];
                                    Log.e("Najkrajsa", String.valueOf(postaje.getPostaja()));
                                }
//                                Log.e("Najkrajsa", String.valueOf(testing));
                            }
                            trenutno.close();
                        }count = 0;

                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            threadVremeTrenutno.start();
            threadVremeTrenutno.join();

        } catch ( XmlPullParserException e) {
            e.printStackTrace();
        } catch ( InterruptedException e) {
            e.printStackTrace();
        }
        return najblizjaPostaja;
    }

    private ArrayList<Postaje> parsePostajaTrenutno(XmlPullParser parserPostajeTrenutno) throws XmlPullParserException, IOException {

        ArrayList<Postaje> Postaje = null;
        int eventType = parserPostajeTrenutno.getEventType();
        Postaje postajeTrenutnoList = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    Postaje = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parserPostajeTrenutno.getName();
                    if (name.equals("metData")) {
                        postajeTrenutnoList = new Postaje();
                    } else if (postajeTrenutnoList != null) {
                        if (name.equals("domain_lat")) {
                            postajeTrenutnoList.setLat(Double.valueOf(parserPostajeTrenutno.nextText()));
                        } else if (name.equals("domain_lon")) {
                            postajeTrenutnoList.setLon(Double.valueOf(parserPostajeTrenutno.nextText()));
                        }
                        break;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parserPostajeTrenutno.getName();
                    if (name.equalsIgnoreCase("metData") && postajeTrenutnoList != null) {
                        Postaje.add(postajeTrenutnoList);
                    }
            }
            eventType = parserPostajeTrenutno.next();
        }
    return Postaje;
    }
}
