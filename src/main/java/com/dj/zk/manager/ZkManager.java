package com.dj.zk.manager;

import com.dj.zk.manager.config.ConfigAll;
import org.springframework.boot.SpringApplication;

/**
 * 描述:
 * <p>
 * 入口类
 * @author : <a href="mailto:liudejian@tinman.cn">liudejian</a>
 * @version : Ver 1.0
 * @date : 2019-12-04 13:03
 */
public class ZkManager {
    public static void main(String[] args) {
        SpringApplication.run(ConfigAll.class, args);
    }
}
