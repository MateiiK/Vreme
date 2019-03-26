//package matekgames.com.vreme;
//
//import android.content.Context;
//import android.util.Log;
//
//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserException;
//import org.xmlpull.v1.XmlPullParserFactory;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//import java.util.ArrayList;
//
//public class Napoved {
//
//    ArrayList<Dan> dnevi;
//    Postaje postaje;
//    String[] najblizjaPostaja;
//    static Context mContext;
//
//
//
//
//    public Napoved(Context context) {
//        this.mContext = context;
//
//    }
//
//
//
//    public myParser(){
//        postaje = new Postaje();
//        najblizjaPostaja = postaje.getPostajeTrenutno(mContext);
//
//        XmlPullParserFactory pullParserFactory;
//        try {
//
//            pullParserFactory = XmlPullParserFactory.newInstance();
//            final XmlPullParser parserZjutrajPopoldne = pullParserFactory.newPullParser();
//            final URL urlVremeZjuPop = new  URL(najblizjaPostaja[1]);
//            parserZjutrajPopoldne.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
//
//
////          ZJUTRAJ / POPOLDNE NAPOVED-->
//
//            Thread threadZjutrajPopoldne = new Thread(new Runnable(){
//                @Override
//                public void run(){
//
//                    try {
//
//                        final InputStream insZjuPop = urlVremeZjuPop.openStream();
//                        parserZjutrajPopoldne.setInput(insZjuPop, null);
//                        ArrayList<Dan> dnevi = parseZjutrajPopoldne(parserZjutrajPopoldne);
//                        Log.e("URL", String.valueOf(urlVremeZjuPop));
//
//                        insZjuPop.close();
//
//                    } catch (XmlPullParserException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            });
//            threadZjutrajPopoldne.start();
//            threadZjutrajPopoldne.join();
//
//            //          TRENUTNA NAPOVED-->
//
////            vremeTrenutno = XmlPullParserFactory.newInstance();
////            final XmlPullParser parserVremeTrenutno = vremeTrenutno.newPullParser();
////            final URL urlVremeTrenutno = new URL(najblizjaPostaja[0]);
////            parserVremeTrenutno.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
////
////            Thread threadVremeTrenutno = new Thread(new Runnable(){
////                @Override
////                public void run(){
////
////                    try {
////
////                        final InputStream trenutno = urlVremeTrenutno.openStream();
////                        parserVremeTrenutno.setInput(trenutno, null);
////                        final ArrayList<Dan> trenutnoVreme = parseVremeTrenutno(parserVremeTrenutno);
////
////                        trenutno.close();
////
////                    } catch (XmlPullParserException e) {
////                        e.printStackTrace();
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                }
////            });
////            threadVremeTrenutno.start();
////            threadVremeTrenutno.join();
////
//        } catch (XmlPullParserException e) {
//
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return dnevi;
//    }
//
//    //          ZJUTRAJ / POPOLDNE NAPOVED-->
//
//    private ArrayList<Dan> parseZjutrajPopoldne(XmlPullParser parserZjutrajPopoldne) throws XmlPullParserException,IOException
//    {
//        ArrayList<Dan> Dan = null;
//        int eventType = parserZjutrajPopoldne.getEventType();
//        Dan dnevi = null;
//        while (eventType != XmlPullParser.END_DOCUMENT){
//            String name;
//            switch (eventType){
//                case XmlPullParser.START_DOCUMENT:
//                    Dan = new ArrayList();
//                    break;
//                case XmlPullParser.START_TAG:
//                    name = parserZjutrajPopoldne.getName();
//                    if (name.equals("metData")) {
//                        dnevi = new Dan();
//                    } else if (dnevi != null) {
//                        if (name.equals("valid_day")) {
//                            dnevi.setDanVTednu(CETStran(parserZjutrajPopoldne.nextText() + Napoved.mContext.getResources().getString(R.string.celzija)));
//                        } else if (name.equals("t")) {
//                            dnevi.setTemp(parserZjutrajPopoldne.nextText());
//                        }else if (name.equals("nn_shortText")) {
//                            String text=parserZjutrajPopoldne.nextText();
//                            dnevi.setRazmere(text);
//                            dnevi.setIcon(Ikona(text));
//                        }else if (name.equals("valid_daypart")) {
//                            dnevi.setDelDneva(parserZjutrajPopoldne.nextText());
//                        }
//                    }
//                    break;
//                case XmlPullParser.END_TAG:
//                    name = parserZjutrajPopoldne.getName();
//                    if (name.equalsIgnoreCase("metData") && dnevi != null){
//                        Dan.add(dnevi);
//                    }
//            }
//            eventType = parserZjutrajPopoldne.next();
//        }
//        return Dan;
//    }
//
//    //          TRENUTNA NAPOVED-->
//
//    XmlPullParserFactory vremeTrenutno;
//
//    private ArrayList<Dan> parseVremeTrenutno(XmlPullParser parserVremeTrenutno) throws XmlPullParserException,IOException
//    {
//        ArrayList<Dan> Dan = null;
//        int eventType = parserVremeTrenutno.getEventType();
//        Dan trenutnoVreme = null;
//        while (eventType != XmlPullParser.END_DOCUMENT){
//            String name;
//            String text;
//            switch (eventType){
//                case XmlPullParser.START_DOCUMENT:
//                    Dan = new ArrayList();
//                    break;
//                case XmlPullParser.START_TAG:
//                    name = parserVremeTrenutno.getName();
//                    if (name.equals("metData")) {
//                        trenutnoVreme = new Dan();
//                    }else if(trenutnoVreme != null) {
//                        if (name.equals("domain_shortTitle")) {
//                            trenutnoVreme.setRegija(parserVremeTrenutno.nextText());
//                        } else if (name.equals("tsValid_issued_day")) {
//                            trenutnoVreme.setDanVTednu(CETStran(parserVremeTrenutno.nextText()));
//                        } else if (name.equals("t")) {
//                            trenutnoVreme.setTemp(parserVremeTrenutno.nextText() + Napoved.mContext.getResources().getString(R.string.celzija));
//                        } else if (name.equals("tsValid_issued")) {
//                            text = parserVremeTrenutno.nextText();
//                            trenutnoVreme.setStanjeOb("Stanje ob: " + Ura(text));
//                            trenutnoVreme.setDatum(Datum(text));
//                        } else if (name.equals("nn_shortText")) {
//                            trenutnoVreme.setIcon(Ikona(parserVremeTrenutno.nextText()));
//                        } else if (name.equals("rh")) {
//                            trenutnoVreme.setVlaga(parserVremeTrenutno.nextText());
//                        } else if (name.equals("ff_val")) {
//                            trenutnoVreme.setVeter(parserVremeTrenutno.nextText());
//                        } else if (name.equals("msl")) {
//                            trenutnoVreme.setTlak(parserVremeTrenutno.nextText());
//                        }
//                    }
//                    break;
//                case XmlPullParser.END_TAG:
//                    name = parserVremeTrenutno.getName();
//                    if (name.equalsIgnoreCase("metData") && trenutnoVreme != null){
//                        if (Dan != null) {
//                            Dan.add(trenutnoVreme);
//                        }
//                    }
//            }
//            eventType = parserVremeTrenutno.next();
//        }
//        return Dan;
//    }
//
//    private static String CETStran(String str) {
//        return str.substring(0, str.length() - 4);
//    }
//
//    private static String Ura(String ura) {
//        return ura.substring(11,16);
//    }
//
//    private static String Datum(String datum) {
//        return datum.substring(0,5);
//    }
//
//    private int Ikona(String ikona) {
//        int resId;
//        if (ikona.equals("jasno")) {
//            resId =  Napoved.mContext.getResources().getIdentifier("ic_jasno", "drawable", Napoved.mContext.getPackageName());
//            return resId;
//        } else if (ikona.equals("delno oblačno")){
//            resId =  Napoved.mContext.getResources().getIdentifier("ic_delno_oblacno", "drawable", Napoved.mContext.getPackageName());
//            return resId;
//        }else if (ikona.equals("pretežno oblačno") || (ikona.equals("zmerno oblačno"))){
//            resId =  Napoved.mContext.getResources().getIdentifier("ic_zmerno_oblacno", "drawable", Napoved.mContext.getPackageName());
//            return resId;
//        }else if (ikona.equals("oblačno")){
//            resId =  Napoved.mContext.getResources().getIdentifier("ic_oblacno", "drawable", Napoved.mContext.getPackageName());
//            return resId;
//        }else {
//            resId =  Napoved.mContext.getResources().getIdentifier("ic_pretezno_jasno_noc", "drawable",  Napoved.mContext.getPackageName());
//            return resId;
//        }
//    }
//}
