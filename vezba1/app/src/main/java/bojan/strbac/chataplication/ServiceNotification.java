package bojan.strbac.chataplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Korisnik on 30.5.2018..
 */

public class ServiceNotification extends Service{

    private BinderNotification binder_notification = null;

    @Override
    public IBinder onBind(Intent intent) {

        if (binder_notification == null) {
            binder_notification = new BinderNotification();
        }
        return (IBinder) binder_notification;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        binder_notification.stop();
        return super.onUnbind(intent);
    }
}
