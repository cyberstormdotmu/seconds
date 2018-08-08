package com.ishoal.core.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ishoal.common.util.OrderReferenceGenerationStrategy;
import com.ishoal.common.util.OrderReferenceGenerator;
import com.ishoal.common.util.TimeBasedOrderReferenceGenerationStrategy;
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
import com.ishoal.core.orders.PriceMovementProcessor;
import com.ishoal.core.orders.RegisterUserConfirmationService;
import com.ishoal.core.orders.VatCalculator;
import com.ishoal.core.payment.PaymentGatewayService;
import com.ishoal.core.payment.SupplierPaymentService;
import com.ishoal.core.persistence.adapter.OrderLineEntityAdapter;
import com.ishoal.core.persistence.repository.AddressEntityRepository;
import com.ishoal.core.persistence.repository.BuyerAppliedCreditEntityRepository;
import com.ishoal.core.persistence.repository.BuyerCreditEntityRepository;
import com.ishoal.core.persistence.repository.BuyerCreditInfoEntityRepository;
import com.ishoal.core.persistence.repository.BuyerProfileEntityRepository;
import com.ishoal.core.persistence.repository.BuyerVendorCreditEntityRepository;
import com.ishoal.core.persistence.repository.BuyerWithdrawCreditEntityRepository;
import com.ishoal.core.persistence.repository.CategoryEntityRepository;
import com.ishoal.core.persistence.repository.ContactUsEntityRepository;
import com.ishoal.core.persistence.repository.OfferEntityRepository;
import com.ishoal.core.persistence.repository.OrderEntityRepository;
import com.ishoal.core.persistence.repository.OrderLineEntityRepository;
import com.ishoal.core.persistence.repository.OrderPaymentEntityRepository;
import com.ishoal.core.persistence.repository.OrderReturnEntityRepository;
import com.ishoal.core.persistence.repository.PaymentGatewayChargesEntityRepository;
import com.ishoal.core.persistence.repository.PriceBandEntityRepository;
import com.ishoal.core.persistence.repository.ProductEntityRepository;
import com.ishoal.core.persistence.repository.ProductImagesEntityRepository;
import com.ishoal.core.persistence.repository.ProductSpecsEntityRepository;
import com.ishoal.core.persistence.repository.SupplierPaymentEntityRepository;
import com.ishoal.core.persistence.repository.UserEntityRepository;
import com.ishoal.core.persistence.repository.UserRoleEntityRepository;
import com.ishoal.core.persistence.repository.VatRateEntityRepository;
import com.ishoal.core.persistence.repository.VendorEntityRepository;
import com.ishoal.core.products.CategoryService;
import com.ishoal.core.products.CreateProductService;
import com.ishoal.core.products.ProductService;
import com.ishoal.core.products.SupplierCreditIntegrationService;
import com.ishoal.core.repository.AddressRepository;
import com.ishoal.core.repository.BuyerAppliedCreditRepository;
import com.ishoal.core.repository.BuyerCreditInfoRepository;
import com.ishoal.core.repository.BuyerProfileRepository;
import com.ishoal.core.repository.BuyerVendorCreditRepository;
import com.ishoal.core.repository.BuyerWithdrawCreditRepository;
import com.ishoal.core.repository.ContactUsRepository;
import com.ishoal.core.repository.OfferRepository;
import com.ishoal.core.repository.OrderRepository;
import com.ishoal.core.repository.OrderReturnRepository;
import com.ishoal.core.repository.PaymentGatewayChargesRepository;
import com.ishoal.core.repository.ProductRepository;
import com.ishoal.core.repository.SupplierPaymentRepository;
import com.ishoal.core.repository.UserRepository;
import com.ishoal.core.repository.VendorRepository;
import com.ishoal.core.user.ResetPasswordService;
import com.ishoal.core.user.SendSmsService;
import com.ishoal.core.user.UserService;
import com.ishoal.core.vendor.BuyerVendorCreditService;
import com.ishoal.core.vendor.ManageVendorService;
import com.ishoal.email.EmailService;
import com.ishoal.email.TemplatePropertiesFactory;
import com.ishoal.email.spring.SpringEmailService;
import com.ishoal.payment.buyer.BuyerPaymentService;
import com.ishoal.sms.SmsService;

@Configuration
@EntityScan(basePackages = { "com.ishoal.core" })
@EnableJpaRepositories(basePackages = { "com.ishoal.core" })
@EnableJpaAuditing
@EnableTransactionManagement
public class ShoalCoreBeanConfiguration {

	@Value("${shoal.webRootUrl}")
	private String webRootUrl;

	@Value("${shoal.mail.contactFormRecipientEmailAddress}")
	private String contactFormRecipientEmailAddress;

	@Resource
	private Environment env;

	@Resource(name = "userEntityRepository")
	private UserEntityRepository userEntityRepository;
	
	@Resource(name = "orderReturnEntityRepository")
	private OrderReturnEntityRepository orderReturnEntityRepository;

	@Resource(name = "userRoleEntityRepository")
	private UserRoleEntityRepository userRoleEntityRepository;

	@Resource(name = "productEntityRepository")
	private ProductEntityRepository productEntityRepository;
	
	@Resource(name = "productSpecsEntityRepository")
    private ProductSpecsEntityRepository productSpecsEntityRepository;
	
	@Resource(name = "productImagesEntityRepository")
    private ProductImagesEntityRepository productImagesEntityRepository;

	@Resource(name = "priceBandEntityRepository")
	private PriceBandEntityRepository priceBandEntityRepository;

	@Resource(name = "orderEntityRepository")
	private OrderEntityRepository orderEntityRepository;

	@Resource(name = "orderLineEntityRepository")
	private OrderLineEntityRepository orderLineEntityRepository;

	@Resource(name = "orderPaymentEntityRepository")
	private OrderPaymentEntityRepository orderPaymentEntityRepository;

	@Resource(name = "offerEntityRepository")
	private OfferEntityRepository offerEntityRepository;

	@Resource(name = "contactUsEntityRepository")
	private ContactUsEntityRepository contactUsEntityRepository;

	@Resource(name = "categoryEntityRepository")
	private CategoryEntityRepository categoryEntityRepository;

	@Resource(name = "vendorEntityRepository")
	private VendorEntityRepository vendorEntityRepository;

	@Resource(name = "vatRateEntityRepository")
	private VatRateEntityRepository vatRateEntityRepository;

	@Resource(name = "buyerCreditEntityRepository")
	private BuyerCreditEntityRepository buyerCreditEntityRepository;

	@Resource(name = "buyerAppliedCreditEntityRepository")
	private BuyerAppliedCreditEntityRepository buyerAppliedCreditEntityRepository;

	@Resource(name = "buyerVendorCreditEntityRepository")
	private BuyerVendorCreditEntityRepository buyerVendorCreditEntityRepository;

	@Resource(name = "buyerProfileEntityRepository")
	private BuyerProfileEntityRepository buyerProfileEntityRepository;

	@Resource(name = "paymentGatewayChargesEntityRepository")
	private PaymentGatewayChargesEntityRepository paymentGatewayChargesEntityRepository;

	@Resource(name = "addressEntityRepository")
	private AddressEntityRepository addressEntityRepository;

	@Resource(name = "buyerCreditInfoEntityRepository")
	private BuyerCreditInfoEntityRepository buyerCreditInfoEntityRepository;

	@Resource(name = "supplierPaymentEntityRepository")
	private SupplierPaymentEntityRepository supplierPaymentEntityRepository;

	@Resource(name = "buyerPaymentService")
	private BuyerPaymentService buyerPaymentService;

	@Resource(name = "smsService")
	private SmsService smsService;

	@Resource(name = "buyerWithdrawCreditEntityRepository")
	private BuyerWithdrawCreditEntityRepository buyerWithdrawCreditEntityRepository;

	@Bean
	public BuyerWithdrawCreditRepository buyerWithdrawCreditRepository() {
		return new BuyerWithdrawCreditRepository(buyerWithdrawCreditEntityRepository, buyerProfileRepository());
	}

	@Bean
	public OrderLineEntityAdapter orderLineEntityAdapter() {
		return new OrderLineEntityAdapter(productEntityRepository, offerEntityRepository, priceBandEntityRepository);
	}

	@Bean
	public UserRepository userRepository() {
		return new UserRepository(userEntityRepository, userRoleEntityRepository,vendorEntityRepository,buyerProfileEntityRepository);
	}

	@Bean
	public ProductRepository productRepository() {
		return new ProductRepository(productEntityRepository, categoryEntityRepository, vendorEntityRepository,
				vatRateEntityRepository, productSpecsEntityRepository, productImagesEntityRepository);
	}
	
	@Bean
	public JavaMailSenderImpl javaMailSenderImpl() {
		return new JavaMailSenderImpl();
	}

	@Bean
	public OfferRepository offerRepository() {
		return new OfferRepository(offerEntityRepository);
	}

	@Bean
	public OrderReturnRepository orderReturnRepository()
	{
		return new OrderReturnRepository(orderReturnEntityRepository, orderEntityRepository, orderLineEntityRepository);
	}
	
	@Bean
	public BuyerVendorCreditRepository buyerVendorCreditRepository() {
		return new BuyerVendorCreditRepository(buyerVendorCreditEntityRepository, buyerProfileEntityRepository);
	}

	@Bean
	public VendorRepository vendorRepository() {
		return new VendorRepository(vendorEntityRepository);
	}

	@Bean
	public SupplierPaymentRepository supplierPaymentRepository() {
		return new SupplierPaymentRepository(supplierPaymentEntityRepository);
	}

	@Bean
	public SendSmsService sendSmsService() {
		return new SendSmsService(userRepository(), smsService);
	}

	@Bean
	public ResetPasswordService resetPasswordService() {
		return new ResetPasswordService(userRepository(), smsService);
	}

	@Bean
	public ProductService productService() {
		return new ProductService(productRepository(), offerRepository());
	}

	@Bean
	public CategoryService categoryService() {
		return new CategoryService(categoryEntityRepository);
	}

	@Bean
	public BuyerWithdrawService buyerWithdrawService() {
		return new BuyerWithdrawService(buyerWithdrawCreditRepository());
	}

	@Bean
	public CreateProductService createProductService() {
		return new CreateProductService(productRepository());
	}

	@Bean
	public SupplierCreditIntegrationService supplierCreditIntegrationService() {
		return new SupplierCreditIntegrationService(vendorEntityRepository);
	}

	@Bean
	public ContactService contactService() {
		return new ContactService(emailService(), contactFormRecipientEmailAddress, contactUsRepository());
	}

	@Bean
	public OfferReportService offerReportService() {
		return new OfferReportService(productRepository(), orderRepository(),supplierPaymentService());
	}

	@Bean
	public BuyerAppliedCreditRepository buyerAppliedCreditRepository() {
		return new BuyerAppliedCreditRepository(buyerAppliedCreditEntityRepository, buyerVendorCreditEntityRepository,
				buyerProfileEntityRepository);
	}

	@Bean
	public OrderRepository orderRepository() {
		return new OrderRepository(userEntityRepository, orderEntityRepository, orderLineEntityAdapter(),
				buyerAppliedCreditRepository(), buyerProfileEntityRepository, buyerVendorCreditEntityRepository, orderReturnRepository());
	}

	@Bean
	public BuyerProfileRepository buyerProfileRepository() {
		return new BuyerProfileRepository(buyerProfileEntityRepository);
	}

	@Bean
	public BuyerCreditInfoRepository creditInfoRepository() {
		return new BuyerCreditInfoRepository(addressEntityRepository, buyerProfileEntityRepository,
				buyerCreditInfoEntityRepository);
	}

	@Bean
	public ContactUsRepository contactUsRepository() {
		return new ContactUsRepository(contactUsEntityRepository);
	}

	@Bean
	public AddressRepository addressRepository() {
		return new AddressRepository(addressEntityRepository, buyerProfileEntityRepository);
	}

	@Bean
	public PaymentGatewayChargesRepository paymentGatewayChargesRepository() {
		return new PaymentGatewayChargesRepository(paymentGatewayChargesEntityRepository);
	}

	@Bean
	public VatCalculator vatCalculator() {
		return new VatCalculator();
	}

	@Bean
	public PriceMovementProcessor priceMovementProcessor() {
		return new PriceMovementProcessor(vatCalculator());
	}

	@Bean
	public OrderSeekService orderSeekService() {
		return new OrderSeekService(orderRepository(), buyerVendorCreditEntityRepository, buyerProfileEntityRepository,
				orderLineEntityRepository, orderPaymentEntityRepository);
	}

	@Bean
	public OrderCancellationService orderCancellationService() {
		return new OrderCancellationService(orderRepository(), buyerAppliedCreditRepository(),
				buyerVendorCreditRepository());
	}

	@Bean
	public OrderConfirmationService orderConfirmationService() {
		return new OrderConfirmationService(productService(), orderRepository(), priceMovementProcessor(),
				buyerAppliedCreditRepository());
	}

	@Bean
	public BuyerVendorCreditService buyerVendorCreditService() {
		return new BuyerVendorCreditService(buyerVendorCreditRepository(),vendorRepository());
	}

	@Bean
	public OrderCreationService orderCreationService() {
		return new OrderCreationService(productService(), orderRepository(), vatCalculator(), buyerProfileService(),
				buyerPaymentService, buyerAppliedCreditRepository(), buyerVendorCreditRepository(),
				paymentGatewayService());
	}

	@Bean
	public OrderPaymentService orderPaymentService() {
		return new OrderPaymentService(orderRepository(), buyerVendorCreditRepository());
	}

	@Bean
	public BuyerCreditService buyerCreditService() {
		return new BuyerCreditService(buyerCreditEntityRepository);
	}

	@Bean
	public OrderReturnsService orderReturnsService()
	{
		return new OrderReturnsService(orderReturnRepository(), orderRepository());
	}
	
	@Bean
	public BuyerRegistrationService createBuyerProfileService() {
		return new BuyerRegistrationService(buyerProfileRepository(), userRepository());
	}

	@Bean
	public ManageBuyerProfileService buyerProfileService() {
		return new ManageBuyerProfileService(buyerProfileRepository());
	}

	@Bean
	public ManageAddressService addressService() {
		return new ManageAddressService(addressRepository());
	}

	@Bean
	public ManageBuyerCreditInfoService manageBuyerCreditInfoService() {
		return new ManageBuyerCreditInfoService(creditInfoRepository());
	}

	@Bean
	public ManageVendorService manageVendorService() {
		return new ManageVendorService(vendorRepository());
	}

	@Bean
	public RegisterUserConfirmationService registerUserConfirmationService() {
		return new RegisterUserConfirmationService(userRepository());
	}

	@Bean
	public ActivateBuyerService activateBuyerService() {
		return new ActivateBuyerService(emailService(), templatePropertiesFactory(), userRepository());
	}

	@Bean
	public UserService userService() {
		return new UserService(userRepository(), emailService(), templatePropertiesFactory());
	}

	@Bean
	public PaymentGatewayService paymentGatewayService() {
		return new PaymentGatewayService(paymentGatewayChargesRepository());
	}

	@Bean
	public SupplierPaymentService supplierPaymentService() {
		return new SupplierPaymentService(supplierPaymentRepository());
	}

	@Bean
	TemplatePropertiesFactory templatePropertiesFactory() {
		return new TemplatePropertiesFactory(webRootUrl);
	}

	@Bean
	public OrderReferenceGenerationStrategy orderReferenceGenerationStrategy() {
		String serverNumber = env.getProperty("shoal.serverNumber");
		OrderReferenceGenerationStrategy strategy = new TimeBasedOrderReferenceGenerationStrategy(
				Integer.parseInt(serverNumber));
		OrderReferenceGenerator.setStrategy(strategy);
		return strategy;
	}

	@Bean
	public EmailService emailService() {
		return new SpringEmailService(javaMailSenderImpl());
	}
}