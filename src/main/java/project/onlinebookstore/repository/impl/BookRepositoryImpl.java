package project.onlinebookstore.repository.impl;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import project.onlinebookstore.exception.DataProcessingException;
import project.onlinebookstore.model.Book;
import project.onlinebookstore.repository.BookRepository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private static final String FIND_ALL_BOOK_QUERY = "FROM Book";
    private final SessionFactory sessionFactory;

    @Autowired
    public BookRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't save entity book: "
                    + book + " to DB", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> allFromBookQuery =
                    session.createQuery(FIND_ALL_BOOK_QUERY, Book.class);
            return allFromBookQuery.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't find all books in DB", e);
        }
    }
}
