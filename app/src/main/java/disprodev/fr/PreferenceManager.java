package disprodev.fr;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    private static final String PREF_NAME = "MyPreferences";
    private static final String KEY_IP = "ip";
    private static final String KEY_PORT = "port";

    // Enregistrer l'adresse IP et le port
    public static void saveIPAddressAndPort(Context context, String ip, int port) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(KEY_IP, ip);
        editor.putInt(KEY_PORT, port);
        editor.apply();
    }

    // Récupérer l'adresse IP enregistrée
    public static String getIPAddress(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(KEY_IP, "");
    }

    // Récupérer le port enregistré
    public static int getPort(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(KEY_PORT, 0); // Remplacer 0 par la valeur par défaut si nécessaire
    }
}
