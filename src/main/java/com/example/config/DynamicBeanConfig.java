package com.example.config;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * 动态创建 mapper、service, class and bean
 */
@Configuration
@AutoConfigureAfter(SpringUtil.class)
public class DynamicBeanConfig {

    private final static Logger log = LoggerFactory.getLogger(DynamicBeanConfig.class);

    private final static String ENTITY_BASE_PACKAGE = "com.example.entity";
    private final static String MAPPER_BASE_PACKAGE = "com.example.mapper";
    private final static String SERVICE_BASE_PACKAGE = "com.example.service";

    @Bean
    public SmartInitializingSingleton registerDynamicServicesAndMappers(SqlSessionFactory sqlSessionFactory) {
        return () -> {

            // 扫描数据库实体，根据实体创建对应 mapper、service
            Set<Class<?>> entityClassSet = ClassUtil.scanPackage(ENTITY_BASE_PACKAGE);
            if (entityClassSet.isEmpty()) {
                return;
            }

            // 已有 service、mapper 避免重复创建
            Set<String> serviceClassNameSet = ClassUtil.scanPackage(SERVICE_BASE_PACKAGE).stream().map(Class::getName).collect(Collectors.toSet());
            Set<String> mapperClassNameSet = ClassUtil.scanPackage(MAPPER_BASE_PACKAGE).stream().map(Class::getName).collect(Collectors.toSet());

            for (Class<?> entityClass : entityClassSet) {

                // 只创建带有 TableName 注解的实体
                TableName tableName = entityClass.getAnnotation(TableName.class);
                if (tableName == null) {
                    continue;
                }

                String mapperClassName = MAPPER_BASE_PACKAGE + "." + entityClass.getSimpleName() + "Mapper";
                String serviceClassName = SERVICE_BASE_PACKAGE + "." + entityClass.getSimpleName() + "Service";

                // mapper、service 存在任意跳过
                if (mapperClassNameSet.contains(mapperClassName) || serviceClassNameSet.contains(serviceClassName)) {
                    continue;
                }

                /* 创建 mapper */
                Class<?> mapperClass = new ByteBuddy()
                        .makeInterface()
                        .implement(TypeDescription.Generic.Builder.parameterizedType(BaseMapper.class, entityClass).build())
                        .name(mapperClassName)
                        .annotateType(AnnotationDescription.Builder.ofType(Mapper.class).build())
                        .make()
                        .load(getClass().getClassLoader(), new ClassLoadingStrategy.ForBootstrapInjection(null, null))
                        .getLoaded();

                MapperFactoryBean<?> factoryBean = new MapperFactoryBean<>(mapperClass);
                factoryBean.setSqlSessionFactory(sqlSessionFactory);
                sqlSessionFactory.getConfiguration().addMapper(mapperClass);

                // 注册 mapper
                Object mapperInstance = null;
                try {
                    SpringUtil.registerBean(getBeanName(mapperClassName), mapperInstance = factoryBean.getObject());
                } catch (Exception e) {
                    log.warn("register BaseMapper error", e);
                }

                /* 创建 service */
                Class<?> serviceClass = new ByteBuddy()
                        .subclass(TypeDescription.Generic.Builder.parameterizedType(ServiceImpl.class, mapperClass, entityClass).build())
                        .name(serviceClassName)
                        .annotateType(AnnotationDescription.Builder.ofType(Service.class).build())
                        .make()
                        .load(getClass().getClassLoader(), new ClassLoadingStrategy.ForBootstrapInjection(null, null))
                        .getLoaded();

                // 注册 service
                try {
                    SpringUtil.registerBean(getBeanName(serviceClassName), serviceClass.newInstance());
                } catch (Exception e) {
                    log.warn("register ServiceImpl error", e);
                }

            }

        };
    }

    // 根据类名获取 bean name
    private String getBeanName(String className) {
        int index = className.lastIndexOf(".");
        String simpleClassName = index != -1 ? className.substring(index + 1) : className;

        char firstChar = simpleClassName.charAt(0);
        if (firstChar >= 'A' && firstChar <= 'Z') {
            firstChar -= 'A' - 'a';
        }
        return firstChar + simpleClassName.substring(1);
    }


}
