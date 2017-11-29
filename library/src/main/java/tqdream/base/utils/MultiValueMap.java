package tqdream.base.base.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * 版权所有 (c) 2012 北京新媒传信科技有限公司。 保留所有权利。<br>
 * 项目名： 飞信 - Android客户端<br>
 * 描述： 一个Key对应多个Value的Map<br>
 * @param <K>
 * @param <V>
 *  
 * @version 1.0
 * @since JDK1.5
 */
public class MultiValueMap<K, V> {
	private Map<K, List<V>> mMap;

	public MultiValueMap() {
		this(0);
	}

	public MultiValueMap(int capacity) {
		mMap = new ConcurrentHashMap<K, List<V>>(capacity);
	}

	public List<V> get(K key) {
		return mMap.get(key);
	}

	public void put(K key, V value) {
		List<V> values = mMap.get(key);
		if (values == null) {
			values = new ArrayList<V>();
			mMap.put(key, values);
		}
		if (!containsValue(key, value)) {
			values.add(value);
		}
	}

	public void putAll(K key, ArrayList<V> values) {
		List<V> valuesMap = mMap.get(key);
		if (valuesMap == null) {
			valuesMap = new ArrayList<V>();
			mMap.put(key, valuesMap);
		}
		if (values != null) {
			for (V v : values) {
				if (!containsValue(key, v)) {
					valuesMap.add(v);
				}
			}
		}
	}

	public List<V> remove(K key) {
		List<V> values = mMap.get(key);
		if (values != null) {
			values.clear();
		}
		return mMap.remove(key);
	}

	public boolean remove(K key, V value) {
		List<V> values = mMap.get(key);
		if (values != null) {
			return values.remove(value);
		}
		return false;
	}

	public boolean removeValue(V value) {
		boolean isRemove = true;
		for (Entry<K, List<V>> entry : mMap.entrySet()) {
			List<V> values = entry.getValue();
			if (values != null) {
				if (!values.remove(value)) {
					isRemove = false;
				}
			}
		}
		return isRemove;
	}

	public void clear() {
		for (Entry<K, List<V>> entry : mMap.entrySet()) {
			List<V> values = entry.getValue();
			if (values != null) {
				values.clear();
			}
		}
		mMap.clear();
	}

	public boolean containsKey(K key) {
		return mMap.containsKey(key);
	}

	public boolean containsValue(K key, V value) {
		List<V> values = mMap.get(key);
		return values != null && values.contains(value);
	}

	public boolean isEmpty() {
		return mMap.isEmpty();
	}

	public int size() {
		return mMap.size();
	}

	public Set<K> keySet() {
		return mMap.keySet();
	}

	public Set<Entry<K, List<V>>> entrySet() {
		return mMap.entrySet();
	}

	public Collection<List<V>> values() {
		return mMap.values();
	}
}