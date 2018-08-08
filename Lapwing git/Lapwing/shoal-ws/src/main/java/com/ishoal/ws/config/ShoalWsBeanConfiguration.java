package com.ishoal.ws.config;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ishoal.core.buyer.ActivateBuyerService;
import com.ishoal.core.buyer.BuyerRegistrationService;
import com.ishoal.core.buyer.ManageAddressService;
import com.ishoal.core.buyer.ManageBuyerCreditInfoService;
import com.ishoal.core.buyer.ManageBuyerProfileService;
import com.ishoal.core.contact.ContactService;
import com.ishoal.core.credit.BuyerCreditService;
import com.ishoal.core.credit.BuyerWithdrawService;
import com.ishoal.core.offer.OfferReportService;
import com.ishoal.core.orders.OrderCancellationService;
import com.ishoal.core.orders.OrderConfirmationService;
import com.ishoal.core.orders.OrderCreationService;
import com.ishoal.core.orders.OrderPaymentService;
import com.ishoal.core.orders.OrderReturnsService;
import com.ishoal.core.orders.OrderSeekService;
import com.ishoal.core.orders.VatCalculator;
import com.ishoal.core.payment.PaymentGatewayService;
import com.ishoal.core.payment.SupplierPaymentService;
import com.ishoal.core.products.CategoryService;
import com.ishoal.core.products.CreateProductService;
import com.ishoal.core.products.ProductService;
import com.ishoal.core.products.SupplierCreditIntegrationService;
import com.ishoal.core.user.ResetPasswordService;
import com.ishoal.core.user.SendSmsService;
import com.ishoal.core.user.UserService;
import com.ishoal.core.vendor.BuyerVendorCreditService;
import com.ishoal.core.vendor.ManageVendorService;
import com.ishoal.ws.admin.controller.AdminLapwingWithdrawCreditController;
import com.ishoal.ws.admin.controller.AdminManageCategoriesController;
import com.ishoal.ws.admin.controller.AdminOfferListingController;
import com.ishoal.ws.admin.controller.AdminOfferReportController;
import com.ishoal.ws.admin.controller.AdminOrderCancellationController;
import com.ishoal.ws.admin.controller.AdminOrderConfirmationController;
import com.ishoal.ws.admin.controller.AdminOrderPaymentController;
import com.ishoal.ws.admin.controller.AdminOrderSeekController;
import com.ishoal.ws.admin.controller.AdminPlaceBuyerOrderController;
import com.ishoal.ws.admin.controller.AdminSupplierPaymentController;
import com.ishoal.ws.admin.controller.BuyersController;
import com.ishoal.ws.admin.controller.CreateProductController;
import com.ishoal.ws.admin.controller.LapwingCreditWithdrawController;
import com.ishoal.ws.admin.controller.SupplierCreditIntegrationController;
import com.ishoal.ws.buyer.controller.AddressController;
import com.ishoal.ws.buyer.controller.BuyerCreditInfoController;
import com.ishoal.ws.buyer.controller.BuyerProfileController;
import com.ishoal.ws.buyer.controller.ContactRequestController;
import com.ishoal.ws.buyer.controller.CreditController;
import com.ishoal.ws.buyer.controller.CreditWithdrawController;
import com.ishoal.ws.buyer.controller.OrderCreationController;
import com.ishoal.ws.buyer.controller.OrderResponseXMLReader;
import com.ishoal.ws.buyer.controller.OrderSeekController;
import com.ishoal.ws.buyer.controller.ProductController;
import com.ishoal.ws.buyer.controller.RegistrationController;
import com.ishoal.ws.buyer.controller.SendSmsController;
import com.ishoal.ws.buyer.controller.ShoppingBasketController;
import com.ishoal.ws.buyer.controller.VatRateController;
import com.ishoal.ws.buyer.controller.VendorController;
import com.ishoal.ws.buyer.dto.adapter.BuyerOrderDtoAdapter;
import com.ishoal.ws.buyer.dto.adapter.PlaceOrderRequestDtoAdapter;
import com.ishoal.ws.buyer.dto.validator.PlaceOrderRequestValidator;
import com.ishoal.ws.common.controller.AuthenticationController;
import com.ishoal.ws.common.controller.ResetPasswordController;
import com.ishoal.ws.common.dto.adapter.OrderSummaryDtoAdapter;
import com.ishoal.ws.exceptionhandler.GlobalExceptionHandler;
import com.ishoal.ws.security.LoginUserDetailsService;
import com.ishoal.ws.security.UserArgumentResolver;
import com.ishoal.ws.session.BuyerSessionArgumentResolver;
import com.ishoal.ws.session.PasswordResetSessionArgumentResolver;
@Configuration
public class ShoalWsBeanConfiguration extends WebMvcConfigurerAdapter {
	
	 
    @Resource(name = "userService")
    private UserService userService;
    
    @Resource(name = "supplierCreditIntegrationService")
    private SupplierCreditIntegrationService supplierCreditIntegrationService;
    
    @Resource(name = "activateBuyerService")
    private ActivateBuyerService activateBuyerService;

    @Resource(name = "orderSeekService")
    private OrderSeekService orderSeekService;

    @Resource(name = "orderCreationService")
    private OrderCreationService orderCreationService;

    @Resource(name = "orderCancellationService")
    private OrderCancellationService orderCancellationService;

    @Resource(name = "orderConfirmationService")
    private OrderConfirmationService orderConfirmationService;

    @Resource(name = "orderPaymentService")
    private OrderPaymentService orderPaymentService;

    @Resource(name = "productService")
    private ProductService productService;

    @Resource(name = "categoryService")
    private CategoryService categoryService;

    @Resource(name = "createProductService")
    private CreateProductService createProductService;

    @Resource(name = "contactService")
    private ContactService contactService;

    @Resource(name = "offerReportService")
    private OfferReportService offerReportService;

    @Resource(name = "buyerCreditService")
    private BuyerCreditService buyerCreditService;
      
    @Resource(name="buyerVendorCreditService")
    private BuyerVendorCreditService buyerVendorCreditService;
    
    @Resource(name = "manageBuyerCreditInfoService")
    private ManageBuyerCreditInfoService manageBuyerCreditInfoService;

    @Resource(name = "createBuyerProfileService")
    private BuyerRegistrationService buyerRegistrationService;

    @Resource(name = "buyerProfileService")
    private ManageBuyerProfileService manageBuyerProfileService;
    
    @Resource(name = "addressService")
    private ManageAddressService manageAddressService;
    
    @Resource(name = "resetPasswordService")
    private ResetPasswordService resetPasswordService;
    
    @Resource(name="buyerWithdrawService")
    private BuyerWithdrawService buyerWithdrawService;
    
    @Resource(name = "manageVendorService")
    private ManageVendorService manageVendorService;
    
    @Resource(name = "sendSmsService")
    private SendSmsService sendSmsService;

    @Resource(name="paymentGatewayService")
    private PaymentGatewayService paymentGatewayService;
    
    @Resource(name="supplierPaymentService")
    private SupplierPaymentService supplierPaymentService;
    
    @Resource(name = "vatCalculator")
    private VatCalculator vatCalculator;

    @Resource(name="orderReturnsService")
    private OrderReturnsService orderReturnsService;
    
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    public LoginUserDetailsService loginUserDetailsService() {
        return new LoginUserDetailsService(userService);
    }

    @Bean
    public AuthenticationController loginController() {
        return new AuthenticationController();
    }

    @Bean
    public OrderResponseXMLReader orderResponseXMLReader(){
        return new OrderResponseXMLReader(orderConfirmationService);
    }
    
    @Bean
    public ProductController productController() {
        return new ProductController(productService, categoryService);
    }
    
    @Bean
    public AdminManageCategoriesController adminManageCategoriesController() {
        return new AdminManageCategoriesController(categoryService);
    }
    
    @Bean
    public AdminPlaceBuyerOrderController adminPlaceBuyerOrderController() {
        return new AdminPlaceBuyerOrderController(userService,manageBuyerProfileService);
    }
    
    @Bean
    public SupplierCreditIntegrationController supplierCreditIntegrationController() {
        return new SupplierCreditIntegrationController(supplierCreditIntegrationService);
    }

    @Bean
    public VendorController vendorController() {
        return new VendorController(manageVendorService);
    }
    
    @Bean
    public AdminLapwingWithdrawCreditController adminLapwingWithdrawCreditController(){
        return new AdminLapwingWithdrawCreditController(buyerWithdrawService);
    }
    
    @Bean
    public LapwingCreditWithdrawController lapwingCreditWithdrawController(){
    	return new LapwingCreditWithdrawController(buyerWithdrawService);
    }
    
    @Bean
    public VatRateController vatRateController() {
        return new VatRateController();
    }

    @Bean
    public AdminOfferListingController adminOfferListingController() {
        return new AdminOfferListingController(productService);
    }

    @Bean
    public AdminOfferReportController adminOfferReportController() {
        return new AdminOfferReportController(offerReportService);
    }

    @Bean
    public ShoppingBasketController shoppingBasketController() {
        return new ShoppingBasketController(productService, vatCalculator);
    }

    @Bean
    public OrderCreationController orderCreationController() {
        return new OrderCreationController(orderCreationService, placeOrderRequestDtoAdapter(), placeOrderRequestValidator(),
                orderSummaryDtoAdapter(), orderConfirmationService);
    }
    
    @Bean
    public RegistrationController registrationController() {
        return new RegistrationController(buyerRegistrationService);
    }

    @Bean
    public BuyerProfileController profileController() {
        return new BuyerProfileController(manageBuyerProfileService);
    }
    @Bean
    public AddressController addressController() {
        return new AddressController(manageAddressService);
    }
    
    @Bean
    public OrderSeekController orderSeekController() {
        return new OrderSeekController(orderSeekService, orderSummaryDtoAdapter(), buyerOrderDtoAdapter());
    }

    @Bean
    public CreditController creditController() {
        return new CreditController(buyerCreditService,buyerWithdrawService,buyerVendorCreditService, paymentGatewayService);
    }

    @Bean
    public BuyersController userAccessController() {
        return new BuyersController(userService, manageBuyerProfileService, activateBuyerService,orderSeekService,
        		manageVendorService,buyerCreditService,buyerWithdrawService,buyerVendorCreditService);
    }

    
    @Bean
    public ResetPasswordController resetPasswordController() {
        return new ResetPasswordController(resetPasswordService);
    }
    
    @Bean
    public SendSmsController sendSmsController() {
        return new SendSmsController(sendSmsService);
    }


    @Bean
    public CreateProductController createProductController() {
        return new CreateProductController(createProductService, productService,orderSeekService);
    }

    @Bean
    public ContactRequestController contactRequestController() {
        return new ContactRequestController(contactService);
    }

    @Bean
    public AdminOrderSeekController adminOrderSeekController() {
        return new AdminOrderSeekController(orderSeekService, orderReturnsService);
    }

    @Bean
    public AdminOrderCancellationController adminOrderCancellationController() {
        return new AdminOrderCancellationController(orderCancellationService);
    }

    @Bean
    public AdminOrderConfirmationController adminOrderConfirmationController() {
        return new AdminOrderConfirmationController(orderConfirmationService);
    }

    @Bean
    public AdminOrderPaymentController adminOrderPaymentController() {
        return new AdminOrderPaymentController(orderPaymentService, orderConfirmationService);
    }
    
    @Bean
    public CreditWithdrawController creditWithdrawController() {
        return new CreditWithdrawController(buyerWithdrawService);
    }
    
    @Bean
    public AdminSupplierPaymentController adminSupplierPaymentController(){
    	return new AdminSupplierPaymentController(supplierPaymentService);
    }

    @Bean
    public PlaceOrderRequestDtoAdapter placeOrderRequestDtoAdapter() {
        return new PlaceOrderRequestDtoAdapter();
    }

    @Bean
    public PlaceOrderRequestValidator placeOrderRequestValidator() {
        return new PlaceOrderRequestValidator();
    }

    @Bean
    public OrderSummaryDtoAdapter orderSummaryDtoAdapter() {
        return new OrderSummaryDtoAdapter();
    }

    
    @Bean
    public BuyerOrderDtoAdapter buyerOrderDtoAdapter() {
        return new BuyerOrderDtoAdapter();
    }

    @Bean
    public UserArgumentResolver userArgumentResolver() {
        return new UserArgumentResolver(userService);
    }

    @Bean
    public BuyerSessionArgumentResolver buyerSessionArgumentResolver() {
        return new BuyerSessionArgumentResolver();
    }

    @Bean
    public SendSmsSessionArgumentResolver sendSmsSessionArgumentResolver() {
        return new SendSmsSessionArgumentResolver();
    }
    
    @Bean
    public PasswordResetSessionArgumentResolver passwordResetSessionArgumentResolver() {
        return new PasswordResetSessionArgumentResolver();
    }
    
    @Bean
    public BuyerCreditInfoController buyerCreditInfoController() {
        return new BuyerCreditInfoController(manageBuyerCreditInfoService,manageBuyerProfileService);
    }
  
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userArgumentResolver());
        argumentResolvers.add(buyerSessionArgumentResolver());
        argumentResolvers.add(passwordResetSessionArgumentResolver());
        argumentResolvers.add(sendSmsSessionArgumentResolver());
    }
}