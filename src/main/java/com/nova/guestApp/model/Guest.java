package com.nova.guestApp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nova.guestApp.enums.CardStatus;
import com.nova.guestApp.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "guests")
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String staffName;

    @Column(nullable = false)
    private String tagId;

    @Column(nullable = false)
    private String purposeOfVisit;

    private String email;

    private String comment;

    private String checkedOutBy;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CardStatus cardStatus;

    private String checkInTime;


   private String checkOutTime;
}
