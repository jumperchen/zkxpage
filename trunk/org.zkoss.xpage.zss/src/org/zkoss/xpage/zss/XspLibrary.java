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
package org.zkoss.xpage.zss;

import com.ibm.xsp.library.AbstractXspLibrary;

/**
 * 
 * @author Dennis Chen
 *
 */
public class XspLibrary extends AbstractXspLibrary {

	public String getLibraryId() {
		//the id of this xsp component, will be related by application, so don't modify after release
		return "org.zkoss.xpage.zss.library";
	}

	@Override
	public String[] getFacesConfigFiles() {
		return new String[] { "META-INF/zk-xpage-zss-faces-config.xml" };
	}

	@Override
	public String getPluginId() {
		return "org.zkoss.xpage.zss";
	}

	@Override
	public String[] getXspConfigFiles() {
		return new String[] { "META-INF/zk-xpage-zss.xsp-config" };
	}

	@Override
	public String getTagVersion() {
		return "0.9.0";
	}

}
