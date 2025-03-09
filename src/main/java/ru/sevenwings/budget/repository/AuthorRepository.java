package ru.sevenwings.budget.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sevenwings.budget.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
