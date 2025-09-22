package com.project.pos.revenue.controller;

import com.project.pos.option.dto.OptionDTO;
import com.project.pos.option.dto.OptionReceiptDTO;
import com.project.pos.option.service.OptionReceiptService;
import com.project.pos.product.dto.ProductDTO;
import com.project.pos.product.dto.ProductReceiptDTO;
import com.project.pos.product.service.ProductReceiptService;
import com.project.pos.revenue.dto.*;
import com.project.pos.revenue.dto.requestDTO.*;
import com.project.pos.revenue.dto.responseDTO.PaymentHistoryDTO;
import com.project.pos.revenue.dto.responseDTO.PaymentReceiptDTO;
import com.project.pos.revenue.service.*;
import com.project.pos.stock.dto.StockMovementDTO;
import com.project.pos.stock.service.StockMovementService;
import com.project.pos.stock.service.StockService;
import com.project.pos.store.dto.MemberDTO;
import com.project.pos.store.dto.PosDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.service.MemberService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
@Log4j2
public class PaymentApiController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PointService pointService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ReceiptService receiptService;
    @Autowired
    private ReceiptMethodService receiptMethodService;
    @Autowired
    private OrderItemOptionService orderItemOptionService;
    @Autowired
    private StockMovementService stockMovementService;
    @Autowired
    private ProductReceiptService productReceiptService;
    @Autowired
    private OptionReceiptService optionReceiptService;

    @GetMapping("/api/getPaymentHistory")
    public PaymentHistoryDTO getPaymentHistory(@RequestParam(value = "waiting") Integer waiting, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PosDTO posDTO=(PosDTO) session.getAttribute("currentPos");
        OrdersDTO ordersDTO = orderService.getOrdersByStorePosWaiting(storeDTO, posDTO, waiting);
        if(ordersDTO==null) return null;

        PaymentDTO paymentDTO = paymentService.getPaymentByOrders(ordersDTO);
        if(paymentDTO==null) return null;

        List<PaymentMethodDTO> methodList = paymentMethodService.getPaymentMethodListByPayment(paymentDTO);
        List<PaymentReceiptDTO> methodReceiptList = new ArrayList<>();
        if(methodList==null) return null;
        methodList.forEach(temp -> {
            List<ReceiptMethodDTO> receiptMethodList = receiptMethodService.getAllReceiptMethodByPaymentMethod(temp);
            if(receiptMethodList==null){
                PaymentReceiptDTO dto = new PaymentReceiptDTO(temp, null);
                methodReceiptList.add(dto);
            }else{
                PaymentReceiptDTO dto = new PaymentReceiptDTO(temp, receiptMethodList.get(0).getReceiptDTO());
                methodReceiptList.add(dto);
            }
        });

        MemberDTO memberDTO = pointService.getCurrentMember(paymentDTO);

        return new PaymentHistoryDTO(memberDTO, methodReceiptList);
    }
    @GetMapping("/api/getTruePaymentHistory")
    public Long getTruePaymentHistory(@RequestParam(value = "waiting") Integer waiting, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PosDTO posDTO=(PosDTO) session.getAttribute("currentPos");
        OrdersDTO ordersDTO = orderService.getOrdersByStorePosWaiting(storeDTO, posDTO, waiting);
        if(ordersDTO==null) return 0L;

        PaymentDTO paymentDTO = paymentService.getPaymentByOrders(ordersDTO);
        if(paymentDTO==null) return 0L;

        return paymentMethodService.getCountProgressPaymentMethodByPayment(paymentDTO);
    }
    @GetMapping("/api/getSeparateListContent")
    public Long getSeparateListContent(HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PosDTO posDTO=(PosDTO) session.getAttribute("currentPos");
        OrdersDTO ordersDTO = orderService.getOrdersByStorePosWaiting(storeDTO, posDTO, 4);
        if(ordersDTO==null) return 0L;

        List<OrderItemDTO> tempList = orderItemService.getOrderItemByOrders(ordersDTO);
        if(tempList==null) return 0L;
        return 1L;
    }
    @PostMapping("/api/saveCashPayment")
    public ResponseEntity<?> saveCashPayment(@Valid @RequestBody SaveCashPaymentDTO saveCashPaymentDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PosDTO posDTO=(PosDTO) session.getAttribute("currentPos");

        OrdersDTO existOrdersDTO = orderService.getOrdersByStorePosWaiting(storeDTO, posDTO, saveCashPaymentDTO.getWaiting());
        if(existOrdersDTO==null) return ResponseEntity.ok(Map.of("message", "Non"));
        PaymentDTO existPaymentDTO = paymentService.getPaymentByOrders(existOrdersDTO);
        if(existPaymentDTO==null) existPaymentDTO = saveNewPaymentDTO(storeDTO, existOrdersDTO);

        PaymentMethodDTO paymentMethodDTO = PaymentMethodDTO.builder()
                .typeValue(0)
                .paymentAmount(saveCashPaymentDTO.getPrice())
                .received(saveCashPaymentDTO.getReceived())
                .changeValue(saveCashPaymentDTO.getChange())
                .status(true)
                .storeDTO(storeDTO)
                .posDTO(posDTO)
                .paymentDTO(existPaymentDTO)
                .build();
        PaymentMethodDTO savedPaymentMethodDTO = paymentMethodService.createPaymentMethod(paymentMethodDTO);
        return returnPaymentResult(storeDTO, savedPaymentMethodDTO);
    }
    @PostMapping("/api/saveCashReceipt")
    public ResponseEntity<?> saveCashReceipt(@Valid @RequestBody SaveCashReceiptDTO saveCashReceiptDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PosDTO posDTO=(PosDTO) session.getAttribute("currentPos");

        OrdersDTO existOrdersDTO = orderService.getOrdersByStorePosWaiting(storeDTO, posDTO, saveCashReceiptDTO.getWaiting());
        if(existOrdersDTO==null) return ResponseEntity.ok(Map.of("message", "Non"));
        PaymentDTO existPaymentDTO = paymentService.getPaymentByOrders(existOrdersDTO);
        if(existPaymentDTO==null) existPaymentDTO = saveNewPaymentDTO(storeDTO, existOrdersDTO);

        PaymentMethodDTO paymentMethodDTO = PaymentMethodDTO.builder()
                .typeValue(0)
                .paymentAmount(saveCashReceiptDTO.getPrice())
                .received(saveCashReceiptDTO.getReceived())
                .changeValue(saveCashReceiptDTO.getChange())
                .status(true)
                .storeDTO(storeDTO)
                .posDTO(posDTO)
                .paymentDTO(existPaymentDTO)
                .build();
        PaymentMethodDTO savedPaymentMethodDTO = paymentMethodService.createPaymentMethod(paymentMethodDTO);

        ReceiptDTO receiptDTO = ReceiptDTO.builder()
                .totalAmount(saveCashReceiptDTO.getReceiptIssuePrice())
                .cashReceiptType(saveCashReceiptDTO.getReceiptType())
                .cashReceiptNumber(saveCashReceiptDTO.getReceiptNumber()+"")
                .status(true)
                .storeDTO(storeDTO)
                .posDTO(posDTO)
                .build();
        ReceiptDTO savedReceiptDTO = receiptService.createReceipt(receiptDTO);

        ReceiptMethodDTO receiptMethodDTO = ReceiptMethodDTO.builder()
                .issuePrice(saveCashReceiptDTO.getReceiptIssuePrice())
                .status(true)
                .receiptDTO(savedReceiptDTO)
                .paymentMethodDTO(savedPaymentMethodDTO)
                .build();
        receiptMethodService.createReceiptMethod(receiptMethodDTO);

        return returnPaymentResult(storeDTO, savedPaymentMethodDTO);
    }
    @PostMapping("/api/saveCardPayment")
    public ResponseEntity<?> saveCardPayment(@Valid @RequestBody SaveCardPaymentDTO saveCardPaymentDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PosDTO posDTO=(PosDTO) session.getAttribute("currentPos");

        OrdersDTO existOrdersDTO = orderService.getOrdersByStorePosWaiting(storeDTO, posDTO, saveCardPaymentDTO.getWaiting());
        if(existOrdersDTO==null) return ResponseEntity.ok(Map.of("message", "Non"));
        PaymentDTO existPaymentDTO = paymentService.getPaymentByOrders(existOrdersDTO);
        if(existPaymentDTO==null) existPaymentDTO = saveNewPaymentDTO(storeDTO, existOrdersDTO);

        PaymentMethodDTO paymentMethodDTO = PaymentMethodDTO.builder()
                .typeValue(1)
                .paymentAmount(saveCardPaymentDTO.getReceived())
                .status(true)
                .storeDTO(storeDTO)
                .posDTO(posDTO)
                .paymentDTO(existPaymentDTO)
                .build();
        PaymentMethodDTO savedPaymentMethodDTO = paymentMethodService.createPaymentMethod(paymentMethodDTO);

        ReceiptDTO receiptDTO = ReceiptDTO.builder()
                .totalAmount(saveCardPaymentDTO.getReceived())
                .cardCompany(saveCardPaymentDTO.getCardCompany())
                .cardNumber(saveCardPaymentDTO.getCardNumber())
                .cardMonth(saveCardPaymentDTO.getCardMonth())
                .status(true)
                .storeDTO(storeDTO)
                .posDTO(posDTO)
                .build();
        ReceiptDTO savedReceiptDTO = receiptService.createReceipt(receiptDTO);

        ReceiptMethodDTO receiptMethodDTO = ReceiptMethodDTO.builder()
                .issuePrice(saveCardPaymentDTO.getReceived())
                .status(true)
                .receiptDTO(savedReceiptDTO)
                .paymentMethodDTO(savedPaymentMethodDTO)
                .build();
        receiptMethodService.createReceiptMethod(receiptMethodDTO);

        return returnPaymentResult(storeDTO, savedPaymentMethodDTO);
    }
    @PostMapping("/api/savePointPayment")
    public ResponseEntity<?> savePointPayment(@Valid @RequestBody SavePointPaymentDTO savePointPaymentDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PosDTO posDTO=(PosDTO) session.getAttribute("currentPos");

        OrdersDTO existOrdersDTO = orderService.getOrdersByStorePosWaiting(storeDTO, posDTO, savePointPaymentDTO.getWaiting());
        if(existOrdersDTO==null) return ResponseEntity.ok(Map.of("message", "Non"));
        PaymentDTO existPaymentDTO = paymentService.getPaymentByOrders(existOrdersDTO);
        if(existPaymentDTO==null) existPaymentDTO = saveNewPaymentDTO(storeDTO, existOrdersDTO);

        PaymentMethodDTO paymentMethodDTO = PaymentMethodDTO.builder()
                .typeValue(2)
                .paymentAmount(savePointPaymentDTO.getReceived())
                .status(true)
                .storeDTO(storeDTO)
                .posDTO(posDTO)
                .paymentDTO(existPaymentDTO)
                .build();
        PaymentMethodDTO savedPaymentMethodDTO = paymentMethodService.createPaymentMethod(paymentMethodDTO);

        MemberDTO memberDTO = memberService.getMemberDTOByPhoneNumber(storeDTO, savePointPaymentDTO.getPhoneNumber());
        PointDTO pointDTO = PointDTO.builder()
                .changingPoint(savePointPaymentDTO.getReceived())
                .remainingPoint(memberDTO.getPoints()-savePointPaymentDTO.getReceived())
                .typeValue(false)
                .storeDTO(storeDTO)
                .memberDTO(memberDTO)
                .paymentMethodDTO(savedPaymentMethodDTO)
                .build();
        pointService.createPoint(pointDTO);
        memberService.updateMemberPoint(memberDTO.getId(), savePointPaymentDTO.getReceived(), false);

        return returnPaymentResult(storeDTO, savedPaymentMethodDTO);
    }
    @PostMapping("/api/saveMemberPoint")
    public ResponseEntity<?> saveMemberPoint(@Valid @RequestBody SaveMemberPointDTO saveMemberPointDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PaymentMethodDTO paymentMethodDTO = paymentMethodService.getPaymentMethodDTOById(saveMemberPointDTO.getId());
        MemberDTO memberDTO = memberService.getMemberDTOByPhoneNumber(storeDTO, saveMemberPointDTO.getPhoneNumber());

        if(memberDTO==null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "해당 회원이 존재하지 않습니다."));

        savePoint(storeDTO, memberDTO, paymentMethodDTO.getPaymentDTO());
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @DeleteMapping("/api/deletePaymentMethod")
    public ResponseEntity<?> deletePaymentMethod(@RequestParam(value = "id") Long id) {
        PaymentMethodDTO paymentMethodDTO = paymentMethodService.getPaymentMethodDTOById(id);

        List<ReceiptMethodDTO> receiptMethodList = receiptMethodService.getTrueReceiptMethodByPaymentMethod(paymentMethodDTO);
        if(receiptMethodList!=null) {
            receiptMethodList.forEach(receiptMethod -> {
                receiptMethodService.deleteReceiptMethod(receiptMethod.getReceiptKey(), receiptMethod.getPaymentMethodKey());
                receiptService.deleteReceipt(receiptMethod.getReceiptKey());
            });
        }

        PointDTO pointDTO = pointService.getPointByPaymentMethod(paymentMethodDTO);
        if(pointDTO!=null && !pointDTO.getTypeValue()){
            PointDTO newPointDTO = PointDTO.builder()
                    .changingPoint(pointDTO.getChangingPoint())
                    .remainingPoint(pointDTO.getRemainingPoint())
                    .typeValue(true)
                    .memberDTO(pointDTO.getMemberDTO())
                    .paymentMethodDTO(pointDTO.getPaymentMethodDTO())
                    .paymentDTO(pointDTO.getPaymentDTO())
                    .storeDTO(pointDTO.getStoreDTO())
                    .build();
            PointDTO savedPointDTO = pointService.createPoint(newPointDTO);
            memberService.updateMemberPoint(pointDTO.getMemberDTO().getId(), savedPointDTO.getChangingPoint(), true);
        }
        paymentMethodService.deletePaymentMethod(id);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }

    private ResponseEntity<?> returnPaymentResult(StoreDTO storeDTO, PaymentMethodDTO methodDTO){
        PaymentDTO paymentDTO = methodDTO.getPaymentDTO();
        OrdersDTO ordersDTO = paymentDTO.getOrdersDTO();
        Long totalPayment = paymentMethodService.getPaymentPriceByOrder(ordersDTO);
        if(ordersDTO.getTotalPaymentAmount()<=totalPayment){
            paymentService.updatePaymentStatus(paymentDTO.getId(), true);
            orderService.updateOrdersWaiting(ordersDTO.getId(), null);

            List<OrderItemDTO> itemList = orderItemService.getOrderItemByOrders(ordersDTO);
            if(itemList!=null) for(OrderItemDTO orderItemDTO:itemList) saveStockChanging(orderItemDTO, storeDTO);

            MemberDTO memberDTO = pointService.getCurrentMember(paymentDTO);
            if(memberDTO==null) {
                return ResponseEntity.ok(Map.of("paymentMethodId", methodDTO.getId(),"message", "Point"));
            }
            else{
                savePoint(storeDTO, memberDTO, methodDTO.getPaymentDTO());
                return ResponseEntity.ok(Map.of("message", "Finish"));
            }
        }
        return ResponseEntity.ok(Map.of("message", "Ongoing"));
    }
    private PaymentDTO saveNewPaymentDTO(StoreDTO storeDTO, OrdersDTO ordersDTO){
        PaymentDTO paymentDTO = PaymentDTO.builder()
                .totalAmount(ordersDTO.getTotalPaymentAmount())
                .status(null)
                .storeDTO(storeDTO)
                .ordersDTO(ordersDTO)
                .build();
        return paymentService.createPayment(paymentDTO);
    }
    private void savePoint(StoreDTO storeDTO, MemberDTO memberDTO, PaymentDTO paymentDTO){
        Long pointPercent = storeDTO.getPointPercent();
        Long totalPayment = paymentDTO.getTotalAmount();
        Long changingPoint = Math.round(totalPayment*pointPercent/100.0);
        PointDTO pointDTO = PointDTO.builder()
                .changingPoint(changingPoint)
                .remainingPoint(memberDTO.getPoints()+changingPoint)
                .typeValue(true)
                .storeDTO(storeDTO)
                .memberDTO(memberDTO)
                .paymentDTO(paymentDTO)
                .build();
        pointService.createPoint(pointDTO);
        memberService.updateMemberOrderFinish(memberDTO.getId(), totalPayment, changingPoint);
    }
    private void saveStockChanging(OrderItemDTO orderItemDTO, StoreDTO storeDTO){
        Long count = orderItemDTO.getProductCount();
        ProductDTO productDTO = orderItemDTO.getProductDTO();
        List<ProductReceiptDTO> productReceiptList = productReceiptService.getProductReceiptByProduct(productDTO);
        List<OptionDTO> optionList = orderItemOptionService.getOptionListByOrderItem(orderItemDTO);

        if(productReceiptList!=null){
            for(ProductReceiptDTO dto:productReceiptList) {
                StockMovementDTO movementDTO = StockMovementDTO.builder()
                        .movementAmount(dto.getQuantity()*count)
                        .presentAmount(dto.getStockDTO().getQuantity()-(dto.getQuantity()*count))
                        .stockCost(dto.getStockDTO().getStockCost()*count)
                        .typeValue(4)
                        .plusType(false)
                        .notes(productDTO.getName()+" "+count+"잔 판매")
                        .storeDTO(storeDTO)
                        .stockDTO(dto.getStockDTO())
                        .build();
                stockMovementService.createStockMovement(movementDTO);
            }
        }
        if(optionList!=null){
            for(OptionDTO optionDTO:optionList){
                List<OptionReceiptDTO> optionReceiptList = optionReceiptService.getOptionReceiptByOption(optionDTO);
                if(optionReceiptList!=null) {
                    for(OptionReceiptDTO dto:optionReceiptList){
                        StockMovementDTO movementDTO = StockMovementDTO.builder()
                                .movementAmount(dto.getQuantity()*count)
                                .presentAmount(dto.getStockDTO().getQuantity()-(dto.getQuantity()*count))
                                .stockCost(dto.getStockDTO().getStockCost()*count)
                                .typeValue(4)
                                .plusType(false)
                                .notes(productDTO.getName()+" "+count+"잔 판매(옵션)")
                                .storeDTO(storeDTO)
                                .stockDTO(dto.getStockDTO())
                                .build();
                        stockMovementService.createStockMovement(movementDTO);
                    }
                }
            }
        }
    }
}
