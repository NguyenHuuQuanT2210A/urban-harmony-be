package com.example.orderservice.service;

import com.example.common.dto.OrderDTO;
import com.example.common.dto.ProductDTO;
import com.example.common.enums.OrderSimpleStatus;
import com.example.orderservice.dto.OrderDetailDTO;
import com.example.common.dto.UserDTO;
import com.example.orderservice.dto.request.FeedbackRequest;
import com.example.orderservice.dto.request.OrderRequest;
import com.example.orderservice.dto.request.PaymentRequest;
import com.example.orderservice.dto.request.UserAndProductId;
import com.example.orderservice.dto.response.ApiResponse;
import com.example.orderservice.dto.response.CountOrderByStatus;
import com.example.orderservice.entities.Order;
import com.example.orderservice.entities.OrderDetailId;
import com.example.orderservice.exception.CustomException;
import com.example.orderservice.helper.LocalDatetimeConverter;
import com.example.orderservice.mapper.FeedbackMapper;
import com.example.orderservice.mapper.OrderDetailMapper;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.repositories.FeedbackRepository;
import com.example.orderservice.repositories.OrderRepository;
import com.example.orderservice.repositories.specification.SpecSearchCriteria;
import com.example.orderservice.specification.OrderSpecification;
import com.example.orderservice.specification.SearchBody;
import com.example.orderservice.specification.SearchCriteria;
import com.example.orderservice.specification.SearchCriteriaOperator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.orderservice.repositories.specification.SearchOperation.OR_PREDICATE_FLAG;
import static com.example.orderservice.util.AppConst.SEARCH_SPEC_OPERATOR;
import static com.example.orderservice.util.AppConst.SORT_BY;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;
    private final UserServiceClientImpl userService;
    private final ProductServiceClientImpl productService;
    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final PaymentClient paymentClient;
    private final CartClient cartClient;
    private final CartRedisClient cartRedisClient;
    private final AddressOrderClient addressOrderClient;

    Specification<jakarta.persistence.criteria.Order> specification = Specification.where(null);

    @Override
    public Page<OrderDTO> getAll(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.map(OrderMapper.INSTANCE::toOrderDTO);
    }

    public Page<OrderDTO> findAllAndSorting(SearchBody searchBody){

        if (searchBody.getStatus() != null){
            specification = specification.and(new OrderSpecification(new SearchCriteria("status", SearchCriteriaOperator.EQUALS, searchBody.getStatus())));
        }

        if (searchBody.getStartDate() != null){
            LocalDateTime startDate = LocalDatetimeConverter.toLocalDateTime(searchBody.getStartDate(), true);
            specification = specification.and(new OrderSpecification(new SearchCriteria("createdAt", SearchCriteriaOperator.GREATER_THAN_OR_EQUALS,startDate)));
        }

        if (searchBody.getEndDate() != null){
            LocalDateTime endDate = LocalDatetimeConverter.toLocalDateTime(searchBody.getEndDate(), false);
            specification = specification.and(new OrderSpecification(new SearchCriteria("createdAt", SearchCriteriaOperator.LESS_THAN_OR_EQUALS,endDate)));
        }

        if (searchBody.getProductName() != null){
            specification = specification.and(new OrderSpecification(new SearchCriteria("", SearchCriteriaOperator.PRODUCT_JOIN_PRODUCT_NAME_LIKE, searchBody.getProductName().trim())));
        }

        if (searchBody.getCustomerName() != null){
            specification = specification.and(new OrderSpecification(new SearchCriteria("accountName", SearchCriteriaOperator.USER_JOIN_LIKE, searchBody.getCustomerName().trim())));
        }

        if (searchBody.getCustomerEmail() != null) {
            specification = specification.and(new OrderSpecification(new SearchCriteria("email", SearchCriteriaOperator.USER_JOIN_LIKE, searchBody.getCustomerEmail().trim())));
        }

        if (searchBody.getCustomerPhone() != null) {
            specification = specification.and(new OrderSpecification(new SearchCriteria("phoneNumber", SearchCriteriaOperator.USER_JOIN_LIKE, searchBody.getCustomerPhone().trim())));
        }

        if (searchBody.getOrderId() != null) {
            specification = specification.and(new OrderSpecification(new SearchCriteria("id", SearchCriteriaOperator.EQUALS, searchBody.getOrderId().trim())));
        }

        List<Sort.Order> orders = new ArrayList<>();

        Sort.Order order1;
        order1 = new Sort.Order(Sort.Direction.DESC, "createdAt");
        if (searchBody.getTimeSorting() !=null){
            if (searchBody.getTimeSorting().contains("oldest")){
                order1 = new Sort.Order(Sort.Direction.ASC, "createdAt");
            }
        }
        if (searchBody.getPriceSorting() !=null){
            Sort.Order order2;
            if (searchBody.getPriceSorting().contains("descending")){
               order2 = new Sort.Order(Sort.Direction.DESC, "totalPrice");
            }else {
               order2 = new Sort.Order(Sort.Direction.ASC, "totalPrice");
            }
            orders.add(order2);
        }

        orders.add(order1);
        Pageable sortedPage = PageRequest.of(searchBody.getPage()-1, searchBody.getLimit(), Sort.by(orders));
        Page<OrderDTO> ordersPage;
        try {
           ordersPage = orderRepository.findAll(specification, sortedPage).map(orderMapper.INSTANCE::toOrderDTO);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Error while fetching orders", HttpStatus.BAD_REQUEST);
        }

        return ordersPage;
    }

    public OrderDTO findById(String id){
        var order = findOrderById(id);
        var orderResponse = orderMapper.toOrderDTO(order);
        orderResponse.getOrderDetails().forEach(orderDetailResponse -> {
            var data = productService.getProductById(orderDetailResponse.getId().getProductId());
            orderDetailResponse.setProduct(data.getData());
        });
        var address = addressOrderClient.getAddressOrderById(order.getAddressOrderId()).getData();
        orderResponse.setUsername(address.getUsername());
        orderResponse.setAddressRegion(address.getAddressRegion());
        orderResponse.setAddressDetail(address.getAddressDetail());
        orderResponse.setPhone(address.getPhone());
        orderResponse.setEmail(userService.getUserById(order.getUserId()).getData().getEmail());

        return orderResponse;
    }

    public String createOrder(OrderRequest request){
        try {
            Order newOrder;
            String paymentResponse;

            ApiResponse<UserDTO> user = userService.getUserById(request.getUserId());
            if (user.getData() == null) {
                throw new CustomException("User not found", HttpStatus.BAD_REQUEST);
            } else if (addressOrderClient.getAddressOrderById(request.getAddressOrderId()) == null) {
                throw new CustomException("Address not found", HttpStatus.BAD_REQUEST);
            }
            try {
                newOrder = orderMapper.orderDTOToOrder(request);
                newOrder.setTotalPrice(request.getTotalPrice());
                newOrder.setStatus(OrderSimpleStatus.CREATED);
                newOrder.setPaymentMethod(request.getPaymentMethod().toUpperCase());

                // Save the order first to get the order ID
                newOrder = orderRepository.save(newOrder);

                Set<OrderDetailDTO> orderDetails = new HashSet<>();
                List<UserAndProductId> ids = new ArrayList<>();

                Order finalNewOrder = newOrder;
                request.getCartItems().forEach(cartItem -> {
                    orderDetails.add(OrderDetailDTO.builder()
                            .order(finalNewOrder)
                            .id(new OrderDetailId(finalNewOrder.getId(), cartItem.getProductId()))
                            .quantity(cartItem.getQuantity())
                            .unitPrice(cartItem.getUnitPrice())
                            .build());

                    ids.add(new UserAndProductId(cartItem.getUserId(), cartItem.getProductId()));
                });

                // Convert OrderDetailDTO to OrderDetail and set them to newOrder
                newOrder.setOrderDetails(orderDetails.stream()
                        .map(orderDetailService::createOrderDetail)
                        .map(orderDetailMapper::orderDetailDTOToOrderDetail)
                        .collect(Collectors.toSet()));
                ;
                // Save the order again with the new order details and total price
                newOrder = orderRepository.save(newOrder);
                cartClient.deleteByIds(ids);
//                cartRedisClient.deleteByIds(ids);
            } catch (Exception e) {
                e.printStackTrace();
                throw new CustomException("Error while creating order", HttpStatus.BAD_REQUEST);
            }

            paymentResponse = paymentClient.creatPayment(new PaymentRequest(newOrder.getId(), request.getPaymentMethod()));

            return paymentResponse;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Error while creating order", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public Object updateOrder(OrderRequest request) {
        OrderDTO existingOrder = orderMapper.toOrderDTO(findOrderById(request.getId()));
        if (existingOrder == null) {
            return "Order not found";
        }
        Order updatedOrder = orderMapper.orderDTOToOrder(request);
        return orderMapper.toOrderDTO(orderRepository.save(updatedOrder));
    }

    @Transactional
    public ResponseEntity<?> deleteOrder(String id) {
        orderRepository.deleteById(id);
        List<OrderDetailDTO> orderDetailDTOs = orderDetailService.findOrderDetailByOrderId(id);
        for (OrderDetailDTO orderDetailDTO : orderDetailDTOs) {
            orderDetailService.deleteOrderDetail(orderDetailDTO.getId());
        }
        return ResponseEntity.ok("Order deleted successfully");
    }

    public Page<OrderDTO> findByUserId(Long userId, SearchBody searchBody) {

        if (searchBody.getStatus() != null){
            specification = specification.and(new OrderSpecification(new SearchCriteria("status", SearchCriteriaOperator.EQUALS, searchBody.getStatus())));
        }

        if (searchBody.getStartDate() != null){
            LocalDateTime startDate = LocalDatetimeConverter.toLocalDateTime(searchBody.getStartDate(), true);
            specification = specification.and(new OrderSpecification(new SearchCriteria("createdAt", SearchCriteriaOperator.GREATER_THAN_OR_EQUALS,startDate)));
        }

        if (searchBody.getEndDate() != null){
            LocalDateTime endDate = LocalDatetimeConverter.toLocalDateTime(searchBody.getEndDate(), false);
            specification = specification.and(new OrderSpecification(new SearchCriteria("createdAt", SearchCriteriaOperator.LESS_THAN_OR_EQUALS,endDate)));
        }

        if (searchBody.getProductName() != null){
            specification = specification.and(new OrderSpecification(new SearchCriteria("", SearchCriteriaOperator.PRODUCT_JOIN_PRODUCT_NAME_LIKE, searchBody.getProductName().trim())));
        }

        if (searchBody.getCustomerName() != null){
            specification = specification.and(new OrderSpecification(new SearchCriteria("accountName", SearchCriteriaOperator.USER_JOIN_LIKE, searchBody.getCustomerName().trim())));
        }

        if (searchBody.getCustomerEmail() != null) {
            specification = specification.and(new OrderSpecification(new SearchCriteria("email", SearchCriteriaOperator.USER_JOIN_LIKE, searchBody.getCustomerEmail().trim())));
        }

        if (searchBody.getCustomerPhone() != null) {
            specification = specification.and(new OrderSpecification(new SearchCriteria("phoneNumber", SearchCriteriaOperator.USER_JOIN_LIKE, searchBody.getCustomerPhone().trim())));
        }

        if (searchBody.getOrderId() != null) {
            specification = specification.and(new OrderSpecification(new SearchCriteria("id", SearchCriteriaOperator.EQUALS, searchBody.getOrderId().trim())));
        }

        List<Sort.Order> orders = new ArrayList<>();

        Sort.Order order1;
        order1 = new Sort.Order(Sort.Direction.DESC, "createdAt");
        if (searchBody.getTimeSorting() !=null){
            if (searchBody.getTimeSorting().contains("oldest")){
                order1 = new Sort.Order(Sort.Direction.ASC, "createdAt");
            }
        }
        if (searchBody.getPriceSorting() !=null){
            Sort.Order order2;
            if (searchBody.getPriceSorting().contains("descending")){
                order2 = new Sort.Order(Sort.Direction.DESC, "totalPrice");
            }else {
                order2 = new Sort.Order(Sort.Direction.ASC, "totalPrice");
            }
            orders.add(order2);
        }

        orders.add(order1);
        Pageable sortedPage = PageRequest.of(searchBody.getPage()-1, searchBody.getLimit(), Sort.by(orders));
        Page<OrderDTO> ordersPage;
        try {

            ordersPage = orderRepository.findOrderByUserId(userId, specification, sortedPage).map(orderMapper.INSTANCE::toOrderDTO);
            ordersPage.getContent().forEach(order -> {
                order.getOrderDetails().forEach(orderDetailResponse -> {
                    var data = productService.getProductById(orderDetailResponse.getId().getProductId());
                    orderDetailResponse.setProduct(data.getData());
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("Error while fetching orders", HttpStatus.BAD_REQUEST);
        }

        return ordersPage;
    }

    @Override
    public List<OrderDTO> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId).stream().map(orderMapper::toOrderDTO).collect(Collectors.toList());
    }

    public OrderDTO findCartByUserId(Long id) {
        return null;
//        return orderMapper.INSTANCE.toOrderDTO(orderRepository.findOrderByUserIdAndStatus(id, OrderSimpleStatus.CREATED));
    }

    @Override
    @Transactional
    public OrderDTO changeStatus(String id, OrderSimpleStatus status) {
        var order = findOrderById(id);

        if (order.getStatus() == OrderSimpleStatus.COMPLETE && status == OrderSimpleStatus.CANCEL) {
            throw new CustomException("Cannot change status from COMPLETE to CANCEL", HttpStatus.BAD_REQUEST);
        }
        if (order.getStatus().equals(OrderSimpleStatus.CANCEL)){
            throw new CustomException("Cannot change status " + order.getStatus(), HttpStatus.BAD_REQUEST);
        }

        if (status == OrderSimpleStatus.CANCEL &&
                (order.getStatus() == OrderSimpleStatus.CREATED ||
                        order.getStatus() == OrderSimpleStatus.PENDING ||
                        order.getStatus() == OrderSimpleStatus.PROCESSING)) {
            order.setStatus(status);
        } else if (order.getStatus().ordinal() + 1 != status.ordinal()) {
            throw new CustomException("Cannot change status from " + order.getStatus() + " to " + status, HttpStatus.BAD_REQUEST);
        } else {
            order.setStatus(status);
        }

        if (status == OrderSimpleStatus.COMPLETE){
            for (var orderDetail : order.getOrderDetails()){
                feedbackRepository.save(feedbackMapper.toFeedback(FeedbackRequest.builder()
                        .orderDetail(orderDetail)
                        .build()));
            }
        }
        orderRepository.save(order);
        return orderMapper.toOrderDTO(order);
    }

    @Override
    public List<ProductDTO> findProductsByOrderId(String orderId) {
        var order = findOrderById(orderId);
        List<ProductDTO> products = new ArrayList<>();
        for (var orderDetail : order.getOrderDetails()){
            var product = productService.getProductById(orderDetail.getId().getProductId());
            products.add(product.getData());
        }
        return products;
    }

    @Override
    public Long countOrders() {
        return orderRepository.count();
    }

    @Override
    public Page<OrderDTO> findOrderByUserIdAndStatus(Long userId, OrderSimpleStatus status, Pageable pageable) {
        return orderRepository.findOrderByUserIdAndStatus(userId, status, pageable).map(orderMapper.INSTANCE::toOrderDTO);
    }

    @Override
    public CountOrderByStatus getCountByStatusOrder() {
        return CountOrderByStatus.builder()
                .created(orderRepository.countOrdersByStatus(OrderSimpleStatus.CREATED))
                .pending(orderRepository.countOrdersByStatus(OrderSimpleStatus.PENDING))
                .processing(orderRepository.countOrdersByStatus(OrderSimpleStatus.PROCESSING))
                .onDelivery(orderRepository.countOrdersByStatus(OrderSimpleStatus.ONDELIVERY))
                .delivered(orderRepository.countOrdersByStatus(OrderSimpleStatus.DELIVERED))
                .complete(orderRepository.countOrdersByStatus(OrderSimpleStatus.COMPLETE))
                .cancel(orderRepository.countOrdersByStatus(OrderSimpleStatus.CANCEL))
                .build();
    }

    private Order findOrderById(String id) {
        return orderRepository.findById(id).orElseThrow(() -> new CustomException("Order not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public Page<OrderDTO> searchBySpecification(Pageable pageable, String sort, String[] order) {
        Pageable pageableSorted = pageable;
        if (StringUtils.hasText(sort)){
            Pattern patternSort = Pattern.compile(SORT_BY);
            Matcher matcher = patternSort.matcher(sort);
            if (matcher.find()) {
                String columnName = matcher.group(1);
                pageableSorted = matcher.group(3).equalsIgnoreCase("desc")
                        ? PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(columnName).descending())
                        : PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(columnName).ascending());
            }
        }

        List<SpecSearchCriteria> params = new ArrayList<>();
        Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
        if (order != null) {
            params.addAll(parseOrderCriteria(order, pattern));
        }

        if (params.isEmpty()) {
            return orderRepository.findAll(pageableSorted).map(orderMapper.INSTANCE::toOrderDTO);
        }

        Specification<Order> result = new com.example.orderservice.repositories.specification.OrderSpecification(params.get(0));
        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).getOrPredicate()
                    ? Specification.where(result).or(new com.example.orderservice.repositories.specification.OrderSpecification(params.get(i)))
                    : Specification.where(result).and(new com.example.orderservice.repositories.specification.OrderSpecification(params.get(i)));
        }

        Page<Order> orders = orderRepository.findAll(Objects.requireNonNull(result), pageableSorted);
        return orders.map(orderMapper.INSTANCE::toOrderDTO);
    }

    private List<SpecSearchCriteria> parseOrderCriteria(String[] order, Pattern pattern) {
        List<SpecSearchCriteria> params = new ArrayList<>();
        for (String o : order) {
            Matcher matcher = pattern.matcher(o);
            if (matcher.find()) {
                SpecSearchCriteria searchCriteria = new SpecSearchCriteria(null, matcher.group(2), matcher.group(4), matcher.group(6), matcher.group(1), matcher.group(3), matcher.group(5));
                if (o.startsWith(OR_PREDICATE_FLAG)) {
                    searchCriteria.setOrPredicate(true);
                }
                params.add(searchCriteria);
            }
        }
        return params;
    }

    private List<SpecSearchCriteria> parseAddressOrderCriteria(String addressOrder, Pattern pattern) {
        List<SpecSearchCriteria> params = new ArrayList<>();
        Matcher matcher = pattern.matcher(addressOrder);
        if (matcher.find()) {
            SpecSearchCriteria searchCriteria = new SpecSearchCriteria(null, matcher.group(2), matcher.group(4), matcher.group(6), matcher.group(1), matcher.group(3), matcher.group(5));
            if (addressOrder.startsWith(OR_PREDICATE_FLAG)){
                searchCriteria.setOrPredicate(true);
            }
            params.add(searchCriteria);
        }
        return params;
    }


}
