package demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.zkoss.poi.ss.usermodel.Comment;
import org.zkoss.poi.ss.usermodel.Sheet;
import org.zkoss.xpage.core.util.Log;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
import org.zkoss.zss.model.Worksheet;

public class CommentBinder implements Serializable {

	private static final long serialVersionUID = 1L;
	List<CommentBindingIndex> indexs = new ArrayList<CommentBindingIndex>();

	public CommentBinder() {

	}

	public CommentBinder(Worksheet sheet) {
		refresh(sheet, 0, 39);
	}

	public CommentBinder(Worksheet sheet, int colstart, int colend) {
		refresh(sheet, colstart, colend);
	}

	public void refresh(Sheet sheet, int colstart, int colend) {
		indexs.clear();
		int fr = sheet.getFirstRowNum();
		int lr = sheet.getLastRowNum();
		for (int r = fr; r <= lr; r++) {
			for (int c = colstart; c <= colend; c++) {
				Comment comment = sheet.getCellComment(r, c);
				if (comment != null) {
					String str = comment.getString().toString();
					if (str != null && !"".equals(str = str.trim())) {
						if (str.startsWith("#{") && str.endsWith("}")) {
							indexs.add(new CommentBindingIndex(r, c, str));
						}
					}
				}
			}
		}
	}

	/**
	 * load data from context to sheet
	 */
	public void loadAll(FacesContext context, Worksheet sheet) {
		// TODO iteration
		Application app = context.getApplication();
		for (CommentBindingIndex cbi : indexs) {
			// Cell cell = sheet.getRow(cbi.row).getCell(cbi.column);
			Range range = Ranges.range(sheet, cbi.row, cbi.column);
			if (range != null) {
				Object value = evalGet(app, context, cbi.expr);
				range.setValue(value);
			}
		}
	}

	private Object evalGet(Application app, FacesContext context, String expr) {
		try {
			ValueBinding vb = app.createValueBinding(expr);
			Object value = vb.getValue(context);
			return value;
		} catch (RuntimeException x) {
			Log.error(this, x.getMessage(), x);
			return x.getClass().toString()+":"+x.getMessage();
		}
	}
	private void evalSet(Application app, FacesContext context, String expr,Object value) {
		try {
			ValueBinding vb = app.createValueBinding(expr);
			vb.setValue(context,value);
		} catch (RuntimeException x) {
			Log.error(this, x.getMessage(), x);
			throw x;
		}
	}

	/**
	 * save data from sheet to context
	 */
	public void saveAll(FacesContext context, Worksheet sheet) {
		// TODO collection.
		Application app = context.getApplication();
		for (CommentBindingIndex cbi : indexs) {
			// Cell cell = sheet.getRow(cbi.row).getCell(cbi.column);
			Range range = Ranges.range(sheet, cbi.row, cbi.column);
			if (range != null) {
				Object value = range.getValue();
				evalSet(app,context,cbi.expr,value);
			}
		}
	}

	static class CommentBindingIndex implements Serializable {
		private static final long serialVersionUID = 1L;
		final int row;
		final int column;
		final String expr;

		public CommentBindingIndex(int row, int column, String expr) {
			this.row = row;
			this.column = column;
			this.expr = expr;
		}
	}
}
