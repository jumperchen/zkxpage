package demo.ctrl;

import javax.faces.context.FacesContext;

import org.zkoss.xpage.core.component.ZulBridgeBase;
import org.zkoss.xpage.core.util.Composer;
import org.zkoss.xpage.zss.component.SpreadsheetBridge;
import org.zkoss.zss.model.Book;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.zss.ui.Spreadsheet;

import demo.CommentBinder;

public class QuarterFormComposer implements Composer {

	public void doAfterCompose(ZulBridgeBase bridge, FacesContext context) throws Exception {
		Spreadsheet ss = ((SpreadsheetBridge) bridge).getSpreadsheet();
		Book book = ss.getBook();
		Worksheet sheet = book.getWorksheetAt(0);
		
		CommentBinder cb = new CommentBinder();
		cb.refresh(sheet, 0, 19);
		
		ss.setAttribute("commentBinder", cb);
		cb.loadAll(context,sheet);
	}



}
