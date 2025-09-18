package fr.afpa.mvc.data;

import org.springframework.stereotype.Component;

import fr.afpa.mvc.model.Note;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Classe représentant une collection de notes.
 * Les permettent d'intéragir avec cette collection.
 * 
 * Classe annotée @Component :
 * cette annotation fait d'elle un bean qui sera instancié et disponible dans "ApplicationContext".
 * Cette instance pourra être utilisée n'importe où via le mécanisme d'injection de dépendance.
 * 
 * TODO ajoutez l'annotation @Component à cette classe pour qu'elle puisse être ajoutée au "ApplicationContext" et utilisé pour l'injection de dépendance
 */
@Component
public class Notes {
    /**
     * Table de hachage qui contiendra toutes les informations concernant les notes
     */
    private final Map<UUID, Note> notesMap = new HashMap<>();

    /**
     * @return Une liste de toutes les notes disponibles.
     */
    public List<Note> getAll(){
        var noteList = new ArrayList<>(notesMap.values());
        noteList.sort(Comparator.comparing(Note::getCreatedOn));
        return noteList;
    }

    /**
     * Ajoute une note à la collection.
     * Cette méthode initialise l'identifiant unique de la note ainsi que sa date de création.
     * 
     * @param note La note à enregistrer dans la collection.
     */
    public void add(Note note) {
        // initialisation de l'identifiant unique de la note
        note.setId(UUID.randomUUID());
        // initialisation de la date de création avec la date actuelle
        note.setCreatedOn(LocalDateTime.now());
        notesMap.put(note.getId(), note);
    }
}
