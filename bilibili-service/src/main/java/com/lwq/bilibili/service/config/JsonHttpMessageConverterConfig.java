package com.lwq.bilibili.service.config;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

//@Configuration注解表示这是一个配置类，@Bean注解表示这是一个bean，@Primary注解表示这个bean是首选的，如果有多个bean都是首选的，那么会报错
@Configuration
class JsonHttpMessageConverterConfig {
    //解释下面代码的作用
    //SpringBoot默认使用的json解析框架是jackson，但是jackson在解析json时，如果key值为null，会报错，而fastjson不会报错，所以这里使用fastjson来解析json
    //fastjson的优点：速度快，使用方便，支持多种json格式，支持循环引用，支持自定义序列化和反序列化，支持自定义输出格式
    //fastjson的缺点：不支持xml格式，不支持json和java的互相转换，不支持json和xml的互相转换，不支持json和bean的互相转换
    //fastjson的使用：https://www.cnblogs.com/softidea/p/5916700.html
    @Bean
    @Primary
    public HttpMessageConverters fastJsonHttpMessageConvertes() {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat, // 格式化
                SerializerFeature.WriteNullStringAsEmpty, // 字符串null返回空字符串
                SerializerFeature.WriteNullListAsEmpty, // 空集合返回空数组
                SerializerFeature.WriteMapNullValue, // 空对象返回空对象
                SerializerFeature.MapSortField, // 排序
                SerializerFeature.DisableCircularReferenceDetect// 禁止循环引用
        );
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(fastJsonHttpMessageConverter);
    }
}