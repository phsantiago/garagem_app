package com.garagem.nupark.dto;

import java.util.Date;


public class GaragemDto {
	private int id_garagem;
	private Date dt_registro;
	private Date dt_delete;
	private boolean deleted;
	private double latitude;
	private double longitude;
	private String titulo;
	private String descricao;
	private int id_usuario_dono;

	public GaragemDto(){
	}

	public int getId_garagem() {
		return id_garagem;
	}
	public void setId_garagem(int id_garagem) {
		this.id_garagem = id_garagem;
	}
	public Date getDt_registro() {
		return dt_registro;
	}
	public void setDt_registro(Date dt_registro) {
		this.dt_registro = dt_registro;
	}
	public Date getDt_delete() {
		return dt_delete;
	}
	public void setDt_delete(Date dt_delete) {
		this.dt_delete = dt_delete;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public int getId_usuario_dono() {
		return id_usuario_dono;
	}
	public void setId_usuario_dono(int id_usuario_dono) {
		this.id_usuario_dono = id_usuario_dono;
	}


}