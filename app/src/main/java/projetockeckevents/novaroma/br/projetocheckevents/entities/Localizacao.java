/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetockeckevents.novaroma.br.projetocheckevents.entities;


import android.os.Parcel;
import android.os.Parcelable;

public class Localizacao implements Parcelable{

    private Integer id;

    private float latitude;

    private float longitude;

    private Evento evento;

    public Localizacao() {
    }

    protected Localizacao(Parcel in) {
        id = in.readInt();
        latitude = in.readFloat();
        longitude = in.readFloat();
        evento = in.readParcelable(Evento.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeFloat(latitude);
        dest.writeFloat(longitude);
        dest.writeParcelable(evento, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Localizacao> CREATOR = new Creator<Localizacao>() {
        @Override
        public Localizacao createFromParcel(Parcel in) {
            return new Localizacao(in);
        }

        @Override
        public Localizacao[] newArray(int size) {
            return new Localizacao[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    @Override
    public String toString() {
        return " Localizacao[ id=" + this.getId() + " ]";
    }

}
