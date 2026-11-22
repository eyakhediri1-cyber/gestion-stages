
package tn.gestionstages.backend.dto;

import java.util.List;

public record AdminDashboardResponse(
    long totalUsers,
    long totalStudents,
    long totalCompanies,
    long totalOffers,
    long totalApplications,
    long disabledUsers,
    List<UserAdminResponse> recentUsers
) {}
