package projetockeckevents.novaroma.br.projetocheckevents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AtualizacaoEventoActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnContinuarAtuEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizacao_evento);

        btnContinuarAtuEvento = (Button) findViewById(R.id.btnContinuarAtuEvento);
        btnContinuarAtuEvento.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnContinuarAtuEvento){

            Intent intent = new Intent(AtualizacaoEventoActivity.this, MapsActivity.class);
            startActivity(intent);
        }
    }
}
