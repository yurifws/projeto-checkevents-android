package projetockeckevents.novaroma.br.projetocheckevents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import projetockeckevents.novaroma.br.projetocheckevents.entities.Contato;
import projetockeckevents.novaroma.br.projetocheckevents.entities.Evento;
import projetockeckevents.novaroma.br.projetocheckevents.entities.Usuario;
import projetockeckevents.novaroma.br.projetocheckevents.util.Helper;

public class CadastroUsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edTxtLoginCad;
    private EditText edTxtSenhaCad;
    private EditText edTxtConfirmaSenhaCad;
    //private EditText edTxtNomeCad;
    //private EditText edTxtSobrenomeCad;
//    private EditText edTxtTelefoneCad;
//    private EditText edTxtCelularCad;
    private EditText edTxtEmailCad;

    private static Boolean resultadoBooleano;

    private RadioButton rbMasculinoCad;
    private RadioButton rbFemininoCad;
//    private RadioButton rbOrganizador;
//    private RadioButton rbUsuarioComum;
    private Button btnCadastrarUsuario;


    private StringBuilder urlComplemento;

    // Progress Dialog Object
    private ProgressDialog prgDialog;
    // Error Msg TextView Object
    private TextView errorMsg;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        edTxtLoginCad = (EditText) findViewById(R.id.edTxtLoginCad);
        edTxtLoginCad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validarLoginCad();
                }
            }
        });

        edTxtSenhaCad = (EditText) findViewById(R.id.edTxtSenhaCad);
        edTxtSenhaCad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
//                    if(!edTxtSenhaCad.getText().equals(edTxtConfirmaSenhaCad.getText())) {
//                        edTxtSenhaCad.setTextColor(Color.RED);
//                        edTxtConfirmaSenhaCad.setTextColor(Color.RED);
//                    }else{
//                        edTxtSenhaCad.setTextColor(Color.GREEN);
//                        edTxtConfirmaSenhaCad.setTextColor(Color.GREEN);
//                    }
                }
            }
        });

        edTxtConfirmaSenhaCad = (EditText) findViewById(R.id.edTxtConfirmaSenhaCad);
        edTxtConfirmaSenhaCad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
//                    if(edTxtSenhaCad.getText().equals(edTxtConfirmaSenhaCad.getText())) {
//                        edTxtSenhaCad.setTextColor(Color.RED);
//                        edTxtConfirmaSenhaCad.setTextColor(Color.RED);
//                    }else{
//                        edTxtSenhaCad.setTextColor(Color.GREEN);
//                        edTxtConfirmaSenhaCad.setTextColor(Color.GREEN);
//                    }
                }
            }
        });

        //edTxtNomeCad = (EditText) findViewById(R.id.edTxtNomeCad);
//        edTxtSobrenomeCad = (EditText) findViewById(R.id.edTxtSobrenomeCad);

        rbMasculinoCad = (RadioButton) findViewById(R.id.rbMasculinoCad);
        rbMasculinoCad.setOnClickListener(this);
        rbMasculinoCad.setChecked(true);

        rbFemininoCad = (RadioButton) findViewById(R.id.rbFemininoCad);
        rbFemininoCad.setOnClickListener(this);

//        rbOrganizador = (RadioButton) findViewById(R.id.rbOrganizador);
//        rbOrganizador.setOnClickListener(this);

//        rbUsuarioComum = (RadioButton) findViewById(R.id.rbUsuarioComum);
//        rbUsuarioComum.setOnClickListener(this);
//        rbUsuarioComum.setChecked(true);

//        edTxtTelefoneCad = (EditText) findViewById(R.id.edTxtTelefoneCad);
//        edTxtTelefoneCad.setOnClickListener(this);

//        edTxtCelularCad = (EditText) findViewById(R.id.edTxtCelularCad);
//        edTxtCelularCad.setOnClickListener(this);

//        edTxtEmailCad = (EditText) findViewById(R.id.edTxtEmailCad);
//        edTxtEmailCad.setOnClickListener(this);

        btnCadastrarUsuario = (Button) findViewById(R.id.btnCadastrarUsuario);
        btnCadastrarUsuario.setOnClickListener(this);

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Por favor, aguarde...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rbMasculinoCad) {
            rbMasculinoCad.setChecked(true);
            rbFemininoCad.setChecked(false);
        }
        if (view.getId() == R.id.rbFemininoCad) {
            rbFemininoCad.setChecked(true);
            rbMasculinoCad.setChecked(false);
        }
//        if (view.getId() == R.id.rbOrganizador) {
//            rbOrganizador.setChecked(true);
//            rbUsuarioComum.setChecked(false);
//        }
//        if (view.getId() == R.id.rbUsuarioComum) {
//            rbUsuarioComum.setChecked(true);
//            rbOrganizador.setChecked(false);
//        }
        if (view.getId() == R.id.btnCadastrarUsuario) {
            prgDialog.show();
            cadastrarUsuario();
        }

    }

    public void  validarLoginCad() {
        String loginCad;
        try {
            Log.i("Validar Usuario", "Entrou no ValidarLogin");
            urlComplemento = new StringBuilder();
            urlComplemento.append("usuario/verificarLogin/");

            loginCad = edTxtLoginCad.getText().toString();

            RequestParams params = new RequestParams();
            params.put("login", loginCad);


            resultadoBooleano = false;
            AcessoRest.get(urlComplemento.toString(), params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    Log.i("Validar Usuario", "Entrou no onSuccess");
                    Gson gson = new Gson();

//                    Type collectionType = new TypeToken<Map<String, Boolean>>() {}.getType();
//                    Map<String, Boolean> retorno = gson.fromJson(response.toString(), collectionType);
                    try {
                        resultadoBooleano = (Boolean) response.get("valido");
                        Log.i("Validar Usuario", "Validou - Resultado: " + String.valueOf(resultadoBooleano));

                        if (resultadoBooleano == true) {
                            edTxtLoginCad.setTextColor(Color.RED);
                        }else{
                            edTxtLoginCad.setTextColor(Color.GREEN);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("Validar Usuario", "Saindo do onSuccess");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.i("Validar Usuario", "Entrou no onFailure");
                    if (statusCode == 404) {
                        Log.i("Validar Usuario", "Erro 404");
                        Toast.makeText(getApplicationContext(), "404 - Nie odnaleziono serwera!", Toast.LENGTH_LONG).show();
                    } else if (statusCode == 500) {
                        Log.i("Validar Usuario", "Erro 500");
                        Toast.makeText(getApplicationContext(), "500 - Coś poszło nie tak po stronie serwera!", Toast.LENGTH_LONG).show();
                    } else if (statusCode == 403) {
                        Log.i("Validar Usuario", "Erro 403");
                        Toast.makeText(getApplicationContext(), "Podano niepoprawne dane!", Toast.LENGTH_LONG).show();
                    } else {
                        Log.i("Validar Usuario", throwable.toString());
                        Toast.makeText(getApplicationContext(), throwable.toString(), Toast.LENGTH_LONG).show();
                    }
                    Log.i("Validar Usuario", "Saindo do onFailure");
                }

            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
//        try {
//            Log.i("Validar Usuario", "Entrando no wait");
//            wait(400);
//            Log.i("Validar Usuario", "Saindo do wait");
//        } catch (InterruptedException e) {
//            Log.i("Validar Usuario", "Erro!");
//            e.printStackTrace();
//        }
        Log.i("Validar Usuario", "Saindo do ValidarLogin");
        //return resultadoBooleano;
    }

    public void cadastrarUsuario() {
        String resultado;
        Usuario usuario;
        Contato contato;
        Gson gson;
        try {
            //Log.i("Cadastro de Usuario", "Iniciar Validação de Login");
            //validarLoginCad();
            //Log.i("Cadastro de Usuario", "Terminou a Validação de Login");
            if (!edTxtLoginCad.getText().toString().trim().equals("") &&
                    !edTxtSenhaCad.getText().toString().trim().equals("") &&
                    !edTxtConfirmaSenhaCad.getText().toString().trim().equals("")) {
                Log.i("Cadastro de Usuario", "Testando se o Usuario é valido");
                if (!resultadoBooleano) {
                    Log.i("Cadastro de Usuario", "Usuario valido");
                    if (edTxtSenhaCad.getText().toString().trim().equals(edTxtConfirmaSenhaCad.getText().toString().trim())) {
                        usuario = new Usuario();
                        contato = new Contato();
                        String login = edTxtLoginCad.getText().toString();
                        String senha = edTxtSenhaCad.getText().toString();
                        //String nome = edTxtNomeCad.getText().toString();
//                        String sobrenome = edTxtSobrenomeCad.getText().toString();
                        String sexo = "M";
//                        boolean isOrganizador = rbOrganizador.isChecked();
                        if (rbMasculinoCad.isChecked()) {
                           sexo = "M";
                        } else {
                            sexo = "F";
                        }

                        urlComplemento = new StringBuilder();
                        urlComplemento.append("usuario/incluir/");

                        usuario.setLogin(login);
                        usuario.setSenha(senha);
                        usuario.setNome("");
                        usuario.setSobrenome("");
                        usuario.setSexo(sexo);
                        usuario.setTipoUsuario(true);

//                        String telefone = edTxtTelefoneCad.getText().toString();
////                        String celular = edTxtCelularCad.getText().toString();
////                        String email = edTxtEmailCad.getText().toString();
//
//                        contato.setTelefone(Integer.parseInt(telefone));
                        contato.setTelefone(0);
//                        contato.setCelular(Integer.parseInt(celular));
                        contato.setCelular(0);
                        //contato.setEmail(email);
                        contato.setEmail("");

                        RequestParams params = new RequestParams();

                        usuario.setContato(contato);

                        gson = new Gson();

                        StringEntity entity = new StringEntity(gson.toJson(usuario), "UTF-8");

                        AcessoRest.post(getApplicationContext(),urlComplemento.toString(), entity, "application/json", new JsonHttpResponseHandler(){

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                                String resultado = response.toString();
                                if (resultado != "null") {
                                    Log.i("JSON", resultado);
                                    Toast.makeText(getApplicationContext(), "Cadastro de usuário realizado com sucesso", Toast.LENGTH_SHORT).show();

                                    prgDialog.dismiss();
                                    finish();
                                }
                                else
                                    Toast.makeText(getApplicationContext(), "Login inválido, ja existente em nossos dados", Toast.LENGTH_SHORT).show();

                                super.onSuccess(statusCode, headers, response);
                            }

                        });
                    } else
                        Toast.makeText(getApplicationContext(), "Confirmaçao de senha não confere", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("Cadastro de Usuario", "Usuario Invalido");
                    Toast.makeText(getApplicationContext(), "Login inválido, ja existente em nossos dados", Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(getApplicationContext(), "Campos obrigatórios(*) náo podem ser nulos", Toast.LENGTH_SHORT).show();

            prgDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cadastro_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_next:
                //startActivity
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("CadastroUsuario Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
