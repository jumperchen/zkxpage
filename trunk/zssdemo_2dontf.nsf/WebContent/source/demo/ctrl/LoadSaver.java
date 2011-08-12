package demo.ctrl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.zkoss.xpage.core.bean.ComponentBinding;
import org.zkoss.xpage.core.component.Action;
import org.zkoss.xpage.core.component.ZulBridgeBase;
import org.zkoss.xpage.zss.component.SpreadsheetBridge;
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
		// this is a dummy impl. for the loading and saving excel to the disk
		return new FileSystemBookService(bookfolder);
	}

	private void addMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
	}
	
	public void doClean(){
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
					addMessage("book "+bookname+" was loaded");
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
					errmsg = e.getMessage();
				}
				if (errmsg != null) {
					addMessage(errmsg);
				} else {
					addMessage("book "+bookname+" was saved");
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
