/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetockeckevents.novaroma.br.projetocheckevents.entities;


import android.os.Parcel;
import android.os.Parcelable;

public class Endereco implements Parcelable{

    private Integer id;

    private String logradouro;

    private int numero;

    private int cep;

    private String bairro;

    private String cidade;

    private String uf;

    private String pais;

    private String complemento;

    private Evento evento;

    public Endereco() {
    }

    protected Endereco(Parcel in) {
        id = in.readInt();
        logradouro = in.readString();
        numero = in.readInt();
        cep = in.readInt();
        bairro = in.readString();
        cidade = in.readString();
        uf = in.readString();
        pais = in.readString();
        complemento = in.readString();
        evento = in.readParcelable(Evento.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(logradouro);
        dest.writeInt(numero);
        dest.writeInt(cep);
        dest.writeString(bairro);
        dest.writeString(cidade);
        dest.writeString(uf);
        dest.writeString(pais);
        dest.writeString(complemento);
        dest.writeParcelable(evento, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Endereco> CREATOR = new Creator<Endereco>() {
        @Override
        public Endereco createFromParcel(Parcel in) {
            return new Endereco(in);
        }

        @Override
        public Endereco[] newArray(int size) {
            return new Endereco[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCep() {
        return cep;
    }

    public void setCep(int cep) {
        this.cep = cep;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }


    @Override
    public String toString() {
        return " Endereco[ id=" + this.getId() + " ]";
    }

}
