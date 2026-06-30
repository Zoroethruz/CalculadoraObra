package com.exemplo.obra.service;

import com.exemplo.obra.dto.*;
import com.exemplo.obra.model.Orcamento;
import com.exemplo.obra.repository.OrcamentoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class MaterialService {

    private final OrcamentoRepository orcamentoRepository;

    public MaterialService(OrcamentoRepository orcamentoRepository) {
        this.orcamentoRepository = orcamentoRepository;
    }

    public ConcretoResponse calcularVolumeConcreto(ConcretoRequest request) {
        double volumeTotal = 0.0;

        for (ArestaRequest aresta : request.getArestas()) {
            double volume = aresta.getComprimento() * aresta.getEspessura() * request.getAlturaViga();
            volumeTotal += volume;
        }

        Orcamento orcamento = new Orcamento();
        orcamento.setNomeUsuario(request.getNomeUsuario());
        orcamento.setTipo("CONCRETO");
        orcamento.setResultado(volumeTotal);
        orcamentoRepository.save(orcamento);

        return new ConcretoResponse(
                BigDecimal.valueOf(volumeTotal).setScale(4, RoundingMode.HALF_UP),
                request.getArestas().size(),
                "Volume de concreto calculado com sucesso."
        );
    }

    public TijoloResponse calcularQuantidadeTijolos(TijoloRequest request) {
        double areaTotalParedes = 0.0;
        double areaAberturas = 0.0;

        for (ArestaRequest aresta : request.getArestas()) {
            double areaParede = aresta.getComprimento() * aresta.getAlturaParede();
            areaTotalParedes += areaParede;

            if (aresta.isPossuiPorta()) {
                areaAberturas += aresta.getLarguraPorta() * aresta.getAlturaPorta();
            }
            if (aresta.isPossuiJanela()) {
                areaAberturas += aresta.getLarguraJanela() * aresta.getAlturaJanela();
            }
        }

        double areaLiquida = areaTotalParedes - areaAberturas;
        double areaTijolo = request.getLarguraTijolo() * request.getAlturaTijolo();
        int quantidadeTijolos = (int) Math.ceil(areaLiquida / areaTijolo);
        int quantidadeComPerda = (int) Math.ceil(quantidadeTijolos * (1 + request.getPercentualPerda() / 100));

        Orcamento orcamento = new Orcamento();
        orcamento.setNomeUsuario(request.getNomeUsuario());
        orcamento.setTipo("TIJOLOS");
        orcamento.setResultado((double) quantidadeComPerda);
        orcamentoRepository.save(orcamento);

        return new TijoloResponse(
                BigDecimal.valueOf(areaTotalParedes).setScale(4, RoundingMode.HALF_UP),
                BigDecimal.valueOf(areaLiquida).setScale(4, RoundingMode.HALF_UP),
                BigDecimal.valueOf(areaAberturas).setScale(4, RoundingMode.HALF_UP),
                quantidadeTijolos,
                quantidadeComPerda,
                "Quantidade de tijolos calculada com sucesso."
        );
    }

    public List<Orcamento> buscarPorNome(String nomeUsuario) {
        return orcamentoRepository.findByNomeUsuario(nomeUsuario);
    }

    public List<Orcamento> buscarTodos() {
        return orcamentoRepository.findAll();
    }
}