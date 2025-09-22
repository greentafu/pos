package com.project.pos.revenue.controller;

import com.project.pos.discount.dto.DiscountDTO;
import com.project.pos.discount.service.DiscountService;
import com.project.pos.option.dto.OptionDTO;
import com.project.pos.option.service.OptionService;
import com.project.pos.product.dto.ProductDTO;
import com.project.pos.product.service.ProductService;
import com.project.pos.revenue.dto.*;
import com.project.pos.revenue.dto.requestDTO.*;
import com.project.pos.revenue.dto.responseDTO.*;
import com.project.pos.revenue.service.*;
import com.project.pos.store.dto.PosDTO;
import com.project.pos.store.dto.StoreDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Log4j2
public class OrderApiController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OptionService optionService;
    @Autowired
    private OrderItemOptionService orderItemOptionService;
    @Autowired
    private OrderItemDiscountService orderItemDiscountService;
    @Autowired
    private OrderDiscountService orderDiscountService;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/api/getOrderPage")
    public OrderPageDTO getOrderPage(@RequestParam(value = "waiting") Integer waiting, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PosDTO posDTO=(PosDTO) session.getAttribute("currentPos");

        OrdersDTO existOrdersDTO = orderService.getOrdersByStorePosWaiting(storeDTO, posDTO, waiting);
        if(existOrdersDTO==null) return null;

        List<OrderItemDTO> orderItemList = orderItemService.getOrderItemByOrders(existOrdersDTO);

        List<OrderRowDTO> rowList = new ArrayList<>();
        Long totalCount = 0L;
        Long totalPayment = 0L;

        NumberFormat formatter = NumberFormat.getInstance();
        if(orderItemList!=null && !orderItemList.isEmpty()){
            for(OrderItemDTO orderItemDTO:orderItemList){
                List<OptionDTO> itemOptionList = orderItemOptionService.getOptionListByOrderItem(orderItemDTO);
                List<DiscountDTO> perBasicDiscountList = orderItemDiscountService.getDiscountListByOrderItem(orderItemDTO, true, true);
                List<DiscountDTO> perPercentDiscountList = orderItemDiscountService.getDiscountListByOrderItem(orderItemDTO, true, false);
                List<DiscountDTO> allBasicDiscountList = orderItemDiscountService.getDiscountListByOrderItem(orderItemDTO, false, true);
                List<DiscountDTO> allPercentDiscountList = orderItemDiscountService.getDiscountListByOrderItem(orderItemDTO, false, false);

                Long basicPayment = 0L;

                Long tempTotal = orderItemDTO.getTotalPerUnit();
                OrderRowDTO itemDTO = new OrderRowDTO("item", existOrdersDTO.getId(), orderItemDTO.getId(), orderItemDTO.getName(),
                        formatter.format(orderItemDTO.getProductPerUnit()), orderItemDTO.getProductCount(), null,
                        formatter.format(tempTotal));
                rowList.add(itemDTO);
                totalCount += orderItemDTO.getProductCount();
                totalPayment += tempTotal;
                basicPayment = tempTotal;

                if(itemOptionList != null && !itemOptionList.isEmpty()){
                    itemOptionList.forEach(option -> {
                        OrderRowDTO optionDTO = new OrderRowDTO("option", orderItemDTO.getId(), option.getId(), option.getName(),
                                null, null, null, null);
                        rowList.add(optionDTO);
                    });
                }
                if(perBasicDiscountList != null && !perBasicDiscountList.isEmpty()){
                    for(DiscountDTO discount : perBasicDiscountList){
                        tempTotal = discount.getDiscountPrice()*orderItemDTO.getProductCount();
                        OrderRowDTO discountDTO = new OrderRowDTO("discount", orderItemDTO.getId(), discount.getId(), discount.getName(),
                                formatter.format(discount.getDiscountPrice()),
                                orderItemDTO.getProductCount(),'-'+formatter.format(tempTotal), null);
                        rowList.add(discountDTO);
                        totalPayment -= tempTotal;
                        basicPayment -= discount.getDiscountPrice();
                    }
                }
                if(perPercentDiscountList != null && !perPercentDiscountList.isEmpty()){
                    for(DiscountDTO discount : perPercentDiscountList){
                        tempTotal = Math.round(basicPayment*(discount.getDiscountPrice()/100.0)*orderItemDTO.getProductCount());
                        OrderRowDTO discountDTO = new OrderRowDTO("discount", orderItemDTO.getId(), discount.getId(), discount.getName(),
                                formatter.format(discount.getDiscountPrice())+"%",
                                orderItemDTO.getProductCount(), '-'+formatter.format(tempTotal), null);
                        rowList.add(discountDTO);
                        totalPayment -= tempTotal;
                        basicPayment -= basicPayment*(Math.round(discount.getDiscountPrice()/100.0));
                    }
                }
                if(allBasicDiscountList != null && !allBasicDiscountList.isEmpty()){
                    for(DiscountDTO discount : allBasicDiscountList){
                        tempTotal = discount.getDiscountPrice();
                        OrderRowDTO discountDTO = new OrderRowDTO("discount", orderItemDTO.getId(), discount.getId(), discount.getName(),
                                formatter.format(discount.getDiscountPrice()),
                                1L, '-'+formatter.format(tempTotal), null);
                        rowList.add(discountDTO);
                        totalPayment -= tempTotal;
                        basicPayment -= tempTotal;
                    }
                }
                if(allPercentDiscountList != null && !allPercentDiscountList.isEmpty()){
                    for(DiscountDTO discount : allPercentDiscountList){
                        tempTotal = Math.round(basicPayment*(discount.getDiscountPrice()/100.0));
                        OrderRowDTO discountDTO = new OrderRowDTO("discount", orderItemDTO.getId(), discount.getId(), discount.getName(),
                                formatter.format(discount.getDiscountPrice())+'%',
                                1L, '-'+formatter.format(tempTotal), null);
                        rowList.add(discountDTO);
                        totalPayment -= tempTotal;
                    }
                }
            }
        }

        List<DiscountDTO> allBasicDiscountList = orderDiscountService.getDiscountListByOrder(existOrdersDTO, true);
        List<DiscountDTO> allPercentDiscountList = orderDiscountService.getDiscountListByOrder(existOrdersDTO, false);
        if(allBasicDiscountList != null && !allBasicDiscountList.isEmpty()){
            for(DiscountDTO discount : allBasicDiscountList){
                Long tempTotal = discount.getDiscountPrice();
                OrderRowDTO discountDTO = new OrderRowDTO("allDiscount", existOrdersDTO.getId(), discount.getId(), discount.getName(),
                        formatter.format(discount.getDiscountPrice()),
                        1L, '-'+formatter.format(tempTotal), null);
                rowList.add(discountDTO);
                totalPayment -= tempTotal;
            }
        }
        if(allPercentDiscountList != null && !allPercentDiscountList.isEmpty()){
            for(DiscountDTO discount : allPercentDiscountList){
                Long tempTotal = Math.round(totalPayment*(discount.getDiscountPrice()/100.0));
                OrderRowDTO discountDTO = new OrderRowDTO("allDiscount", existOrdersDTO.getId(), discount.getId(), discount.getName(),
                        formatter.format(discount.getDiscountPrice())+'%',
                        1L, '-'+formatter.format(tempTotal), null);
                rowList.add(discountDTO);
            }
        }

        Long paymentPrice = paymentMethodService.getPaymentPriceByOrder(existOrdersDTO);
        if(paymentPrice==null) paymentPrice=0L;

        return new OrderPageDTO(existOrdersDTO, null, paymentPrice, totalCount, rowList);
    }
    @GetMapping("/api/getOrderItemOptionDetail")
    public OrderItemOptionDetailDTO getOrderItemOptionDetail(@RequestParam(value = "id") String id) {
        OrderItemDTO orderItemDTO = orderItemService.getOrderItemDTOById(Long.parseLong(id));
        List<OptionDTO> optionList = orderItemOptionService.getOptionListByOrderItem(orderItemDTO);
        return new OrderItemOptionDetailDTO(orderItemDTO, optionList);
    }
    @GetMapping("/api/getOrderItemDiscountDetail")
    public OrderItemDiscountDetailDTO getOrderItemDiscountDetail(@RequestParam(value = "id") String id) {
        OrderItemDTO orderItemDTO = orderItemService.getOrderItemDTOById(Long.parseLong(id));
        List<DiscountDTO> discountList = orderItemDiscountService.getDiscountListByOnlyOrderItem(orderItemDTO);
        return new OrderItemDiscountDetailDTO(orderItemDTO, discountList);
    }
    @GetMapping("/api/getOrderDiscountDetail")
    public OrderDiscountDetailDTO getOrderDiscountDetail(@RequestParam(value = "waiting") Integer waiting, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PosDTO posDTO=(PosDTO) session.getAttribute("currentPos");
        OrdersDTO ordersDTO = orderService.getOrdersByStorePosWaiting(storeDTO, posDTO, waiting);
        if(ordersDTO==null) return null;
        List<DiscountDTO> discountList = orderDiscountService.getDiscountListByOnlyOrder(ordersDTO);
        return new OrderDiscountDetailDTO(ordersDTO, discountList);
    }
    @PostMapping("/api/saveOrderItemOption")
    public ResponseEntity<?> saveOrderItemOption(@RequestBody SaveOrderOptionDTO saveOrderOptionDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PosDTO posDTO=(PosDTO) session.getAttribute("currentPos");

        ProductDTO productDTO = productService.getProductDTOById(saveOrderOptionDTO.getProduct());

        OrdersDTO existOrdersDTO = orderService.getOrdersByStorePosWaiting(storeDTO, posDTO, saveOrderOptionDTO.getWaiting());
        if(existOrdersDTO==null) {
            OrdersDTO ordersDTO = OrdersDTO.builder()
                    .orderAmount(0L)
                    .orderDiscountAmount(0L)
                    .totalDiscountAmount(0L)
                    .totalPaymentAmount(0L)
                    .waiting(saveOrderOptionDTO.getWaiting())
                    .storeDTO(storeDTO)
                    .posDTO(posDTO)
                    .build();
            existOrdersDTO = orderService.createOrders(ordersDTO);
        }

        OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                .name(productDTO.getName())
                .productPrice(productDTO.getProductPrice())
                .productCost(productDTO.getProductCost())
                .productCount(1L)
                .productPerUnit(productDTO.getProductPrice())
                .totalOption(0L)
                .totalDiscount(0L)
                .totalPerUnit(0L)
                .totalPayment(0L)
                .storeDTO(storeDTO)
                .productDTO(productDTO)
                .ordersDTO(existOrdersDTO)
                .build();
        OrderItemDTO savedOrderItemDTO = orderItemService.createOrderItem(orderItemDTO);

        List<Long> optionList = saveOrderOptionDTO.getOptionList();
        for(Long temp:optionList){
            OptionDTO optionDTO = optionService.getOptionDTOById(temp);
            OrderItemOptionDTO existOrderItemOptionDTO = orderItemOptionService.getOrderItemOptionDTOById(savedOrderItemDTO.getId(), optionDTO.getId());

            if(existOrderItemOptionDTO==null){
                OrderItemOptionDTO orderItemOptionDTO = OrderItemOptionDTO.builder()
                        .optionName(optionDTO.getName())
                        .optionPrice(optionDTO.getOptionPrice())
                        .optionCost(optionDTO.getOptionCost())
                        .orderItemDTO(savedOrderItemDTO)
                        .optionDTO(optionDTO)
                        .build();
                orderItemOptionService.createOrderItemOption(orderItemOptionDTO);
            }
        }

        orderItemService.updateOrderItem(savedOrderItemDTO.getId());
        orderService.updateOrders(existOrdersDTO.getId());

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @PostMapping("/api/moveOrderItem")
    public ResponseEntity<?> moveOrderItem(@RequestBody UpdateOrderItemLocationDTO updateOrderItemLocationDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PosDTO posDTO=(PosDTO) session.getAttribute("currentPos");

        Long id = updateOrderItemLocationDTO.getId();
        Integer type = updateOrderItemLocationDTO.getType();
        if(type==1 || type==2){
            OrderItemDTO separateItem = orderItemService.getOrderItemDTOById(id);
            if(separateItem==null) return null;

            OrdersDTO existOrdersDTO = orderService.getOrdersByStorePosWaiting(storeDTO, posDTO, updateOrderItemLocationDTO.getWaiting());
            PaymentDTO orderPaymentDTO = paymentService.getPaymentByOrders(existOrdersDTO);
            if(orderPaymentDTO!=null){
                Long paymentMethodCount = paymentMethodService.getCountPaymentMethodByPayment(orderPaymentDTO);
                if(paymentMethodCount!=0L) existOrdersDTO = copyOrders(existOrdersDTO);
            }
            OrderItemDTO existItem = orderItemService.getOrderItemByCopyId(existOrdersDTO, separateItem.getCopyLocation());
            if(existItem==null && separateItem.getCopyLocation()!=null) existItem = orderItemService.getOrderItemByCopyLocation(existOrdersDTO, separateItem.getCopyLocation());
            if(existItem==null) existItem = orderItemService.getOrderItemByCopyLocation(existOrdersDTO, separateItem.getId());
            if(existItem==null) existItem = copyOrderItemDTO(existOrdersDTO, separateItem);

            OrdersDTO separateOrdersDTO = separateItem.getOrdersDTO();
            PaymentDTO separatePaymentDTO = paymentService.getPaymentByOrders(separateOrdersDTO);
            if(separatePaymentDTO!=null){
                Long paymentMethodCount = paymentMethodService.getCountPaymentMethodByPayment(separatePaymentDTO);
                if(paymentMethodCount!=0L) separateItem = copyOrdersByOrderItem(separateItem);
            }

            Long count = (type==1)?separateItem.getProductCount():1;
            orderItemService.updateOrderItemCount(existItem.getId(), existItem.getProductCount()+count);
            orderItemService.updateOrderItem(existItem.getId());
            orderService.updateOrders(existItem.getOrdersDTO().getId());

            if(separateItem.getProductCount()-count>0){
                orderItemService.updateOrderItemCount(separateItem.getId(), separateItem.getProductCount()-count);
                orderItemService.updateOrderItem(separateItem.getId());
            }else orderItemService.deleteOrderItem(separateItem.getId());
            orderService.updateOrders(separateItem.getOrdersDTO().getId());
        }else if(type==3 || type==4){
            OrderItemDTO existItem = orderItemService.getOrderItemDTOById(id);
            if(existItem==null) return null;

            OrdersDTO existOrdersDTO = orderService.getOrdersByStorePosWaiting(storeDTO, posDTO, updateOrderItemLocationDTO.getWaiting());
            PaymentDTO orderPaymentDTO = paymentService.getPaymentByOrders(existOrdersDTO);
            if(orderPaymentDTO!=null){
                Long paymentMethodCount = paymentMethodService.getCountPaymentMethodByPayment(orderPaymentDTO);
                if(paymentMethodCount!=0L) {
                    existItem = copyOrdersByOrderItem(existItem);
                    existOrdersDTO = existItem.getOrdersDTO();
                }
            }

            OrdersDTO separateOrdersDTO = orderService.getOrdersByStorePosWaiting(storeDTO, posDTO, 4);
            OrderItemDTO separateItem = null;
            if(separateOrdersDTO==null){
                OrdersDTO ordersDTO = OrdersDTO.builder()
                        .orderAmount(0L)
                        .orderDiscountAmount(0L)
                        .totalDiscountAmount(0L)
                        .totalPaymentAmount(0L)
                        .waiting(4)
                        .storeDTO(storeDTO)
                        .posDTO(posDTO)
                        .build();
                separateOrdersDTO = orderService.createOrders(ordersDTO);
                separateItem = copyOrderItemDTO(separateOrdersDTO, existItem);
            }else{
                separateItem = orderItemService.getOrderItemByCopyId(separateOrdersDTO, existItem.getCopyLocation());
                if(separateItem==null && existItem.getCopyLocation()!=null) separateItem = orderItemService.getOrderItemByCopyLocation(separateOrdersDTO, existItem.getCopyLocation());
                if(separateItem==null) separateItem = orderItemService.getOrderItemByCopyLocation(separateOrdersDTO, existItem.getId());
                if(separateItem==null) separateItem = copyOrderItemDTO(separateOrdersDTO, existItem);

            }

            Long count = (type==4)?existItem.getProductCount():1;
            orderItemService.updateOrderItemCount(separateItem.getId(), separateItem.getProductCount()+count);
            orderItemService.updateOrderItem(separateItem.getId());
            orderService.updateOrders(separateItem.getOrdersDTO().getId());

            if(existItem.getProductCount()-count>0){
                orderItemService.updateOrderItemCount(existItem.getId(), existItem.getProductCount()-count);
                orderItemService.updateOrderItem(existItem.getId());
            }else orderItemService.deleteOrderItem(existItem.getId());
            orderService.updateOrders(existItem.getOrdersDTO().getId());
        }

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @PutMapping("/api/updateOrderItemOption")
    public ResponseEntity<?> updateOrderItemOption(@RequestBody UpdateOrderItemOptionDTO updateOrderItemOptionDTO) {
        OrderItemDTO existOrderItemDTO = orderItemService.getOrderItemDTOById(updateOrderItemOptionDTO.getOrderItemId());

        PaymentDTO existPaymentDTO = paymentService.getPaymentByOrders(existOrderItemDTO.getOrdersDTO());
        if(existPaymentDTO!=null){
            Long paymentMethodCount = paymentMethodService.getCountPaymentMethodByPayment(existPaymentDTO);
            if(paymentMethodCount!=0L) existOrderItemDTO = copyOrdersByOrderItem(existOrderItemDTO);
        }

        List<Long> optionList = updateOrderItemOptionDTO.getOptionList();
        for(Long temp:optionList){
            OptionDTO optionDTO = optionService.getOptionDTOById(temp);
            OrderItemOptionDTO existOrderItemOptionDTO = orderItemOptionService.getOrderItemOptionDTOById(existOrderItemDTO.getId(), optionDTO.getId());

            if(existOrderItemOptionDTO==null){
                OrderItemOptionDTO orderItemOptionDTO = OrderItemOptionDTO.builder()
                        .optionName(optionDTO.getName())
                        .optionPrice(optionDTO.getOptionPrice())
                        .optionCost(optionDTO.getOptionCost())
                        .orderItemDTO(existOrderItemDTO)
                        .optionDTO(optionDTO)
                        .build();
                orderItemOptionService.createOrderItemOption(orderItemOptionDTO);
            }
        }

        List<Long> existOptions = orderItemOptionService.getOrderItemOptionList(existOrderItemDTO);
        List<Long> result = new ArrayList<>(existOptions);
        result.removeAll(optionList);
        for(Long temp:result) orderItemOptionService.deleteOrderItemOption(existOrderItemDTO.getId(), temp);

        orderItemService.updateOrderItem(existOrderItemDTO.getId());
        orderService.updateOrders(existOrderItemDTO.getOrdersDTO().getId());

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @PutMapping("/api/updateOrderItemDiscount")
    public ResponseEntity<?> updateOrderItemDiscount(@RequestBody UpdateOrderItemDiscountDTO updateOrderItemDiscountDTO) {
        OrderItemDTO existOrderItemDTO = orderItemService.getOrderItemDTOById(updateOrderItemDiscountDTO.getOrderItemId());

        PaymentDTO existPaymentDTO = paymentService.getPaymentByOrders(existOrderItemDTO.getOrdersDTO());
        if(existPaymentDTO!=null){
            Long paymentMethodCount = paymentMethodService.getCountPaymentMethodByPayment(existPaymentDTO);
            if(paymentMethodCount!=0L) existOrderItemDTO = copyOrdersByOrderItem(existOrderItemDTO);
        }

        List<Long> discountList = updateOrderItemDiscountDTO.getDiscountList();
        for(Long temp:discountList){
            DiscountDTO discountDTO = discountService.getDiscountDTOById(temp);
            OrderItemDiscountDTO existOrderItemDiscountDTO = orderItemDiscountService.getOrderItemDiscountDTOById(existOrderItemDTO.getId(), discountDTO.getId());

            if(existOrderItemDiscountDTO==null){
                OrderItemDiscountDTO orderItemDiscountDTO = OrderItemDiscountDTO.builder()
                        .discountPrice(discountDTO.getDiscountPrice())
                        .orderItemDTO(existOrderItemDTO)
                        .discountDTO(discountDTO)
                        .build();
                orderItemDiscountService.createOrderItemDiscount(orderItemDiscountDTO);
            }
        }

        List<Long> existDiscounts = orderItemDiscountService.getOrderItemDiscountList(existOrderItemDTO);
        List<Long> result = new ArrayList<>(existDiscounts);
        result.removeAll(discountList);
        for(Long temp:result) orderItemDiscountService.deleteOrderItemDiscount(existOrderItemDTO.getId(), temp);

        orderItemService.updateOrderItem(existOrderItemDTO.getId());
        orderService.updateOrders(existOrderItemDTO.getOrdersDTO().getId());

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @PutMapping("/api/updateOrderDiscount")
    public ResponseEntity<?> updateOrderDiscount(@RequestBody UpdateOrderDiscountDTO updateOrderDiscountDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PosDTO posDTO=(PosDTO) session.getAttribute("currentPos");
        OrdersDTO ordersDTO = orderService.getOrdersByStorePosWaiting(storeDTO, posDTO, updateOrderDiscountDTO.getWaiting());

        PaymentDTO existPaymentDTO = paymentService.getPaymentByOrders(ordersDTO);
        if(existPaymentDTO!=null){
            Long paymentMethodCount = paymentMethodService.getCountPaymentMethodByPayment(existPaymentDTO);
            if(paymentMethodCount!=0L) ordersDTO = copyOrders(ordersDTO);
        }

        List<Long> discountList = updateOrderDiscountDTO.getDiscountList();
        for(Long temp:discountList){
            DiscountDTO discountDTO = discountService.getDiscountDTOById(temp);
            OrderDiscountDTO existOrderDiscountDTO = orderDiscountService.getOrderDiscountDTOById(ordersDTO.getId(), discountDTO.getId());

            if(existOrderDiscountDTO==null){
                OrderDiscountDTO orderDiscountDTO = OrderDiscountDTO.builder()
                        .discountPrice(discountDTO.getDiscountPrice())
                        .ordersDTO(ordersDTO)
                        .discountDTO(discountDTO)
                        .build();
                orderDiscountService.createOrderDiscount(orderDiscountDTO);
            }
        }

        List<Long> existDiscounts = orderDiscountService.getOrderDiscountList(ordersDTO);
        List<Long> result = new ArrayList<>(existDiscounts);
        result.removeAll(discountList);
        for(Long temp:result) orderDiscountService.deleteOrderDiscount(ordersDTO.getId(), temp);

        orderService.updateOrders(ordersDTO.getId());

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @PutMapping("/api/updateOrderItemCount")
    public ResponseEntity<?> updateOrderItemCount(@RequestBody UpdateOrderItemCountDTO updateOrderItemCountDTO) {
        OrderItemDTO existOrderItemDTO = orderItemService.getOrderItemDTOById(updateOrderItemCountDTO.getId());

        PaymentDTO existPaymentDTO = paymentService.getPaymentByOrders(existOrderItemDTO.getOrdersDTO());
        if(existPaymentDTO!=null){
            Long paymentMethodCount = paymentMethodService.getCountPaymentMethodByPayment(existPaymentDTO);
            if(paymentMethodCount!=0L) existOrderItemDTO = copyOrdersByOrderItem(existOrderItemDTO);
        }

        if(updateOrderItemCountDTO.getCount()==0L) orderItemService.deleteOrderItem(existOrderItemDTO.getId());
        else{
            OrderItemDTO updateOrderItemDTO = orderItemService.updateOrderItemCount(existOrderItemDTO.getId(), updateOrderItemCountDTO.getCount());
            orderItemService.updateOrderItem(updateOrderItemDTO.getId());
        }
        orderService.updateOrders(existOrderItemDTO.getOrdersDTO().getId());

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @DeleteMapping("/api/deleteOrders")
    public ResponseEntity<?> deleteOrders(@RequestParam(value = "waiting") Integer waiting, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PosDTO posDTO=(PosDTO) session.getAttribute("currentPos");
        OrdersDTO ordersDTO = orderService.getOrdersByStorePosWaiting(storeDTO, posDTO, waiting);
        if(ordersDTO==null) return null;
        orderService.deleteOrders(ordersDTO.getId());

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @DeleteMapping("/api/deleteOrderItem")
    public ResponseEntity<?> deleteOrderItem(@RequestParam(value = "id") String id) {
        String type = id.split("_")[0];
        if(Objects.equals(type, "item")){
            Long orderItemId = Long.parseLong(id.split("_")[2]);

            OrderItemDTO existOrderItemDTO = orderItemService.getOrderItemDTOById(orderItemId);
            PaymentDTO existPaymentDTO = paymentService.getPaymentByOrders(existOrderItemDTO.getOrdersDTO());
            if(existPaymentDTO!=null){
                Long paymentMethodCount = paymentMethodService.getCountPaymentMethodByPayment(existPaymentDTO);
                if(paymentMethodCount!=0L) existOrderItemDTO = copyOrdersByOrderItem(existOrderItemDTO);
            }
            orderItemService.deleteOrderItem(existOrderItemDTO.getId());
            orderService.updateOrders(existOrderItemDTO.getOrdersDTO().getId());
        }else if(Objects.equals(type, "option")){
            Long orderItemId = Long.parseLong(id.split("_")[1]);
            Long optionId = Long.parseLong(id.split("_")[2]);

            OrderItemDTO existOrderItemDTO=orderItemOptionService.getOrderItemOptionDTOById(orderItemId, optionId).getOrderItemDTO();
            PaymentDTO existPaymentDTO = paymentService.getPaymentByOrders(existOrderItemDTO.getOrdersDTO());
            if(existPaymentDTO!=null){
                Long paymentMethodCount = paymentMethodService.getCountPaymentMethodByPayment(existPaymentDTO);
                if(paymentMethodCount!=0L) existOrderItemDTO = copyOrdersByOrderItem(existOrderItemDTO);
            }
            orderItemOptionService.deleteOrderItemOption(existOrderItemDTO.getId(), optionId);
            orderItemService.updateOrderItem(existOrderItemDTO.getId());
            orderService.updateOrders(existOrderItemDTO.getOrdersDTO().getId());
        }else if(Objects.equals(type, "discount")){
            Long orderItemId = Long.parseLong(id.split("_")[1]);
            Long discountId = Long.parseLong(id.split("_")[2]);

            OrderItemDTO existOrderItemDTO=orderItemDiscountService.getOrderItemDiscountDTOById(orderItemId, discountId).getOrderItemDTO();
            PaymentDTO existPaymentDTO = paymentService.getPaymentByOrders(existOrderItemDTO.getOrdersDTO());
            if(existPaymentDTO!=null){
                Long paymentMethodCount = paymentMethodService.getCountPaymentMethodByPayment(existPaymentDTO);
                if(paymentMethodCount!=0L) existOrderItemDTO = copyOrdersByOrderItem(existOrderItemDTO);
            }

            orderItemDiscountService.deleteOrderItemDiscount(existOrderItemDTO.getId(), discountId);
            orderItemService.updateOrderItem(existOrderItemDTO.getId());
            orderService.updateOrders(existOrderItemDTO.getOrdersDTO().getId());
        }else if(Objects.equals(type, "allDiscount")){
            Long orderId = Long.parseLong(id.split("_")[1]);
            Long discountId = Long.parseLong(id.split("_")[2]);

            OrdersDTO existOrdersDTO = orderService.getOrdersDTOById(orderId);
            PaymentDTO existPaymentDTO = paymentService.getPaymentByOrders(existOrdersDTO);
            if(existPaymentDTO!=null){
                Long paymentMethodCount = paymentMethodService.getCountPaymentMethodByPayment(existPaymentDTO);
                if(paymentMethodCount!=0L) existOrdersDTO = copyOrders(existOrdersDTO);
            }

            orderDiscountService.deleteOrderDiscount(existOrdersDTO.getId(), discountId);
            orderService.updateOrders(existOrdersDTO.getId());
        }
        return ResponseEntity.ok(Map.of("message", "Success"));
    }

    private OrdersDTO copyOrders(OrdersDTO ordersDTO){
        OrdersDTO tempOrdersDTO = OrdersDTO.builder()
                .orderAmount(ordersDTO.getOrderAmount())
                .orderDiscountAmount(ordersDTO.getOrderDiscountAmount())
                .totalDiscountAmount(ordersDTO.getTotalDiscountAmount())
                .totalPaymentAmount(ordersDTO.getTotalPaymentAmount())
                .waiting(ordersDTO.getWaiting())
                .storeDTO(ordersDTO.getStoreDTO())
                .posDTO(ordersDTO.getPosDTO())
                .build();
        OrdersDTO newOrdersDTO = orderService.createOrders(tempOrdersDTO);

        List<DiscountDTO> orderDiscountList = orderDiscountService.getDiscountListByOnlyOrder(ordersDTO);
        if(orderDiscountList!=null){
            orderDiscountList.forEach(discount -> {
                OrderDiscountDTO orderDiscountDTO = OrderDiscountDTO.builder()
                        .discountPrice(discount.getDiscountPrice())
                        .ordersDTO(newOrdersDTO)
                        .discountDTO(discount)
                        .build();
                orderDiscountService.createOrderDiscount(orderDiscountDTO);
            });
        }
        List<OrderItemDTO> itemList = orderItemService.getOrderItemByOrders(ordersDTO);
        if(itemList!=null){
            itemList.forEach(item -> {
                OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                        .name(item.getName())
                        .productPrice(item.getProductPrice())
                        .productCost(item.getProductCost())
                        .productCount(item.getProductCount())
                        .productPerUnit(item.getProductPerUnit())
                        .totalOption(item.getTotalOption())
                        .totalDiscount(item.getTotalDiscount())
                        .totalPerUnit(item.getTotalPerUnit())
                        .totalPayment(item.getTotalPayment())
                        .copyLocation((item.getCopyLocation()!=null)? item.getCopyLocation() : item.getId())
                        .storeDTO(item.getStoreDTO())
                        .productDTO(item.getProductDTO())
                        .ordersDTO(newOrdersDTO)
                        .build();
                OrderItemDTO newOrderItemDTO = orderItemService.createOrderItem(orderItemDTO);

                List<OptionDTO> optionList = orderItemOptionService.getOptionListByOrderItem(item);
                if(optionList!=null){
                    optionList.forEach(option -> {
                        OrderItemOptionDTO orderItemOptionDTO = OrderItemOptionDTO.builder()
                                .optionName(option.getName())
                                .optionCost(option.getOptionCost())
                                .optionPrice(option.getOptionPrice())
                                .optionDTO(option)
                                .orderItemDTO(newOrderItemDTO)
                                .build();
                        orderItemOptionService.createOrderItemOption(orderItemOptionDTO);
                    });
                }
                List<DiscountDTO> discountList = orderItemDiscountService.getDiscountListByOnlyOrderItem(item);
                if(discountList!=null){
                    discountList.forEach(discount -> {
                        OrderItemDiscountDTO orderItemDiscountDTO = OrderItemDiscountDTO.builder()
                                .discountPrice(discount.getDiscountPrice())
                                .discountDTO(discount)
                                .orderItemDTO(newOrderItemDTO)
                                .build();
                        orderItemDiscountService.createOrderItemDiscount(orderItemDiscountDTO);
                    });
                }
            });
        }

        PaymentDTO paymentDTO = paymentService.getPaymentByOrders(ordersDTO);
        paymentService.updatePaymentStatus(paymentDTO.getId(), false);
        orderService.updateOrdersWaiting(ordersDTO.getId(), null);
        return newOrdersDTO;
    }
    private OrderItemDTO copyOrdersByOrderItem(OrderItemDTO originOrderItemDTO){
        OrdersDTO ordersDTO = originOrderItemDTO.getOrdersDTO();
        OrdersDTO tempOrdersDTO = OrdersDTO.builder()
                .orderAmount(ordersDTO.getOrderAmount())
                .orderDiscountAmount(ordersDTO.getOrderDiscountAmount())
                .totalDiscountAmount(ordersDTO.getTotalDiscountAmount())
                .totalPaymentAmount(ordersDTO.getTotalPaymentAmount())
                .waiting(ordersDTO.getWaiting())
                .storeDTO(ordersDTO.getStoreDTO())
                .posDTO(ordersDTO.getPosDTO())
                .build();
        OrdersDTO newOrdersDTO = orderService.createOrders(tempOrdersDTO);

        List<DiscountDTO> orderDiscountList = orderDiscountService.getDiscountListByOnlyOrder(ordersDTO);
        if(orderDiscountList!=null){
            orderDiscountList.forEach(discount -> {
                OrderDiscountDTO orderDiscountDTO = OrderDiscountDTO.builder()
                        .discountPrice(discount.getDiscountPrice())
                        .ordersDTO(newOrdersDTO)
                        .discountDTO(discount)
                        .build();
                orderDiscountService.createOrderDiscount(orderDiscountDTO);
            });
        }
        OrderItemDTO resultOrderItemDTO = null;

        List<OrderItemDTO> itemList = orderItemService.getOrderItemByOrders(ordersDTO);
        if(itemList!=null){
            for(OrderItemDTO item:itemList){
                OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                        .name(item.getName())
                        .productPrice(item.getProductPrice())
                        .productCost(item.getProductCost())
                        .productCount(item.getProductCount())
                        .productPerUnit(item.getProductPerUnit())
                        .totalOption(item.getTotalOption())
                        .totalDiscount(item.getTotalDiscount())
                        .totalPerUnit(item.getTotalPerUnit())
                        .totalPayment(item.getTotalPayment())
                        .copyLocation(item.getCopyLocation()!=null?item.getCopyLocation() : item.getId())
                        .storeDTO(item.getStoreDTO())
                        .productDTO(item.getProductDTO())
                        .ordersDTO(newOrdersDTO)
                        .build();
                OrderItemDTO newOrderItemDTO = orderItemService.createOrderItem(orderItemDTO);
                if(item.getId()==originOrderItemDTO.getId()) resultOrderItemDTO = newOrderItemDTO;

                List<OptionDTO> optionList = orderItemOptionService.getOptionListByOrderItem(item);
                if(optionList!=null){
                    optionList.forEach(option -> {
                        OrderItemOptionDTO orderItemOptionDTO = OrderItemOptionDTO.builder()
                                .optionName(option.getName())
                                .optionCost(option.getOptionCost())
                                .optionPrice(option.getOptionPrice())
                                .optionDTO(option)
                                .orderItemDTO(newOrderItemDTO)
                                .build();
                        orderItemOptionService.createOrderItemOption(orderItemOptionDTO);
                    });
                }
                List<DiscountDTO> discountList = orderItemDiscountService.getDiscountListByOnlyOrderItem(item);
                if(discountList!=null){
                    discountList.forEach(discount -> {
                        OrderItemDiscountDTO orderItemDiscountDTO = OrderItemDiscountDTO.builder()
                                .discountPrice(discount.getDiscountPrice())
                                .discountDTO(discount)
                                .orderItemDTO(newOrderItemDTO)
                                .build();
                        orderItemDiscountService.createOrderItemDiscount(orderItemDiscountDTO);
                    });
                }
            }
        }

        PaymentDTO paymentDTO = paymentService.getPaymentByOrders(ordersDTO);
        paymentService.updatePaymentStatus(paymentDTO.getId(), false);
        orderService.updateOrdersWaiting(ordersDTO.getId(), null);
        return resultOrderItemDTO;
    }
    private OrderItemDTO copyOrderItemDTO(OrdersDTO copyOrdersDTO, OrderItemDTO originItemDTO){
        OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                .name(originItemDTO.getName())
                .productPrice(originItemDTO.getProductPrice())
                .productCost(originItemDTO.getProductCost())
                .productCount(0L)
                .productPerUnit(originItemDTO.getProductPerUnit())
                .totalOption(originItemDTO.getTotalOption())
                .totalDiscount(originItemDTO.getTotalDiscount())
                .totalPerUnit(originItemDTO.getTotalPerUnit())
                .totalPayment(originItemDTO.getTotalPayment())
                .copyLocation(originItemDTO.getId())
                .storeDTO(originItemDTO.getStoreDTO())
                .productDTO(originItemDTO.getProductDTO())
                .ordersDTO(copyOrdersDTO)
                .build();
        OrderItemDTO copyItemDTO = orderItemService.createOrderItem(orderItemDTO);

        List<OptionDTO> optionList = orderItemOptionService.getOptionListByOrderItem(originItemDTO);
        if(optionList!=null){
            for(OptionDTO option:optionList){
                OrderItemOptionDTO orderItemOptionDTO = OrderItemOptionDTO.builder()
                        .optionName(option.getName())
                        .optionCost(option.getOptionCost())
                        .optionPrice(option.getOptionPrice())
                        .optionDTO(option)
                        .orderItemDTO(copyItemDTO)
                        .build();
                orderItemOptionService.createOrderItemOption(orderItemOptionDTO);
            }
        }
        List<DiscountDTO> discountList = orderItemDiscountService.getDiscountListByOnlyOrderItem(originItemDTO);
        if(discountList!=null){
            for(DiscountDTO discount:discountList){
                OrderItemDiscountDTO orderItemDiscountDTO = OrderItemDiscountDTO.builder()
                        .discountPrice(discount.getDiscountPrice())
                        .discountDTO(discount)
                        .orderItemDTO(copyItemDTO)
                        .build();
                orderItemDiscountService.createOrderItemDiscount(orderItemDiscountDTO);
            }
        }

        return copyItemDTO;
    }
}