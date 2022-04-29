package com.example.demo.service.impl;

import com.example.demo.constant.StudentBusinessConstant;
import com.example.demo.model.pojo.Student;
import com.example.demo.model.vo.StudentQueryDto;
import com.example.demo.service.StudentOperationService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author yzh
 */
@Service
public class StudentOperationServiceImpl implements StudentOperationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public int insertStudent(Student student) {
        try {
            student.setTimer(LocalDateTime.now());
            mongoTemplate.insert(student);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void batchInsertStudent(List<String> studentName) {
        try {
            studentName.forEach(name -> {
                Student student = new Student();
                student.setId(UUID.randomUUID().toString().replaceAll("-",""));
                student.setUsername(name);
                student.setTimer(LocalDateTime.now());
                mongoTemplate.insert(student);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Long countStudentNums(StudentQueryDto queryDto) {
        if (!dealWithDto(queryDto)) {
            return 0L;
        }
        Long nums = mongoTemplate.count(new Query(), Student.class);
        return nums;
    }

    @Override
    public List<Student> queryStudentOnPage(StudentQueryDto queryDto) {
        if (!dealWithDto(queryDto)) {
            return new ArrayList<>();
        }
        Query query = new Query();
        query.with(Sort.by("timer").descending());
        query.skip((Integer.parseInt(queryDto.getPageNum()) - 1) * 10 )
                .limit(Integer.parseInt(queryDto.getPageSize()));
        List<Student> reList = mongoTemplate.find(query, Student.class);
        return reList;
    }

    private boolean dealWithDto(StudentQueryDto queryDto) {
        if (Objects.isNull(queryDto)) {
            return false;
        }
        if (StringUtils.isEmpty(queryDto.getPageNum())) {
            queryDto.setPageNum(StudentBusinessConstant.PAGE_NUM);
        }
        if (StringUtils.isEmpty(queryDto.getPageSize())) {
            queryDto.setPageSize(StudentBusinessConstant.PAGE_SIZE);
        }
        return true;
    }

    @Override
    public int updateStudent(Student student) {
        //通过query根据id查询出对应对象，通过update对象进行修改
        Query query = new Query(Criteria.where("_id").is(student.getId()));
        Update update = new Update().set("username", student.getUsername());
        try {
            mongoTemplate.updateFirst(query, update, Student.class);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public int removeStudent(Long id) {
        Query query = new Query(Criteria.where("_id").is(id));
        try {
            mongoTemplate.remove(query,Student.class);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public List<Student> findAll() {
        return mongoTemplate.findAll(Student.class);
    }

    @Override
    public Student findOne(Student student) {
        Query query = new Query(Criteria.where("_id").is(student.getId()));
        Student one = mongoTemplate.findOne(query, Student.class);
        return one;
    }

    @Override
    public List<Student> findlike(Student student) {
        Pattern pattern = Pattern.compile("^.*" + student.getUsername().trim() + ".*$", Pattern.CASE_INSENSITIVE);
        Query query = new Query(Criteria.where("username").regex(pattern));
        List<Student> studentList = mongoTemplate.find(query, Student.class);
        return studentList;
    }

    @Override
    public List<Student> findmore(Student student) {
        Query query = new Query(Criteria.where("username").is(student.getUsername()));
        List<Student> students = mongoTemplate.find(query, Student.class);
        return students;
    }


    /**
     * 版本问题：MongoDB 和 SpringBoot 这两个版本有问题
     * 查看源码可直接修改
     */
    @Override
    public List<Student> findtime(Student student) {
        Query query = new Query();
//        List<String> sortProperties = new ArrayList<>();
//        sortProperties.add("timer");
        query.with(Sort.by("timer"));
        List<Student> students = mongoTemplate.find(query, Student.class);
        return students;
    }

    @Override
    public List<Student> findtimeByPage(Student student) {
        Query query = new Query();
//        query.with(new Sort(Sort.Direction.DESC, "timer"));
        query.with(Sort.by("timer"));
        query.skip(0).limit(3);
        List<Student> students = mongoTemplate.find(query, Student.class);
        return students;

    }
}
