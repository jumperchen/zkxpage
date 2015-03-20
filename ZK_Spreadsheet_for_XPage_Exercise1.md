In this exercise, we have two input fields that bind to a controller (SBean)'s `cell` and `value` attributes. It also has two buttons to get and set values in Spreadsheet.

## Create XPages ##
  1. Create Xpages in your database
  1. Drag two inputText boxes(named inputCell and inputValue) to the Xpages. Change the layout. Bind the values to #{sbean.cell} & #{sbean.value} respectively.
  1. Add two buttons(named getBtn and setBtn) to the Xpages.
  1. Add action to getBtn button by invoking `sbean.doGet()` and set a partial update ID to inputValue.
  1. Add action to setBtn button by invoking `sbean.doSet()` and set a partial update ID to setBtn. (Note: You don't have to set a partial update to the ZK component as this will be taken care of automatically. However if you didn't set any partial update IDs, the whole FORM will be refreshed. So it is suggested to set a partial ID to 'setBtn' element to save some effort).

Page Preview:<br />![http://zkxpage.googlecode.com/svn/trunk/docs/images/simpledemo_1.png](http://zkxpage.googlecode.com/svn/trunk/docs/images/simpledemo_1.png)

The source code:
```
<?xml version="1.0" encoding="UTF-8"?>
<xp:view xmlns:xp="http://www.ibm.com/xsp/core"
	xmlns:zk="http://www.zkoss.org/xpage"
	xmlns:xp_1="http://www.ibm.com/xsp/coreex">

	Cell:
	<xp:inputText id="inputCell" value="#{sbean.cell}"></xp:inputText>
	,Value:
	<xp:inputText id="inputValue" value="#{sbean.value}"></xp:inputText>
	<xp:br></xp:br>

	<xp:button value="Get" id="getBtn">
		<xp:eventHandler event="onclick" submit="true" refreshMode="partial" refreshId="inputValue">
			<xp:this.action>
				<xp:executeScript script="#{javascript:sbean.doGet()}"></xp:executeScript>
			</xp:this.action>
		</xp:eventHandler>
	</xp:button>
	<xp:button value="Set" id="setBtn">
		<xp:eventHandler event="onclick" submit="true" refreshMode="partial" refreshId="setBtn">
			<xp:this.action>
				<xp:executeScript script="#{javascript:sbean.doSet()}"></xp:executeScript>
			</xp:this.action>
		</xp:eventHandler>
	</xp:button>
</xp:view>

```
## Add a spreadsheet component ##
  1. Let's continue with the above example. Drag a spreadsheet from Controls View (in the ZK category) to the XPages.
  1. Name the spreadsheet <i>myspreadsheet</i>, Bind it to `zkComponentBinding['spreadsheet1']`. Set the width and height to 600px and 400px respectively.
Now the Xpages source code will look something like this:
```
<?xml version="1.0" encoding="UTF-8"?>
<xp:view xmlns:xp="http://www.ibm.com/xsp/core"
	xmlns:zk="http://www.zkoss.org/xpage"
	xmlns:xp_1="http://www.ibm.com/xsp/coreex">

	Cell:
	<xp:inputText id="inputCell" value="#{sbean.cell}"></xp:inputText>
	,Value:
	<xp:inputText id="inputValue" value="#{sbean.value}"></xp:inputText>
	<xp:br></xp:br>

	<xp:button value="Get" id="getBtn">
		<xp:eventHandler event="onclick" submit="true" refreshMode="partial" refreshId="inputValue">
			<xp:this.action>
				<xp:executeScript script="#{javascript:sbean.doGet()}"></xp:executeScript>
			</xp:this.action>
		</xp:eventHandler>
	</xp:button>
	<xp:button value="Set" id="setBtn">
		<xp:eventHandler event="onclick" submit="true" refreshMode="partial" refreshId="setBtn">
			<xp:this.action>
				<xp:executeScript script="#{javascript:sbean.doSet()}"></xp:executeScript>
			</xp:this.action>
		</xp:eventHandler>
	</xp:button>
	<xp:br></xp:br>
	<zk:spreadsheet id="myspreadsheet" height="400px" width="600px">
		<zk:this.binding><![CDATA[#{zkComponentBinding['spreadsheet1']}]]></zk:this.binding>
	</zk:spreadsheet>	
</xp:view>
```

You page preview should be like : <br />
Page Preview<br />![http://zkxpage.googlecode.com/svn/trunk/docs/images/simpledemo_2.png](http://zkxpage.googlecode.com/svn/trunk/docs/images/simpledemo_2.png)

## Write the controller (SBean) ##
In this exercise, we will use the general JSF Backing Bean to control the Xpages. Here is the steps:
  1. Create a Java class(test.SBean) in one of the source folders (except `Local`. If you do not have any other source folders, please create a new source folder).
  1. Add two member fields `cell`, `value` and the getter/setter. The Xpages will be binded with these member fields (remember what we have binded in the Xpages?, the `#{sbean.cell},#{sbean.value`}).
  1. Add two methods `doGet`, `doSet`. Implementing the logic to get and set the spreadsheet values will depend on the selected cell. When clicking the button, these two methods will be called.
Following is the source code to control the Xpages:
```
package test;

import org.zkoss.poi.ss.usermodel.RichTextString;
import org.zkoss.xpage.core.bean.ComponentBinding;
import org.zkoss.xpage.core.component.Action;
import org.zkoss.xpage.core.component.ZulBridgeBase;
import org.zkoss.zss.model.FormatText;
import org.zkoss.zss.model.Range;
import org.zkoss.zss.model.Ranges;
public class SBean {

	String cell = "A1";
	String value;

	public String getCell() {
		return cell;
	}
	public void setCell(String cell) {
		this.cell = cell;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public void doSet(){
		//get the binding bridge back, and execute the action
		ComponentBinding.getBridge("spreadsheet1").execute(new Action(){
			public void doAction(ZulBridgeBase bridge) {
				//get the spreadsheet
				Spreadsheet ss = ((SpreadsheetBridge)bridge).getSpreadsheet();
				//use spreadsheet api to get value
				final Range range = Ranges.range(ss.getSelectedSheet(), cell);
				range.setEditText(value);
				
			}});
	}
	
	public void doGet(){
		//get the binding bridge back, and execute the action
		ComponentBinding.getBridge("spreadsheet1").execute(new Action(){
			public void doAction(ZulBridgeBase bridge) {
				//get the spreadsheet
				Spreadsheet ss = ((SpreadsheetBridge)bridge).getSpreadsheet();
				//use spreadsheet api to get value
				Range range = Ranges.range(ss.getSelectedSheet(), cell);
				FormatText ft = range.getFormatText();
				if (ft != null && ft.isCellFormatResult()) {
					value = ft.getCellFormatResult().text;
				} else {
					final RichTextString rstr = range == null ? null : range
							.getRichEditText();
					value = rstr != null ? rstr.getString() : "";
				}
			}});
	}
}

```

Ok, now comes to the final step:
  1. Edit the file `WebContent/WEB-INF/faces-config.xml` by adding a sbean. It uses the Java class you just created.
```
<?xml version="1.0" encoding="UTF-8"?>
<faces-config>
  <managed-bean>
    <managed-bean-name>sbean</managed-bean-name>
    <managed-bean-class>test.SBean</managed-bean-class>
    <managed-bean-scope>request</managed-bean-scope>
  </managed-bean>
  <!--AUTOGEN-START-BUILDER: Automatically generated by IBM Lotus Domino Designer. Do not modify.-->
  <!--AUTOGEN-END-BUILDER: End of automatically generated section-->
</faces-config>
```
## Test and Run ##
Open the browser, link to the Xpages , for example `http://localhost/demo.nsf/spreadsheet.xsp`, and try the following simple test steps:
  1. Click the spreadsheet and make cell A1 the active cell, set the cell value to `abcd`. When you press the Get button, you will see the value for cell A1 become `abcd`.
  1. Edit the input text fields, set Cell to A2, Value to `1234`, When you press the Set button, you will then see cell A2 of spreadsheet become `1234`.
  1. Click the spreadsheet. To sum up a range of cells (say from C1 to C3), select a blank cell you want to sum, cell B1 in this example. To add up the numbers from cell C1 to C3 and place the answer the cell B1, write the formula `=SUM(C1:C3)` in cell B1. Now, you can set any values you want in cell C1 to C3 in spreadsheet or by entering the numbers in the input text fields and clicking the Set button. You will always see B1 be the sum of values between cell C1 to C3.

![http://zkxpage.googlecode.com/svn/trunk/docs/images/simpledemo_3.png](http://zkxpage.googlecode.com/svn/trunk/docs/images/simpledemo_3.png)