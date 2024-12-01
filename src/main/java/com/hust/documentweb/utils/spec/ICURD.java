package com.hust.documentweb.utils.spec;

import java.util.List;
import java.util.Optional;

public interface ICURD<U, RQ, RP,ID>{
    List<RP> findAll();
    Optional<RP> findById(ID id);
    RP updateById(ID id, U dto);
    void deleteAllById(List<ID> ids);
}
