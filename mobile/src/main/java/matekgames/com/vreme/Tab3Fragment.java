package matekgames.com.vreme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;



public class Tab3Fragment extends Fragment {

    TextView[] dneviVNaprej;
    TextView[] dnevi2;
    ImageView[] ikone;
    Postaje postaje;
    String[] najblizjaPostaja;
    Integer count =0;
    Spinner spin;

    String[] bankNames={"Bela krajina","Bovška","Dolenjska","Gorenjska","Goriška","Kočevska","Koroška","Ljubljana in okolica","Notranjska","Obala","Podravje","Pomurje","Savinjska","Spodnje Posavje","Zgornjesavska"};

    private static final String TAG = "Tab3Fragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.modelska_napoved, container, false);


        dneviVNaprej = new TextView[6];
        dneviVNaprej[0] = view.findViewById(R.id.danes);
        dneviVNaprej[1] = view.findViewById(R.id.jutri);
        dneviVNaprej[2] = view.findViewById(R.id.cez2dni);
        dneviVNaprej[3] = view.findViewById(R.id.cez3dni);
        dneviVNaprej[4] = view.findViewById(R.id.cez4dni);
        dneviVNaprej[5] = view.findViewById(R.id.cez5dni);

        dnevi2 = new TextView[6];
        dnevi2[0] = view.findViewById(R.id.danes2);
        dnevi2[1] = view.findViewById(R.id.jutri2);
        dnevi2[2] = view.findViewById(R.id.cez2dni2);
        dnevi2[3] = view.findViewById(R.id.cez3dni2);
        dnevi2[4] = view.findViewById(R.id.cez4dni2);
        dnevi2[5] = view.findViewById(R.id.cez5dni2);

        ikone = new ImageView[6];
        ikone[0] = view.findViewById(R.id.danes_ikona);
        ikone[1] = view.findViewById(R.id.jutri_ikona);
        ikone[2] = view.findViewById(R.id.cez2dni_ikona);
        ikone[3] = view.findViewById(R.id.cez3dni_ikona);
        ikone[4] = view.findViewById(R.id.cez4dni_ikona);
        ikone[5] = view.findViewById(R.id.cez5dni_ikona);

        spin = view.findViewById(R.id.spinner);

        postaje = new Postaje();
        najblizjaPostaja = postaje.getPostajeNapoved(getContext());

        ShowIt();

//        spin.setOnItemSelectedListener();

        ArrayAdapter aa = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_item,bankNames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);



//        Performing action onItemSelected and onNothing selected
//        @Override
//        public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
//            Toast.makeText(getContext(), bankNames[position], Toast.LENGTH_LONG).show();
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> arg0) {
//// TODO Auto-generated method stub
//
//        }

        return view;
    }

    public void ShowIt(){


        XmlPullParserFactory pullParserFactory;
        try {

            pullParserFactory = XmlPullParserFactory.newInstance();
            final XmlPullParser parserVremeNapoved = pullParserFactory.newPullParser();
            final URL urlVremeZjuPop = new  URL(najblizjaPostaja[1]);
            parserVremeNapoved.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);


//          ZJUTRAJ / POPOLDNE NAPOVED-->



            Thread threadZjutrajPopoldne = new Thread(new Runnable(){
                @Override
                public void run(){

                    try {

                        final InputStream insZjuPop = urlVremeZjuPop.openStream();
                        parserVremeNapoved.setInput(insZjuPop, null);
                        final ArrayList<Dan> dnevi = parseVremeNapoved(parserVremeNapoved);
                        Log.e("URL", String.valueOf(urlVremeZjuPop));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                for (Dan dan : dnevi) {
                                    String text;
                                    String text2;

                                    text = dan.getDanVTednu() + "\n"
                                            + dan.getRazmere();
                                    text2 = dan.getMinTemp() + getString(R.string.celzija) + " / " + dan.getMaxTemp() + getString(R.string.celzija);
                                    dneviVNaprej[count].setText(text);
                                    dnevi2[count].setText(text2);
                                    ikone[count].setImageResource(dan.getIcon());
                                    count++;
                                } count=0;
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

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private ArrayList<Dan> parseVremeNapoved(XmlPullParser parserVremeNapoved) throws XmlPullParserException,IOException
    {
        ArrayList<Dan> Dan = null;
        int eventType = parserVremeNapoved.getEventType();
        Dan trenutnoNapoved = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            String name;
            String text;
            String text2;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    Dan = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parserVremeNapoved.getName();
                    if (name.equals("metData")) {
                        trenutnoNapoved = new Dan();
                    }else if(trenutnoNapoved != null) {
                        if (name.equals("valid_day")) {
                            trenutnoNapoved.setDanVTednu(CETStran(parserVremeNapoved.nextText()));
                        } else if (name.equals("tnsyn")) {
                            trenutnoNapoved.setMinTemp(parserVremeNapoved.nextText());
                        }else if (name.equals("txsyn")) {
                            trenutnoNapoved.setMaxTemp(parserVremeNapoved.nextText());
                        } else if (name.equals("tsValid_issued")) {
                            text = parserVremeNapoved.nextText();
                            trenutnoNapoved.setStanjeOb("Stanje ob: " + Ura(text));
                            trenutnoNapoved.setDatum(Datum(text));
                        } else if (name.equals("nn_shortText")) {
                            text2=parserVremeNapoved.nextText();
                            trenutnoNapoved.setRazmere(text2);
                            trenutnoNapoved.setIcon(Ikona(text2));
                        } else if (name.equals("rh")) {
                            trenutnoNapoved.setVlaga(parserVremeNapoved.nextText());
                        } else if (name.equals("ff_val")) {
                            trenutnoNapoved.setVeter(parserVremeNapoved.nextText());
                        } else if (name.equals("msl")) {
                            trenutnoNapoved.setTlak(parserVremeNapoved.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parserVremeNapoved.getName();
                    if (name.equalsIgnoreCase("metData") && trenutnoNapoved != null){
                        if (Dan != null) {
                            Dan.add(trenutnoNapoved);
                        }
                    }
            }
            eventType = parserVremeNapoved.next();
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