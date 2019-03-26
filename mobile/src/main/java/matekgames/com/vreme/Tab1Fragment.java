package matekgames.com.vreme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class Tab1Fragment extends Fragment {
    TextView datum;
    TextView regija;
    TextView temp;
    TextView stanjeOb;
    TextView vlaga;
    TextView veter;
    TextView tlak;
    ImageView trenutno_ikona;
    TextView[] dneviVreme;
    int count = 0;
//    Dan dnevi;
//    Dan trenutnoVreme;
//
//    ArrayList<Dan> dan;
    ArrayList<Dan> trenutnoVreme;

    ImageView dan1_ikona;
    ImageView dan2_ikona;
    Postaje postaje;
    String[] najblizjaPostaja;
//    Napoved napoved;

    private static final String TAG = "Tab1Fragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.vreme_trenutno, container, false);

        regija = view.findViewById(R.id.regija);
        datum = view.findViewById(R.id.datum);
        stanjeOb = view.findViewById(R.id.stanjeOb);
        temp = view.findViewById(R.id.temp);
        trenutno_ikona = view.findViewById(R.id.trenutno_ikona);
        vlaga = view.findViewById(R.id.vlaga);
        veter = view.findViewById(R.id.veter);
        tlak = view.findViewById(R.id.veterSunek);

        dneviVreme = new TextView[4];
        dneviVreme[0] = view.findViewById(R.id.dan1);
        dneviVreme[1] = view.findViewById(R.id.dan12);
        dneviVreme[2] = view.findViewById(R.id.dan2);
        dneviVreme[3] = view.findViewById(R.id.dan22);

//        dneviVreme[2] = view.findViewById(R.id.dan1_ikona);

        dan1_ikona = view.findViewById(R.id.dan1_ikona);
        dan2_ikona = view.findViewById(R.id.dan2_ikona);
        postaje = new Postaje();
        najblizjaPostaja = postaje.getPostajeTrenutno(getContext());
//        napoved = new Napoved(getContext());

            myParser();

        return view;
    }

//    private void DisplayIt(){
//
//
//
//        String text;
//        String text2;
//
//        text = dnevi.get(0).getDelDneva() + "\n"+ dan.get(0).getRazmere();
//        text2 = dan.get(0).getTemp();
//        dneviVreme[0].setText(text);
//        dneviVreme[1].setText(text2);
//        dan1_ikona.setImageResource(dan.get(0).getIcon());
//        text = dan.get(1).getDelDneva() + "\n" + dan.get(1).getRazmere();
//        text2 = dan.get(1).getTemp();
//        dneviVreme[2].setText(text);
//        dneviVreme[3].setText(text2);
//        dan2_ikona.setImageResource(dan.get(1).getIcon());
//    }


    private void myParser(){


        XmlPullParserFactory pullParserFactory;
        try {

            pullParserFactory = XmlPullParserFactory.newInstance();
            final XmlPullParser parserZjutrajPopoldne = pullParserFactory.newPullParser();
            final URL urlVremeZjuPop = new  URL(najblizjaPostaja[1]);
            parserZjutrajPopoldne.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);


////          ZJUTRAJ / POPOLDNE NAPOVED-->



            Thread threadZjutrajPopoldne = new Thread(new Runnable(){
                @Override
                public void run(){

                    try {

                        final InputStream insZjuPop = urlVremeZjuPop.openStream();
                        parserZjutrajPopoldne.setInput(insZjuPop, null);
                        final ArrayList<Dan> dnevi = parseZjutrajPopoldne(parserZjutrajPopoldne);
                        Log.e("URL", String.valueOf(urlVremeZjuPop));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                    String text;
                                    String text2;

                                    text = dnevi.get(0).getDelDneva() + "\n"
                                            + dnevi.get(0).getRazmere();
                                    text2 = dnevi.get(0).getTemp() + getString(R.string.celzija);
                                    dneviVreme[0].setText(text);
                                    dneviVreme[1].setText(text2);
                                    dan1_ikona.setImageResource(dnevi.get(0).getIcon());
                                text = dnevi.get(1).getDelDneva() + "\n"
                                        + dnevi.get(1).getRazmere();
                                text2 = dnevi.get(1).getTemp() + getString(R.string.celzija);
                                dneviVreme[2].setText(text);
                                dneviVreme[3].setText(text2);
                                dan2_ikona.setImageResource(dnevi.get(1).getIcon());


                            }

                        });
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

            //          TRENUTNA NAPOVED-->


            vremeTrenutno = XmlPullParserFactory.newInstance();
            final XmlPullParser parserVremeTrenutno = vremeTrenutno.newPullParser();
            final URL urlVremeTrenutno = new URL(najblizjaPostaja[0]);
            parserVremeTrenutno.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

            Thread threadVremeTrenutno = new Thread(new Runnable(){
                @Override
                public void run(){

                    try {

                       final InputStream trenutno = urlVremeTrenutno.openStream();
                        parserVremeTrenutno.setInput(trenutno, null);
                        final ArrayList<Dan> trenutnoVreme = parseVremeTrenutno(parserVremeTrenutno);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                regija.setText(trenutnoVreme.get(0).getRegija());
                                datum.setText(trenutnoVreme.get(0).getDanVTednu() + ", " + trenutnoVreme.get(0).getDatum());
                                temp.setText(trenutnoVreme.get(0).getTemp());
                                stanjeOb.setText(trenutnoVreme.get(0).getStanjeOb());
                                trenutno_ikona.setImageResource(trenutnoVreme.get(0).getIcon());
                                vlaga.setText("Vlažnost: "+trenutnoVreme.get(0).getVlaga()+ "%");
                                veter.setText("Veter: "+trenutnoVreme.get(0).getVeter()+"m/s");
                                tlak.setText("Tlak: "+trenutnoVreme.get(0).getTlak()+"hPa");
                            }
                        });
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
                            dnevi.setDanVTednu(CETStran(parserZjutrajPopoldne.nextText()));
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

    //          TRENUTNA NAPOVED-->

     XmlPullParserFactory vremeTrenutno;

    private ArrayList<Dan> parseVremeTrenutno(XmlPullParser parserVremeTrenutno) throws XmlPullParserException,IOException
    {
        ArrayList<Dan> Dan = null;
        int eventType = parserVremeTrenutno.getEventType();
        Dan trenutnoVreme = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            String name;
            String text;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                Dan = new ArrayList();
                break;
                case XmlPullParser.START_TAG:
                    name = parserVremeTrenutno.getName();
                    if (name.equals("metData")) {
                        trenutnoVreme = new Dan();
                    }else if(trenutnoVreme != null) {
                        if (name.equals("domain_shortTitle")) {
                            trenutnoVreme.setRegija(parserVremeTrenutno.nextText());
                        } else if (name.equals("tsValid_issued_day")) {
                            trenutnoVreme.setDanVTednu(CETStran(parserVremeTrenutno.nextText()));
                        } else if (name.equals("t")) {
                            trenutnoVreme.setTemp(parserVremeTrenutno.nextText() + getString(R.string.celzija));
                        } else if (name.equals("tsValid_issued")) {
                            text = parserVremeTrenutno.nextText();
                            trenutnoVreme.setStanjeOb("Stanje ob: " + Ura(text));
                            trenutnoVreme.setDatum(Datum(text));
                        } else if (name.equals("nn_shortText")) {
                            trenutnoVreme.setIcon(Ikona(parserVremeTrenutno.nextText()));
                        } else if (name.equals("rh")) {
                            trenutnoVreme.setVlaga(parserVremeTrenutno.nextText());
                        } else if (name.equals("ff_val")) {
                            trenutnoVreme.setVeter(parserVremeTrenutno.nextText());
                        } else if (name.equals("msl")) {
                            trenutnoVreme.setTlak(parserVremeTrenutno.nextText());
                        }
                    }
                        break;
                case XmlPullParser.END_TAG:
                    name = parserVremeTrenutno.getName();
                    if (name.equalsIgnoreCase("metData") && trenutnoVreme != null){
                        if (Dan != null) {
                            Dan.add(trenutnoVreme);
                        }
                    }
            }
            eventType = parserVremeTrenutno.next();
        }
        return Dan;
    }

    private static String CETStran(String str) {
        return str.substring(0, str.length() - 4);
    }

    private static String Ura(String ura) {
        return ura.substring(11,16);
    }

    private static String Datum(String datum) {
        return datum.substring(0,5);
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
        }else if (ikona.equals("oblačno")){
            resId = getResources().getIdentifier("ic_oblacno", "drawable", getActivity().getPackageName());
            return resId;
        }else {
            resId = getResources().getIdentifier("ic_pretezno_jasno_noc", "drawable", getActivity().getPackageName());
            return resId;
        }
        }


    }



