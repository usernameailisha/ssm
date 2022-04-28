package com.wh.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wh.demo.entity.PhoneBook;
import com.wh.demo.service.AdminService;
import com.wh.demo.service.PhoneBookService;
import com.wh.demo.utils.PinYinUtil;
import com.wh.demo.utils.QiniuUploadUtils;
import com.wh.demo.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/phone")
public class PhoneBookController {

    @Autowired
    PhoneBookService phoneBookService;

    @PostMapping("/getAll")
    public List<PhoneBook> getAll() {
        return phoneBookService.list();
    }

    //加载通讯录的列表数据
    @RequestMapping("/loadPhoneBook")
    public R loadPhoneBook() {
        Map<String, List<PhoneBook>> map = new HashMap<>();
        char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        //遍历数组，查询对应首字母吗的人
        for (char letter : letters) {
            //把char转换成string
            String initial = String.valueOf(letter);
            //根据initial字段，查询对应的人，放入集合
            QueryWrapper<PhoneBook> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("initial", initial);
            List<PhoneBook> list = phoneBookService.list(queryWrapper);
            //把首字母和对应的集合放入map
            map.put(initial, list);
        }
        //返回数据封装
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", map);
        return R.ok(resultMap);
    }


    //图片上传
    @RequestMapping("/uploadImage")
    //MultipartFile:  专门用来进行文件上传的类
    public R uploadImage(MultipartFile file) {
        System.out.println("file = " + file);
        //获取文件的名称
        String filename = file.getOriginalFilename();
        System.out.println("filename = " + filename);
        //调用七牛云上传的方法
        return QiniuUploadUtils.uploadImage(file);
    }

    //添加通讯录数据
    //前端：json => 后端：实体 使用 @RequestBody
    @PostMapping("/add")
    public R addPhoneBook(@RequestBody PhoneBook phoneBook) {
        try {
            //中文转换为拼音
            phoneBook.setInitial(PinYinUtil.getPinYin(phoneBook.getName()).substring(0, 1).toUpperCase());
            boolean save = phoneBookService.save(phoneBook);
            if (save) {
                return R.ok("添加成功！");
            } else {
                return R.error("添加失败！");
            }
        } catch (Exception e) {
            return R.error("服务器错误！");
        }
    }

    //验证手机号的唯一性
    @PostMapping("/checkPhone")
    public R checkPhone(@RequestBody PhoneBook phoneBook) {
        try {
            QueryWrapper<PhoneBook> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phonenumber", phoneBook.getPhonenumber());
            PhoneBook pb = phoneBookService.getOne(queryWrapper);
            if (pb == null) {
                return R.ok();
            } else {
                return R.error();
            }
        } catch (Exception e) {
            return R.error("服务器错误！");
        }

    }

    //验证座机号的唯一性
    @PostMapping("/checkTele")
    public R checkTele(@RequestBody PhoneBook phoneBook) {
        try {
            QueryWrapper<PhoneBook> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("telenumber", phoneBook.getTelenumber());
            PhoneBook pb = phoneBookService.getOne(queryWrapper);
            if (pb == null) {
                return R.ok();
            } else {
                return R.error();
            }
        } catch (Exception e) {
            return R.error("服务器错误！");
        }

    }

    //根据id查询数据，回显到页面
    @RequestMapping("/loadById")
    public R loadById(Integer id){
        try {
            Map<String, Object> map = new HashMap<>();
            PhoneBook byId = phoneBookService.getById(id);
            System.out.println("byId = " + byId);
            if (byId != null){
                map.put("data",byId);
                return R.ok(map);
            }else {
                return R.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("服务器错误");
        }
    }

    //根据id修改数据
    @RequestMapping("/revise")
    public R revise(@RequestBody PhoneBook phoneBook){
        //中文转换为拼音
        phoneBook.setInitial(PinYinUtil.getPinYin(phoneBook.getName()).substring(0, 1).toUpperCase());
        boolean b = phoneBookService.updateById(phoneBook);
        if (b){
            return R.ok("修改成功");
        }else {
            return R.error();
        }
    }

    //根据id修改数据
    @RequestMapping("del/{id}")
    public R del(@PathVariable("id") Integer id){
        boolean b = phoneBookService.removeById(id);
        if (b){
            return R.ok();
        }else {
            return R.error();
        }
    }
}
