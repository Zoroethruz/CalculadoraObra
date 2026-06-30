package com.exemplo.obra.repository;

import com.exemplo.obra.model.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

    List<Orcamento> findByNomeUsuario(String nomeUsuario);
}