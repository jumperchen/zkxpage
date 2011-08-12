package demo.service;

import java.io.IOException;

import org.zkoss.zss.model.Book;

public interface BookService {
	Book loadBook(String name) throws IOException;
	void saveBook(String name,Book book) throws IOException;
}
