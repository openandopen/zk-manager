package com.dj.zk.manager.utils.json;

import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;

/**
 * TODO liudejian 类描述.
 *
 * @author : <a href="mailto:zuiwoxing@qq.com">liudejian</a>
 * @version : Ver 1.0
 * @date : 2015-10-15 下午4:28:55
 */
public class JsonSerializerProvider extends DefaultSerializerProvider {

    /**
     * serialVersionUID:TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = -2891437067035919790L;

    public JsonSerializerProvider() {
        super();
    }

    public JsonSerializerProvider(JsonSerializerProvider src) {
        super(src);
    }

    protected JsonSerializerProvider(SerializerProvider src,
                                     SerializationConfig config, SerializerFactory f) {
        super(src, config, f);
    }

    @Override
    public DefaultSerializerProvider copy() {
        if (getClass() != JsonSerializerProvider.class) {
            return super.copy();
        }
        return new JsonSerializerProvider(this);
    }

    @Override
    public JsonSerializerProvider createInstance(
            SerializationConfig config, SerializerFactory jsf) {
        return new JsonSerializerProvider(this, config, jsf);
    }
}
