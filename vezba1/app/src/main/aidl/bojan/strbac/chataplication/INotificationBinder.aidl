// INotificationBinder.aidl
package bojan.strbac.chataplication;

// Declare any non-default types here with import statements

import bojan.strbac.chataplication.INotificationCallback;

interface INotificationBinder {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     void setCallback(in INotificationCallback callback);
    //void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
    //        double aDouble, String aString);
}
