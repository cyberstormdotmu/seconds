package com.ishoal.core.domain;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ishoal.core.domain.Order.anOrder;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.joda.time.DateTime.now;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class OrdersTest {

    @Test
    public void shouldBeAbleToAddOrder() {
        Order order = anOrder().build();
        Orders orders = Orders.over(new ArrayList<>()).add(order);
        assertThat(orders.iterator().next(), is(order));
    }


    @Test
    public void shouldSortByDateOrderDescending() {
        Order earliest = anOrder().id(OrderId.from(1L)).created(now().minusDays(2)).build();
        Order middle = anOrder().id(OrderId.from(2L)).created(now().minusDays(1)).build();
        Order latest = anOrder().id(OrderId.from(3L)).created(now()).build();

        Orders unsorted = Orders.over(middle, earliest, latest);
        Orders sorted = unsorted.orderedByDateDescending();

        List<Order> orderList = sorted.stream().collect(Collectors.toList());
        assertThat(orderList, contains(latest, middle, earliest));
    }

    @Test
    public void shouldBeAbleToCombineOrders() {
        Order order1 = anOrder().id(OrderId.from(1L)).build();
        Order order2 = anOrder().id(OrderId.from(2L)).build();
        Order order3 = anOrder().id(OrderId.from(3L)).build();
        Order order4 = anOrder().id(OrderId.from(4L)).build();

        Orders oneAndTwo = Orders.over(order1, order2);
        Orders threeAndFour = Orders.over(order3, order4);

        Orders all = oneAndTwo.combineWith(threeAndFour);

        assertOrders(all, order1, order2, order3, order4);
    }

    @Test
    public void shouldNotGetDuplicatesWhenCombiningOrders() {
        Order order1 = anOrder().id(OrderId.from(1L)).build();
        Order order2 = anOrder().id(OrderId.from(2L)).build();
        Order order3 = anOrder().id(OrderId.from(3L)).build();
        Order order4 = anOrder().id(OrderId.from(4L)).build();

        Orders oneTwoAndThree = Orders.over(order1, order2, order3);
        Orders threeAndFour = Orders.over(order3, order4);

        Orders all = oneTwoAndThree.combineWith(threeAndFour);

        assertOrders(all, order1, order2, order3, order4);
    }

    private void assertOrders(Orders orders, Order... expectedOrders) {
        assertThat(orders.size(), is(expectedOrders.length));

        List<Order> orderList = orders.stream().collect(Collectors.toList());
        for(Order order : expectedOrders) {
            assertTrue(orderList.contains(order));
        }
    }
}