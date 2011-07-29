package demo.ctrl;

import org.zkoss.xpage.core.bean.JsfContext;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.Composer;

import demo.data.QuarterBean;
import demo.data.QuarterBeanProvider;

public class QuarterReportComposer implements Composer{

	public void doAfterCompose(Component comp) throws Exception {
		QuarterBean bean = (QuarterBean)JsfContext.instance().getBean("quarterBean");
		QuarterBeanProvider.queryQuarterBean(1, bean);
		comp.setAttribute("quarterBean", bean,false);
    }

}
