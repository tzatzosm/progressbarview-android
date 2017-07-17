package qq.max.progressbar.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;


import java.util.Random;

import com.max.progressbarview.ProgressBarView;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();

    Button button;
    ProgressBarView progressBarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        progressBarView = (ProgressBarView) findViewById(R.id.progressView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                progressBarView.setPrimaryProgressValue(random.nextInt(100) + 1);
            }
        });

        progressBarView.setOnProgressChangedListener(new ProgressBarView.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(ProgressBarView progressBarView, float progress, boolean fromUser) {
                Log.d(TAG, String.format("onProgressChanged %f, %b", progress, fromUser));
            }

            @Override
            public void onTouchStart(ProgressBarView progressBarView) {
                Log.d(TAG, String.format("onTouchStart"));
            }

            @Override
            public void onTouchEnd(ProgressBarView progressBarView) {
                Log.d(TAG, String.format("onTouchEnd"));
            }
        });

        progressBarView.setMinProgressValue(0f);
        progressBarView.setMaxProgressValue(100f);
        progressBarView.setPrimaryProgressValue(30);
        progressBarView.setSecondaryProgressValue(60);
    }


}
