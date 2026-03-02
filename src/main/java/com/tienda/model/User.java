package com.tienda.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

@Entity
@Table(name="usuarios")
public class User  implements UserDetails{
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "nombre")
	private String name;
	@Column(name = "email")
	private String email;
	@Column(name = "password")
	private String password;
	
	public enum Rol{ ADMIN, COMPRADOR}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "rol")
	private Rol rol;
	
	@Column(name = "fecha_registro")
	private LocalDateTime register_date;
	
	@OneToMany(mappedBy = "user")
	private List<Sale> sales;

	public List<Sale> getSales() {
		return sales;
	}

	public void setSales(List<Sale> sales) {
		this.sales = sales;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public LocalDateTime getRegister_date() {
		return register_date;
	}

	public void setRegister_date(LocalDateTime register_date) {
		this.register_date = register_date;
	}

	public List<Sale> getSells() {
		return sales;
	}

	public void setSells(List<Sale> sales) {
		this.sales = sales;
	}	
	
	// MÉTODOS DE USERDETAILS

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // Spring Security necesita una lista de "autoridades"
        List<GrantedAuthority> authorities = new ArrayList<>();

        // IMPORTANTE: los roles deben empezar por "ROLE_"
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.rol));

        return authorities;    
	}
	
    @Override
    public String getUsername() {
        return this.name; // Este será el identificador del usuario
    }

    // Estos métodos normalmente devuelven true
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

}
