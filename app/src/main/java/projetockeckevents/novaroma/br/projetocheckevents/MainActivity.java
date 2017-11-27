package projetockeckevents.novaroma.br.projetocheckevents;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;
import projetockeckevents.novaroma.br.projetocheckevents.entities.Usuario;


public class MainActivity extends Activity implements View.OnClickListener {
    private static final String PREF_NAME = "LoginActivityPreferences";
    private TextView txtCadastro;
    private Button btnLogar;
    private EditText edTxtLogin;
    private EditText edTxtSenha;
    private CheckBox ckbLoginAutomatico;

    private StringBuilder urlComplemento;

    // Progress Dialog Object
    private ProgressDialog prgDialog;
    // Error Msg TextView Object
    private TextView errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //verificar sharedpreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String login = sharedPreferences.getString("login", "");
        String senha = sharedPreferences.getString("senha", "");

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Por favor, aguarde...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);



        urlComplemento = new StringBuilder();
        urlComplemento.append("usuario/logar");
        RequestParams params = new RequestParams();
        params.put("login", login);
        params.put("senha", senha);



        AcessoRest.get(urlComplemento.toString(),params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                Log.i("oncreate", "teste");
                Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").enableComplexMapKeySerialization().create();
                Usuario usuario = gson.fromJson(response.toString(), Usuario.class);
                if(usuario != null){
                    prgDialog.show();

                    Intent intent = new Intent(MainActivity.this, NavigationDrawerActivity.class);
                    intent.putExtra("usuario", usuario);
                    startActivity(intent);

                    prgDialog.dismiss();
                }

                super.onSuccess(statusCode, headers, response);
            }
        });

        txtCadastro = (TextView) findViewById(R.id.txtCadastro);
        txtCadastro.setOnClickListener(this);

        btnLogar = (Button) findViewById(R.id.btnLogar);
        btnLogar.setOnClickListener(this);


        edTxtLogin = (EditText) findViewById(R.id.edTxtLogin);
        edTxtSenha = (EditText) findViewById(R.id.edTxtSenha);

        ckbLoginAutomatico = (CheckBox) findViewById(R.id.ckbLoginAutomatico);
        ckbLoginAutomatico.setChecked(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txtCadastro) {
            prgDialog.show();
            chamarTelaCadastro();
        }
        if (view.getId() == R.id.btnLogar) {
            prgDialog.show();
            chamarTelaMenu(edTxtLogin.getText().toString(), edTxtSenha.getText().toString());
            //chamarTelaMenu();
        }
    }

    private void chamarTelaCadastro(){
        try {
            Intent intent = new Intent(MainActivity.this, CadastroUsuarioActivity.class);
            startActivity(intent);

            prgDialog.dismiss();
        }catch (Exception ex){
            ex.getStackTrace();
        }
    }

    private void chamarTelaMenu(String login, String senha){
        try {
                urlComplemento = new StringBuilder();
                urlComplemento.append("usuario/logar");
                RequestParams params = new RequestParams();
                params.put("login", login);
                params.put("senha", senha);

                AcessoRest.get(urlComplemento.toString(),params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                        Log.i("chamarTela", "teste");
                        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy").enableComplexMapKeySerialization().create();
                        Usuario usuario = gson.fromJson(response.toString(), Usuario.class);
                        if(usuario != null) {
                            Log.i("chamarTela", response.toString());
                            Toast.makeText(getApplicationContext(), "Login efetuado com sucesso.", Toast.LENGTH_SHORT).show();

                            if(ckbLoginAutomatico.isChecked()){
                                SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("login", usuario.getLogin());
                                editor.putString("senha", usuario.getSenha());
                                editor.commit();

                            }
                            Intent intent = new Intent(MainActivity.this, NavigationDrawerActivity.class);
                            intent.putExtra("usuario", usuario);
                            startActivity(intent);
                            prgDialog.dismiss();
                        }else
                            Toast.makeText(getApplicationContext(), "Usuario e/ou senha incorretos.", Toast.LENGTH_SHORT).show();

                        prgDialog.dismiss();
                        super.onSuccess(statusCode, headers, response);
                    }
                });
        }catch (Exception ex){
            ex.getStackTrace();
        }
    }
}
