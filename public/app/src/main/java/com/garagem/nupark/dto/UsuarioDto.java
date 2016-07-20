package com.garagem.nupark.dto;


public class UsuarioDto extends RetornoDto {
	private int idUsuario;
	private String email;
	private String nome;
	private int facebookId;
	private String salt;
	private String senha;

	private String telefone;

	public UsuarioDto(){
	}
	
	public int getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public int getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(int facebookId) {
		this.facebookId = facebookId;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Override
	public String toString() {
		return "UsuarioDto [idUsuario=" + idUsuario + ", email=" + email + ", nome=" + nome + ", facebookId="
				+ facebookId + ", telefone=" + telefone + "]";
	}
	
	
}
