package org.project.dev.payment.repository;


import org.project.dev.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {


    @Modifying
    @Query(value = "UPDATE payment_tb " +
            "SET pg_token = :pgToken " +
            "WHERE payment_id = :paymentId", nativeQuery = true)
    void updatePgToken(@Param("paymentId") Long paymentId,@Param("pgToken") String pgToken);

    @Modifying
    @Query(value = "UPDATE payment_tb " +
            "SET is_succed = :isSucced " +
            "WHERE payment_id = :paymentId", nativeQuery = true)
    void updateIsSucced(@Param("paymentId") Long paymentId,@Param("isSucced") int isSucced);
}
