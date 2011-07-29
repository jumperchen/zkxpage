package demo.ctrl;

import javax.faces.context.FacesContext;

import org.zkoss.xpage.core.bean.ComponentBinding;
import org.zkoss.xpage.core.component.Action;
import org.zkoss.xpage.core.component.ZulBridgeBase;
import org.zkoss.xpage.zss.component.SpreadsheetBridge;
import org.zkoss.zss.model.Worksheet;
import org.zkoss.zss.ui.Spreadsheet;

import demo.CommentBinder;

public class QuarterForm {

	
	public void doSubmit(){
		ComponentBinding.getBridge("formsheet").execute(new Action(){
			public void doAction(ZulBridgeBase bridge) {
				//get the spreadsheet
				Spreadsheet ss = ((SpreadsheetBridge)bridge).getSpreadsheet();
				
				Worksheet sheet = ss.getBook().getWorksheetAt(0);
				FacesContext context = FacesContext.getCurrentInstance();
				
				CommentBinder cb = (CommentBinder)ss.getAttribute("commentBinder");
				//save to bean
				cb.saveAll(context, sheet);
			}});
	}
	
	
	public void doStyle1(){
		doStyle("/formsheet1.xlsx","600px");
	}
	
	public void doStyle2(){
		doStyle("/formsheet2.xlsx","800px");
	}
	
	public void doStyle(final String src, final String width){
		//get the binding bridge back, and execute the action
		ComponentBinding.getBridge("formsheet").execute(new Action(){
			public void doAction(ZulBridgeBase bridge) {
				//get the spreadsheet
				Spreadsheet ss = ((SpreadsheetBridge)bridge).getSpreadsheet();
				
				Worksheet sheet = ss.getBook().getWorksheetAt(0);
				FacesContext context = FacesContext.getCurrentInstance();
				
				CommentBinder cb = (CommentBinder)ss.getAttribute("commentBinder");
				//save to bean
				cb.saveAll(context, sheet);

				//set new 
				ss.setSrc(src);
				ss.setWidth(width);
				
				//refresh comment binder
				sheet = ss.getBook().getWorksheetAt(0);
				cb.refresh(sheet, 0, 29);
				cb.loadAll(context,sheet);
			}});
	}
}
