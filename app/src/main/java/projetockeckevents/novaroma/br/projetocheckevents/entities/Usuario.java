/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetockeckevents.novaroma.br.projetocheckevents.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Usuario implements Parcelable{


    private Integer id;

    private String login;

    private String senha;

    private boolean tipoUsuario;

    private String nome;

    private String sobrenome;

    private String sexo;

    private Contato contato;

    private List<Evento> eventos;

    private Evento evento;

    public Usuario() {
    }

    public Usuario(int id, String login, String senha) {
        this.id = id;
        this.login = login;
        this.senha = senha;
    }

    protected Usuario(Parcel in) {
        id = in.readInt();
        login = in.readString();
        senha = in.readString();
        tipoUsuario = in.readByte() != 0;
        nome = in.readString();
        sobrenome = in.readString();
        sexo = in.readString();
        contato = in.readParcelable(Contato.class.getClassLoader());
        evento = in.readParcelable(Evento.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(login);
        dest.writeString(senha);
        dest.writeByte((byte) (tipoUsuario ? 1 : 0));
        dest.writeString(nome);
        dest.writeString(sobrenome);
        dest.writeString(sexo);
        dest.writeParcelable(contato, flags);
        dest.writeParcelable(evento, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public boolean isTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(boolean tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

}
