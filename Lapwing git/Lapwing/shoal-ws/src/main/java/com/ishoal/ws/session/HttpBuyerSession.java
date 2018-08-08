package com.ishoal.ws.session;

import com.ishoal.core.domain.User;
import com.ishoal.ws.buyer.dto.ShoppingBasketDto;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static com.ishoal.ws.buyer.dto.ShoppingBasketDto.anEmptyShoppingBasket;

public class HttpBuyerSession implements BuyerSession {

    private static final String BASKET = "basket";
    private static final String BUYER = "buyer";

    private final HttpSession httpSession;

    public HttpBuyerSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @Override
    public ShoppingBasketDto getBasket() {
        return Optional.ofNullable((ShoppingBasketDto) httpSession.getAttribute(HttpBuyerSession.BASKET)).orElse(anEmptyShoppingBasket());
    }

    @Override
    public void updateBasket(ShoppingBasketDto basket) {
        httpSession.setAttribute(HttpBuyerSession.BASKET, basket);
    }

    @Override
    public void addBuyerToSession(User user) {
        httpSession.setAttribute(HttpBuyerSession.BUYER, user);
    }
    
    @Override
    public User getUser(){
        return (User) httpSession.getAttribute(HttpBuyerSession.BUYER);  
    }

    @Override
    public boolean removeUser() {
        
        boolean result = false;
        if(getUser() != null){
            httpSession.removeAttribute(HttpBuyerSession.BUYER);
            result = true;
        }else{
            result = false;
        }
        return result;
    }
    
}
