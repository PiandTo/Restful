package edu.school21.restful.restful;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.school21.restful.json.CourseRequest;
import edu.school21.restful.json.LessonRequest;
import edu.school21.restful.json.UserRequest;
import edu.school21.restful.jwt.JwtProvider;
import edu.school21.restful.models.Course;
import edu.school21.restful.models.DayOfWeek;
import edu.school21.restful.models.Role;
import edu.school21.restful.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class RestfulTestApplicationTests {

    private String token;
    private final JwtProvider provider;
    private final MockMvc mockMvc;

    @Autowired
    public RestfulTestApplicationTests(JwtProvider jwtProvider, MockMvc mockMvc) {
        this.provider = jwtProvider;
        this.mockMvc = mockMvc;

        User user = new User("Mikhail1", "Malev1", Role.ADMINISTRATOR, "snaomi1", "1");
        this.token = "Bearer " + provider.generateAccessToken(user);
    }

    @Test
    public void testGetUserById() throws Exception{

        UserRequest user = UserRequest.builder()
                .firstName("mikhail")
                .lastName("malev")
                .login("snaomi")
                .role(Role.STUDENT)
                .password("12345")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);

        MockHttpServletResponse response = mockMvc.perform(post("/users")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token))
//                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("mikhail"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("malev"))
                .andExpect(status().isCreated()).andReturn().getResponse();
        Integer id = JsonPath.parse(response.getContentAsString()).read("$.id");

        mockMvc.perform(get("/users/{id}", id).header("Authorization", token))
//                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("mikhail"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("malev"))
                .andExpect(status().isOk());
    }

    @Test
    public void testException() throws Exception {
        mockMvc.perform(get("/users/{id}", 100))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserRequest user = UserRequest.builder()
                .firstName("Petr")
                .lastName("Petrov")
                .login("snaomi")
                .role(Role.ADMINISTRATOR)
                .password("11111")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);

        mockMvc.perform(put("/users/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Petr"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Petrov"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUser() throws Exception {
        UserRequest user = UserRequest.builder()
                .firstName("Dmitriy")
                .lastName("Petrov")
                .login("Hemelia")
                .role(Role.TEACHER)
                .password("11111")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(user);

        MockHttpServletResponse response = (MockHttpServletResponse) mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Dmitriy"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Petrov"))
                .andExpect(status().isCreated()).andReturn().getResponse();

        Integer id = JsonPath.parse(response.getContentAsString()).read("$.id");

        mockMvc.perform(delete("/users/{id}", id)
                        .header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllCourses( ) throws Exception {
        mockMvc.perform(get("/courses").header("Authorization", token))
            .andExpect(status().isOk());
    }

    @Test
    public void addCourse( ) throws Exception {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("uuuu-MM-d");
        String text = date.format(formatters);
        LocalDate parsedDate = LocalDate.parse(text, formatters);

        CourseRequest user = CourseRequest.builder()
                .courseName("mikhail")
                .description("malev")
                .startDate(parsedDate)
                .endDate(parsedDate)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String json = objectMapper.writeValueAsString(user);

        MockHttpServletResponse response = mockMvc.perform(post("/courses")
                        .header("Authorization", token)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.courseName").value("mikhail"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("malev"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value(parsedDate.format(formatters)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate").value(parsedDate.format(formatters)))
                .andExpect(status().isCreated()).andReturn().getResponse();

        Integer id = JsonPath.parse(response.getContentAsString()).read("$.id");

        mockMvc.perform(get("/courses/{id}", id).header("Authorization", token))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.courseName").value("mikhail"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("malev"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value(parsedDate.format(formatters)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate").value(parsedDate.format(formatters)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateCourse( ) throws Exception {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("uuuu-MM-d");
        String text = date.format(formatters);
        LocalDate parsedDate = LocalDate.parse(text, formatters);

        CourseRequest course = CourseRequest.builder()
                .courseName("mikhail")
                .description("malev")
                .startDate(parsedDate)
                .endDate(parsedDate)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String json = objectMapper.writeValueAsString(course);

        MockHttpServletResponse response = mockMvc.perform(post("/courses")
                        .header("Authorization", token)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.courseName").value("mikhail"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("malev"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value(parsedDate.format(formatters)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate").value(parsedDate.format(formatters)))
                .andExpect(status().isCreated()).andReturn().getResponse();

        Integer id = JsonPath.parse(response.getContentAsString()).read("$.id");

        course.setCourseName("Biography");
        json = objectMapper.writeValueAsString(course);

        mockMvc.perform(put("/courses/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header("Authorization", token))
//                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.courseName").value("Biography"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("malev"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value(parsedDate.format(formatters)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate").value(parsedDate.format(formatters)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCourse( ) throws Exception {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("uuuu-MM-d");
        String text = date.format(formatters);
        LocalDate parsedDate = LocalDate.parse(text, formatters);

        CourseRequest user = CourseRequest.builder()
                .courseName("mikhail")
                .description("malev")
                .startDate(parsedDate)
                .endDate(parsedDate)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String json = objectMapper.writeValueAsString(user);

        MockHttpServletResponse response = mockMvc.perform(post("/courses")
                        .header("Authorization", token)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.courseName").value("mikhail"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("malev"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value(parsedDate.format(formatters)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endDate").value(parsedDate.format(formatters)))
                .andExpect(status().isCreated()).andReturn().getResponse();

        Integer id = JsonPath.parse(response.getContentAsString()).read("$.id");

        mockMvc.perform(delete("/courses/{id}", id).header("Authorization", token))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetLessonsfromCourseId() throws Exception {
            mockMvc.perform(
                    get("/courses/20/lessons?pageSize=2")
                            .header("Authorization", token))
                    .andDo(print())
                    .andExpect(status().isOk());
    }

    @Test
    public void testAddLessonToCourse() throws Exception {
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("H:mm:ss");
        String text = time.format(formatters);
        LocalTime parsedDate = LocalTime.parse(text, formatters);

        LessonRequest lessonRequest = LessonRequest.builder()
                .dayOfWeek(DayOfWeek.FRIDAY)
                .startTime(parsedDate)
                .endTime(parsedDate)
                .teacherId(60)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String json = objectMapper.writeValueAsString(lessonRequest);

        mockMvc.perform(
                        post("/courses/20/lessons")
                                .header("Authorization", token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void updateLesson() throws Exception {
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("H:mm:ss");
        String text = time.format(formatters);
        LocalTime parsedDate = LocalTime.parse(text, formatters);

        LessonRequest lessonRequest = LessonRequest.builder()
                .dayOfWeek(DayOfWeek.FRIDAY)
                .startTime(parsedDate)
                .endTime(parsedDate)
                .teacherId(60)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        String json = objectMapper.writeValueAsString(lessonRequest);

        MockHttpServletResponse m = mockMvc.perform(
                        post("/courses/20/lessons")
                                .header("Authorization", token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andDo(print())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dayOfWeek").value("FRIDAY"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.startTime").value(parsedDate))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.endTime").value(parsedDate))
                .andExpect(status().isCreated()).andReturn().getResponse();

        Integer id = JsonPath.parse(m.getContentAsString()).read("$.id");

        lessonRequest.setTeacherId(70);
        lessonRequest.setDayOfWeek(DayOfWeek.MONDAY);
        json = objectMapper.writeValueAsString(lessonRequest);

        mockMvc.perform(
                put("/courses/20/lessons/{id}", id)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dayOfWeek").value("MONDAY"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.startTime").value(parsedDate))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.endTime").value(parsedDate))
                .andExpect(status().isOk());
    }

}
