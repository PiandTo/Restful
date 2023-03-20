package edu.school21.restful.repository;

import edu.school21.restful.models.Lesson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LessonRepository extends PagingAndSortingRepository<Lesson, Long> {
    Page<Lesson> findByCourses_Id(long id, Pageable b);
}
