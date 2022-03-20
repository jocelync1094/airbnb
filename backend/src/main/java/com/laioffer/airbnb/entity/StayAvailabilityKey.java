package com.laioffer.airbnb.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class StayAvailabilityKey implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long stay_id;
    private LocalDate date;
    public StayAvailabilityKey() {}

    public StayAvailabilityKey(Long stay_id, LocalDate date) {
        this.stay_id = stay_id;
        this.date = date;
    }

    public Long getStay_id() {
        return stay_id;
    }

    public StayAvailabilityKey setStay_id(Long stay_id) {
        this.stay_id = stay_id;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StayAvailabilityKey that = (StayAvailabilityKey) o;
        return Objects.equals(stay_id, that.stay_id) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stay_id, date);
    }

    public LocalDate getDate() {
        return date;
    }

    public StayAvailabilityKey setDate(LocalDate date) {
        this.date = date;
        return this;
    }

}