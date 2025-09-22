package com.project.pos.revenue.controller;

import com.project.pos.discount.dto.DiscountDTO;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.option.dto.OptionDTO;
import com.project.pos.revenue.dto.*;
import com.project.pos.revenue.dto.queryDTO.*;
import com.project.pos.revenue.dto.requestDTO.SaveNewCashReceiptDTO;
import com.project.pos.revenue.dto.responseDTO.*;
import com.project.pos.revenue.service.*;
import com.project.pos.store.dto.PosDTO;
import com.project.pos.store.dto.StoreDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class RevenueApiController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDiscountService orderDiscountService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderItemOptionService orderItemOptionService;
    @Autowired
    private OrderItemDiscountService orderItemDiscountService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private ReceiptMethodService receiptMethodService;
    @Autowired
    private ReceiptService receiptService;

    @GetMapping("/api/getReceiptPage")
    public Page<ReceiptPageDTO> getReceiptPage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                               @RequestParam(value = "searchText", required = false) String searchText, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PosDTO posDTO=(PosDTO) session.getAttribute("currentPos");
        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).searchText(searchText).storeDTO(storeDTO).posDTO(posDTO)
                .build();
        return orderService.getReceiptPage(requestDTO);
    }
    @GetMapping("/api/getOrderListPage")
    public RevenueOrderDTO getOrderListPage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                            @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,
                                            @RequestParam(value = "posNumber") Long posNumber, @RequestParam(value = "receiptNumber") String receiptNumber,
                                            @RequestParam(value = "searchText", required = false) String searchText, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");

        LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
        LocalDate tempEnd = LocalDate.parse(endDate);
        LocalDateTime end = tempEnd.atTime(LocalTime.MAX);

        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).startDate(start).endDate(end).posNumber(posNumber)
                .receiptNumber(receiptNumber).searchText(searchText).storeDTO(storeDTO)
                .build();
        Page<RevenueOrderPageDTO> pageDTO = orderService.getRevenueOrderPage(requestDTO);
        RevenueOrderSummaryDTO summaryDTO = orderService.getRevenueOrderSummary(requestDTO);
        return new RevenueOrderDTO(pageDTO, summaryDTO);
    }
    @GetMapping("/api/getDateListPage")
    public RevenueDateDTO getDateListPage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                            @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,
                                            @RequestParam(value = "posNumber") Long posNumber, @RequestParam(value = "type") Integer type,
                                            HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");

        LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
        LocalDate tempEnd = LocalDate.parse(endDate);
        LocalDateTime end = tempEnd.atTime(LocalTime.MAX);

        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).startDate(start).endDate(end).posNumber(posNumber)
                .type(type).storeDTO(storeDTO)
                .build();
        Page<RevenueDatePageDTO> pageDTO = orderService.getRevenueDatePage(requestDTO);
        RevenueDateSummaryDTO summaryDTO = orderService.getRevenueDateSummary(requestDTO);
        return new RevenueDateDTO(pageDTO, summaryDTO);
    }
    @GetMapping("/api/getMenuListPage")
    public RevenueMenuDTO getMenuListPage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                            @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,
                                            @RequestParam(value = "searchText", required = false) String searchText, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");

        LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
        LocalDate tempEnd = LocalDate.parse(endDate);
        LocalDateTime end = tempEnd.atTime(LocalTime.MAX);

        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).startDate(start).endDate(end).searchText(searchText).storeDTO(storeDTO)
                .build();
        Page<RevenueMenuPageDTO> pageDTO = orderService.getRevenueMenuPage(requestDTO);
        RevenueMenuSummaryDTO summaryDTO = orderService.getRevenueMenuSummary(requestDTO);
        return new RevenueMenuDTO(pageDTO, summaryDTO);
    }
    @GetMapping("/api/getMenuChartPage")
    public RevenueMenuChartDTO getMenuChartPage(@RequestParam(value = "startDate") String startDate,
                                           @RequestParam(value = "endDate") String endDate, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");

        LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
        LocalDate tempEnd = LocalDate.parse(endDate);
        LocalDateTime end = tempEnd.atTime(LocalTime.MAX);
        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .startDate(start).endDate(end).storeDTO(storeDTO)
                .build();
        
        RevenueMenuSummaryDTO summaryDTO = orderService.getRevenueMenuSummary(requestDTO);
        if(summaryDTO.getNumber()==0) return new RevenueMenuChartDTO(null, null);
        Long summaryTotalPayment = summaryDTO.getTotalPayment();
        Long summaryCost = summaryDTO.getCost1()+summaryDTO.getCost2();
        Long summaryVat = summaryTotalPayment/11;
        Integer summaryNet = Math.round(summaryTotalPayment-summaryVat);
        Long summaryProfit = summaryNet-summaryCost;

        List<RevenueChartDTO> makeChartDTO = new ArrayList<>();
        List<RevenueMenuPageDTO> listDTO = orderService.getRevenueMenuChartList(requestDTO);
        for(RevenueMenuPageDTO pageDTO:listDTO){
            Long totalPayment = pageDTO.getTotalPayment();
            Long cost = pageDTO.getCost1()+pageDTO.getCost2();
            Long vat = totalPayment/11;
            Integer net = Math.round(totalPayment-vat);
            Long profit = net-cost;
            Double percent = (double)profit/net*100.0;

            RevenueChartDTO chartDTO = RevenueChartDTO.builder()
                    .name(pageDTO.getName())
                    .net(net)
                    .profit(profit)
                    .percent(percent)
                    .totalPercent((double)profit/summaryProfit*100.0)
                    .build();
            makeChartDTO.add(chartDTO);
        }
        makeChartDTO.sort(Comparator.comparing(RevenueChartDTO::getProfit).reversed());

        List<RevenueChartDTO> result = new ArrayList<>();
        List<RevenueChartDTO> otherList = new ArrayList<>();
        Integer otherNet = 0;
        Long otherProfit = 0L;

        for (int i=0; i<makeChartDTO.size(); i++) {
            if (i<6) result.add(makeChartDTO.get(i));
            else {
                otherList.add(makeChartDTO.get(i));
                otherNet += makeChartDTO.get(i).getNet();
                otherProfit += makeChartDTO.get(i).getProfit();
            }
        }
        RevenueChartDTO chartDTO = RevenueChartDTO.builder()
                .name("기타")
                .net(otherNet)
                .profit(otherProfit)
                .percent((otherNet==0)? 0d:(double)otherProfit/otherNet*100.0)
                .totalPercent((double)otherProfit/summaryProfit*100.0)
                .build();
        result.add(chartDTO);

        return new RevenueMenuChartDTO(result, otherList);
    }
    @GetMapping("/api/getReceipt")
    public OrderPageDTO getReceipt(@RequestParam(value = "id") Long id) {
        OrdersDTO ordersDTO = orderService.getOrdersDTOById(id);
        if(ordersDTO==null) return null;
        PaymentDTO paymentDTO = paymentService.getPaymentByOrders(ordersDTO);

        List<OrderItemDTO> orderItemList = orderItemService.getOrderItemByOrders(ordersDTO);

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
                OrderRowDTO itemDTO = new OrderRowDTO("item", null, null, orderItemDTO.getName(),
                        formatter.format(orderItemDTO.getProductPerUnit()), orderItemDTO.getProductCount(), null,
                        formatter.format(tempTotal));
                rowList.add(itemDTO);
                totalCount += orderItemDTO.getProductCount();
                totalPayment += tempTotal;
                basicPayment = tempTotal;

                if(itemOptionList != null && !itemOptionList.isEmpty()){
                    itemOptionList.forEach(option -> {
                        OrderRowDTO optionDTO = new OrderRowDTO("option", null, null, option.getName(),
                                null, null, null, null);
                        rowList.add(optionDTO);
                    });
                }
                if(perBasicDiscountList != null && !perBasicDiscountList.isEmpty()){
                    for(DiscountDTO discount : perBasicDiscountList){
                        tempTotal = discount.getDiscountPrice()*orderItemDTO.getProductCount();
                        OrderRowDTO discountDTO = new OrderRowDTO("discount", null, null, discount.getName(),
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
                        OrderRowDTO discountDTO = new OrderRowDTO("discount", null, null, discount.getName(),
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
                        OrderRowDTO discountDTO = new OrderRowDTO("discount", null, null, discount.getName(),
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
                        OrderRowDTO discountDTO = new OrderRowDTO("discount", null, null, discount.getName(),
                                formatter.format(discount.getDiscountPrice())+'%',
                                1L, '-'+formatter.format(tempTotal), null);
                        rowList.add(discountDTO);
                        totalPayment -= tempTotal;
                    }
                }
            }
        }

        List<DiscountDTO> allBasicDiscountList = orderDiscountService.getDiscountListByOrder(ordersDTO, true);
        List<DiscountDTO> allPercentDiscountList = orderDiscountService.getDiscountListByOrder(ordersDTO, false);
        if(allBasicDiscountList != null && !allBasicDiscountList.isEmpty()){
            for(DiscountDTO discount : allBasicDiscountList){
                Long tempTotal = discount.getDiscountPrice();
                OrderRowDTO discountDTO = new OrderRowDTO("allDiscount", null, null, discount.getName(),
                        formatter.format(discount.getDiscountPrice()),
                        1L, '-'+formatter.format(tempTotal), null);
                rowList.add(discountDTO);
                totalPayment -= tempTotal;
            }
        }
        if(allPercentDiscountList != null && !allPercentDiscountList.isEmpty()){
            for(DiscountDTO discount : allPercentDiscountList){
                Long tempTotal = Math.round(totalPayment*(discount.getDiscountPrice()/100.0));
                OrderRowDTO discountDTO = new OrderRowDTO("allDiscount",null, null, discount.getName(),
                        formatter.format(discount.getDiscountPrice())+'%',
                        1L, '-'+formatter.format(tempTotal), null);
                rowList.add(discountDTO);
            }
        }

        Long paymentPrice = paymentMethodService.getPaymentPriceByOrder(ordersDTO);
        if(paymentPrice==null) paymentPrice=0L;

        return new OrderPageDTO(ordersDTO, paymentDTO.getStatus(), paymentPrice, totalCount, rowList);
    }
    @GetMapping("/api/getCashReceipt")
    public IssueReceiptDTO getCashReceipt(@RequestParam(value = "id") Long id) {
        OrdersDTO ordersDTO = orderService.getOrdersDTOById(id);
        if(ordersDTO==null) return null;
        PaymentDTO paymentDTO = paymentService.getPaymentByOrders(ordersDTO);
        if(paymentDTO==null) return null;

        List<ReceiptDTO> issueList = receiptMethodService.getCashReceiptByPayment(paymentDTO);
        List<ReceiptDTO> cancelList = receiptMethodService.getCancelCashReceiptByPayment(paymentDTO);
        return new IssueReceiptDTO(issueList, cancelList);
    }
    @GetMapping("/api/getNonCashReceipt")
    public List<NonIssueReceiptDTO> getNonCashReceipt(@RequestParam(value = "id") Long id) {
        OrdersDTO ordersDTO = orderService.getOrdersDTOById(id);
        if(ordersDTO==null) return null;
        PaymentDTO paymentDTO = paymentService.getPaymentByOrders(ordersDTO);
        if(paymentDTO==null) return null;
        return paymentMethodService.getNonIssuePaymentMethodList(paymentDTO);
    }
    @GetMapping("/api/getIssueNonCashReceipt")
    public List<NonIssueReceiptDTO> getIssueNonCashReceipt(@RequestParam(value = "idList") List<Long> idList) {
        return paymentMethodService.getNonIssuePaymentMethodByIdList(idList);
    }
    @GetMapping("/api/getCardReceipt")
    public IssueReceiptDTO getCardReceipt(@RequestParam(value = "id") Long id) {
        OrdersDTO ordersDTO = orderService.getOrdersDTOById(id);
        if(ordersDTO==null) return null;
        PaymentDTO paymentDTO = paymentService.getPaymentByOrders(ordersDTO);
        if(paymentDTO==null) return null;

        List<ReceiptDTO> issueList = receiptMethodService.getCardReceiptByPayment(paymentDTO);
        List<ReceiptDTO> cancelList = receiptMethodService.getCancelCardReceiptByPayment(paymentDTO);
        return new IssueReceiptDTO(issueList, cancelList);
    }
    @PostMapping("/api/saveNewCashReceipt")
    public ResponseEntity<?> saveNewCashReceipt(@RequestBody SaveNewCashReceiptDTO saveNewCashReceiptDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PosDTO posDTO=(PosDTO) session.getAttribute("currentPos");

        ReceiptDTO receiptDTO = ReceiptDTO.builder()
                .totalAmount(saveNewCashReceiptDTO.getTotalPrice())
                .cashReceiptType(saveNewCashReceiptDTO.getReceiptType())
                .cashReceiptNumber(saveNewCashReceiptDTO.getReceiptNumber()+"")
                .status(true)
                .storeDTO(storeDTO)
                .posDTO(posDTO)
                .build();
        ReceiptDTO savedReceiptDTO = receiptService.createReceipt(receiptDTO);

        Map<String, Long> receiptMap = saveNewCashReceiptDTO.getReceiptMap();
        receiptMap.forEach((key, value) -> {
            Long id = Long.parseLong(key);
            PaymentMethodDTO paymentMethodDTO = paymentMethodService.getPaymentMethodDTOById(id);
            ReceiptMethodDTO receiptMethodDTO = ReceiptMethodDTO.builder()
                    .receiptKey(savedReceiptDTO.getId())
                    .paymentMethodKey(paymentMethodDTO.getId())
                    .issuePrice(value)
                    .status(true)
                    .receiptDTO(savedReceiptDTO)
                    .paymentMethodDTO(paymentMethodDTO)
                    .build();
            receiptMethodService.createReceiptMethod(receiptMethodDTO);
        });

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @DeleteMapping("/api/deleteCashReceipt")
    public ResponseEntity<?> deleteCashReceipt(@RequestParam(value = "idList") List<Long> idList) {
        idList.forEach(id -> {
            ReceiptDTO receiptDTO = receiptService.getReceiptDTOById(id);
            if(receiptDTO.getStatus()){
                receiptService.deleteReceipt(receiptDTO.getId());
                List<ReceiptMethodDTO> methodList = receiptMethodService.getReceiptMethodByReceipt(receiptDTO);
                if(methodList!=null) methodList.forEach(method -> receiptMethodService.deleteReceiptMethod(method.getReceiptKey(), method.getPaymentMethodKey()));
            }
        });
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @DeleteMapping("/api/deleteReceipt")
    public ResponseEntity<?> deleteReceipt(@RequestParam(value = "id") Long id) {
        OrdersDTO ordersDTO = orderService.getOrdersDTOById(id);
        PaymentDTO paymentDTO = paymentService.getPaymentByOrders(ordersDTO);
        paymentService.updatePaymentStatus(paymentDTO.getId(), false);
        List<PaymentMethodDTO> methodList = paymentMethodService.getPaymentMethodListByPayment(paymentDTO);
        if(methodList==null) return null;
        for(PaymentMethodDTO method:methodList){
            paymentMethodService.deletePaymentMethod(method.getId());
            List<ReceiptMethodDTO> receiptMethodList = receiptMethodService.getAllReceiptMethodByPaymentMethod(method);
            if(receiptMethodList!=null){
                for(ReceiptMethodDTO temp:receiptMethodList){
                    receiptMethodService.deleteReceiptMethod(temp.getReceiptKey(), temp.getPaymentMethodKey());
                    receiptService.deleteReceipt(temp.getReceiptKey());
                }
            }
        }
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}