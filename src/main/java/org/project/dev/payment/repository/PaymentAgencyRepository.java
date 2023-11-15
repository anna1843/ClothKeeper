package org.project.dev.payment.repository;

import org.project.dev.payment.entity.PaymentAgencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentAgencyRepository extends JpaRepository<PaymentAgencyEntity,Long> {
}
