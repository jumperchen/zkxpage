# Model Concept #
In ZK Spreadsheet, it uses a [Book](http://www.zkoss.org/javadoc/latest/zss/org/zkoss/zss/model/Book.html) as its data model. You can set the model using the function `spreadsheet.setSrc(file_path_of_excel)` and giving it the location to an excel file at `WebContent` or in the `ClassPath`. Otherwise, you can use `ExcelImporter` to import a book model from a `File` or a `InputStream`.
To save the book model, you can use `ExcelExporter` to export a book model to be a `OutputStream` so that you could store it in a `File` or to a database.
![http://zkxpage.googlecode.com/svn/trunk/docs/images/impexp1.png](http://zkxpage.googlecode.com/svn/trunk/docs/images/impexp1.png)

# Load Book #
To use an `ExcelImporter`, you have to create a new instance of the `ExcelImporter`, for example: new `ExcelImporter()`, or retrieve an instantiated object using `Importers.getImporter("excel")`. Following is an example of loading a book from a local file.
```
public Book loadBook(String name) throws IOException {
	File f = new File(folder, name);
	if (!f.exists() || !f.isFile()) {
		return null;
	}
	ExcelImporter imp = new ExcelImporter();
	Book book = imp.imports(f);
	return book;
}
```
It is also possible to load a book from a database or other places where you only need to prepare a `InputStream` and use `importer.imports(inputStream,bookName)` to import it.
# Save Book #
To use an `ExcelExporter`, you have to create a new instance of the `ExcelExporter` or retrieve an instantiated object using  `Exporters.getExporter("excel")`. The following shows an example of saving a book to a local file:
```
public void saveBook(String name, Book book) throws IOException {
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
```