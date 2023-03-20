package edu.school21.restful.controllers;

import edu.school21.restful.DTO.CourseDTO;
import edu.school21.restful.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;

    @RequestMapping(value = "/{id}/publish", method = {RequestMethod.POST}, produces = {"application/hal+json"})
    public ResponseEntity<?> publishCourseById(@PathVariable(name = "id") Long id) throws URISyntaxException {
        CourseDTO findCourse = courseService.findById(id);
        if (!findCourse.isPublished()) {
            CourseDTO findCourseById = courseService.publishCourseById(id);
            Collection<CourseDTO> userModel = Collections.singleton(findCourseById);

            return new ResponseEntity<>(CollectionModel.of(userModel).withFallbackType(CourseDTO.class), HttpStatus.OK);
        }
        return new ResponseEntity<>("Курс уже опубликован!!! Нельзя опубликовать курс дважды!!!", HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{id}/publish", method = {RequestMethod.HEAD}, produces = {"application/hal+json"})
    public ResponseEntity<?> publishCourseByIdHead(@PathVariable(name = "id") Long id)
    {
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = {"application/hal+json"})
    public CollectionModel<CourseDTO> findUserById(@PathVariable(name = "id") Long id) {
        CourseDTO findCourse = courseService.findById(id);
        Link link = linkTo(CourseController.class).slash(id).withSelfRel();
        Link publishLink = linkTo(CourseController.class).slash(findCourse.getId()).slash("publish").withRel("publish");
        findCourse.add(link);
        findCourse.add(publishLink);
        Collection<CourseDTO> userModel = Collections.singleton(findCourse);
        return CollectionModel.of(userModel, publishLink).withFallbackType(CourseDTO.class);
    }
}
