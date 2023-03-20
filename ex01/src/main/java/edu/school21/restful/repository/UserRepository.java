package edu.school21.restful.repository;

import edu.school21.restful.models.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
	User findByLogin(String login);
}

