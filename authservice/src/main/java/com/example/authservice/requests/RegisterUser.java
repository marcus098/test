package com.example.authservice.requests;

import jakarta.validation.constraints.*;

public class RegisterUser {
    @NotBlank(message = "Email obbligatoria")
    @Email(message = "Email non valida")
    private String email;
    @NotBlank(message = "Username obbligatorio")
    private String username;
    @NotBlank(message = "Password obbligatoria")
    private String password;
    @NotBlank(message = "Ruolo obbligatorio")
    @Pattern(regexp = "USER|SHOP", message = "Ruolo deve essere ROLE_USER o ROLE_SHOP") // todo da vedere nell'altro progetto
    private String role;  // "USER" o "SHOP"
    @NotBlank(message = "latitutine obbligatoria")
    private Double latitude;
    @NotBlank(message = "longitudine obbligatoria")
    private Double longitude;

    public RegisterUser() {

    }

    public @NotEmpty String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty String email) {
        this.email = email;
    }

    public @NotEmpty String getUsername() {
        return username;
    }

    public void setUsername(@NotEmpty String username) {
        this.username = username;
    }

    public @NotEmpty String getPassword() {
        return password;
    }

    public void setPassword(@NotEmpty String password) {
        this.password = password;
    }

    public @NotEmpty @Pattern(regexp = "USER|SHOP", message = "Errore, il ruolo deve essere USER o SHOP") String getRole() {
        return role;
    }

    public void setRole(@NotEmpty @Pattern(regexp = "USER|SHOP", message = "Errore, il ruolo deve essere USER o SHOP") String role) {
        this.role = role;
    }

    public @NotNull Double getLatitude() {
        return latitude;
    }

    public void setLatitude(@NotNull Double latitude) {
        this.latitude = latitude;
    }

    public @NotNull Double getLongitude() {
        return longitude;
    }

    public void setLongitude(@NotNull Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "RegisterUser{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
