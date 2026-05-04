package com.meclist.interfaces;

import com.meclist.dto.checklist.administrativo.PropostaPdfSnapshot;

public interface PropostaPdfGateway {
    byte[] gerar(PropostaPdfSnapshot snapshot);
}