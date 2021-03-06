package com.huntto.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ListUtils {
	private static final Integer INTEGER_ONE = 1;

	public static boolean isEqualCollection(Collection a, Collection b) {
		if (a.size() != b.size()) { // size是最简单的相等条件
			return false;
		}
		Map mapa = getCardinalityMap(a);
		Map mapb = getCardinalityMap(b);

		// 转换map后，能去掉重复的，这时候size就是非重复项，也是先决条件
		if (mapa.size() != mapb.size()) {
			return false;
		}
        for (Object obj : mapa.keySet()) {
            // 查询同一个obj，首先两边都要有，而且还要校验重复个数，就是map.value
			if (getFreq(obj, mapa) != getFreq(obj, mapb)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 以obj为key，可以防止重复，如果重复就value++ 这样实际上记录了元素以及出现的次数
	 */
    private static Map getCardinalityMap(Collection coll) {
        Map count = new HashMap();
        for (Object obj : coll) {
            Integer c = (Integer) count.get(obj);
			if (c == null)
				count.put(obj, INTEGER_ONE);
			else {
				count.put(obj, new Integer(c.intValue() + 1));
			}
		}
		return count;
	}

    private static int getFreq(Object obj, Map freqMap) {
        Integer count = (Integer) freqMap.get(obj);
		if (count != null) {
			return count.intValue();
		}
		return 0;
	}
}
