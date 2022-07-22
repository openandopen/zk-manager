package com.dj.zk.manager.config.prop;

import com.dj.zk.manager.exceptions.GeneralException;
import com.dj.zk.manager.utils.json.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.client.ConnectStringParser;
import org.apache.zookeeper.client.HostProvider;
import org.apache.zookeeper.client.StaticHostProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.CollectionUtils;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述:
 *
 * @author : <a href="mailto:zuiwoxing@qq.com">dejian.liu</a>
 * @version : Ver 1.0
 * @date : 2022-07-22 14:12
 */
@Accessors(chain = true)
@Data
@ConfigurationProperties(value = "com.dj.zk")
public class ZkProperties {
    static ArrayList<InetSocketAddress> zooCacheLists = new ArrayList<InetSocketAddress>();
    /**
     * 用户信息
     */
    public final static ConcurrentHashMap<String, UserInfo> userMap = new ConcurrentHashMap<String, UserInfo>();

    /**
     * key = host:port
     * value = InetSocketAddress
     */
    static Map<String, InetSocketAddress> cacheZkHostMap = new ConcurrentHashMap<String, InetSocketAddress>();

    private Boolean enableLog = true;

    /**
     * zookeeper连接地址
     */
    private String zkAddress;

    /**
     * digest secret(与authType配合使用)
     */
    private String secret;

    private String authType = "digest";
    /**
     * 根节点
     */
    private String rootPath = "/";




    /**
     * 用户列表
     */
    private List<UserInfo> userInfos;


    static HostProvider hostProvider = null;

    public String getZookeeperConnectUrl() {

        if (StringUtils.isEmpty(this.zkAddress)) {
            throw new GeneralException("zkAddress can't null !");
        }
        return this.zkAddress;
    }

    public String getDefaultRoot() {
        String root = this.rootPath;
        if (StringUtils.isEmpty(root)) {
            throw new GeneralException("rootPath can't null!");
        }
        if (!root.startsWith("/")) {
            return "/" + root;
        }
        return root;
    }

    public InetSocketAddress getZkHost(String host) {
        if (cacheZkHostMap.isEmpty()) {
            getZooAddresses();
        }
       return cacheZkHostMap.get(host);

    }

    /**
     * @return
     * @description 取得zoo地址列表
     */
    public List<InetSocketAddress> getZooAddresses() {
        String zooUrls = getZookeeperConnectUrl();
        ConnectStringParser parse = new ConnectStringParser(zooUrls);
        if (zooCacheLists.size() > 0) {
            return zooCacheLists;
        }
        List<InetSocketAddress> resList = parse.getServerAddresses();
        for (InetSocketAddress net : resList) {
            cacheZkHostMap.put(net.toString(), net);
        }
        zooCacheLists.addAll(resList);
        return zooCacheLists;
    }

    /**
     * @description 取得轮循zoo地址
     * @return
     */
    public static InetSocketAddress getLoopZooAddress() {
        if(hostProvider != null) {
            try {
                hostProvider = new StaticHostProvider(zooCacheLists);
            } catch (Exception e) {
            }
        }
        return hostProvider.next(10);
    }


    public UserInfo getUser(String userName) {
        if (!userMap.isEmpty()) {
           return userMap.get(userName);
        }
        List<UserInfo> userInfosList = this.getUserInfos();
        for (UserInfo user : userInfosList) {
            userMap.put(user.getUserName(), user);
        }
        return userMap.get(userName);
    }

}
