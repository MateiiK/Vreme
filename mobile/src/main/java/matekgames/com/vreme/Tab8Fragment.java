package matekgames.com.vreme;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;


public class Tab8Fragment extends Fragment {
    TextView tekst;
    WebView webText;
    private static final String TAG = "Tab1Fragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.vreme_tekst, container, false);

        tekst = view.findViewById(R.id.textNapoved);
        webText = view.findViewById(R.id.textWeb);


        URL url = null;
        try {
            url = new URL("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_si_text.html");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        webText.getSettings().setLoadWithOverviewMode(true);
//        webText.getSettings().setUseWideViewPort(true);
        webText.getSettings().setBuiltInZoomControls(true);
        webText.loadUrl("http://meteo.arso.gov.si/uploads/probase/www/fproduct/text/sl/fcast_si_text.html");
//        tekst.setText(Html.fromHtml(url));
        return view;
    }



}



