package com.practice.Rentread.Repository;

import com.practice.Rentread.Entities.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book,Long> {
}
