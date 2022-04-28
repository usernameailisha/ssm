package com.wh.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_phonebook")
public class PhoneBook {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String phonenumber;

    private String telenumber;

    private String workaddress;

    private String homeaddress;

    private String image;

    private String remark;

    private String initial;

}