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
		// Given
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

		// When
		boolean result = orderService.isDishValid(orderItemDto, dbDish);

		// Then
		assertTrue(result);
	}

	@Test
	void testIsDishValid_InvalidDish() {
		// Given
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

		// When
		boolean result = orderService.isDishValid(orderItemDto, dbDish);

		// Then
		assertFalse(result);
	}

	@Test
	void testIsDishTypeValid_ValidType() {
		// Given
		String orderItemType = DishType.mainCourse.name();
		String dbDishType = DishType.mainCourse.name();

		// When
		boolean result = orderService.isDishTypeValid(orderItemType, dbDishType);

		// Then
		assertTrue(result);
	}

	@Test
	void testIsDishTypeValid_InvalidType() {
		// Given
		String orderItemType = DishType.mainCourse.name();
		String dbDishType = DishType.soup.name();

		// When
		boolean result = orderService.isDishTypeValid(orderItemType, dbDishType);

		// Then
		assertFalse(result);
	}

	@Test
	void testIsDishNameValid_ValidName() {
		// Given
		String orderItemName = "Chicken Curry";
		String dbDishName = "Chicken Curry";

		// When
		boolean result = orderService.isDishNameValid(orderItemName, dbDishName);

		// Then
		assertTrue(result);
	}

	@Test
	void testIsDishNameValid_InvalidName() {
		// Given
		String orderItemName = "Chicken Curry";
		String dbDishName = "Chicken";

		// When
		boolean result = orderService.isDishNameValid(orderItemName, dbDishName);

		// Then
		assertFalse(result);
	}

	@Test
	void testIsDishPriceValid_ValidPrice() {
		// Given
		Double orderItemPrice = 10.0;
		Double dbDishPrice = 10.0;

		// When
		boolean result = orderService.isDishPriceValid(orderItemPrice, dbDishPrice);

		// Then
		assertTrue(result);
	}

	@Test
	void testIsDishPriceValid_InvalidPrice() {
		// Given
		Double orderItemPrice = 15.0;
		Double dbDishPrice = 10.0;

		// When
		boolean result = orderService.isDishPriceValid(orderItemPrice, dbDishPrice);

		// Then
		assertFalse(result);
	}

	@Test
	void testRecalculateCost() {
		// Given
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

		// When
		double result = orderService.recalculateCost(orderDto);

		// Then
		assertEquals(35.0, result);
	}

	@Test
	void testRoundToTwoDecimalPlaces() {
		// Given
		double input = 12.3456;
		double expectedOutput = 12.35;

		// When
		double result = orderService.roundToTwoDecimalPlaces(input);

		// Then
		assertEquals(expectedOutput, result);
	}
}
