package com.meclist.exception;

import org.springframework.http.HttpStatus;

public class ItemNaoPertenceAoChecklistException  extends CustomException {

    public ItemNaoPertenceAoChecklistException(Long itemChecklistId) {
        super(HttpStatus.BAD_REQUEST, "ITEM_NAO_PERTENCE_AO_CHECKLIST",
              "O item com ID " + itemChecklistId + " não pertence a este checklist.");
    }
    
}