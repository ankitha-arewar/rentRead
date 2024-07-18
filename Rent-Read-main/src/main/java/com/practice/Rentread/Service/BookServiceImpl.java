package com.practice.Rentread.Service;

import com.practice.Rentread.Entities.Book;
import com.practice.Rentread.Entities.User;
import com.practice.Rentread.Exceptions.BookNotAvailableException;
import com.practice.Rentread.Exceptions.BookNotFoundException;
import com.practice.Rentread.Exceptions.UserRentCapacityFullException;
import com.practice.Rentread.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements  BookService{

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserService userService;

    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book getBookById(Long bookId) throws BookNotFoundException{
        if(bookRepository.findById(bookId).isEmpty()) throw new BookNotFoundException("Book Id not found");
        return bookRepository.findById(bookId).get();
    }

    @Override
    public List<Book> getAllBooks() {
        Iterable<Book> booksIterable = bookRepository.findAll();
        List<Book> books = new ArrayList<>();
        for(Book book : booksIterable) books.add(book);
        return books;
    }

    @Override
    public Book updateBookById(Book book, Long bookId) {
        if(bookRepository.findById(bookId).isEmpty()) throw new BookNotFoundException("Book Id not found");
        Book bookToUpdate = bookRepository.findById(bookId).get();
        bookToUpdate.setAuthor(book.getAuthor());
        bookToUpdate.setAvailabilityStatus(book.isAvailabilityStatus());
        bookToUpdate.setGenre(book.getGenre());
        bookToUpdate.setTitle(book.getTitle());
        return bookRepository.save(bookToUpdate);
    }

    @Override
    public void deleteBookById(Long bookId) throws BookNotFoundException {
        if(bookRepository.findById(bookId).isEmpty()) throw new BookNotFoundException("Book Id not found");
        bookRepository.deleteById(bookId);
    }

    @Override
    public boolean checkisBookAvaliable(Long bookId)throws BookNotFoundException{
        if(bookRepository.findById(bookId).isEmpty()) throw new BookNotFoundException("Book Id not found");
        Book book = bookRepository.findById(bookId).get();
        if(book.isAvailabilityStatus() == true)return true;
        return false;
    }

    @Override
    public void rentBook(Long bookId, String email) throws BookNotAvailableException, UserRentCapacityFullException, UsernameNotFoundException, BookNotFoundException{
        if(userService.findUserByEmail(email).isEmpty()) throw new UsernameNotFoundException("User does not exist");
        if(bookRepository.findById(bookId).isEmpty()) throw new BookNotFoundException("Book Id not found");
        if(checkisBookAvaliable(bookId) == false) throw new BookNotAvailableException("Book is not available for rent");

        User user = userService.findUserByEmail(email).get();
        if(user.getBook1() != -1 && user.getBook2() != -1) throw new UserRentCapacityFullException(email + " already has maximum book holding");
        Book book = bookRepository.findById(bookId).get();

        book.setAvailabilityStatus(false);
        updateBookById(book, book.getBookId());

        if(user.getBook1() == -1L) user.setBook1(book.getBookId());
        else if(user.getBook2() == -1L) user.setBook2(book.getBookId());

        userService.save(user);
    }

    @Override
    public void returnBook(Long bookId, String email)throws UsernameNotFoundException, BookNotFoundException{
        if(userService.findUserByEmail(email).isEmpty()) throw new UsernameNotFoundException("User does not exception");
        if(bookRepository.findById(bookId).isEmpty()) throw new BookNotFoundException("Book Id not found");

        User user = userService.findUserByEmail(email).get();
        if(user.getBook1() == bookId) user.setBook1(-1L);
        else if(user.getBook2() == bookId) user.setBook2(-1L);

        Book book = bookRepository.findById(bookId).get();
        book.setAvailabilityStatus(true);
        updateBookById(book,bookId);
        userService.save(user);
    }
}
