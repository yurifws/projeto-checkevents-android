/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetockeckevents.novaroma.br.projetocheckevents.entities;


import android.os.Parcel;
import android.os.Parcelable;

public class Contato implements Parcelable{

    private Integer id;

    private int telefone;

    private int celular;

    private String email;

    private Usuario usuario;

    private Evento evento;


    public Contato() {
    }

    protected Contato(Parcel in) {
        id = in.readInt();
        telefone = in.readInt();
        celular = in.readInt();
        email = in.readString();
        usuario = in.readParcelable(Usuario.class.getClassLoader());
        evento = in.readParcelable(Evento.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(telefone);
        dest.writeInt(celular);
        dest.writeString(email);
        dest.writeParcelable(usuario, flags);
        dest.writeParcelable(evento, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Contato> CREATOR = new Creator<Contato>() {
        @Override
        public Contato createFromParcel(Parcel in) {
            return new Contato(in);
        }

        @Override
        public Contato[] newArray(int size) {
            return new Contato[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTelefone() {
        return telefone;
    }

    public void setTelefone(int telefone) {
        this.telefone = telefone;
    }

    public int getCelular() {
        return celular;
    }

    public void setCelular(int celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

}
