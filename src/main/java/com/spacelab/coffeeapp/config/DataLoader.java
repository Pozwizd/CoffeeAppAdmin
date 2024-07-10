package com.spacelab.coffeeapp.config;

import com.spacelab.coffeeapp.entity.*;
import com.spacelab.coffeeapp.service.*;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final CityService cityService;
    private final LocationService locationService;
    private final OrderService orderService;
    private final DeliveryService deliveryService;
    private final OrderItemService orderItemService;

    private final Faker faker;

    @EventListener(ApplicationReadyEvent.class)
    public void loadData() {

        long currentUserCount = userService.countUsers();
        if (currentUserCount < 10) {
            long usersToCreate = 10 - currentUserCount;
            IntStream.range(0, (int) usersToCreate).forEach(i -> {
                User user = new User();
                user.setName(faker.name().fullName());
                user.setEmail(faker.internet().emailAddress());
                user.setRole(Role.ADMIN);
                user.setPassword(faker.internet().password(8, 16));
                userService.saveUser(user);
            });
        }

        long currentCategoryCount = categoryService.countCategory();
        if (currentCategoryCount < 5) {
            List<String> defaultCategories = Arrays.asList("Кофе", "Чай", "Еда", "Десерт", "Пиво");
            long categoriesToCreate = 5 - currentCategoryCount;
            IntStream.range(0, (int) categoriesToCreate).forEach(i -> {
                Category category = new Category();
                String baseName = defaultCategories.get(i);
                category.setName(baseName);
                category.setStatus(Category.Status.ACTIVE);
                categoryService.saveCategory(category);
            });
        }

        long currentProductCount = productService.countProducts();
        if (currentProductCount < 10) {
            long productsToCreate = 10 - currentProductCount;
            IntStream.range(0, (int) productsToCreate).forEach(i -> {
                Product product = new Product();
                product.setName(faker.coffee().name1());
                product.setStatus(Product.Status.ACTIVE);
                product.setDescription(faker.coffee().name2());
                product.setCategory(categoryService.getCategory(1L));
                productService.saveProduct(product);
            });
        }

        long currentCustomerCount = customerService.countCustomers();
        if (currentCustomerCount < 10) {
            long customersToCreate = 10 - currentCustomerCount;
            IntStream.range(0, (int) customersToCreate).forEach(i -> {
                Customer customer = new Customer();
                customer.setName(faker.name().fullName());
                customer.setEmail(faker.internet().emailAddress());
                customer.setAddress(faker.address().fullAddress());
                customer.setPhoneNumber(faker.phoneNumber().phoneNumber());
                customer.setDateOfBirth(Date.valueOf(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
                customer.setLanguage(Language.EN);
                customer.setStatus(CustomerStatus.ACTIVE);
                customerService.saveCustomer(customer);
            });
        }

        long currentCityCount = cityService.countCities();
        if (currentCityCount < 10) {
            long citiesToCreate = 10 - currentCityCount;
            IntStream.range(0, (int) citiesToCreate).forEach(i -> {
                City city = new City();
                city.setName(faker.address().city());
                city.setRegion(faker.address().state());
                city.setPostalCode(faker.address().zipCode());
                cityService.saveCity(city);
            });
        }

        long currentLocationCount = locationService.countLocations();
        if (currentLocationCount < 10) {
            Random random = new Random();
            long locationsToCreate = 10 - currentLocationCount;
            IntStream.range(0, (int) locationsToCreate).forEach(i -> {
                Location location = new Location();
                long[] cityIds = cityService.findAllCities().stream().mapToLong(City::getId).toArray();
                location.setCity(cityService.getCity(cityIds[random.nextInt(cityIds.length)]));
                location.setStreet(faker.address().streetName());
                location.setBuilding(faker.address().buildingNumber());
                location.setLatitude(faker.address().latitude());
                location.setLongitude(faker.address().longitude());
                locationService.saveLocation(location);
            });
        }

        long currentDeliveryCount = deliveryService.countDeliveries();
        if (currentDeliveryCount < 10) {
            long deliveriesToCreate = 10 - currentDeliveryCount;
            IntStream.range(0, (int) deliveriesToCreate).forEach(i -> {
                Delivery delivery = new Delivery();
                delivery.setName(faker.name().fullName());
                delivery.setPhoneNumber(faker.phoneNumber().phoneNumber());
                delivery.setCity(cityService.getCity(1L));
                delivery.setStreet(faker.address().streetName());
                delivery.setBuilding(faker.address().buildingNumber());
                delivery.setApartment(faker.address().zipCode());
                delivery.setDeliveryTime(faker.date().future(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                delivery.setActualDeliveryTime(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                delivery.setChangeAmount(faker.number().randomDouble(2, 0, 100));
                deliveryService.saveDelivery(delivery);
            });
        }

        long currentOrderCount = orderService.countOrders();
        if (currentOrderCount < 10) {
            long ordersToCreate = 10 - currentOrderCount;
            IntStream.range(0, (int) ordersToCreate).forEach(i -> {
                Orders order = new Orders();
                order.setDateTimeOfCreate(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                order.setDateTimeOfUpdate(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                order.setDateTimeOfReady(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                order.setPayment(Orders.Payment.CASH);
                order.setStatus(Orders.OrderStatus.NEW);
                order.setCustomer(customerService.getCustomer((long) faker.number().numberBetween(1, 10)));

                orderService.saveOrder(order);
            });
        }

        long currentOrderItem = orderItemService.countOrderItems();
        if (currentOrderItem < 10) {
            long orderItemsToCreate = 10 - currentOrderItem;
            IntStream.range(0, (int) orderItemsToCreate).forEach(i -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setQuantity(faker.number().numberBetween(1, 5));
                orderItem.setProduct(productService.getProduct((long) faker.number().numberBetween(1, 10)));
                orderItem.setOrders(orderService.getOrder((long) faker.number().numberBetween(1, 10)));
                orderItemService.saveOrderItem(orderItem);
            });
        }


    }


}
