package demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.impl.ExcelExporter;
import org.zkoss.zss.model.impl.ExcelImporter;

public class FileSystemBookService implements BookService {

	File folder;

	public FileSystemBookService(File folder) {
		this.folder = folder;
	}

	public FileSystemBookService(String folder) {
		this(new File(folder));
	}

	/**
	 * load the book base on the name and folder,
	 */
	public Book loadBook(String name) throws IOException {
		if (!folder.isDirectory()) {
			return null;
		}
		File f = new File(folder, name);
		if (!f.exists() || !f.isFile()) {
			return null;
		}
		ExcelImporter imp = new ExcelImporter();
		Book book = imp.imports(f);
		return book;
	}

	/**
	 * save the book base on the name and folder
	 */
	public void saveBook(String name, Book book) throws IOException {
		if (!folder.isDirectory()) {
			return;
		}
		File f = new File(folder, name);
		OutputStream os = null;
		try {
			os = new FileOutputStream(f);
			ExcelExporter exp = new ExcelExporter();
			exp.export(book, os);
		} finally {
			if (os != null) {
				os.close();
			}
		}

	}

}
