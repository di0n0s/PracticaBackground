package netmind.com.practicabackground;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "In Main Activity";
    private ProgressBar mProgressBar;

    public void setmProgressBar(ProgressBar mProgressBar) {
        this.mProgressBar = mProgressBar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button startThread_Btn = (Button) this.findViewById(R.id.btnStartThread);
        startThread_Btn.setOnClickListener(this);
        final Button startAsyncTask_Btn = (Button) this.findViewById(R.id.btnStartAsyncTask);
        startAsyncTask_Btn.setOnClickListener(this);
        this.mProgressBar = (ProgressBar) this.findViewById(R.id.progressBar);
        this.mProgressBar.setProgress(0);
    }

    public void sleepForAWhile(int numSeconds) {
        long endTime = System.currentTimeMillis() + (numSeconds * 1000);

        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    Log.i(MainActivity.TAG, "Sleeping...");
                    this.wait(endTime - System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnStartThread) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.i(MainActivity.TAG, "Thread started");
                    //sleepForAWhile(20);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Sleeping...", Toast.LENGTH_SHORT).show();
                        }
                    });
                    sleepForAWhile(20);
                }
            }).start();

        } else if (view.getId() == R.id.btnStartAsyncTask) {
            MyAsyncTask mAsyncTask = new MyAsyncTask(this);
            mAsyncTask.execute(1);




        }

    }

    private class MyAsyncTask extends AsyncTask<Integer, Integer, String> {

        private Context mContext;

        public MyAsyncTask(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected String doInBackground(Integer... params) {
            for (int idx = 1; idx <= 5; idx++){
                sleepForAWhile(params[0]);
                publishProgress(idx*20);
            }
            return "AsyncTask finished";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mProgressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(this.mContext, s, Toast.LENGTH_SHORT).show();
        }
    }


}
