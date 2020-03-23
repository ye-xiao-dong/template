package com.yd.springbootdemo;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootTest

class ApplicationTests {

	@Test
	void contextLoads() {

	}

    public  static  <V,K> Map<K,V> listToMap(List<V> tList, Function<V,K> function){
        return tList.parallelStream().collect(Collectors.toMap( function, v -> v));
    }

    public static void main (String[] args) {
        List<com.yd.springbootdemo.model.Test> l = new ArrayList<>();
        l.add(new com.yd.springbootdemo.model.Test(){{this.setId(1);this.setName("1");}});
        l.add(new com.yd.springbootdemo.model.Test(){{this.setId(2);this.setName("2");}});
        l.add(new com.yd.springbootdemo.model.Test(){{this.setId(3);this.setName("3");}});
        l.add(new com.yd.springbootdemo.model.Test(){{this.setId(4);this.setName("4");}});
        l.add(new com.yd.springbootdemo.model.Test(){{this.setId(5);this.setName("5");}});
        l.add(new com.yd.springbootdemo.model.Test(){{this.setId(6);this.setName("6");}});
        listToMap(l, com.yd.springbootdemo.model.Test::getId);
    }


}
