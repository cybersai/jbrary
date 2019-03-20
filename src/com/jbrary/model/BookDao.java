package com.jbrary.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDao {

    public static List<Book> all(){
        try(PreparedStatement statement = DBHelper.getInstance().prepare(Query.SELECT_ALL_BOOKS)) {
            ResultSet resultSet = statement.executeQuery();
            List<Book> books = new ArrayList<>();
            sqliteQueryToBook(resultSet, books);
            statement.close();
            return books;
        } catch (SQLException e) {
            System.out.println("An error occurred while trying to get all books");
            e.printStackTrace();
            return new ArrayList<Book>();
        }

    }

    private static void sqliteQueryToBook(ResultSet resultSet, List<Book> books) throws SQLException {
        while (resultSet.next()) {
            books.add(
                    new Book(
                            resultSet.getInt(Query.Book.ID_INDEX),
                            resultSet.getString(Query.Book.AUTHOR_INDEX),
                            resultSet.getString(Query.Book.TITLE_INDEX),
                            resultSet.getString(Query.Book.PUBLISHER_INDEX),
                            resultSet.getInt(Query.Book.YEAR_INDEX),
                            resultSet.getString(Query.Book.EDITION_INDEX),
                            resultSet.getInt(Query.Book.QUANTITY_INDEX)
                    )
            );
        }
    }

    public static void insert(Book book){
        try (PreparedStatement statement = DBHelper.getInstance().prepare(Query.INSERT_BOOK)) {
            bookToSqliteQuery(book, statement);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("An error occurred while trying add new book");
            e.printStackTrace();
        }
    }

    private static void bookToSqliteQuery(Book book, PreparedStatement statement) throws SQLException {
        statement.setString(1, book.getAuthor());
        statement.setString(2, book.getTitle());
        statement.setString(3, book.getPublisher());
        statement.setInt(4, book.getYear());
        statement.setString(5, book.getEdition());
        statement.setInt(6, book.getQuantity());
    }

    public static void update(Book book) {
        try(PreparedStatement statement = DBHelper.getInstance().prepare(Query.UPDATE_BOOK)) {
            bookToSqliteQuery(book, statement);
            statement.setInt(7, book.getId());
            statement.execute();
        } catch (SQLException e) {
            System.out.println("An error occurred while trying update book");
            e.printStackTrace();
        }
    }

    public static void delete(Book book) {
        try(PreparedStatement statement = DBHelper.getInstance().prepare(Query.DELETE_BOOK)) {
            statement.setInt(1, book.getId());
            statement.execute();
        } catch (SQLException e) {
            System.out.println("An error occurred while trying update book");
            e.printStackTrace();
        }
    }

    public static List<Book> searchByTitle(String title) {
        try(PreparedStatement statement = DBHelper.getInstance().prepare(Query.SEARCH_BOOK)) {
            List<Book> books = new ArrayList<>();
            statement.setString(1, "%" + title + "%");
            ResultSet resultSet =  statement.executeQuery();
            sqliteQueryToBook(resultSet, books);
            return books;
        } catch (SQLException e) {
            System.out.println("An error occurred while trying search for book");
            e.printStackTrace();
            return new ArrayList<Book>();
        }
    }

    public static void main(String[] args) throws SQLException {
        DBHelper.getInstance().open();
//        Book book = new Book(3,"Mavis Mensah", "Intro to C Again", "C++ Girls", 2018, "1st Edition", 125);
        List<Book> books = searchByTitle("C");
        books.stream().forEach(b -> System.out.println(b.getId() + ":" + b.getTitle()));
        DBHelper.getInstance().close();
    }
}
