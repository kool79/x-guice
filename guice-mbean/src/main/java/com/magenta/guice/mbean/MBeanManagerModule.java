package com.magenta.guice.mbean;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.maxifier.guice.mbean.*;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

@Deprecated
public final class MBeanManagerModule extends AbstractModule {

    private final String domain;
    private final MBeanServer mbeanServer;

    @Deprecated
    public static Module platform(String domain) {
        return new MBeanManagerModule(domain, ManagementFactory.getPlatformMBeanServer());
    }

    @Deprecated
    public static Module server(String domain, MBeanServer mbeanServer) {
        return new MBeanManagerModule(domain, mbeanServer);
    }

    @Deprecated
    public static Module noOperations() {
        return new AbstractModule() {
            @Override
            protected void configure() {
                bind(MBeanManager.class).toInstance(MBeanManager.NO_OPERATIONS);
            }
        };
    }


    public MBeanManagerModule(String domain, MBeanServer mbeanServer) {
        this.domain = domain;
        this.mbeanServer = mbeanServer;
    }

    @Override
    protected void configure() {
        bind(MBeanManager.class).toInstance(new MBeanManagerImpl(domain, mbeanServer, new CGLIBMBeanGenerator()));
        MBeanTypeListener listener = new MBeanTypeListener();
        requestInjection(listener);
        //noinspection unchecked
        bindListener(new AnnotationMatcher(MBean.class, com.maxifier.guice.mbean.MBean.class), listener);
    }
}
