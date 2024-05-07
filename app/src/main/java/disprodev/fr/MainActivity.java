package disprodev.fr;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private String BASE_URL = null;
    private static final String oEmbed_URL = "https://www.youtube.com/oembed?format=json&url=https://www.youtube.com/watch?v=";
    private Handler mHandler = new Handler();
    private int mDelay = 100; // Délai en millisecondes entre chaque répétition

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            updateProgressBar();
            mHandler.postDelayed(this, mDelay);
        }
    };


    private void startRepeatingTask() {
        mRunnable.run();
    }

    private void stopRepeatingTask() {
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String ip = PreferenceManager.getIPAddress(this);
        int port = PreferenceManager.getPort(this);

        BASE_URL = "http://"+ip +":"+port+"/track/";
        startRepeatingTask();
        ToggleButton toggleButton = findViewById(R.id.toggleButton);
        Button nextButton = findViewById(R.id.button_Next);
        Button prevButton = findViewById(R.id.button_Prev);
        updateToggleButtonStateandnameofmusic(toggleButton);
        updateVideoTitle();
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton2);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(BASE_URL + "next");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("POST");
                            connection.setRequestProperty("Content-Type", "application/json");
                            connection.setDoOutput(true);

                            int responseCode = connection.getResponseCode();
                            if (responseCode == HttpURLConnection.HTTP_OK) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                updateVideoTitle();
                                            }
                                        }, 300);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                });
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
 prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(BASE_URL + "prev");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("POST");
                            connection.setRequestProperty("Content-Type", "application/json");
                            connection.setDoOutput(true);

                            int responseCode = connection.getResponseCode();
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            public void run() {
                                                updateVideoTitle();
                                            }
                                        }, 300);
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
        TextView toggleButtonLabel = findViewById(R.id.toggleButton);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleButtonLabel.setText("Pause");
                } else {
                    toggleButtonLabel.setText("Play");
                }
            }
        });
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(BASE_URL + "toggle-play-state");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("POST");
                            connection.setRequestProperty("Content-Type", "application/json");
                            connection.setDoOutput(true);

                            int responseCode = connection.getResponseCode();
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                });
                            } else {
                                // Error
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }

    private void updateProgressBar() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Connexion à l'API pour obtenir l'état de lecture actuel
                    URL url = new URL(BASE_URL + "state");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }
                        bufferedReader.close();
                        inputStream.close();

                        JSONObject jsonResponse = new JSONObject(response.toString());
                        float uiProgress = jsonResponse.getInt("uiProgress");
                        float duration = jsonResponse.getInt("duration");
                        boolean isPlaying = jsonResponse.getBoolean("playing");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                    ToggleButton toggleButton = findViewById(R.id.toggleButton);
                                    toggleButton.setChecked(isPlaying);
                                updateVideoTitle();
                                updateSeekBar(uiProgress, duration);
                            }
                        });
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    showToast("Error: " + e.getMessage());
                }
            }
        }).start();
    }

    private void updateSeekBar(float uiProgress, float duration) {
        SeekBar progressBar = findViewById(R.id.seekBar);
        int progress = (int) ((uiProgress / duration) * 100);
        progressBar.setProgress(progress);
    }


    private void updateVideoTitle() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Connexion à l'API pour obtenir l'état de lecture actuel
                    URL url = new URL(BASE_URL + "state");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }
                        bufferedReader.close();
                        inputStream.close();

                        // Analyse de la réponse JSON
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        String id = jsonResponse.getString("id");
                        final boolean isPlaying = jsonResponse.getBoolean("playing");

                        // Connexion à l'API oEmbed pour obtenir les détails de la vidéo
                        URL obj = new URL(oEmbed_URL + id);
                        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                        con.setRequestMethod("GET");
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        String inputLine;
                        StringBuilder response1 = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            response1.append(inputLine);
                        }
                        in.close();

                        // Analyse de la réponse JSON
                        JSONObject jsonObject = new JSONObject(response1.toString());
                        final String title = jsonObject.getString("title");
                        final String author_name = jsonObject.getString("author_name");
                        final String thumbnailUrl = jsonObject.getString("thumbnail_url");
                                ToggleButton toggleButton = findViewById(R.id.toggleButton);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                 toggleButton.setChecked(isPlaying);

                                ImageView imageView = findViewById(R.id.videoThumbnailImageView);
                                Picasso.get()
                                        .load(thumbnailUrl)
                                        .into(imageView);
                                TextView videoTitleTextView = findViewById(R.id.videoTitleTextView);
                                TextView videoAutorsTextView = findViewById(R.id.videoAutorsTextView);
                                videoTitleTextView.setText(title);
                                videoAutorsTextView.setText(author_name);
                            }
                        });

                    } else {
                        // Affichage d'un message d'erreur en cas de problème de connexion
                        Log.e("HTTP_ERROR", "HTTP error code: " + responseCode);
                        showToast("Failed to fetch video title");
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    showToast("Error: " + e.getMessage());
                }
            }
        }).start();
    }

    // Méthode utilitaire pour afficher un toast
    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Méthode pour récupérer l'état de lecture actuel à partir de l'API
    private void updateToggleButtonStateandnameofmusic(final ToggleButton toggleButton) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(BASE_URL + "state");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");

                    int responseCode = connection.getResponseCode();
                    Log.d("HTTP_RESPONSE_CODE", "Response code: " + responseCode); // Ajout du log pour le code de réponse HTTP

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }
                        bufferedReader.close();
                        inputStream.close();

                        Log.d("JSON_RESPONSE", "Response: " + response.toString()); // Ajout du log pour la réponse JSON

                        JSONObject jsonResponse = new JSONObject(response.toString());
                        final boolean isPlaying = jsonResponse.getBoolean("playing");

                        Log.d("IS_PLAYING", "Is playing: " + isPlaying); // Ajout du log pour l'état de lecture récupéré

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toggleButton.setChecked(isPlaying);
                            }
                        });
                    } else {
                        // Gérer les erreurs de requête
                        Log.e("HTTP_ERROR", "HTTP error code: " + responseCode); // Ajout du log pour les erreurs de requête HTTP
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
        }
        private void play() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(BASE_URL + "play");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/json");

                        int responseCode = connection.getResponseCode();
                        Log.d("HTTP_RESPONSE_CODE", "Response code: " + responseCode); // Ajout du log pour le code de réponse HTTP

                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            InputStream inputStream = connection.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                response.append(line);
                            }
                            bufferedReader.close();
                            inputStream.close();

                            Log.d("JSON_RESPONSE", "Response: " + response.toString()); // Ajout du log pour la réponse JSON

                            JSONObject jsonResponse = new JSONObject(response.toString());
                            final boolean isPlaying = jsonResponse.getBoolean("playing");

                            Log.d("IS_PLAYING", "Is playing: " + isPlaying); // Ajout du log pour l'état de lecture récupéré

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                }
                            });
                        } else {
                            // Gérer les erreurs de requête
                            Log.e("HTTP_ERROR", "HTTP error code: " + responseCode); // Ajout du log pour les erreurs de requête HTTP
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        }
        private void pause() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(BASE_URL + "pause");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");

                    int responseCode = connection.getResponseCode();
                    Log.d("HTTP_RESPONSE_CODE", "Response code: " + responseCode); // Ajout du log pour le code de réponse HTTP

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }
                        bufferedReader.close();
                        inputStream.close();

                        Log.d("JSON_RESPONSE", "Response: " + response.toString()); // Ajout du log pour la réponse JSON

                        JSONObject jsonResponse = new JSONObject(response.toString());
                        final boolean isPlaying = jsonResponse.getBoolean("playing");

                        Log.d("IS_PLAYING", "Is playing: " + isPlaying); // Ajout du log pour l'état de lecture récupéré

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                    } else {
                        // Gérer les erreurs de requête
                        Log.e("HTTP_ERROR", "HTTP error code: " + responseCode); // Ajout du log pour les erreurs de requête HTTP
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
}