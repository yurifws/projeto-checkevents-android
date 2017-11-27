package projetockeckevents.novaroma.br.projetocheckevents;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import me.drakeet.materialdialog.MaterialDialog;
import projetockeckevents.novaroma.br.projetocheckevents.entities.Evento;
import projetockeckevents.novaroma.br.projetocheckevents.entities.Usuario;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   OnMapReadyCallback,
                   View.OnClickListener,
                GoogleApiClient.OnConnectionFailedListener,
                GoogleApiClient.ConnectionCallbacks,
                LocationListener {

    private static final String PREF_NAME = "LoginActivityPreferences";
    private static final int REQUEST_PERMISSIONS_CODE = 128;
    private MaterialDialog mMaterialDialog;

    //private MenuItem miCadastrarEvento;

    private GoogleMap googleMap;
    private MapFragment mapFragment;
    private Marker marker;

    private Evento eventoGlobal;

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationRequest mLocationRequest;


    private StringBuilder urlComplemento;

    private Intent intent;
    private Usuario usuarioLogado;
    private double latitudeCentro;
    private double longitudeCentro;

    // Progress Dialog Object
    private ProgressDialog prgDialog;
    // Error Msg TextView Object
    private TextView errorMsg;

    private List<Evento> listEventos;
    //private Collection<LatLng> listLatLgn;
    private Collection<Marker> listMarkers;

    private Button btnCadastrarEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        intent = getIntent();
        usuarioLogado = (Usuario) intent.getParcelableExtra("usuario");

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //miCadastrarEvento = (MenuItem) findViewById(R.id.miCadastrarEvento);

        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Por favor, aguarde...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        btnCadastrarEvento = (Button) findViewById(R.id.btnCadastrarEvento);
        btnCadastrarEvento.setOnClickListener(this);

//        carregarEventosCadastrados();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.miCadastrarEvento) {
//            if (usuarioLogado.isTipoUsuario())
//                chamarTelaCadastroEvento();
//            else
//                Toast.makeText(getApplicationContext(), "Permissoes de organizador necessárias para criação de eventos. ", Toast.LENGTH_SHORT).show();
//        } else
        if (id == R.id.miGerenciarConta) {
            chamarTelaAtualizacaoUsuario();
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
        } else if (id == R.id.miGerenciarEventos) {
            Intent intent = new Intent(NavigationDrawerActivity.this, AtualizacaoEventoActivity.class);
            startActivity(intent);

        } else if (id == R.id.miLogout) {
            fazerLogout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onMapReady(GoogleMap map) {

        double lat = -8.0655751;
        double lng = -34.9180396;

        googleMap = map;

        if (googleMap != null) {
            googleMap.clear();
//            carregarEventosCadastrados();
//            marcarEventosCadastradosNoMapa();
        }


        //MyLocation
        callAccessLocation();

        if(mGoogleApiClient != null)
            callConnection();

        if (mLocation != null) {
            lat = mLocation.getLatitude();
            lng = mLocation.getLongitude();
        }


        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16));

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
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {

            @Override
            public void onCameraMove() {
                btnCadastrarEvento.setVisibility(View.INVISIBLE);
            }
        });

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                btnCadastrarEvento.setVisibility(View.VISIBLE);

                try {
                    urlComplemento = new StringBuilder();
                    urlComplemento.append("evento/buscarPorDistancia/");

                    RequestParams params = new RequestParams();
                    params.put("km", 1.0);
                    params.put("lat", googleMap.getCameraPosition().target.latitude);
                    params.put("lng", googleMap.getCameraPosition().target.longitude);
                    latitudeCentro = googleMap.getCameraPosition().target.latitude;
                    longitudeCentro = googleMap.getCameraPosition().target.longitude;


                    AcessoRest.get(urlComplemento.toString(), params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            Gson gson = new Gson();

                            String resultado = response.toString();

                            GsonBuilder builder = new GsonBuilder().setDateFormat("dd-MM-yyyy");
                            gson = builder.enableComplexMapKeySerialization().create();

                            Type collectionType = new TypeToken<List<Evento>>() {
                            }.getType();
                            listEventos = new ArrayList<Evento>();
                            if (!resultado.equals("[]"))
                                listEventos = gson.fromJson(resultado, collectionType);
                            marcarEventosCadastradosNoMapa();
                            Log.i("JSON", resultado);
                            super.onSuccess(statusCode, headers, response);
                        }

                    });


                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                btnCadastrarEvento.setVisibility(View.VISIBLE);
            }
        });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //btnCadastrarEvento.setVisibility(View.INVISIBLE);
                urlComplemento = new StringBuilder();
                urlComplemento.append("evento/buscarPorLatLng/");

                RequestParams params = new RequestParams();
                params.put("latitude", marker.getPosition().latitude);
                params.put("longitude", marker.getPosition().longitude);

                AcessoRest.get(urlComplemento.toString(), params, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        Gson gson = new Gson();

                        String resultado = response.toString();
                        //Evento eventoClick = gson.fromJson(resultado, Evento.class);
                        eventoGlobal = gson.fromJson(resultado, Evento.class);
                        Log.i("JSON", resultado);
                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });

                marker.showInfoWindow();
                return true;
            }
        });

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {


            @Override
            public void onInfoWindowClick(Marker marker) {
                try {
                    urlComplemento = new StringBuilder();
                    urlComplemento.append("evento/buscarPorLatLng/");

                    RequestParams params = new RequestParams();
                    params.put("latitude", marker.getPosition().latitude);
                    params.put("longitude", marker.getPosition().longitude);

                    AcessoRest.get(urlComplemento.toString(), params, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            Gson gson = new Gson();


                            String resultado = response.toString();

                            GsonBuilder builder = new GsonBuilder().setDateFormat("dd-MM-yyyy");
                            gson = builder.enableComplexMapKeySerialization().create();

                            Evento eventoClick = gson.fromJson(resultado, Evento.class);
                            Log.i("JSON", resultado);
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }

                    });
                } catch (Exception ex) {
                    ex.getStackTrace();
                }


            }
        });

        initLocationRequest();

    }


    private void chamarTelaAtualizacaoUsuario() {
        try {
            Intent intent = new Intent(NavigationDrawerActivity.this, AtualizacaoUsuarioActivity.class);
            intent.putExtra("usuario", usuarioLogado);
            startActivityForResult(intent, 1);
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    private void chamarTelaCadastroEvento() {
        try {
            Intent intent = new Intent(NavigationDrawerActivity.this, CadastroEventoActivity.class);
            intent.putExtra("organizador", usuarioLogado);
            intent.putExtra("latitudeCentro", latitudeCentro);
            intent.putExtra("longitudeCentro", longitudeCentro);
            startActivity(intent);
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    private void fazerLogout() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().commit();
            this.finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void adicionaMarker(LatLng latLng, String title, String snippet) {
        MarkerOptions markerOptions = new MarkerOptions();
        //BitmapDescriptor bitMap = BitmapDescriptorFactory.fromResource(R.drawable.ic_local_bar_black_24dp);
        //.icon(bitMap)
        markerOptions.position(latLng)
                .title(title)
                .snippet(snippet)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker48));

        marker = googleMap.addMarker(markerOptions);
    }

    protected void limparMarcadores() {
        googleMap.clear();
    }

    public void callAccessLocation() {
        //Log.i(TAG, "callAccessLocation()");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(NavigationDrawerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                callDialog("É preciso a permission ACCESS_FINE_LOCATION para apresentação dos eventos locais.", new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_CODE);
            }
        } else {
            googleMap.setMyLocationEnabled(true);
        }
    }

    private void callDialog(String message, final String[] permissions) {
        mMaterialDialog = new MaterialDialog(this)
                .setTitle("Permission")
                .setMessage(message)
                .setPositiveButton("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ActivityCompat.requestPermissions(NavigationDrawerActivity.this, permissions, REQUEST_PERMISSIONS_CODE);
                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }

    private void carregarEventosCadastrados() {
        try {
            urlComplemento = new StringBuilder();
            urlComplemento.append("evento/buscarPorDistancia/");

            RequestParams params = new RequestParams();
            params.put("km", 1.0);
            params.put("lat", googleMap.getCameraPosition().target.latitude);
            params.put("lng", googleMap.getCameraPosition().target.longitude);

            AcessoRest.get(urlComplemento.toString(), params, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Gson gson = new Gson();


                    String resultado = response.toString();

                    GsonBuilder builder = new GsonBuilder().setDateFormat("dd-MM-yyyy");
                    gson = builder.enableComplexMapKeySerialization().create();

                    Type collectionType = new TypeToken<List<Evento>>() {
                    }.getType();
                    listEventos = new ArrayList<Evento>();
                    if (!resultado.equals("[]"))
                        listEventos = gson.fromJson(resultado, collectionType);
                    marcarEventosCadastradosNoMapa();
                    //marcarEventosCadastradosNoMapa();
                    Log.i("JSON", resultado);
                    super.onSuccess(statusCode, headers, response);
                }

            });


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void buscarLocalizacaoEvento() {

    }


    private void marcarEventosCadastradosNoMapa() {
        Gson gson = new Gson();
        if (listEventos != null) {
            limparMarcadores();
            for (int i = 0; i < listEventos.size(); i++) {
                //adicionaMarker(new LatLng(evento.getLocalizacao().getLatitude(), evento.getLocalizacao().getLongitude()), evento.getDescricao().toString(), evento.getTipoEvento().toString());
                Evento evento = ((Evento) listEventos.get(i));
                adicionaMarker(new LatLng(evento.getLocalizacao().getLatitude(), evento.getLocalizacao().getLongitude()), evento.getDescricao(), evento.getEndereco().getComplemento());

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Log.i(TAG, "test");
        switch (requestCode) {
            case REQUEST_PERMISSIONS_CODE:
                for (int i = 0; i < permissions.length; i++) {

                    if (permissions[i].equalsIgnoreCase(Manifest.permission.ACCESS_FINE_LOCATION)
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                        googleMap.setMyLocationEnabled(true);
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                usuarioLogado = data.getParcelableExtra("usuario");
            }
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                usuarioLogado = data.getParcelableExtra("usuario");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        intent = getIntent();
        usuarioLogado = (Usuario) intent.getParcelableExtra("usuario");

        if (googleMap != null) {
            double lat = latitudeCentro;
            double lng = longitudeCentro;
            googleMap.clear();

            if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
                startLocationUpdate();
            }

            if(mLocation != null){
                lat = mLocation.getLatitude();
                lng = mLocation.getLongitude();
            }

            //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 16));
            carregarEventosCadastrados();
            marcarEventosCadastradosNoMapa();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mGoogleApiClient != null){
            stopLocationUpdate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnCadastrarEvento) {
            if (usuarioLogado.isTipoUsuario()) {
                latitudeCentro = googleMap.getCameraPosition().target.latitude;
                longitudeCentro = googleMap.getCameraPosition().target.longitude;
                chamarTelaCadastroEvento();
            } else
                Toast.makeText(getApplicationContext(), "Permissoes de organizador necessárias para criação de eventos. ", Toast.LENGTH_SHORT).show();

        }
    }

    private synchronized void callConnection() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void initLocationRequest() {
        mLocationRequest = new LocationRequest().setInterval(5000)
                .setFastestInterval(2000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("startLocationUpdate", "Falta Permissão");
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, NavigationDrawerActivity.this);
    }

    private void stopLocationUpdate(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, NavigationDrawerActivity.this);
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("onConnected", "Conectou");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("onConnected", "Falta Permissão");
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        startLocationUpdate();
        Log.i("onLocationChanged", "Latitude: " + mLocation.getLatitude() + "/Longitude: " + mLocation.getLongitude());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("onConnectedSuspended", "Desconectou");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("onConnectedFailed", "Conexão Falhou: " + connectionResult);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("onLocationChanged", "Entrou");
        double latAnterior = mLocation.getLatitude();
        double lngAnterior = mLocation.getLongitude();
        if(location != null){
            Log.i("onLocationChanged", "Location não está nulo");
            Log.i("onLocationChanged", "Latitude Anterior: " + latAnterior + "/Longitude Anterior: " + lngAnterior);
            double latNovo = location.getLatitude();
            double lngNovo = location.getLongitude();
            Log.i("onLocationChanged", "Latitude Atualizado: " + latNovo + "/Longitude Atualizado: " + lngNovo);
            if(isMoveu(latAnterior, lngAnterior, latNovo, lngNovo)) {
                mLocation = location;
            }
        }
    }

    protected boolean isMoveu(double latAnterio, double lngAnterior, double latAtual, double lngAtual){
        return (Double.compare(latAnterio, latAtual) != 0) || (Double.compare(lngAnterior, lngAtual) != 0);
    }
}
