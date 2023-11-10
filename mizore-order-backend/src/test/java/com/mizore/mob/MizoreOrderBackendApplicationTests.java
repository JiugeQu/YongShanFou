package com.mizore.mob;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.mizore.mob.dto.OrderDTO;
import com.mizore.mob.dto.OrderDishDTO;
import com.mizore.mob.entity.Dish;
import com.mizore.mob.entity.OrderDish;
import com.mizore.mob.entity.User;
import com.mizore.mob.mapper.OrderMapper;
import com.mizore.mob.service.impl.DishServiceImpl;
import com.mizore.mob.util.BeanUtil;
import com.mizore.mob.util.JWT;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootTest
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
		s1.setAge(10);
		map2.put(s1,"s1");
		s1.setAge(20);
		System.out.println(map2.get(s1));
	}

	@Test
	void testInt() {
		Integer integer = null;
		int n = integer;
		System.out.println(n);
	}

	@Resource
	DishServiceImpl dishService;
	@Test
	void testGetMaxPriority() {
		System.out.println(dishService.getMaxPriority());
	}

	@Test
	void testBeanUtilCopy() {
		Dish d1 = new Dish();
		d1.setPriority(1);
		d1.setPrice(new BigDecimal(14));
		Dish d2 = new Dish();
		d2.setName("gbjd");
		d2.setPrice(new BigDecimal(100));
		BeanUtil.copyNotNullProperties(d1, d2);
		System.out.println(d2);
	}
	@Test
	void testBeanUtilCopyExtend() {
		System.out.println(Arrays.toString(OrderDTO.class.getDeclaredFields()));

		OrderDish od1 = new OrderDish();
		od1.setOrderCode(3L);
		OrderDishDTO od2 = new OrderDishDTO();
		od2.setDishImg("img");
//		od2.setOrderCode(2L);
		BeanUtil.copyNotNullProperties(od1, od2);
		System.out.println(od2);
		System.out.println(od2.getOrderCode());
	}

	@Test
	public void testBeanUtilCopyExtend2() {
		Student s = new Student();
		s.setAge(10);
		User user = new User();
		user.setName("mizore");
		user.setId(11);
		BeanUtil.copyNotNullProperties(user, s);
		System.out.println(s.getName());
		System.out.println(s.getId());
	}
	@Resource
	private OrderMapper orderMapper;
	@Test
	public void testIn() {
		List<Byte> states = new ArrayList<>();
		states.add((byte) 1);
		states.add((byte) 2);
		System.out.println(orderMapper.testIn(new ArrayList<>(states)));
	}
}


class Student{
	private int age;
	private String namee;

	private int id;

	public String getName() {
		return namee;
	}

	public void setName(String name) {
		this.namee = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

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