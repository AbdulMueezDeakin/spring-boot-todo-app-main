package co.sohamds.spring.todo.controllers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import co.sohamds.spring.todo.domain.Todo;
import co.sohamds.spring.todo.repository.TodoRepository;

@WebMvcTest(TodoController.class)
@Import(TestSecurityConfig.class) // Import the test security configuration
public class TodoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoRepository todoRepository;

    @Test
    @WithMockUser
    public void testIndex() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(view().name("index.html"));
    }

    @Test
    @WithMockUser
    public void testTodos() throws Exception {
        List<Todo> todos = Arrays.asList(new Todo(10L, "python ML", "its completed"));
        given(todoRepository.findAll()).willReturn(todos);

        mockMvc.perform(get("/todos"))
               .andExpect(status().isOk())
               .andExpect(view().name("todos"))
               .andExpect(model().attributeExists("todos"))
               .andExpect(model().attribute("todos", todos));
    }

    @Test
    @WithMockUser
    public void testAddTodo() throws Exception {
        mockMvc.perform(post("/todoNew")
               .param("todoItem", "New Task")
               .param("status", "No"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/todos"));

        verify(todoRepository).save(new Todo("New Task", "No"));
    }

    @Test
    @WithMockUser
    public void testDeleteTodo() throws Exception {
        mockMvc.perform(post("/todoDelete/{id}", 1L))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/todos"));

        verify(todoRepository).deleteById(1L);
    }

    @Test
    @WithMockUser
    public void testUpdateTodo() throws Exception {
        Todo todo = new Todo(1L, "Task", "No");
        given(todoRepository.findById(1L)).willReturn(Optional.of(todo));

        mockMvc.perform(post("/todoUpdate/{id}", 1L))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/todos"));

        verify(todoRepository).save(todo);
    }
}
