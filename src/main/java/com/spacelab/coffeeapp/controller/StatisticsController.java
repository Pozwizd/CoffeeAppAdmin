package com.spacelab.coffeeapp.controller;

import com.spacelab.coffeeapp.dto.TopProduct;
import com.spacelab.coffeeapp.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping({"/"})
@AllArgsConstructor
public class StatisticsController {

    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final CityService cityService;
    private final LocationService locationService;
    private final OrderService orderService;
    private final DeliveryService deliveryService;
    private final OrderItemService orderItemService;
    private final AttributeValueService attributeValueService;
    private final AttributeProductService attributeProductService;
    private final OrderItemAttributeService orderItemAttributeService;



    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("pageActive", "statistic");
        model.addAttribute("title", "Статистика");
    }

    @GetMapping({"/", ""})
    public ModelAndView index() {
        return new ModelAndView("statistic");
    }

    @GetMapping("/orders")
    @ResponseBody
    public Integer getAllCountOrders() {
        return orderService.countAllOrders();
    }

    @GetMapping("/orders/today")
    @ResponseBody
    public Integer getTodayCountOrders() {
        return orderService.countTodayOrders();
    }

    @GetMapping("/sales/total")
    @ResponseBody
    public Long getTotalSales() {
        return orderService.calculateTotalSales();
    }

    @GetMapping("/sales/today")
    @ResponseBody
    public Long getTodaySales() {
        return orderService.calculateTodaySales();
    }

    @GetMapping("/customers")
    @ResponseBody
    public Integer getAllCustomers() {
        return customerService.countAllCustomers();
    }

    @GetMapping("/customers/today")
    @ResponseBody
    public Integer getTodayNewCustomers() {
        return customerService.countTodayNewCustomers();
    }

    @GetMapping("/customers/active/last-month")
    @ResponseBody
    public Long getActiveCustomersLastMonth() {
        return customerService.getCountOfCustomersWithOrdersLastWeek();
    }

    @GetMapping("/customers/changes/last-week")
    @ResponseBody
    public Double getChangesLastWeek() {
        return customerService.calculateChangesLastWeek();
    }

    @GetMapping("/sales/chart")
    @ResponseBody
    public Map<String, List<Integer>> getSalesChartData(@RequestParam int quantity, @RequestParam int months) {
        return productService.findTopProductsSalesByMonth(quantity, months);
    }


    @GetMapping("/products/top")
    @ResponseBody
    public List<TopProduct> getTopProducts() {
        return productService.getTop3Products();
    }

}
