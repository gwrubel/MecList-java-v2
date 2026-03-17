package com.meclist.usecase.checklist;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.meclist.interfaces.StorageService;

public class StorageCompensationContext {

    private final List<String> uploadsNovos = new ArrayList<>();
    private final List<String> pathsAntigosParaDeletarAposCommit = new ArrayList<>();

    public void registrarUploadNovo(String path) {
        uploadsNovos.add(path);
    }

    public void registrarPathAntigoParaDeletarAposCommit(String path) {
        pathsAntigosParaDeletarAposCommit.add(path);
    }

    public void registrarPathsAntigosParaDeletarAposCommit(List<String> paths) {
        pathsAntigosParaDeletarAposCommit.addAll(paths);
    }

    public void registrarDeleteAposCommit(StorageService storageService) {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            deletarPathsAntigos(storageService);
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                deletarPathsAntigos(storageService);
            }
        });
    }

    public void compensarUploadsNovos(StorageService storageService) {
        for (String path : uploadsNovos) {
            try {
                storageService.delete(path);
            } catch (Exception ignored) {
            }
        }
    }

    private void deletarPathsAntigos(StorageService storageService) {
        for (String path : pathsAntigosParaDeletarAposCommit) {
            try {
                storageService.delete(path);
            } catch (Exception ignored) {
            }
        }
    }
}