package com.example.demo.controller;

import com.example.demo.constant.RetCode;
import com.example.demo.model.pojo.Student;
import com.example.demo.model.vo.Result;
import com.example.demo.model.vo.StudentQueryDto;
import com.example.demo.service.StudentOperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.List;

/**
 * @author yzh
 */
@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentOperationService studentOperationService;

    @PostMapping("/add")
    @ResponseBody
    public Result addStudent(Student student){
        // 保证新增的student的id不重复 没有就新增
        if (Objects.isNull(student.getId())) {
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            student.setId(uuid);
        }
        int reCode = studentOperationService.insertStudent(student);
        if (reCode == 1) {
            return new Result(RetCode.SUCCESS.getCode(), reCode);
        }
        return new Result(RetCode.FAILURE.getCode(), 0);
    }

    @PostMapping("/batchAdd")
    public Result batchAddStudent(String names) throws Exception {
        if (StringUtils.containsWhitespace(names)) {
            throw new Exception(RetCode.BLANK_STUDENT_NAME.getMsg());
        }
        List<String> studentNames = Arrays.asList(names.split(","));
        studentOperationService.batchInsertStudent(studentNames);
        return new Result(RetCode.SUCCESS.getCode(), 1);
    }


    @PostMapping("/queryAll")
    public Result queryAllStudent(){
        List<Student> reList = studentOperationService.findAll();
        log.info("查询所有学生成功");
        
        return new Result(RetCode.SUCCESS.getCode(), reList);
    }

    /**
     * 分页查询接口
     * @param queryDto
     * @return
     */
    @PostMapping("/queryPage")
    public Result queryStudentOnPage(StudentQueryDto queryDto){
        Long studentNums = studentOperationService.countStudentNums(queryDto);
        if (studentNums <= 0L) {
            return new Result(RetCode.NULL_STUDENT.getCode(), new Object());
        }
        List<Student> reList = studentOperationService.queryStudentOnPage(queryDto);
        return new Result(RetCode.SUCCESS.getCode(),
                            reList,
                            queryDto.getPageNum(),
                            queryDto.getPageSize(),
                            studentNums);
    }




//    /** 查询玩家账户信息列表 */
//    @PostMapping("/list")
//    @ResponseBody
//    public TableDataInfo list(User user) {
//        Query query = MOngodbstartPage();
//        ViewResponse viewResponse = userService.selectUserList(user, query);
//        return MongogetDataTable(viewResponse.getLists(), viewResponse.getSize());
//    }
//
//
//    /** 设置请求分页数据 得到query */
//    protected Query MOngodbstartPage() {
//        PageDomain pageDomain = TableSupport.buildPageRequest();
//        Integer pageNum = pageDomain.getPageNum();
//        Integer pageSize = pageDomain.getPageSize();
//        Query query = new Query().skip((pageNum - 1) * pageSize).limit(pageSize);
//        return query;
//    }
//    //后续查询将query代入接口中，如需其他查询条件  就query.方法  这要既有条件，又有分页了
//        userService.selectUserList(user, query);

}
