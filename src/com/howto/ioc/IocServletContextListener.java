package com.howto.ioc;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class IocServletContextListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        BeanFactory.init();
    }

}
