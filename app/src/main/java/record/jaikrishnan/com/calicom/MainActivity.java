package record.jaikrishnan.com.calicom;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView t;
    String line,answer1 = null;
    String output = "";
    String answer[];
    String outputFile;
    Button voice;
    BufferedReader in;
    static final int check = 1111;
    VideoView videoView;
    ArrayList<String> results;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = (TextView) findViewById(R.id.value);
        voice = (Button) findViewById(R.id.voice);
        voice.setOnClickListener(this);
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/sub.txt";
        File encyptFile=new File(outputFile);
        System.out.println(encyptFile.exists());
        System.out.println(encyptFile.canRead());

        try {
            in = new BufferedReader(new FileReader(outputFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            line = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(line != null)
        {

            output+=line;
            output+="\n";
            try {
                line = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        videoView =(VideoView)findViewById(R.id.videoview);

        //Creating MediaController
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);

        //specify the location of media file
        Uri uri=Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/quantico.mp4");

        //Setting MediaController and URI, then starting the videoView
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();


    }

    void seek(String ans){
        String[] data = output.split("\n\n");

        for(int i = 0;i<data.length;i++){

            if(data[i].contains(ans)){
                answer1 = data[i];
                //System.out.println(data[i]);
                break;
                //System.out.println(answer[1]);
            }

        }
        if(answer1==null){
            return;
        }

            answer = answer1.split("\n");
            System.out.println(answer[1].substring(0, 8));
            System.out.println(answer[1].substring(0, 8).substring(6, 8));
            System.out.println(answer[1].substring(0, 8).substring(3, 5));
            System.out.println(answer[1].substring(0, 8).substring(0, 2));
            System.out.println("Value for seeking " + calculate(answer[1].substring(0, 8)));
            int cal = calculate(answer[1].substring(0, 8));
            Toast.makeText(MainActivity.this, "Value for seeking " + cal, Toast.LENGTH_LONG).show();
            cal = cal*1000;
            videoView.seekTo(cal);
            videoView.start();

    }

    static int calculate(String value){
        int hr = 0,min = 0,sec = 0;
        sec = Integer.parseInt(value.substring(6,8));
        min = Integer.parseInt(value.substring(3,5));
        hr = Integer.parseInt(value.substring(0,2));
        hr = hr * 60 * 60;
        min = min * 60;
        return hr+min+sec;
    }

    @Override
    public void onClick(View v) {
        videoView.pause();
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to Forward!!!");
        startActivityForResult(i, check);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == check && resultCode == RESULT_OK) {

            results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            seek(results.get(0));
            t.setText("Your text is "+results.get(0));
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
