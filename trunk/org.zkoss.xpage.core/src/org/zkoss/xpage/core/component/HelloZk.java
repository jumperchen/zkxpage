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
package org.zkoss.xpage.core.component;

import org.zkoss.xpage.core.Constants;

/**
 * a simple hello zk component
 * @author Dennis Chen
 *
 */
public class HelloZk extends ZulBridgeBase {
	private static final long serialVersionUID = 1L;

	public HelloZk() {
		super();
		setRendererType("org.zkoss.xpage.hellozk");
	}

	@Override
	public String getFamily() {
		return Constants.COMPONENT_FAMILY;
	}

}
