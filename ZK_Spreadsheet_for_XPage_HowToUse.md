## Enable the library in project ##
Before using a spreadsheet, you have to enable the ZK Spreadsheet for XPages library for your application. To do this, go to _Application Properties->Advanced_, enable the library IDs **org.zkoss.xpage.core.library** and **org.zkoss.xpage.zss.library**. <br /> ![http://zkxpage.googlecode.com/svn/trunk/docs/images/howto_1.png](http://zkxpage.googlecode.com/svn/trunk/docs/images/howto_1.png)

## Add the spreadsheet component to XPages ##
To add a spreadsheet component to XPages,
  1. Edit the page
  1. In **Controls View**, open the **ZK** category. You should see the component **spreadsheet**(![http://zkxpage.googlecode.com/svn/trunk/org.zkoss.xpage.zss/images/icon/zss_16.png](http://zkxpage.googlecode.com/svn/trunk/org.zkoss.xpage.zss/images/icon/zss_16.png)). Drag it to the Xpages.
  1. In the page **Design** tab, you will see the spreadsheet preview as the following: ![http://zkxpage.googlecode.com/svn/trunk/org.zkoss.xpage.zss/images/zss_preview.jpg](http://zkxpage.googlecode.com/svn/trunk/org.zkoss.xpage.zss/images/zss_preview.jpg)
  1. In the page **Source** tab, you will see a ZK component namespace(`xmlns:zk="http://www.zkoss.org/xpage"`) in the root node and a spreadsheet node in the XML content. It will look like something below:
```
<zk:spreadsheet id="spreadsheet1">
</zk:spreadsheet>
```

## Component Properties ##
You need to set following properties to configure the spreadsheet.
### Essential properties ###
| **Prpoerty** | **Description** |
|:-------------|:----------------|
| id | The component id (ex, `spreadsheet1`) |
| width | The width (ex, `600px`)|
| height | The height (ex, `400px`)|
| src | The src location of a book model. It could locate either in the `WebContent` or class path (ex, `/WEB-INF/demo.xls`)|
| maxcolumns | The maximum visible number of columns of the spreadsheet (ex, `40`)|
| maxrows | The maximum visible number of rows of this spreadsheet (ex, `40`)|
| binding | The component binding expression. This is usually an EL expression like ` #{zkComponentBinding['a_binding_name']} ` |

  * **_zkComponentBinding_** is a util bean which helps you to easily bind components in XPages and access components in the Java code.
  * Following is a very general example of using this component:
```
<zk:spreadsheet id="spreadsheet1" height="400px" width="600px"
	src="/WEB-INF/demo_sample.xls" binding="#{zkComponentBinding['spreadsheet1']}">
</zk:spreadsheet>
```

### Other available properties in XPages ###
| **Prpoerty** | **Description** |
|:-------------|:----------------|
| apply | the ZK component composer, please read zk developer document for more detail |
| style | the CSS style |
| sclass | the CSS class |
| zclass | the ZK Cascading Style class(es) for this component |
| zindex | the Z index |
| hflex | the horizontal flex hint of this component |
| vflex | the vertical flex hint of this component |


  * Not all ZK Spreadsheet properties/features are directly available in the ZK Spreadsheet for XPages Components.
  * For more ZK Spreadsheet features, please read [online document](http://books.zkoss.org/wiki/ZK_Spreadsheet_Docs).

## Access ZK Spreadsheet in Controller ##
It is very easy to interact with ZK Spreadsheet  with the XPages Java Controller.
  1. Use `ComponentBinding.getBridge("a_binding_name")` to get a `org.zkoss.xpage.zss.component.SpreadsheetBridge` back.
  1. To use the bridge component, you cannot directly use it access it's internal ZK Spreadsheet. Instead, you call `SpreadsheetBridge.execute(Action action)` to do an action( get and update the ZK Spreadsheet) in the method. Get ZK Spreadsheet through `Action.doAction(ZulBridgeBase bridge)` by invoking `bridge.getSpreadsheet();` in the action.
Following is a simple example to set/get values in a ZK Spreadsheet
```
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
```

In the `Action.doAction(ZulBridgeBase bridge)` method, it is safe to access ZK Components. You can get and update values, the updates will be automatically refresh to client side (user's browser)

Please refer to [ZK Spreadsheet Essentials](http://books.zkoss.org/wiki/ZK_Spreadsheet_Essentials) for all the detail.