
if(!window.zkXPage){

zkXPage = {};

/* escape jsf client id to jq selector */
zkXPage.jqid =  function(id) {
	if(typeof(id)=='string'){
		id = id.replace(/:/g, "\\:");
	}
	return id;
};

/* re-locate css link to head */
zkXPage.relocateCss = function(domid){
	var odom = jq(zkXPage.jqid(domid));
	//move link to head
	var links = odom.children('link');
	var head = jq('head');
	
	var hmap = {};
	if(links.length>0){
		head.children('link').each(function(){
			if(this.href){
				hmap[''+this.href] = true;
			}
		});
	}
	links.each(function(){
		if(this.href && !hmap[''+this.href]){
			head.append(this);
		}
	});
}

/* reattach widget's dom beofer the fake dom */
zkXPage.reattach = function(widgetid,domid){
	var w = zk.Widget.$(zkXPage.jqid(widgetid));
	if(w){
		var wdom = w.$n();
		setTimeout(function(){
			//have to get new one
			var ndom = jq(zkXPage.jqid(domid));
			jq(wdom).insertBefore(ndom);
			//ndom.append(wdom);
		},0);
	}
}

}
