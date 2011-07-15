package org.zkoss.xpage.core.component;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;

public interface ZKComponent {
	
	public Component getComponent();

	public Desktop getDesktop();
	
	public boolean isDesktopTimeout();
}
