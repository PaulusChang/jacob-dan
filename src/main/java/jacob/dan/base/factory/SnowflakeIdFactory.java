package jacob.dan.base.factory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;

import org.springframework.core.io.UrlResource;

import jacob.dan.base.constant.Constant;

public class SnowflakeIdFactory {
	
	private static SnowflakeIdFactory factory = null;
	private static final String PROPERTIES_PATH = Constant.CONF_PATH + "snowflake.properties";

	private final long twepoch = 1288834974657L;
	private final long workerIdBits = 5L;
	private final long datacenterIdBits = 5L;
	private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
	private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
	private final long sequenceBits = 12L;
	private final long workerIdShift = sequenceBits;
	private final long datacenterIdShift = sequenceBits + workerIdBits;
	private final long timestampLeftShift = sequenceBits + workerIdBits
			+ datacenterIdBits;
	private final long sequenceMask = -1L ^ (-1L << sequenceBits);

	private long workerId;
	private long datacenterId;
	private long sequence = 0L;
	private long lastTimestamp = -1L;

	static {
		new SnowflakeIdFactory();
		UrlResource urlResource = null;
		try {
			urlResource = new UrlResource(PROPERTIES_PATH);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		Properties prop = new Properties();
		/*
		 * java.util.Properties
		 * 该类可以解析properties文件，并将
		 * 内容以类似Map的形式给我们使用。
		 * "="左面为key,"="右面为value.
		 */
		try {
			prop.load(urlResource.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long workerId = Long.valueOf(prop.getProperty("workerId"));
		if (workerId > factory.maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format(
					"worker Id can't be greater than %d or less than 0",
					factory.maxWorkerId));
		}
		factory.workerId = Long.valueOf(prop.getProperty("workerId"));
		
		Long datacenterId = Long.valueOf(prop.getProperty("datacenterId"));
		if (datacenterId > factory.maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(String.format(
					"datacenter Id can't be greater than %d or less than 0",
					factory.maxDatacenterId));
		}
		factory.datacenterId = Long.valueOf(prop.getProperty("datacenterId"));
	}
	
	/**
	 * Spring加载的时候会执行
	 */
	public SnowflakeIdFactory() {
		if (null != factory) {
			return;
		}
		factory = this;
	}

	public static SnowflakeIdFactory getInstance() {
		return factory;
	}

	public synchronized long nextId() {
		long timestamp = timeGen();
		if (timestamp < lastTimestamp) {
			// 服务器时钟被调整了,ID生成器停止服务.
			throw new RuntimeException(
					String.format(
							"Clock moved backwards.  Refusing to generate id for %d milliseconds",
							lastTimestamp - timestamp));
		}
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0L;
		}

		lastTimestamp = timestamp;
		return ((timestamp - twepoch) << timestampLeftShift)
				| (datacenterId << datacenterIdShift)
				| (workerId << workerIdShift) | sequence;
	}

	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp <= lastTimestamp) {
			timestamp = timeGen();
		}
		return timestamp;
	}

	protected long timeGen() {
		return System.currentTimeMillis();
	}

	public static void main(String[] args) {
		
		SnowflakeIdFactory factory = new SnowflakeIdFactory();
		for (int i = 0; i < 2; i++) {
			System.out.println(factory.nextId());;
		}

	}

	public void initWorkerId(long workerId) {
		if (this.workerId != 0) {
			throw new RuntimeException("SnowflakeIdFactory.workerId已经完成初始化");
		}
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(String.format(
					"worker Id can't be greater than %d or less than 0",
					maxWorkerId));
		}
		this.workerId = workerId;
	}

	public void initDatacenterId(long datacenterId) {
		if (this.datacenterId != 0) {
			throw new RuntimeException("SnowflakeIdFactory.datacenterId已经完成初始化");
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(String.format(
					"datacenter Id can't be greater than %d or less than 0",
					maxDatacenterId));
		}
		this.datacenterId = datacenterId;
	}

	public String nextStringId() {
		return String.valueOf(nextId());
	}

	public long nextLongId() {
		return nextId();
	}
}