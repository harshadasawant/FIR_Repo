package com.police.fir.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Component
@Entity
public class ExceptionDBConnect {

    @Id
    private  String fir_reg_num;
    private LocalDateTime exceptionTime;
    private String exceptionMessage;
    private String exceptionCause;
    @Column(columnDefinition = "longtext")
    private String exceptionStatckTrace;

}
