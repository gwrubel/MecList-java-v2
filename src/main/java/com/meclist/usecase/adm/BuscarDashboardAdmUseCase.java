package com.meclist.usecase.adm;

import org.springframework.stereotype.Service;

import com.meclist.dto.admin.DashboardAdmResponse;
import com.meclist.interfaces.AdminDashboardGateway;

@Service
public class BuscarDashboardAdmUseCase {

    private final AdminDashboardGateway adminDashboardGateway;

    public BuscarDashboardAdmUseCase(AdminDashboardGateway adminDashboardGateway) {
        this.adminDashboardGateway = adminDashboardGateway;
    }

    public DashboardAdmResponse executar() {
        return adminDashboardGateway.buscarDadosDashboard();
    }
}
