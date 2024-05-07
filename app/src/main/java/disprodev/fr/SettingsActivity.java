package disprodev.fr;

import static android.widget.Toast.LENGTH_SHORT;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_ip);
        EditText ipEditText = findViewById(R.id.editIpTexte);
        EditText portEditText = findViewById(R.id.editPortTexte);
        Button confirmButton = findViewById(R.id.button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = ipEditText.getText().toString();
                String portStr = portEditText.getText().toString();
                if (TextUtils.isEmpty(ip) || TextUtils.isEmpty(portStr)) {
                    Toast.makeText(SettingsActivity.this, "Veuillez remplir tous les champs", LENGTH_SHORT).show();                } else {
                    Log.d(TAG, "Confirm button clicked");
                    saveIPAddressAndPort();
                }
            }
        });
    }

    private void saveIPAddressAndPort() {
        EditText ipEditText = findViewById(R.id.editIpTexte);
        EditText portEditText = findViewById(R.id.editPortTexte);

        String ip = ipEditText.getText().toString();
        int port = Integer.parseInt(portEditText.getText().toString());

        Log.d(TAG, "IP Address: " + ip);
        Log.d(TAG, "Port: " + port);

        // Enregistrer l'adresse IP et le port en utilisant le contexte de cette activité
        PreferenceManager.saveIPAddressAndPort(this, ip, port);

        // Afficher un message de confirmation
        String message = "Adresse IP : " + ip + ", port : " + port + " enregistrés avec succès.";

        Log.d(TAG, message);
        Toast.makeText(this, message, LENGTH_SHORT).show();
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
