package com.viki.projects.saas_ai_editor.entity;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Subscription {

    private Long id;
    private User user;
    private Plan plan;
    private SubscriptionStatus status;
    private String stripCustomerId;
    private String stripeSubscriptionId;
    private Instant currentPeriodStart;
    private Instant currentPeriodEnd;
    private Boolean cancelAtPeriodEnd = false;
    private Instant createdAt;
    private Instant updatedAt;
}
