package jg.actionfigures.server.Controllers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import jg.actionfigures.server.API.FigureRepository;
import jg.actionfigures.server.API.FilterRepository;
import jg.actionfigures.server.API.OrderItemRepository;
import jg.actionfigures.server.API.OrderRepository;
import jg.actionfigures.server.API.UserRepository;
import jg.actionfigures.server.AuthModule.Utils.JwtUtils;
import jg.actionfigures.server.AuthModule.Utils.TokenEnum;
import jg.actionfigures.server.Models.PostgerSql.Figure;
import jg.actionfigures.server.Models.PostgerSql.Filter;
import jg.actionfigures.server.Models.PostgerSql.Order;
import jg.actionfigures.server.Models.PostgerSql.OrderItem;
import jg.actionfigures.server.Models.PostgerSql.User;

import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/test")
public class MainController {

    @Autowired
    FigureRepository figureRepository;

    @Autowired
    FilterRepository filterRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    private JwtUtils jwtUtils = new JwtUtils();


    @GetMapping("/xd")
    public String test() {
        return "тест";
    }

    @GetMapping("/catalog")
    public List<Filter> catalog() {
        List<Filter> figureList = (List<Filter>) filterRepository.findAll();
        return figureList;
    }

    @PostMapping("/getFigure")
    public List<Filter> getFigureById(@RequestBody Integer id) {
        Figure figure = figureRepository.findById(id).get();
        List<Filter> figureList = (List<Filter>) filterRepository.findByFigure(figure);
        return figureList;
    }

    @PostMapping("/createOrder") 
    public void createOrder(@RequestBody String order, HttpServletRequest request1) {

        // строка по типу f{id}xcount

        String authToken = jwtUtils.extractToken(request1);
        String login = jwtUtils.extractUsername(authToken, TokenEnum.ACCESS);

        User user = userRepository.findByLogin(login);

        List<OrderItem> orderList = new ArrayList<OrderItem>();
        Order newOrder = new Order();
        String[] items = order.split(";");
        String id = "", count = "";

        newOrder.setDate(Date.valueOf(LocalDate.now()));
        newOrder.setId((int) orderRepository.count() + 1);
        newOrder.setState("В обработке");
        newOrder.setUser(user);
        orderRepository.save(newOrder);

        order = order.substring(1, order.length() - 1);
        for(int i = 0; i < items.length; i++) {
            System.out.println(items);
            id = items[i].substring(items[i].indexOf("{") + 1, items[i].indexOf("}"));
            count = items[i].substring(items[i].indexOf("x") + 1);
            if(count.charAt(count.length() - 1) == ';' || count.charAt(count.length() - 1) == '"') {
                count = count.substring(0, count.length() - 1);
            }
            Figure figure = figureRepository.findById(Integer.parseInt(id)).get();
            OrderItem orderItem = new OrderItem();
            orderItem.setCount(Integer.parseInt(count));
            orderItem.setFigure(figure);
            orderItem.setId((int) orderItemRepository.count() + 1 + i);
            orderItem.setOrder(newOrder);
            orderList.add(orderItem);
        }

        for(int i = 0; i < orderList.size(); i++) {
            orderItemRepository.save(orderList.get(i));
            System.out.println("Сохранена фигурка: " + orderList.get(i).getFigure().getTitle());
        }

    }

    @PostMapping("/getOrders")
    public List<OrderItem> getOrders(HttpServletRequest request) {
        String authToken = jwtUtils.extractToken(request);
        String login = jwtUtils.extractUsername(authToken, TokenEnum.ACCESS);
        User user = userRepository.findByLogin(login);
        List<Order> myOrders = orderRepository.findByUserId(user.getId());
        List<OrderItem> items = new ArrayList<OrderItem>();
        for(int i = 0; i < myOrders.size(); i++) {
            items.addAll(orderItemRepository.findAllByOrderId(myOrders.get(i).getId()));
        }
        return items;
    }
}
