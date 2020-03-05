package com.example.covid.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.example.covid.R;

public class HomeFragment extends Fragment{

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView textConfirm = root.findViewById(R.id.text_confirm);
        final TextView textDischarged = root.findViewById(R.id.text_discharged);
        final TextView textDeath = root.findViewById(R.id.text_death);

        final TextView textdConfirm = root.findViewById(R.id.text_dconfirm);
        final TextView textdDischarged = root.findViewById(R.id.text_ddischarged);
        final TextView textdDeath = root.findViewById(R.id.text_ddeath);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textConfirm.setText(s);
            }
        });

        new Thread(new Runnable() {
            ArrayList<String> texts;
            String confirm, death, discharged;
            int dConfirm = 0, dDeath = 0, dDischarge = 0;
            @Override
            public void run() {

                try {
                    texts = getVirusInfo();
                    //Get latest confirm number
                    int index = texts.get(0).lastIndexOf("comfirmCase");
                    String updatedText = texts.get(0).substring(index);

                    StringBuilder sb = new StringBuilder(
                            updatedText.length());
                    boolean find = false;
                    for(int i = 13; i < updatedText.length(); i++){
                        char c = updatedText.charAt(i);
                        if(c > 47 && c < 58){
                            find = true;
                            sb.append(c);
                        }else if (find) break;
                    }
                    confirm = sb.toString();

                    //Get confirm number from last day
                    int dIndex = index - 160;                           //Location of last day's data
                    updatedText = texts.get(0).substring(dIndex);

                    sb = new StringBuilder(
                            updatedText.length());
                    find = false;
                    for(int i = 13; i < updatedText.length(); i++){
                        char c = updatedText.charAt(i);
                        if(c > 47 && c < 58){
                            find = true;
                            sb.append(c);
                        }else if (find) break;
                    }
                    dConfirm = Integer.parseInt(confirm) - Integer.parseInt(sb.toString());

                    //Get latest death number
                    index = texts.get(0).lastIndexOf("death");
                    updatedText = texts.get(0).substring(index);

                    sb = new StringBuilder(
                            updatedText.length());
                    find = false;
                    for(int i = 7; i < updatedText.length(); i++){
                        char c = updatedText.charAt(i);
                        if(c > 47 && c < 58){
                            find = true;
                            sb.append(c);
                        }else if (find) break;
                    }
                    death = sb.toString();

                    //Get death number from last day

                    dIndex = index - 160;                           //Location of last day's data
                    updatedText = texts.get(0).substring(dIndex);

                    sb = new StringBuilder(
                            updatedText.length());
                    find = false;
                    for(int i = 7; i < updatedText.length(); i++){
                        char c = updatedText.charAt(i);
                        if(c > 47 && c < 58){
                            find = true;
                            sb.append(c);
                        }else if (find) break;
                    }
                    dDeath = Integer.parseInt(death) - Integer.parseInt(sb.toString());

                    //Get latest discharge number
                    index = texts.get(0).lastIndexOf("recover");
                    updatedText = texts.get(0).substring(index);

                    sb = new StringBuilder(
                            updatedText.length());
                    find = false;
                    for(int i = 9; i < updatedText.length(); i++){
                        char c = updatedText.charAt(i);
                        if(c > 47 && c < 58){
                            find = true;
                            sb.append(c);
                        }else if (find) break;
                    }
                    discharged = sb.toString();

                    //Get discharge number from last day
                    dIndex = index - 160;                           //Location of last day's data
                    updatedText = texts.get(0).substring(dIndex);

                    sb = new StringBuilder(
                            updatedText.length());
                    find = false;
                    for(int i = 9; i < updatedText.length(); i++){
                        char c = updatedText.charAt(i);
                        if(c > 47 && c < 58){
                            find = true;
                            sb.append(c);
                        }else if (find) break;
                    }
                    dDischarge = Integer.parseInt(discharged) - Integer.parseInt(sb.toString());


                } catch (IOException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable(){
                    public void run(){
                        textConfirm.setText(confirm);
                        textDeath.setText(death);
                        textDischarged.setText(discharged);
                        textdConfirm.setText(Integer.toString(dConfirm));
                        textdDeath.setText(Integer.toString(dDeath));
                        textdDischarged.setText(Integer.toString(dDischarge));
                    }
                });
            }

        }).start();
        return root;
    }

    public ArrayList<String> getVirusInfo() throws IOException{
        ArrayList<String> urls = new ArrayList<String>(); //to read each line
        URL url = new URL("https://api.n-cov.info/figure"); //My text file location
        //First open the connection
        HttpURLConnection conn=(HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(60000); // timing out in a minute

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        //t=(TextView)findViewById(R.id.TextView1); // ideally do this in onCreate()
        String str;
        while ((str = in.readLine()) != null) {
            urls.add(str);
        }
        in.close();
        return urls;
    }
}
