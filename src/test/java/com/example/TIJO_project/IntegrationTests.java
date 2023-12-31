package com.example.TIJO_project;

import com.example.TIJO_project.config.EnableMongoTestServer;

import com.example.TIJO_project.dto.DishDto;
import com.example.TIJO_project.dto.OrderDto;
import com.example.TIJO_project.dto.OrderItemDto;
import com.example.TIJO_project.mapper.DishMapper;
import com.example.TIJO_project.model.*;
import com.example.TIJO_project.repository.DishRepository;
import com.example.TIJO_project.repository.QrCodeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@EnableMongoTestServer
@EnableMongoRepositories(basePackages = "com.example.TIJO_project.repository")
class IntegrationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private DishRepository dishRepository;

	@Autowired
	private QrCodeRepository qrCodeRepository;

	private static ObjectMapper mapper;

	@Autowired
	private DishMapper dishMapper;

	@BeforeAll
	static void setUp() {
		mapper = new ObjectMapper();
		mapper.findAndRegisterModules();
	}

	@Test
	public void testAddToEmptyOrder() throws Exception {
		// Given
		Dish dish = Dish.builder()
				.id("1")
				.dishType(DishType.mainCourse)
				.name("Pizza")
				.price(10.0)
				.ingredients(Arrays.asList("Cheese", "Tomato"))
				.build();
		dishRepository.save(dish);

		DishDto dishDto = dishMapper.toDto(dish);

		OrderDto orderDto = OrderDto.builder()
				.tableNoId("table123")
				.cost(0.0)
				.order(List.of())
				.paymentMethod(PaymentMethod.cash)
				.build();

		// When
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/order/addToOrder")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(orderDto))
						.param("dishId", dish.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		OrderDto updatedOrderDto = mapper.readValue(content, OrderDto.class);

		// Then
		assertNotNull(updatedOrderDto);
		assertEquals(1, updatedOrderDto.getOrder().size());
		assertEquals(dishDto.getId(), updatedOrderDto.getOrder().get(0).getDish().getId());
		assertEquals(1, updatedOrderDto.getOrder().get(0).getQuantity());
		assertEquals(dishDto.getPrice(), updatedOrderDto.getCost());
	}

	@Test
	public void testAddNonExistingDishToEmptyOrder() throws Exception {
		// Given
		OrderDto orderDto = OrderDto.builder()
				.tableNoId("table123")
				.cost(0.0)
				.order(List.of())
				.paymentMethod(PaymentMethod.cash)
				.build();

		// When
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/order/addToOrder")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(orderDto))
						.param("dishId", "nonExistingDishId"))  // provide a non-existing dish ID here
				.andExpect(MockMvcResultMatchers.status().isBadRequest())  // expect a bad request status
				.andReturn();

		String content = result.getResponse().getContentAsString();

		// Then
		assertEquals("Dish do not exist", content);
	}

	@Test
	public void testAddSameDishToOrder() throws Exception {
		// Given
		Dish dish = Dish.builder()
				.id("1")
				.dishType(DishType.mainCourse)
				.name("Pizza")
				.price(10.0)
				.ingredients(Arrays.asList("Cheese", "Tomato"))
				.build();
		dishRepository.save(dish);

		DishDto dishDto = dishMapper.toDto(dish);

		OrderItemDto orderItemDto = OrderItemDto.builder()
				.dish(dishDto)
				.quantity(1)
				.cost(10.0)
				.build();

		List<OrderItemDto> orderItems = Collections.singletonList(orderItemDto);

		OrderDto orderDto = OrderDto.builder()
				.tableNoId("table123")
				.cost(10.0)
				.order(orderItems)
				.paymentMethod(PaymentMethod.cash)
				.build();

		// When
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/order/addToOrder")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(orderDto))
						.param("dishId", dish.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		OrderDto updatedOrderDto = mapper.readValue(content, OrderDto.class);

		// Then
		assertNotNull(updatedOrderDto);
		assertEquals(1, updatedOrderDto.getOrder().size());
		assertEquals(dishDto.getId(), updatedOrderDto.getOrder().get(0).getDish().getId());
		assertEquals(2, updatedOrderDto.getOrder().get(0).getQuantity());
		assertEquals(dishDto.getPrice() * 2, updatedOrderDto.getCost());
	}

	@Test
	public void testAddDifferentDishesToOrder() throws Exception {
		// Given
		Dish pizza = Dish.builder()
				.id("1")
				.dishType(DishType.mainCourse)
				.name("Pizza")
				.price(10.0)
				.ingredients(Arrays.asList("Cheese", "Tomato"))
				.build();
		dishRepository.save(pizza);

		Dish pasta = Dish.builder()
				.id("2")
				.dishType(DishType.mainCourse)
				.name("Pasta")
				.price(8.0)
				.ingredients(Arrays.asList("Tomato Sauce", "Basil"))
				.build();
		dishRepository.save(pasta);

		DishDto pizzaDto = dishMapper.toDto(pizza);

		DishDto pastaDto = dishMapper.toDto(pasta);

		OrderDto orderDto = OrderDto.builder()
				.tableNoId("table123")
				.cost(0.0)
				.order(Collections.singletonList(
						OrderItemDto.builder()
								.dish(pizzaDto)
								.quantity(1)
								.cost(10.0)
								.build()
				))
				.paymentMethod(PaymentMethod.cash)
				.build();

		// When
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/order/addToOrder")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(orderDto))
						.param("dishId", pasta.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		OrderDto updatedOrderDto = mapper.readValue(content, OrderDto.class);


		OrderItemDto pizzaOrderItem = updatedOrderDto.getOrder().stream()
				.filter(orderItem -> orderItem.getDish().getId().equals(pizzaDto.getId()))
				.findFirst()
				.orElse(null);

		OrderItemDto pastaOrderItem = updatedOrderDto.getOrder().stream()
				.filter(orderItem -> orderItem.getDish().getId().equals(pastaDto.getId()))
				.findFirst()
				.orElse(null);

		// Then
		assertNotNull(updatedOrderDto);
		assertEquals(2, updatedOrderDto.getOrder().size());
		assertNotNull(pizzaOrderItem);
		assertEquals(1, pizzaOrderItem.getQuantity());
		assertEquals(pizzaDto.getPrice(), pizzaOrderItem.getCost());
		assertNotNull(pastaOrderItem);
		assertEquals(1, pastaOrderItem.getQuantity());
		assertEquals(pastaDto.getPrice(), pastaOrderItem.getCost());
	}

	@Test
	public void testDecreaseDishQuantityInOrder() throws Exception {
		// Given
		Dish pizza = Dish.builder()
				.id("1")
				.dishType(DishType.mainCourse)
				.name("Pizza")
				.price(10.0)
				.ingredients(Arrays.asList("Cheese", "Tomato"))
				.build();
		dishRepository.save(pizza);

		DishDto pizzaDto = dishMapper.toDto(pizza);

		OrderDto orderDto = OrderDto.builder()
				.tableNoId("table123")
				.cost(20.0)
				.order(Collections.singletonList(
						OrderItemDto.builder()
								.dish(pizzaDto)
								.quantity(2)
								.cost(20.0)
								.build()
				))
				.paymentMethod(PaymentMethod.cash)
				.build();

		// When
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/order/removeFromOrder")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(orderDto))
						.param("dishId", pizza.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		OrderDto updatedOrderDto = mapper.readValue(content, OrderDto.class);

		OrderItemDto pizzaOrderItem = updatedOrderDto.getOrder().stream()
				.filter(orderItem -> orderItem.getDish().getId().equals(pizzaDto.getId()))
				.findFirst()
				.orElse(null);

		// Then
		assertNotNull(updatedOrderDto);
		assertEquals(1, updatedOrderDto.getOrder().size());
		assertNotNull(pizzaOrderItem);
		assertEquals(1, pizzaOrderItem.getQuantity());
		assertEquals(pizzaDto.getPrice(), pizzaOrderItem.getCost());
		assertEquals(10.0, updatedOrderDto.getCost());
	}

	@Test
	public void testDecreaseNonExistingDishQuantityInOrder() throws Exception {
		// Given
		Dish pizza = Dish.builder()
				.id("1")
				.dishType(DishType.mainCourse)
				.name("Pizza")
				.price(10.0)
				.ingredients(Arrays.asList("Cheese", "Tomato"))
				.build();
		dishRepository.save(pizza);

		Dish pasta = Dish.builder()
				.id("2")
				.dishType(DishType.mainCourse)
				.name("Pasta")
				.price(8.0)
				.ingredients(Arrays.asList("Tomato Sauce", "Basil"))
				.build();
		dishRepository.save(pasta);

		DishDto pizzaDto = dishMapper.toDto(pizza);

		OrderDto orderDto = OrderDto.builder()
				.tableNoId("table123")
				.cost(20.0)
				.order(Collections.singletonList(
						OrderItemDto.builder()
								.dish(pizzaDto)
								.quantity(2)
								.cost(20.0)
								.build()
				))
				.paymentMethod(PaymentMethod.cash)
				.build();

		// When
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/order/removeFromOrder")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(orderDto))
						.param("dishId", pasta.getId()))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andReturn();

		String content = result.getResponse().getContentAsString();

		// Then
		assertEquals("Order do not include dish", content);
	}

	@Test
	public void testRemoveDishFromOrderQuantityOne() throws Exception {
		// Given
		Dish pizza = Dish.builder()
				.id("1")
				.dishType(DishType.mainCourse)
				.name("Pizza")
				.price(10.0)
				.ingredients(Arrays.asList("Cheese", "Tomato"))
				.build();
		dishRepository.save(pizza);

		DishDto pizzaDto = DishDto.builder()
				.id("1")
				.dishType(DishType.mainCourse)
				.name("Pizza")
				.price(10.0)
				.ingredients(Arrays.asList("Cheese", "Tomato"))
				.build();

		OrderDto orderDto = OrderDto.builder()
				.tableNoId("table123")
				.cost(10.0)
				.order(Collections.singletonList(
						OrderItemDto.builder()
								.dish(pizzaDto)
								.quantity(1)
								.cost(10.0)
								.build()
				))
				.paymentMethod(PaymentMethod.cash)
				.build();

		// When
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/order/removeFromOrder")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(orderDto))
						.param("dishId", pizza.getId()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		OrderDto updatedOrderDto = mapper.readValue(content, OrderDto.class);

		// Then
		assertNotNull(updatedOrderDto);
		assertTrue(updatedOrderDto.getOrder().isEmpty());
		assertEquals(0.0, updatedOrderDto.getCost(), 0.01);
	}

	@Test
	public void testAcceptOrder() throws Exception {
		// Given
		Dish pizza = Dish.builder()
				.id("1")
				.dishType(DishType.mainCourse)
				.name("Pizza")
				.price(10.0)
				.ingredients(Arrays.asList("Cheese", "Tomato"))
				.build();
		dishRepository.save(pizza);

		String qrCode = "table123";

		DishDto pizzaDto = dishMapper.toDto(pizza);

		OrderDto orderDto = OrderDto.builder()
				.tableNoId(qrCode)
				.cost(10.0)
				.order(Collections.singletonList(
						OrderItemDto.builder()
								.dish(pizzaDto)
								.quantity(1)
								.cost(10.0)
								.build()
				))
				.paymentMethod(PaymentMethod.cash)
				.build();

		qrCodeRepository.save(QrCode.builder().id(qrCode).qrCode(qrCode).build());

		// When
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/order/acceptOrder")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(orderDto)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();

		Order acceptedOrder = mapper.readValue(content, Order.class);

		// Then
		assertNotNull(acceptedOrder);
		assertNotNull(acceptedOrder.getId());
		assertEquals("table123", acceptedOrder.getTableNoId());
		assertEquals(10.0, acceptedOrder.getCost());
		assertNotNull(acceptedOrder.getOrder());
		assertEquals(1, acceptedOrder.getOrder().size());
		assertEquals("1", acceptedOrder.getOrder().get(0).getDish().getId());
		assertEquals(1, acceptedOrder.getOrder().get(0).getQuantity());
		assertEquals(10.0, acceptedOrder.getOrder().get(0).getCost());
		assertEquals(PaymentMethod.cash, acceptedOrder.getPaymentMethod());
	}

	@Test
	public void testAcceptOrderWithInvalidDish() throws Exception {
		// Given
		DishDto invalidDishDto = DishDto.builder()
				.id("invalidDishId")
				.dishType(DishType.mainCourse)
				.name("Invalid name")
				.price(20.0)
				.ingredients(List.of("Ingredient"))
				.build();

		Dish invalidDishInDatabase = Dish.builder()
				.id("invalidDishId")
				.dishType(DishType.mainCourse)
				.name("Valid name")
				.price(20.0)
				.ingredients(List.of("Ingredient"))
				.build();
		dishRepository.save(invalidDishInDatabase);

		OrderDto orderDto = OrderDto.builder()
				.tableNoId("table123")
				.cost(20.0)
				.order(Collections.singletonList(
						OrderItemDto.builder()
								.dish(invalidDishDto)
								.quantity(1)
								.cost(20.0)
								.build()
				))
				.paymentMethod(PaymentMethod.cash)
				.build();


		// When
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/order/acceptOrder")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(orderDto)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andReturn();

		String content = result.getResponse().getContentAsString();

		// Then
		assertEquals("One of the dishes is not valid", content);
	}

	@Test
	public void testAcceptOrderWithNoDishes() throws Exception {
		// Given
		OrderDto orderDto = OrderDto.builder()
				.tableNoId("table123")
				.cost(20.0)
				.order(List.of())
				.paymentMethod(PaymentMethod.cash)
				.build();

		// When
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/order/acceptOrder")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(orderDto)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andReturn();

		String content = result.getResponse().getContentAsString();

		// Then
		assertEquals("Order is empty", content);
	}


	private static String asJsonString(final Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
