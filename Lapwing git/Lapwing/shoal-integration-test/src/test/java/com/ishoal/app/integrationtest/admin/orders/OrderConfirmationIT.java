package com.ishoal.app.integrationtest.admin.orders;

import com.ishoal.app.integrationtest.AbstractIntegrationTest;
import com.ishoal.app.integrationtest.DirtiesDb;
import com.ishoal.app.integrationtest.interactions.admin.AdminFetchOrder;
import com.ishoal.app.integrationtest.interactions.buyer.FetchProduct;
import com.ishoal.core.domain.OrderStatus;
import com.ishoal.ws.admin.dto.AdminOrderDto;
import com.ishoal.ws.buyer.dto.ProductDto;
import com.ishoal.ws.buyer.dto.ShoppingBasketItemDto;
import com.ishoal.ws.common.dto.OrderLineDto;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidAdminAuthentication;
import static com.ishoal.app.integrationtest.AuthenticationHelper.usingValidBuyerAuthentication;
import static com.ishoal.app.integrationtest.IntegrationTestConstants.ORDER_REFERENCE_UNCONFIRMED;
import static com.ishoal.app.integrationtest.IntegrationTestConstants.PRODUCT_CODE_HPELITE840;
import static com.ishoal.app.integrationtest.IntegrationTestConstants.PRODUCT_CODE_HPSPECTRE;
import static com.ishoal.app.integrationtest.interactions.admin.ConfirmOrder.confirmOrder;
import static com.ishoal.app.integrationtest.interactions.buyer.PlaceOrder.anOrderFor;
import static com.ishoal.app.integrationtest.interactions.buyer.PlaceOrder.placeOrderAndReturnReference;
import static com.ishoal.app.integrationtest.interactions.buyer.UpdateBasket.basketItem;
import static com.ishoal.app.integrationtest.interactions.buyer.UpdateBasket.updateAndReturnBasket;
import static com.ishoal.app.integrationtest.matchers.AccessDeniedMatcher.accessDenied;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@DirtiesDb
public class OrderConfirmationIT extends AbstractIntegrationTest {

    @Test
    public void cannotConfirmOrderWhenNotAuthenticated() {
        usingNoAuthentication();
        confirmOrder(ORDER_REFERENCE_UNCONFIRMED, 0L)
                .then()
                .statusCode(accessDenied());
    }

    @Test
    public void cannotConfirmOrderWhenAuthenticatedAsBuyer() {
        usingValidBuyerAuthentication();
        confirmOrder(ORDER_REFERENCE_UNCONFIRMED, 0L)
                .then()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void shouldBeAbleToConfirmOrder() {
        usingValidAdminAuthentication();
        confirmOrder(ORDER_REFERENCE_UNCONFIRMED, 0L)
                .then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void badRequestReturnedWhenConfirmWithWrongVersionNumber() {
        usingValidAdminAuthentication();
        confirmOrder(ORDER_REFERENCE_UNCONFIRMED, 1L)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void confirmationShouldChangeOrderStatus() {
        usingValidAdminAuthentication();
        confirmOrder(ORDER_REFERENCE_UNCONFIRMED, 0L);
        assertThat(order(ORDER_REFERENCE_UNCONFIRMED).getSummary().getStatus(), is(OrderStatus.CONFIRMED.getDisplayName()));
    }

    @Test
    public void confirmationShouldIncreaseCurrentVolumeOnOffer() {
        usingValidAdminAuthentication();
        Long initialVolume = hpElite840().getCurrentVolume();
        confirmOrder(ORDER_REFERENCE_UNCONFIRMED, 0L);
        Long newVolume = hpElite840().getCurrentVolume();
        assertThat(newVolume, is(greaterThan(initialVolume)));
    }

    @Test
    public void confirmationShouldMoveCurrentPriceBandIfCurveHasMovedSinceOrderPlacement() {
        usingValidBuyerAuthentication();
        String curveMovingOrderReference = placeOrderForQuantity(1000);
        String orderToTestReference = placeOrderForQuantity(1);

        usingValidAdminAuthentication();
        confirmOrder(curveMovingOrderReference, 0L);

        confirmOrder(orderToTestReference, 0L);

        OrderLineDto orderLine = orderLine(orderToTestReference);
        assertThat(orderLine.getInitialUnitPrice(), is(not(orderLine.getCurrentUnitPrice())));
    }

    @Test
    public void confirmationShouldCreateCreditIfCurveHasMovedSincePlacement() {
        usingValidBuyerAuthentication();
        String curveMovingOrderReference = placeOrderForQuantity(1000);
        String orderToTestReference = placeOrderForQuantity(1);

        usingValidAdminAuthentication();
        confirmOrder(curveMovingOrderReference, 0L);

        confirmOrder(orderToTestReference, 0L);

        OrderLineDto orderLine = orderLine(orderToTestReference);
        assertThat(orderLine.getCredits(), hasSize(1));
    }

    @Test
    public void confirmationShouldCreateCreditOnMultipleOffersIfCurveMovedSincePlacement() {
        usingValidBuyerAuthentication();
        String curveMovingOrderReference = placeOrderForTwoProducts(1000, 1000L);
        String orderToTestReference = placeOrderForTwoProducts(1, 1);

        usingValidAdminAuthentication();
        confirmOrder(curveMovingOrderReference, 0L);
        confirmOrder(orderToTestReference, 0L);

        AdminOrderDto orderToTest = order(orderToTestReference);
        for(OrderLineDto line : orderToTest.getLines()) {
            assertThat(line.getCredits(), hasSize(1));
        }
    }

    @Test
    public void confirmationShouldCreateCreditOnMultipleOffersOnConfirmedOrderIfNewlyConfirmedOrderMovesPriceCurve() {
        usingValidBuyerAuthentication();
        String curveMovingOrderReference = placeOrderForTwoProducts(1000, 1000);
        String orderToTestReference = placeOrderForTwoProducts(1, 1);

        usingValidAdminAuthentication();
        confirmOrder(orderToTestReference, 0L);
        confirmOrder(curveMovingOrderReference, 0L);

        AdminOrderDto orderToTest = order(orderToTestReference);
        for(OrderLineDto line : orderToTest.getLines()) {
            assertThat(line.getCredits(), hasSize(1));
        }
    }

    private String placeOrderForQuantity(long quantity) {
        return placeOrderAndReturnReference(anOrderFor(updateAndReturnBasket(PRODUCT_CODE_HPELITE840, quantity)));
    }

    private String placeOrderForTwoProducts(long quantityHpElite840, long quantityHpSpectre) {
        ShoppingBasketItemDto hpElite840 = basketItem(PRODUCT_CODE_HPELITE840, quantityHpElite840);
        ShoppingBasketItemDto hpSpectre = basketItem(PRODUCT_CODE_HPSPECTRE, quantityHpSpectre);
       
        return placeOrderAndReturnReference(anOrderFor(updateAndReturnBasket(hpElite840, hpSpectre)));
    }

    private ProductDto hpElite840() {
        return FetchProduct.fetchProductAsDto(PRODUCT_CODE_HPELITE840);
    }

    private OrderLineDto orderLine(String orderReference) {
        AdminOrderDto order = order(orderReference);
        return order.getLines().get(0);
    }

    private AdminOrderDto order(String orderReference) {
        return AdminFetchOrder.adminFetchOrderAsDto(orderReference);
    }
}