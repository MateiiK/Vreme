package matekgames.com.vreme;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Postaje {


    GPSTracker gps;
    Context mContext;
    double lat;
    double lon;
    String postaja;
    String[] najblizjaPostaja;
    String[] najblizjaPostajaNapoved;
    String base = ("http://meteo.arso.gov.si/uploads/probase/www/");
    int count;
    private boolean asked = false;
    double latMin = 0;
    double lonMin = 0;
    double latMax = 0;
    double lonMax = 0;
    float[] izid = new float[1];
    double testing = 10000000;


    public String getPostaja() {
        return postaja;
    }

    public void setPostaja(String postaja) {
        this.postaja = postaja;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }


    String[] urlPostajeTrenutno = new String[]{
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

    String[] urlPostajeZjutrajPopoldne = new String[]{
            "fproduct/text/sl/fcast_SI_BELOKRANJSKA_latest.xml",
            "fproduct/text/sl/fcast_SI_BOVSKA_latest.xml",
            "fproduct/text/sl/fcast_SLOVENIA_MIDDLE_latest.xml",
            "fproduct/text/sl/fcast_SI_DOLENJSKA_latest.xml",
            "fproduct/text/sl/fcast_SI_GORENJSKA_latest.xml",
            "fproduct/text/sl/fcast_SI_GORISKA_latest.xml",
            "fproduct/text/sl/fcast_SI_KOCEVSKA_latest.xml",
            "fproduct/text/sl/fcast_SI_KOROSKA_latest.xml",
            "fproduct/text/sl/fcast_SI_OSREDNJESLOVENSKA_latest.xml",
            "fproduct/text/sl/fcast_SI_NOTRANJSKO-KRASKA_latest.xml",
            "fproduct/text/sl/fcast_SI_OBALNO-KRASKA_latest.xml",
            "fproduct/text/sl/fcast_SI_PODRAVSKA_latest.xml",
            "fproduct/text/sl/fcast_SI_POMURSKA_latest.xml",
            "fproduct/text/sl/fcast_SI_SAVINJSKA_latest.xml",
            "fproduct/text/sl/fcast_SI_SPODNJEPOSAVSKA_latest.xml",
            "fproduct/text/sl/fcast_SI_ZGORNJESAVSKA_latest.xml"
    };

    String[] urlPostajeTriDni = new String[]{
            "fproduct/text/sl/fcast_SLOVENIA_SOUTH-EAST_latest.xml",
            "fproduct/text/sl/fcast_SLOVENIA_SOUTH-WEST_latest.xml",
            "fproduct/text/sl/fcast_SLOVENIA_MIDDLE_latest.xml",
            "fproduct/text/sl/fcast_SLOVENIA_NORTH-EAST_latest.xml",
            "fproduct/text/sl/fcast_SLOVENIA_NORTH-WEST_latest.xml"
    };

    String[] urlModelskaNapoved = new String[]{
            "fproduct/text/sl/forecast_SI_BELOKRANJSKA_latest.xml",
            "fproduct/text/sl/forecast_SI_BOVSKA_latest.xml",
            "fproduct/text/sl/forecast_SI_DOLENJSKA_latest.xml",
            "fproduct/text/sl/forecast_SI_GORENJSKA_latest.xml",
            "fproduct/text/sl/forecast_SI_GORISKA_latest.xml",
            "fproduct/text/sl/forecast_SI_KOCEVSKA_latest.xml",
            "fproduct/text/sl/forecast_SI_KOROSKA_latest.xml",
            "fproduct/text/sl/forecast_SI_OSREDNJESLOVENSKA_latest.xml",
            "fproduct/text/sl/forecast_SI_NOTRANJSKO-KRASKA_latest.xml",
            "fproduct/text/sl/forecast_SI_OBALNO-KRASKA_latest.xml",
            "fproduct/text/sl/forecast_SI_PODRAVSKA_latest.xml",
            "fproduct/text/sl/forecast_SI_POMURSKA_latest.xml",
            "fproduct/text/sl/forecast_SI_SAVINJSKA_latest.xml",
            "fproduct/text/sl/forecast_SI_SPODNJEPOSAVSKA_latest.xml",
            "fproduct/text/sl/forecast_SI_ZGORNJESAVSKA_latest.xml"
    };


    XmlPullParserFactory postajePullParserFactory;

    public String[] getPostajeTrenutno(Context context) {

        gps = new GPSTracker(context);

        najblizjaPostaja = new String[5];
        try {
            postajePullParserFactory = XmlPullParserFactory.newInstance();
            final XmlPullParser parserPostajeTrenutno = postajePullParserFactory.newPullParser();

            parserPostajeTrenutno.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            Thread threadVremeTrenutno = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (count = 0; count < urlPostajeTrenutno.length; count++) {
                            final URL postajeTrenutnoURL = new URL(base + urlPostajeTrenutno[count]);
                            final InputStream trenutno = postajeTrenutnoURL.openStream();
                            parserPostajeTrenutno.setInput(trenutno, null);
                            ArrayList<Postaje> postajeTrenutnoList = parsePostajaTrenutno(parserPostajeTrenutno);


                            for (Postaje postaje : postajeTrenutnoList) {
                                Location.distanceBetween(gps.getLatitude(), gps.getLongitude(), postaje.getLat(), postaje.getLon(), izid);
                                if (testing > izid[0]) {
                                    testing = izid[0];
                                    najblizjaPostaja[0] = base + urlPostajeTrenutno[count];

                                }
                            }
                            trenutno.close();
                        }
                        testing = 10000000;

                        for (count = 0; count < urlPostajeZjutrajPopoldne.length; count++) {
                            final URL postajeZjutrajPopoldneURL = new URL(base + urlPostajeZjutrajPopoldne[count]);
                            final InputStream trenutno = postajeZjutrajPopoldneURL.openStream();
                            parserPostajeTrenutno.setInput(trenutno, null);
                            ArrayList<Postaje> postajeTrenutnoList = parsePostajaTrenutno(parserPostajeTrenutno);


                            for (Postaje postaje : postajeTrenutnoList) {
                                Location.distanceBetween(gps.getLatitude(), gps.getLongitude(), postaje.getLat(), postaje.getLon(), izid);
                                if (testing > izid[0]) {
                                    testing = izid[0];
                                    najblizjaPostaja[1] = base + urlPostajeZjutrajPopoldne[count];
                                    Log.e("Postaje...",najblizjaPostaja[1]);
                                }
                            }
                            trenutno.close();
                        }
                        testing = 10000000;

                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            threadVremeTrenutno.start();
            threadVremeTrenutno.join();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return najblizjaPostaja;
    }



    public String[] getPostajeNapoved(Context context) {

        gps = new GPSTracker(context);
        if(!asked) {
            gps.canGetLocation();
            asked = true;
        }
        najblizjaPostajaNapoved = new String[5];
        try {
            postajePullParserFactory = XmlPullParserFactory.newInstance();
            final XmlPullParser parserPostajeTrenutno = postajePullParserFactory.newPullParser();

            parserPostajeTrenutno.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            Thread threadVremeNapoved = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        for (count = 0; count < urlPostajeTriDni.length; count++) {
                            final URL postajeTrenutnoURL = new URL(base + urlPostajeTriDni[count]);
                            final InputStream trenutno = postajeTrenutnoURL.openStream();
                            parserPostajeTrenutno.setInput(trenutno, null);
                            ArrayList<Postaje> postajeTrenutnoList = parsePostajaTrenutno(parserPostajeTrenutno);


                            for (Postaje postaje : postajeTrenutnoList) {
                                Location.distanceBetween(gps.getLatitude(), gps.getLongitude(), postaje.getLat(), postaje.getLon(), izid);
                                if (testing > izid[0]) {
                                    testing = izid[0];

                                    najblizjaPostajaNapoved[0] = base + urlPostajeTriDni[count];
                                }
                            }
                            trenutno.close();
                        }
                        testing = 10000000;

                        for (count = 0; count < urlModelskaNapoved.length; count++) {
                            final URL postajeTrenutnoURL = new URL(base + urlModelskaNapoved[count]);
                            final InputStream trenutno = postajeTrenutnoURL.openStream();
                            parserPostajeTrenutno.setInput(trenutno, null);
                            ArrayList<Postaje> postajeTrenutnoList = parsePostajaTrenutno(parserPostajeTrenutno);


                            for (Postaje postaje : postajeTrenutnoList) {
                                Location.distanceBetween(gps.getLatitude(), gps.getLongitude(), postaje.getLat(), postaje.getLon(), izid);
                                if (testing > izid[0]) {
                                    testing = izid[0];

                                    najblizjaPostajaNapoved[1] = base + urlModelskaNapoved[count];
                                }
                            }
                            trenutno.close();
                        }
                        testing = 10000000;

                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            threadVremeNapoved.start();
            threadVremeNapoved.join();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return najblizjaPostajaNapoved;
    }

            private ArrayList<Postaje> parsePostajaTrenutno (XmlPullParser parserPostajeTrenutno) throws
            XmlPullParserException, IOException {

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


