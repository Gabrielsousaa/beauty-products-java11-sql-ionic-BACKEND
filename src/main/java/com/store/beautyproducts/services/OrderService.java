package com.store.beautyproducts.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.store.beautyproducts.Security.UserSS;
import com.store.beautyproducts.domain.tb_Client;
import com.store.beautyproducts.domain.tb_ItemOrder;
import com.store.beautyproducts.domain.tb_Order;
import com.store.beautyproducts.domain.tb_PaymentWithBankSlip;
import com.store.beautyproducts.domain.enums.StatusPayment;
import com.store.beautyproducts.repositories.ItemOrderRepository;
import com.store.beautyproducts.repositories.OrderRepository;
import com.store.beautyproducts.repositories.PaymentRepository;
import com.store.beautyproducts.services.exceptions.AuthorizationException;
import com.store.beautyproducts.services.exceptions.ObjectNotFoundException;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repo;

    @Autowired
    private BankSlipService backslipService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ItemOrderRepository itemOrderRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private EmailService emailService;


    public tb_Order find(Integer id) {
        Optional<tb_Order> obj = repo.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Object not found ID: " + id + ", TypeOf: " + tb_Order.class.getName()));
    }

    public tb_Order insert(tb_Order obj) {
        obj.setId(null);
        obj.setInstant(new Date());
        obj.setClient(clientService.find(obj.getClient().getId()));
        obj.getPayment().setStatus(StatusPayment.PENDING);
        obj.getPayment().setOrder(obj);
        if (obj.getPayment() instanceof tb_PaymentWithBankSlip) {
            tb_PaymentWithBankSlip pagto = (tb_PaymentWithBankSlip) obj.getPayment();
            backslipService.fillPaymentWithBankSlip(pagto, obj.getInstant());
        }
        obj = repo.save(obj);
        paymentRepository.save(obj.getPayment());
        for (tb_ItemOrder ip : obj.getItens()) {
            ip.setDiscount(0.0);
            ip.setProduct(productService.find(ip.getProduct().getId()));
            ip.setPrice(ip.getProduct().getPrice());
            ip.setOrder(obj);

        }
        itemOrderRepository.saveAll(obj.getItens());
        emailService.sendOrderConfirmationHtmlEmail(obj);
        return obj;
    }

    public Page<tb_Order> findPage( Integer page, Integer linesPerPage, String orderBy,String direction) {
        UserSS user = UserService.authenticate();
        if(user == null){
            throw new AuthorizationException("Acesso negado");
        }    

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
        tb_Client client = clientService.find(user.getUserId());
        return repo.findByClient(client, pageRequest);
    }
}
