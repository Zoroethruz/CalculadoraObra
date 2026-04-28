package com.exemplo.obra.service;

import com.exemplo.obra.dto.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MaterialService {

    public ConcretoResponse calcularVolumeConcreto(ConcretoRequest request) {
        double volumeTotal = 0.0;

        for (ArestaRequest aresta : request.getArestas()) {
            // Volume da viga baldrame = comprimento x espessura x alturaViga
            double volume = aresta.getComprimento() * aresta.getEspessura() * request.getAlturaViga();
            volumeTotal += volume;
        }

        return new ConcretoResponse(
                BigDecimal.valueOf(volumeTotal).setScale(4, java.math.RoundingMode.HALF_UP),
                request.getArestas().size(),
                "Volume de concreto calculado com sucesso."
        );
    }

    public TijoloResponse calcularQuantidadeTijolos(TijoloRequest request) {
        double areaTotalParedes = 0.0;
        double areaAberturas = 0.0;

        for (ArestaRequest aresta : request.getArestas()) {
            // Área total da parede = comprimento x alturaParede
            double areaParede = aresta.getComprimento() * aresta.getAlturaParede();
            areaTotalParedes += areaParede;

            // Desconta porta e janela
            if (aresta.isPossuiPorta()) {
                areaAberturas += aresta.getLarguraPorta() * aresta.getAlturaPorta();
            }
            if (aresta.isPossuiJanela()) {
                areaAberturas += aresta.getLarguraJanela() * aresta.getAlturaJanela();
            }
        }

        double areaLiquida = areaTotalParedes - areaAberturas;

        // Área de um tijolo
        double areaTijolo = request.getLarguraTijolo() * request.getAlturaTijolo();

        int quantidadeTijolos = (int) Math.ceil(areaLiquida / areaTijolo);
        int quantidadeComPerda = (int) Math.ceil(quantidadeTijolos * (1 + request.getPercentualPerda() / 100));

        return new TijoloResponse(
                BigDecimal.valueOf(areaTotalParedes).setScale(4, java.math.RoundingMode.HALF_UP),
                BigDecimal.valueOf(areaLiquida).setScale(4, java.math.RoundingMode.HALF_UP),
                BigDecimal.valueOf(areaAberturas).setScale(4, java.math.RoundingMode.HALF_UP),
                quantidadeTijolos,
                quantidadeComPerda,
                "Quantidade de tijolos calculada com sucesso."
        );
    }
}
