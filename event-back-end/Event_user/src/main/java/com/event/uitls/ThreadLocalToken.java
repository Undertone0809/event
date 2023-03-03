package com.event.uitls;

import org.springframework.stereotype.Component;

@Component
public class ThreadLocalToken {
    private static final ThreadLocal<String> local = new ThreadLocal<>();

    public void setToken(String token){
        local.set(token);
    }
    public String getToken(){
        return local.get();
    }
    public void clear(){
        local.remove();
    }
}

