package de.devtime.examples.library.context;

import org.springframework.context.ApplicationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractManualAutowiredBean<B> {

  protected ApplicationContext appContext;

  protected AbstractManualAutowiredBean() {
    autowire();
  }

  public B autowire() {
    ApplicationContextProvider.findApplicationContext()
        .ifPresent(context -> {
          this.appContext = context;
          this.appContext.getAutowireCapableBeanFactory().autowireBean(this);
        });
    log.debug("appContext: {}", this.appContext);
    return self();
  }

  @SuppressWarnings("unchecked")
  protected B self() {
    return (B) this;
  }
}
