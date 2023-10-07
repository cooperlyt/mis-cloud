package io.github.cooperlyt.mis.work.camunda.delegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BeanInjectionHelper {

  private static AutowireCapableBeanFactory beanFactory;

  @Autowired
  public void setApplicationContext(ApplicationContext applicationContext) {
    beanFactory = applicationContext.getAutowireCapableBeanFactory();
  }

  public static void autowireBean(Object bean) {
    beanFactory.autowireBean(bean);
  }

}
