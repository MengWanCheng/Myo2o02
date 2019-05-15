package com.imooc.o2o.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 强指定redis的JedisPool接口构造函数，创建jedispool
 * @author Administrator
 *
 */
public class JedisPoolWriper {
	
	private JedisPool jedisPool;
	/**
	 * 构造函数
	 * @param poolConfig
	 * @param host
	 * @param port
	 */
	public JedisPoolWriper(final JedisPoolConfig poolConfig, final String host,
			final int port) {
		try {
			jedisPool = new JedisPool(poolConfig, host, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public JedisPool getJedisPool() {
		return jedisPool;
	}
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
}
