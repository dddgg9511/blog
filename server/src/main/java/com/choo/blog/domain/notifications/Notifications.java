package com.choo.blog.domain.notifications;

import com.choo.blog.domain.users.Users;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;

@Entity
public class Notifications {
    @Id @GeneratedValue
    @Column(name = "notification_id")
    private Long id;

    @Enumerated
    private NotificationType type;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Users sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Users receiver;
}
