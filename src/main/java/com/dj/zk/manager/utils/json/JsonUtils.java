package com.dj.zk.manager.utils.json;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.databind.ser.std.NullSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @version Ver 1.0
 * @description: jsonUtils 工具类
 * @Date 2013-4-23 下午12:32:29
 */
@Slf4j
public class JsonUtils {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 增加ObjectMapper 对象池模式，提高性能
     */
    public static ConcurrentLinkedQueue<ObjectMapper> MAPPER_QUEUE = new ConcurrentLinkedQueue<ObjectMapper>();
    static boolean isPretty = false;
    static JsonDateFormat defaultDateFormat = new JsonDateFormat(DEFAULT_DATE_FORMAT);
    static JsonSerializerProvider sp = new JsonSerializerProvider();

    static {
        sp.setNullValueSerializer(NullSerializer.instance);

    }

    public static Map<JsonGenerator.Feature, Boolean> USER_FEATURE = new HashMap<>();

    /**
     * 获取mapper对象
     *
     * @return
     * @author : <a href="mailto:zuiwoxing@qq.com">liudejian</a>  2014-12-10 下午2:28:03
     */
    private static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = MAPPER_QUEUE.poll();
        if (mapper == null) {
            mapper = new ObjectMapper(null, sp, null);
            //将数字作来字符串输出(1.为了兼容json-lib-2.4-jdk1.5, 2.长整型在返回前端页面时JS无法处理，将丢失后面的尾数)
            mapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, Boolean.TRUE);
            if (!USER_FEATURE.isEmpty()) {
                for (Iterator<Map.Entry<JsonGenerator.Feature, Boolean>> it = USER_FEATURE.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<JsonGenerator.Feature, Boolean> entry = it.next();
                    mapper.configure(entry.getKey(), entry.getValue());
                }
            }
        }
        return mapper;
    }

    /**
     * 返回ObjectMapper对象
     *
     * @param mapper
     * @author : <a href="mailto:zuiwoxing@qq.com">liudejian</a>  2014-12-10 下午2:28:14
     */
    private static void returnMapper(ObjectMapper mapper) {
        if (mapper != null) {
            MAPPER_QUEUE.offer(mapper);
        }
    }

    public static boolean isPretty() {
        return isPretty;
    }

    public static void setPretty(boolean isPretty) {
        JsonUtils.isPretty = isPretty;
    }

    /**
     * JSON串转换为Java泛型对象，可以是各种类型，此方法最为强大。用法看测试用例。
     *
     * @param jsonString SON字符串
     * @param tr         TypeReference,例如: new TypeReference< List<FamousUser> >(){}
     * @param <T>
     * @return
     */
    public static <T> T json2GenericObject(String jsonString, TypeReference<T> tr) {
        return json2GenericObject(jsonString, tr, DEFAULT_DATE_FORMAT);
    }

    /**
     * JSON串转换为Java泛型对象，可以是各种类型，此方法最为强大。用法看测试用例。
     *
     * @param <T>
     * @param jsonString JSON字符串
     * @param tr         TypeReference,例如: new TypeReference< List<FamousUser> >(){}
     * @return List对象列表
     */
    public static <T> T json2GenericObject(String jsonString, TypeReference<T> tr, String dateFormat) {
        ObjectMapper objectMapper = null;
        if (StringUtils.isNotEmpty(jsonString)) {
            try {
                objectMapper = getObjectMapper();
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

                if (StringUtils.isBlank(dateFormat)) {
                    objectMapper.setDateFormat(defaultDateFormat);
                } else {
                    objectMapper.setDateFormat(new JsonDateFormat(dateFormat));
                }
                return (T) objectMapper.readValue(jsonString, tr);
            } catch (Exception e) {
                log.error("parse json[{}] error:{}", jsonString, e.getMessage());
            } finally {
                returnMapper(objectMapper);
            }
        }
        return null;
    }

    /**
     * Json字符串转Java对象
     *
     * @param jsonString
     * @param c
     * @param <T>
     * @return
     */
    public static <T> T json2Object(String jsonString, Class<T> c) {
        return json2Object(jsonString, c, DEFAULT_DATE_FORMAT);
    }

    /**
     * Json字符串转Java对象
     *
     * @param jsonString
     * @param c
     * @return
     */
    public static <T> T json2Object(String jsonString, Class<T> c, String dateFormat) {
        ObjectMapper objectMapper = null;
        if (StringUtils.isNotEmpty(jsonString)) {
            try {
                objectMapper = getObjectMapper();
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                if (StringUtils.isEmpty(dateFormat)) {
                    objectMapper.setDateFormat(defaultDateFormat);
                } else {
                    objectMapper.setDateFormat(new JsonDateFormat(dateFormat));
                }
                return (T) objectMapper.readValue(jsonString, c);
            } catch (Exception e) {
                log.error("parse json[{}] error:{}", jsonString, e.getMessage());
            } finally {
                returnMapper(objectMapper);
            }
        }
        return null;
    }


    /**
     * Java对象转Json字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        return toJson(object, null, null, DEFAULT_DATE_FORMAT);
    }

    public static String toJson(Object object, boolean isPretty) {
        return toJson(object, null, null, DEFAULT_DATE_FORMAT, isPretty);
    }

    /**
     * Java对象转Json字符串
     *
     * @param object
     * @param dateFormat
     * @return
     */
    public static String toJson(Object object, String dateFormat) {
        return toJson(object, null, null, dateFormat);
    }

    public static String toJson(Object object, String dateFormat, boolean isPretty) {
        return toJson(object, null, null, dateFormat, isPretty);
    }

    /**
     * @param object
     * @param executeFields
     * @param includeFields
     * @param dateFormat
     * @return
     */
    public static String toJson(Object object, String[] executeFields,
                                String[] includeFields, String dateFormat) {
        return toJson(object, executeFields, includeFields, dateFormat, false);
    }

    /**
     * Java对象转Json字符串
     *
     * @param object        目标对象
     * @param executeFields 排除字段
     * @param includeFields 包含字段
     * @param dateFormat    时间格式化
     * @return
     */
    public static String toJson(Object object, String[] executeFields,
                                String[] includeFields, String dateFormat, boolean isPretty) {
        String jsonString = "";
        ObjectMapper objectMapper = null;
        try {
            JsonSerializerFactory bidBeanFactory = JsonSerializerFactory.instance;
            objectMapper = getObjectMapper();

            if (StringUtils.isEmpty(dateFormat)) {
                objectMapper.setDateFormat(defaultDateFormat);
            } else {
                objectMapper.setDateFormat(new JsonDateFormat(dateFormat));
            }
            if (includeFields != null) {
                String filterId = "includeFilter";
                objectMapper.setFilterProvider(new SimpleFilterProvider()
                        .addFilter(filterId, SimpleBeanPropertyFilter.filterOutAllExcept(includeFields)));
                bidBeanFactory.setFilterId(filterId);
                objectMapper.setSerializerFactory(bidBeanFactory);

            } else if (includeFields == null && executeFields != null) {
                String filterId = "executeFilter";
                FilterProvider filterProvider = new SimpleFilterProvider()
                        .addFilter(filterId,
                                SimpleBeanPropertyFilter.serializeAllExcept(executeFields));
                objectMapper.setFilterProvider(filterProvider);
                bidBeanFactory.setFilterId(filterId);
                objectMapper.setSerializerFactory(bidBeanFactory);

            }
            if (isPretty) {
                jsonString = objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(object);
            } else {
                jsonString = objectMapper.writeValueAsString(object);
            }
        } catch (Exception e) {
            log.error("json to object error:{}", e.getMessage(), e);
        } finally {
            returnMapper(objectMapper);
        }
        return jsonString;
    }


}
