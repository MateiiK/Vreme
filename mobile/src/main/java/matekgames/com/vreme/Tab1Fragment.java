package matekgames.com.vreme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;


public class Tab1Fragment extends Fragment {
    TextView datum;
    TextView regija;
    TextView temp;
    TextView stanjeOb;
    ImageView trenutno_ikona;
    int count = 0;
    TextView[] dneviVreme;
    TextView[] vNaprej;
    TextView[] vNaprej2;
    ImageView[] ikone;
    ImageView dan1_ikona;
    Postaje postaje;


    private static final String TAG = "Tab1Fragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab1, container, false);


        regija = view.findViewById(R.id.regija);
        datum = view.findViewById(R.id.datum);
        stanjeOb = view.findViewById(R.id.stanjeOb);
        temp = view.findViewById(R.id.temp);
        trenutno_ikona = view.findViewById(R.id.trenutno_ikona);

        dneviVreme = new TextView[3];
        dneviVreme[0] = view.findViewById(R.id.dan1);
        dneviVreme[1] = view.findViewById(R.id.dan12);
//        dneviVreme[2] = view.findViewById(R.id.dan1_ikona);

        dan1_ikona = view.findViewById(R.id.dan1_ikona);
        vNaprej = new TextView[4];
        vNaprej[0] = view.findViewById(R.id.danes);
        vNaprej[1] = view.findViewById(R.id.jutri);
        vNaprej[2] = view.findViewById(R.id.cez2dni);
        vNaprej[3] = view.findViewById(R.id.cez3dni);

        vNaprej2 = new TextView[4];
        vNaprej2[0] = view.findViewById(R.id.danes2);
        vNaprej2[1] = view.findViewById(R.id.jutri2);
        vNaprej2[2] = view.findViewById(R.id.cez2dni2);
        vNaprej2[3] = view.findViewById(R.id.cez3dni2);

        ikone = new ImageView[4];
        ikone[0] = view.findViewById(R.id.danes_ikona);
        ikone[1] = view.findViewById(R.id.jutri_ikona);
        ikone[2] = view.findViewById(R.id.cez2dni_ikona);
        ikone[3] = view.findViewById(R.id.cez3dni_ikona);



        postaje = new Postaje();

        XmlPullParserFactory pullParserFactory;

        try {
            String test = new String("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_SI_OSREDNJESLOVENSKA_latest.xml");
            pullParserFactory = XmlPullParserFactory.newInstance();
            final XmlPullParser parserZjutrajPopoldne = pullParserFactory.newPullParser();
//            final URL urlVremeZjuPop = new URL("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_SI_OSREDNJESLOVENSKA_latest.xml");
            final URL urlVremeZjuPop = new URL(test);
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
                        String text2;
                            text = dnevi.get(0).getDelDneva() + "\n"
                                    + dnevi.get(0).getRazmere();
                            text2 = dnevi.get(0).getTemp() + getString(R.string.celzija);
                        dneviVreme[0].setText(text);
                        dneviVreme[1].setText(text2);
                        dan1_ikona.setImageResource(dnevi.get(0).getIcon());
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
                        String text2;

                        for (Dan dan:dnevi2) {

                            text=dan.getDatum()+ "\n" + dan.getRazmere();
                            text2=dan.getMinTemp()+getString(R.string.celzija) + " / " + dan.getMaxTemp()+getString(R.string.celzija);
                            vNaprej[count].setText(text);
                            vNaprej2[count].setText(text2);
                            ikone[count].setImageResource(dan.getIcon());
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
            final URL urlVremeTrenutno = new URL(postaje.getPostaje(getContext()));
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
                            String text=parserZjutrajPopoldne.nextText();
                            dnevi.setRazmere(text);
                            dnevi.setIcon(Ikona(text));
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
                            String text=parserVnaprej.nextText();
                            dnevi2.setRazmere(text);
                            dnevi2.setIcon(Ikona(text));
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
            String text;
            switch (eventType){
                case XmlPullParser.START_TAG:
                    name = parserVremeTrenutno.getName();
                        if (name.equals("domain_shortTitle")) {
                            regija.setText(parserVremeTrenutno.nextText());
                        }else if (name.equals("tsValid_issued_day")) {
                            datum.setText(CETStran(parserVremeTrenutno.nextText()));
                        }else if (name.equals("t")) {
                            temp.setText(parserVremeTrenutno.nextText()+ getString(R.string.celzija));
                        } else if (name.equals("tsValid_issued")) {
                            text = parserVremeTrenutno.nextText();
                            stanjeOb.setText("Stanje ob: "+Ura(text)+", " + Datum(text));
                        }else if ( name.equals("nn_shortText")){
                            text=parserVremeTrenutno.nextText();
                            trenutno_ikona.setImageResource(Ikona(text));
                        }
                        break;
            }
            eventType = parserVremeTrenutno.next();
        }

    }

    private static String CETStran(String str) {
        return str.substring(0, str.length() - 4);
    }

    private static String Ura(String ura) {
        return ura.substring(11,16);
    }

    private static String Datum(String datum) {
        return datum.substring(0,10);
    }

    private int Ikona(String ikona) {
        int resId;
        if (ikona.equals("jasno")) {
            resId = getResources().getIdentifier("ic_jasno", "drawable", getActivity().getPackageName());
            return resId;
        } else if (ikona.equals("delno oblačno")){
            resId = getResources().getIdentifier("ic_delno_oblacno", "drawable", getActivity().getPackageName());
            return resId;
        }else if (ikona.equals("pretežno oblačno") || (ikona.equals("zmerno oblačno"))){
            resId = getResources().getIdentifier("ic_zmerno_oblacno", "drawable", getActivity().getPackageName());
            return resId;
        }else if (ikona.equals("oblacno")){
            resId = getResources().getIdentifier("ic_oblacno", "drawable", getActivity().getPackageName());
            return resId;
        }else {
            resId = getResources().getIdentifier("ic_pretezno_jasno_noc", "drawable", getActivity().getPackageName());
            return resId;
        }
        }
    }



