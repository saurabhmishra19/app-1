package park.epam.com.parkit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BasicService extends Service {
    public BasicService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("---created","created");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Toast.makeText(this, "on-start", Toast.LENGTH_LONG).show();

        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();

        Toast.makeText(this, "Service Dst", Toast.LENGTH_LONG).show();

    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d("onbind","binded");
        // TODO: Return the communication channel to the service.
         return null;
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
