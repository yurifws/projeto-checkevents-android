package projetockeckevents.novaroma.br.projetocheckevents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import projetockeckevents.novaroma.br.projetocheckevents.entities.Contato;
import projetockeckevents.novaroma.br.projetocheckevents.entities.Endereco;
import projetockeckevents.novaroma.br.projetocheckevents.entities.Evento;
import projetockeckevents.novaroma.br.projetocheckevents.entities.Localizacao;
import projetockeckevents.novaroma.br.projetocheckevents.entities.TipoEvento;
import projetockeckevents.novaroma.br.projetocheckevents.entities.Usuario;
import projetockeckevents.novaroma.br.projetocheckevents.util.Helper;

public class CadastroEventoActivity extends AppCompatActivity
        implements View.OnClickListener {
    private EditText edTxtDescricaoCad;
    private EditText edTxtDataInicioCad;
    private EditText edTxtDataTerminoCad;
    private Button btnCadastrarEvento;

    private StringBuilder urlComplemento;

    private Intent intent;
    private Usuario organizador;
    private double latitudeCentro;
    private double longitudeCentro;

    private LatLng latLng;

    // Progress Dialog Object
    private ProgressDialog prgDialog;
    // Error Msg TextView Object
    private TextView errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_evento);

        intent = getIntent();
        organizador = (Usuario) intent.getParcelableExtra("organizador");

        latitudeCentro =  intent.getDoubleExtra("latitudeCentro", latitudeCentro);
        longitudeCentro =   intent.getDoubleExtra("longitudeCentro", longitudeCentro);
        latLng = new LatLng(intent.getDoubleExtra("latitudeCentro", latitudeCentro), intent.getDoubleExtra("longitudeCentro", longitudeCentro));

        edTxtDescricaoCad = (EditText) findViewById(R.id.edTxtDescricaoCad);
        edTxtDescricaoCad.setOnClickListener(this);

        edTxtDataInicioCad = (EditText) findViewById(R.id.edTxtDataInicioCad);
        edTxtDataInicioCad.setOnClickListener(this);

        edTxtDataTerminoCad = (EditText) findViewById(R.id.edTxtDataTerminoCad);
        edTxtDataTerminoCad.setOnClickListener(this);

        btnCadastrarEvento = (Button) findViewById(R.id.btnCadastrarEvento);
        btnCadastrarEvento.setOnClickListener(this);

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Por favor, aguarde...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
    }


    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btnCadastrarEvento){
            cadastrarEvento(latLng);
        }


    }

//    private void cadastrarEvento(){
//        String resultado;
//
//        //@Path("inserir/{descricao}/{logradouro}/{numero}/{cep}/{bairro}/{cidade}/{uf}/{pais}/{complemento}/{latitude}/{longitude}/{telefone}/{celular}/{email}/{nomeTipoEvento}/{organizador}")
//
//        try{
//            urlComplemento = new StringBuilder();
//            urlComplemento.append("evento/inserir/");
//            urlComplemento.append(edTxtDescricaoCad.getText());
//            urlComplemento.append("/");
//            urlComplemento.append("logradouro"); // logradouro
//            urlComplemento.append("/");
//            urlComplemento.append(1); // numero
//            urlComplemento.append("/");
//            urlComplemento.append(1); // cep
//            urlComplemento.append("/");
//            urlComplemento.append("bairro"); // bairro
//            urlComplemento.append("/");
//            urlComplemento.append("cidade"); // cidade
//            urlComplemento.append("/");
//            urlComplemento.append("uf"); // uf
//            urlComplemento.append("/");
//            urlComplemento.append("pais"); // pais
//            urlComplemento.append("/");
//            urlComplemento.append("complemento"); // complemento
//            urlComplemento.append("/");
//            urlComplemento.append(-1.0); // latitude
//            urlComplemento.append("/");
//            urlComplemento.append(-1.0); // longitude
//            urlComplemento.append("/");
//            urlComplemento.append(1); // telefone
//            urlComplemento.append("/");
//            urlComplemento.append(1); // celular
//            urlComplemento.append("/");
//            urlComplemento.append("email"); // email
//            urlComplemento.append("/");
//            urlComplemento.append("nometipoEvento"); // nomeTipoEvento
//            urlComplemento.append("/");
//            urlComplemento.append(organizador.getId()); // organizador
//            urlComplemento.append("/");
//
//            resultado = ar.get(urlComplemento.toString());
//            Log.i("JSON", resultado);
//            Toast.makeText(getApplicationContext(), "Cadastro de evento realizado com sucesso", Toast.LENGTH_SHORT).show();
//
//        }catch(Exception ex){
//            ex.getStackTrace();
//        }
//
//    }


    public void cadastrarEvento(LatLng latLng){
        Geocoder geocoder = new Geocoder(CadastroEventoActivity.this);

        List<Address> addresses;

        String logradouro;
        String numero;
        String cep ;
        String bairro;
        String cidade;
        String estado;
        String uf;
        String pais;
        String complemento;

        try {

            String descricao = Helper.codificar(edTxtDescricaoCad.getText().toString());

            try {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            } catch (IOException e) {
                addresses = new ArrayList<>();
            }

            if (addresses.size() > 0){
                logradouro = addresses.get(0).getThoroughfare();
                numero = "00";
                cep = addresses.get(0).getPostalCode();
                bairro = addresses.get(0).getSubLocality();
                cidade = addresses.get(0).getLocality();
                estado = addresses.get(0).getAdminArea();
                uf = addresses.get(0).getAdminArea();//"00"; //Helper.codificar(addresses.get(0).getAddressLine(3);
                pais = addresses.get(0).getCountryName();
                complemento = addresses.get(0).getAddressLine(0);
            } else {
                logradouro = "1";
                numero = "00";
                cep = "1";
                bairro = "1";
                cidade = "1";
                estado = "1";
                uf = "1";
                pais = "1";
                complemento = "1";
            }

            montarCadastroEvento(descricao,logradouro, cep, bairro, cidade, uf, pais, complemento, latLng);

            //} catch (IOException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void montarCadastroEvento(String descricao, String logradouro, String cep, String bairro, String cidade, String uf, String pais, String complemento, LatLng latLng){
        //@Path("inserir/{descricao}/{logradouro}/{numero}/{cep}/{bairro}/{cidade}/{uf}/{pais}/{complemento}/{latitude}/{longitude}/{telefone}/{celular}/{email}/{nomeTipoEvento}/{organizador}")
        Evento evento;
        Endereco endereco;
        Localizacao localizacao;
        Contato contato;
        TipoEvento tpEvento;
        Gson gson;
        try{
            evento = new Evento();
            endereco = new Endereco();
            localizacao = new Localizacao();
            contato = new Contato();
            tpEvento = new TipoEvento();

            urlComplemento = new StringBuilder();
            urlComplemento.append("evento/incluir/");
            evento.setDescricao(descricao); //descricao

            endereco.setLogradouro(logradouro); // logradouro
            endereco.setNumero(0);
            endereco.setCep(Integer.parseInt(cep.replace("-", "")));
            endereco.setBairro(bairro);
            endereco.setCidade(cidade);
            endereco.setUf(uf);
            endereco.setPais(pais);
            endereco.setComplemento(complemento);

            localizacao.setLatitude(Float.parseFloat(String.valueOf(latLng.latitude)));
            localizacao.setLongitude(Float.parseFloat(String.valueOf(latLng.longitude)));

            contato.setTelefone(1);
            contato.setCelular(1);
            contato.setEmail("Email");

            tpEvento.setNome("nomeTipoEvento");

            evento.setOrganizador(new Usuario());
            evento.getOrganizador().setId(organizador.getId());

            SimpleDateFormat sdf = new SimpleDateFormat();

            String dataInicial = sdf.format(new Date());
            String dataTermino = sdf.format(new Date());


            evento.setEndereco(endereco);
            evento.setLocalizacao(localizacao);
            evento.setContato(contato);
            evento.setTipoEvento(tpEvento);
//            evento.setDataInicio(sdf.parse(dataInicial));
//            evento.setDataTermino(sdf.parse(dataTermino));

            gson = new Gson();

            StringEntity entity = new StringEntity(gson.toJson(evento), "UTF-8");

            AcessoRest.put(getApplicationContext(),urlComplemento.toString(), entity, "application/json", new JsonHttpResponseHandler(){

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response){


                    String resultado = response.toString();

                    Gson gson = new Gson();

                    //GsonBuilder builder = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
                    //gson = builder.enableComplexMapKeySerialization().create();
                    //Evento eventoRetorno = gson.fromJson(resultado, Evento.class);
                    Log.i("JSON", resultado);
                    Toast.makeText(getApplicationContext(), "Cadastro de evento realizado com sucesso", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CadastroEventoActivity.this, NavigationDrawerActivity.class);
//                    if(organizador.getEventos() == null){
//                        organizador.setEventos(new ArrayList<Evento>());
//                    }
//                    organizador.getEventos().add(eventoRetorno);
                    intent.putExtra("usuario", organizador);
                    setResult(RESULT_OK, intent);
                    startActivity(intent);
                    prgDialog.dismiss();
                    finish();
                    super.onSuccess(statusCode, headers, response);
                }

            });

        }catch(Exception ex){
            ex.getStackTrace();
        }
    }
}
