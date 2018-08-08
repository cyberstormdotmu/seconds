package com.ishoal.ws.session;

import com.ishoal.ws.buyer.dto.ShoppingBasketDto;

import static com.ishoal.ws.buyer.dto.ShoppingBasketDto.anEmptyShoppingBasket;

import com.ishoal.core.domain.User;

public class FakeBuyerSession implements BuyerSession {

    private ShoppingBasketDto basket = anEmptyShoppingBasket();

    private User user = null;
    
    /* This method is here to initially set a basket within the FakeBuyerSession
     * in a way which does not cause an invocation on updateBasket so that
     * we can verify interactions with updateBasket when using a Mockito Spy
     */
    public void initializeBasket(ShoppingBasketDto basket) {
        this.basket = basket;
    }

    @Override
    public ShoppingBasketDto getBasket() {
        synchronized (basket) {
            return basket;
        }
    }

    @Override
    public void updateBasket(ShoppingBasketDto basket) {
        synchronized (basket) {
            this.basket = basket;
        }
    }

    @Override
    public void addBuyerToSession(User user) {}
    
    @Override
    public User getUser(){
        return user;
    }

    @Override
    public boolean removeUser() {
        return false;
    }
    
}