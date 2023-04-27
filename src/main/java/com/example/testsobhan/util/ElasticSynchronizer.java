package com.example.testsobhan.util;

import com.example.testsobhan.document.ProductModel;
import com.example.testsobhan.entity.Product;
import com.example.testsobhan.repository.ProductRepository;
import com.example.testsobhan.repository.elastic.ProductElasticRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ElasticSynchronizer {

    private final ProductRepository productRepository;
    private final ProductElasticRepository productElasticRepository;
    private final ModelMapper modelMapper;

    private static final Logger LOG = LoggerFactory.getLogger(ElasticSynchronizer.class);

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void sync() {
        LOG.info("Start Syncing - {}", LocalDateTime.now());
        this.syncProducts();
        LOG.info(" End Syncing - {}", LocalDateTime.now());
    }

    private void syncProducts() {
        Specification<Product> userSpecification = (root, criteriaQuery, criteriaBuilder) ->
                getModificationDatePredicate(criteriaBuilder, root);
        List<Product> productList;
        if (productElasticRepository.count() == 0) {
            productList = productRepository.findAll();
        } else {
            productList = productRepository.findAll(userSpecification);
        }
        for (Product product : productList) {
            LOG.info("Syncing User - {}", product.getId());
            productElasticRepository.save(modelMapper.map(product, ProductModel.class));
        }
    }

    private static Predicate getModificationDatePredicate(CriteriaBuilder cb, Root<?> root) {
        Expression<Timestamp> currentTime;
        currentTime = cb.currentTimestamp();
        Expression<Timestamp> currentTimeMinus = cb.literal(new Timestamp(System.currentTimeMillis() -
                (Constants.INTERVAL_IN_MILLISECOND)));
        return cb.between(root.<Date>get(Constants.MODIFICATION_DATE),
                currentTimeMinus,
                currentTime
        );
    }
}
