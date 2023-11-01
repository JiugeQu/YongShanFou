package com.mizore.mob;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.mizore.mob.util.JWT;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//@SpringBootTest
class MizoreOrderBackendApplicationTests {

	public static void main(String[] args) {

		FastAutoGenerator.create("jdbc:mysql://localhost:3306/mizore_order?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", "mizore", "mizore")
				.globalConfig(builder -> {
					builder.author("mizore") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
							.outputDir("/home/mizore/IdeaProjects/mizore-order/mizore-order-backend/src/main/java/"); // 指定输出目录
				})
				.packageConfig(builder -> {
					builder.parent("com.mizore.mob") // 设置父包名
//                            .moduleName("model") // 设置父包模块名
							.pathInfo(Collections.singletonMap(OutputFile.xml, "/home/mizore/IdeaProjects/mizore-order/mizore-order-backend/src/main/resources/mapper")); // 设置mapperXml生成路径
				})
				.strategyConfig(builder -> {
					builder.addInclude(
//							"comment," +
//									"dish," +
									"order"
//									"order_dish," +
//									"user"
					) // 设置需要生成的表名
//							.addTablePrefix("foo_"); // 设置过滤表前缀
							.controllerBuilder().enableFileOverride()
							.mapperBuilder().enableFileOverride()
							.serviceBuilder().enableFileOverride()
							.entityBuilder().enableFileOverride();

				})
				.templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
				.execute();
	}
	@Test
	void contextLoads() {
	}

	@Test
	void generateJwt() {
		Hashtable<String, Object> map = new Hashtable<>();
		map.put("name", "mizore");
		String jwt = JWT.generateJWT(map);
		System.out.println(jwt);
		Claims claims = JWT.parseJWT(jwt);
		System.out.println(claims);
	}

	@Test
	void printTime() {
		System.out.println(new Date(1698112122000L));
	}

	@Test
	void testMap() {
		Map<Integer, String> map = new ConcurrentHashMap<>();
		Integer k0 = 22905518;
		map.put(k0, "22905518");
		System.out.println(map);
		Integer k = 22905518;
		k0 = 22905519;
		System.out.println(map.get(k0));
		Map<Student, String> map2 = new ConcurrentHashMap<>();
		Student s1 = new Student();
		s1.age = 10;
		map2.put(s1,"s1");
		s1.age = 20;
		System.out.println(map2.get(s1));
	}


}

class Student{
	int age;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Student student = (Student) o;
		return age == student.age;
	}

	@Override
	public int hashCode() {
		return Objects.hash(age);
	}
}