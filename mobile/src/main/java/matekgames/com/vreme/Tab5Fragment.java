package matekgames.com.vreme;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Tab5Fragment extends Fragment {
    TextView tvAddress;
    Button btnShowLocation;
    GPSTracker gps;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab5, container, false);
        tvAddress = view.findViewById(R.id.tvAddress);
        btnShowLocation = view.findViewById(R.id.btnShowLocation);
        // Show location button click event


        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Create class object
                gps = new GPSTracker(getActivity());

                // Check if GPS enabled
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    GPSTracker.LocationAddress locationAddress = new GPSTracker.LocationAddress();
                    locationAddress.getAddressFromLocation(latitude, longitude,
                            getContext(), new GeocoderHandler());
                    // \n is for new line
                    Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    String result = "Latitude: " + latitude + " Longitude: " + longitude;
                    tvAddress.setText(result);
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    // Setting DialogHelp Title
                    alertDialog.setTitle("GPS is settings");
                    // Setting DialogHelp Message
                    alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
                    // On pressing Settings button
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            getActivity().startActivity(intent);
                        }
                    });
            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
            }
        });




        return view;
    }

    class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            tvAddress.setText(locationAddress);
        }
    }
}
