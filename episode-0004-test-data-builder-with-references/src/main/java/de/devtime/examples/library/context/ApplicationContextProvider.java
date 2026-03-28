package de.devtime.examples.library.context;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

  private static AtomicReference<ApplicationContext> appContext = new AtomicReference<>();

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    log.info("Spring application context is available in ApplicationContextProvider class for non managed classes");
    ApplicationContextProvider.appContext.set(applicationContext);
  }

  public static Optional<ApplicationContext> findApplicationContext() {
    ApplicationContext applicationContext = appContext.get();
    if (applicationContext == null) {
      log.warn("No spring application context available! Please make sure to set an application context.");
    }
    return Optional.ofNullable(applicationContext);
  }

  public static ApplicationContext getApplicationContext() {
    ApplicationContext applicationContext = appContext.get();
    if (applicationContext == null) {
      throw new NullPointerException(
          "No spring application context available! Please make sure to set an application context.");
    }
    return applicationContext;
  }

}
