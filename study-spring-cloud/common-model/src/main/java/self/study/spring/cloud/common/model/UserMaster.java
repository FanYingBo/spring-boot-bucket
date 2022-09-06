package self.study.spring.cloud.common.model;

import self.study.spring.cloud.common.dto.resp.UserDTO;

public class UserMaster extends BaseEntity<UserMaster, UserDTO>{

    private Long userId;

    private String userName;

    private String email;

    private String address;
    private Integer age;
    private String jobType;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    @Override
    public UserMaster fromDTO(UserDTO baseDTO) {
        this.setUserId(baseDTO.getUserId());
        this.setUserName(baseDTO.getUserName());
        this.setAddress(baseDTO.getAddress());
        this.setAge(baseDTO.getAge());
        this.setEmail(baseDTO.getEmail());
        this.setJobType(baseDTO.getJobType());
        return this;
    }

}
