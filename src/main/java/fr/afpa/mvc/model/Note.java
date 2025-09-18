package fr.afpa.mvc.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Classe représentant un note
 */
public class Note {
    /**
     * Identifiant unique de la note
     */
    private UUID id;
    /**
     * Titre de la note
     */
    private String title;
    /**
     * Texte contenu dans la note
     */
    private String text;
    /**
     * Date de création de la note
     */
    private LocalDateTime createdOn;

    public Note() {}

    public Note(String title, String text) {
        this.title = title;
        this.text = text;
    }

    /**
     * TODO implémenter la méthode qui sera appelée pour formatter la date correctement sur l'interface graphique
     * Exemple de format : 17/04/2025 - 10:30
     * 
     * @return La date correctement formatée
     */
    @JsonIgnore
    public String getStringFormatedCreatedOn() {
        // TODO formatter la date correctement
        // Documentation utile -> https://www.baeldung.com/java-datetimeformatter#4-datetimeformatter-using-locales
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu - H:m", Locale.FRENCH);
        return dateTimeFormatter.format(createdOn);
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
}
