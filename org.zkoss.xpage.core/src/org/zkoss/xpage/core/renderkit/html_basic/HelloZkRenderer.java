/*
 * Copyright 2011 Potix Corporation. All Rights Reserved.
 * 
 * Licensed under the GNU GENERAL PUBLIC LICENSE Version 3 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.gnu.org/licenses/gpl-3.0.txt
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package org.zkoss.xpage.core.renderkit.html_basic;

import java.lang.reflect.Method;

import org.zkoss.xpage.core.component.ZulBridgeBase;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Messagebox;

import com.ibm.domino.xsp.module.nsf.NotesContext;
/**
 * this is a very simple component's renderer, it test zk ajax and access notes context in zk au 
 * @author Dennis Chen
 *
 */
public class HelloZkRenderer extends ZulRendererBase {

	private static String getUserName(){
		try{
			NotesContext nc = NotesContext.getCurrentUnchecked();
			if(nc==null) return "no context";
			Object sess = nc.getCurrentSession();
			if(sess==null) return "no session";
			
			Method sm = sess.getClass().getMethod("getCommonUserName");
            String username = (String)sm.invoke(sess, (Object[])null);
            return username;
		}catch(Throwable x){
			x.printStackTrace();
			return x.getMessage();
		}
	}
	
	@Override
	protected HtmlBasedComponent createRootComponent(Page page,ZulBridgeBase bridge){
		Button button = new Button();
		button.setLabel("Hi "+getUserName()+", Click me to verify zk ajax");
		button.addEventListener("onClick", new EventListener(){
			public void onEvent(Event evt)
					throws Exception {
				Messagebox.show("Hi "+getUserName()+", zk is ajax update is running");
			}});
		return button;
	}
}
