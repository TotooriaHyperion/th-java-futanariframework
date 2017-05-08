package com.futanari.util;

import org.apache.log4j.Logger;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

import java.io.InputStream;
import java.util.*;

/**
 * <p>
 * redis通用工具类
 * </p>
 * 
 * @author clj 文档：
 * http://love398146779.iteye.com/blog/2124123
 * http://www.cnblogs.com/edisonfeng/p/3571870.html
 * http://blog.csdn.net/ithomer/article/details/9213185
 * 
 */
@SuppressWarnings("deprecation")
public class RedisUtil {

	private static Logger loger = Logger.getLogger(RedisUtil.class);
	private static final Object LOCK = new Object();
	// Redis服务器IP
	private static String ADDR_ARRAY = "192.168.10.223";

	// Redis的端口号
	private static int PORT = 6379;

	// 访问密码
	private static String AUTH = "";

	// 可用连接实例的最大数目，默认值为8；
	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	private static int MAX_ACTIVE = 200;

	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private static int MAX_IDLE = 10;

	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private static int MAX_WAIT = 2000;

	// 超时时间
	private static int TIMEOUT = 20000;

	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private static boolean TEST_ON_BORROW = true;

	private JedisPool pool = null;
	private Properties properties;
	private static RedisUtil instance;

	private void init() {
		try {
			properties = new Properties();
			InputStream inStream = this.getClass().getClassLoader().getResourceAsStream("redis.properties");
			properties.load(inStream);
			ADDR_ARRAY = properties.getProperty("redis.host");
			PORT = Integer.valueOf(properties.getProperty("redis.port"));
			AUTH = properties.getProperty("redis.auth");
			MAX_IDLE = Integer.valueOf(properties.getProperty("redis.maxIdle"));
			MAX_ACTIVE = Integer.valueOf(properties.getProperty("redis.maxActive"));
			MAX_WAIT = Integer.valueOf(properties.getProperty("redis.maxWait"));
			TEST_ON_BORROW = Boolean.valueOf(properties.getProperty("redis.testOnBorrow"));
			TIMEOUT = Integer.valueOf(properties.getProperty("redis.timeOut"));

			JedisPoolConfig config = new JedisPoolConfig();
			// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
			// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
			config.setMaxWaitMillis(MAX_WAIT);
			// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			config.setMaxIdle(MAX_IDLE);
			// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			config.setMaxTotal(MAX_ACTIVE);
			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(TEST_ON_BORROW);
			// pool = new JedisPool(config, "192.168.0.121", 6379, 100000);
			pool = new JedisPool(config, ADDR_ARRAY, PORT, TIMEOUT);
		} catch (Exception e) {
			loger.error("RedisUtil.init Exception:", e);
		}
	}

	private boolean isCacheEnabled() {
		boolean useCache = false;
		try {
			useCache = Boolean.valueOf(properties.getProperty("cache.enable"));
		} catch (Exception e) {
			useCache = false;
			loger.info("Please enable memcached");
		}
		return useCache;
	}

	private RedisUtil() {
		init();
	}

	public static RedisUtil getInstance() {
		if (instance == null) {
			synchronized (LOCK) {
				if (instance == null) {
					instance = new RedisUtil();
				}
			}
		}
		return instance;
	}

	/**
	 * <p>
	 * 通过key获取储存在redis中的value
	 * </p>
	 * <p>
	 * 并释放连接
	 * </p>
	 * 
	 * @param key String key
	 * @return 成功返回value 失败返回null
	 */
	public String get(String key) {
		Jedis jedis = null;
		String value = null;
		try {
			jedis = pool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return value;
	}

	/**
	 * <p>
	 * 向redis存入key和value,并释放连接资源
	 * </p>
	 * <p>
	 * 如果key已经存在 则覆盖
	 * </p>
	 * 
	 * @param key String key
	 * @param value String value
	 * @return 成功 返回OK 失败返回 0
	 */
	public String set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.set(key, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
			return "0";
		} finally {
			returnResource(pool, jedis);
		}
	}

	/**
	 * <p>
	 * 删除指定的key,也可以传入一个包含key的数组
	 * </p>
	 * 
	 * @param keys
	 *            一个key 也可以使 string 数组
	 * @return 返回删除成功的个数
	 */
	public Long del(String... keys) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.del(keys);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
			return 0L;
		} finally {
			returnResource(pool, jedis);
		}
	}

	/**
	 * <p>
	 * 通过key向指定的value值追加值
	 * </p>
	 * 
	 * @param key String key
	 * @param str String str
	 * @return 成功返回 添加后value的长度 失败 返回 添加的 value 的长度 异常返回0L
	 */
	public Long append(String key, String str) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.append(key, str);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
			return 0L;
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 判断key是否存在
	 * </p>
	 * 
	 * @param key String key
	 * @return true OR false
	 */
	public Boolean exists(String key) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.exists(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
			return false;
		} finally {
			returnResource(pool, jedis);
		}
	}

	/**
	 * <p>
	 * 设置key value,如果key已经存在则返回0,nx==> not exist
	 * </p>
	 * 
	 * @param key key
	 * @param value value
	 * @return 成功返回1 如果存在 和 发生异常 返回 0
	 */
	public Long setnx(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.setnx(key, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
			return 0L;
		} finally {
			returnResource(pool, jedis);
		}
	}

	/**
	 * <p>
	 * 设置key value并制定这个键值的有效期
	 * </p>
	 * 
	 * @param key key
	 * @param value value
	 * @param seconds
	 *            单位:秒
	 * @return 成功返回OK 失败和异常返回null
	 */
	public String setex(String key, String value, int seconds) {
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			res = jedis.setex(key, seconds, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key 和offset 从指定的位置开始将原先value替换
	 * </p>
	 * <p>
	 * 下标从0开始,offset表示从offset下标开始替换
	 * </p>
	 * <p>
	 * 如果替换的字符串长度过小则会这样
	 * </p>
	 * <p>
	 * example:
	 * </p>
	 * <p>
	 * value : bigsea@zto.cn
	 * </p>
	 * <p>
	 * str : abc
	 * </p>
	 * <P>
	 * 从下标7开始替换 则结果为
	 * </p>
	 * <p>
	 * RES : bigsea.abc.cn
	 * </p>
	 * 
	 * @param key key
	 * @param str str
	 * @param offset
	 *            下标位置
	 * @return 返回替换后 value 的长度
	 */
	public Long setrange(String key, String str, int offset) {
		Jedis jedis = null;
		try {
			jedis = pool.getResource();
			return jedis.setrange(key, offset, str);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
			return 0L;
		} finally {
			returnResource(pool, jedis);
		}
	}

	/**
	 * <p>
	 * 通过批量的key获取批量的value
	 * </p>
	 * 
	 * @param keys
	 *            string数组 也可以是一个key
	 * @return 成功返回value的集合, 失败返回null的集合 ,异常返回空
	 */
	public List<String> mget(String... keys) {
		Jedis jedis = null;
		List<String> values = null;
		try {
			jedis = pool.getResource();
			values = jedis.mget(keys);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return values;
	}

	/**
	 * <p>
	 * 批量的设置key:value,可以一个
	 * </p>
	 * <p>
	 * example:
	 * </p>
	 * <p>
	 * obj.mset(new String[]{"key2","value1","key2","value2"})
	 * </p>
	 * 
	 * @param keysvalues keysvalues
	 * @return 成功返回OK 失败 异常 返回 null
	 * 
	 */
	public String mset(String... keysvalues) {
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			res = jedis.mset(keysvalues);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 批量的设置key:value,可以一个,如果key已经存在则会失败,操作会回滚
	 * </p>
	 * <p>
	 * example:
	 * </p>
	 * <p>
	 * obj.msetnx(new String[]{"key2","value1","key2","value2"})
	 * </p>
	 * 
	 * @param keysvalues keysvalues
	 * @return 成功返回1 失败返回0
	 */
	public Long msetnx(String... keysvalues) {
		Jedis jedis = null;
		Long res = 0L;
		try {
			jedis = pool.getResource();
			res = jedis.msetnx(keysvalues);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 设置key的值,并返回一个旧值
	 * </p>
	 * 
	 * @param key key
	 * @param value value
	 * @return 旧值 如果key不存在 则返回null
	 */
	public String getset(String key, String value) {
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			res = jedis.getSet(key, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过下标 和key 获取指定下标位置的 value
	 * </p>
	 * 
	 * @param key key
	 * @param startOffset
	 *            开始位置 从0 开始 负数表示从右边开始截取
	 * @param endOffset endOffset
	 * @return 如果没有返回null
	 */
	public String getrange(String key, int startOffset, int endOffset) {
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			res = jedis.getrange(key, startOffset, endOffset);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key 对value进行加值+1操作,当value不是int类型时会返回错误,当key不存在是则value为1
	 * </p>
	 * 
	 * @param key String key
	 * @return 加值后的结果
	 */
	public Long incr(String key) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.incr(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key给指定的value加值,如果key不存在,则这是value为该值
	 * </p>
	 * 
	 * @param key String key
	 * @param integer Long key
	 * @return
	 */
	public Long incrBy(String key, Long integer) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.incrBy(key, integer);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 对key的值做减减操作,如果key不存在,则设置key为-1
	 * </p>
	 * 
	 * @param key String key
	 * @return Long
	 */
	public Long decr(String key) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.decr(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 减去指定的值
	 * </p>
	 * 
	 * @param key String key
	 * @param integer Long integer
	 * @return Long
	 */
	public Long decrBy(String key, Long integer) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.decrBy(key, integer);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key获取value值的长度
	 * </p>
	 * 
	 * @param key String key
	 * @return 失败返回null
	 */
	public Long serlen(String key) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.strlen(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key给field设置指定的值,如果key不存在,则先创建
	 * </p>
	 * 
	 * @param key String key
	 * @param field
	 *            字段
	 * @param value String value
	 * @return 如果存在返回0 异常返回null
	 */
	public Long hset(String key, String field, String value) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.hset(key, field, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key给field设置指定的值,如果key不存在则先创建,如果field已经存在,返回0
	 * </p>
	 *
	 * @param key String key
	 * @param field
	 *            字段
	 * @param value String value
	 * @return Long
	 */
	public Long hsetnx(String key, String field, String value) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.hsetnx(key, field, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key同时设置 hash的多个field
	 * </p>
	 * 
	 * @param key
	 * @param hash
	 * @return 返回OK 异常返回null
	 */
	public String hmset(String key, Map<String, String> hash) {
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			res = jedis.hmset(key, hash);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key 和 field 获取指定的 value
	 * </p>
	 * 
	 * @param key
	 * @param field
	 * @return 没有返回null
	 */
	public String hget(String key, String field) {
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			res = jedis.hget(key, field);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key 和 fields 获取指定的value 如果没有对应的value则返回null
	 * </p>
	 * 
	 * @param key
	 * @param fields
	 *            可以使 一个String 也可以是 String数组
	 * @return
	 */
	public List<String> hmget(String key, String... fields) {
		Jedis jedis = null;
		List<String> res = null;
		try {
			jedis = pool.getResource();
			res = jedis.hmget(key, fields);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key给指定的field的value加上给定的值
	 * </p>
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public Long hincrby(String key, String field, Long value) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.hincrBy(key, field, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key和field判断是否有指定的value存在
	 * </p>
	 * 
	 * @param key
	 * @param field
	 * @return
	 */
	public Boolean hexists(String key, String field) {
		Jedis jedis = null;
		Boolean res = false;
		try {
			jedis = pool.getResource();
			res = jedis.hexists(key, field);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key返回field的数量
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	public Long hlen(String key) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.hlen(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;

	}

	/**
	 * <p>
	 * 通过key 删除指定的 field
	 * </p>
	 * 
	 * @param key
	 * @param fields
	 *            可以是 一个 field 也可以是 一个数组
	 * @return
	 */
	public Long hdel(String key, String... fields) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.hdel(key, fields);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key返回所有的field
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> hkeys(String key) {
		Jedis jedis = null;
		Set<String> res = null;
		try {
			jedis = pool.getResource();
			res = jedis.hkeys(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key返回所有和key有关的value
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	public List<String> hvals(String key) {
		Jedis jedis = null;
		List<String> res = null;
		try {
			jedis = pool.getResource();
			res = jedis.hvals(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key获取所有的field和value
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	public Map<String, String> hgetall(String key) {
		Jedis jedis = null;
		Map<String, String> res = null;
		try {
			jedis = pool.getResource();
			res = jedis.hgetAll(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key向list头部添加字符串
	 * </p>
	 * 
	 * @param key
	 * @param strs
	 *            可以使一个string 也可以使string数组
	 * @return 返回list的value个数
	 */
	public Long lpush(String key, String... strs) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.lpush(key, strs);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key向list尾部添加字符串
	 * </p>
	 * 
	 * @param key
	 * @param strs
	 *            可以使一个string 也可以使string数组
	 * @return 返回list的value个数
	 */
	public Long rpush(String key, String... strs) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.rpush(key, strs);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key在list指定的位置之前或者之后 添加字符串元素
	 * </p>
	 * 
	 * @param key
	 * @param where
	 *            LIST_POSITION枚举类型
	 * @param pivot
	 *            list里面的value
	 * @param value
	 *            添加的value
	 * @return
	 */
	public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.linsert(key, where, pivot, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key设置list指定下标位置的value
	 * </p>
	 * <p>
	 * 如果下标超过list里面value的个数则报错
	 * </p>
	 * 
	 * @param key
	 * @param index
	 *            从0开始
	 * @param value
	 * @return 成功返回OK
	 */
	public String lset(String key, Long index, String value) {
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			res = jedis.lset(key, index, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key从对应的list中删除指定的count个 和 value相同的元素
	 * </p>
	 * 
	 * @param key
	 * @param count
	 *            当count为0时删除全部
	 * @param value
	 * @return 返回被删除的个数
	 */
	public Long lrem(String key, long count, String value) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.lrem(key, count, value);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key保留list中从strat下标开始到end下标结束的value值
	 * </p>
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return 成功返回OK
	 */
	public String ltrim(String key, long start, long end) {
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			res = jedis.ltrim(key, start, end);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key从list的头部删除一个value,并返回该value
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	public String lpop(String key) {
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			res = jedis.lpop(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key从list尾部删除一个value,并返回该元素
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	public String rpop(String key) {
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			res = jedis.rpop(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key从一个list的尾部删除一个value并添加到另一个list的头部,并返回该value
	 * </p>
	 * <p>
	 * 如果第一个list为空或者不存在则返回null
	 * </p>
	 * 
	 * @param srckey
	 * @param dstkey
	 * @return
	 */
	public String rpoplpush(String srckey, String dstkey) {
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			res = jedis.rpoplpush(srckey, dstkey);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key获取list中指定下标位置的value
	 * </p>
	 * 
	 * @param key
	 * @param index
	 * @return 如果没有返回null
	 */
	public String lindex(String key, long index) {
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			res = jedis.lindex(key, index);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key返回list的长度
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	public Long llen(String key) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.llen(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key获取list指定下标位置的value
	 * </p>
	 * <p>
	 * 如果start 为 0 end 为 -1 则返回全部的list中的value
	 * </p>
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> lrange(String key, long start, long end) {
		Jedis jedis = null;
		List<String> res = null;
		try {
			jedis = pool.getResource();
			res = jedis.lrange(key, start, end);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key向指定的set中添加value
	 * </p>
	 * 
	 * @param key
	 * @param members
	 *            可以是一个String 也可以是一个String数组
	 * @return 添加成功的个数
	 */
	public Long sadd(String key, String... members) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.sadd(key, members);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key删除set中对应的value值
	 * </p>
	 * 
	 * @param key
	 * @param members
	 *            可以是一个String 也可以是一个String数组
	 * @return 删除的个数
	 */
	public Long srem(String key, String... members) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.srem(key, members);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key随机删除一个set中的value并返回该值
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	public String spop(String key) {
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			res = jedis.spop(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key获取set中的差集
	 * </p>
	 * <p>
	 * 以第一个set为标准
	 * </p>
	 * 
	 * @param keys
	 *            可以使一个string 则返回set中所有的value 也可以是string数组
	 * @return
	 */
	public Set<String> sdiff(String... keys) {
		Jedis jedis = null;
		Set<String> res = null;
		try {
			jedis = pool.getResource();
			res = jedis.sdiff(keys);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key获取set中的差集并存入到另一个key中
	 * </p>
	 * <p>
	 * 以第一个set为标准
	 * </p>
	 * 
	 * @param dstkey
	 *            差集存入的key
	 * @param keys
	 *            可以使一个string 则返回set中所有的value 也可以是string数组
	 * @return
	 */
	public Long sdiffstore(String dstkey, String... keys) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.sdiffstore(dstkey, keys);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key获取指定set中的交集
	 * </p>
	 * 
	 * @param keys
	 *            可以使一个string 也可以是一个string数组
	 * @return
	 */
	public Set<String> sinter(String... keys) {
		Jedis jedis = null;
		Set<String> res = null;
		try {
			jedis = pool.getResource();
			res = jedis.sinter(keys);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key获取指定set中的交集 并将结果存入新的set中
	 * </p>
	 * 
	 * @param dstkey
	 * @param keys
	 *            可以使一个string 也可以是一个string数组
	 * @return
	 */
	public Long sinterstore(String dstkey, String... keys) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.sinterstore(dstkey, keys);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key返回所有set的并集
	 * </p>
	 * 
	 * @param keys
	 *            可以使一个string 也可以是一个string数组
	 * @return
	 */
	public Set<String> sunion(String... keys) {
		Jedis jedis = null;
		Set<String> res = null;
		try {
			jedis = pool.getResource();
			res = jedis.sunion(keys);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key返回所有set的并集,并存入到新的set中
	 * </p>
	 * 
	 * @param dstkey
	 * @param keys
	 *            可以使一个string 也可以是一个string数组
	 * @return
	 */
	public Long sunionstore(String dstkey, String... keys) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.sunionstore(dstkey, keys);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key将set中的value移除并添加到第二个set中
	 * </p>
	 * 
	 * @param srckey
	 *            需要移除的
	 * @param dstkey
	 *            添加的
	 * @param member
	 *            set中的value
	 * @return
	 */
	public Long smove(String srckey, String dstkey, String member) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.smove(srckey, dstkey, member);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key获取set中value的个数
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	public Long scard(String key) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.scard(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key判断value是否是set中的元素
	 * </p>
	 * 
	 * @param key
	 * @param member
	 * @return
	 */

	public Boolean sismember(String key, String member) {
		Jedis jedis = null;
		Boolean res = null;
		try {
			jedis = pool.getResource();
			res = jedis.sismember(key, member);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key获取set中随机的value,不删除元素
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	public String srandmember(String key) {
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			res = jedis.srandmember(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key获取set中所有的value
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> smembers(String key) {
		Jedis jedis = null;
		Set<String> res = null;
		try {
			jedis = pool.getResource();
			res = jedis.smembers(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key向zset中添加value,score,其中score就是用来排序的
	 * </p>
	 * <p>
	 * 如果该value已经存在则根据score更新元素
	 * </p>
	 * 
	 * @param key
	 * @param scoreMembers
	 * @return
	 */
	public Long zadd(String key, Map<String, Double> scoreMembers) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.zadd(key, scoreMembers);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key向zset中添加value,score,其中score就是用来排序的
	 * </p>
	 * <p>
	 * 如果该value已经存在则根据score更新元素
	 * </p>
	 *
	 * @param key String key
	 * @param score double score
	 * @param member String member
	 * @return Long
	 */
	public Long zadd(String key, double score, String member) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.zadd(key, score, member);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key删除在zset中指定的value
	 * </p>
	 *
	 * @param key String key
	 * @param members String... members
	 *            可以使一个string 也可以是一个string数组
	 * @return Long
	 */
	public Long zrem(String key, String... members) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.zrem(key, members);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key增加该zset中value的score的值
	 * </p>
	 *
	 * @param key String key
	 * @param score double score
	 * @param member String member
	 * @return Long
	 */
	public Double zincrby(String key, double score, String member) {
		Jedis jedis = null;
		Double res = null;
		try {
			jedis = pool.getResource();
			res = jedis.zincrby(key, score, member);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key返回zset中value的排名
	 * </p>
	 * <p>
	 * 下标从小到大排序
	 * </p>
	 *
	 * @param key String key
	 * @param member String member
	 * @return Long
	 */
	public Long zrank(String key, String member) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.zrank(key, member);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key返回zset中value的排名
	 * </p>
	 * <p>
	 * 下标从大到小排序
	 * </p>
	 * 
	 * @param key String key
	 * @param member String member
	 * @return Long
	 */
	public Long zrevrank(String key, String member) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.zrevrank(key, member);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key将获取score从start到end中zset的value
	 * </p>
	 * <p>
	 * socre从大到小排序
	 * </p>
	 * <p>
	 * 当start为0 end为-1时返回全部
	 * </p>
	 * 
	 * @param key String key
	 * @param start long start
	 * @param end long end
	 * @return Set<String>
	 */
	public Set<String> zrevrange(String key, long start, long end) {
		Jedis jedis = null;
		Set<String> res = null;
		try {
			jedis = pool.getResource();
			res = jedis.zrevrange(key, start, end);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key返回指定score内zset中的value
	 * </p>
	 *
	 * @param key String key
	 * @param min String min
	 * @param max String max
	 * @return Long
	 */
	public Set<String> zrangebyscore(String key, String max, String min) {
		Jedis jedis = null;
		Set<String> res = null;
		try {
			jedis = pool.getResource();
			res = jedis.zrevrangeByScore(key, max, min);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key返回指定score内zset中的value
	 * </p>
	 *
	 * @param key String key
	 * @param min double min
	 * @param max double max
	 * @return Long
	 */
	public Set<String> zrangeByScore(String key, double max, double min) {
		Jedis jedis = null;
		Set<String> res = null;
		try {
			jedis = pool.getResource();
			res = jedis.zrevrangeByScore(key, max, min);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 返回指定区间内zset中value的数量
	 * </p>
	 * 
	 * @param key String key
	 * @param min String min
	 * @param max String max
	 * @return Long
	 */
	public Long zcount(String key, String min, String max) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.zcount(key, min, max);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key返回zset中的value个数
	 * </p>
	 * 
	 * @param key String key
	 * @return Long
	 */
	public Long zcard(String key) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.zcard(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key获取zset中value的score值
	 * </p>
	 *
	 * @param key String key
	 * @param member String member
	 * @return Double
	 */
	public Double zscore(String key, String member) {
		Jedis jedis = null;
		Double res = null;
		try {
			jedis = pool.getResource();
			res = jedis.zscore(key, member);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key删除给定区间内的元素
	 * </p>
	 * 
	 * @param key String key
	 * @param start long start
	 * @param end long end
	 * @return Long
	 */
	public Long zremrangeByRank(String key, long start, long end) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.zremrangeByRank(key, start, end);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key删除指定score内的元素
	 * </p>
	 * 
	 * @param key String key
	 * @param start double start
	 * @param end double end
	 * @return Long
	 */
	public Long zremrangeByScore(String key, double start, double end) {
		Jedis jedis = null;
		Long res = null;
		try {
			jedis = pool.getResource();
			res = jedis.zremrangeByScore(key, start, end);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 返回满足pattern表达式的所有key
	 * </p>
	 * <p>
	 * keys(*)
	 * </p>
	 * <p>
	 * 返回所有的key
	 * </p>
	 * 
	 * @param pattern String
	 * @return Set<String>
	 */
	public Set<String> keys(String pattern) {
		Jedis jedis = null;
		Set<String> res = null;
		try {
			jedis = pool.getResource();
			res = jedis.keys(pattern);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}

	/**
	 * <p>
	 * 通过key判断值得类型
	 * </p>
	 * 
	 * @param key key
	 * @return String
	 */
	public String type(String key) {
		Jedis jedis = null;
		String res = null;
		try {
			jedis = pool.getResource();
			res = jedis.type(key);
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
	
    /**
     * 获得JedisPool
     * @return JedisPool
     */
    public JedisPool getJedisPool(){
    	return this.pool;
    }
    
	/**
	 * 返还到连接池
	 * 
	 * @param pool JedisPool
	 * @param jedis Jedis
	 */
	private static void returnResource(JedisPool pool, Jedis jedis) {
		if (jedis != null && pool != null) {
			pool.returnResource(jedis);
		}
	}

	public static void main(String[] args) throws Exception {
		Set<String> keys = RedisUtil.getInstance().keys("*");
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String key = it.next();
			System.out.println(key);
		}
	}

	public Pipeline pipelined() {
		Jedis jedis = null;
		Pipeline res = null;
		try {
			jedis = pool.getResource();
			res = jedis.pipelined();
		} catch (Exception e) {
			pool.returnBrokenResource(jedis);
			loger.error("{}", e);
		} finally {
			returnResource(pool, jedis);
		}
		return res;
	}
}
