package edu.school21.restful.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.school21.restful.DTO.CourseDTO;
import edu.school21.restful.enums.CourseStatus;
import edu.school21.restful.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.RestMediaTypes;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureRestDocs("target/generated-snippets")
class CourseControllerTest {

    CourseDTO courseDTO;
    CourseDTO publishedCourse;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;

    @BeforeEach
    void init() {
        courseDTO = new CourseDTO();
        courseDTO.setCourseName("Geometry");
        courseDTO.setStatus(CourseStatus.DRAFT);
        courseDTO.setStartDate(LocalDate.of(2022, 10,10));
        courseDTO.setEndDate(LocalDate.of(2022, 10,22));
        courseDTO.setDescription("Test course 2");
        courseDTO.setPublished(false);
        courseDTO.setId(23L);

        publishedCourse =  new CourseDTO();
        publishedCourse.setCourseName("Geometry");
        publishedCourse.setStartDate(LocalDate.of(2022, 10,10));
        publishedCourse.setEndDate(LocalDate.of(2022, 10,22));
        publishedCourse.setDescription("Test course 2");
        publishedCourse.setId(23L);
        publishedCourse.setPublished(true);
        publishedCourse.setStatus(CourseStatus.PUBLISHED);
        when(courseService.publishCourseById(courseDTO.getId())).thenReturn(publishedCourse);
    }

    @Test
    void publishCourseByIdTest() throws Exception {
        when(courseService.findById(courseDTO.getId())).thenReturn(courseDTO);
        ResultActions resultActions = mockMvc.perform(post("/courses/{id}/publish", courseDTO.getId())
                .accept(RestMediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andDo(print());
        resultActions.andDo(document("publishCourseByIdTest",
                responseFields(
                fieldWithPath("_embedded.courseDTOes[].id").description("id of the course"),
                fieldWithPath("_embedded.courseDTOes[].courseName").description("The name of the course"),
                fieldWithPath("_embedded.courseDTOes[].description").description("The description of the course"),
                fieldWithPath("_embedded.courseDTOes[].startDate").description("The start date of the course"),
                fieldWithPath("_embedded.courseDTOes[].endDate").description("The end date of the course"),
                fieldWithPath("_embedded.courseDTOes[].status").description("The status of the course"),
                fieldWithPath("_embedded.courseDTOes[].published").description("The course is published or not")
                )));
    }

    @Test
    void publishCourseByIdErrorTest() throws Exception {
        when(courseService.findById(courseDTO.getId())).thenReturn(publishedCourse);
        ResultActions resultActions = mockMvc.perform(post("/courses/{id}/publish", publishedCourse.getId())
                        .accept(RestMediaTypes.HAL_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print());
        resultActions.andDo(document("publishCourseByIdErrorTest"));
    }

    @Test
    void findUserByITest() throws Exception {
        when(courseService.findById(courseDTO.getId())).thenReturn(courseDTO);
        ResultActions resultActions = mockMvc.perform(get("/courses/" + courseDTO.getId())
                .accept(RestMediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andDo(print());
        resultActions.andDo(document("findUserByIdTest"));
    }
}
