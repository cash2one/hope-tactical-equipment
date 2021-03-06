package org.hope6537.dto;

import java.util.List;

/**
 * 实体DTO
 * Created by hope6537 by Code Generator
 */
public class StudentDto extends BasicDto {

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 年龄
     */
    private String age;

    /**
     * 家长ID
     */
    private Integer parentId;

    /**
     * 班级ID
     */
    private Integer classesId;

    /**
     * 关联活动
     */
    private List<EventDto> relationEventList;

    public StudentDto() {

    }

    public StudentDto(String name, String sex, String age, Integer parentId, Integer classesId) {

        this.name = name;
        this.sex = sex;
        this.age = age;
        this.parentId = parentId;
        this.classesId = classesId;


    }

    public List<EventDto> getRelationEventList() {
        return relationEventList;
    }

    public void setRelationEventList(List<EventDto> relationEventList) {
        this.relationEventList = relationEventList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getClassesId() {
        return classesId;
    }

    public void setClassesId(Integer classesId) {
        this.classesId = classesId;
    }

}
    