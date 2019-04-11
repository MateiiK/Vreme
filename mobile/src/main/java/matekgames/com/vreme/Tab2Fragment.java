package matekgames.com.vreme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import matekgames.com.vreme.database.DatabaseHelper;

public class Tab2Fragment extends Fragment {

    private static final String TAG = "Tab2Fragment";

    ArrayList<Dan> dnevi2;
    GPSTracker gps;
    Postaje postaje;
    String[] najblizjaPostaja;
    boolean update = true;

    TextView[] vNaprej;
    TextView[] vNaprej2;
    ImageView[] ikone;
    TextView regija;
    int count = 0;
    MyDB mojabaza;
    Data dbHelper;
    SQLiteDatabase database;
    Dan dan;
    DatabaseHelper test;
    public final static String Postaje_Ime="Postaje"; // name of table


    public final static String Postaje_danVTednu="danVTednu";  // name of employee
    public final static String Postaje_Regija="regija";
    public final static String Postaje_Razmere="razmere";
    public final static String Postaje_minTemp="minTemp";
    public final static String Postaje_maxTemp="maxTemp";
    public final static String Postaje_Datum="datum";
    public final static String Postaje_veterSmer="datum";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.napoved_prognostika,container,false);
        postaje = new Postaje();
        najblizjaPostaja = postaje.getPostajeNapoved(getContext());

        regija = view.findViewById(R.id.kraj);

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


mojabaza = new MyDB(getContext());
//        dan = new Dan();
        napoved();
//        ShowIt();
        setRetainInstance(true);

        test = new DatabaseHelper(getContext());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setRetainInstance(true);
//        ShowIt();
        if(update) {
            napoved();
            update = false;
        }
    }


    private void ShowIt(){
        for(int i=0;i<4;i++) {
            vNaprej[i].setText(dan.getDanVTednu()+"\n"+dan.getRazmere());
            vNaprej2[i].setText(dan.getMinTemp()+" / " +dan.getMaxTemp());
//                vNaprej[i].setText(mojabaza.selectRecords().getString(0) + "\n" + mojabaza.selectRecords().getString(1));
//                vNaprej2[i].setText(mojabaza.selectRecords().getString(2) + " / " + mojabaza.selectRecords().getString(3));
//            Log.e("Baza", mojabaza.selectRecords().getString(0));
//                mojabaza.selectRecords().close();
            }
        }




    public class MyDB{



        public final static String Postaje_Ime="Postaje"; // name of table

        /**
         *
         * @param context
         */
        public MyDB(Context context){
            dbHelper = new Data(context);
            database = dbHelper.getWritableDatabase();
        }

        public long createRecords(String datum, String razmere, String minTemp, String maxTemp){
            ContentValues values = new ContentValues();
            values.put(Postaje_Datum, datum);
            values.put(Postaje_Razmere, razmere);
            values.put(Postaje_minTemp, minTemp);
            values.put(Postaje_maxTemp, maxTemp);
            return database.insert(Postaje_Ime, null, values);
        }


        public Cursor selectRecords() {
            String[] cols = new String[] {Postaje_Datum, Postaje_Razmere, Postaje_minTemp, Postaje_maxTemp};
            Cursor mCursor = database.query(Postaje_Ime,cols,null
                    , null, null, null, null,null);
            if(mCursor != null && mCursor.getCount() > 0){
                mCursor.moveToFirst();
            }

            return mCursor; // iterate to get each value.
        }

    }


    private void napoved() {

        //          4 DNEVNA NAPOVED-->

        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            final XmlPullParser parserVnaprej = pullParserFactory.newPullParser();
            final URL urlVnaprej = new URL(najblizjaPostaja[0]);
            parserVnaprej.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

            Thread threadVnaprej = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {

                        final InputStream insVnaprej = urlVnaprej.openStream();
                        parserVnaprej.setInput(insVnaprej, null);
                        dnevi2 = parseVnaprej(parserVnaprej);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                for (Dan dan : dnevi2) {
                                    regija.setText(dan.getRegija().substring(0,1).toUpperCase()+dan.getRegija().substring(1));
                                    vNaprej[count].setText(dan.getDanVTednu()+"\n"+dan.getRazmere());
                                    vNaprej2[count].setText(dan.getMinTemp()+ getString(R.string.celzija)+" / "+dan.getMaxTemp()+ getString(R.string.celzija));
//                                    test.insertNote(dan.getDatum());
//                                    db.insertNote(dan.getDanVTednu());
//                                    mojabaza.createRecords(dan.getDatum(),dan.getRazmere(),dan.getMinTemp(),dan.getMaxTemp());
                                    count++;
                                }count = 0;
                            }
                        });

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

        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //          4 DNEVNA NAPOVED-->


    public ArrayList<Dan> parseVnaprej(XmlPullParser parserVnaprej) throws XmlPullParserException,IOException
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
                        if (name.equals("domain_longTitle")) {
                            dnevi2.setRegija(parserVnaprej.nextText());
                        }else if (name.equals("valid_day")) {
                            dnevi2.setDanVTednu(CETStran(parserVnaprej.nextText()));
                        }else if (name.equals("tn")) {
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
                        if (Dan != null) {
                            Dan.add(dnevi2);
                        }
                    }
            }
            eventType = parserVnaprej.next();
        }
        return Dan;
    }

    private static String CETStran(String str) {
        return str.substring(0, str.length() - 4);
    }
    private int Ikona(String ikona) {
        int resId;
        switch (ikona) {
            case "":
                return 0;
            case "jasno":
                resId = getResources().getIdentifier("ic_jasno", "drawable", getActivity().getPackageName());
                return resId;
            case "delno oblačno":
                resId = getResources().getIdentifier("ic_delno_oblacno", "drawable", getActivity().getPackageName());
                return resId;
            case "pretežno oblačno":
            case "zmerno oblačno":
                resId = getResources().getIdentifier("ic_zmerno_oblacno", "drawable", getActivity().getPackageName());
                return resId;
            case "oblačno":
                resId = getResources().getIdentifier("ic_oblacno", "drawable", getActivity().getPackageName());
                return resId;
            case "pretežno jasno":
                resId = getResources().getIdentifier("ic_pretezno_jasno","drawable",getActivity().getPackageName());
                return resId;
            default:
                resId = getResources().getIdentifier("ic_pretezno_jasno_noc", "drawable", getActivity().getPackageName());
                return resId;
        }
    }

}
