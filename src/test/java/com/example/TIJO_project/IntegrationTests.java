package com.example.TIJO_project;

import com.example.TIJO_project.controller.OrderController;
import com.example.TIJO_project.controller.QrCodeController;
import com.example.TIJO_project.dto.OrderDto;
import com.example.TIJO_project.dto.DishDto;
import com.example.TIJO_project.dto.OrderItemDto;
import com.example.TIJO_project.model.Dish;
import com.example.TIJO_project.model.DishType;
import com.example.TIJO_project.model.QrCode;
import com.example.TIJO_project.repository.QrCodeRepository;
import com.example.TIJO_project.service.DishService;
import com.example.TIJO_project.service.OrderService;
import com.example.TIJO_project.repository.DishRepository;
import com.example.TIJO_project.mapper.DishMapper;
import com.example.TIJO_project.service.QrCodeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@AutoConfigureMockMvc
@SpringBootTest
class IntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderService orderService;

	@MockBean
	private DishService dishService;

	@MockBean
	private DishRepository dishRepository;

	@MockBean
	private QrCodeRepository qrCodeRepository;

	@MockBean
	private DishMapper dishMapper;

	@MockBean
	private QrCodeService qrCodeService;

	@Test
	void addToOrder_EmptyOrder_ReturnsOk() throws Exception {
		// Arrange
		String dishId = "validDishId";
		OrderDto orderDto = new OrderDto();
		DishDto dishDto = DishDto.builder().id(dishId).build();
		when(dishRepository.findById(dishId)).thenReturn(Optional.of(new Dish()));
		when(dishMapper.toDto(any())).thenReturn(dishDto);
		doReturn(ResponseEntity.ok(orderDto)).when(orderService).addToOrder(any(), any());

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.post("/order/addToOrder")
						.contentType(MediaType.APPLICATION_JSON)
						.param("dishId", dishId)
						.content("{}"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void addToOrder_SameDishId_ReturnsOk() throws Exception {
		// Arrange
		String dishId = "validDishId";
		OrderDto orderDto = new OrderDto();
		DishDto dishDto = DishDto.builder().id(dishId).build();
		when(dishRepository.findById(dishId)).thenReturn(Optional.of(new Dish()));
		when(dishMapper.toDto(any())).thenReturn(dishDto);

		OrderItemDto existingDishDto = OrderItemDto.builder().dish(dishDto).quantity(1).build();
		orderDto = orderDto.toBuilder().order(List.of(existingDishDto)).build();

		doReturn(ResponseEntity.ok(orderDto)).when(orderService).addToOrder(any(), any());

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.post("/order/addToOrder")
						.contentType(MediaType.APPLICATION_JSON)
						.param("dishId", dishId)
						.content(asJsonString(orderDto)))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void addToOrder_DifferentDishId_ReturnsOk() throws Exception {
		// Arrange
		String initialDishId = "initialDishId";
		OrderDto orderDto = new OrderDto();
		DishDto initialDishDto = DishDto.builder().id(initialDishId).build();
		OrderItemDto initialDishItemDto = OrderItemDto.builder().dish(initialDishDto).quantity(1).build();
		orderDto = orderDto.toBuilder().order(List.of(initialDishItemDto)).build();

		String newDishId = "differentDishId";
		DishDto newDishDto = DishDto.builder().id(newDishId).build();
		when(dishRepository.findById(newDishId)).thenReturn(Optional.of(new Dish()));
		when(dishMapper.toDto(any())).thenReturn(newDishDto);

		OrderItemDto newDishItemDto = OrderItemDto.builder().dish(newDishDto).quantity(1).build();

		doReturn(ResponseEntity.ok(orderDto)).when(orderService).addToOrder(any(), any());

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.post("/order/addToOrder")
						.contentType(MediaType.APPLICATION_JSON)
						.param("dishId", newDishItemDto.getDish().getId())
						.content(asJsonString(orderDto)))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void removeFromOrder_DishNotInOrder_ReturnsBadRequest() throws Exception {
		// Arrange
		String nonExistingDishId = "nonExistingDishId";
		OrderDto orderDto = new OrderDto();

		when(dishRepository.findById(nonExistingDishId)).thenReturn(Optional.of(new Dish()));
		doReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order do not include dish"))
				.when(orderService).removeFromOrder(any(), any());

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.post("/order/removeFromOrder")
						.contentType(MediaType.APPLICATION_JSON)
						.param("dishId", nonExistingDishId)
						.content(asJsonString(orderDto)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	void removeFromOrder_DecreaseQuantity_ReturnsOk() throws Exception {
		// Arrange
		String existingDishId = "existingDishId";
		OrderDto orderDto = new OrderDto();
		DishDto existingDishDto = DishDto.builder().id(existingDishId).build();
		OrderItemDto existingDishItemDto = OrderItemDto.builder().dish(existingDishDto).quantity(2).build();
		orderDto.setOrder(Collections.singletonList(existingDishItemDto));

		when(dishRepository.findById(existingDishId)).thenReturn(Optional.of(new Dish()));
		doReturn(ResponseEntity.ok(orderDto)).when(orderService).removeFromOrder(any(), any());

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.post("/order/removeFromOrder")
						.contentType(MediaType.APPLICATION_JSON)
						.param("dishId", existingDishItemDto.getDish().getId())
						.content(asJsonString(orderDto)))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void removeFromOrder_RemoveDish_ReturnsOk() throws Exception {
		// Arrange
		String existingDishId = "existingDishId";
		OrderDto orderDto = new OrderDto();
		DishDto existingDishDto = DishDto.builder().id(existingDishId).build();
		OrderItemDto existingDishItemDto = OrderItemDto.builder().dish(existingDishDto).quantity(1).build();
		orderDto.setOrder(Collections.singletonList(existingDishItemDto));

		when(dishRepository.findById(existingDishId)).thenReturn(Optional.of(new Dish()));

		doReturn(ResponseEntity.ok(orderDto)).when(orderService).removeFromOrder(any(), any());

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.post("/order/removeFromOrder")
						.contentType(MediaType.APPLICATION_JSON)
						.param("dishId", existingDishItemDto.getDish().getId())
						.content(asJsonString(orderDto)))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void checkCode_ValidCode_ReturnsOk() throws Exception {
		// Arrange
		String qrCodeId = "validQrCodeId";
		QrCode qrCode = new QrCode();

		when(qrCodeRepository.findById(qrCodeId)).thenReturn(Optional.of(new QrCode()));
		when(qrCodeService.checkCode(any())).thenReturn(Optional.of(qrCode));

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/qrCode/getValue/{id}", qrCodeId)).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void checkCode_InvalidCode_ReturnsNotFound() throws Exception {
		// Arrange
		String invalidQrCodeId = "invalidQrCodeId";

		when(qrCodeRepository.findById(invalidQrCodeId)).thenReturn(Optional.empty());
		when(qrCodeService.checkCode(any())).thenReturn(Optional.empty());

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/qrCode/getValue/{id}", invalidQrCodeId))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	void getAllDishes_ReturnsOk() throws Exception {
		// Arrange
		List<Dish> dishes = Arrays.asList(
				new Dish("1", DishType.soup, "Dish1", 10.0, Arrays.asList("Ingredient1", "Ingredient2")),
				new Dish("2", DishType.soup, "Dish2", 15.0, Arrays.asList("Ingredient3", "Ingredient4"))
		);

		when(dishService.getAllDishes()).thenReturn(dishes);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/dishes/getAllDishes"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().json(asJsonString(dishes)));
	}

	@Test
	void getAllDishes_EmptyList_ReturnsOk() throws Exception {
		// Arrange
		List<Dish> emptyDishList = Collections.emptyList();

		when(dishService.getAllDishes()).thenReturn(emptyDishList);

		// Act and Assert
		mockMvc.perform(MockMvcRequestBuilders.get("/getAllDishes"))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
