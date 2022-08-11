package be.vdab.fietsen.services;

import be.vdab.fietsen.exceptions.DocentNietGevondenException;
import be.vdab.fietsen.repositories.DocentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
class DefaultDocentService implements DocentService {
    private final DocentRepository docentRepository;

    public DefaultDocentService(DocentRepository docentRepository) {
        this.docentRepository = docentRepository;
    }

    @Override
    public void opslag(long id, BigDecimal percentage) {
        docentRepository.findById(id).orElseThrow(DocentNietGevondenException::new).opslag(percentage);
    }
}