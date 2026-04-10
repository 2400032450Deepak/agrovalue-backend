package com.agrovalue.backend.service.impl;

import com.agrovalue.backend.dto.*;
import com.agrovalue.backend.entity.*;
import com.agrovalue.backend.exception.ResourceNotFoundException;
import com.agrovalue.backend.repository.*;
import com.agrovalue.backend.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminUserRepository adminUserRepository;
    private final AdminProductRepository adminProductRepository;
    private final AdminOrderRepository adminOrderRepository;
    private final UserAccountStatusRepository userAccountStatusRepository;
    private final ProductModerationStatusRepository productModerationStatusRepository;
    private final ReportRepository reportRepository;

    public AdminServiceImpl(
            AdminUserRepository adminUserRepository,
            AdminProductRepository adminProductRepository,
            AdminOrderRepository adminOrderRepository,
            UserAccountStatusRepository userAccountStatusRepository,
            ProductModerationStatusRepository productModerationStatusRepository,
            ReportRepository reportRepository
    ) {
        this.adminUserRepository = adminUserRepository;
        this.adminProductRepository = adminProductRepository;
        this.adminOrderRepository = adminOrderRepository;
        this.userAccountStatusRepository = userAccountStatusRepository;
        this.productModerationStatusRepository = productModerationStatusRepository;
        this.reportRepository = reportRepository;
    }

    @Override
    public AdminDashboardResponse getDashboard() {
        long totalUsers = adminUserRepository.count();
        long totalFarmers = adminUserRepository.countFarmers();
        long totalBuyers = adminUserRepository.countBuyers();
        long totalProducts = adminProductRepository.count();
        long activeProducts = productModerationStatusRepository.countActiveProducts();

        AdminDashboardResponse response = new AdminDashboardResponse();
        response.setTotalUsers(totalUsers);
        response.setTotalFarmers(totalFarmers);
        response.setTotalBuyers(totalBuyers);
        response.setTotalProducts(totalProducts);
        response.setActiveProducts(activeProducts);
        response.setPendingProducts(Math.max(totalProducts - activeProducts, 0));
        response.setTotalRevenue(adminOrderRepository.totalRevenue());
        response.setTotalOrders(adminOrderRepository.count());
        response.setActiveSessions(0); // mock
        response.setIssuesCount(reportRepository.countOpenIssues());
        return response;
    }

    @Override
    public List<AdminUserResponse> getAllUsers() {
        List<User> users = adminUserRepository.findAllWithRoles();
        Map<Long, UserAccountStatus> statusByUserId = userAccountStatusRepository.findAll().stream()
                .collect(Collectors.toMap(UserAccountStatus::getUserId, Function.identity()));

        return users.stream().map(user -> toAdminUserResponse(user, statusByUserId.get(user.getId()))).toList();
    }

    @Override
    public AdminUserResponse getUserById(Long id) {
        User user = adminUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        UserAccountStatus status = userAccountStatusRepository.findById(id).orElse(null);
        return toAdminUserResponse(user, status);
    }

    @Override
    @Transactional
    public AdminUserResponse updateUserStatus(Long id, String status) {
        User user = adminUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        UserAccountStatus accountStatus = userAccountStatusRepository.findById(id)
                .orElse(new UserAccountStatus(id, normalizeStatus(status), LocalDateTime.now()));

        accountStatus.setStatus(normalizeStatus(status));
        accountStatus.setUpdatedAt(LocalDateTime.now());
        userAccountStatusRepository.save(accountStatus);

        return toAdminUserResponse(user, accountStatus);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = adminUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        adminUserRepository.delete(user);
        userAccountStatusRepository.deleteById(id);
    }

    @Override
    public List<AdminProductResponse> getAllProducts() {
        List<Product> products = adminProductRepository.findAllWithFarmer();
        Map<Long, ProductModerationStatus> statusByProductId = productModerationStatusRepository.findAll().stream()
                .collect(Collectors.toMap(ProductModerationStatus::getProductId, Function.identity()));

        return products.stream().map(product -> toAdminProductResponse(product, statusByProductId.get(product.getId()))).toList();
    }

    @Override
    @Transactional
    public AdminProductResponse updateProductStatus(Long id, String status) {
        Product product = adminProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        ProductModerationStatus moderationStatus = productModerationStatusRepository.findById(id)
                .orElse(new ProductModerationStatus(id, normalizeStatus(status), LocalDateTime.now()));

        moderationStatus.setStatus(normalizeStatus(status));
        moderationStatus.setUpdatedAt(LocalDateTime.now());
        productModerationStatusRepository.save(moderationStatus);

        return toAdminProductResponse(product, moderationStatus);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = adminProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        adminProductRepository.delete(product);
        productModerationStatusRepository.deleteById(id);
    }

    @Override
    public List<AdminTransactionResponse> getAllTransactions() {
        return adminOrderRepository.findAllByOrderByOrderedAtDesc().stream().map(order -> {
            AdminTransactionResponse response = new AdminTransactionResponse();
            response.setOrderId(order.getId());
            response.setUserId(order.getUser().getId());
            response.setUserEmail(order.getUser().getEmail());
            response.setOrderStatus(order.getStatus());
            response.setTotalAmount(order.getTotalAmount());
            response.setOrderedAt(order.getOrderedAt());
            return response;
        }).toList();
    }

    @Override
    public List<AdminReportResponse> getAllReports() {
        return reportRepository.findAll().stream()
                .map(this::toAdminReportResponse)
                .toList();
    }

    @Override
    @Transactional
    public AdminReportResponse resolveReport(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
        report.setStatus("RESOLVED");
        report.setResolvedAt(LocalDateTime.now());
        reportRepository.save(report);
        return toAdminReportResponse(report);
    }

    @Override
    public List<FarmerAnalyticsResponse> getFarmerAnalytics() {
        return adminOrderRepository.farmerAnalytics().stream().map(row -> {
            FarmerAnalyticsResponse response = new FarmerAnalyticsResponse();
            response.setFarmerId(asLong(row[0]));
            response.setFarmerName((String) row[1]);
            response.setFarmerEmail((String) row[2]);
            response.setTotalProducts(asLong(row[3]));
            response.setTotalOrders(asLong(row[4]));
            response.setTotalRevenue(asBigDecimal(row[5]));
            return response;
        }).toList();
    }

    private AdminUserResponse toAdminUserResponse(User user, UserAccountStatus status) {
        AdminUserResponse response = new AdminUserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setVerified(user.isVerified());
        response.setStatus(status == null ? "ACTIVE" : status.getStatus());
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        response.setRoles(roles);
        return response;
    }

    private AdminProductResponse toAdminProductResponse(Product product, ProductModerationStatus status) {
        AdminProductResponse response = new AdminProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setFarmerId(product.getFarmer().getId());
        response.setFarmerName(product.getFarmer().getName());
        response.setCreatedAt(product.getCreatedAt());
        response.setStatus(status == null ? "PENDING" : status.getStatus());
        return response;
    }

    private AdminReportResponse toAdminReportResponse(Report report) {
        AdminReportResponse response = new AdminReportResponse();
        response.setId(report.getId());
        response.setTitle(report.getTitle());
        response.setDescription(report.getDescription());
        response.setStatus(report.getStatus());
        response.setCreatedAt(report.getCreatedAt());
        response.setResolvedAt(report.getResolvedAt());
        return response;
    }

    private String normalizeStatus(String status) {
        return status == null ? "PENDING" : status.trim().toUpperCase();
    }

    private long asLong(Object value) {
        if (value instanceof Long v) {
            return v;
        }
        if (value instanceof Integer v) {
            return v.longValue();
        }
        if (value instanceof BigInteger v) {
            return v.longValue();
        }
        return 0L;
    }

    private BigDecimal asBigDecimal(Object value) {
        if (value instanceof BigDecimal v) {
            return v;
        }
        if (value instanceof Double v) {
            return BigDecimal.valueOf(v);
        }
        if (value instanceof Long v) {
            return BigDecimal.valueOf(v);
        }
        return BigDecimal.ZERO;
    }
}
