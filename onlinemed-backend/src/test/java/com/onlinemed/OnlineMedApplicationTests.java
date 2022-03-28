package com.onlinemed;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.function.Function;

@SpringBootTest
class OnlineMedApplicationTests {
	@Override
	public String toString() {
		return "OnlineMedApplicationTests{}";
	}



	@Test
	void contextLoads() {
		final ArrayList<Integer> ints = new ArrayList<>();
		ints.add(1);

		final ArrayList<Integer> ints2 = new ArrayList<>() {
			@Override
			public boolean add(Integer o) {
				return super.add(o);
			}

			{
				add(1);
			}
		};

		System.out.println(ints);
		System.out.println(ints2);
	}

}
