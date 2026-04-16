package com.meclist.dto.cliente;

import java.util.List;

public record DashboardClienteResponse(
    String nomeCliente,
    List<VeiculoResumo> veiculos,
    List<ChecklistCardResumo> checklists
) {
    
}
