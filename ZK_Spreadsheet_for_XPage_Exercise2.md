In this exercise, we will show you how to load and save excel files from File on the Server File System and then download those excel files to the client side.

## Create XPages ##
  1. Create Xpages in your database
  1. Add four buttons labeled "clean", "load", "save" and "download" to the page.
  1. Add a `messages` component to show the message of action at runtime.
  1. Add a `spreadsheetRich` to the page.
  1. Create an `empty.xlsx` file in `WebContent` as a default template of `spreadsheetRich`.
  1. Add action to the button "clean" by invoking `loadSaver.doClean()` and set a partial update ID to messages.
  1. Add action to the button "load" by invoking `loadSaver.doLoad()` and set a partial update ID to messages.
  1. Add action to the button "save" by invoking `loadSaver.doSave()` and set a partial update ID to messages.
  1. Add action to the button "download" button by invoking `loadSaver.doDownload()` and set a partial update ID to messages.


Page Preview:<br />![http://zkxpage.googlecode.com/svn/trunk/docs/images/ex2_1.png](http://zkxpage.googlecode.com/svn/trunk/docs/images/ex2_1.png)

The source code:
```
<?xml version="1.0" encoding="UTF-8"?>
<xp:view xmlns:xp="http://www.ibm.com/xsp/core" xmlns:h="http://java.sun.com/jsf/html"
	xmlns:zk="http://www.zkoss.org/xpage" style="height:100%;width:100%">
	This example saves and loads excel from a folder on the server (in this demo,
	c:/temp/loadSaver.xlsx) 
	<table>
		<tr>
			<td>
				<xp:button id="button1" value="Clean">
					<xp:eventHandler event="onclick" submit="true"
						refreshMode="partial" refreshId="messages">
						<xp:this.action>
							<xp:executeScript script="#{javascript:loadSaver.doClean()}">
							</xp:executeScript>
						</xp:this.action>
					</xp:eventHandler>
				</xp:button>
			</td>		
			<td>
				<xp:button id="button2" value="Load">
					<xp:eventHandler event="onclick" submit="true"
						refreshMode="partial" refreshId="messages">
						<xp:this.action>
							<xp:executeScript script="#{javascript:loadSaver.doLoad()}">
							</xp:executeScript>
						</xp:this.action>
					</xp:eventHandler>
				</xp:button>
			</td>
			<td>
				<xp:button id="button3" value="Save">
					<xp:eventHandler event="onclick" submit="true"
						refreshMode="partial" refreshId="messages">
						<xp:this.action>
							<xp:executeScript script="#{javascript:loadSaver.doSave()}">
							</xp:executeScript>
						</xp:this.action>
					</xp:eventHandler>
				</xp:button>
			</td>
			<td>
				<xp:button id="button4" value="Download">
					<xp:eventHandler event="onclick" submit="true"
						refreshMode="partial" refreshId="messages">
						<xp:this.action>
							<xp:executeScript script="#{javascript:loadSaver.doDownload()}">
							</xp:executeScript>
						</xp:this.action>
					</xp:eventHandler>
				</xp:button>
			</td>
			<td>
				<xp:panel id="messages">
					<xp:messages id="messages1"></xp:messages>
				</xp:panel>

			</td>
		</tr>
	</table>
	<zk:spreadsheetRich id="spreadsheetRich1" width="1080px" binding="#{zkComponentBinding['sheet']}"
		maxrows="200" maxcolumns="20" height="400px" src="/empty.xlsx">
	</zk:spreadsheetRich>
</xp:view>
```

## Write the controller `LoadSaver` ##
In this exercise, we use a `BookService` interface to provide functions to load and save Book and has a `FileSystemBookService` implementation, we will also use the general JSF Backing Bean to control XPages. Here are the steps:
  1. Create a Java interface named `demo.service.BookService` to load and save a book
```
public interface BookService {
	Book loadBook(String name) throws IOException;
	void saveBook(String name,Book book) throws IOException;
}
```
  1. Create a Java class `demo.service.FileSystemBookService` to implement `BookService` to load and save Book from the local file system by the use of `ExcelImporter` and `ExcelExporter`
```
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
```
  1. By creating a java class `demo.ctrl.LoadSaver` as a controller of the page, it will use the book service to load and save the book. Add four methods for mapping to the actions respectively in the page. Following is the source code to control Xpages:
```
package demo.ctrl;

import java.io.ByteArrayOutputStream;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.zkoss.xpage.core.bean.ComponentBinding;
import org.zkoss.xpage.core.component.Action;
import org.zkoss.xpage.core.component.ZulBridgeBase;
import org.zkoss.xpage.zss.component.SpreadsheetRichBridge;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.impl.ExcelExporter;
import org.zkoss.zss.ui.Spreadsheet;
import org.zkoss.zul.Filedownload;

import demo.service.BookService;
import demo.service.FileSystemBookService;

public class LoadSaver {

	String bookfolder = "C:/temp";
	String bookname = "loadSaver.xlsx";

	private BookService getBookService() {
		return new FileSystemBookService(bookfolder);
	}

	private void addMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
	}

	public void doClean() {
		ComponentBinding.getBridge("sheet").execute(new Action() {
			public void doAction(ZulBridgeBase bridge) {
				// get the spreadsheet
				Spreadsheet ss = ((SpreadsheetRichBridge) bridge).getSpreadsheet();
				ss.setSrc(null);
				ss.setSrc("/empty.xlsx");
				addMessage("book has been cleaned");
			}
		});
	}

	public void doLoad() {
		ComponentBinding.getBridge("sheet").execute(new Action() {
			public void doAction(ZulBridgeBase bridge) {
				// get the spreadsheet
				Spreadsheet ss = ((SpreadsheetRichBridge) bridge).getSpreadsheet();
				Book book = null;
				String errmsg = null;
				try {
					book = getBookService().loadBook(bookname);
				} catch (Exception e) {
					errmsg = e.getMessage();
				}
				if (book == null) {
					if (errmsg != null) {
						addMessage(errmsg);
					}
					addMessage("book not found");
				} else {
					ss.setBook(book);
					addMessage("book " + bookname + " was loaded");
				}
			}
		});

	}

	public void doSave() {
		ComponentBinding.getBridge("sheet").execute(new Action() {
			public void doAction(ZulBridgeBase bridge) {
				// get the spreadsheet
				Spreadsheet ss = ((SpreadsheetRichBridge) bridge).getSpreadsheet();
				Book book = ss.getBook();
				String errmsg = null;
				try {
					getBookService().saveBook(bookname, book);
				} catch (Exception e) {
					e.printStackTrace();
					errmsg = e.getMessage();
				}
				if (errmsg != null) {
					addMessage(errmsg);
				} else {
					addMessage("book " + bookname + " was saved");
				}
			}
		});

	}

	public void doDownload() {
		ComponentBinding.getBridge("sheet").execute(new Action() {
			public void doAction(ZulBridgeBase bridge) {
				// get the spreadsheet
				Spreadsheet ss = ((SpreadsheetRichBridge) bridge).getSpreadsheet();
				ExcelExporter exporter = new ExcelExporter();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Book book = ss.getBook();
				exporter.export(book, baos);
				Filedownload.save(baos.toByteArray(), "application/excel", bookname);
			}
		});
	}

}

```

Ok, now comes to the final step:
  1. Edit the file `WebContent/WEB-INF/faces-config.xml` by adding a bean 'loadSaver' . It uses the Java class you just created.
```
<?xml version="1.0" encoding="UTF-8"?>
<faces-config>
  <managed-bean>
    <managed-bean-name>loadSaver</managed-bean-name>
    <managed-bean-class>demo.ctrl.LoadSaver</managed-bean-class>
    <managed-bean-scope>request</managed-bean-scope>
  </managed-bean>
  <!--AUTOGEN-START-BUILDER: Automatically generated by IBM Lotus Domino Designer. Do not modify.-->
  <!--AUTOGEN-END-BUILDER: End of automatically generated section-->
</faces-config>
```
## Test and Run ##
Open the browser, link to Xpages, for example `http://localhost/demo.nsf/loadSaver.xsp`, and try the following simple test steps:
  1. Edit the spreadsheet, add some text and then click the save. Check the file in C:/temp
  1. Click clean to reset the spreadsheet before pressing the button "load". Check if the book has been loaded from the file you have just saved.