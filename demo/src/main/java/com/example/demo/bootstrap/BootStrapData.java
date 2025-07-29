package com.example.demo.bootstrap;

import com.example.demo.domain.Author;
import com.example.demo.domain.Book;
import com.example.demo.domain.Publisher;
import com.example.demo.repositories.AuthorRepository;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.PublisherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;


@Component
public class BootStrapData implements CommandLineRunner {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;

    public BootStrapData(AuthorRepository authorRepository, BookRepository bookRepository, PublisherRepository publisherRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // --- Create Publisher ---
        Publisher publisher = new Publisher("SFG Publishing",
                "3135 1st Ave N Suite 14481",
                "St. Petersberg",
                "FL",
                "33784");
        publisherRepository.save(publisher);
        System.out.println("Publisher count: " + publisherRepository.count());

        // --- Create Author + Book Pair 1 ---
        Author eric = new Author("Eric", "Evans", new HashSet<>());
        Book ddd = new Book("Domain Driven Design", "123123", new HashSet<>());
        ddd.setPublisher(publisher);
        //settitng publisher to book
        ddd.getAuthors().add(eric);
        //adding author to book
        eric.getBooks().add(ddd);
        //adding book to author
        authorRepository.save(eric);
        bookRepository.save(ddd);
        publisher.getBooks().add(ddd);


        // --- Create Author + Book Pair 2 ---
        Author john = new Author("John", "Smith", new HashSet<>());
        Book wingsOfFire = new Book("Wings of Fire", "1234567891", new HashSet<>());
        wingsOfFire.setPublisher(publisher);
        //adding publisher to books
        wingsOfFire.getAuthors().add(john);
        //adding author to books
        john.getBooks().add(wingsOfFire);
        //adding books to author

        publisher.getBooks().add(wingsOfFire);
        authorRepository.save(john);
        bookRepository.save(wingsOfFire);
        publisherRepository.save(publisher);
        // --- Final logs ---
        System.out.println("Started in Bootstrap");
        System.out.println("Number of Books: " + bookRepository.count());
        // Re-fetch publisher to get updated book list if needed
        Publisher savedPublisher = publisherRepository.findById(publisher.getId()).orElse(null);
        if (savedPublisher != null) {
            System.out.println("Publisher number of books: " + savedPublisher.getBooks().size());
        }
    }
}
