package com.example.TIJO_project;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.example.TIJO_project.dto.DishDto;
import com.example.TIJO_project.dto.OrderDto;
import com.example.TIJO_project.dto.OrderItemDto;
import com.example.TIJO_project.model.Dish;
import com.example.TIJO_project.model.DishType;
import com.example.TIJO_project.model.PaymentMethod;
import com.example.TIJO_project.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class UnitTests {
	@InjectMocks
	private OrderService orderService;

	private OrderDto orderDto;
	private DishDto dishDto;

	@BeforeEach
	void setUp() {
		orderDto = OrderDto.builder()
				.tableNoId("1")
				.cost(0.0)
				.order(Collections.emptyList())
				.paymentMethod(PaymentMethod.card)
				.build();

		dishDto = DishDto.builder()
				.id("1")
				.dishType(DishType.mainCourse)
				.name("Example Dish")
				.price(10.0)
				.ingredients(Arrays.asList("Ingredient1", "Ingredient2"))
				.build();
	}

	@Test
	void testIsDishValid_ValidDish() {
		Dish dbDish = Dish.builder()
				.id("1")
				.dishType(DishType.mainCourse)
				.name("Chicken Curry")
				.price(10.0)
				.ingredients(List.of("Chicken", "Curry Sauce"))
				.build();

		OrderItemDto orderItemDto = OrderItemDto.builder()
				.dish(DishDto.builder()
						.id("1")
						.dishType(DishType.mainCourse)
						.name("Chicken Curry")
						.price(10.0)
						.ingredients(List.of("Chicken", "Curry Sauce"))
						.build())
				.quantity(2)
				.cost(20.0)
				.build();

		boolean result = orderService.isDishValid(orderItemDto, dbDish);

		assertTrue(result);
	}

	@Test
	void testIsDishValid_InvalidDish() {
		Dish dbDish = Dish.builder()
				.id("1")
				.dishType(DishType.mainCourse)
				.name("Chicken Curry")
				.price(10.0)
				.ingredients(List.of("Chicken", "Curry Sauce"))
				.build();

		OrderItemDto orderItemDto = OrderItemDto.builder()
				.dish(DishDto.builder()
						.id("1")
						.dishType(DishType.mainCourse)
						.name("Invalid Dish Name")
						.price(10.0)
						.ingredients(List.of("Chicken", "Curry Sauce"))
						.build())
				.quantity(2)
				.cost(20.0)
				.build();

		boolean result = orderService.isDishValid(orderItemDto, dbDish);

		assertFalse(result);
	}

	@Test
	void testIsDishTypeValid_ValidType() {
		boolean result = orderService.isDishTypeValid("MAIN_COURSE", "MAIN_COURSE");
		assertTrue(result);
	}

	@Test
	void testIsDishTypeValid_InvalidType() {
		boolean result = orderService.isDishTypeValid("DESSERT", "MAIN_COURSE");
		assertFalse(result);
	}

	@Test
	void testIsDishNameValid_ValidName() {
		boolean result = orderService.isDishNameValid("Chicken Curry", "Chicken Curry");
		assertTrue(result);
	}

	@Test
	void testIsDishNameValid_InvalidName() {
		boolean result = orderService.isDishNameValid("Invalid Dish Name", "Chicken Curry");
		assertFalse(result);
	}

	@Test
	void testIsDishPriceValid_ValidPrice() {
		boolean result = orderService.isDishPriceValid(10.0, 10.0);
		assertTrue(result);
	}

	@Test
	void testIsDishPriceValid_InvalidPrice() {
		boolean result = orderService.isDishPriceValid(15.0, 10.0);
		assertFalse(result);
	}

	@Test
	void testRecalculateCost() {
		OrderItemDto orderItemDto1 = OrderItemDto.builder()
				.dish(dishDto)
				.quantity(2)
				.cost(20.0)
				.build();

		DishDto secondDishDto = DishDto.builder()
				.id("2")
				.dishType(DishType.mainCourse)
				.name("Second Dish")
				.price(15.0)
				.ingredients(Arrays.asList("Ingredient1", "Ingredient2"))
				.build();

		OrderItemDto orderItemDto2 = OrderItemDto.builder()
				.dish(secondDishDto)
				.quantity(1)
				.cost(15.0)
				.build();

		orderDto.setOrder(Arrays.asList(orderItemDto1, orderItemDto2));

		double result = orderService.recalculateCost(orderDto);

		assertEquals(35.0, result);
	}

	@Test
	void testRoundToTwoDecimalPlaces() {
		double input = 12.3456;
		double expectedOutput = 12.35;

		double result = orderService.roundToTwoDecimalPlaces(input);

		assertEquals(expectedOutput, result);
	}
}
