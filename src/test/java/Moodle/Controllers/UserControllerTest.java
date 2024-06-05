package Moodle.Controllers;

import Moodle.Model.Role;
import Moodle.Model.Users;
import Moodle.Repositories.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
//@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvs;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UsersRepository usersRepository;

    @Test
    void getUser() throws Exception{
        //given
        Users user1 = new Users();
        user1.setRole(Role.admin);
        user1.setId(20);
        user1.setMail("root2@gmail.com");
        user1.setName("name");
        user1.setSurname("surname");
        user1.setPassword("df");
        user1.setIsAccountBlocked(false);
        usersRepository.save(user1);
        //when
        MvcResult mvcResult = mockMvs.perform(MockMvcRequestBuilders.get("/get/user/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
    //.andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)));
        //then
        Users user = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Users.class);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getMail()).isEqualTo("root@gmail.com");
        assertThat(user.getRole()).isEqualTo(Role.admin);
    }

}