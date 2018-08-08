package com.ishoal.ws.session;

import com.ishoal.core.domain.User;
import com.ishoal.ws.buyer.dto.ShoppingBasketDto;

public interface BuyerSession {
    
    ShoppingBasketDto getBasket();

    void updateBasket(ShoppingBasketDto basket);
    
    void addBuyerToSession(User user);
    
    User getUser();
    
    boolean removeUser();
    
}
