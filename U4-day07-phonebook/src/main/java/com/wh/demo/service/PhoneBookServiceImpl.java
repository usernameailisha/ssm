package com.wh.demo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wh.demo.dao.PhoneBookMapper;
import com.wh.demo.entity.PhoneBook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PhoneBookServiceImpl extends ServiceImpl<PhoneBookMapper, PhoneBook> implements PhoneBookService {
}
