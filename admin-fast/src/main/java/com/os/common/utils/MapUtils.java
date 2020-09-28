

package com.os.common.utils;

import java.util.HashMap;
import java.util.Map;


/**
 * Map工具类
 *
 * @author Mark sunlightcs@gmail.com
 */
public class MapUtils extends HashMap<String, Object> {

    @Override
    public MapUtils put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public static boolean isok(R r){
        String code = mstr(r,"code");
        return code.equals("0");
    }
    public static String mstr(Map<String,Object> param, String key){
        if(!param.containsKey(key))
            return "";
        Object obj = param.get(key);
        if(obj == null)
            return "";
        return obj.toString();
    }
    public static Integer mint(Map<String,Object> param, String key){
        String val = mstr(param, key);
        return val.equals("") ? null : Integer.valueOf(val);
    }
    public static Integer mint(Map<String,Object> param, String key, Integer dfv){
        String val = mstr(param, key);
        return val.equals("") ? dfv : Integer.valueOf(val);
    }

    public static Integer oint(Integer value){
        if (value == null) {
            return 0;
        }
        return value;
    }
}
