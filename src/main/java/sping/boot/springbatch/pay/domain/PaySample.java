package sping.boot.springbatch.pay.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PaySample {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long amount;
    private boolean successStatus;

    public PaySample(Long amount, boolean successStatus) {
        this.amount = amount;
        this.successStatus = successStatus;
    }

    public void success() {
        this.successStatus = true;
    }
}
