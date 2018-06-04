package bojan.strbac.chataplication;

/**
 * Created by student on 4.6.2018.
 */

public class Crypto {
    public native String crypt(String message);

    static {
        System.loadLibrary("crypt");
    }
}
