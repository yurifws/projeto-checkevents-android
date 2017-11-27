package projetockeckevents.novaroma.br.projetocheckevents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import projetockeckevents.novaroma.br.projetocheckevents.entities.Contato;
import projetockeckevents.novaroma.br.projetocheckevents.entities.Usuario;
import projetockeckevents.novaroma.br.projetocheckevents.util.Helper;

public class AtualizacaoUsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView txtLoginAtu;
    private EditText edTxtSenhaAtu;
    private EditText edTxtConfirmaSenhaAtu;
    private EditText edTxtNomeAtu;
    private EditText edTxtSobrenomeAtu;
    private RadioButton rbMasculinoAtu;
    private RadioButton rbFemininoAtu;
    private RadioButton rbOrganizadorAtu;
    private RadioButton rbUsuarioComumAtu;

    private EditText edTxtTelefoneAtu;
    private EditText edTxtCelularAtu;
    private EditText edTxtEmailAtu;

    private Button btnAtualizarUsuario;

    private Intent intent;
    private Usuario usuarioLogado;

    private StringBuilder urlComplemento;

    // Progress Dialog Object
    private ProgressDialog prgDialog;
    // Error Msg TextView Object
    private TextView errorMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizacao_usuario);


        intent = getIntent();
        usuarioLogado = (Usuario) intent.getParcelableExtra("usuario");

        txtLoginAtu = (TextView) findViewById(R.id.txtLoginAtu);

        edTxtSenhaAtu = (EditText) findViewById(R.id.edTxtSenhaAtu);
        edTxtConfirmaSenhaAtu = (EditText) findViewById(R.id.edTxtConfirmaSenhaAtu);

        edTxtNomeAtu = (EditText) findViewById(R.id.edTxtNomeAtu);
        edTxtSobrenomeAtu = (EditText) findViewById(R.id.edTxtSobrenomeAtu);

        rbMasculinoAtu = (RadioButton) findViewById(R.id.rbMasculinoAtu);
        rbMasculinoAtu.setOnClickListener(this);
        //rbMasculinoCad.setChecked(true);

        rbFemininoAtu = (RadioButton) findViewById(R.id.rbFemininoAtu);
        rbFemininoAtu.setOnClickListener(this);

        rbOrganizadorAtu = (RadioButton) findViewById(R.id.rbOrganizadorAtu);
        rbOrganizadorAtu.setOnClickListener(this);

        rbUsuarioComumAtu = (RadioButton) findViewById(R.id.rbUsuarioComumAtu);
        rbUsuarioComumAtu.setOnClickListener(this);
        //rbUsuarioComum.setChecked(true);

        edTxtTelefoneAtu = (EditText) findViewById(R.id.edTxtTelefoneAtu);
        edTxtTelefoneAtu.setOnClickListener(this);

        edTxtCelularAtu = (EditText) findViewById(R.id.edTxtCelularAtu);
        edTxtCelularAtu.setOnClickListener(this);

        edTxtEmailAtu = (EditText) findViewById(R.id.edTxtEmailAtu);
        edTxtEmailAtu.setOnClickListener(this);

        carregarDadosUsuario();

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Por favor, aguarde...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.rbMasculinoAtu){
            rbMasculinoAtu.setChecked(true);
            rbFemininoAtu.setChecked(false);
        }
        if(view.getId() == R.id.rbFemininoAtu){
            rbFemininoAtu.setChecked(true);
            rbMasculinoAtu.setChecked(false);
        }
        if(view.getId() == R.id.rbOrganizadorAtu){
            rbOrganizadorAtu.setChecked(true);
            rbUsuarioComumAtu.setChecked(false);
        }
        if(view.getId() == R.id.rbUsuarioComumAtu){
            rbUsuarioComumAtu.setChecked(true);
            rbOrganizadorAtu.setChecked(false);
        }
        if(view.getId() == R.id.btnAtualizarUsuario){
            prgDialog.show();
            atualizarUsuario();
        }

    }

    private void carregarDadosUsuario(){
        try{
            txtLoginAtu.setText(usuarioLogado.getLogin());
            edTxtSenhaAtu.setText(usuarioLogado.getSenha());
            edTxtConfirmaSenhaAtu.setText(usuarioLogado.getSenha());
            edTxtNomeAtu.setText(usuarioLogado.getNome());
            edTxtSobrenomeAtu.setText(usuarioLogado.getSobrenome());

            if(usuarioLogado.getSexo().equals("M"))
                rbMasculinoAtu.setChecked(true);
            else
                rbFemininoAtu.setChecked(true);

            if(usuarioLogado.isTipoUsuario() == true)
                rbOrganizadorAtu.setChecked(true);
            else
                rbUsuarioComumAtu.setChecked(true);

            edTxtTelefoneAtu.setText(usuarioLogado.getContato().getTelefone() + "");
            edTxtCelularAtu.setText(usuarioLogado.getContato().getCelular()+ "");
            edTxtEmailAtu.setText(usuarioLogado.getContato().getEmail()+ "");


        }catch (Exception ex){
            ex.getStackTrace();
        }

    }

    public void atualizarUsuario() {
        String resultado;
        Usuario usuario;
        Gson gson;

        try {
            gson = new Gson();
            if(!edTxtSenhaAtu.getText().toString().trim().equals("") &&
                    !edTxtConfirmaSenhaAtu.getText().toString().trim().equals("")) {
                if(edTxtSenhaAtu.getText().toString().trim().equals(edTxtConfirmaSenhaAtu.getText().toString().trim())) {
                    String senhaAtualizado = edTxtSenhaAtu.getText().toString();
                    String nomeAtualizado = edTxtNomeAtu.getText().toString();
                    String sobrenomeAtualizado = edTxtSobrenomeAtu.getText().toString();
                    String sexoAtualizado = "M";
                    String telefoneAtualizado = edTxtTelefoneAtu.getText().toString();
                    String celularAtualizado = edTxtCelularAtu.getText().toString();
                    String emailAtualizado = edTxtEmailAtu.getText().toString();
                    boolean tipoUsuario = rbOrganizadorAtu.isChecked();

                    if (rbMasculinoAtu.isChecked()) {
                        sexoAtualizado = "M";
                    } else {
                        sexoAtualizado = "F";
                    }

                    urlComplemento = new StringBuilder();
                    urlComplemento.append("usuario/atualizar/");
                    usuario = usuarioLogado;

                    usuario.setTipoUsuario(tipoUsuario);

                    //verificar se a senha foi alterada
                    if(!usuario.getSenha().equals(senhaAtualizado))
                        usuario.setSenha(senhaAtualizado);

                    if(!usuario.getNome().equals(nomeAtualizado))
                        usuario.setNome(nomeAtualizado);

                    if(!usuario.getSobrenome().equals(sobrenomeAtualizado))
                        usuario.setSobrenome(sobrenomeAtualizado);

                    if(!usuario.getSexo().equals(sexoAtualizado))
                        usuario.setSexo(sexoAtualizado);

                    if(usuario.getContato() != null) {

                        if (!String.valueOf(usuario.getContato().getTelefone()).equals(telefoneAtualizado))
                            usuario.getContato().setTelefone(Integer.parseInt(telefoneAtualizado));

                    if(!String.valueOf(usuario.getContato().getCelular()).equals(celularAtualizado))
                        usuario.getContato().setCelular(Integer.parseInt(celularAtualizado));

                    if(!usuario.getContato().getEmail().equals(emailAtualizado))
                        usuario.getContato().setEmail(emailAtualizado);
                    } else {
                        usuario.setContato(new Contato());
                        usuario.getContato().setTelefone(Integer.parseInt(telefoneAtualizado));
                        usuario.getContato().setCelular(Integer.parseInt(celularAtualizado));
                        usuario.getContato().setEmail(emailAtualizado);
                    }

                    gson = new Gson();

                    StringEntity entity = new StringEntity(gson.toJson(usuario), "UTF-8");


                    AcessoRest.put(getApplicationContext(),urlComplemento.toString(), entity, "application/json", new JsonHttpResponseHandler(){

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response){

                            Gson gson = new Gson();
                            Usuario usuarioRetorno = gson.fromJson(response.toString(), Usuario.class);
                            Toast.makeText(getApplicationContext(), "Atualização realizada com sucesso", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AtualizacaoUsuarioActivity.this, NavigationDrawerActivity.class);
                            intent.putExtra("usuario", usuarioRetorno);
                            setResult(RESULT_OK, intent);

                            usuarioLogado = usuarioRetorno;


                            startActivity(intent);
                            prgDialog.dismiss();
                            finish();
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            if (statusCode == 404) {
                                Toast.makeText(getApplicationContext(), "404 - Nie odnaleziono serwera!", Toast.LENGTH_LONG).show();
                            } else if (statusCode == 500) {
                                Toast.makeText(getApplicationContext(), "500 - Coś poszło nie tak po stronie serwera!", Toast.LENGTH_LONG).show();
                            } else if (statusCode == 403) {
                                Toast.makeText(getApplicationContext(), "Podano niepoprawne dane!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), throwable.toString(), Toast.LENGTH_LONG).show();
                            }
                        }

                    });
                } else
                    Toast.makeText(getApplicationContext(), "Confirmação de senha não confere", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getApplicationContext(), "Campos obrigatórios(*) náo podem ser nulos", Toast.LENGTH_SHORT).show();

            prgDialog.dismiss();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
