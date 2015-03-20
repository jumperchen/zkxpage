Installing [XPages Extension Library](http://extlib.openntf.org/) for ZK Spreadsheet is very straightforward. If you have any problems following the instruction below, you could also refer to the [documentation](http://www.openntf.org/Projects/pmt.nsf/122B29BEE5A3E3A58625779F001DEA75/%24file/XPages%20Extension%20Library%20Documentation.pdf) of XPages Extension Library or other online [slideshows](http://www.slideshare.net/WorkFlowStudios/xpages-extension-toolkit-library-installation-steps). They have more installation screenshots.

## Installing the library on Domino Designer ##
  1. The library is provided as an Eclipse Update Site ([Download](http://code.google.com/p/zkxpage/downloads/list)) which should be installed using the Eclipse Update Manager. To enable the Update Manager, you should display the Designer _Preferences->Domino Designer_ and check _Enable Eclipse plug-in installation_.
  1. Now, the menu _File->Application->Install..._ is enabled. Select it and choose to install a new feature.
  1. Add the **updateSite.zip** file.
  1. Accept the License terms in order to continue with the installation for the the plugin then restart Designer.
  1. Verify that the plug-ins are properly installed from the menu _Help->About Domino Designer_ using the plug-in details button. Check if there are **org.zkoss.xpage.core** and **org.zkoss.xpage.zss** <br />![http://zkxpage.googlecode.com/svn/trunk/docs/images/installation_4.png](http://zkxpage.googlecode.com/svn/trunk/docs/images/installation_4.png)
## Installing the library on Domino Server ##
> The library can also be installed as a set of plug-in on the Domino server, but currently there is no Update Manager UI available on the Domino server. Therefore you must install it manually.
  1. Unpack the folder **features**,**plugins** of the **updateSite.zip** into the directory(ex, **C:\IBM\Lotus\Domino\data\domino\workspace\applications\eclipse**) located in your Domino data directory (be careful to strictly respect the directory hierarchy). Under the Eclipse folder, there should be two folders named **features** and **plugins**. (Note: you must delete the old version of jar files in this plugin).<br />![http://zkxpage.googlecode.com/svn/trunk/docs/images/installation_2.png](http://zkxpage.googlecode.com/svn/trunk/docs/images/installation_2.png)
  1. Add permission to jvm security policy grant session (ex, **C:\IBM\Lotus\Domino\jvm\lib\security\java.policy**). Please consult your Domino admin if you have any security issue.
```
grant { 
  //....other permission....
  //for components
  permission java.lang.RuntimePermission "getClassLoader";
  //for zk spreadsheet pdf export
  permission java.lang.RuntimePermission "getenv.windir"
}
```
  1. Restart your server or the http task(use command **restart task http**). The library should be available. To verify that the library is properly installed and running, use command **tell http osgi ss org.zkoss.xpage**. You should get a result showing the plug-ins at least with in the **RESOLVED** state. <br />![http://zkxpage.googlecode.com/svn/trunk/docs/images/installation_1.png](http://zkxpage.googlecode.com/svn/trunk/docs/images/installation_1.png)