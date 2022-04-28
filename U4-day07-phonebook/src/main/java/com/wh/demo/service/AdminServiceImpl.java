package com.wh.demo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wh.demo.dao.AdminMapper;
import com.wh.demo.entity.Admin;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
}
