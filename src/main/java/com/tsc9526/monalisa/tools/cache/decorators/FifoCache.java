package com.tsc9526.monalisa.tools.cache.decorators;

import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;

import com.tsc9526.monalisa.tools.cache.Cache;

/**
 * FIFO (first in, first out) cache decorator
 */
public class FifoCache implements Cache {

	private final Cache delegate;
	private final LinkedList<Object> keyList;
	private int size;

	public FifoCache(Cache delegate) {
		this.delegate = delegate;
		this.keyList = new LinkedList<Object>();
		this.size = 1024;
	}

	public String getId() {
		return delegate.getId();
	}

	public int getSize() {
		return delegate.getSize();
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void putObject(Object key, Object value,long ttlInSeconds) {
		cycleKeyList(key);
		delegate.putObject(key, value,ttlInSeconds);
	}

	public Object getObject(Object key) {
		return delegate.getObject(key);
	}

	public Object removeObject(Object key) {
		return delegate.removeObject(key);
	}

	public void clear() {
		delegate.clear();
		keyList.clear();
	}

	public ReadWriteLock getReadWriteLock() {
		return delegate.getReadWriteLock();
	}

	private void cycleKeyList(Object key) {
		keyList.addLast(key);
		if (keyList.size() > size) {
			Object oldestKey = keyList.removeFirst();
			delegate.removeObject(oldestKey);
		}
	}

}
