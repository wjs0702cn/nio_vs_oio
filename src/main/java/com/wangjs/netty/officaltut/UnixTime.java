package com.wangjs.netty.officaltut;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UnixTime {
	private final long time;

	public UnixTime(long time) {
		super();
		this.time = time;
	}

	public long getTime() {
		return time;
	}

	@Override
	public String toString() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
			.format(new Date((time-2208988800L)*1000L));
	}

}
