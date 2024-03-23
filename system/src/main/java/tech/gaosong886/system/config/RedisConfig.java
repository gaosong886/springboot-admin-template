package tech.gaosong886.system.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * Redis Template 配置类
 */
@Configuration
public class RedisConfig {
        @Bean
        RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
                RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
                template.setConnectionFactory(lettuceConnectionFactory);

                // LocalDatetime 对象序列化
                JavaTimeModule timeModule = new JavaTimeModule();
                timeModule.addDeserializer(LocalDate.class,
                                new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                timeModule.addDeserializer(LocalDateTime.class,
                                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                timeModule.addSerializer(LocalDate.class,
                                new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                timeModule.addSerializer(LocalDateTime.class,
                                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

                ObjectMapper objectMapper = new ObjectMapper();

                // 设置属性访问级别为 ALL，意味着任何级别的属性（public、protected、private等）都可以被序列化和反序列化
                // JsonAutoDetect.Visibility.ANY 表示任何可见性的属性都会被考虑
                objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

                // 激活默认的类型信息包含功能，以便在序列化 JSON 时能够保留对象的原始类型信息
                // 使用 LaissezFaireSubTypeValidator 作为子类型验证器，不进行严格的子类型验证
                // ObjectMapper.DefaultTyping.NON_FINAL 表示仅对非 final 类进行类型信息的包含
                // JsonTypeInfo.As.WRAPPER_ARRAY 表示类型信息将以包装数组的形式呈现
                objectMapper.activateDefaultTyping(
                                LaissezFaireSubTypeValidator.instance,
                                ObjectMapper.DefaultTyping.NON_FINAL,
                                JsonTypeInfo.As.WRAPPER_ARRAY);

                // 禁用 SerializationFeature 中的 WRITE_DATES_AS_TIMESTAMPS 特性
                // 在序列化日期时，不会将其转换为时间戳格式，而会使用 ISO 8601 格式
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                objectMapper.registerModule(timeModule);

                Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
                                objectMapper,
                                Object.class);

                StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
                template.setKeySerializer(stringRedisSerializer);
                template.setHashKeySerializer(stringRedisSerializer);
                template.setValueSerializer(jackson2JsonRedisSerializer);
                template.setHashValueSerializer(jackson2JsonRedisSerializer);
                template.afterPropertiesSet();
                return template;
        }
}
