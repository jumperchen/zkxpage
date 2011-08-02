package demo.ctrl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.zkoss.pivot.Calculator;
import org.zkoss.pivot.Calculators;
import org.zkoss.pivot.GroupHandler;
import org.zkoss.pivot.PivotModel;
import org.zkoss.pivot.Pivottable;
import org.zkoss.pivot.impl.TabularPivotModel;
import org.zkoss.pivot.util.Trees;
import org.zkoss.xpage.core.bean.ComponentBinding;
import org.zkoss.xpage.core.component.Action;
import org.zkoss.xpage.core.component.ZulBridgeBase;
import org.zkoss.xpage.pivot.component.PivottableBridge;

public class FlightTicket {

	private long now = new Date().getTime();
	private static final long DAY = 1000 * 60 * 60 * 24;

	String orient = "row";

	
	
	public String getOrient() {
    	return orient;
    }

	public void setOrient(String orient) {
    	this.orient = orient;
    }

	public PivotModel getModel() {
		return buildModel();
	}

	private Date dt(int i) {
		return new Date(now + i * DAY);
	}


	public List<String> getColumns() {
		return Arrays.asList(new String[] { "Agent", "Customer", "Airline", "Flight", "Date", "Origin", "Destination",
		        "Price", "Mileage" });
	}


	private List<List<Object>> getData() {
		Object[][] objs = new Object[][] {
		        { "Carlene Valone", "Tameka Meserve", "ATB Air", "AT15", dt(-7), "Berlin", "Paris", 186.6, 545 },
		        { "Antonio Mattos", "Sharon Roundy", "Jasper", "JS1", dt(-5), "Frankfurt", "Berlin", 139.5, 262 },
		        { "Russell Testa", "Carl Whitmore", "Epsilon", "EP2", dt(-3), "Dublin", "London", 108.0, 287 },
		        { "Antonio Mattos", "Velma Sutherland", "Epsilon", "EP5", dt(-1), "Berlin", "London", 133.5, 578 },
		        { "Carlene Valone", "Cora Million", "Jasper", "JS30", dt(-4), "Paris", "Frankfurt", 175.4, 297 },
		        { "Richard Hung", "Candace Marek", "DTB Air", "BK201", dt(-5), "Manchester", "Paris", 168.5, 376 },
		        { "Antonio Mattos", "Albert Briseno", "Fujito", "FJ1", dt(-7), "Berlin", "Osaka", 886.9, 5486 },
		        { "Russell Testa", "Louise Knutson", "HST Air", "HT6", dt(-2), "Prague", "London", 240.6, 643 },
		        { "Antonio Mattos", "Jessica Lunsford", "Jasper", "JS9", dt(-4), "Munich", "Lisbon", 431.6, 1222 },
		        { "Becky Schafer", "Lula Lundberg", "Jasper", "JS1", dt(-3), "Frankfurt", "Berlin", 160.5, 262 },
		        { "Carlene Valone", "Tameka Meserve", "Epsilon", "EP5", dt(-3), "Berlin", "London", 104.6, 578 },
		        { "Antonio Mattos", "Yvonne Melendez", "Epsilon", "EP5", dt(-2), "Berlin", "London", 150.5, 578 },
		        { "Antonio Mattos", "Josephine Whitley", "ATB Air", "AT15", dt(-6), "Berlin", "Paris", 192.6, 545 },
		        { "Antonio Mattos", "Velma Sutherland", "DTB Air", "BK201", dt(-6), "Manchester", "Paris", 183.8, 376 },
		        { "Richard Hung", "Blanca Samuel", "Fujito", "FJ2", dt(-7), "Berlin", "Osaka", 915.3, 5486 },
		        { "Russell Testa", "Katherine Bennet", "Epsilon", "EP23", dt(-4), "Lisbon", "London", 214.8, 987 },
		        { "Joann Cleaver", "Alison Apodaca", "Jasper", "JS1", dt(-5), "Frankfurt", "Berlin", 166.3, 262 },
		        { "Antonio Mattos", "Tameka Meserve", "Epsilon", "EP21", dt(-1), "London", "Lisbon", 153.8, 987 },
		        { "Carlene Valone", "Janie Harper", "KST Air", "KT10", dt(-2), "Prague", "Paris", 187.9, 550 },
		        { "Russell Testa", "Myrtle Fournier", "Jasper", "JS30", dt(-4), "Paris", "Frankfurt", 207.5, 297 },
		        { "Joann Cleaver", "Victor Michalski", "Jasper", "JS2", dt(-3), "Frankfurt", "Amsterdam", 470.3, 224 },
		        { "Carlene Valone", "Renee Marrow", "Epsilon", "EP19", dt(-4), "London", "Dublin", 133.6, 287 },
		        { "Carlene Valone", "Harold Fletcher", "Jasper", "JS2", dt(-4), "Frankfurt", "Amsterdam", 435.3, 224 },
		        { "Antonio Mattos", "Velma Sutherland", "Jasper", "JS7", dt(-4), "Munich", "Amsterdam", 421.1, 413 },
		        { "Becky Schafer", "Dennis Labbe", "Epsilon", "EP8", dt(-6), "London", "Paris", 134.4, 213 },
		        { "Joann Cleaver", "Louis Brumfield", "Epsilon", "EP4", dt(-2), "London", "Berlin", 132.3, 578 },
		        { "Antonio Mattos", "Eunice Alcala", "Jasper", "JS11", dt(-1), "Munich", "Frankfurt", 178.4, 189 },
		        { "Russell Testa", "Velma Sutherland", "Epsilon", "EP4", dt(-7), "London", "Berlin", 155.7, 578 } };
		List<List<Object>> list = new ArrayList<List<Object>>();
		for (Object[] a : objs)
			list.add(Arrays.asList(a));
		return list;
	}

	private PivotModel buildModel() {
		TabularPivotModel model = new TabularPivotModel(getData(), getColumns());
		configPerformance(model);
		return model;
	}

	private void fillRemainingFields(TabularPivotModel model) {
		for (String name : getColumns()) {
			if (model.getField(name) == null) {
				model.addField(name, -1, null);
			}
		}
	}

	private void expandRowTree(TabularPivotModel model) {
		Trees.openDown(model.getRowHeaderTree().getRoot(), true);
	}

	private void configPerformance(TabularPivotModel model) {
		model.removeAllFields();
		model.addColumnField("Airline");
		model.addColumnField("Flight");
		model.addRowField("Agent");
		model.addRowField("Customer");
		model.addDataField("Price");
		model.addDataField("Mileage");

		model.getField("Airline").setSubtotals(new Calculator[] { Calculators.AVERAGE, Calculators.COUNT });
		model.getField("Agent").setSubtotals(new Calculator[] { Calculators.AVERAGE, Calculators.COUNT });

		fillRemainingFields(model);
//		expandRowTree(model);
	}

	private void configCitySales(TabularPivotModel model) {
		model.removeAllFields();
		model.addColumnField("Origin");
		model.addColumnField("Destination");
		model.addRowField("Airline");
		model.addRowField("Flight");
		model.addDataField("Customer");
		model.addDataField("Price");

		fillRemainingFields(model);
		expandRowTree(model);
	}

	private void configSalesRace(TabularPivotModel model) {
		model.removeAllFields();
		model.addColumnField("Agent");
		model.addRowField("Date");
		model.addDataField("Customer");
		model.addDataField("Price");

		model.getField("Date").setGroupHandler(new GroupHandler() {
			public Object getGroup(Object data) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
				return format.format((Date) data);
			}
		});

		fillRemainingFields(model);
		expandRowTree(model);
	}

	// public void setModel(PivotModel model) {
	// this.model = model;
	// }

	public void doPerformance() {
		// get the binding bridge back, and execute the action
		ComponentBinding.getBridge("pivottable").execute(new Action() {
			public void doAction(ZulBridgeBase bridge) {
				Pivottable table = ((PivottableBridge) bridge).getPivottable();
				configPerformance((TabularPivotModel) table.getModel());
				table.setDataFieldOrient(orient);
			}
		});
	}

	public void doSalesByCity() {
		// get the binding bridge back, and execute the action
		ComponentBinding.getBridge("pivottable").execute(new Action() {
			public void doAction(ZulBridgeBase bridge) {
				Pivottable table = ((PivottableBridge) bridge).getPivottable();
				configCitySales((TabularPivotModel) table.getModel());
				table.setDataFieldOrient(orient);
			}
		});
	}

	public void doSalesRace() {
		// get the binding bridge back, and execute the action
		ComponentBinding.getBridge("pivottable").execute(new Action() {
			public void doAction(ZulBridgeBase bridge) {
				Pivottable table = ((PivottableBridge) bridge).getPivottable();
				configSalesRace((TabularPivotModel) table.getModel());
				table.setDataFieldOrient(orient);
			}
		});
	}
	
	public void doOrient() {
		// get the binding bridge back, and execute the action
		ComponentBinding.getBridge("pivottable").execute(new Action() {
			public void doAction(ZulBridgeBase bridge) {
				Pivottable table = ((PivottableBridge) bridge).getPivottable();
				table.setDataFieldOrient(orient);
			}
		});
	}
}
