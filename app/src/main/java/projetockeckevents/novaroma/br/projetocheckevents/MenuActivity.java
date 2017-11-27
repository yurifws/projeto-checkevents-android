package projetockeckevents.novaroma.br.projetocheckevents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import projetockeckevents.novaroma.br.projetocheckevents.entities.Usuario;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnAtualizarUsuario;
    private Button btnCadastrarEvento;
    private Button btnMostrarMapa;

    private Intent intent;
    private Usuario usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        intent = getIntent();
        usuarioLogado = (Usuario) intent.getParcelableExtra("usuario");

        btnAtualizarUsuario = (Button) findViewById(R.id.btnAtualizarUsuario);
        btnAtualizarUsuario.setOnClickListener(this);

        btnCadastrarEvento = (Button) findViewById(R.id.btnCadastrarEvento);
        btnCadastrarEvento.setOnClickListener(this);

        btnMostrarMapa = (Button) findViewById(R.id.btnMostrarMapa);
        btnMostrarMapa.setOnClickListener(this);

        habilitarCriacaoEvento();



    }

    @Override
    protected void onRestart() {
        super.onRestart();


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAtualizarUsuario) {
            chamarTelaAtualizacaoUsuario();
        }
        if (view.getId() == R.id.btnCadastrarEvento) {
            chamarTelaCadastroEvento();
        }
        if (view.getId() == R.id.btnMostrarMapa) {
            chamarTelaMapa();
        }

    }

    private void chamarTelaAtualizacaoUsuario(){
        try{
            Intent intent = new Intent(MenuActivity.this, AtualizacaoUsuarioActivity.class);
            intent.putExtra("usuario", usuarioLogado);
            startActivityForResult(intent, 1);
        }catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    private void chamarTelaCadastroEvento(){
        try{
            Intent intent = new Intent(MenuActivity.this, CadastroEventoActivity.class);
            intent.putExtra("organizador", usuarioLogado);
            startActivity(intent);
        }catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    private void chamarTelaMapa(){
        try{
            Intent intent = new Intent(MenuActivity.this, MapsActivity.class);
            intent.putExtra("organizador", usuarioLogado);
            startActivity(intent);
        }catch (Exception ex){
            ex.getStackTrace();
        }
    }

    private void habilitarCriacaoEvento(){
        try{
            if(usuarioLogado.isTipoUsuario()){
                btnCadastrarEvento.setEnabled(true);
            }else
                btnCadastrarEvento.setEnabled(false);

        }catch (Exception ex){
            ex.getStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if(resultCode == RESULT_OK){
                usuarioLogado = data.getParcelableExtra("usuario");
                habilitarCriacaoEvento();
            }
        }
        if (requestCode == 2){
            if(resultCode == RESULT_OK){
                usuarioLogado = data.getParcelableExtra("usuario");
            }
        }
    }
}
