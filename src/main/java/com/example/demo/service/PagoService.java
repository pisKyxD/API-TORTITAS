package com.example.demo.service;

import com.example.demo.model.Pago;
import com.example.demo.repository.PagoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public Pago findById(Long id) {
        return pagoRepository.findById(id).orElse(null);
    }

    public List<Pago> findAll() {
        return pagoRepository.findAll();
    }

    public Pago save(Pago pago) {
        return pagoRepository.save(pago);
    }

    public Pago update(Long id, Pago pago) {
        Pago pagoToUpdate = pagoRepository.findById(id).orElse(null);
        if (pagoToUpdate != null) {
            pagoToUpdate.setMonto(pago.getMonto());
            pagoToUpdate.setMetodo_pago(pago.getMetodo_pago());
            pagoToUpdate.setEstado_pago(pago.getEstado_pago());
            pagoToUpdate.setFecha_pago(pago.getFecha_pago());
            return pagoRepository.save(pagoToUpdate);
        }
        return null;
    }

    public Pago patch(Long id, Pago pago) {
        Pago pagoToPatch = pagoRepository.findById(id).orElse(null);
        if (pagoToPatch != null) {
            if (pago.getMonto() != null) pagoToPatch.setMonto(pago.getMonto());
            if (pago.getMetodo_pago() != null) pagoToPatch.setMetodo_pago(pago.getMetodo_pago());
            if (pago.getEstado_pago() != null) pagoToPatch.setEstado_pago(pago.getEstado_pago());
            if (pago.getFecha_pago() != null) pagoToPatch.setFecha_pago(pago.getFecha_pago());
            return pagoRepository.save(pagoToPatch);
        }
        return null;
    }

    public void delete(Long id) {
        pagoRepository.deleteById(id);
    }
}
