
if(!window.zkXPage){

zkXPage = {};

zkXPage.jqid =  function(id) {
	if(typeof(id)=='string'){
		id = id.replace(/:/g, "\\:");
	}
	return id;
};

zkXPage.detach = function(widgetid){
	var w = zk.Widget.$(zkXPage.jqid(widgetid));
	if(w){
		var dt = w.desktop;
		w.detach();
		//no more children in desktop. remove it.
		if(dt){
			var c = dt.firstChild;
			var rm = true;;
			while(c){
				if(!c.$instanceof(zk.Page)){
					rm = false;
					break;
				}
				c = c.nextSibling;
			}
			if(rm){
				zkXPage.removeDesktop(dt.id);
			}
		}
	}
}

zkXPage.removeDesktop = function(dtid){
	var bRmDesktop = !zk.opera /*&& !zk.keepDesktop && !zk.zkuery*/;
	if (bRmDesktop/* || zk.pfmeter*/) {
		try {
			var dt = zk.Desktop.all[dtid];
			if(dt){
				delete zk.Desktop.all[dtid];
				dt.detach();
				jq.ajax(zk.$default({
					url: zk.ajaxURI(null, {desktop:dt,au:true}),
					data: {dtid: dtid, cmd_0: bRmDesktop?"rmDesktop":"dummy", opt_0: "i"},
					beforeSend: function (xhr) {
						if (zk.pfmeter) zAu._pfsend(dt, xhr, true);
					},
					//2011/04/22 feature 3291332
					//Use sync request for chrome and safari.
					//Note: when pressing F5, the request's URL still arrives before this even async:false
					async: !zk.safari
				}, zAu.ajaxSettings), true/*fixed IE memory issue for jQuery 1.4.x*/);
			}
		} catch (e) { //silent
		}
	}	
}


}
