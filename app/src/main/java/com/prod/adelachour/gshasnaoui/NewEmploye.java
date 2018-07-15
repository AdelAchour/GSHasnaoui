package com.prod.adelachour.gshasnaoui;


public class NewEmploye {

    String nom, prenom, telephone, email, dateNaissance, filiale, departement, poste;
    boolean isAdmin;

    public NewEmploye(String nom, String prenom, String telephone, String email, String dateNaissance, String poste, boolean isAdmin) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.dateNaissance = dateNaissance;
        //this.filiale = filiale;
        //this.departement = departement;
        this.poste = poste;
        this.isAdmin = isAdmin;
    }

    public NewEmploye(){

    }


}
