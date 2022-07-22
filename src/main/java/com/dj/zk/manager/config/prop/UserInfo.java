package com.dj.zk.manager.config.prop;

import lombok.Data;

import java.io.Serializable;

/**
 * 描述:
 *
 * @author : <a href="mailto:zuiwoxing@qq.com">dejian.liu</a>
 * @version : Ver 1.0
 * @date : 2022-07-22 15:41
 */
@Data
public class UserInfo implements Serializable {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码MD5加载后的结果
     */
    private String userPwd;

    /**
     * 1=admin
     * 2=一般用户
     */
    private Integer userType;
}
