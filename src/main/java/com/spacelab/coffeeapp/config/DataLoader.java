package com.spacelab.coffeeapp.config;

import com.spacelab.coffeeapp.entity.*;
import com.spacelab.coffeeapp.service.*;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final UserService userService;
//    private final CategoryService categoryService;
//    private final ProductService productService;
//    private final CustomerService customerService;
//    private final CityService cityService;
//    private final LocationService locationService;
//    private final OrderService orderService;
//    private final DeliveryService deliveryService;
//    private final OrderItemService orderItemService;
//    private final AttributeValueService attributeValueService;
//    private final AttributeProductService attributeProductService;
//    private final OrderItemAttributeService orderItemAttributeService;
//
//    private final Faker faker;
//
    @EventListener(ApplicationReadyEvent.class)
    public void loadData() {
        loadUsers();
//        loadCategories();
//        loadProductsAndAttributes();
//        loadCustomers();
//        loadCities();
//        loadLocations();
//        loadOrdersAndOrderItems();
//        loadDeliveries();
    }

    private void loadUsers() {
        User user = new User();
        user.setName("admin");
        user.setEmail("example@gmail.com");
        user.setRole(User.Role.ADMIN);
        user.setPassword("admin");
        userService.saveUser(user);
//        long currentUserCount = userService.countUsers();
//        if (currentUserCount < 10) {
//            long usersToCreate = 25 - currentUserCount;
//            IntStream.range(0, (int) usersToCreate).forEach(i -> {
//                User user = new User();
//                user.setName("admin" + i);
//                user.setEmail(faker.internet().emailAddress());
//                user.setRole(User.Role.ADMIN);
//                user.setPassword(faker.internet().password(8, 16));
//                userService.saveUser(user);
//            });
//        }
    }

//    private void loadCategories() {
//        long currentCategoryCount = categoryService.countCategory();
//        if (currentCategoryCount < 20) {
//            List<String> defaultCategories = Arrays.asList("Кофе", "Чай", "Еда", "Десерт", "Пиво", "Сок", "Сэндвич", "Суп", "Салат", "Паста", "Пицца", "Гамбургер", "Шоколад", "Мороженое", "Кекс", "Печенье", "Торт", "Молочный коктейль", "Энергетик", "Вода");
//            long categoriesToCreate = 20 - currentCategoryCount;
//            IntStream.range(0, (int) categoriesToCreate).forEach(i -> {
//                Category category = new Category();
//                String baseName = defaultCategories.get(i);
//                category.setName(baseName);
//                category.setStatus(Category.Status.ACTIVE);
//                categoryService.saveCategory(category);
//            });
//        }
//    }
//
//    private void loadProductsAndAttributes() {
//        long currentProductCount = productService.countProducts();
//        if (currentProductCount < 25) {
//            long productsToCreate = 25 - currentProductCount;
//            IntStream.range(0, (int) productsToCreate).forEach(i -> {
//                Product product = new Product();
//                product.setName(faker.commerce().productName());
//                product.setStatus(Product.Status.ACTIVE);
//                product.setDescription(faker.lorem().sentence());
//                product.setCategory(categoryService.getCategory((long) faker.number().numberBetween(1, 20)).get());
//                productService.saveProduct(product);
//
//                AttributeProduct sizeAttribute = new AttributeProduct();
//                sizeAttribute.setName("Size");
//                sizeAttribute.setType(AttributeProduct.TypeAttribute.Option);
//                sizeAttribute.setProducts(Collections.singletonList(product));
//                attributeProductService.saveAttributeProduct(sizeAttribute);
//                product.setAttributeProducts(Collections.singletonList(sizeAttribute));
//                productService.saveProduct(product);
//
//                AttributeValue smallSize = new AttributeValue();
//                smallSize.setName("Small");
//                smallSize.setAttributeProduct(sizeAttribute);
//                smallSize.setPrice(29.50);
//                attributeValueService.saveAttributeValue(smallSize);
//
//                AttributeValue mediumSize = new AttributeValue();
//                mediumSize.setName("Medium");
//                mediumSize.setAttributeProduct(sizeAttribute);
//                mediumSize.setPrice(60.00);
//                attributeValueService.saveAttributeValue(mediumSize);
//
//                AttributeValue largeSize = new AttributeValue();
//                largeSize.setName("Large");
//                largeSize.setAttributeProduct(sizeAttribute);
//                largeSize.setPrice(100.00);
//                attributeValueService.saveAttributeValue(largeSize);
//            });
//        }
//    }
//
//    private void loadCustomers() {
//        long currentCustomerCount = customerService.countCustomers();
//        if (currentCustomerCount < 25) {
//            long customersToCreate = 25 - currentCustomerCount;
//            IntStream.range(0, (int) customersToCreate).forEach(i -> {
//                Customer customer = new Customer();
//                customer.setName(faker.name().fullName());
//                customer.setEmail(faker.internet().emailAddress());
//                customer.setAddress(faker.address().fullAddress());
//                customer.setPhoneNumber("+380" + faker.number().numberBetween(100000000, 999999999));
//                customer.setPassword(faker.internet().password(8, 16));
//                customer.setRegistrationDate(LocalDate.now());
//                customer.setDateOfBirth(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//                customer.setLanguage(Language.EN);
//                customer.setStatus(CustomerStatus.ACTIVE);
//                customerService.saveCustomer(customer);
//            });
//        }
//    }
//
//    private void loadCities() {
//        long currentCityCount = cityService.countCities();
//        if (currentCityCount < 25) {
//            long citiesToCreate = 25 - currentCityCount;
//            IntStream.range(0, (int) citiesToCreate).forEach(i -> {
//                City city = new City();
//                city.setName(faker.address().city());
//                city.setRegion(faker.address().state());
//                city.setPostalCode(faker.address().zipCode());
//                cityService.saveCity(city);
//            });
//        }
//    }
//
//    private void loadLocations() {
//        long currentLocationCount = locationService.countLocations();
//        if (currentLocationCount < 25) {
//            Random random = new Random();
//            long locationsToCreate = 25 - currentLocationCount;
//            IntStream.range(0, (int) locationsToCreate).forEach(i -> {
//                Location location = new Location();
//                long[] cityIds = cityService.findAllCities().stream().mapToLong(City::getId).toArray();
//                location.setCity(cityService.getCity(cityIds[random.nextInt(cityIds.length)]));
//                location.setStreet(faker.address().streetName());
//                location.setBuilding(faker.address().buildingNumber());
//                location.setLatitude(faker.address().latitude());
//                location.setLongitude(faker.address().longitude());
//                locationService.saveLocation(location);
//            });
//        }
//    }
//
//    private void loadOrdersAndOrderItems() {
//        long currentOrderCount = orderService.countAllOrders();
//        if (currentOrderCount < 25) {
//            long ordersToCreate = 25 - currentOrderCount;
//            IntStream.range(0, (int) ordersToCreate).forEach(i -> {
//                Order order = new Order();
//                order.setDateTimeOfCreate(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
//                order.setDateTimeOfReady(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
//                order.setPayment(Order.Payment.CASH);
//                order.setStatus(Order.OrderStatus.DONE);
//                if (i % 3 == 0) {
//                    order.setStatus(Order.OrderStatus.CANCELLED);
//                }
//                order.setCustomer(customerService.getCustomer((long) faker.number().numberBetween(1, 25)));
//
//                orderService.saveOrder(order);
//
//                long orderItemCount = orderItemService.countOrderItems();
//                long orderItemsToCreate = 25 - orderItemCount;
//                IntStream.range(0, (int) orderItemsToCreate).forEach(j -> {
//                    OrderItem orderItem = new OrderItem();
//                    orderItem.setQuantity(faker.number().numberBetween(1, 5));
//                    Product product = productService.getProduct((long) faker.number().numberBetween(1, 25)).get();
//                    orderItem.setProduct(product);
//                    orderItem.setOrder(order);
//
//                    orderItemService.saveOrderItem(orderItem);
//
//                    List<AttributeProduct> attributeProducts = attributeProductService.findByProduct(product.getId());
//                    attributeProducts.forEach(attributeProduct -> {
//                        List<AttributeValue> attributeValues = attributeValueService.findByAttributeProduct(attributeProduct.getId());
//                        AttributeValue chosenValue = attributeValues.get(faker.number().numberBetween(0, attributeValues.size()));
//                        OrderItemAttribute orderItemAttribute = new OrderItemAttribute();
//                        orderItemAttribute.setAttributeProduct(attributeProduct);
//                        orderItemAttribute.setOrderItem(orderItem);
//
//                        orderItemAttribute.setAttributeValue(chosenValue);
//
//                        orderItemAttributeService.saveOrderItemAttribute(orderItemAttribute);
//                    });
//                });
//            });
//        }
//    }
//
//    private void loadDeliveries() {
//        long currentDeliveryCount = deliveryService.countDeliveries();
//        if (currentDeliveryCount < 25) {
//            long deliveriesToCreate = 25 - currentDeliveryCount;
//            IntStream.range(0, (int) deliveriesToCreate).forEach(i -> {
//                Delivery delivery = new Delivery();
//                delivery.setName(faker.name().fullName());
//                delivery.setPhoneNumber("+380" + faker.number().numberBetween(100000000, 999999999));
//                delivery.setCity(cityService.getCity((long) faker.number().numberBetween(1, 25)));
//                delivery.setStreet(faker.address().streetName());
//                delivery.setBuilding(faker.address().buildingNumber());
//                delivery.setSubDoor(faker.address().buildingNumber());
//                delivery.setApartment(faker.address().zipCode());
//                delivery.setStatus(Delivery.DeliveryStatus.DELIVERED);
//                delivery.setOrder(orderService.getOrder((long) i + 1));
//                delivery.setDeliveryDate(faker.date().future(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
//                delivery.setDeliveryTime(faker.date().future(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalTime());
//                delivery.setChangeAmount(faker.number().randomDouble(2, 0, 100));
//                deliveryService.saveDelivery(delivery);
//            });
//        }
//    }
}
