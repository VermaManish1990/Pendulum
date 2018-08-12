package com.pend.arena.view;


import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;
import com.pend.R;


import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class FilterDialogFragment extends DialogFragment {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private String TAG="FilterDialogFragment";
    private EditText txtLocation;
    private Button searchButton;
    private RadioGroup radioGroup;
    private RangeSeekBar ageSeekBar,distanceSeekBar;
    private TextView ageTextView,distanceTextView;
    private RadioButton radioButton;
    private  int leftAgeValue,rightAgeValue,leftDistanceValue,rightDistanceValue;
    private Double latitude,longitude;
    private long mLastClickTime = 0;
    /*@Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        // the content
        final LinearLayout root = new LinearLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("onreateView","on createview");
        final View v = inflater.inflate(R.layout.arena_filter_layout, container, false);
        txtLocation = (EditText)v.findViewById(R.id.txtLocation);
        searchButton=(Button) v.findViewById(R.id.search_button);
        radioGroup=(RadioGroup) v.findViewById(R.id.radiogroup);
        ageSeekBar=(RangeSeekBar)v.findViewById(R.id.age_seekbar);
        ageTextView=(TextView)v.findViewById(R.id.age_text);
        distanceTextView=(TextView)v.findViewById(R.id.distance_textview);
        distanceSeekBar=(RangeSeekBar)v.findViewById(R.id.distance_seekbar);

        ageSeekBar.setRangeRules(18, 60, 10,1);//set min 、max、minimum interva
        ageSeekBar.setRangeInterval(1);
        txtLocation.setKeyListener(null);

        distanceSeekBar.setRangeRules(0, 100, 10,1);//set min 、max、minimum interva
        distanceSeekBar.setRangeInterval(1);

        ageSeekBar.setValue(20,50);
        leftAgeValue=20;
        rightAgeValue=50;
        distanceSeekBar.setValue(10,50);
        leftDistanceValue=10;
        rightDistanceValue=50;
        rightAgeValue=50;

        ageTextView.setText("20 to 50 years");
        distanceTextView.setText("10 to 50 km");

        ageSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                //leftValue is left seekbar value, rightValue is right seekbar value

                leftAgeValue = (int)leftValue;
                rightAgeValue=(int) rightValue;
                ageTextView.setText((int)leftValue+" to "+(int)rightValue+" years");
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view,  boolean isLeft) {
                //start tracking touch
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view,  boolean isLeft) {
                //stop tracking touch
            }
        });

        distanceSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                //leftValue is left seekbar value, rightValue is right seekbar value

                leftDistanceValue = (int)leftValue;
                rightDistanceValue=(int) rightValue;
                distanceTextView.setText((int)leftValue+" to "+(int)rightValue+" km");
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view,  boolean isLeft) {
                //start tracking touch
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view,  boolean isLeft) {
                //stop tracking touch
            }
        });

        txtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // mis-clicking prevention, using threshold of 1000 ms
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();


                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtLocation.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(),"Please enter location",Toast.LENGTH_LONG).show();
                }

                else {
                    Log.e("radiogroup", radioGroup.getCheckedRadioButtonId() + "");


                    // get selected radio button from radioGroup
                    int selectedId = radioGroup.getCheckedRadioButtonId();


                    // find the radiobutton by returned id
                    radioButton = (RadioButton) v.findViewById(selectedId);


                    Intent intent = new Intent(getActivity(), SearchUserActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("ageTo", rightAgeValue);
                    bundle.putInt("ageFrom", leftAgeValue);
                    bundle.putString("area", txtLocation.getText().toString());
                    bundle.putString("sex", radioButton.getText().toString());
                    bundle.putInt("distanceTo", rightDistanceValue);
                    bundle.putInt("distanceFrom", leftDistanceValue);
                    bundle.putDouble("latitude", latitude);
                    bundle.putDouble("longitude", longitude);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                Log.i(TAG, "Place: " + place.getName());
                txtLocation.setText(place.getName());
                latitude=place.getLatLng().latitude;
                longitude=place.getLatLng().longitude;

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
