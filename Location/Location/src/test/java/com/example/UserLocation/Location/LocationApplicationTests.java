package com.example.UserLocation.Location;

import com.example.UserLocation.Location.controller.UserController;
import com.example.UserLocation.Location.entity.User;
import com.example.UserLocation.Location.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class LocationApplicationTests {

	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
	}

	@Test
	public void testGetNearestUsers() throws Exception {
		User referenceUser = new User();
		referenceUser.setId(1L);
		referenceUser.setName("Reference User");
		referenceUser.setLatitude(0.0);
		referenceUser.setLongitude(0.0);

		User user1 = new User();
		user1.setId(2L);
		user1.setName("User 1");
		user1.setLatitude(0.1);
		user1.setLongitude(0.1);

		User user2 = new User();
		user2.setId(3L);
		user2.setName("User 2");
		user2.setLatitude(0.2);
		user2.setLongitude(0.2);

		List<User> nearestUsers = new ArrayList<>();
		nearestUsers.add(user1);
		nearestUsers.add(user2);

		when(userService.getNearestUsers(2)).thenReturn(nearestUsers);
		when(userService.getUserById(1L)).thenReturn(Optional.of(referenceUser));

		mockMvc.perform(get("/get_users/2"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id").value(2))
				.andExpect(jsonPath("$[0].name").value("User 1"))
				.andExpect(jsonPath("$[0].latitude").value(0.1))
				.andExpect(jsonPath("$[0].longitude").value(0.1))
				.andExpect(jsonPath("$[1].id").value(3))
				.andExpect(jsonPath("$[1].name").value("User 2"))
				.andExpect(jsonPath("$[1].latitude").value(0.2))
				.andExpect(jsonPath("$[1].longitude").value(0.2));
	}
}
