package fr.afpa.mvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.afpa.mvc.data.Notes;
import fr.afpa.mvc.model.Note;
import fr.afpa.mvc.restcontrollers.NoteRestController;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NoteRestControllerTests {

    @MockBean
    private Notes notes;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    @DisplayName("Class is marked as @RestController")
    void classIsMarkedAsRestController() {
        var restControllerAnnotation = NoteRestController.class.getAnnotation(RestController.class);

        assertNotNull(restControllerAnnotation);
    }

    @Test
    @Order(2)
    @DisplayName("Base URL `/api/notes` is specified in @RequestMapping")
    void requestMappingIsSpecified() {
        var requestMappingAnnotation = NoteRestController.class.getAnnotation(RequestMapping.class);
        var urlMapping = extractUrlMapping(requestMappingAnnotation);

        assertThat(urlMapping).isEqualTo("/api/notes");
    }

    @Test
    @Order(3)
    @DisplayName("Method that get notes is marked with @GetMapping")
    void getMappingIsImplemented() {
        var foundMethodWithGetMapping = Arrays.stream(NoteRestController.class.getDeclaredMethods())
                .anyMatch(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(GetMapping.class))
                );

        assertTrue(foundMethodWithGetMapping);
    }

    @Test
    @Order(4)
    @DisplayName("Get method has no parameters")
    void getNotesMethodHasNoParameters() {
        var getNotesMethod = Arrays.stream(NoteRestController.class.getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(GetMapping.class))
                )
                .findAny()
                .orElseThrow();

        assertThat(getNotesMethod.getParameterCount()).isZero();
    }

    @Test
    @Order(5)
    @DisplayName("Get method returns stored notes as List<Note>")
    void getNotesMethodReturnsNoteList() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        var controller = new NoteRestController(notes);
        var noteList = givenNoteList();
        var getNotesMethod = Arrays.stream(NoteRestController.class.getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(GetMapping.class))
                )
                .findAny()
                .orElseThrow();

        var response = getNotesMethod.invoke(controller);

        assertThat(response).isEqualTo(noteList);
    }

    @Test
    @Order(6)
    @DisplayName("GET endpoint is completed ✅")
    void getNotes() throws Exception {
        List<Note> noteList = List.of(
                new Note("Test", "Hello, World!"),
                new Note("Greeting", "Hey")
        );
        when(notes.getAll()).thenReturn(noteList);

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[\n" +
                        "  {\n" +
                        "    \"title\": \"Test\",\n" +
                        "    \"text\": \"Hello, World!\"\n" +
                        "  },\n" +
                        "  {\n" +
                        "    \"title\": \"Greeting\",\n" +
                        "    \"text\": \"Hey\"\n" +
                        "  }\n" +
                        "]"));
    }

    @Test
    @Order(7)
    @DisplayName("Method that adds a note is marked with @PostMapping")
    void postMappingIsImplemented() {
        var foundMethodWithGetMapping = Arrays.stream(NoteRestController.class.getDeclaredMethods())
                .anyMatch(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(PostMapping.class))
                );

        assertTrue(foundMethodWithGetMapping);
    }

    @Test
    @Order(8)
    @DisplayName("Add method accepts Note as a parameter")
    void addNoteMethodAcceptsNewNoteAsParameter() {
        var addNoteMethod = Arrays.stream(NoteRestController.class.getDeclaredMethods())
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
    @Order(9)
    @DisplayName("Add method parameter note is marked with @RequestBody")
    void addNoteMethodParameterIsMarkedAsRequestBody() {
        var addNoteMethod = Arrays.stream(NoteRestController.class.getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(PostMapping.class))
                )
                .findAny()
                .orElseThrow();

        var noteParam = addNoteMethod.getParameters()[0];
        var requestBodyAnnotation = noteParam.getDeclaredAnnotation(RequestBody.class);

        assertNotNull(requestBodyAnnotation);
    }

    @Test
    @Order(10)
    @DisplayName("Add method returns ResponseEntity")
    void addNoteMethodReturnsVoid() {
        var addNoteMethod = Arrays.stream(NoteRestController.class.getDeclaredMethods())
                .filter(
                        method -> Arrays.stream(method.getDeclaredAnnotations())
                                .anyMatch(a -> a.annotationType().equals(PostMapping.class))
                )
                .findAny()
                .orElseThrow();

        var returnType = addNoteMethod.getReturnType();

        assertThat(returnType).isEqualTo(ResponseEntity.class);
    }

    @Test
    @Order(11)
    @DisplayName("Add method uses storage to add a new note")
    void addNotePassPostedNote() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        var note = new Note("Test", "Hello, World!");
        var controller = new NoteRestController(notes);
        var addNoteMethod = Arrays.stream(NoteRestController.class.getDeclaredMethods())
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
    @DisplayName("POST endpoint responds with error when fields are empty")
    void addNoteRespondWithClientErrorWhenFieldsAreEmpty() throws Exception {
        mockMvc.perform(post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(new Note()))
        )
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(13)
    @DisplayName("POST endpoint is completed ✅")
    void addNote() throws Exception {
        Note note = new Note("Test", "Hello, World!");

        mockMvc.perform(post("/api/notes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(note))
                ).andExpect(status().isCreated());
    }

    private String asJsonString(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    private String extractUrlMapping(RequestMapping requestMapping) {
        if (requestMapping.value().length > 0) {
            return requestMapping.value()[0];
        } else {
            return requestMapping.path()[0];
        }
    }

    private List<Note> givenNoteList() {
        List<Note> noteList = List.of(
                new Note("Test", "Hello, World!"),
                new Note("Greeting", "Hey")
        );
        when(notes.getAll()).thenReturn(noteList);
        return noteList;
    }
}

