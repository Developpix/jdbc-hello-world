package com.kanibl.dbms.lab;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Application {
    private final DatabaseService databaseService;
    private JedisPool jedisPool;
    private final static int TTL = 120;
    private static final String ALL_USERS_KEY = "ALL_USERS_KEY";
    private static final String USER_KEY_FORMAT = "USER_%d";

    public Application() throws Exception {
        databaseService = new DatabaseService("jdbc:derby:/tmp/my-db\n;create=true");
        databaseService.createSchema();

        this.jedisPool = new JedisPool(new JedisPoolConfig(), "127.0.0.1", 6379);

        // Initialiser le pool.
        Jedis jedis = jedisPool.getResource();
        jedis.close();
    }


    public List<Map<String, String>> getUserAllAsMap() throws Exception {
        Jedis jedis = this.jedisPool.getResource();

        String res = jedis.get(ALL_USERS_KEY);
        Gson gson = new Gson();

        if(res == null) {
            List<Map<String, String>> data = this.databaseService.getUserAllAsMap();
            res = gson.toJson(data);

            jedis.set(ALL_USERS_KEY, res, SetParams.setParams().ex(TTL));
            return data;
        }

        return gson.fromJson(res, new TypeToken<LinkedList<TreeMap<String, String>>>(){}.getType());
    }


    public Map<String, String> getUserById(int id) throws Exception {
        Jedis jedis = this.jedisPool.getResource();

        String key = String.format(USER_KEY_FORMAT, id);
        String res = jedis.get(key);
        Gson gson = new Gson();

        if(res == null) {
            Map<String, String> data = this.databaseService.getUserById(id);
            res = gson.toJson(data);

            jedis.set(key, res, SetParams.setParams().ex(TTL));
            return data;
        }

        return gson.fromJson(res, new TypeToken<TreeMap<String, String>>(){}.getType());
    }

    public void closeConnection() throws SQLException {
        this.databaseService.closeConnection();
    }
}
