package org.jboss.errai.bus.server;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.servlet.ServletModule;
import org.jboss.errai.bus.client.MessageBus;
import org.jboss.errai.bus.server.security.auth.AuthenticationAdapter;
import org.jboss.errai.bus.server.security.auth.DefaultAdapter;
import org.jboss.errai.bus.server.service.ErraiService;
import org.jboss.errai.bus.server.service.ErraiServiceConfigurator;
import org.jboss.errai.bus.server.service.ErraiServiceConfiguratorImpl;
import org.jboss.errai.bus.server.service.ErraiServiceImpl;

import java.util.Enumeration;
import java.util.ResourceBundle;


public class ErraiModule extends ServletModule {
    @Override
    protected void configureServlets() {
        ResourceBundle bundle = ResourceBundle.getBundle("ErraiService");
        Enumeration<String> keys = bundle.getKeys();

        boolean authAdapterSpecified = false;

        String key;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();

            if ("errai.application_context".equals(key)) {
                String appContext = bundle.getString("errai.application_context") + "erraiBus";
                serve(appContext).with(ErraiServletImpl.class);
            } else if ("errai.authentication_adapter".equals(key)) {
                try {
                    Class<? extends AuthenticationAdapter> authAdapterClass = Class.forName(bundle.getString(key))
                            .asSubclass(AuthenticationAdapter.class);
                    bind(AuthenticationAdapter.class).to(authAdapterClass);
                    authAdapterSpecified = true;

                }
                catch (Exception e) {
                    throw new RuntimeException("Could not load authentication adapter: "
                            + bundle.getString(key), e);
                }

            }
        }

        bind(MessageBus.class).to(ServerMessageBusImpl.class);
        bind(ServerMessageBus.class).to(ServerMessageBusImpl.class);
        bind(ErraiService.class).to(ErraiServiceImpl.class);
        bind(ErraiServiceConfigurator.class).to(ErraiServiceConfiguratorImpl.class);
        bind(ErraiModule.class).toInstance(this);
       
        if (!authAdapterSpecified) {
            bind(AuthenticationAdapter.class).to(DefaultAdapter.class);
        }
    }

    @Override
    public <T> LinkedBindingBuilder<T> bind(Key<T> key) {
        return super.bind(key);
    }

    @Override
    public <T> AnnotatedBindingBuilder<T> bind(TypeLiteral<T> typeLiteral) {
        return super.bind(typeLiteral);
    }

    @Override
    public <T> AnnotatedBindingBuilder<T> bind(Class<T> clazz) {
        return super.bind(clazz);
    }
}
