package ru.gurzhiy.crawler;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class DashBoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnOkResponce() throws Exception {
        this.mockMvc.perform(get("/dictionary_search/"))
                .andExpect(status().isOk())
                .andExpect(view().name("userForm"));
    }




}
