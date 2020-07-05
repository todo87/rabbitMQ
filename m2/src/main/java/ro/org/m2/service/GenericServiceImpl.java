package ro.org.m2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import ro.org.m2.model.GenericModel;
import ro.org.m2.repo.GenericRepo;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class GenericServiceImpl<T extends GenericModel, ID extends Serializable> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GenericRepo<T, ID> genericRepo;

    public GenericServiceImpl() {
    }

    public Optional<T> findOne(Specification<T> spec) {
        return this.genericRepo.findOne(spec);
    }

    public <S extends T> Optional<S> findOne(Example<S> example) {
        return this.genericRepo.findOne(example);
    }

    public List<T> findAll() {
        return this.genericRepo.findAll();
    }

    public Page<T> findAll(Pageable pageable) {
        return this.genericRepo.findAll(pageable);
    }

    public List<T> findAll(Sort sort) {
        return this.genericRepo.findAll(sort);
    }

    public List<T> findAllDesc() {
        return this.genericRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<T> findAll(Specification<T> spec) {
        return this.genericRepo.findAll(spec);
    }

    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return this.genericRepo.findAll(pageable);
    }

    public List<T> findAll(Specification<T> spec, Sort sort) {
        return this.genericRepo.findAll(sort);
    }

    public List<T> findAllById(Iterable<ID> ids) {
        return this.genericRepo.findAllById(ids);
    }

    public Optional<T> findById(ID id) {
        return this.genericRepo.findById(id);
    }

    public T findByIdAndGet(ID id) {
        Optional<T> optionalT = this.genericRepo.findById(id);
        return optionalT.isPresent() ? optionalT.get() : null;
    }

    public T save(T entity) {
        return this.genericRepo.save(entity);
    }

    public List<T> saveAll(Iterable<T> entities) {
        return this.genericRepo.saveAll(entities);
    }

    public T saveAndFlush(T entity) {
        return this.genericRepo.saveAndFlush(entity);
    }

    public void flush() {
        this.genericRepo.flush();
    }

    public void deleteAll() {
        this.genericRepo.deleteAll();
    }

    public void deleteById(ID id) {
        this.genericRepo.deleteById(id);
    }

    public void delete(T entity) {
        this.genericRepo.delete(entity);
    }

    public void deleteAll(Iterable<T> entities) {
        this.genericRepo.deleteAll(entities);
    }

    public void deleteInBatch(Iterable<T> entities) {
        this.genericRepo.deleteInBatch(entities);
    }

    public void deleteAllInBatch() {
        this.genericRepo.deleteAllInBatch();
    }

    public long count() {
        return genericRepo.count();
    }

    public long count(Specification<T> spec) {
        return this.genericRepo.count(spec);
    }

    public <S extends T> long count(Example<S> example) {
        return this.genericRepo.count(example);
    }

}
