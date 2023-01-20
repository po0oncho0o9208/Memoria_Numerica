package com.gametops.memorianumerica;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Principal extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN = 9001;
    FirebaseAuth mAuth;
    ImageView play, ajustes, trofeos, btngoogle;
    LinearLayout layout;
    ProgressBar progresbar;
    Sonido click;
    GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseFirestore db;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        //MobileAds.initialize(this, "ca-app-pub-1984616735532779~3068362417");

        sharedPreferences = getSharedPreferences("record", Context.MODE_PRIVATE);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        click = new Sonido(R.raw.click, this);
        play = findViewById(R.id.btnplay);
        btngoogle = findViewById(R.id.btngoogle);
        btngoogle.setOnClickListener(this);


        if (mAuth.getCurrentUser() != null) {


            // btngoogle.setText("sesion iniciada " + mAuth.getCurrentUser().getEmail());
            btngoogle.setBackgroundResource(R.drawable.btngooglecheck);

        } else {
            Toast.makeText(this, "No se ha iniciado sesion", Toast.LENGTH_SHORT).show();
        }

        play.setOnClickListener(this);
        ajustes = findViewById(R.id.btnajustes);
        ajustes.setOnClickListener(this);
        trofeos = findViewById(R.id.btntrofeos);
        trofeos.setOnClickListener(this);
        layout = findViewById(R.id.layoutprincipal);
        progresbar = findViewById(R.id.pgbr);


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Ajustes.vibrar(this, 50);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View vi = inflater.inflate(R.layout.dialogoconfirm, null);
            builder.setView(vi);
            final AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            Button botonsi = vi.findViewById(R.id.botonsi);

            botonsi.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Ajustes.vibrar(Principal.this, 50);
                            dialog.cancel();
                            Principal.super.onDestroy();
                            System.exit(0);
                        }
                    }
            );
            Button botonno = vi.findViewById(R.id.botonno);
            botonno.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Ajustes.vibrar(Principal.this, 50);
                            dialog.cancel();

                        }
                    }
            );
            dialog.show();
            //Metodos.dialogo( this, getLayoutInflater(), "¿seguro deseas salir de la aplicacion?", 0 );
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        layout.setVisibility(View.GONE);
        progresbar.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        layout.setVisibility(View.VISIBLE);
        progresbar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        layout.setVisibility(View.VISIBLE);
        progresbar.setVisibility(View.VISIBLE);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent;
        click.play();
        Ajustes.vibrar(this, 50);
        if (v.getId() == R.id.btngoogle) {
            if (mAuth.getCurrentUser() != null) {

                dialogsingoutgoogle();

            } else {

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        } else {
            switch (v.getId()) {
                case R.id.btnajustes:
                    intent = new Intent(this, Ajustes.class);
                    break;
                case R.id.btnplay:
                    intent = new Intent(this, Niveles.class);
                    break;
                case R.id.btntrofeos:
                    intent = new Intent(this, Trofeos.class);
                    break;
                default:
                    intent = null;
                    break;
            }
            startActivity(intent);
        }

    }

    private void dialogsingoutgoogle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View vi = inflater.inflate(R.layout.dialogoconfirm, null);
        builder.setView(vi);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        TextView txt = vi.findViewById(R.id.txtconfirm);
        txt.setText("Deseas cerrar la sesion de " + mAuth.getCurrentUser().getEmail() + " \n No podras recuperar tus logros si cambias de dispositivo");
        Button botonsi = vi.findViewById(R.id.botonsi);
        botonsi.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Ajustes.vibrar(Principal.this, 50);
                        dialog.cancel();
                        mAuth.signOut();
                        btngoogle.setBackgroundResource(R.drawable.btngoogle);

                    }
                }
        );
        Button botonno = vi.findViewById(R.id.botonno);
        botonno.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Ajustes.vibrar(Principal.this, 50);
                        dialog.cancel();

                    }
                }
        );
        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                System.out.println(e.getCause() + "????????????????????????" + e.toString());
                Toast.makeText(this, "No has iniciado sesion " + e.getCause() + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(Principal.this, "Bienvenido " + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                            //  btngoogle.setText("sesion de " + mAuth.getCurrentUser().getEmail());
                            btngoogle.setBackgroundResource(R.drawable.btngooglecheck);
                            verificarExistenciaDB();

                        } else {


                            // If sign in fails, display a message to the user.
                            // updateUI(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Principal.this, "No has iniciado sesion " + e.getCause() + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private boolean verificarExistenciaDB() {
        final boolean[] ver = new boolean[1];
        if (mAuth.getCurrentUser() != null) {
            db.collection("users").document(mAuth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.getResult().get("0contestada1") != null) {
                        Toast.makeText(Principal.this, "Ya existe la BD ", Toast.LENGTH_SHORT).show();
                        ver[0] = (boolean) task.getResult().get("0contestada1");
                        dialogoCrearDB();
                    } else {
                        crearDB();
                        Toast.makeText(Principal.this, "Se creara la BD ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(Principal.this, "Sin conexion al servidor", Toast.LENGTH_SHORT).show();
        }
        return ver[0];
    }


    private void leerDB() {

        if (mAuth.getCurrentUser() != null) {
            db.collection("users").document(mAuth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    for (int n = 0; n < 3; n++) {
                        for (int i = 0; i < 27; i++) {
                            long num = (long) task.getResult().get(n + "record" + i);
                            editor.putInt(n + "record" + i, (int) num);
                            editor.putBoolean(n + "contestada" + (i + 1), (Boolean) task.getResult().get(n + "contestada" + (i + 1)));
                        }
                    }
                    editor.commit();
                }
            });
        } else {
            Toast.makeText(Principal.this, "Sin conexion al servidor", Toast.LENGTH_SHORT).show();
        }

    }

    private void dialogoCrearDB() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View vi = inflater.inflate(R.layout.dialogoconfirm, null);
        builder.setView(vi);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        TextView txt = vi.findViewById(R.id.txtconfirm);

        if (sharedPreferences.getBoolean("0contestada1", false)) {
            txt.setText("Deseas recuperar el progreso guardado de " + mAuth.getCurrentUser().getEmail() + " se perdera el progreso actual");
        } else
            txt.setText("Deseas recuperar el progreso guardado de " + mAuth.getCurrentUser().getEmail());
        Button botonsi = vi.findViewById(R.id.botonsi);
        botonsi.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Ajustes.vibrar(Principal.this, 50);
                        dialog.cancel();
                        leerDB();

                    }
                }
        );
        Button botonno = vi.findViewById(R.id.botonno);
        botonno.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Ajustes.vibrar(Principal.this, 50);
                        dialog.cancel();
                        crearDB();

                    }
                }
        );
        dialog.show();

    }


    private void crearDB() {

        //verificar si hay datos en el dispositivo


        Map<String, Object> user = new HashMap<>();


        for (int n = 0; n < 3; n++) {
            for (int i = 0; i < 27; i++) {

                user.put(n + "record" + i, sharedPreferences.getInt(n + "record" + i, 0));
                user.put(n + "contestada" + (i + 1), sharedPreferences.getBoolean(n + "contestada" + (i + 1), false));
            }
        }
        db.collection("users").document(mAuth.getCurrentUser().getEmail()).set(user);


            /*
            db.collection("users").document(mAuth.getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Toast.makeText(Principal.this, "Hechos", Toast.LENGTH_SHORT).show();
                }
            });*/
            /*
            db.collection("users")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            Toast.makeText(Principal.this, "Hecho", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Error adding document", e);
                            Toast.makeText(Principal.this, "nop" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
*/

    }
}


