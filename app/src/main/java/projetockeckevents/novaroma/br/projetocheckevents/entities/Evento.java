/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetockeckevents.novaroma.br.projetocheckevents.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collection;
import java.util.Date;


public class Evento implements Parcelable {

    private Integer id;

    private String descricao;

    private Date dataInicio;

    private Date dataTermino;

    private Endereco endereco;

    private Localizacao localizacao;

    private Contato contato;

    private TipoEvento tipoEvento;

    private Usuario organizador;

    public Evento() {
    }

    public Evento(String descricao, Date dataInicio, Date dataTermino, Endereco endereco, Localizacao localizacao, Contato contato, TipoEvento tipoEvento, Usuario organizador) {
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataTermino = dataTermino;
        this.endereco = endereco;
        this.localizacao = localizacao;
        this.contato = contato;
        this.tipoEvento = tipoEvento;
        this.organizador = organizador;
    }

    protected Evento(Parcel in) {
        id = in.readInt();
        descricao = in.readString();
        endereco = in.readParcelable(Endereco.class.getClassLoader());
        localizacao = in.readParcelable(Localizacao.class.getClassLoader());
        contato = in.readParcelable(Contato.class.getClassLoader());
        tipoEvento = in.readParcelable(TipoEvento.class.getClassLoader());
        organizador = in.readParcelable(Usuario.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(descricao);
        dest.writeParcelable(endereco, flags);
        dest.writeParcelable(localizacao, flags);
        dest.writeParcelable(contato, flags);
        dest.writeParcelable(tipoEvento, flags);
        dest.writeParcelable(organizador, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Evento> CREATOR = new Creator<Evento>() {
        @Override
        public Evento createFromParcel(Parcel in) {
            return new Evento(in);
        }

        @Override
        public Evento[] newArray(int size) {
            return new Evento[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    @Override
    public String toString() {
        return " Evento[ id=" + this.getId() + " ]";
    }


    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public Usuario getOrganizador() {
        return organizador;
    }

    public void setOrganizador(Usuario organizador) {
        this.organizador = organizador;
    }





}
