package fr.afpa.mvc;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.afpa.mvc.data.Notes;
import fr.afpa.mvc.model.Note;
import fr.afpa.mvc.mvccontrollers.NoteController;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NoteControllerTests {
    @MockBean
    private Notes notes;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    @DisplayName("Class is marked as @Controller")
    void classIsMarkedAsController() {
        var controllerAnnotation = NoteController.class.getAnnotation(Controller.class);

        assertNotNull(controllerAnnotation);
    }

    @Test
    @Order(2)
    @DisplayName("Base URL `/notes` is specified in @RequestMapping")
    void requestMappingIsSpecified() {
        var requestMappingAnnotation = NoteController.class.getAnnotation(RequestMapping.class);
        var urlMapping = extractUrlMapping(requestMappingAnnotation);

        assertThat(urlMapping).isEqualTo("/notes");
    }
    @Test
    @Order(3)
    @DisplayName("Method that get notes is marked with @GetMapping")
    void getMappingIsImplemented() {
        var foundMethodWithGetMapping = Arrays.stream(NoteController.class.getDeclaredMethods())
                .anyMatch(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(GetMapping.class))
                );

        assertTrue(foundMethodWithGetMapping);
    }

    @Test
    @Order(4)
    @DisplayName("Get method accepts org.springframework.ui.Model as a parameter")
    void getNotesMethodAcceptsModelAsParameter() {
        var getNotesMethod = Arrays.stream(NoteController.class.getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(GetMapping.class))
                )
                .findAny()
                .orElseThrow();

        assertThat(getNotesMethod.getParameterCount()).isEqualTo(1);
        assertThat(getNotesMethod.getParameterTypes()[0]).isEqualTo(Model.class);
    }

    @Test
    @Order(5)
    @DisplayName("Get method returns view name \"notes\"")
    void getNotesMethodReturnsNotesViewName() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        var controller = new NoteController(notes);
        var getNotesMethod = Arrays.stream(controller.getClass().getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(GetMapping.class))
                )
                .findAny()
                .orElseThrow();

        var viewName = getNotesMethod.invoke(controller, new BindingAwareModelMap());

        assertThat(viewName).isEqualTo("notes");
    }

    @Test
    @Order(6)
    @DisplayName("Get method adds noteList attribute to the model")
    void getNotesMethodAddsNoteListToTheModel() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        var noteList = givenNoteList();
        var model = new BindingAwareModelMap();
        var controller = new NoteController(notes);
        var getNotesMethod = Arrays.stream(controller.getClass().getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(GetMapping.class))
                )
                .findAny()
                .orElseThrow();

        getNotesMethod.invoke(controller, model);

        assertThat(model.get("noteList")).isEqualTo(noteList);
    }

    @Test
    @Order(7)
    @DisplayName("GET endpoint is completed ✅")
    void getNotes() throws Exception {
        var noteList = givenNoteList();

        mockMvc.perform(get("/notes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("noteList"))
                .andExpect(model().attribute("noteList", noteList));
    }
    
    @Test
    @Order(8)
    @DisplayName("Method that adds a note is marked with @PostMapping")
    void postMappingIsImplemented() {
        var foundMethodWithGetMapping = Arrays.stream(NoteController.class.getDeclaredMethods())
                .anyMatch(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(PostMapping.class))
                );

        assertTrue(foundMethodWithGetMapping);
    }

    @Test
    @Order(9)
    @DisplayName("Add method accepts Note as a parameter")
    void addNoteMethodAcceptsNewNoteAsParameter() {
        var addNoteMethod = Arrays.stream(NoteController.class.getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(PostMapping.class))
                )
                .findAny()
                .orElseThrow();

        assertThat(addNoteMethod.getParameterCount()).isEqualTo(1);
        assertThat(addNoteMethod.getParameterTypes()[0]).isEqualTo(Note.class);

    }

    @Test
    @Order(10)
    @DisplayName("Add method returns \"redirect:/notes\"")
    void addNoteMethodReturnsRedirectToNotes() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        var controller = new NoteController(notes);
        var addNoteMethod = Arrays.stream(NoteController.class.getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(PostMapping.class))
                )
                .findAny()
                .orElseThrow();

        var response = addNoteMethod.invoke(controller, new Note("Test", "Hello, World!"));

        assertThat(response).isEqualTo("redirect:/notes");
    }

    @Test
    @Order(11)
    @DisplayName("Add method uses storage to add a note")
    void addNotePassPostedNote() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        var note = new Note("Test", "Hello, World!");
        var controller = new NoteController(notes);
        var addNoteMethod = Arrays.stream(NoteController.class.getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(PostMapping.class))
                )
                .findAny()
                .orElseThrow();


        addNoteMethod.invoke(controller, note);

        verify(notes).add(note);
    }
    
    @Test
    @Order(12)
    @DisplayName("POST endpoint is completed ✅")
    void addNote() throws Exception {
        Note note = new Note("Test", "Hello, World!");

        mockMvc.perform(post("/notes")
                .param("title", note.getTitle())
                .param("text", note.getText())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/notes"));
    }

    private String extractUrlMapping(RequestMapping requestMapping) {
        if (requestMapping.value().length > 0) {
            return requestMapping.value()[0];
        } else {
            return requestMapping.path()[0];
        }
    }

    private List<Note> givenNoteList() {
        Note note1 = new Note("Test", "Hello, World!");
        note1.setCreatedOn(LocalDateTime.now());
        Note note2 = new Note("Greeting", "Hey");
        note2.setCreatedOn(LocalDateTime.now());

        List<Note> noteList = List.of(note1, note2);
        when(notes.getAll()).thenReturn(noteList);
        return noteList;
    }
}
