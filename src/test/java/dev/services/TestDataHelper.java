package dev.services;

import dev.account.user.*;
import dev.services.comment.Comment;
import dev.services.comment.CommentRepository;
import dev.services.food.Food;
import dev.services.food.FoodRepository;
import dev.services.order.OrderRepository;
import dev.services.rating.Rating;
import dev.services.rating.RatingRepository;
import dev.services.restaurant.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Nelson Tanko
 */
@Component
public class TestDataHelper {

    @Autowired FoodRepository foodRepository;
    @Autowired UserAccountRepository userAccountRepository;
    @Autowired CommentRepository commentRepository;
    @Autowired RatingRepository ratingRepository;
    @Autowired RestaurantRepository restaurantRepository;
    @Autowired CourierRepository courierRepository;
    @Autowired OrderRepository orderRepository;
    @Autowired private AddressRepository addressRepository;

    public void clearData(){
        commentRepository.deleteAll();
        ratingRepository.deleteAll();
        orderRepository.deleteAll();
        restaurantRepository.deleteAll();
        foodRepository.deleteAll();
        userAccountRepository.deleteAll();
    }

    public User createUser(String email, Set<String> roles) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(RandomStringUtils.randomAlphanumeric(60));

        Set<Authority> authorities = roles.stream()
                .map(role -> {
                    Authority authority = new Authority();
                    authority.setName(role);
                    return authority;
                })
                .collect(Collectors.toSet());
        user.setAuthorities(authorities);

        return userAccountRepository.saveAndFlush(user);
    }

    public Restaurant createRestaurant(String name, boolean active, boolean available, Double latitude, Double longitude) {
        Restaurant restaurant = Restaurant.builder()
                .name(name)
                .email("rest@abuja.ng")
                .phoneNumber("07080899911")
                .active(active)
                .available(available)
                .build();

        Address address = Address.builder().city("Abuja")
                .country("Nigeria")
                .street("123 Main St")
                .latitude(latitude)
                .longitude(longitude)
                .restaurant(restaurant)
                .build();
        restaurant.setAddress(address);

        Courier courier = Courier.builder()
                .name("Tasty Bites courier")
                .restaurant(restaurant)
                .build();
        restaurant.setCourier(courier);

        courierRepository.save(courier);
        return restaurantRepository.saveAndFlush(restaurant);
    }

    public RestaurantDTO.Request createRestaurant(){
        return RestaurantDTO.Request.builder()
                .name("Tasty Bites")
                .email("tasty@abuja.ng")
                .phoneNumber("07070888905")
                .address(createAddressRequest())
                .courier(createCourierRequest())
                .build();
   }

    public CourierDTO.Request createCourierRequest(){
        return new CourierDTO.Request("Tasty Bites Courier");
    }

    public AddressDTO.Request createAddressRequest(){
        return new AddressDTO.Request("123 Main St", "Abuja", "Nigeria", 40.7128, -74.0060);
    }

    public Food createFood(){
        Food food = Food.builder()
                .name("Pizza Margherita")
                .description("Classic Italian pizza")
                .price(new BigDecimal("12.99"))
                .available(true)
                .build();
        return foodRepository.saveAndFlush(food);
    }

    public void createFoodsWithComments(Food food, User user){
        createFoodComment("Very nice food", food, user);
        createFoodComment("Delicious", food, user);
    }

    public void createMultipleFoods(){
        createFood("Pizza Margherita", "Classic Italian pizza", new BigDecimal("12.99"), true);
        createFood("Olive Orange", "Niger Cuisine", new BigDecimal("222.99"), true);
        createFood("Banana Flush", "Benue Delight", new BigDecimal("50.55"), false);
    }

    public void createFood(String name, String description, BigDecimal price, boolean available) {
        Food food = Food.builder()
                .name(name)
                .description(description)
                .price(price)
                .available(available)
                .build();
        foodRepository.saveAndFlush(food);
    }

    public void createFoodComment(String content, Food food, User user){
        Comment comment = Comment.builder()
                .content(content)
                .food(food)
                .user(user)
                .build();
        commentRepository.saveAndFlush(comment);
    }

    public void createRating(int rating, Food food, User user){
        Rating newRating = Rating.builder()
                .rating(rating)
                .user(user)
                .food(food)
                .build();
        ratingRepository.save(newRating);
    }
}
