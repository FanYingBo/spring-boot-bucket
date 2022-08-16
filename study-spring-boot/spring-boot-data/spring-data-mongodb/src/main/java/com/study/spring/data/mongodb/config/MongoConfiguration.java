package com.study.spring.data.mongodb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoConfiguration {

    /**
     * 添加converter ，避免在collection中文本出现
     * _class: 'com.study.spring.data.mongodb.pojo.Book'
     * @param databaseFactory
     * @param mappingContext
     * @return
     */
    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory databaseFactory,
                                                       MongoMappingContext mappingContext){
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(databaseFactory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, mappingContext);
        mappingConverter.setTypeMapper(defaultMongoTypeMapper());
        mappingConverter.setCustomConversions(customConversions());
        return mappingConverter;
    }
    @Bean
    public MongoTypeMapper defaultMongoTypeMapper() {
        return new DefaultMongoTypeMapper(null);
    }

    @Bean
    public MongoCustomConversions customConversions(){
        List<Converter> converters = new ArrayList<>();
        converters.add(new BigDecimalToDecimal128Converter());
        converters.add(new Decimal128ToBigDecimalConverter());
        return new MongoCustomConversions(converters);
    }
}
