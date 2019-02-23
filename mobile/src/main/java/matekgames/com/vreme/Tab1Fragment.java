package matekgames.com.vreme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;



public class Tab1Fragment extends Fragment{
TextView datum;
TextView regija;
TextView temp;
int count = 0;
TextView[] dneviVreme;
TextView[] vNaprej;

    private static final String TAG = "Tab1Fragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab1, container, false);

        regija = view.findViewById(R.id.regija);
        datum = view.findViewById(R.id.datum);
        temp = view.findViewById(R.id.temp);

        dneviVreme = new TextView[3];
        dneviVreme[0] = view.findViewById(R.id.dan1);
        dneviVreme[1] = view.findViewById(R.id.dan2);
        dneviVreme[2] = view.findViewById(R.id.dan3);

        vNaprej = new TextView[4];
        vNaprej[0] = view.findViewById(R.id.danes);
        vNaprej[1] = view.findViewById(R.id.jutri);
        vNaprej[2] = view.findViewById(R.id.cez2dni);
        vNaprej[3] = view.findViewById(R.id.cez3dni);


        XmlPullParserFactory pullParserFactory;

        try {

            pullParserFactory = XmlPullParserFactory.newInstance();
            final XmlPullParser parserZjutrajPopoldne = pullParserFactory.newPullParser();
            final URL urlVremeZjuPop = new URL("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_SI_OSREDNJESLOVENSKA_latest.xml");
         //   InputStream in_sZjuPop = Objects.requireNonNull(getActivity()).getAssets().open("vreme3.xml");
            parserZjutrajPopoldne.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);


//          ZJUTRAJ / POPOLDNE NAPOVED-->


            Thread threadZjutrajPopoldne = new Thread(new Runnable(){
                @Override
                public void run(){

                    try {

                        final InputStream insZjuPop = urlVremeZjuPop.openStream();
                        parserZjutrajPopoldne.setInput(insZjuPop, null);
                        ArrayList<Dan> dnevi = parseZjutrajPopoldne(parserZjutrajPopoldne);

                        String text;

                        for (Dan dan:dnevi) {

                            text=dan.getDatum()+ "\n" + dan.getDelDneva() + "\n"
                                    + dan.getRazmere() + "\n" + dan.getTemp()+getString(R.string.celzija) + "\n";
                            dneviVreme[count].setText(text);
                            count++;
                        }
                            count=0;
                        insZjuPop.close();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            threadZjutrajPopoldne.start();
            threadZjutrajPopoldne.join();


//          4 DNEVNA NAPOVED-->


            pullParserFactory = XmlPullParserFactory.newInstance();
            final XmlPullParser parserVnaprej = pullParserFactory.newPullParser();
            final URL urlVnaprej = new URL("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_SLOVENIA_MIDDLE_latest.xml");
            //   InputStream in_sZjuPop = Objects.requireNonNull(getActivity()).getAssets().open("vreme3.xml");
            parserVnaprej.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

            Thread threadVnaprej = new Thread(new Runnable(){
                @Override
                public void run(){

                    try {

                        final InputStream insVnaprej = urlVnaprej.openStream();
                        parserVnaprej.setInput(insVnaprej, null);
                        ArrayList<Dan> dnevi2 = parseVnaprej(parserVnaprej);

                        String text;

                        for (Dan dan:dnevi2) {

                            text=dan.getDatum()+ "\n" + dan.getMinTemp()+getString(R.string.celzija) + "\n"
                                    + dan.getMaxTemp()+getString(R.string.celzija) + "\n" + dan.getVeter() + "\n" + dan.getRazmere();
                            vNaprej[count].setText(text);
                            count++;
                        }
                        count=0;
                        insVnaprej.close();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            threadVnaprej.start();
            threadVnaprej.join();


            //          TRENUTNA NAPOVED-->


            vremeTrenutno = XmlPullParserFactory.newInstance();
            final XmlPullParser parserVremeTrenutno = vremeTrenutno.newPullParser();
            final URL urlVremeTrenutno = new URL("http://meteo.arso.gov.si/uploads/probase/www/observ/surface/text/sl/observationAms_LJUBL-ANA_BEZIGRAD_latest.xml");
            parserVremeTrenutno.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

            Thread threadVremeTrenutno = new Thread(new Runnable(){
                @Override
                public void run(){

                    try {

                       final InputStream trenutno = urlVremeTrenutno.openStream();
                        parserVremeTrenutno.setInput(trenutno, null);
                        parseVremeTrenutno(parserVremeTrenutno);
                        trenutno.close();


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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return view;
    }

    //          ZJUTRAJ / POPOLDNE NAPOVED-->

    private ArrayList<Dan> parseZjutrajPopoldne(XmlPullParser parserZjutrajPopoldne) throws XmlPullParserException,IOException
    {
        ArrayList<Dan> Dan = null;
        int eventType = parserZjutrajPopoldne.getEventType();
        Dan dnevi = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    Dan = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parserZjutrajPopoldne.getName();


                    if (name.equals("metData")) {
                        dnevi = new Dan();
                    } else if (dnevi != null) {
                        if (name.equals("valid_day")) {
                            dnevi.setDatum(CETStran(parserZjutrajPopoldne.nextText()));
                        } else if (name.equals("t")) {
                            dnevi.setTemp(parserZjutrajPopoldne.nextText());
                        }else if (name.equals("nn_shortText")) {
                            dnevi.setRazmere(parserZjutrajPopoldne.nextText());
                        }else if (name.equals("valid_daypart")) {
                            dnevi.setDelDneva(parserZjutrajPopoldne.nextText());
                        }
                    }

                    break;
                case XmlPullParser.END_TAG:
                    name = parserZjutrajPopoldne.getName();
                    if (name.equalsIgnoreCase("metData") && dnevi != null){
                        Dan.add(dnevi);
                    }
            }
            eventType = parserZjutrajPopoldne.next();
        }

        return Dan;

    }


    //          4 DNEVNA NAPOVED-->


    private ArrayList<Dan> parseVnaprej(XmlPullParser parserVnaprej) throws XmlPullParserException,IOException
    {
        ArrayList<Dan> Dan = null;
        int eventType = parserVnaprej.getEventType();
        Dan dnevi2 = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    Dan = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parserVnaprej.getName();


                    if (name.equals("metData")) {
                        dnevi2 = new Dan();
                    } else if (dnevi2 != null) {
                        if (name.equals("valid_day")) {
                            dnevi2.setDatum(CETStran(parserVnaprej.nextText()));
                        } else if (name.equals("tn")) {
                            dnevi2.setMinTemp(parserVnaprej.nextText());
                        }else if (name.equals("tx")) {
                            dnevi2.setMaxTemp(parserVnaprej.nextText());
                        }else if (name.equals("nn_shortText")) {
                            dnevi2.setRazmere(parserVnaprej.nextText());
                        }else if (name.equals("dd_longText")) {
                            dnevi2.setVeter(parserVnaprej.nextText());
                        }

                    }

                    break;
                case XmlPullParser.END_TAG:
                    name = parserVnaprej.getName();
                    if (name.equalsIgnoreCase("metData") && dnevi2 != null){
                        Dan.add(dnevi2);
                    }
            }
            eventType = parserVnaprej.next();
        }

        return Dan;

    }


    //          TRENUTNA NAPOVED-->

     XmlPullParserFactory vremeTrenutno;

    public void parseVremeTrenutno(XmlPullParser parserVremeTrenutno) throws XmlPullParserException,IOException
    {
        int eventType = parserVremeTrenutno.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name;
            switch (eventType){
                case XmlPullParser.START_TAG:
                    name = parserVremeTrenutno.getName();
                        if (name.equals("domain_shortTitle")) {
                            regija.setText(parserVremeTrenutno.nextText());
                        } else if (name.equals("tsValid_issued")) {
                            datum.setText(CETStran(parserVremeTrenutno.nextText()));
                        }else if (name.equals("t")) {
                            temp.setText(parserVremeTrenutno.nextText()+getString(R.string.celzija));
                        }
                        break;
            }
            eventType = parserVremeTrenutno.next();
        }

    }

    private static String CETStran(String str) {
        return str.substring(0, str.length() - 4);
    }
}
