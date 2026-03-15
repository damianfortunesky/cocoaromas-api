package com.cocoaromas.api.infrastructure.persistence.entity.user;

import com.cocoaromas.api.infrastructure.persistence.entity.UserEntity;
import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "user_addresses")
public class UserAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 80)
    private String label;

    @Column(name = "receiver_name", nullable = false, length = 140)
    private String receiverName;

    @Column(name = "receiver_phone", length = 40)
    private String receiverPhone;

    @Column(nullable = false, length = 160)
    private String street;

    @Column(name = "street_number", length = 20)
    private String streetNumber;

    @Column(length = 20)
    private String floor;

    @Column(length = 20)
    private String apartment;

    @Column(nullable = false, length = 120)
    private String city;

    @Column(length = 120)
    private String state;

    @Column(name = "postal_code", nullable = false, length = 20)
    private String postalCode;

    @Column(name = "country_code", nullable = false, length = 2)
    private String countryCode;

    @Column(length = 300)
    private String reference;

    @Column(name = "is_default_shipping", nullable = false)
    private boolean defaultShipping;

    @Column(name = "is_default_billing", nullable = false)
    private boolean defaultBilling;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public Long getId() { return id; }
    public UserEntity getUser() { return user; }
    public String getLabel() { return label; }
    public String getReceiverName() { return receiverName; }
    public String getReceiverPhone() { return receiverPhone; }
    public String getStreet() { return street; }
    public String getStreetNumber() { return streetNumber; }
    public String getFloor() { return floor; }
    public String getApartment() { return apartment; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getPostalCode() { return postalCode; }
    public String getCountryCode() { return countryCode; }
    public String getReference() { return reference; }
    public boolean isDefaultShipping() { return defaultShipping; }
    public boolean isDefaultBilling() { return defaultBilling; }
    public boolean isActive() { return active; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public void setUser(UserEntity user) { this.user = user; }
    public void setLabel(String label) { this.label = label; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public void setReceiverPhone(String receiverPhone) { this.receiverPhone = receiverPhone; }
    public void setStreet(String street) { this.street = street; }
    public void setStreetNumber(String streetNumber) { this.streetNumber = streetNumber; }
    public void setFloor(String floor) { this.floor = floor; }
    public void setApartment(String apartment) { this.apartment = apartment; }
    public void setCity(String city) { this.city = city; }
    public void setState(String state) { this.state = state; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public void setReference(String reference) { this.reference = reference; }
    public void setDefaultShipping(boolean defaultShipping) { this.defaultShipping = defaultShipping; }
    public void setDefaultBilling(boolean defaultBilling) { this.defaultBilling = defaultBilling; }
    public void setActive(boolean active) { this.active = active; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
}
