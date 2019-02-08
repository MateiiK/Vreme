package matekgames.com.vreme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import java.util.Objects;


public class Tab1Fragment extends Fragment{
TextView datum;
TextView regija;
TextView temp;
int count = 0;
TextView[] dneviVreme;
String reg = "";
String dat = "";
String tem = "";


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


        XmlPullParserFactory pullParserFactory;

        try {



            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();


            InputStream in_sZjuPop = Objects.requireNonNull(getActivity()).getAssets().open("vreme3.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

            parser.setInput(in_sZjuPop, null);

            ArrayList<Dan> dnevi = parseXML(parser);
            String text;

            for (Dan dan:dnevi) {

                text=dan.getDatum()+ "\n" + dan.getDelDneva() + "\n"
                        + dan.getRazmere() + "\n" + dan.getTemp()+getString(R.string.celzija) + "\n";
                dneviVreme[count].setText(text);
                count++;


            }

            vremeTrenutno = XmlPullParserFactory.newInstance();
           final XmlPullParser parser2 = vremeTrenutno.newPullParser();


          final URL url = new URL("http://meteo.arso.gov.si/uploads/probase/www/observ/surface/text/sl/observationAms_LJUBL-ANA_BEZIGRAD_latest.xml");

//            final InputStream trenutno = Objects.requireNonNull(getActivity()).getAssets().open("vreme.xml");
            parser2.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);


            Thread thread = new Thread(new Runnable(){
                @Override
                public void run(){

                    try {

                       final InputStream trenutno = url.openStream();
                        parser2.setInput(trenutno, null);
                        parseXML2(parser2);


                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            thread.join();
            regija.setText(reg);
            datum.setText(dat);
            temp.setText(tem+getString(R.string.celzija));

        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return view;
    }
    private ArrayList<Dan> parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        ArrayList<Dan> Dan = null;
        int eventType = parser.getEventType();
        Dan dnevi = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    Dan = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();


                    if (name.equals("metData")) {
                        dnevi = new Dan();
                    } else if (dnevi != null) {
                        if (name.equals("valid_day")) {
                            dnevi.setDatum(parser.nextText());
                        } else if (name.equals("t")) {
                            dnevi.setTemp(parser.nextText());
                        }else if (name.equals("nn_shortText")) {
                            dnevi.setRazmere(parser.nextText());
                        }else if (name.equals("valid_daypart")) {
                            dnevi.setDelDneva(parser.nextText());
                        }
                    }

                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("metData") && dnevi != null){
                        Dan.add(dnevi);
                    }
            }
            eventType = parser.next();
        }

        return Dan;

    }

     XmlPullParserFactory vremeTrenutno;

    public void parseXML2(XmlPullParser parser2) throws XmlPullParserException,IOException
    {
        int eventType = parser2.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name;
            switch (eventType){
                case XmlPullParser.START_TAG:
                    name = parser2.getName();
                        if (name.equals("domain_shortTitle")) {
                            reg = parser2.nextText();
                        } else if (name.equals("tsValid_issued")) {
                            dat = parser2.nextText();
                        }else if (name.equals("t")) {
                            tem = parser2.nextText();
                        }
                        break;
            }
            eventType = parser2.next();
        }

    }

}
