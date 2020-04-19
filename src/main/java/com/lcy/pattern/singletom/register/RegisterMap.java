package com.lcy.pattern.singletom.register;

import java.util.HashMap;
import java.util.Map;

public class RegisterMap {

    private RegisterMap(){}

    private static Map<String,Object> register = new HashMap<String,Object>();

    public static RegisterMap getInstance(String name) throws ClassNotFoundException {
        if(name == null) {
            name = RegisterMap.class.getName();
        }
        if(register.get(name) == null) {
            register.put(name,new RegisterMap());
        }
        return (RegisterMap) register.get(name);
    }


}
