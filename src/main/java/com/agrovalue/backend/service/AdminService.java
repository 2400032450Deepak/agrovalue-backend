package com.agrovalue.backend.service;

import com.agrovalue.backend.dto.*;

import java.util.List;

public interface AdminService {
    AdminDashboardResponse getDashboard();
    List<AdminUserResponse> getAllUsers();
    AdminUserResponse getUserById(Long id);
    AdminUserResponse updateUserStatus(Long id, String status);
    void deleteUser(Long id);
    List<AdminProductResponse> getAllProducts();
    AdminProductResponse updateProductStatus(Long id, String status);
    void deleteProduct(Long id);
    List<AdminTransactionResponse> getAllTransactions();
    List<AdminReportResponse> getAllReports();
    AdminReportResponse resolveReport(Long id);
    List<FarmerAnalyticsResponse> getFarmerAnalytics();
}
