package projetockeckevents.novaroma.br.projetocheckevents;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import projetockeckevents.novaroma.br.projetocheckevents.entities.Usuario;
import projetockeckevents.novaroma.br.projetocheckevents.util.Helper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MapFragment mapFragment;
    private Marker marker;
    private Collection<LatLng> listLatLgn;


    private StringBuilder urlComplemento;

    private Intent intent;
    private Usuario organizador;

    // Progress Dialog Object
    private ProgressDialog prgDialog;
    // Error Msg TextView Object
    private TextView errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        intent = getIntent();
        organizador = (Usuario) intent.getParcelableExtra("organizador");

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Por favor, aguarde...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
    }
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        listLatLgn = new ArrayList<LatLng>();
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-8.074718, -34.911587), 16));

        //Markers
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        //Eventos
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                if (marker != null)
                    marker.remove();
                adicionaMarker(new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude), "1", "1");
            }

        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //if (marker != null)
                //    marker.remove();
                adicionaMarker(latLng, "2", "2");
                listLatLgn.add(latLng);
                //getLocation(latLng);

            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

            }
        });


    }



    public void adicionaMarker(LatLng latLng, String title, String snippet){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng).title(title).snippet(snippet).draggable(true);

        marker = googleMap.addMarker(markerOptions);

    }

//    public void getLocation(LatLng latLng){
//        Geocoder geocoder = new Geocoder(MapsActivity.this);
//
//        try {
//            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//            String logradouro = Helper.codificar(addresses.get(0).getThoroughfare());
//            String numero = "1"; //Helper.codificar();
//            String cep = Helper.codificar(addresses.get(0).getPostalCode());
//            String bairro = Helper.codificar(addresses.get(0).getSubLocality());
//            String cidade = Helper.codificar(addresses.get(0).getLocality());
//            String estado = Helper.codificar(addresses.get(0).getAdminArea());
//            String uf = "1"; //Helper.codificar(addresses.get(0).getAddressLine(3);
//            String pais = Helper.codificar(addresses.get(0).getCountryName());
//            String complemento = Helper.codificar(addresses.get(0).getAddressLine(0));
//
//            cadastrarEvento(logradouro, cep, bairro, cidade, estado, uf, pais, complemento, latLng);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


//    private void cadastrarEvento(String logradouro, String cep, String bairro, String cidade, String estado, String uf, String pais, String complemento, LatLng latLng){
//        String resultado;
//
//        //@Path("inserir/{descricao}/{logradouro}/{numero}/{cep}/{bairro}/{cidade}/{uf}/{pais}/{complemento}/{latitude}/{longitude}/{telefone}/{celular}/{email}/{nomeTipoEvento}/{organizador}")
//
//        try{
//            urlComplemento = new StringBuilder();
//            urlComplemento.append("evento/inserir/");
//            urlComplemento.append("descricao"); //descricao
//            urlComplemento.append("/");
//            urlComplemento.append(logradouro); // logradouro
//            urlComplemento.append("/");
//            urlComplemento.append(1); // numero  urlComplemento.append(numero) falta tratar e pegar de location...
//            urlComplemento.append("/");
//            urlComplemento.append(1); // cep    urlComplemento.append(cep); precisa tratar antes de mandar...pois é string
//            urlComplemento.append("/");
//            urlComplemento.append(bairro); // bairro
//            urlComplemento.append("/");
//            urlComplemento.append(cidade); // cidade
//            urlComplemento.append("/");
//            urlComplemento.append(uf); // uf urlComplemento.append(uf) falta achar o adresses dele pela location
//            urlComplemento.append("/");
//            urlComplemento.append(pais); // pais
//            urlComplemento.append("/");
//            urlComplemento.append(complemento); // complemento
//            urlComplemento.append("/");
//            urlComplemento.append(latLng.latitude); // latitude
//            urlComplemento.append("/");
//            urlComplemento.append(latLng.longitude); // longitude
//            urlComplemento.append("/");
//            urlComplemento.append(1); // telefone
//            urlComplemento.append("/");
//            urlComplemento.append(1); // celular
//            urlComplemento.append("/");
//            urlComplemento.append("email"); // email
//            urlComplemento.append("/");
//            urlComplemento.append("nometipoEvento"); // nomeTipoEvento
//            urlComplemento.append("/");
//            urlComplemento.append(organizador.getId()); // organizador teste
//            urlComplemento.append("/");
//
//            //resultado = ar.get(urlComplemento.toString());
//            Log.i("JSON", resultado);
//            Toast.makeText(getApplicationContext(), "Cadastro de evento realizado com sucesso", Toast.LENGTH_SHORT).show();
//
//        }catch(Exception ex){
//            ex.getStackTrace();
//        }
//
//    }
}
