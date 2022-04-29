package com.example.demo.service;


import com.example.demo.model.pojo.Student;
import com.example.demo.model.vo.StudentQueryDto;

import java.util.List;

public interface StudentOperationService {

    int insertStudent(Student student);

    void batchInsertStudent(List<String> studentName);

    /**
     * 分页查询
     * @param queryDto
     * @return
     */
    Long countStudentNums(StudentQueryDto queryDto);
    List<Student> queryStudentOnPage(StudentQueryDto queryDto);

    int updateStudent(Student student);

    int removeStudent(Long id);

    List<Student> findAll();

    Student findOne(Student student);

    List<Student> findlike(Student student);

    List<Student> findmore(Student student);

    List<Student> findtime(Student student);

    List<Student> findtimeByPage(Student student);


}
