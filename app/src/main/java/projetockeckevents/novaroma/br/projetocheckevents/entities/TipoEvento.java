/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetockeckevents.novaroma.br.projetocheckevents.entities;


import android.os.Parcel;
import android.os.Parcelable;

public class TipoEvento implements Parcelable {

    private Integer id;

    private String nome;

    private Evento evento;


    public TipoEvento() {
    }

    protected TipoEvento(Parcel in) {
        id = in.readInt();
        nome = in.readString();
        evento = in.readParcelable(Evento.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nome);
        dest.writeParcelable(evento, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TipoEvento> CREATOR = new Creator<TipoEvento>() {
        @Override
        public TipoEvento createFromParcel(Parcel in) {
            return new TipoEvento(in);
        }

        @Override
        public TipoEvento[] newArray(int size) {
            return new TipoEvento[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    @Override
    public String toString() {
        return " TipoEvento[ id=" + this.getId() + " ]";
    }


}
